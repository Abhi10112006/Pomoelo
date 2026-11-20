package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SettingsManager
import com.example.ui.theme.FontOption
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.PomoMotion
import com.example.ui.theme.blend
import com.example.ui.theme.luminance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun ThemeFontCustomizer(
    modifier: Modifier = Modifier
) {
    val currentTheme = LocalAppTheme.current
    val currentAppFont = LocalAppFont.current
    val currentFont by SettingsManager.fontState.collectAsState()
    val view = LocalView.current

    var showCustomizer by remember { mutableStateOf(false) }
    val customizerBackProgress = remember { Animatable(0f) }
    var customizerBackEdge by remember { mutableIntStateOf(BackEventCompat.EDGE_LEFT) }

    PredictiveBackHandler(enabled = showCustomizer) { progressFlow ->
        try {
            progressFlow.collect { event ->
                customizerBackEdge = event.swipeEdge
                customizerBackProgress.snapTo(event.progress.coerceIn(0f, 1f))
            }
            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
            showCustomizer = false
            customizerBackProgress.snapTo(0f)
        } catch (e: CancellationException) {
            withContext(NonCancellable) {
                customizerBackProgress.animateTo(0f, animationSpec = PomoMotion.fastSpatial())
            }
            throw e
        }
    }

    AnimatedContent(
        targetState = showCustomizer,
        transitionSpec = {
            PomoMotion.horizontalSlideTransition(forward = targetState)
        },
        modifier = Modifier.graphicsLayer {
            val p = customizerBackProgress.value
            val edgeDirection = if (customizerBackEdge == BackEventCompat.EDGE_LEFT) 1f else -1f
            val customizerGestureScale = if (showCustomizer) 1f - (p * 0.06f) else 1f
            val customizerCornerDp = if (showCustomizer) (p * 24f).dp else 0.dp
            scaleX = customizerGestureScale
            scaleY = customizerGestureScale
            if (showCustomizer) {
                translationX = edgeDirection * p * 24.dp.toPx()
                shape = RoundedCornerShape(customizerCornerDp)
                clip = customizerCornerDp > 0.dp
            }
            compositingStrategy = CompositingStrategy.Offscreen
        },
        label = "CustomizerTransition"
    ) { isCustomizerOpen ->
        if (isCustomizerOpen) {
            AppCustomizerScreen(
                onNavigateBack = { showCustomizer = false }
            )
        } else {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Section 1: Make It Yours (Replaces old Themes)
                val makeItYoursInteraction = remember { MutableInteractionSource() }
                val isMakeItYoursPressed by makeItYoursInteraction.collectIsPressedAsState()
                val makeItYoursScale by animateFloatAsState(
                    targetValue = if (isMakeItYoursPressed) 0.96f else 1f,
                    animationSpec = PomoMotion.pressScaleSpec,
                    label = "makeItYoursScale"
                )
                val makeItYoursElevation by animateDpAsState(
                    targetValue = if (isMakeItYoursPressed) 1.dp else 3.dp,
                    animationSpec = PomoMotion.elevationSpec,
                    label = "makeItYoursElevation"
                )

                Card(
                    onClick = {
                        try {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (_: Exception) {}
                        showCustomizer = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = makeItYoursScale
                            scaleY = makeItYoursScale
                        },
                    shape = RoundedCornerShape(24.dp),
                    interactionSource = makeItYoursInteraction,
                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                    border = BorderStroke(1.dp, currentTheme.cardBorder),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = makeItYoursElevation,
                        pressedElevation = 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(4.dp, CircleShape, spotColor = currentTheme.primary)
                                .clip(CircleShape)
                                .background(currentTheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "✨", fontSize = 24.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Make It Yours",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary,
                                fontFamily = currentAppFont
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Create a PomoPal that feels like you.",
                                fontSize = 13.sp,
                                color = currentTheme.textSecondary,
                                fontFamily = currentAppFont
                            )
                        }
                    }
                }

                // Section 2: Typography
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .shadow(3.dp, CircleShape, spotColor = currentTheme.secondary)
                            .clip(CircleShape)
                            .background(currentTheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FontDownload,
                            contentDescription = null,
                            tint = if (currentTheme.secondary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Typography",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textPrimary,
                            fontFamily = currentAppFont
                        )
                        Text(
                            text = "Choose the personality of your PomoPal.",
                            fontSize = 12.sp,
                            color = currentTheme.textSecondary,
                            fontFamily = currentAppFont
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FontOption.entries.forEachIndexed { index, font ->
                        val isSelected = currentFont.id == font.id
                        val revealProgress = remember { Animatable(0f) }
                        LaunchedEffect(Unit) {
                            delay(index * 40L)
                            revealProgress.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
                        }
                        val borderColor by animateColorAsState(
                            targetValue = if (isSelected) currentTheme.primary else currentTheme.cardBorder,
                            animationSpec = PomoMotion.colorSpec,
                            label = "fontBorder"
                        )
                        val targetBgColor = if (isSelected) {
                            if (currentTheme.isDark) {
                                currentTheme.surface.blend(currentTheme.primary, 0.18f)
                            } else {
                                currentTheme.surface.blend(currentTheme.primary, 0.08f)
                            }
                        } else {
                            currentTheme.surface
                        }
                        val cardBgColor by animateColorAsState(
                            targetValue = targetBgColor,
                            animationSpec = PomoMotion.colorSpec,
                            label = "fontCardBg"
                        )
                        val cardElevation by animateDpAsState(
                            targetValue = if (isSelected) 4.dp else 1.dp,
                            animationSpec = PomoMotion.elevationSpec,
                            label = "fontElevation"
                        )

                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()
                        val scale by animateFloatAsState(
                            targetValue = if (isPressed) 0.97f else (if (isSelected) 1.01f else 1f),
                            animationSpec = PomoMotion.pressScaleSpec,
                            label = "fontScale"
                        )
                        val elevation by animateDpAsState(
                            targetValue = if (isPressed) 1.dp else cardElevation,
                            animationSpec = PomoMotion.elevationSpec,
                            label = "fontElevation"
                        )

                        Card(
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                SettingsManager.setFontId(font.id)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    val p = revealProgress.value
                                    alpha = p.coerceIn(0f, 1f)
                                    val baseScale = 0.95f + (0.05f * p)
                                    scaleX = scale * baseScale
                                    scaleY = scale * baseScale
                                    translationX = (1f - p) * 18.dp.toPx()
                                },
                            shape = RoundedCornerShape(20.dp),
                            interactionSource = interactionSource,
                            colors = CardDefaults.cardColors(
                                containerColor = cardBgColor
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = borderColor
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = elevation.coerceAtLeast(0.dp),
                                pressedElevation = 1.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Header: Font name + Badge + Selection Check
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            text = font.displayName,
                                            fontFamily = font.fontFamily,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) currentTheme.primary else currentTheme.textPrimary
                                        )
                                        Surface(
                                            color = currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.25f else 0.14f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = font.badge,
                                                fontFamily = font.fontFamily,
                                                fontSize = 11.sp,
                                                color = currentTheme.primary,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = isSelected,
                                        enter = scaleIn(
                                            initialScale = 0.4f,
                                            animationSpec = PomoMotion.expressiveSpatial()
                                        ) + fadeIn(animationSpec = PomoMotion.fastEffects()),
                                        exit = scaleOut(
                                            targetScale = 0.4f,
                                            animationSpec = PomoMotion.fastSpatial()
                                        ) + fadeOut(animationSpec = PomoMotion.fastEffects())
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(currentTheme.primary),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Check,
                                                contentDescription = "Selected",
                                                tint = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                                // Typography Previews
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Aa Bb Cc 123",
                                        fontFamily = font.fontFamily,
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) currentTheme.primary else currentTheme.textPrimary
                                    )
                                    Text(
                                        text = "Focus • Study • Grow",
                                        fontFamily = font.fontFamily,
                                        fontSize = 15.sp,
                                        color = currentTheme.textSecondary
                                    )
                                }

                                // Description/Subtext
                                Text(
                                    text = font.description,
                                    fontFamily = font.fontFamily,
                                    fontSize = 13.sp,
                                    color = currentTheme.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
