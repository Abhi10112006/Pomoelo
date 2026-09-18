import re

with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'r') as f:
    content = f.read()

# ADD IMPORTS
imports = """import androidx.compose.ui.graphics.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*"""

new_imports = """import androidx.compose.ui.graphics.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin"""

content = content.replace(imports, new_imports)

# ADD DOODLE CLASSES
top_classes = """private val SpeechBubbleShape"""

new_classes = """enum class DoodleType { STAR, SPARKLE, BOOK, PENCIL, TARGET, CLOUD, CUP, LEAF, SPROUT }

data class DoodleData(
    val type: DoodleType,
    val xRatio: Float,
    val yRatio: Float,
    val delayMs: Int,
    val durationMs: Int
)

@Composable
fun AmbientDoodles(step: Int, theme: com.example.ui.theme.AppColors) {
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

private val SpeechBubbleShape"""

content = content.replace(top_classes, new_classes)

# ADD TONE GENERATOR AND EFFECTS TO InteractiveAboutScreen
target_vars = """    var step by remember { mutableIntStateOf(0) }
    var charsToDisplay by remember { mutableIntStateOf(0) }
    var skipTyping by remember { mutableStateOf(false) }"""

new_vars = """    val toneGenerator = remember {
        try {
            android.media.ToneGenerator(android.media.AudioManager.STREAM_SYSTEM, 30) // Subtle volume
        } catch (e: Exception) { null }
    }
    DisposableEffect(Unit) {
        onDispose { toneGenerator?.release() }
    }

    var step by remember { mutableIntStateOf(0) }
    var charsToDisplay by remember { mutableIntStateOf(0) }
    var skipTyping by remember { mutableStateOf(false) }"""
content = content.replace(target_vars, new_vars)

# MODIFY THE LAUNCHED EFFECT FOR TYPING AND SOUND
target_effect = """        // Typing animation
        while (charsToDisplay < text.length) {
            if (skipTyping) {
                charsToDisplay = text.length
                break
            }
            delay(40)
            charsToDisplay++
        }
        
        // Wait 2 seconds before auto-advance
        var waitTime = 0
        while (waitTime < 2000) {
            if (skipTyping) {
                break
            }
            delay(50)
            waitTime += 50
        }
        
        if (step < messages.lastIndex) {
            step++
        }"""

new_effect = """        // Typing animation
        while (charsToDisplay < text.length) {
            if (skipTyping) {
                charsToDisplay = text.length
                break
            }
            try {
                toneGenerator?.startTone(android.media.ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 10)
            } catch (e: Exception) {}
            delay(40)
            charsToDisplay++
        }
        
        // Typing completed chime
        try {
            toneGenerator?.startTone(android.media.ToneGenerator.TONE_PROP_ACK, 25)
        } catch (e: Exception) {}
        
        // Wait 2 seconds before auto-advance
        var waitTime = 0
        while (waitTime < 2000) {
            if (skipTyping) {
                break
            }
            delay(50)
            waitTime += 50
        }
        
        if (step < messages.lastIndex) {
            step++
        }"""
content = content.replace(target_effect, new_effect)

# ADD AMBIENT DOODLES TO THE BOX
target_box = """    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (step < messages.lastIndex) {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    
                    if (charsToDisplay < messages[step].length) {
                        skipTyping = true // Instantly reveal text
                    } else {
                        skipTyping = true // Instantly skip the 2-second delay
                    }
                }
            }
    ) {"""

new_box = """    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (step < messages.lastIndex) {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    
                    if (charsToDisplay < messages[step].length) {
                        skipTyping = true // Instantly reveal text
                    } else {
                        skipTyping = true // Instantly skip the 2-second delay
                    }
                }
            }
    ) {
        AmbientDoodles(step = step, theme = currentTheme)"""
content = content.replace(target_box, new_box)

with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'w') as f:
    f.write(content)
