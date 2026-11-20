package com.example.ui.components

import android.content.Context
import android.opengl.Matrix
import android.view.Choreographer
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
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
import com.google.android.filament.Engine
import com.google.android.filament.Entity
import com.google.android.filament.EntityManager
import com.google.android.filament.IndirectLight
import com.google.android.filament.LightManager
import com.google.android.filament.Renderer
import com.google.android.filament.Scene
import com.google.android.filament.SwapChain
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

/**
 * Custom transparent-swapchain Filament host for 3D Judy.
 * Replaces Filament's ModelViewer (which hardcodes opaque 1-arg createSwapChain) so Judy renders
 * cleanly with full alpha transparency directly on top of Compose UI with zero black backdrop.
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

    // Exact 21 joint handles and their rest local matrices
    private var jointEntities: IntArray = IntArray(0)
    private var jointRestMatrices: Array<FloatArray> = emptyArray()
    private var morphRenderables: IntArray = IntArray(0)
    private val readyRenderables = IntArray(128)
    private val rootMatrix = FloatArray(16)

    // Studio lighting entities
    @Entity
    private val keyLight: Int = EntityManager.get().create()
    @Entity
    private val fillLight: Int = EntityManager.get().create()
    @Entity
    private val rimLight: Int = EntityManager.get().create()
    private val indirectLight: IndirectLight

    @Volatile
    var isDestroyed = false
        private set

    init {
        textureView.isOpaque = false

        // True translucent rendering pipeline (alpha = 0 background)
        scene.skybox = null
        view.scene = scene
        view.camera = camera
        view.blendMode = FilamentView.BlendMode.TRANSLUCENT
        view.setShadowingEnabled(false)

        renderer.clearOptions = renderer.clearOptions.apply {
            clear = true
            discard = true
            clearColor[0] = 0f
            clearColor[1] = 0f
            clearColor[2] = 0f
            clearColor[3] = 0f
        }

        // Balanced framing for full-body Judy (0.0m..1.44m) with generous headroom for bunny jumps
        camera.setExposure(16.0f, 1.0f / 125.0f, 100.0f)
        camera.lookAt(
            0.0, 0.78, 2.72,
            0.0, 0.78, 0.0,
            0.0, 1.0, 0.0
        )

        // Studio 3-point lighting + warm spherical harmonics ambient light
        val sh = FloatArray(27).apply {
            this[0] = 1.15f; this[1] = 1.10f; this[2] = 1.12f
            this[6] = 0.36f; this[7] = 0.35f; this[8] = 0.40f
        }
        indirectLight = IndirectLight.Builder()
            .irradiance(3, sh)
            .intensity(48_000f)
            .build(engine)
        scene.indirectLight = indirectLight

        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 0.98f, 0.95f)
            .intensity(96_000f)
            .direction(-0.28f, -0.52f, -0.82f)
            .castShadows(false)
            .build(engine, keyLight)
        scene.addEntity(keyLight)

        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(0.92f, 0.95f, 1.0f)
            .intensity(34_000f)
            .direction(0.45f, -0.15f, -0.88f)
            .castShadows(false)
            .build(engine, fillLight)
        scene.addEntity(fillLight)

        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(0.86f, 0.92f, 1.0f)
            .intensity(56_000f)
            .direction(0.58f, -0.22f, 0.58f)
            .castShadows(false)
            .build(engine, rimLight)
        scene.addEntity(rimLight)

        uiHelper.renderCallback = object : UiHelper.RendererCallback {
            override fun onNativeWindowChanged(surface: Surface) {
                if (isDestroyed) return
                swapChain?.let { engine.destroySwapChain(it) }
                // CRITICAL: Pass uiHelper.swapChainFlags (CONFIG_TRANSPARENT = 1L) so the surface has an alpha channel!
                swapChain = engine.createSwapChain(surface, uiHelper.swapChainFlags)
                textureView.display?.let { displayHelper.attach(renderer, it) }
            }

            override fun onDetachedFromSurface() {
                displayHelper.detach()
                swapChain?.let {
                    if (!isDestroyed) {
                        engine.destroySwapChain(it)
                        engine.flushAndWait()
                    }
                    swapChain = null
                }
            }

            override fun onResized(width: Int, height: Int) {
                if (isDestroyed || width <= 0 || height <= 0) return
                view.viewport = Viewport(0, 0, width, height)
                val aspect = width.toDouble() / height.toDouble()
                camera.setProjection(36.0, aspect, 0.1, 20.0, Camera.Fov.VERTICAL)
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

        // Capture exact 21 joint entities & their bind-pose local matrices directly from skin 0
        val tm = engine.transformManager
        val joints = if (created.instance.skinCount > 0) {
            created.instance.getJointsAt(0)
        } else {
            IntArray(0)
        }
        jointEntities = joints
        jointRestMatrices = Array(joints.size) { idx ->
            val mat = FloatArray(16)
            Matrix.setIdentityM(mat, 0)
            val tInst = tm.getInstance(joints[idx])
            if (tInst != 0) tm.getTransform(tInst, mat)
            mat
        }

        // Capture all renderables with facial morph targets (6 targets: blink_l, blink_r, mouth_open, smile, brow_up, brow_focus)
        val rm = engine.renderableManager
        val morphList = mutableListOf<Int>()
        for (entity in created.renderableEntities) {
            val rInst = rm.getInstance(entity)
            if (rInst != 0 && rm.getMorphTargetCount(rInst) >= JudyMorphs.COUNT) {
                morphList += entity
            }
        }
        morphRenderables = morphList.toIntArray()

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

    fun updateRimColor(expression: MascotExpression) {
        if (isDestroyed) return
        val lm = engine.lightManager
        val rimInst = lm.getInstance(rimLight)
        if (rimInst == 0) return
        when (expression) {
            MascotExpression.EXCITED, MascotExpression.HAPPY -> lm.setColor(rimInst, 1.0f, 0.87f, 0.78f)
            MascotExpression.FOCUS -> lm.setColor(rimInst, 0.72f, 0.90f, 1.0f)
            MascotExpression.RELAX -> lm.setColor(rimInst, 0.78f, 0.97f, 0.88f)
            MascotExpression.CURIOUS -> lm.setColor(rimInst, 0.88f, 0.84f, 1.0f)
            MascotExpression.IDLE -> lm.setColor(rimInst, 0.86f, 0.92f, 1.0f)
        }
    }

    fun applyPhysicsAndRender(frameTimeNanos: Long, physics: JudyPhysicsEngine) {
        if (isDestroyed || !uiHelper.isReadyToRender) return
        val currentSwapChain = swapChain ?: return

        // Pump async texture/buffer uploads and add ready renderables to scene
        resourceLoader.asyncUpdateLoad()
        populateReadyRenderables()

        val currentAsset = asset
        if (currentAsset != null) {
            val tm = engine.transformManager
            val rm = engine.renderableManager

            // Batch all 21 joint local transforms + root transform atomically inside a TransformManager transaction
            tm.openLocalTransformTransaction()
            val boneCount = minOf(jointEntities.size, JudyBones.COUNT)
            for (i in 0 until boneCount) {
                val tInst = tm.getInstance(jointEntities[i])
                if (tInst != 0) {
                    val mat = physics.composeBoneMatrix(jointRestMatrices[i], i)
                    tm.setTransform(tInst, mat)
                }
            }

            // Root transform: orbit cleanly around center (y = 0.72) while keeping squash/stretch grounded at feet (y = 0)
            val rootInst = tm.getInstance(currentAsset.root)
            if (rootInst != 0) {
                Matrix.setIdentityM(rootMatrix, 0)
                Matrix.translateM(rootMatrix, 0, 0f, physics.rootOffsetY + 0.72f, 0f)
                Matrix.rotateM(rootMatrix, 0, physics.rootYawDeg, 0f, 1f, 0f)
                Matrix.rotateM(rootMatrix, 0, physics.rootPitchDeg, 1f, 0f, 0f)
                Matrix.translateM(rootMatrix, 0, 0f, -0.72f, 0f)
                Matrix.scaleM(rootMatrix, 0, physics.rootScaleX, physics.rootScaleY, physics.rootScaleZ)
                tm.setTransform(rootInst, rootMatrix)
            }
            tm.commitLocalTransformTransaction()

            // Compute and upload all 21 world skin matrices to GPU skinning buffers
            animator?.updateBoneMatrices()

            // Push all 6 live facial morph weights (blink_l, blink_r, mouth_open, smile, brow_up, brow_focus)
            val weights = physics.morphWeights
            for (entity in morphRenderables) {
                val rInst = rm.getInstance(entity)
                if (rInst != 0) {
                    rm.setMorphWeights(rInst, weights, 0)
                }
            }
        }

        if (renderer.beginFrame(currentSwapChain, frameTimeNanos)) {
            renderer.render(view)
            renderer.endFrame()
        }
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
        morphRenderables = IntArray(0)
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
            scene.indirectLight = null
            engine.destroyEntity(keyLight)
            engine.destroyEntity(fillLight)
            engine.destroyEntity(rimLight)
            EntityManager.get().destroy(keyLight)
            EntityManager.get().destroy(fillLight)
            EntityManager.get().destroy(rimLight)
            engine.destroyIndirectLight(indirectLight)
        }

        runCatching {
            swapChain?.let { engine.destroySwapChain(it) }
            swapChain = null
            engine.destroyRenderer(renderer)
            engine.destroyView(view)
            engine.destroyScene(scene)
            engine.destroyCameraComponent(cameraEntity)
            EntityManager.get().destroy(cameraEntity)
            engine.destroy()
        }
    }
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
    var loadFailed by remember { mutableStateOf(false) }
    var isLifecycleResumed by remember { mutableStateOf(true) }
    var isViewReleased by remember { mutableStateOf(false) }
    var shadowJumpScale by remember { mutableFloatStateOf(1f) }

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

    // Smooth 3D touch orbit & tap impulse state
    val orbitState = remember {
        object {
            var userYawDeg = 0f
            var userPitchDeg = 0f
            var yawVelocity = 0f
            var isDragging = false
            var lastInteractionNs = 0L
            var tapImpulseStartNs = 0L
        }
    }

    // Load Judy 21-bone GLB off the main thread once the custom transparent host is ready
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
    // Resuming resets lastRenderNanos = 0L & lastFrameNs = 0L for an immediate, jump-free first frame.
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
        val startTimeNs = System.nanoTime()
        var lastFrameNs = 0L
        var lastRenderNanos = 0L
        var lastRimExpr: MascotExpression? = null

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
                    ((frameTimeNanos - lastFrameNs) / 1_000_000_000f).coerceIn(0.001f, 0.05f)
                }
                lastFrameNs = frameTimeNanos
                val elapsedSec = (frameTimeNanos - startTimeNs) / 1_000_000_000f

                val expr = currentExpression
                if (expr != lastRimExpr) {
                    lastRimExpr = expr
                    host.updateRimColor(expr)
                }

                // Update drag inertia & smooth auto-return to front after 2.6s idle
                if (!orbitState.isDragging) {
                    orbitState.userYawDeg += orbitState.yawVelocity * dt
                    orbitState.yawVelocity *= exp(-5.0f * dt)
                    val idleSec = (frameTimeNanos - orbitState.lastInteractionNs) / 1_000_000_000f
                    if (orbitState.lastInteractionNs > 0L && idleSec > 2.6f) {
                        val returnBlend = 1f - exp(-2.6f * dt)
                        orbitState.userYawDeg += (0f - orbitState.userYawDeg) * returnBlend
                        orbitState.userPitchDeg += (0f - orbitState.userPitchDeg) * returnBlend
                    }
                }

                val tapElapsedSec = if (orbitState.tapImpulseStartNs > 0L) {
                    (frameTimeNanos - orbitState.tapImpulseStartNs) / 1_000_000_000f
                } else 99f

                physicsEngine.step(
                    dt = dt,
                    elapsedSec = elapsedSec,
                    expression = expr,
                    isBreathing = currentBreathing,
                    isSpeaking = currentSpeaking,
                    tapElapsedSec = tapElapsedSec,
                    userYawDeg = orbitState.userYawDeg,
                    userPitchDeg = orbitState.userPitchDeg,
                    userYawVelocityDeg = orbitState.yawVelocity
                )

                // Ground shadow dynamically shrinks & softens as Judy leaps into the air
                val airScale = (1f - (physicsEngine.rootOffsetY / 0.45f)).coerceIn(0.55f, 1.05f)
                if (abs(airScale - shadowJumpScale) > 0.01f) {
                    shadowJumpScale = airScale
                }

                try {
                    host.applyPhysicsAndRender(frameTimeNanos, physicsEngine)
                } catch (_: Throwable) {
                }
            }
        }

        choreographer.postFrameCallback(callback)
        onDispose {
            choreographer.removeFrameCallback(callback)
        }
    }

    Box(
        modifier = modifier.testTag("judy_3d_mascot"),
        contentAlignment = Alignment.Center
    ) {
        if (loadFailed) {
            JudyAvatarBadge(modifier = Modifier.fillMaxSize(), isBreathing = isBreathing)
        } else {
            // Soft jump-reactive ground contact shadow directly on the app surface
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.46f)
                    .height(14.dp)
                    .graphicsLayer {
                        scaleX = shadowJumpScale
                        scaleY = shadowJumpScale
                        alpha = shadowJumpScale
                    }
            ) {
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = if (currentTheme.isDark) 0.32f else 0.16f),
                            Color.Transparent
                        )
                    )
                )
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    TextureView(ctx).apply {
                        isOpaque = false
                        val host = JudyFilamentHost(this)

                        addOnAttachStateChangeListener(object : android.view.View.OnAttachStateChangeListener {
                            override fun onViewAttachedToWindow(v: android.view.View) = Unit
                            override fun onViewDetachedFromWindow(v: android.view.View) {
                                isViewReleased = true
                                host.destroy()
                            }
                        })

                        var downX = 0f
                        var downY = 0f
                        var lastX = 0f
                        var lastY = 0f
                        var downTime = 0L
                        var lastMoveTime = 0L

                        setOnTouchListener { v, event ->
                            if (!currentInteractive) {
                                return@setOnTouchListener false
                            }
                            when (event.actionMasked) {
                                MotionEvent.ACTION_DOWN -> {
                                    downX = event.x
                                    downY = event.y
                                    lastX = event.x
                                    lastY = event.y
                                    downTime = event.eventTime
                                    lastMoveTime = event.eventTime
                                    orbitState.isDragging = true
                                    orbitState.yawVelocity = 0f
                                    orbitState.lastInteractionNs = System.nanoTime()
                                    v.parent?.requestDisallowInterceptTouchEvent(true)
                                }
                                MotionEvent.ACTION_MOVE -> {
                                    val dx = event.x - lastX
                                    val dy = event.y - lastY
                                    val dtMs = (event.eventTime - lastMoveTime).coerceAtLeast(1L)
                                    orbitState.userYawDeg += dx * 0.50f
                                    orbitState.userPitchDeg = (orbitState.userPitchDeg + dy * 0.22f).coerceIn(-18f, 18f)
                                    orbitState.yawVelocity = ((dx * 0.50f) / (dtMs / 1000f)).coerceIn(-380f, 380f)
                                    orbitState.lastInteractionNs = System.nanoTime()
                                    lastX = event.x
                                    lastY = event.y
                                    lastMoveTime = event.eventTime
                                }
                                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                    orbitState.isDragging = false
                                    orbitState.lastInteractionNs = System.nanoTime()
                                    v.parent?.requestDisallowInterceptTouchEvent(false)
                                    if (event.actionMasked == MotionEvent.ACTION_UP) {
                                        val totalDx = abs(event.x - downX)
                                        val totalDy = abs(event.y - downY)
                                        if (totalDx < 22f && totalDy < 22f && (event.eventTime - downTime) < 280L) {
                                            orbitState.tapImpulseStartNs = System.nanoTime()
                                            orbitState.userYawDeg *= 0.20f
                                            orbitState.userPitchDeg *= 0.20f
                                            orbitState.yawVelocity = 0f
                                            physicsEngine.triggerTapImpulse()
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
                },
                onRelease = {
                    isViewReleased = true
                    hostHolder?.destroy()
                    hostHolder = null
                }
            )

            if (!isModelReady) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
