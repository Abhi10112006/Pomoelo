package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SettingsManager
import com.example.ui.theme.FontOption
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.luminance

@Composable
fun ThemeFontCustomizer(
    modifier: Modifier = Modifier
) {
    val currentTheme = LocalAppTheme.current
    val currentAppFont = LocalAppFont.current
    val currentFont by SettingsManager.fontState.collectAsState()
    val view = LocalView.current

    var showCustomizer by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = showCustomizer,
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> (fullWidth * 0.15f).toInt() },
                    animationSpec = tween(320, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(280, delayMillis = 40, easing = LinearOutSlowInEasing)
                )).togetherWith(
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -(fullWidth * 0.15f).toInt() },
                        animationSpec = tween(260, easing = FastOutSlowInEasing)
                    ) + fadeOut(
                        animationSpec = tween(200, easing = FastOutSlowInEasing)
                    )
                ).using(
                    SizeTransform(clip = false) { _, _ ->
                        tween(300, easing = FastOutSlowInEasing)
                    }
                )
            } else {
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> -(fullWidth * 0.15f).toInt() },
                    animationSpec = tween(320, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(280, delayMillis = 40, easing = LinearOutSlowInEasing)
                )).togetherWith(
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> (fullWidth * 0.15f).toInt() },
                        animationSpec = tween(260, easing = FastOutSlowInEasing)
                    ) + fadeOut(
                        animationSpec = tween(200, easing = FastOutSlowInEasing)
                    )
                ).using(
                    SizeTransform(clip = false) { _, _ ->
                        tween(300, easing = FastOutSlowInEasing)
                    }
                )
            }
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            role = Role.Button,
                            onClickLabel = "Open Make It Yours customizer",
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                showCustomizer = true
                            }
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                    border = BorderStroke(1.dp, currentTheme.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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

                // Section 2: Playful Fonts
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                    border = BorderStroke(1.dp, currentTheme.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                                    text = "Playful Fonts",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.textPrimary,
                                    fontFamily = currentAppFont
                                )
                                Text(
                                    text = "Cute typography tailored for high focus",
                                    fontSize = 12.sp,
                                    color = currentTheme.textSecondary,
                                    fontFamily = currentAppFont
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            FontOption.entries.forEach { font ->
                                val isSelected = currentFont.id == font.id
                                val borderColor by animateColorAsState(
                                    targetValue = if (isSelected) currentTheme.primary else currentTheme.cardBorder,
                                    label = "fontBorder"
                                )
                                val bgColor by animateColorAsState(
                                    targetValue = if (isSelected) {
                                        currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.18f else 0.08f)
                                    } else {
                                        currentTheme.backgroundSecondary
                                    },
                                    label = "fontBg"
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(
                                            elevation = if (isSelected) 4.dp else 0.dp,
                                            shape = RoundedCornerShape(20.dp),
                                            spotColor = if (isSelected) currentTheme.primary else currentTheme.shadowColor
                                        )
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(bgColor)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = borderColor,
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .clickable(
                                            role = Role.RadioButton,
                                            onClickLabel = "Select ${font.displayName} font",
                                            onClick = {
                                                try {
                                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                                } catch (e: Exception) {}
                                                SettingsManager.setFontId(font.id)
                                            }
                                        )
                                        .padding(18.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
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

                                            if (isSelected) {
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

                                        // Sample Preview using that font
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(if (isSelected) currentTheme.surface.copy(alpha = 0.85f) else currentTheme.surface)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (isSelected) currentTheme.primary.copy(alpha = 0.25f) else currentTheme.cardBorder.copy(alpha = 0.6f),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(horizontal = 14.dp, vertical = 12.dp)
                                        ) {
                                            Text(
                                                text = font.sampleText,
                                                fontFamily = font.fontFamily,
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                letterSpacing = 0.5.sp,
                                                color = if (isSelected) currentTheme.primary else currentTheme.textPrimary
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
    }
}
