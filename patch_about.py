import re

with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'r') as f:
    content = f.read()

replacement = """package com.example.ui.components

import androidx.compose.ui.graphics.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.LocalAppFont
import kotlinx.coroutines.delay

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

    var step by remember { mutableIntStateOf(0) }
    var charsToDisplay by remember { mutableIntStateOf(0) }
    var skipTyping by remember { mutableStateOf(false) }

    val messages = listOf(
        "Hey! I'm PomoPal. 👋",
        "I'm here to help you focus...",
        "...take meaningful breaks...",
        "...and make studying a little less painful. 😌",
        "I was built with one simple goal...",
        "To help you show up.",
        "And keep growing. 🌱",
        "Made with ❤️ by Abhinav Yaduvanshi\\n\\nPomoPal 3.0 — Momentum"
    )

    val expressions = listOf(
        MascotExpression.HAPPY, // "Hey! I'm PomoPal."
        MascotExpression.FOCUS, // "I'm here to help you focus..."
        MascotExpression.RELAX, // "...take meaningful breaks..."
        MascotExpression.IDLE,  // "...and make studying a little less painful."
        MascotExpression.FOCUS, // "I was built with one simple goal..."
        MascotExpression.IDLE,  // "To help you show up."
        MascotExpression.HAPPY, // "And keep growing."
        MascotExpression.HAPPY  // Credits
    )

    val isCredits = step == messages.lastIndex
    
    LaunchedEffect(step) {
        val text = messages[step]
        skipTyping = false
        charsToDisplay = 0
        
        if (isCredits) {
            charsToDisplay = text.length
            return@LaunchedEffect
        }
        
        // Typing animation
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
        }
    }

    // Scale animation on tap
    val scale = remember { Animatable(1f) }
    LaunchedEffect(step) {
        if (step > 0) {
            scale.animateTo(1.05f, animationSpec = tween(150))
            scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
    }

    Box(
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp, bottom = 48.dp, start = 32.dp, end = 32.dp)
        ) {
            // Mascot Container (Takes up upper half)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .graphicsLayer { 
                        scaleX = scale.value
                        scaleY = scale.value
                    }
            ) {
                PomoPalMascot(
                    expression = expressions[step],
                    modifier = Modifier.size(240.dp),
                    isBreathing = !isCredits // Stop heavy breathing on credits for subtleness
                )
            }

            // Message Text (Centered in lower half)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(400)) + slideInVertically(animationSpec = tween(400)) { it / 2 }).togetherWith(
                            fadeOut(animationSpec = tween(200))
                        )
                    },
                    label = "MessageTransition"
                ) { currentStep ->
                    val text = messages[currentStep]
                    val isCreditsLocal = currentStep == messages.lastIndex
                    if (isCreditsLocal) {
                        // Final credits style
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 24.dp)) {
                            Text(
                                text = "Made with ❤️ by Abhinav Yaduvanshi",
                                fontFamily = currentFont,
                                fontSize = 16.sp,
                                color = currentTheme.textSecondary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "PomoPal 3.0 — Momentum",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = currentTheme.textPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Speech Bubble style
                        val displayedText = if (currentStep == step) text.take(charsToDisplay) else text
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .shadow(8.dp, SpeechBubbleShape, ambientColor = currentTheme.primary.copy(alpha = 0.5f), spotColor = currentTheme.primary.copy(alpha = 0.5f))
                                .background(currentTheme.surface, SpeechBubbleShape)
                                .border(1.dp, currentTheme.textPrimary.copy(alpha = 0.05f), SpeechBubbleShape)
                                .padding(top = 40.dp, bottom = 28.dp, start = 24.dp, end = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayedText,
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = currentTheme.textPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            
            // Interaction hint (Removed, replaced with Spacer)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
"""

with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'w') as f:
    f.write(replacement)
