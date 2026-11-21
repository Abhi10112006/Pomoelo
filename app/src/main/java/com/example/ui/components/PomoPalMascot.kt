package com.example.ui.components

import android.content.Context
import android.opengl.Matrix
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import android.view.ViewConfiguration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ui.theme.LocalAppTheme
import com.google.android.filament.Camera
import com.google.android.filament.ColorGrading
import com.google.android.filament.Engine
import com.google.android.filament.Entity
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import com.google.android.filament.Renderer
import com.google.android.filament.Scene
import com.google.android.filament.SwapChain
import com.google.android.filament.ToneMapper
import com.google.android.filament.View as FilamentView
import com.google.android.filament.Viewport
import com.google.android.filament.android.DisplayHelper
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.Animator
import com.google.android.filament.gltfio.AssetLoader
import com.google.android.filament.gltfio.FilamentAsset
import com.google.android.filament.gltfio.ResourceLoader
import com.google.android.filament.gltfio.UbershaderProvider
import com.google.android.filament.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.exp

enum class MascotExpression {
    IDLE, FOCUS, RELAX, HAPPY, EXCITED, CURIOUS
}

enum class MascotRenderMode {
    FULL,
    THROTTLED_30FPS,
    PAUSED
}

/**
 * Non-static CompositionLocal governing Judy's 3D Filament cadence so overlay/drag
 * mode transitions never force full-subtree recomposition or swapchain recreation.
 */
val LocalMascotRenderMode = compositionLocalOf { MascotRenderMode.FULL }

private const val THROTTLED_30FPS_INTERVAL_NS = 33_333_333L
private const val HOST_TAG = "JudyFilamentHost"

private object FilamentRuntime {
    val isAvailable: Boolean by lazy {
        try {
            Utils.init()
            true
        } catch (_: Throwable) {
            false
        }
    }
}

private suspend fun loadJudyGlbBuffer(context: Context): ByteBuffer? = withContext(Dispatchers.IO) {
    try {
        context.assets.openFd("models/judy.glb").use { afd ->
            val size = afd.length.toInt()
            val buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())
            afd.createInputStream().use { input ->
                val channel = input.channel
                while (buffer.hasRemaining()) {
                    if (channel.read(buffer) == -1) break
                }
            }
            buffer.flip()
            buffer
        }
    } catch (_: Throwable) {
        try {
            val bytes = context.assets.open("models/judy.glb").use { it.readBytes() }
            ByteBuffer.allocateDirect(bytes.size)
                .order(ByteOrder.nativeOrder())
                .put(bytes)
                .apply { flip() }
        } catch (_: Throwable) {
            null
        }
    }
}

private class MorphTargetBinding(
    val entity: Int,
    val slotToMorphIndex: IntArray,
    morphCount: Int
) {
    val scratchWeights = FloatArray(morphCount)
}

/**
 * Custom transparent-swapchain Filament PBR host for 3D Judy:
 *  - Analytical pre-convolved 7-level studio specular IBL (eye catchlights + soft wrap)
 *  - ACES filmic ColorGrading (mobile-safe MEDIUM LUT), FXAA + Temporal dithering
 *  - SSAO disabled over CONFIG_TRANSPARENT TextureView to prevent dark silhouette edge halos
 *  - Name-resolved 30-bone skeleton & 8 facial morph target bindings with missing-asset diagnostics
 */
private class JudyFilamentHost(
    private val textureView: TextureView
) {
    val engine: Engine = Engine.create()
    val renderer: Renderer = engine.createRenderer()
    val scene: Scene = engine.createScene()
    val view: FilamentView = engine.createView()

    @Entity
    private val cameraEntity: Int = EntityManager.get().create()
    val camera: Camera = engine.createCamera(cameraEntity)

    private val materialProvider = UbershaderProvider(engine)
    private val assetLoader = AssetLoader(engine, materialProvider, EntityManager.get())
    private val resourceLoader = ResourceLoader(engine, true)

    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
        isOpaque = false
    }
    private val displayHelper = DisplayHelper(textureView.context)

    private var swapChain: SwapChain? = null
    private var asset: FilamentAsset? = null
    private var animator: Animator? = null
    private var sourceDataReleased = false

    // Name-resolved 30-bone handles and their bind-pose local matrices
    private var jointEntities: IntArray = IntArray(0)
    private var jointRestMatrices: Array<FloatArray> = emptyArray()
    private var morphBindings: List<MorphTargetBinding> = emptyList()
    private val readyRenderables = IntArray(128)
    private val rootMatrix = FloatArray(16)

    // Smoothly interpolated rim-light RGB so mood lighting transitions glide with zero snap
    private var rimR = 0.86f
    private var rimG = 0.92f
    private var rimB = 1.0f

    // Full Filament PBR 4-light dark-OLED studio rig (Key + Fill + Strong Mood Rim + Cool Counter-Rim) + ACES
    @Entity
    private val keyLight: Int = EntityManager.get().create()
    @Entity
    private val fillLight: Int = EntityManager.get().create()
    @Entity
    private val rimLight: Int = EntityManager.get().create()
    @Entity
    private val counterRimLight: Int = EntityManager.get().create()
    private val studioIbl: JudyStudioIbl = JudyStudioIbl.create(engine)
    private val colorGrading: ColorGrading? = runCatching {
        ColorGrading.Builder()
            .toneMapper(ToneMapper.ACES())
            .quality(ColorGrading.QualityLevel.MEDIUM)
            .contrast(1.06f)
            .saturation(1.06f)
            .build(engine)
    }.getOrNull()

    @Volatile
    var isDestroyed = false
        private set

    val isAssetUploaded: Boolean
        get() = !isDestroyed && asset != null && (sourceDataReleased || runCatching { resourceLoader.asyncGetLoadProgress() >= 1f }.getOrDefault(false))

    init {
        textureView.isOpaque = false

        // True translucent rendering pipeline (alpha = 0 background) + mobile-safe Filament PBR post-processing
        scene.skybox = null
        view.scene = scene
        view.camera = camera
        view.blendMode = FilamentView.BlendMode.TRANSLUCENT
        view.setShadowingEnabled(false)
        colorGrading?.let { view.colorGrading = it }
        view.antiAliasing = FilamentView.AntiAliasing.FXAA
        view.dithering = FilamentView.Dithering.TEMPORAL
        // Explicitly disable SSAO on transparent TextureView to prevent dark silhouette edge halos over radial aura
        view.ambientOcclusionOptions = view.ambientOcclusionOptions.apply {
            enabled = false
        }

        renderer.clearOptions = renderer.clearOptions.apply {
            clear = true
            discard = true
            clearColor[0] = 0f
            clearColor[1] = 0f
            clearColor[2] = 0f
            clearColor[3] = 0f
        }

        // Closer hero full-body framing (soles planted flush at y = 0.00m on contact shadow + full hop headroom)
        camera.setExposure(16.0f, 1.0f / 125.0f, 100.0f)
        camera.lookAt(
            0.0, 0.76, 2.40,
            0.0, 0.735, 0.0,
            0.0, 1.0, 0.0
        )

        // 128x128 dual-catchlight studio IBL + 4-point dark-OLED lighting rig
        scene.indirectLight = studioIbl.indirectLight

        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 0.97f, 0.94f)
            .intensity(84_000f)
            .direction(-0.26f, -0.46f, -0.85f)
            .castShadows(false)
            .build(engine, keyLight)
        scene.addEntity(keyLight)

        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(0.92f, 0.96f, 1.0f)
            .intensity(32_000f)
            .direction(0.46f, -0.14f, -0.88f)
            .castShadows(false)
            .build(engine, fillLight)
        scene.addEntity(fillLight)

        // Strong mood-tinted primary rim backlight carving crisp separation on ears, shoulders & navy pajamas
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(0.84f, 0.92f, 1.0f)
            .intensity(132_000f)
            .direction(0.62f, -0.22f, 0.75f)
            .castShadows(false)
            .build(engine, rimLight)
        scene.addEntity(rimLight)

        // Crisp cool-white counter-rim backlight ensuring the opposite silhouette edge pops off dark OLED UI
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(0.90f, 0.96f, 1.0f)
            .intensity(84_000f)
            .direction(-0.66f, -0.18f, 0.73f)
            .castShadows(false)
            .build(engine, counterRimLight)
        scene.addEntity(counterRimLight)

        uiHelper.renderCallback = object : UiHelper.RendererCallback {
            override fun onNativeWindowChanged(surface: Surface) {
                if (isDestroyed) return
                runCatching {
                    swapChain?.let { engine.destroySwapChain(it) }
                    // CRITICAL: Pass uiHelper.swapChainFlags (CONFIG_TRANSPARENT = 1L) so the surface has an alpha channel!
                    swapChain = engine.createSwapChain(surface, uiHelper.swapChainFlags)
                    textureView.display?.let { displayHelper.attach(renderer, it) }
                }.onFailure { err ->
                    Log.w(HOST_TAG, "SwapChain creation warning: ${err.message}")
                    swapChain = null
                }
            }

            override fun onDetachedFromSurface() {
                runCatching { displayHelper.detach() }
                swapChain?.let { current ->
                    swapChain = null
                    if (!isDestroyed) {
                        runCatching {
                            engine.destroySwapChain(current)
                            engine.flushAndWait()
                        }
                    }
                }
            }

            override fun onResized(width: Int, height: Int) {
                if (isDestroyed || width <= 0 || height <= 0) return
                runCatching {
                    view.viewport = Viewport(0, 0, width, height)
                    val aspect = width.toDouble() / height.toDouble()
                    camera.setProjection(34.5, aspect, 0.1, 20.0, Camera.Fov.VERTICAL)
                }
            }
        }
        uiHelper.attachTo(textureView)
    }

    fun loadGlb(buffer: ByteBuffer): Boolean {
        if (isDestroyed) return false
        destroyAsset()

        val created = assetLoader.createAsset(buffer) ?: return false
        asset = created
        sourceDataReleased = false

        // Begin asynchronous texture & vertex buffer upload (do NOT call releaseSourceData yet!)
        resourceLoader.asyncBeginLoad(created)
        animator = created.instance.animator

        // Resolve all 30 bones by name first (falling back to skin joint index) and warn on any mismatch
        val tm = engine.transformManager
        val skinJoints = if (created.instance.skinCount > 0) {
            created.instance.getJointsAt(0)
        } else {
            IntArray(0)
        }
        val missingBones = mutableListOf<String>()
        val resolvedJoints = IntArray(JudyBones.COUNT) { idx ->
            val boneName = JudyBones.NAMES[idx]
            val byName = created.getFirstEntityByName(boneName)
            when {
                byName != 0 -> byName
                idx < skinJoints.size -> {
                    missingBones += "$boneName(fallback->idx:$idx)"
                    skinJoints[idx]
                }
                else -> {
                    missingBones += "$boneName(missing)"
                    0
                }
            }
        }
        if (missingBones.isNotEmpty()) {
            Log.w(HOST_TAG, "Judy rig bone resolution warnings: ${missingBones.joinToString(", ")}")
        }
        jointEntities = resolvedJoints
        jointRestMatrices = Array(JudyBones.COUNT) { idx ->
            val mat = FloatArray(16)
            Matrix.setIdentityM(mat, 0)
            val entity = resolvedJoints[idx]
            if (entity != 0) {
                val tInst = tm.getInstance(entity)
                if (tInst != 0) tm.getTransform(tInst, mat)
            }
            mat
        }

        // Resolve facial morph targets by name on each morph-capable renderable
        val rm = engine.renderableManager
        val bindings = mutableListOf<MorphTargetBinding>()
        val missingMorphSet = linkedSetOf<String>()
        for (entity in created.renderableEntities) {
            val rInst = rm.getInstance(entity)
            if (rInst == 0) continue
            val morphCount = rm.getMorphTargetCount(rInst)
            if (morphCount <= 0) continue

            val targetNames = runCatching { created.getMorphTargetNames(entity) }.getOrNull().orEmpty()
            val nameToIndex = HashMap<String, Int>(targetNames.size)
            for (mIdx in targetNames.indices) {
                nameToIndex[targetNames[mIdx]] = mIdx
            }

            val slotMap = IntArray(JudyMorphs.COUNT) { slot ->
                val expectedName = JudyMorphs.NAMES[slot]
                val resolved = nameToIndex[expectedName]
                when {
                    resolved != null && resolved < morphCount -> resolved
                    targetNames.isEmpty() && slot < morphCount -> slot
                    else -> {
                        missingMorphSet += expectedName
                        -1
                    }
                }
            }
            bindings += MorphTargetBinding(entity, slotMap, morphCount)
        }
        if (bindings.isEmpty()) {
            Log.w(HOST_TAG, "No morph-capable renderables found in judy.glb")
        } else if (missingMorphSet.isNotEmpty()) {
            Log.w(HOST_TAG, "Missing expected Judy morph targets: ${missingMorphSet.joinToString(", ")}")
        }
        morphBindings = bindings

        populateReadyRenderables()
        return true
    }

    private fun populateReadyRenderables() {
        val currentAsset = asset ?: return
        var count = currentAsset.popRenderables(readyRenderables)
        while (count > 0) {
            scene.addEntities(readyRenderables.copyOf(count))
            count = currentAsset.popRenderables(readyRenderables)
        }
        if (!sourceDataReleased && resourceLoader.asyncGetLoadProgress() >= 1f) {
            sourceDataReleased = true
            runCatching { currentAsset.releaseSourceData() }
        }
    }

    fun stepRimColor(expression: MascotExpression, dt: Float) {
        if (isDestroyed) return
        val targetR: Float
        val targetG: Float
        val targetB: Float
        when (expression) {
            MascotExpression.EXCITED -> {
                targetR = 1.0f; targetG = 0.78f; targetB = 0.56f
            }
            MascotExpression.HAPPY -> {
                targetR = 1.0f; targetG = 0.86f; targetB = 0.70f
            }
            MascotExpression.FOCUS -> {
                targetR = 0.58f; targetG = 0.86f; targetB = 1.0f
            }
            MascotExpression.RELAX -> {
                targetR = 0.66f; targetG = 0.98f; targetB = 0.84f
            }
            MascotExpression.CURIOUS -> {
                targetR = 0.82f; targetG = 0.76f; targetB = 1.0f
            }
            MascotExpression.IDLE -> {
                targetR = 0.82f; targetG = 0.92f; targetB = 1.0f
            }
        }
        val blend = 1f - exp(-5.5f * dt.coerceIn(0.001f, 0.033f))
        rimR += (targetR - rimR) * blend
        rimG += (targetG - rimG) * blend
        rimB += (targetB - rimB) * blend

        val lm = engine.lightManager
        val rimInst = lm.getInstance(rimLight)
        if (rimInst != 0) {
            lm.setColor(rimInst, rimR, rimG, rimB)
        }
        // Subtle mood blend on counter-rim (mostly crisp cool-white for clean contrast)
        val counterInst = lm.getInstance(counterRimLight)
        if (counterInst != 0) {
            lm.setColor(
                counterInst,
                0.76f + rimR * 0.24f,
                0.80f + rimG * 0.20f,
                0.84f + rimB * 0.16f
            )
        }
    }

    fun applyPhysicsAndRender(frameTimeNanos: Long, physics: JudyPhysicsEngine): Boolean {
        if (isDestroyed || !uiHelper.isReadyToRender) return false
        val currentSwapChain = swapChain ?: return false

        // Pump async texture/buffer uploads and add ready renderables to scene
        runCatching {
            resourceLoader.asyncUpdateLoad()
            populateReadyRenderables()
        }

        val currentAsset = asset
        if (currentAsset != null) {
            val tm = engine.transformManager
            val rm = engine.renderableManager

            // Batch all 30 joint local transforms + grounded horizontal turntable root atomically
            tm.openLocalTransformTransaction()
            val boneCount = minOf(jointEntities.size, JudyBones.COUNT)
            for (i in 0 until boneCount) {
                val entity = jointEntities[i]
                if (entity == 0) continue
                val tInst = tm.getInstance(entity)
                if (tInst != 0) {
                    val mat = physics.composeBoneMatrix(jointRestMatrices[i], i)
                    tm.setTransform(tInst, mat)
                }
            }

            // Grounded horizontal-only 360° turntable transform keeping soles flat on floor plane (y = 0)
            val rootInst = tm.getInstance(currentAsset.root)
            if (rootInst != 0) {
                Matrix.setIdentityM(rootMatrix, 0)
                Matrix.translateM(rootMatrix, 0, 0f, physics.rootOffsetY, 0f)
                Matrix.rotateM(rootMatrix, 0, physics.rootYawDeg, 0f, 1f, 0f)
                Matrix.scaleM(rootMatrix, 0, physics.rootScaleX, physics.rootScaleY, physics.rootScaleZ)
                tm.setTransform(rootInst, rootMatrix)
            }
            tm.commitLocalTransformTransaction()

            // Compute and upload all 30 world skin matrices to GPU skinning buffers
            animator?.updateBoneMatrices()

            // Push all 8 name-resolved facial morph weights
            val weights = physics.morphWeights
            for (binding in morphBindings) {
                val rInst = rm.getInstance(binding.entity)
                if (rInst != 0) {
                    val out = binding.scratchWeights
                    out.fill(0f)
                    val slotMap = binding.slotToMorphIndex
                    for (slot in 0 until JudyMorphs.COUNT) {
                        val targetIdx = slotMap[slot]
                        if (targetIdx in out.indices) {
                            out[targetIdx] = weights[slot]
                        }
                    }
                    rm.setMorphWeights(rInst, out, 0)
                }
            }
        }

        if (renderer.beginFrame(currentSwapChain, frameTimeNanos)) {
            renderer.render(view)
            renderer.endFrame()
            return true
        }
        return false
    }

    private fun destroyAsset() {
        asset?.let { current ->
            runCatching { resourceLoader.asyncCancelLoad() }
            runCatching { scene.removeEntities(current.entities) }
            runCatching { assetLoader.destroyAsset(current) }
        }
        asset = null
        animator = null
        jointEntities = IntArray(0)
        jointRestMatrices = emptyArray()
        morphBindings = emptyList()
        sourceDataReleased = false
    }

    fun destroy() {
        if (isDestroyed) return
        isDestroyed = true

        runCatching { uiHelper.detach() }
        runCatching { displayHelper.detach() }
        destroyAsset()
        runCatching { resourceLoader.evictResourceData() }
        runCatching { resourceLoader.destroy() }
        runCatching { assetLoader.destroy() }
        runCatching { materialProvider.destroyMaterials() }
        runCatching { materialProvider.destroy() }

        runCatching {
            scene.removeEntity(keyLight)
            scene.removeEntity(fillLight)
            scene.removeEntity(rimLight)
            scene.removeEntity(counterRimLight)
            scene.indirectLight = null
            view.colorGrading = null
            engine.destroyEntity(keyLight)
            engine.destroyEntity(fillLight)
            engine.destroyEntity(rimLight)
            engine.destroyEntity(counterRimLight)
            EntityManager.get().destroy(keyLight)
            EntityManager.get().destroy(fillLight)
            EntityManager.get().destroy(rimLight)
            EntityManager.get().destroy(counterRimLight)
            studioIbl.destroy(engine)
            colorGrading?.let { engine.destroyColorGrading(it) }
        }

        runCatching {
            swapChain?.let { engine.destroySwapChain(it) }
            swapChain = null
            engine.destroyRenderer(renderer)
            engine.destroyView(view)
            engine.destroyScene(scene)
            engine.destroyCameraComponent(cameraEntity)
            EntityManager.get().destroy(cameraEntity)
            // Ensure pending GPU uploads & teardown commands finish before destroying the native Engine
            runCatching { engine.flushAndWait() }
            engine.destroy()
        }
    }
}

private fun normalizeAngleDeg(angle: Float): Float {
    if (!angle.isFinite()) return 0f
    var wrapped = angle % 360f
    if (wrapped > 180f) wrapped -= 360f
    if (wrapped <= -180f) wrapped += 360f
    return wrapped
}

@Composable
fun PomoPalMascot(
    expression: MascotExpression,
    modifier: Modifier = Modifier,
    isBreathing: Boolean = true,
    isSpeaking: Boolean = false,
    interactive: Boolean = true,
    isActive: Boolean = true,
    onTap: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentTheme = LocalAppTheme.current

    if (!FilamentRuntime.isAvailable) {
        JudyAvatarBadge(modifier = modifier, isBreathing = isBreathing)
        return
    }

    var isModelReady by remember { mutableStateOf(false) }
    var isVisualReady by remember { mutableStateOf(false) }
    var loadFailed by remember { mutableStateOf(false) }
    var isLifecycleResumed by remember { mutableStateOf(true) }
    var isViewReleased by remember { mutableStateOf(false) }
    var shadowJumpScale by remember { mutableFloatStateOf(1f) }
    var shadowShiftXRatio by remember { mutableFloatStateOf(0f) }

    val currentExpression by rememberUpdatedState(expression)
    val currentBreathing by rememberUpdatedState(isBreathing)
    val currentSpeaking by rememberUpdatedState(isSpeaking)
    val currentInteractive by rememberUpdatedState(interactive)
    val currentOnTap by rememberUpdatedState(onTap)
    val shouldRender by rememberUpdatedState(isActive && isLifecycleResumed && !isViewReleased)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> isLifecycleResumed = true
                Lifecycle.Event.ON_PAUSE -> isLifecycleResumed = false
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            isViewReleased = true
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var hostHolder by remember { mutableStateOf<JudyFilamentHost?>(null) }
    var textureViewHolder by remember { mutableStateOf<TextureView?>(null) }
    val physicsEngine = remember { JudyPhysicsEngine() }

    // Persistent continuous clock & horizontal 360° turntable state (survives PAUSED/ON_PAUSE without jumping!)
    val orbitState = remember {
        object {
            var accumulatedSec = 0f
            var tapElapsedSec = 99f
            var readyTimerSec = 0f
            var userYawDeg = 0f
            var yawVelocity = 0f
            var isDragging = false
            var returnToFrontFast = false
            var idleSinceInteractionSec = 0f
            var hasInteracted = false
        }
    }

    // Load compressed 30-bone PBR Judy GLB off the main thread once the transparent host is ready
    LaunchedEffect(hostHolder) {
        val host = hostHolder ?: return@LaunchedEffect
        val glbBuffer = loadJudyGlbBuffer(context)
        if (isViewReleased || host.isDestroyed) return@LaunchedEffect
        if (glbBuffer == null) {
            loadFailed = true
            return@LaunchedEffect
        }
        val attached = textureViewHolder?.isAttachedToWindow ?: true
        if (!attached || isViewReleased || host.isDestroyed) return@LaunchedEffect

        try {
            val loaded = host.loadGlb(glbBuffer)
            if (loaded) {
                // Prime first posed frame immediately so Judy never flashes in bind T-pose
                physicsEngine.step(
                    dt = 0.016f,
                    elapsedSec = orbitState.accumulatedSec,
                    expression = currentExpression,
                    isBreathing = currentBreathing,
                    isSpeaking = currentSpeaking,
                    tapElapsedSec = orbitState.tapElapsedSec,
                    userYawDeg = orbitState.userYawDeg,
                    userYawVelocityDeg = 0f
                )
                isModelReady = true
            } else {
                loadFailed = true
            }
        } catch (_: Throwable) {
            loadFailed = true
        }
    }

    // Drive Choreographer real-time physics, skeleton skinning, facial morphs & rendering.
    // PAUSED unregisters Choreographer completely (zero 120Hz wakeups).
    // Accumulated clock advances smoothly across pauses/resumes with zero jump or snap.
    // THROTTLED_30FPS gates rendering via nanosecond interval (refresh-rate independent 60/90/120/144Hz)
    // without restarting the effect or recreating the TextureView swapchain.
    val renderMode = LocalMascotRenderMode.current
    val currentRenderMode by rememberUpdatedState(renderMode)
    val isLoopActive = shouldRender && renderMode != MascotRenderMode.PAUSED

    DisposableEffect(hostHolder, isLoopActive, isModelReady) {
        val host = hostHolder
        if (host == null || !isLoopActive || !isModelReady) {
            return@DisposableEffect onDispose {}
        }

        val choreographer = Choreographer.getInstance()
        var lastFrameNs = 0L
        var lastRenderNanos = 0L

        val callback = object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if (isViewReleased || host.isDestroyed || textureViewHolder?.isAttachedToWindow == false) return
                choreographer.postFrameCallback(this)

                val mode = currentRenderMode
                if (mode == MascotRenderMode.PAUSED) return

                if (mode == MascotRenderMode.THROTTLED_30FPS &&
                    lastRenderNanos != 0L &&
                    (frameTimeNanos - lastRenderNanos) < THROTTLED_30FPS_INTERVAL_NS
                ) {
                    return
                }
                lastRenderNanos = frameTimeNanos

                val dt = if (lastFrameNs == 0L) {
                    0.016f
                } else {
                    ((frameTimeNanos - lastFrameNs) / 1_000_000_000f).coerceIn(0.001f, 0.033f)
                }
                lastFrameNs = frameTimeNanos

                orbitState.accumulatedSec += dt
                if (orbitState.tapElapsedSec < 10f) {
                    orbitState.tapElapsedSec += dt
                }

                val expr = currentExpression
                host.stepRimColor(expr, dt)

                // Update horizontal 360° spin inertia & smooth shortest-arc return to front
                if (!orbitState.isDragging) {
                    orbitState.userYawDeg += orbitState.yawVelocity * dt
                    orbitState.yawVelocity *= exp(-5.2f * dt)
                    if (abs(orbitState.yawVelocity) < 0.25f) {
                        orbitState.yawVelocity = 0f
                    }
                    orbitState.idleSinceInteractionSec += dt

                    if (orbitState.returnToFrontFast) {
                        orbitState.userYawDeg = normalizeAngleDeg(orbitState.userYawDeg)
                        val fastBlend = 1f - exp(-7.5f * dt)
                        orbitState.userYawDeg += (0f - orbitState.userYawDeg) * fastBlend
                        if (abs(orbitState.userYawDeg) < 0.15f) {
                            orbitState.userYawDeg = 0f
                            orbitState.returnToFrontFast = false
                        }
                    } else if (orbitState.hasInteracted && orbitState.idleSinceInteractionSec > 2.6f) {
                        // Normalize to (-180..180] so she always takes the shortest smooth arc back to front
                        orbitState.userYawDeg = normalizeAngleDeg(orbitState.userYawDeg)
                        val returnBlend = 1f - exp(-2.8f * dt)
                        orbitState.userYawDeg += (0f - orbitState.userYawDeg) * returnBlend
                        if (abs(orbitState.userYawDeg) < 0.1f && orbitState.yawVelocity == 0f) {
                            orbitState.userYawDeg = 0f
                            orbitState.hasInteracted = false
                        }
                    }
                }

                physicsEngine.step(
                    dt = dt,
                    elapsedSec = orbitState.accumulatedSec,
                    expression = expr,
                    isBreathing = currentBreathing,
                    isSpeaking = currentSpeaking,
                    tapElapsedSec = orbitState.tapElapsedSec,
                    userYawDeg = orbitState.userYawDeg,
                    userYawVelocityDeg = orbitState.yawVelocity
                )

                // Ground shadow dynamically shrinks & softens as Judy leaps and subtly tracks contrapposto weight shift
                val heightAboveGround = (physicsEngine.rootOffsetY - 0.009f).coerceAtLeast(0f)
                val airScale = (1f - (heightAboveGround / 0.34f)).coerceIn(0.50f, 1.02f)
                if (abs(airScale - shadowJumpScale) > 0.008f) {
                    shadowJumpScale = airScale
                }
                val shiftRatio = (physicsEngine.groundedHipShiftX * 1.8f).coerceIn(-0.08f, 0.08f)
                if (abs(shiftRatio - shadowShiftXRatio) > 0.004f) {
                    shadowShiftXRatio = shiftRatio
                }

                val rendered = runCatching {
                    host.applyPhysicsAndRender(frameTimeNanos, physicsEngine)
                }.getOrDefault(false)

                if (!isVisualReady && rendered) {
                    orbitState.readyTimerSec += dt
                    if (host.isAssetUploaded || orbitState.readyTimerSec > 0.65f) {
                        isVisualReady = true
                    }
                }
            }
        }

        choreographer.postFrameCallback(callback)
        onDispose {
            choreographer.removeFrameCallback(callback)
        }
    }

    // Smooth crossfade reveal once GPU textures & first posed frame are ready (eliminates T-pose pop-in)
    val modelRevealAlpha by animateFloatAsState(
        targetValue = if (isVisualReady) 1f else 0f,
        animationSpec = tween(durationMillis = 240, easing = FastOutSlowInEasing),
        label = "judyRevealAlpha"
    )

    Box(
        modifier = modifier.testTag("judy_3d_mascot"),
        contentAlignment = Alignment.Center
    ) {
        if (loadFailed) {
            JudyAvatarBadge(modifier = Modifier.fillMaxSize(), isBreathing = isBreathing)
        } else {
            // Multi-layered anchored floor contact shadow directly beneath Judy's paws:
            // subtle stage floor sheen (visible on dark OLED) + wide penumbra + dense contact umbra
            val shadowTint = when (expression) {
                MascotExpression.EXCITED, MascotExpression.HAPPY -> Color(0xFFFFB588)
                MascotExpression.FOCUS -> Color(0xFF82C8FF)
                MascotExpression.RELAX -> Color(0xFF8CEBC0)
                MascotExpression.CURIOUS -> Color(0xFFC4B5FD)
                MascotExpression.IDLE -> currentTheme.primary
            }
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.58f)
                    .height(26.dp)
                    .graphicsLayer {
                        translationX = size.width * shadowShiftXRatio
                        scaleX = shadowJumpScale
                        scaleY = shadowJumpScale
                        alpha = (0.35f + 0.65f * shadowJumpScale) * modelRevealAlpha
                    }
            ) {
                // 1. Subtle mood-tinted stage floor bounce ellipse so the floor plane reads clearly on dark OLED
                if (currentTheme.isDark) {
                    drawOval(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                shadowTint.copy(alpha = 0.16f),
                                shadowTint.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    )
                }
                // 2. Wide soft ambient occlusion penumbra
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = if (currentTheme.isDark) 0.62f else 0.24f),
                            Color.Black.copy(alpha = if (currentTheme.isDark) 0.22f else 0.08f),
                            Color.Transparent
                        )
                    ),
                    topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.12f, size.height * 0.18f),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.76f, size.height * 0.64f)
                )
                // 3. Dense grounded paw contact umbra anchoring soles firmly to the floor
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = if (currentTheme.isDark) 0.88f else 0.38f),
                            Color.Black.copy(alpha = if (currentTheme.isDark) 0.35f else 0.12f),
                            Color.Transparent
                        )
                    ),
                    topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.24f, size.height * 0.32f),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.52f, size.height * 0.38f)
                )
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = modelRevealAlpha },
                factory = { ctx ->
                    val touchSlopPx = ViewConfiguration.get(ctx).scaledTouchSlop.toFloat().coerceAtLeast(18f)
                    TextureView(ctx).apply {
                        isOpaque = false
                        val host = runCatching { JudyFilamentHost(this) }.getOrElse { err ->
                            Log.e(HOST_TAG, "Failed to initialize JudyFilamentHost: ${err.message}", err)
                            loadFailed = true
                            null
                        }

                        if (host != null) {
                            addOnAttachStateChangeListener(object : android.view.View.OnAttachStateChangeListener {
                                override fun onViewAttachedToWindow(v: android.view.View) = Unit
                                override fun onViewDetachedFromWindow(v: android.view.View) {
                                    isViewReleased = true
                                    host.destroy()
                                    if (hostHolder === host) hostHolder = null
                                    if (textureViewHolder === v) textureViewHolder = null
                                }
                            })

                            var activePointerId = MotionEvent.INVALID_POINTER_ID
                            var downX = 0f
                            var downY = 0f
                            var lastX = 0f
                            var downTime = 0L
                            var lastMoveTime = 0L
                            var exceededSlop = false

                            setOnTouchListener { v, event ->
                                if (!currentInteractive) {
                                    return@setOnTouchListener false
                                }
                                when (event.actionMasked) {
                                    MotionEvent.ACTION_DOWN -> {
                                        activePointerId = event.getPointerId(0)
                                        downX = event.x
                                        downY = event.y
                                        lastX = event.x
                                        downTime = event.eventTime
                                        lastMoveTime = event.eventTime
                                        exceededSlop = false
                                        orbitState.isDragging = true
                                        orbitState.returnToFrontFast = false
                                        orbitState.yawVelocity = 0f
                                        orbitState.idleSinceInteractionSec = 0f
                                        orbitState.hasInteracted = true
                                        v.parent?.requestDisallowInterceptTouchEvent(true)
                                    }
                                    MotionEvent.ACTION_MOVE -> {
                                        val idx = event.findPointerIndex(activePointerId)
                                        if (idx >= 0) {
                                            val curX = event.getX(idx)
                                            val curY = event.getY(idx)
                                            if (abs(curX - downX) > touchSlopPx || abs(curY - downY) > touchSlopPx) {
                                                exceededSlop = true
                                            }
                                            val dx = curX - lastX
                                            val dtMs = (event.eventTime - lastMoveTime).coerceAtLeast(1L)
                                            val degDelta = dx * 0.50f
                                            orbitState.userYawDeg += degDelta
                                            val instantVel = (degDelta / (dtMs / 1000f)).coerceIn(-360f, 360f)
                                            // Low-pass filter fling velocity for silky momentum release
                                            orbitState.yawVelocity = orbitState.yawVelocity * 0.45f + instantVel * 0.55f
                                            orbitState.idleSinceInteractionSec = 0f
                                            lastX = curX
                                            lastMoveTime = event.eventTime
                                        }
                                    }
                                    MotionEvent.ACTION_POINTER_UP -> {
                                        // Smoothly hand off active drag finger if multi-touch occurs (prevents spin spike!)
                                        val upIdx = event.actionIndex
                                        if (event.getPointerId(upIdx) == activePointerId) {
                                            val newIdx = if (upIdx == 0) 1 else 0
                                            if (newIdx < event.pointerCount) {
                                                activePointerId = event.getPointerId(newIdx)
                                                lastX = event.getX(newIdx)
                                                lastMoveTime = event.eventTime
                                            }
                                        }
                                    }
                                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                        val idx = event.findPointerIndex(activePointerId)
                                        val endX = if (idx >= 0) event.getX(idx) else event.x
                                        val endY = if (idx >= 0) event.getY(idx) else event.y
                                        activePointerId = MotionEvent.INVALID_POINTER_ID
                                        orbitState.isDragging = false
                                        orbitState.idleSinceInteractionSec = 0f
                                        v.parent?.requestDisallowInterceptTouchEvent(false)

                                        if (event.actionMasked == MotionEvent.ACTION_UP) {
                                            val totalDx = abs(endX - downX)
                                            val totalDy = abs(endY - downY)
                                            if (!exceededSlop &&
                                                totalDx < touchSlopPx &&
                                                totalDy < touchSlopPx &&
                                                (event.eventTime - downTime) < 300L
                                            ) {
                                                orbitState.tapElapsedSec = 0f
                                                orbitState.yawVelocity = 0f
                                                orbitState.returnToFrontFast = true
                                                physicsEngine.triggerTapReaction(currentExpression)
                                                currentOnTap?.invoke()
                                            }
                                        }
                                    }
                                }
                                true
                            }

                            textureViewHolder = this
                            hostHolder = host
                        }
                    }
                },
                onRelease = { view ->
                    isViewReleased = true
                    hostHolder?.destroy()
                    hostHolder = null
                    if (textureViewHolder === view) textureViewHolder = null
                }
            )

            if (modelRevealAlpha < 0.99f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = 1f - modelRevealAlpha },
                    contentAlignment = Alignment.Center
                ) {
                    JudyAvatarBadge(modifier = Modifier.fillMaxSize(0.72f), isBreathing = true)
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = currentTheme.primary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
