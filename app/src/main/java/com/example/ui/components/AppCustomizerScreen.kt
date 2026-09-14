package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SettingsManager
import com.example.service.toColor
import com.example.service.toLongValue
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.ThemeOption
import com.example.ui.theme.luminance

val backgroundOptions = listOf(
    Color(0xFFFAF9F6), // Default Ivory
    Color(0xFFFFF0EC), // Peach
    Color(0xFFF8F4FA), // Lavender
    Color(0xFFF1F8E9), // Matcha
    Color(0xFFE1F5FE), // Ocean
    Color(0xFFFFF0F5), // Sakura
    Color(0xFF231D3C), // Dark Galaxy
    Color(0xFF1E1E1E), // Dark Classic
)

val primaryOptions = listOf(
    Color(0xFFF28F75), // Default Coral
    Color(0xFFFF7043), // Candy Orange
    Color(0xFF9575CD), // Purple
    Color(0xFF66BB6A), // Green
    Color(0xFF29B6F6), // Blue
    Color(0xFFF06292), // Pink
    Color(0xFFD47AFF), // Neon Purple
)

val secondaryOptions = listOf(
    Color(0xFF8BB5CA), // Default Sky Blue
    Color(0xFFFFB74D), // Candy Yellow
    Color(0xFF80CBC4), // Teal
    Color(0xFFFFA000), // Amber
    Color(0xFFFF8A65), // Coral
    Color(0xFFFFD54F), // Yellow
    Color(0xFF64FFDA), // Neon Teal
)

val accentOptions = listOf(
    Color(0xFF9EAC95), // Default Sage
    Color(0xFFFFAB91), // Candy Peach
    Color(0xFF4DB6AC), // Teal
    Color(0xFFFFB300), // Amber
    Color(0xFF4DD0E1), // Cyan
    Color(0xFFFF80AB), // Pink
    Color(0xFF80D8FF), // Light Blue
)

@Composable
fun AppCustomizerScreen(
    onNavigateBack: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentAppFont = LocalAppFont.current

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = currentAppFont)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            val view = LocalView.current
            var showResetDialog by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(currentTheme.surface)
                        .border(1.dp, currentTheme.cardBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = currentTheme.textPrimary
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "Make It Yours",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Reset Button
                IconButton(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        if (SettingsManager.isCustomized()) {
                            showResetDialog = true
                        } else {
                            SettingsManager.resetToPomoPal()
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(currentTheme.surface)
                        .border(1.dp, currentTheme.cardBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Replay,
                        contentDescription = "Reset to PomoPal",
                        tint = currentTheme.textSecondary
                    )
                }
            }

            // Live Preview Area
            val infiniteTransition = rememberInfiniteTransition(label = "preview_pulse")
            val progress by infiniteTransition.animateFloat(
                initialValue = 0.75f,
                targetValue = 0.95f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "progress"
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.background),
                border = BorderStroke(1.dp, currentTheme.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Focus Session",
                            color = currentTheme.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        
                        // Animated Timer Arc
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { 1f },
                                modifier = Modifier.size(110.dp),
                                color = currentTheme.primary.copy(alpha = 0.15f),
                                strokeWidth = 8.dp,
                                strokeCap = StrokeCap.Round
                            )
                            CircularProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.size(110.dp),
                                color = currentTheme.primary,
                                strokeWidth = 8.dp,
                                strokeCap = StrokeCap.Round
                            )
                            Text(
                                text = "25:00",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary
                            )
                        }
                        
                        // Start Button
                        Box(
                            modifier = Modifier
                                .background(currentTheme.primary, RoundedCornerShape(20.dp))
                                .padding(horizontal = 28.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "Start Focus",
                                color = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        
                        // Mini Task Card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .background(currentTheme.surface, RoundedCornerShape(12.dp))
                                .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(16.dp).border(2.dp, currentTheme.secondary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Deep Work Phase", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = currentTheme.textPrimary)
                                Text("Productivity", fontSize = 9.sp, color = currentTheme.accent)
                            }
                        }
                    }
                }
            }

            // Section: Appearance Mode
            val currentMode by SettingsManager.appearanceState.collectAsState()
            
            CustomizationSection(
                title = "Appearance",
                subtitle = "System-wide theme setting",
                theme = currentTheme
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppearanceOption("System", SettingsManager.AppearanceMode.SYSTEM, currentMode)
                    AppearanceOption("Light", SettingsManager.AppearanceMode.LIGHT, currentMode)
                    AppearanceOption("Dark", SettingsManager.AppearanceMode.DARK, currentMode)
                }
            }

            // Section: Background Customization
            val customBg = SettingsManager.getCustomBgColor()?.toColor() ?: if (currentTheme.isDark) Color(0xFF1E1E1E) else ThemeOption.PREMIUM.background
            val customPri = SettingsManager.getCustomPrimaryColor()?.toColor() ?: ThemeOption.PREMIUM.primary
            val customSec = SettingsManager.getCustomSecondaryColor()?.toColor() ?: ThemeOption.PREMIUM.secondary
            val customAcc = SettingsManager.getCustomAccentColor()?.toColor() ?: ThemeOption.PREMIUM.accent

            CustomizationSection(
                title = "Background Color",
                subtitle = "Main app canvas",
                theme = currentTheme
            ) {
                ColorPickerRow(
                    options = backgroundOptions,
                    selectedColor = customBg,
                    onColorSelected = { color -> 
                        SettingsManager.setCustomBgColor(color.toLongValue())
                        val isDarkColor = color.luminance() < 0.5f
                        if (isDarkColor && currentMode == SettingsManager.AppearanceMode.LIGHT) {
                            SettingsManager.setAppearanceMode(SettingsManager.AppearanceMode.DARK)
                        } else if (!isDarkColor && currentMode == SettingsManager.AppearanceMode.DARK) {
                            SettingsManager.setAppearanceMode(SettingsManager.AppearanceMode.LIGHT)
                        }
                    }
                )
            }

            // Section: Primary
            CustomizationSection(
                title = "Primary",
                subtitle = "Buttons & main actions",
                theme = currentTheme
            ) {
                ColorPickerRow(
                    options = primaryOptions,
                    selectedColor = customPri,
                    onColorSelected = { color -> SettingsManager.setCustomPrimaryColor(color.toLongValue()) }
                )
            }

            // Section: Secondary
            CustomizationSection(
                title = "Secondary",
                subtitle = "Supporting elements",
                theme = currentTheme
            ) {
                ColorPickerRow(
                    options = secondaryOptions,
                    selectedColor = customSec,
                    onColorSelected = { color -> SettingsManager.setCustomSecondaryColor(color.toLongValue()) }
                )
            }
            
            // Section: Accent
            CustomizationSection(
                title = "Accent",
                subtitle = "Small highlights & details",
                theme = currentTheme
            ) {
                ColorPickerRow(
                    options = accentOptions,
                    selectedColor = customAcc,
                    onColorSelected = { color -> SettingsManager.setCustomAccentColor(color.toLongValue()) }
                )
            }

            if (showResetDialog) {
                AlertDialog(
                    onDismissRequest = { showResetDialog = false },
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "↺",
                                fontSize = 20.sp,
                                color = currentTheme.primary
                            )
                            Text(
                                text = "Reset to PomoPal",
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary
                            )
                        }
                    },
                    text = {
                        Text(
                            text = "Restore default colors, appearance, and visual settings to the original PomoPal Signature design? Your selected font will be kept.",
                            color = currentTheme.textSecondary,
                            fontSize = 14.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                                } catch (e: Exception) {}
                                SettingsManager.resetToPomoPal()
                                showResetDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentTheme.primary,
                                contentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Reset",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showResetDialog = false }
                        ) {
                            Text(
                                text = "Cancel",
                                color = currentTheme.textSecondary
                            )
                        }
                    },
                    containerColor = currentTheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RowScope.AppearanceOption(
    label: String,
    mode: SettingsManager.AppearanceMode,
    currentMode: SettingsManager.AppearanceMode
) {
    val theme = LocalAppTheme.current
    val isSelected = mode == currentMode
    val onSelectedColor = if (theme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
    val bgColor by animateColorAsState(if (isSelected) theme.primary else theme.surface, label = "bg")
    val textColor by animateColorAsState(if (isSelected) onSelectedColor else theme.textPrimary, label = "text")
    val borderColor by animateColorAsState(if (isSelected) theme.primary else theme.cardBorder, label = "border")

    val view = LocalView.current
    Box(
        modifier = Modifier
            .weight(1f)
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { 
                try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                SettingsManager.setAppearanceMode(mode) 
            }
            .semantics { role = Role.RadioButton }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ColorPickerRow(
    options: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            options.forEach { color ->
                val isSelected = selectedColor.toArgb() == color.toArgb()
                
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = if (isSelected) 4.dp else 1.dp,
                            shape = CircleShape,
                            spotColor = if (isSelected) color else currentTheme.shadowColor
                        )
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) currentTheme.primary else currentTheme.cardBorder,
                            shape = CircleShape
                        )
                        .clickable(
                            onClickLabel = "Select color",
                            role = Role.RadioButton,
                            onClick = { 
                                try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                onColorSelected(color) 
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected color",
                            tint = if (color.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            // Add extra space at the end to ensure the last item is not covered by the gradient when scrolled
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Gradient fade on the right edge
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(48.dp)
                .height(48.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            currentTheme.surface.copy(alpha = 0.8f),
                            currentTheme.surface
                        )
                    )
                )
        )
    }
}

@Composable
fun CustomizationSection(
    title: String,
    subtitle: String,
    theme: ThemeOption,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = theme.textPrimary
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = theme.textSecondary
            )
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = theme.surface),
            border = BorderStroke(1.dp, theme.cardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}

