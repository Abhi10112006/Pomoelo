package com.example.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(navController: NavController, onSignInSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val view = LocalView.current

    // Steps: 0 = Init, 1 = Logo Entrance, 2 = Welcome Text, 3 = Name Field, 4 = Button
    var step by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        delay(200)
        step = 1
        delay(600)
        step = 2
        delay(600)
        step = 3
        delay(500)
        step = 4
    }

    // Floating animation for the logo
    val infiniteTransition = rememberInfiniteTransition()
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentTheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo / Icon
        AnimatedVisibility(
            visible = step >= 1,
            enter = fadeIn(tween(800)) + scaleIn(initialScale = 0.8f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        ) {
            Box(
                modifier = Modifier
                    .offset(y = (-8).dp + (floatAnim * 8).dp)
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(currentTheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍅",
                    fontSize = 40.sp,
                    modifier = Modifier.offset(y = (floatAnim * 4).dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Titles
        AnimatedVisibility(
            visible = step >= 2,
            enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { 20 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "PomoPal",
                    fontSize = 32.sp,
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
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Input Field
        AnimatedVisibility(
            visible = step >= 3,
            enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { 20 })
        ) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "What should PomoPal call you?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary,
                    modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(currentTheme.surface)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "✨ ",
                            fontSize = 18.sp
                        )
                        BasicTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
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
                                    if (name.isNotBlank()) {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        SettingsManager.setUserName(name)
                                        onSignInSuccess()
                                    }
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
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AnimatedVisibility(
            visible = step >= 4,
            enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { 20 })
        ) {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        SettingsManager.setUserName(name)
                        onSignInSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (name.isNotBlank()) currentTheme.primary else currentTheme.surface,
                    contentColor = if (name.isNotBlank()) {
                        if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                    } else currentTheme.textSecondary
                ),
                elevation = if (name.isNotBlank()) ButtonDefaults.buttonElevation(defaultElevation = 4.dp) else ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = "Let's Begin →",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont
                )
            }
        }
    }
}
