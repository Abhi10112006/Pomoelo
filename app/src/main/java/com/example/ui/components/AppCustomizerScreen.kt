package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SettingsManager
import com.example.service.toColor
import com.example.service.toLongValue
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.PomoMotion
import com.example.ui.theme.ThemeOption
import com.example.ui.theme.luminance
import kotlinx.coroutines.delay

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
                val backInteraction = remember { MutableInteractionSource() }
                val isBackPressed by backInteraction.collectIsPressedAsState()
                val backScale by animateFloatAsState(
                    targetValue = if (isBackPressed) 0.92f else 1f,
                    animationSpec = PomoMotion.pressScaleSpec,
                    label = "backScale"
                )

                Surface(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .scale(backScale),
                    shape = CircleShape,
                    color = currentTheme.surface,
                    border = BorderStroke(1.dp, currentTheme.cardBorder),
                    interactionSource = backInteraction
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = currentTheme.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
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
                val resetInteraction = remember { MutableInteractionSource() }
                val isResetPressed by resetInteraction.collectIsPressedAsState()
                val resetScale by animateFloatAsState(
                    targetValue = if (isResetPressed) 0.92f else 1f,
                    animationSpec = PomoMotion.pressScaleSpec,
                    label = "resetScale"
                )

                Surface(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        if (SettingsManager.isCustomized()) {
                            showResetDialog = true
                        } else {
                            SettingsManager.resetToPomoPal()
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .scale(resetScale),
                    shape = CircleShape,
                    color = currentTheme.surface,
                    border = BorderStroke(1.dp, currentTheme.cardBorder),
                    interactionSource = resetInteraction
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Replay,
                            contentDescription = "Reset to PomoPal",
                            tint = currentTheme.textSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
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
                        val startBtnTextColor by animateColorAsState(
                            targetValue = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                            animationSpec = PomoMotion.colorSpec,
                            label = "startBtnTextColor"
                        )
                        Box(
                            modifier = Modifier
                                .background(currentTheme.primary, RoundedCornerShape(20.dp))
                                .padding(horizontal = 28.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "Start Focus",
                                color = startBtnTextColor,
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
            val isSystemDark = isSystemInDarkTheme()
            val themeState by SettingsManager.themeState.collectAsState()
            val customBg = themeState.background
            val customPri = themeState.primary
            val customSec = themeState.secondary
            val customAcc = themeState.accent

            // Helper function to handle appearance mode selection with automatic background adaptation
            val handleAppearanceSelect: (SettingsManager.AppearanceMode) -> Unit = { mode ->
                SettingsManager.setAppearanceMode(mode)
                val isTargetDark = when (mode) {
                    SettingsManager.AppearanceMode.DARK -> true
                    SettingsManager.AppearanceMode.LIGHT -> false
                    SettingsManager.AppearanceMode.SYSTEM -> isSystemDark
                }
                val currentBgColor = SettingsManager.getCustomBgColor()?.toColor() 
                    ?: if (isTargetDark) Color(0xFF1E1E1E) else Color(0xFFFAF9F6)
                val isCurrentlyDarkBg = currentBgColor.luminance() < 0.5f

                if (isTargetDark && !isCurrentlyDarkBg) {
                    // Current background is light -> automatically switch to dark Classic (0xFF1E1E1E)
                    SettingsManager.setCustomBgColor(Color(0xFF1E1E1E).toLongValue())
                } else if (!isTargetDark && isCurrentlyDarkBg) {
                    // Current background is dark -> automatically switch to light Ivory (0xFFFAF9F6)
                    SettingsManager.setCustomBgColor(Color(0xFFFAF9F6).toLongValue())
                }
            }

            CustomizationSection(
                title = "Appearance",
                subtitle = "System-wide theme setting",
                theme = currentTheme,
                staggerIndex = 0
            ) {
                AppearanceSegmentedPill(
                    currentMode = currentMode,
                    onSelect = handleAppearanceSelect
                )
            }

            // Section: Background Customization
            CustomizationSection(
                title = "Background Color",
                subtitle = "Main app canvas",
                theme = currentTheme,
                staggerIndex = 1
            ) {
                ColorPickerRow(
                    options = backgroundOptions,
                    selectedColor = customBg,
                    onColorSelected = { color -> 
                        SettingsManager.setCustomBgColor(color.toLongValue())
                        val isDarkColor = color.luminance() < 0.5f
                        if (isDarkColor) {
                            if (currentMode == SettingsManager.AppearanceMode.LIGHT || (currentMode == SettingsManager.AppearanceMode.SYSTEM && !isSystemDark)) {
                                SettingsManager.setAppearanceMode(SettingsManager.AppearanceMode.DARK)
                            }
                        } else {
                            if (currentMode == SettingsManager.AppearanceMode.DARK || (currentMode == SettingsManager.AppearanceMode.SYSTEM && isSystemDark)) {
                                SettingsManager.setAppearanceMode(SettingsManager.AppearanceMode.LIGHT)
                            }
                        }
                    }
                )
            }

            // Section: Primary
            CustomizationSection(
                title = "Primary",
                subtitle = "Buttons & main actions",
                theme = currentTheme,
                staggerIndex = 2
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
                theme = currentTheme,
                staggerIndex = 3
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
                theme = currentTheme,
                staggerIndex = 4
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
fun AppearanceSegmentedPill(
    currentMode: SettingsManager.AppearanceMode,
    onSelect: (SettingsManager.AppearanceMode) -> Unit
) {
    val theme = LocalAppTheme.current
    val view = LocalView.current
    val options = remember {
        listOf(
            "System" to SettingsManager.AppearanceMode.SYSTEM,
            "Light" to SettingsManager.AppearanceMode.LIGHT,
            "Dark" to SettingsManager.AppearanceMode.DARK
        )
    }
    val selectedIndex = options.indexOfFirst { it.second == currentMode }.coerceAtLeast(0)
    val onSelectedColor = if (theme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(theme.background)
            .border(1.dp, theme.cardBorder.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
            .padding(4.dp)
    ) {
        val segmentWidth = maxWidth / options.size
        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex,
            animationSpec = PomoMotion.expressiveSpatial(Dp.VisibilityThreshold),
            label = "appearancePillOffset"
        )

        Box(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(segmentWidth)
                    .fillMaxHeight()
                    .shadow(3.dp, RoundedCornerShape(10.dp), spotColor = theme.primary.copy(alpha = 0.4f))
                    .clip(RoundedCornerShape(10.dp))
                    .background(theme.primary)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                options.forEach { (label, mode) ->
                    val isSelected = mode == currentMode
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val pressScale by animateFloatAsState(
                        targetValue = if (isPressed) 0.94f else 1f,
                        animationSpec = PomoMotion.pressScaleSpec,
                        label = "appearanceItemScale"
                    )
                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) onSelectedColor else theme.textPrimary,
                        animationSpec = PomoMotion.colorSpec,
                        label = "appearanceTextColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .graphicsLayer {
                                scaleX = pressScale
                                scaleY = pressScale
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(
                                interactionSource = interactionSource,
                                indication = ripple(bounded = true),
                                role = Role.Tab,
                                onClick = {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                    onSelect(mode)
                                }
                            )
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = textColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
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
                val targetBorderWidth = if (isSelected) 3.dp else 1.dp
                val targetBorderColor = if (isSelected) currentTheme.primary else currentTheme.cardBorder
                val borderWidth by animateDpAsState(targetBorderWidth, PomoMotion.dpSpatial, label = "borderWidth")
                val borderColor by animateColorAsState(targetBorderColor, PomoMotion.colorSpec, label = "borderColor")
                val itemInteraction = remember { MutableInteractionSource() }
                val isItemPressed by itemInteraction.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isItemPressed) 0.90f else (if (isSelected) 1.08f else 1f),
                    animationSpec = PomoMotion.expressiveSpatial(),
                    label = "scale"
                )
                val elevation by animateDpAsState(
                    targetValue = if (isItemPressed) 1.dp else (if (isSelected) 6.dp else 1.dp),
                    animationSpec = PomoMotion.elevationSpec,
                    label = "elevation"
                )
                val iconTintColor by animateColorAsState(
                    if (color.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                    animationSpec = PomoMotion.colorSpec,
                    label = "iconTint"
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .size(48.dp)
                        .shadow(
                            elevation = elevation.coerceAtLeast(0.dp),
                            shape = CircleShape,
                            spotColor = if (isSelected) color else currentTheme.shadowColor
                        )
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = borderWidth,
                            color = borderColor,
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = itemInteraction,
                            indication = ripple(bounded = true, radius = 24.dp),
                            onClickLabel = "Select color",
                            role = Role.RadioButton,
                            onClick = { 
                                try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                onColorSelected(color) 
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isSelected,
                        enter = scaleIn(
                            initialScale = 0.4f,
                            animationSpec = PomoMotion.expressiveSpatial()
                        ) + fadeIn(PomoMotion.fastEffects()),
                        exit = scaleOut(
                            targetScale = 0.4f,
                            animationSpec = PomoMotion.fastSpatial()
                        ) + fadeOut(PomoMotion.fastEffects())
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected color",
                            tint = iconTintColor,
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
    staggerIndex: Int = 0,
    content: @Composable () -> Unit
) {
    val revealProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (staggerIndex > 0) delay(staggerIndex * 35L)
        revealProgress.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
    }

    Column(
        modifier = Modifier.graphicsLayer {
            val p = revealProgress.value
            alpha = p.coerceIn(0f, 1f)
            val s = 0.96f + (0.04f * p)
            scaleX = s
            scaleY = s
            translationX = (1f - p) * 16.dp.toPx()
        },
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
