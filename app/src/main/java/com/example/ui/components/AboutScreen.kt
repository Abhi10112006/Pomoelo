package com.example.ui.components

import androidx.compose.ui.graphics.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.PomoMotion
import kotlinx.coroutines.delay

enum class DoodleType { STAR, SPARKLE, BOOK, PENCIL, TARGET, CLOUD, CUP, LEAF, SPROUT }

data class DoodleData(
    val type: DoodleType,
    val xRatio: Float,
    val yRatio: Float,
    val delayMs: Int,
    val durationMs: Int
)

@Composable
fun AmbientDoodles(step: Int, theme: com.example.ui.theme.ThemeOption) {
    val doodles = remember(step) {
        when (step) {
            0 -> listOf(
                DoodleData(DoodleType.STAR, 0.15f, 0.2f, 0, 3000),
                DoodleData(DoodleType.SPARKLE, 0.85f, 0.25f, 300, 2800),
                DoodleData(DoodleType.STAR, 0.8f, 0.6f, 600, 3200)
            )
            1 -> listOf(
                DoodleData(DoodleType.TARGET, 0.2f, 0.3f, 100, 3000),
                DoodleData(DoodleType.PENCIL, 0.85f, 0.2f, 400, 2500)
            )
            2 -> listOf(
                DoodleData(DoodleType.CLOUD, 0.15f, 0.25f, 0, 3500),
                DoodleData(DoodleType.CUP, 0.8f, 0.4f, 500, 3000)
            )
            3 -> listOf(
                DoodleData(DoodleType.STAR, 0.2f, 0.5f, 200, 2800),
                DoodleData(DoodleType.BOOK, 0.85f, 0.3f, 0, 3200)
            )
            4 -> listOf(
                DoodleData(DoodleType.TARGET, 0.15f, 0.3f, 200, 3000),
                DoodleData(DoodleType.SPARKLE, 0.8f, 0.5f, 500, 2500)
            )
            5 -> listOf(
                DoodleData(DoodleType.STAR, 0.85f, 0.2f, 100, 3000)
            )
            6 -> listOf(
                DoodleData(DoodleType.SPROUT, 0.2f, 0.4f, 0, 3500),
                DoodleData(DoodleType.LEAF, 0.8f, 0.3f, 400, 3000),
                DoodleData(DoodleType.LEAF, 0.15f, 0.6f, 800, 3000)
            )
            7 -> listOf(
                DoodleData(DoodleType.SPARKLE, 0.2f, 0.2f, 0, 4000),
                DoodleData(DoodleType.STAR, 0.8f, 0.3f, 500, 3500),
                DoodleData(DoodleType.SPARKLE, 0.15f, 0.6f, 1000, 3800),
                DoodleData(DoodleType.STAR, 0.85f, 0.7f, 1500, 3500)
            )
            else -> emptyList()
        }
    }

    val progresses = doodles.map { doodle ->
        val progress = remember(step, doodle) { Animatable(0f) }
        LaunchedEffect(step, doodle) {
            delay(doodle.delayMs.toLong())
            progress.animateTo(1f, animationSpec = tween(doodle.durationMs, easing = LinearOutSlowInEasing))
        }
        progress
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        doodles.forEachIndexed { index, doodle ->
            val p = progresses[index].value
            if (p > 0f && p < 1f) {
                val alpha = if (p < 0.2f) p / 0.2f else if (p > 0.8f) (1f - p) / 0.2f else 1f
                val yOffset = p * size.height * 0.15f
                val x = size.width * doodle.xRatio
                val y = size.height * doodle.yRatio + yOffset
                val rotation = p * 60f * (if (index % 2 == 0) 1f else -1f)
                val doodleSize = size.width * 0.08f
                
                withTransform({
                    translate(left = x, top = y)
                    rotate(rotation)
                }) {
                    drawDoodle(doodle.type, doodleSize, theme.textPrimary.copy(alpha = alpha * 0.35f))
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawDoodle(type: DoodleType, dSize: Float, color: Color) {
    val s = dSize / 2f
    when (type) {
        DoodleType.STAR -> {
            val path = Path().apply {
                moveTo(0f, -s)
                quadraticTo(s*0.2f, -s*0.2f, s, 0f)
                quadraticTo(s*0.2f, s*0.2f, 0f, s)
                quadraticTo(-s*0.2f, s*0.2f, -s, 0f)
                quadraticTo(-s*0.2f, -s*0.2f, 0f, -s)
                close()
            }
            drawPath(path, color)
        }
        DoodleType.SPARKLE -> {
            drawCircle(color, radius = s * 0.15f, center = Offset(0f, -s*0.7f))
            drawCircle(color, radius = s * 0.15f, center = Offset(0f, s*0.7f))
            drawCircle(color, radius = s * 0.15f, center = Offset(-s*0.7f, 0f))
            drawCircle(color, radius = s * 0.15f, center = Offset(s*0.7f, 0f))
            drawCircle(color, radius = s * 0.3f, center = Offset(0f, 0f))
        }
        DoodleType.BOOK -> {
            val path = Path().apply {
                moveTo(-s*0.8f, -s*0.6f)
                lineTo(s*0.8f, -s*0.6f)
                lineTo(s*0.8f, s*0.6f)
                lineTo(-s*0.8f, s*0.6f)
                close()
                moveTo(0f, -s*0.6f)
                lineTo(0f, s*0.6f)
            }
            drawPath(path, color, style = Stroke(width = dSize * 0.1f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        DoodleType.PENCIL -> {
            val path = Path().apply {
                moveTo(-s*0.7f, s*0.7f)
                lineTo(-s*0.4f, s*0.7f)
                lineTo(s*0.6f, -s*0.3f)
                lineTo(s*0.3f, -s*0.6f)
                lineTo(-s*0.7f, s*0.4f)
                close()
                moveTo(-s*0.7f, s*0.7f)
                lineTo(-s*0.55f, s*0.55f)
            }
            drawPath(path, color, style = Stroke(width = dSize * 0.1f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        DoodleType.TARGET -> {
            drawCircle(color, radius = s * 0.8f, style = Stroke(width = dSize * 0.1f))
            drawCircle(color, radius = s * 0.4f, style = Stroke(width = dSize * 0.1f))
            drawCircle(color, radius = dSize * 0.1f)
        }
        DoodleType.CLOUD -> {
            val path = Path().apply {
                moveTo(-s*0.5f, s*0.3f)
                arcTo(androidx.compose.ui.geometry.Rect(-s, -s*0.3f, 0f, s*0.3f), 180f, 180f, false)
                arcTo(androidx.compose.ui.geometry.Rect(-s*0.4f, -s*0.7f, s*0.4f, s*0.1f), 180f, 180f, false)
                arcTo(androidx.compose.ui.geometry.Rect(0f, -s*0.4f, s, s*0.4f), 180f, 180f, false)
            }
            drawPath(path, color, style = Stroke(width = dSize * 0.1f, cap = StrokeCap.Round))
            drawLine(color, start = Offset(-s*0.5f, s*0.3f), end = Offset(s*0.5f, s*0.3f), strokeWidth = dSize * 0.1f, cap = StrokeCap.Round)
        }
        DoodleType.CUP -> {
            val path = Path().apply {
                moveTo(-s*0.5f, -s*0.5f)
                lineTo(s*0.5f, -s*0.5f)
                lineTo(s*0.4f, s*0.5f)
                lineTo(-s*0.4f, s*0.5f)
                close()
            }
            drawPath(path, color, style = Stroke(width = dSize * 0.1f, cap = StrokeCap.Round, join = StrokeJoin.Round))
            drawArc(color, startAngle = -90f, sweepAngle = 180f, useCenter = false, topLeft = Offset(s*0.4f, -s*0.2f), size = Size(s*0.4f, s*0.5f), style = Stroke(width = dSize * 0.1f, cap = StrokeCap.Round))
            val steam = Path().apply {
                moveTo(-s*0.2f, -s*0.7f)
                quadraticTo(-s*0.4f, -s*0.9f, -s*0.2f, -s*1.1f)
                moveTo(s*0.2f, -s*0.6f)
                quadraticTo(0f, -s*0.8f, s*0.2f, -s*1.0f)
            }
            drawPath(steam, color, style = Stroke(width = dSize * 0.08f, cap = StrokeCap.Round))
        }
        DoodleType.LEAF -> {
            val path = Path().apply {
                moveTo(0f, s*0.8f)
                quadraticTo(-s, s*0.2f, 0f, -s*0.8f)
                quadraticTo(s, s*0.2f, 0f, s*0.8f)
                close()
                moveTo(0f, s*0.8f)
                lineTo(0f, -s*0.6f)
            }
            drawPath(path, color, style = Stroke(width = dSize * 0.1f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        DoodleType.SPROUT -> {
            val path = Path().apply {
                moveTo(0f, s*0.8f)
                quadraticTo(0f, s*0.2f, -s*0.5f, 0f)
                quadraticTo(-s*0.8f, -s*0.5f, -s*0.2f, -s*0.5f)
                quadraticTo(0f, -s*0.2f, 0f, s*0.2f)
                
                moveTo(0f, s*0.4f)
                quadraticTo(s*0.3f, 0f, s*0.8f, -s*0.2f)
                quadraticTo(s*0.4f, -s*0.7f, s*0.2f, -s*0.3f)
                quadraticTo(0f, -s*0.1f, 0f, s*0.2f)
            }
            drawPath(path, color, style = Stroke(width = dSize * 0.1f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}

private val SpeechBubbleShape = object : androidx.compose.ui.graphics.Shape {
    override fun createOutline(size: androidx.compose.ui.geometry.Size, layoutDirection: androidx.compose.ui.unit.LayoutDirection, density: androidx.compose.ui.unit.Density): androidx.compose.ui.graphics.Outline {
        val cornerRadius = with(density) { 24.dp.toPx() }
        val tailHeight = with(density) { 12.dp.toPx() }
        val tailWidth = with(density) { 24.dp.toPx() }
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(cornerRadius, tailHeight)
            lineTo(size.width / 2f - tailWidth / 2f, tailHeight)
            lineTo(size.width / 2f, 0f)
            lineTo(size.width / 2f + tailWidth / 2f, tailHeight)
            lineTo(size.width - cornerRadius, tailHeight)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(size.width - cornerRadius * 2, tailHeight, size.width, tailHeight + cornerRadius * 2),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(size.width, size.height - cornerRadius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(size.width - cornerRadius * 2, size.height - cornerRadius * 2, size.width, size.height),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(cornerRadius, size.height)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, size.height - cornerRadius * 2, cornerRadius * 2, size.height),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(0f, tailHeight + cornerRadius)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(0f, tailHeight, cornerRadius * 2, tailHeight + cornerRadius * 2),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
        return androidx.compose.ui.graphics.Outline.Generic(path)
    }
}

@Composable
fun InteractiveAboutScreen() {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val view = LocalView.current

    val toneGenerator = remember {
        try {
            android.media.ToneGenerator(android.media.AudioManager.STREAM_SYSTEM, 30)
        } catch (e: Exception) { null }
    }
    DisposableEffect(Unit) {
        onDispose { toneGenerator?.release() }
    }

    var step by remember { mutableIntStateOf(0) }
    var charsToDisplay by remember { mutableIntStateOf(0) }
    var skipTyping by remember { mutableStateOf(false) }
    var patReactionText by remember { mutableStateOf<String?>(null) }
    var patBurstKey by remember { mutableIntStateOf(0) }
    var customMoodOverride by remember { mutableStateOf<MascotExpression?>(null) }
    var customSpeakPulse by remember { mutableStateOf(false) }

    val messages = remember {
        listOf(
            "Hey! I'm Judy, your PomoPal. 🐰",
            "I'm here to help you lock in & focus...",
            "...take meaningful, cozy breaks...",
            "...and make studying feel lighter. 😌",
            "I was built with one simple goal...",
            "To help you show up, every day.",
            "And keep growing, one block at a time. 🌱",
            "Made with ❤️ by Abhinav Yaduvanshi\n\nPomoPal 3.0 — Momentum"
        )
    }

    val expressions = remember {
        listOf(
            MascotExpression.EXCITED, // "Hey! I'm Judy, your PomoPal."
            MascotExpression.FOCUS,   // "I'm here to help you lock in & focus..."
            MascotExpression.RELAX,   // "...take meaningful, cozy breaks..."
            MascotExpression.CURIOUS, // "...and make studying feel lighter."
            MascotExpression.FOCUS,   // "I was built with one simple goal..."
            MascotExpression.IDLE,    // "To help you show up, every day."
            MascotExpression.HAPPY,   // "And keep growing..."
            MascotExpression.HAPPY    // Credits
        )
    }

    val emotionLabels = remember {
        listOf(
            "✨ Excited to meet you",
            "🎯 Locked In",
            "🌿 Cozy Break Mode",
            "💜 Right Beside You",
            "🌟 Purpose Driven",
            "💪 Steady Momentum",
            "🌱 Growing Together",
            "🐰 Interactive Companion"
        )
    }

    val patQuotes = remember {
        listOf(
            "Hehe! *ear wiggle* ✨",
            "You've got this! 🥕",
            "Cheering for you! 💜",
            "Let's stay focused! 🌟"
        )
    }

    val isCredits = step == messages.lastIndex
    val activeExpression = if (patReactionText != null) {
        MascotExpression.EXCITED
    } else if (isCredits && customMoodOverride != null) {
        customMoodOverride!!
    } else {
        expressions[step]
    }

    val targetAuraColor = when (activeExpression) {
        MascotExpression.EXCITED -> Color(0xFFFF8A65)
        MascotExpression.HAPPY -> currentTheme.primary
        MascotExpression.FOCUS -> Color(0xFF5C9CE6)
        MascotExpression.RELAX -> Color(0xFF66BB8A)
        MascotExpression.CURIOUS -> Color(0xFF9C88FF)
        MascotExpression.IDLE -> currentTheme.secondary
    }
    val auraColor by animateColorAsState(
        targetValue = targetAuraColor,
        animationSpec = PomoMotion.colorSpec,
        label = "judyAuraColor"
    )

    val auraInfinite = rememberInfiniteTransition(label = "aboutAura")
    val auraPulse by auraInfinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraPulse"
    )

    LaunchedEffect(patReactionText) {
        if (patReactionText != null) {
            delay(1800)
            patReactionText = null
        }
    }

    LaunchedEffect(customSpeakPulse) {
        if (customSpeakPulse) {
            delay(1400)
            customSpeakPulse = false
        }
    }

    LaunchedEffect(step) {
        val text = messages[step]
        skipTyping = false
        charsToDisplay = 0
        customMoodOverride = null

        if (isCredits) {
            charsToDisplay = text.length
            return@LaunchedEffect
        }

        while (charsToDisplay < text.length) {
            if (skipTyping) {
                charsToDisplay = text.length
                break
            }
            try {
                toneGenerator?.startTone(android.media.ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 10)
            } catch (_: Exception) {}
            delay(38)
            charsToDisplay++
        }

        try {
            toneGenerator?.startTone(android.media.ToneGenerator.TONE_PROP_ACK, 25)
        } catch (_: Exception) {}

        var waitTime = 0
        while (waitTime < 2200) {
            if (skipTyping) break
            delay(50)
            waitTime += 50
        }

        if (step < messages.lastIndex) {
            step++
        }
    }

    val isCurrentlySpeaking = (!isCredits && charsToDisplay < messages[step].length) || customSpeakPulse || patReactionText != null

    val advanceStory = {
        if (step < messages.lastIndex) {
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
            if (charsToDisplay < messages[step].length) {
                skipTyping = true
            } else {
                step++
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { advanceStory() }
    ) {
        AmbientDoodles(step = step, theme = currentTheme)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            // Story progress indicator dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                messages.indices.forEach { idx ->
                    val isCurrent = idx == step
                    val dotWidth by animateDpAsState(
                        targetValue = if (isCurrent) 22.dp else 7.dp,
                        animationSpec = PomoMotion.expressiveSpatial(Dp.VisibilityThreshold),
                        label = "dotWidth_$idx"
                    )
                    val dotColor by animateColorAsState(
                        targetValue = if (idx <= step) auraColor else currentTheme.cardBorder,
                        animationSpec = PomoMotion.colorSpec,
                        label = "dotColor_$idx"
                    )
                    Box(
                        modifier = Modifier
                            .height(7.dp)
                            .width(dotWidth)
                            .background(dotColor, RoundedCornerShape(4.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                step = idx
                            }
                    )
                }
            }

            // Interactive 3D Judy Stage (seamlessly blended over mood-reactive radial glow)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Mood-reactive glowing aura behind transparent 3D Judy
                Canvas(
                    modifier = Modifier
                        .size(260.dp)
                        .graphicsLayer {
                            scaleX = auraPulse
                            scaleY = auraPulse
                        }
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                auraColor.copy(alpha = if (currentTheme.isDark) 0.36f else 0.24f),
                                auraColor.copy(alpha = 0.10f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 2f
                        )
                    )
                }

                // Live Transparent Interactive 3D Judy (full-body skeleton + spring physics)
                PomoPalMascot(
                    expression = activeExpression,
                    modifier = Modifier.size(width = 272.dp, height = 286.dp),
                    isBreathing = true,
                    isSpeaking = isCurrentlySpeaking,
                    interactive = true,
                    onTap = {
                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                        patBurstKey++
                        patReactionText = patQuotes[patBurstKey % patQuotes.size]
                    }
                )

                // Floating pat reaction toast over Judy
                androidx.compose.animation.AnimatedVisibility(
                    visible = patReactionText != null,
                    enter = fadeIn(PomoMotion.fastEffects()) +
                        scaleIn(initialScale = 0.72f, animationSpec = PomoMotion.expressiveSpatial()) +
                        slideInVertically(animationSpec = PomoMotion.intOffsetFastSpatial) { 20 },
                    exit = fadeOut(PomoMotion.fastEffects()) +
                        scaleOut(targetScale = 0.85f, animationSpec = PomoMotion.fastSpatial()) +
                        slideOutVertically(animationSpec = PomoMotion.intOffsetFastSpatial) { -16 },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = auraColor.copy(alpha = 0.35f))
                            .background(currentTheme.surface, RoundedCornerShape(16.dp))
                            .border(1.dp, auraColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = patReactionText.orEmpty(),
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = currentTheme.textPrimary
                        )
                    }
                }
            }

            // Live Emotion Status Pill + Drag hint
            val moodLabel = if (isCredits && customMoodOverride != null) {
                when (customMoodOverride) {
                    MascotExpression.EXCITED -> "🤩 Feeling Excited"
                    MascotExpression.FOCUS -> "🎯 Focus Mode"
                    MascotExpression.RELAX -> "🌿 Cozy & Relaxed"
                    MascotExpression.CURIOUS -> "🤔 Curious Mood"
                    else -> "😊 Happy & Ready"
                }
            } else {
                emotionLabels[step]
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .background(auraColor.copy(alpha = 0.14f), RoundedCornerShape(20.dp))
                    .border(1.dp, auraColor.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = moodLabel,
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = currentTheme.textPrimary
                )
                if (isCurrentlySpeaking) {
                    Text(
                        text = "• speaking",
                        fontFamily = currentFont,
                        fontSize = 11.sp,
                        color = auraColor
                    )
                }
            }

            // Message Text / Credits Mood Playground
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 178.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        PomoMotion.verticalCrossfadeTransition(upward = true)
                    },
                    label = "MessageTransition"
                ) { currentStep ->
                    val text = messages[currentStep]
                    val isCreditsLocal = currentStep == messages.lastIndex
                    if (isCreditsLocal) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(currentTheme.surface, RoundedCornerShape(24.dp))
                                .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(24.dp))
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = "Try Judy's Moods",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = currentTheme.textSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val moods = listOf(
                                "😊 Happy" to MascotExpression.HAPPY,
                                "🤩 Excited" to MascotExpression.EXCITED,
                                "🎯 Focus" to MascotExpression.FOCUS,
                                "🌿 Relax" to MascotExpression.RELAX,
                                "🤔 Curious" to MascotExpression.CURIOUS
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                moods.forEach { (label, mood) ->
                                    val selected = activeExpression == mood
                                    val moodInteraction = remember { MutableInteractionSource() }
                                    val isMoodPressed by moodInteraction.collectIsPressedAsState()
                                    val moodScale by animateFloatAsState(
                                        targetValue = if (isMoodPressed) 0.90f else (if (selected) 1.06f else 1f),
                                        animationSpec = PomoMotion.expressiveSpatial(),
                                        label = "moodScale"
                                    )
                                    val moodBg by animateColorAsState(
                                        targetValue = if (selected) auraColor else currentTheme.backgroundSecondary,
                                        animationSpec = PomoMotion.colorSpec,
                                        label = "moodBg"
                                    )
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .graphicsLayer {
                                                scaleX = moodScale
                                                scaleY = moodScale
                                            }
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(moodBg)
                                            .clickable(
                                                interactionSource = moodInteraction,
                                                indication = ripple(bounded = true)
                                            ) {
                                                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                customMoodOverride = mood
                                                customSpeakPulse = true
                                            }
                                            .padding(vertical = 7.dp, horizontal = 2.dp)
                                    ) {
                                        Text(
                                            text = label.substringBefore(" "),
                                            fontSize = 15.sp
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Made with ❤️ by Abhinav Yaduvanshi",
                                fontFamily = currentFont,
                                fontSize = 14.sp,
                                color = currentTheme.textSecondary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "PomoPal 3.0 — Momentum",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = currentTheme.textPrimary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            val replayInteraction = remember { MutableInteractionSource() }
                            val isReplayPressed by replayInteraction.collectIsPressedAsState()
                            val replayScale by animateFloatAsState(
                                targetValue = if (isReplayPressed) 0.93f else 1f,
                                animationSpec = PomoMotion.pressScaleSpec,
                                label = "replayScale"
                            )
                            Text(
                                text = "↻ Replay Judy's Story",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = currentTheme.primary,
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = replayScale
                                        scaleY = replayScale
                                    }
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(currentTheme.primary.copy(alpha = 0.12f))
                                    .clickable(
                                        interactionSource = replayInteraction,
                                        indication = ripple(bounded = true)
                                    ) {
                                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                        step = 0
                                    }
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    } else {
                        val displayedText = if (currentStep == step) text.take(charsToDisplay) else text
                        val finishedTyping = currentStep == step && charsToDisplay >= text.length

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = SpeechBubbleShape,
                                    ambientColor = auraColor.copy(alpha = 0.35f),
                                    spotColor = auraColor.copy(alpha = 0.35f)
                                )
                                .background(currentTheme.surface, SpeechBubbleShape)
                                .border(1.dp, auraColor.copy(alpha = 0.22f), SpeechBubbleShape)
                                .clickable { advanceStory() }
                                .padding(top = 34.dp, bottom = 22.dp, start = 22.dp, end = 22.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = displayedText,
                                    fontFamily = currentFont,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 19.sp,
                                    color = currentTheme.textPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (finishedTyping) "Tap bubble to continue →" else "Drag Judy to spin • Tap to skip",
                                    fontFamily = currentFont,
                                    fontSize = 11.sp,
                                    color = currentTheme.textSecondary.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
