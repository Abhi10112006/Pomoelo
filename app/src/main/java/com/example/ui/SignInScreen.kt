package com.example.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.service.SettingsManager
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.luminance
import com.example.ui.theme.blend
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(navController: NavController, onSignInSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val view = LocalView.current
    val density = LocalDensity.current

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

    val smoothDecelEasing = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)

    LaunchedEffect(Unit) {
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
            .imePadding()
    ) {
        val minHeight = maxHeight
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Hero Mascot with gentle scale-in, fade-in and subtle idle breathing
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                        translationY = logoTranslationY.value + floatOffsetPx
                    }
                    .size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Ambient Glow
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .graphicsLayer {
                            scaleX = pulseAnim
                            scaleY = pulseAnim
                        }
                        .clip(CircleShape)
                        .background(currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.08f else 0.05f))
                )
                // Inner Ambient Glow
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .graphicsLayer {
                            scaleX = pulseAnim * 0.95f
                            scaleY = pulseAnim * 0.95f
                        }
                        .clip(CircleShape)
                        .background(currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.15f else 0.1f))
                )
                // Core Tomato Mascot
                Text(
                    text = "🍅",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Staggered Titles
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
                    color = currentTheme.textPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Your little space for better focus.",
                    fontSize = 16.sp,
                    fontFamily = currentFont,
                    color = currentTheme.textSecondary
                )
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Staggered Input Field Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = inputAlpha.value
                        translationY = inputTranslationY.value
                    },
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Let's make this yours",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = currentFont,
                    color = currentTheme.textSecondary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )
                Text(
                    "What should PomoPal call you?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary,
                    modifier = Modifier.padding(start = 8.dp, bottom = 16.dp)
                )

                val borderColor by animateColorAsState(
                    targetValue = when {
                        showError -> errorColor
                        isFocused -> currentTheme.primary
                        else -> currentTheme.cardBorder
                    },
                    label = "borderColor"
                )

                val surfaceBgColor by animateColorAsState(
                    targetValue = if (isFocused) {
                        currentTheme.surface.blend(currentTheme.primary, if (currentTheme.isDark) 0.15f else 0.05f)
                    } else currentTheme.surface,
                    label = "surfaceBgColor"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(surfaceBgColor)
                        .border(2.dp, borderColor, RoundedCornerShape(24.dp))
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "✨ ",
                            fontSize = 18.sp
                        )
                        BasicTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                showError = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
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
                                    focusManager.clearFocus()
                                    // DO NOT SUBMIT HERE - ONLY DISMISS KEYBOARD
                                }
                            ),
                            decorationBox = { innerTextField ->
                                if (name.isEmpty()) {
                                    Text(
                                        text = "Your name",
                                        color = currentTheme.textSecondary.copy(alpha = 0.5f),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = currentFont
                                    )
                                }
                                innerTextField()
                            }
                        )
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

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // Staggered CTA Button
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
                        focusManager.clearFocus()
                        if (name.isNotBlank()) {
                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                            SettingsManager.setUserName(name.trim())
                            onSignInSuccess()
                        } else {
                            showError = true
                            try { view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS) } catch (e: Exception) {}
                        }
                    },
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(buttonScale),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentTheme.primary,
                        contentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 0.dp)
                ) {
                    Text(
                        text = "Let's Begin →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

