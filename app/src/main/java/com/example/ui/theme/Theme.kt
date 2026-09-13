package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

enum class ThemeOption(
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
    PREMIUM(
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
    ),
    CANDY(
        id = "candy",
        displayName = "Candy Sweet",
        emoji = "🍬",
        tagline = "Strawberry cream & warm peach blush",
        primary = Color(0xFFFF7043),
        primaryDark = Color(0xFFE64A19),
        primaryLight = Color(0xFFFF8A80),
        secondary = Color(0xFFFFB74D),
        accent = Color(0xFFFFAB91),
        background = Color(0xFFFFF0EC),
        backgroundSecondary = Color(0xFFFCE4EC),
        surface = Color(0xFFFFFFFF),
        cardBorder = Color(0xFFFFD180).copy(alpha = 0.45f),
        textPrimary = Color(0xFF5D4037),
        textSecondary = Color(0xFF8D6E63),
        timerArcColors = listOf(Color(0xFFFF7043), Color(0xFFFF8A80), Color(0xFFFFCC80)),
        runningBg = Color(0xFFFFF0EC),
        breakBg = Color(0xFFE8F5E9),
        pillActiveBg = Color(0xFFFFEBEE),
        shadowColor = Color(0xFFFF7043).copy(alpha = 0.35f),
        isDark = false
    ),
    PASTEL(
        id = "pastel",
        displayName = "Pastel Dream",
        emoji = "🌸",
        tagline = "Soft lavender mist & fresh pastel mint",
        primary = Color(0xFF9575CD),
        primaryDark = Color(0xFF7E57C2),
        primaryLight = Color(0xFFB39DDB),
        secondary = Color(0xFF80CBC4),
        accent = Color(0xFF4DB6AC),
        background = Color(0xFFF8F4FA),
        backgroundSecondary = Color(0xFFEDE7F6),
        surface = Color(0xFFFFFFFF),
        cardBorder = Color(0xFFCE93D8).copy(alpha = 0.45f),
        textPrimary = Color(0xFF4527A0),
        textSecondary = Color(0xFF7E57C2),
        timerArcColors = listOf(Color(0xFF9575CD), Color(0xFFB39DDB), Color(0xFF80CBC4)),
        runningBg = Color(0xFFF3E5F5),
        breakBg = Color(0xFFE0F2F1),
        pillActiveBg = Color(0xFFEDE7F6),
        shadowColor = Color(0xFF9575CD).copy(alpha = 0.35f),
        isDark = false
    ),
    GALAXY(
        id = "galaxy",
        displayName = "Galaxy Starlight",
        emoji = "✨",
        tagline = "Cute cosmic neon starlight & nebula indigo",
        primary = Color(0xFFD47AFF),
        primaryDark = Color(0xFFAB47BC),
        primaryLight = Color(0xFFE1BEE7),
        secondary = Color(0xFF64FFDA),
        accent = Color(0xFF80D8FF),
        background = Color(0xFF19162B),
        backgroundSecondary = Color(0xFF231D3C),
        surface = Color(0xFF282245),
        cardBorder = Color(0xFFB388FF).copy(alpha = 0.35f),
        textPrimary = Color(0xFFF5EEFF),
        textSecondary = Color(0xFFB39DDB),
        timerArcColors = listOf(Color(0xFFD47AFF), Color(0xFFB388FF), Color(0xFF64FFDA)),
        runningBg = Color(0xFF221B3B),
        breakBg = Color(0xFF162933),
        pillActiveBg = Color(0xFF382F5E),
        shadowColor = Color(0xFFD47AFF).copy(alpha = 0.45f),
        isDark = true
    ),
    MATCHA(
        id = "matcha",
        displayName = "Matcha & Honey",
        emoji = "🍵",
        tagline = "Soothing matcha tea with sweet golden honey",
        primary = Color(0xFF66BB6A),
        primaryDark = Color(0xFF43A047),
        primaryLight = Color(0xFFA5D6A7),
        secondary = Color(0xFFFFA000),
        accent = Color(0xFFFFB300),
        background = Color(0xFFF1F8E9),
        backgroundSecondary = Color(0xFFE8F5E9),
        surface = Color(0xFFFFFFFF),
        cardBorder = Color(0xFFA5D6A7).copy(alpha = 0.5f),
        textPrimary = Color(0xFF2E4732),
        textSecondary = Color(0xFF558B2F),
        timerArcColors = listOf(Color(0xFF66BB6A), Color(0xFFA5D6A7), Color(0xFFFFB300)),
        runningBg = Color(0xFFF1F8E9),
        breakBg = Color(0xFFFFF8E1),
        pillActiveBg = Color(0xFFE8F5E9),
        shadowColor = Color(0xFF66BB6A).copy(alpha = 0.35f),
        isDark = false
    ),
    OCEAN(
        id = "ocean",
        displayName = "Ocean Breeze",
        emoji = "🌊",
        tagline = "Sky aqua waters with sunny coral peach",
        primary = Color(0xFF29B6F6),
        primaryDark = Color(0xFF0288D1),
        primaryLight = Color(0xFF81D4FA),
        secondary = Color(0xFFFF8A65),
        accent = Color(0xFF4DD0E1),
        background = Color(0xFFE1F5FE),
        backgroundSecondary = Color(0xFFE0F2F1),
        surface = Color(0xFFFFFFFF),
        cardBorder = Color(0xFF81D4FA).copy(alpha = 0.5f),
        textPrimary = Color(0xFF1A365D),
        textSecondary = Color(0xFF0277BD),
        timerArcColors = listOf(Color(0xFF29B6F6), Color(0xFF4DD0E1), Color(0xFFFF8A65)),
        runningBg = Color(0xFFE1F5FE),
        breakBg = Color(0xFFFFF3E0),
        pillActiveBg = Color(0xFFE0F7FA),
        shadowColor = Color(0xFF29B6F6).copy(alpha = 0.35f),
        isDark = false
    ),
    SAKURA(
        id = "sakura",
        displayName = "Sakura Blossom",
        emoji = "🌸",
        tagline = "Delicate spring cherry blossom & sweet cream",
        primary = Color(0xFFF06292),
        primaryDark = Color(0xFFD81B60),
        primaryLight = Color(0xFFF48FB1),
        secondary = Color(0xFFFFD54F),
        accent = Color(0xFFFF80AB),
        background = Color(0xFFFFF0F5),
        backgroundSecondary = Color(0xFFFCE4EC),
        surface = Color(0xFFFFFFFF),
        cardBorder = Color(0xFFF48FB1).copy(alpha = 0.45f),
        textPrimary = Color(0xFF4A154B),
        textSecondary = Color(0xFF880E4F),
        timerArcColors = listOf(Color(0xFFF06292), Color(0xFFF48FB1), Color(0xFFFFD54F)),
        runningBg = Color(0xFFFFF0F5),
        breakBg = Color(0xFFE8F5E9),
        pillActiveBg = Color(0xFFFCE4EC),
        shadowColor = Color(0xFFF06292).copy(alpha = 0.35f),
        isDark = false
    );

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
        fun fromId(id: String?): ThemeOption {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: PREMIUM
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
    val colorScheme = themeOption.toColorScheme()
    val typography = createAppTypography(fontOption.fontFamily)

    CompositionLocalProvider(
        LocalAppTheme provides themeOption,
        LocalAppFont provides fontOption.fontFamily
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
