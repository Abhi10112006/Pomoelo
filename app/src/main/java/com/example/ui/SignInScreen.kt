package com.example.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.service.SettingsManager
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.luminance
import com.example.ui.theme.blend
import com.example.ui.components.PomoPalMascot
import com.example.ui.components.MascotExpression
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Interactive3DTile(
    emoji: String,
    tag: String,
    reaction: String,
    baseColor: Color,
    accentColor: Color,
    initialRotationZ: Float = 0f,
    floatDurationY: Int = 3400,
    floatDurationX: Int = 4200,
    floatOffsetY: Float = 12f,
    floatOffsetX: Float = 10f,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val scope = rememberCoroutineScope()
    
    var isPressed by remember { mutableStateOf(false) }
    var touchX by remember { mutableStateOf(0f) }
    var touchY by remember { mutableStateOf(0f) }
    var showReaction by remember { mutableStateOf(false) }

    val tapBounceScale = remember { Animatable(1f) }
    val spinAngle = remember { Animatable(0f) }
    val zeroGJoltY = remember { Animatable(0f) }

    // Multi-axis Zero-Gravity Ambient Floating (Lissajous orbit)
    val infiniteTransition = rememberInfiniteTransition(label = "3d_float_$emoji")
    val ambientFloatY by infiniteTransition.animateFloat(
        initialValue = -floatOffsetY,
        targetValue = floatOffsetY,
        animationSpec = infiniteRepeatable(
            animation = tween(floatDurationY, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "3d_floaty"
    )
    val ambientFloatX by infiniteTransition.animateFloat(
        initialValue = -floatOffsetX,
        targetValue = floatOffsetX,
        animationSpec = infiniteRepeatable(
            animation = tween(floatDurationX, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "3d_floatx"
    )
    val ambientRotZ by infiniteTransition.animateFloat(
        initialValue = initialRotationZ - 5f,
        targetValue = initialRotationZ + 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(floatDurationY + 600, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "3d_rotZ"
    )
    val ambientRotX by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(floatDurationX + 400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "3d_rotX_ambient"
    )
    val ambientRotY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(floatDurationY + 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "3d_rotY_ambient"
    )

    // Interactive press physics
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "pressScale"
    )
    val targetRotX = if (isPressed) -(touchY / 36f).coerceIn(-1f, 1f) * 28f else 0f
    val targetRotY = if (isPressed) (touchX / 36f).coerceIn(-1f, 1f) * 28f else 0f

    val rotX by animateFloatAsState(
        targetValue = targetRotX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "rotX"
    )
    val rotY by animateFloatAsState(
        targetValue = targetRotY,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "rotY"
    )
    val shadowElev by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 12.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "shadowElev"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Floating reaction speech bubble toast
        AnimatedVisibility(
            visible = showReaction,
            enter = fadeIn(tween(180)) + scaleIn(tween(220, easing = LinearOutSlowInEasing), initialScale = 0.7f) + slideInVertically(initialOffsetY = { 20 }),
            exit = fadeOut(tween(250)) + scaleOut(tween(200), targetScale = 0.8f) + slideOutVertically(targetOffsetY = { -20 }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-44).dp)
                .zIndex(10f)
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (currentTheme.isDark) Color(0xFF1E1E24).copy(alpha = 0.95f) else Color.White.copy(alpha = 0.95f),
                border = BorderStroke(1.dp, baseColor.copy(alpha = 0.6f)),
                shadowElevation = 6.dp
            ) {
                Text(
                    text = reaction,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        // The 3D Touchable Tile in Zero Gravity
        Box(
            modifier = Modifier
                .size(width = 82.dp, height = 90.dp)
                .graphicsLayer {
                    this.translationX = ambientFloatX
                    this.translationY = ambientFloatY + zeroGJoltY.value
                    this.rotationZ = ambientRotZ + spinAngle.value
                    this.rotationX = rotX + ambientRotX
                    this.rotationY = rotY + ambientRotY
                    this.scaleX = pressScale * tapBounceScale.value
                    this.scaleY = pressScale * tapBounceScale.value
                    this.cameraDistance = 16f * density
                }
                .shadow(
                    elevation = shadowElev,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = baseColor.copy(alpha = 0.4f),
                    spotColor = baseColor.copy(alpha = if (currentTheme.isDark) 0.65f else 0.4f)
                )
                .clip(RoundedCornerShape(22.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            baseColor,
                            accentColor
                        )
                    )
                )
                .border(
                    BorderStroke(
                        width = 1.5.dp,
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = if (currentTheme.isDark) 0.45f else 0.75f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { offset ->
                            isPressed = true
                            touchX = offset.x - size.width / 2f
                            touchY = offset.y - size.height / 2f
                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                            tryAwaitRelease()
                            isPressed = false
                        },
                        onTap = {
                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                            scope.launch {
                                launch {
                                    tapBounceScale.animateTo(1.2f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                                    tapBounceScale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                                }
                                launch {
                                    spinAngle.animateTo(spinAngle.targetValue + 360f, animationSpec = spring(dampingRatio = 0.58f, stiffness = 160f))
                                }
                                launch {
                                    zeroGJoltY.animateTo(-22f, animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow))
                                    zeroGJoltY.animateTo(0f, animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessVeryLow))
                                }
                                showReaction = true
                                delay(2200)
                                showReaction = false
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(6.dp)
            ) {
                Text(
                    text = emoji,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.22f)
                ) {
                    Text(
                        text = tag,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = currentFont,
                        color = Color.White.copy(alpha = 0.95f),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SignInScreen(navController: NavController, onSignInSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var isExiting by remember { mutableStateOf(false) }

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val density = LocalDensity.current

    val exitAlpha by animateFloatAsState(
        targetValue = if (isExiting) 0f else 1f,
        animationSpec = tween(durationMillis = 350, easing = CubicBezierEasing(0.33f, 1f, 0.68f, 1f)),
        label = "exitAlpha"
    )
    val exitScale by animateFloatAsState(
        targetValue = if (isExiting) 0.94f else 1f,
        animationSpec = tween(durationMillis = 350, easing = CubicBezierEasing(0.33f, 1f, 0.68f, 1f)),
        label = "exitScale"
    )
    val exitTranslationY by animateFloatAsState(
        targetValue = if (isExiting) -24f else 0f,
        animationSpec = tween(durationMillis = 350, easing = CubicBezierEasing(0.33f, 1f, 0.68f, 1f)),
        label = "exitTranslationY"
    )

    val initialOffsetPx = with(density) { 24.dp.toPx() }
    val logoInitialOffsetPx = with(density) { 32.dp.toPx() }

    // Animatable values for gentle, high-performance, GPU-accelerated entrance
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.72f) }
    val logoTranslationY = remember { Animatable(logoInitialOffsetPx) }

    val titleAlpha = remember { Animatable(0f) }
    val titleTranslationY = remember { Animatable(initialOffsetPx) }

    val inputAlpha = remember { Animatable(0f) }
    val inputTranslationY = remember { Animatable(initialOffsetPx) }

    val buttonAlpha = remember { Animatable(0f) }
    val buttonTranslationY = remember { Animatable(initialOffsetPx) }

    val bgElementsAlpha = remember { Animatable(0f) }

    val smoothDecelEasing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)

    LaunchedEffect(Unit) {
        // 0. 3D Elements Entrance
        launch {
            bgElementsAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1200, easing = LinearOutSlowInEasing)
            )
        }
        
        // 1. Hero Mascot Entrance: Gentle scale-in and fade-in
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 750, easing = FastOutSlowInEasing)
            )
        }
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 850, easing = smoothDecelEasing)
            )
        }
        launch {
            logoTranslationY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 850, easing = smoothDecelEasing)
            )
        }

        // 2. Staggered Title Entrance
        delay(140)
        launch {
            titleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
        launch {
            titleTranslationY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 700, easing = smoothDecelEasing)
            )
        }

        // 3. Staggered Input Field Entrance
        delay(140)
        launch {
            inputAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
        launch {
            inputTranslationY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 700, easing = smoothDecelEasing)
            )
        }

        // 4. Staggered CTA Button Entrance
        delay(140)
        launch {
            buttonAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
        launch {
            buttonTranslationY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 700, easing = smoothDecelEasing)
            )
        }
    }

    // Subtle ambient breathing & floating for the hero logo (smooth, calm loop)
    val infiniteTransition = rememberInfiniteTransition(label = "logo_idle")
    val floatAnimDp by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val errorColor = if (currentTheme.isDark) Color(0xFFCF6679) else Color(0xFFB00020)
    val floatOffsetPx = with(density) { floatAnimDp.dp.toPx() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(currentTheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        val availableHeight = maxHeight
        // Mesmerizing Ambient Glow Orbs in background (Spatial Lighting Canvas)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = bgElementsAlpha.value }
        ) {
            val w = size.width
            val h = size.height

            // Top-left luminous cosmic aura (indigo/violet)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF6C5CE7).copy(alpha = if (currentTheme.isDark) 0.16f else 0.08f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.15f, h * 0.12f),
                    radius = w * 0.65f
                )
            )

            // Top-right luminous aura (warm primary coral glow)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.20f else 0.10f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.85f, h * 0.18f),
                    radius = w * 0.70f
                )
            )

            // Lower-ambient subtle warm glow for grounded balance
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.09f else 0.05f),
                        Color.Transparent
                    ),
                    center = Offset(w * 0.5f, h * 0.82f),
                    radius = w * 0.60f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = availableHeight)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .graphicsLayer {
                        alpha = exitAlpha
                        scaleX = exitScale
                        scaleY = exitScale
                        translationY = exitTranslationY
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // 1. Zero-Gravity Interactive Student Constellation (Spacious, unconstrained floating space)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .graphicsLayer { alpha = bgElementsAlpha.value }
                    .padding(top = 10.dp)
            ) {
                // 📚 STUDY - Floating top-left in zero gravity
                Interactive3DTile(
                    emoji = "📚",
                    tag = "STUDY",
                    reaction = "Study Mode Ready! 📖",
                    baseColor = Color(0xFF4A69FF),
                    accentColor = Color(0xFF6C5CE7),
                    initialRotationZ = -9f,
                    floatDurationY = 3800,
                    floatDurationX = 4600,
                    floatOffsetY = 12f,
                    floatOffsetX = 10f,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 12.dp, top = 10.dp)
                )

                // 🎯 FOCUS - Floating top-right in zero gravity
                Interactive3DTile(
                    emoji = "🎯",
                    tag = "FOCUS",
                    reaction = "Deep Focus Activated! ⚡",
                    baseColor = currentTheme.primary,
                    accentColor = currentTheme.primary.blend(Color(0xFFFF7675), 0.5f),
                    initialRotationZ = 7f,
                    floatDurationY = 4200,
                    floatDurationX = 3500,
                    floatOffsetY = 14f,
                    floatOffsetX = 11f,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 14.dp, top = 4.dp)
                )

                // ☕ BREAK - Floating freely in lower-center zero gravity space
                Interactive3DTile(
                    emoji = "☕",
                    tag = "BREAK",
                    reaction = "Recharge & Reset! ☕",
                    baseColor = Color(0xFFFF9F43),
                    accentColor = Color(0xFFEE5253),
                    initialRotationZ = 12f,
                    floatDurationY = 3400,
                    floatDurationX = 4400,
                    floatOffsetY = 10f,
                    floatOffsetX = 12f,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(x = 24.dp)
                        .padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 2. Hero Mascot with warm breathing aura & crisp depth
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                        translationY = logoTranslationY.value + floatOffsetPx
                    }
                    .size(130.dp),
                contentAlignment = Alignment.Center
            ) {
                val primaryColor = currentTheme.primary
                val glowAlpha = if (currentTheme.isDark) 0.38f else 0.22f
                Canvas(
                    modifier = Modifier
                        .size(130.dp)
                        .graphicsLayer {
                            scaleX = pulseAnim
                            scaleY = pulseAnim
                            alpha = glowAlpha
                        }
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(primaryColor, Color.Transparent),
                            center = center,
                            radius = size.width / 2f
                        )
                    )
                }

                // Core Mascot with crisp 3D styling
                PomoPalMascot(
                    expression = MascotExpression.HAPPY,
                    modifier = Modifier.size(76.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Staggered Branding & Typography
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = titleAlpha.value
                        translationY = titleTranslationY.value
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "PomoPal",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = currentFont,
                    letterSpacing = (-0.5).sp,
                    color = currentTheme.textPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Your little space for better focus.",
                    fontSize = 15.sp,
                    fontFamily = currentFont,
                    color = currentTheme.textSecondary.copy(alpha = 0.85f)
                )
            }

            Spacer(modifier = Modifier.weight(1.5f))

            // 4. Staggered Input Field Area with mesmerizing interactive feedback
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = inputAlpha.value
                        translationY = inputTranslationY.value
                    },
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
                ) {
                    Text(
                        "LET'S MAKE THIS YOURS",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = currentFont,
                        letterSpacing = 1.6.sp,
                        color = currentTheme.primary
                    )
                }
                Text(
                    "What should PomoPal call you?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary,
                    modifier = Modifier.padding(start = 6.dp, bottom = 14.dp)
                )

                val borderColor by animateColorAsState(
                    targetValue = when {
                        showError -> errorColor
                        isFocused -> currentTheme.primary
                        name.isNotBlank() -> currentTheme.primary.copy(alpha = 0.6f)
                        else -> currentTheme.cardBorder
                    },
                    label = "borderColor"
                )

                val surfaceBgColor by animateColorAsState(
                    targetValue = if (isFocused) {
                        currentTheme.surface.blend(currentTheme.primary, if (currentTheme.isDark) 0.14f else 0.06f)
                    } else currentTheme.surface,
                    label = "surfaceBgColor"
                )

                val cardShadowElev by animateDpAsState(
                    targetValue = if (isFocused) 8.dp else 3.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "cardShadow"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(cardShadowElev, RoundedCornerShape(26.dp), spotColor = currentTheme.primary.copy(alpha = 0.25f))
                        .clip(RoundedCornerShape(26.dp))
                        .background(surfaceBgColor)
                        .border(2.dp, borderColor, RoundedCornerShape(26.dp))
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "✨",
                            fontSize = 20.sp,
                            modifier = Modifier.graphicsLayer {
                                rotationZ = if (isFocused) 15f else 0f
                                scaleX = if (isFocused) 1.15f else 1f
                                scaleY = if (isFocused) 1.15f else 1f
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        BasicTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                showError = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester)
                                .onFocusChanged { isFocused = it.isFocused },
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont,
                                color = currentTheme.textPrimary
                            ),
                            cursorBrush = SolidColor(currentTheme.primary),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            decorationBox = { innerTextField ->
                                if (name.isEmpty()) {
                                    Text(
                                        text = "Enter your nickname or name",
                                        color = currentTheme.textSecondary.copy(alpha = 0.5f),
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = currentFont
                                    )
                                }
                                innerTextField()
                            }
                        )

                        // Clear button when name has text
                        if (name.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    name = ""
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear input",
                                    tint = currentTheme.textSecondary.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Live Personalized Greeting Feedback
                AnimatedVisibility(
                    visible = name.isNotBlank(),
                    enter = fadeIn(tween(220)) + expandVertically(tween(220)),
                    exit = fadeOut(tween(180)) + shrinkVertically(tween(180))
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.16f else 0.10f),
                        border = BorderStroke(1.dp, currentTheme.primary.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .padding(start = 6.dp, top = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "👋",
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Hey ${name.trim()}! Ready to crush your focus sessions?",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = currentFont,
                                color = currentTheme.primary
                            )
                        }
                    }
                }

                // Error message area
                AnimatedVisibility(
                    visible = showError,
                    enter = fadeIn(tween(250)) + expandVertically(tween(250)),
                    exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
                ) {
                    Text(
                        "Please enter a name to continue.",
                        color = errorColor,
                        fontSize = 12.sp,
                        fontFamily = currentFont,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.5f))

            // 5. Staggered CTA Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = buttonAlpha.value
                        translationY = buttonTranslationY.value
                    }
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val buttonScale by animateFloatAsState(targetValue = if (isPressed) 0.96f else 1f, label = "buttonScale")

                Button(
                    onClick = {
                        if (isExiting) return@Button
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        if (name.isNotBlank()) {
                            try { view.performHapticFeedback(HapticFeedbackConstants.CONFIRM) } catch (e: Exception) {}
                            SettingsManager.setUserName(name.trim())
                            isExiting = true
                            coroutineScope.launch {
                                delay(320)
                                onSignInSuccess()
                            }
                        } else {
                            showError = true
                            try { view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS) } catch (e: Exception) {}
                        }
                    },
                    enabled = !isExiting,
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .scale(buttonScale),
                    shape = RoundedCornerShape(29.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentTheme.primary,
                        contentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 1.dp)
                ) {
                    AnimatedContent(
                        targetState = isExiting to (name.isNotBlank()),
                        transitionSpec = {
                            fadeIn(tween(220)) togetherWith fadeOut(tween(200))
                        },
                        label = "btn_content"
                    ) { (exiting, hasName) ->
                        if (exiting) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                                    strokeWidth = 2.2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Welcome, ${name.trim()}! ✨",
                                    fontSize = 16.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = currentFont
                                )
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (hasName) "Let's Begin, ${name.trim()} →" else "Let's Begin →",
                                    fontSize = 16.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = currentFont
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
}


