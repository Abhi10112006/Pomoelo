package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SettingsManager
import com.example.service.toLongValue
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

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .background(currentTheme.surface, CircleShape)
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
                    SettingsManager.setCustomBgColor(null)
                    SettingsManager.setCustomPrimaryColor(null)
                    SettingsManager.setCustomSecondaryColor(null)
                    SettingsManager.setCustomAccentColor(null)
                },
                modifier = Modifier
                    .background(currentTheme.surface, CircleShape)
                    .border(1.dp, currentTheme.cardBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Replay,
                    contentDescription = "Reset",
                    tint = currentTheme.textSecondary
                )
            }
        }

        // Live Preview Area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = currentTheme.background),
            border = BorderStroke(1.dp, currentTheme.cardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Fake Timer Ring
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(4.dp, currentTheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "25:00",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textPrimary
                        )
                    }
                    
                    // Fake Pills
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(currentTheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Secondary", color = currentTheme.secondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .background(currentTheme.accent.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Accent", color = currentTheme.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Appearance Mode
        CustomizationSection(
            title = "Appearance",
            subtitle = "System-wide theme setting",
            theme = currentTheme
        ) {
            val currentMode by SettingsManager.appearanceState.collectAsState()
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
        CustomizationSection(
            title = "Background Color",
            subtitle = "Main app canvas",
            theme = currentTheme
        ) {
            ColorPickerRow(
                options = backgroundOptions,
                selectedColor = currentTheme.background,
                onColorSelected = { color -> SettingsManager.setCustomBgColor(color.toLongValue()) }
            )
        }

        // Section: Primary Accent
        CustomizationSection(
            title = "Primary Accent",
            subtitle = "Buttons and main highlights",
            theme = currentTheme
        ) {
            ColorPickerRow(
                options = primaryOptions,
                selectedColor = currentTheme.primary,
                onColorSelected = { color -> SettingsManager.setCustomPrimaryColor(color.toLongValue()) }
            )
        }

        // Section: Secondary
        CustomizationSection(
            title = "Secondary Details",
            subtitle = "Supporting highlights and shapes",
            theme = currentTheme
        ) {
            ColorPickerRow(
                options = secondaryOptions,
                selectedColor = currentTheme.secondary,
                onColorSelected = { color -> SettingsManager.setCustomSecondaryColor(color.toLongValue()) }
            )
        }
        
        // Section: Accent
        CustomizationSection(
            title = "Accent Details",
            subtitle = "Tertiary highlights and shapes",
            theme = currentTheme
        ) {
            ColorPickerRow(
                options = accentOptions,
                selectedColor = currentTheme.accent,
                onColorSelected = { color -> SettingsManager.setCustomAccentColor(color.toLongValue()) }
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
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
    val bgColor by animateColorAsState(if (isSelected) theme.primary else theme.surface, label = "bg")
    val textColor by animateColorAsState(if (isSelected) Color.White else theme.textPrimary, label = "text")
    val borderColor by animateColorAsState(if (isSelected) theme.primary else theme.cardBorder, label = "border")

    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { SettingsManager.setAppearanceMode(mode) }
            .padding(vertical = 12.dp),
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { color ->
            val isSelected = selectedColor == color
            val animatedScale by androidx.compose.animation.core.animateFloatAsState(
                targetValue = if (isSelected) 1.2f else 1f,
                label = "scale"
            )
            
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(color) },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = if (color.luminance() > 0.5f) Color.Black else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
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
