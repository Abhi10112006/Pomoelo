package com.example.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.service.SettingsManager

data class ThemeOption(
    val id: String,
    val displayName: String,
    val emoji: String,
    val tagline: String,
    val primary: Color,
    val primaryDark: Color,
    val primaryLight: Color,
    val secondary: Color,
    val accent: Color,
    val background: Color,
    val backgroundSecondary: Color,
    val surface: Color,
    val cardBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val timerArcColors: List<Color>,
    val runningBg: Color,
    val breakBg: Color,
    val pillActiveBg: Color,
    val shadowColor: Color,
    val isDark: Boolean = false
) {
    fun toColorScheme(): ColorScheme {
        return if (isDark) {
            darkColorScheme(
                primary = primary,
                onPrimary = Color.Black,
                primaryContainer = surface,
                onPrimaryContainer = textPrimary,
                secondary = secondary,
                onSecondary = Color.Black,
                background = background,
                onBackground = textPrimary,
                surface = surface,
                onSurface = textPrimary
            )
        } else {
            lightColorScheme(
                primary = primary,
                onPrimary = Color.White,
                primaryContainer = primaryLight,
                onPrimaryContainer = textPrimary,
                secondary = secondary,
                onSecondary = Color.White,
                background = background,
                onBackground = textPrimary,
                surface = surface,
                onSurface = textPrimary
            )
        }
    }

    companion object {
        val PREMIUM = ThemeOption(
            id = "premium",
            displayName = "Premium Ivory",
            emoji = "✨",
            tagline = "Warm cream canvas & coral accent",
            primary = Color(0xFFF28F75), // Coral/orange primary
            primaryDark = Color(0xFFD36F57),
            primaryLight = Color(0xFFFAD7CF),
            secondary = Color(0xFF8BB5CA), // Sky blue secondary
            accent = Color(0xFF9EAC95), // Muted green
            background = Color(0xFFFAF9F6), // Warm cream/ivory
            backgroundSecondary = Color(0xFFF3EFE9),
            surface = Color(0xFFFFFFFF),
            cardBorder = Color(0xFFE5DCD0),
            textPrimary = Color(0xFF4A3F35), // Deep warm-brown text
            textSecondary = Color(0xFF8A7D71),
            timerArcColors = listOf(Color(0xFFF28F75), Color(0xFF8BB5CA), Color(0xFF9EAC95)),
            runningBg = Color(0xFFFAF9F6),
            breakBg = Color(0xFFE9F2F0),
            pillActiveBg = Color(0xFFFCEBE7),
            shadowColor = Color(0xFFD4C9BD).copy(alpha = 0.5f),
            isDark = false
        )

        fun fromId(id: String?): ThemeOption {
            return PREMIUM
        }
    }
}

val LocalAppTheme = compositionLocalOf { ThemeOption.PREMIUM }

@Composable
fun MyApplicationTheme(
    themeOption: ThemeOption = ThemeOption.PREMIUM,
    fontOption: FontOption = FontOption.COMIC_NEUE,
    content: @Composable () -> Unit
) {
    val appearanceMode by SettingsManager.appearanceState.collectAsState()
    val isSystemDark = isSystemInDarkTheme()
    val isAppearanceDark = when (appearanceMode) {
        SettingsManager.AppearanceMode.DARK -> true
        SettingsManager.AppearanceMode.LIGHT -> false
        SettingsManager.AppearanceMode.SYSTEM -> isSystemDark
    }

    val hasCustomBg = SettingsManager.getCustomBgColor() != null
    val baseBackground = if (hasCustomBg) {
        themeOption.background
    } else {
        if (isAppearanceDark) Color(0xFF161514) else ThemeOption.PREMIUM.background
    }
    
    val bgLuminance = baseBackground.luminance()
    val isBgDark = bgLuminance < 0.5f

    // Derive text contrast strictly from actual active background luminance
    val baseTextPri = if (isBgDark) Color(0xFFF7F7F7) else Color(0xFF26201D)
    val baseTextSec = if (isBgDark) Color(0xFFB4B4B4) else Color(0xFF6B5F58)
    
    val surface = if (isBgDark) {
        baseBackground.lighten(0.08f)
    } else {
        Color.White.blend(baseBackground, 0.25f)
    }
    
    val textPri = ensureContrast(baseTextPri, surface, minContrast = 4.5)
    val textSec = ensureContrast(baseTextSec, surface, minContrast = 3.0)
    
    val border = if (isBgDark) baseBackground.lighten(0.18f) else baseBackground.darken(0.12f)
    val bgSecondary = if (isBgDark) baseBackground.lighten(0.04f) else baseBackground.darken(0.06f)

    val activePri = ensureContrast(themeOption.primary, baseBackground, minContrast = 3.0)
    val activeSec = ensureContrast(themeOption.secondary, baseBackground, minContrast = 3.0)
    val activeAcc = ensureContrast(themeOption.accent, baseBackground, minContrast = 3.0)
    
    val priDark = activePri.darken(0.15f)
    val priLight = activePri.lighten(0.15f)
    
    val pillActive = if (isBgDark) activePri.copy(alpha = 0.28f) else activePri.copy(alpha = 0.15f)
    val shadow = if (isBgDark) Color.Black.copy(alpha = 0.5f) else Color(0xFF4A3F35).copy(alpha = 0.15f)
    val breakBg = if (isBgDark) baseBackground.blend(activeSec, 0.12f) else baseBackground.blend(activeSec, 0.10f)

    val safeTheme = themeOption.copy(
        background = baseBackground,
        runningBg = baseBackground,
        breakBg = breakBg,
        surface = surface,
        cardBorder = border,
        backgroundSecondary = bgSecondary,
        pillActiveBg = pillActive,
        textPrimary = textPri,
        textSecondary = textSec,
        primary = activePri,
        primaryDark = priDark,
        primaryLight = priLight,
        secondary = activeSec,
        accent = activeAcc,
        timerArcColors = listOf(activePri, activeSec, activeAcc),
        shadowColor = shadow,
        isDark = isBgDark
    )

    val duration = 400
    val animBg by animateColorAsState(safeTheme.background, tween(duration), label = "bg")
    val animPri by animateColorAsState(safeTheme.primary, tween(duration), label = "pri")
    val animPriDark by animateColorAsState(safeTheme.primaryDark, tween(duration), label = "priDark")
    val animPriLight by animateColorAsState(safeTheme.primaryLight, tween(duration), label = "priLight")
    val animSec by animateColorAsState(safeTheme.secondary, tween(duration), label = "sec")
    val animAcc by animateColorAsState(safeTheme.accent, tween(duration), label = "acc")
    val animRunBg by animateColorAsState(safeTheme.runningBg, tween(duration), label = "runBg")
    val animBreakBg by animateColorAsState(safeTheme.breakBg, tween(duration), label = "breakBg")
    val animSurface by animateColorAsState(safeTheme.surface, tween(duration), label = "surface")
    val animBorder by animateColorAsState(safeTheme.cardBorder, tween(duration), label = "border")
    val animBgSec by animateColorAsState(safeTheme.backgroundSecondary, tween(duration), label = "bgSec")
    val animPillActiveBg by animateColorAsState(safeTheme.pillActiveBg, tween(duration), label = "pillActiveBg")
    val animTextPri by animateColorAsState(safeTheme.textPrimary, tween(duration), label = "textPri")
    val animTextSec by animateColorAsState(safeTheme.textSecondary, tween(duration), label = "textSec")
    val animShadow by animateColorAsState(safeTheme.shadowColor, tween(duration), label = "shadow")

    val animatedThemeOption = safeTheme.copy(
        background = animBg,
        primary = animPri,
        primaryDark = animPriDark,
        primaryLight = animPriLight,
        secondary = animSec,
        accent = animAcc,
        runningBg = animRunBg,
        breakBg = animBreakBg,
        surface = animSurface,
        cardBorder = animBorder,
        backgroundSecondary = animBgSec,
        pillActiveBg = animPillActiveBg,
        textPrimary = animTextPri,
        textSecondary = animTextSec,
        shadowColor = animShadow,
        timerArcColors = listOf(animPri, animSec, animAcc)
    )

    val colorScheme = animatedThemeOption.toColorScheme()
    val typography = createAppTypography(fontOption.fontFamily)

    CompositionLocalProvider(
        LocalAppTheme provides animatedThemeOption,
        LocalAppFont provides fontOption.fontFamily
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
