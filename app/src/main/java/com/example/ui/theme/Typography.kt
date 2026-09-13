package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// Local, crash-proof, 100% offline-first Comic Neue font family (Comic Sans refined)
val ComicNeueFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.comic_neue_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.comic_neue_bold, weight = FontWeight.Bold, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Sniglet font family (Cute, rounded, bubbly handwriting)
val SnigletFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.sniglet_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Balsamiq Sans font family (Comic hand-drawn sketching style)
val BalsamiqSansFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.balsamiq_sans_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.balsamiq_sans_bold, weight = FontWeight.Bold, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Patrick Hand font family (Neat comic script handwriting)
val PatrickHandFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.patrick_hand_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Short Stack font family (Playful rounded geometric handwriting)
val ShortStackFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.short_stack_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Chewy font family (Bouncy cartoon candy font)
val ChewyFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.chewy_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Special Elite font family (Vintage typewriter)
val SpecialEliteFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.special_elite_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Monospace
}

// Poppins font family (Clean modern sans)
val PoppinsFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.poppins_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.poppins_medium, weight = FontWeight.Medium, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.poppins_bold, weight = FontWeight.Bold, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.SansSerif
}

enum class FontOption(
    val id: String,
    val displayName: String,
    val previewSample: String,
    val badge: String,
    val fontFamily: FontFamily
) {
    COMIC_NEUE("comic_neue", "Comic Neue", "Cute & bouncy Comic Sans vibe", "Popular", ComicNeueFontFamily),
    SNIGLET("sniglet", "Sniglet", "Soft, rounded & adorable letters", "Bubbly", SnigletFontFamily),
    BALSAMIQ("balsamiq", "Balsamiq Sans", "Hand-drawn playful comic notebook", "Comic", BalsamiqSansFontFamily),
    PATRICK_HAND("patrick_hand", "Patrick Hand", "Friendly and neat handwritten script", "Casual", PatrickHandFontFamily),
    SHORT_STACK("short_stack", "Short Stack", "Playful geometric curved strokes", "Playful", ShortStackFontFamily),
    CHEWY("chewy", "Chewy", "Chunky candy-shop cartoon bouncy", "Sweet", ChewyFontFamily);

    companion object {
        fun fromId(id: String?): FontOption {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: COMIC_NEUE
        }
    }
}

fun createAppTypography(fontFamily: FontFamily): Typography {
    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}

// Composition Local for the currently active font family
val LocalAppFont = compositionLocalOf { ComicNeueFontFamily }

// Backward compatibility references
val AppFontFamily get() = ComicNeueFontFamily
val CursiveFontFamily get() = ComicNeueFontFamily
val MonospaceFontFamily get() = SpecialEliteFontFamily




