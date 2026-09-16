package com.example.ui.components

import androidx.compose.ui.graphics.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.LocalAppFont
import kotlinx.coroutines.delay

@Composable
fun InteractiveAboutScreen() {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val view = LocalView.current

    var step by remember { mutableIntStateOf(0) }
    var isTransitioning by remember { mutableStateOf(false) }

    val messages = listOf(
        "Hey! I'm PomoPal. 👋",
        "I'm here to help you focus...",
        "...take meaningful breaks...",
        "...and make studying a little less painful. 😌",
        "I was built with one simple goal...",
        "To help you show up.",
        "And keep growing. 🌱",
        "Made with ❤️ by Abhinav Yaduvanshi\n\nPomoPal 3.0 — Momentum"
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
                if (!isTransitioning && step < messages.lastIndex) {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    step++
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
                    .height(160.dp),
                contentAlignment = Alignment.Center
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
                    if (currentStep == messages.lastIndex) {
                        // Final credits style
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        // Storyteller style
                        Text(
                            text = text,
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = currentTheme.textPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            // Interaction hint (Pushed to bottom)
            Box(
                modifier = Modifier.height(24.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isCredits,
                    enter = fadeIn(tween(500, delayMillis = 1000)),
                    exit = fadeOut(tween(200))
                ) {
                    val infinitePulse = rememberInfiniteTransition(label = "pulse")
                    val alphaPulse by infinitePulse.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 0.8f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "alphaPulse"
                    )
                    
                    Text(
                        text = "Tap to continue →",
                        fontFamily = currentFont,
                        fontSize = 14.sp,
                        color = currentTheme.primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.graphicsLayer { alpha = alphaPulse }
                    )
                }
            }
        }
    }
}
