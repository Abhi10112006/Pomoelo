package com.example.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontWeight
import com.example.R

// Local, crash-proof, 100% offline-first Comic Neue font family (Cute/Comic Sans alternative)
val ComicNeueFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.comic_neue_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.comic_neue_bold, weight = FontWeight.Bold, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Local, crash-proof, 100% offline-first Special Elite font family (Elegant/Typewriter)
val SpecialEliteFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.special_elite_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Monospace
}

// Local, crash-proof, 100% offline-first Poppins font family (Premium fallback)
val PoppinsFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.poppins_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.poppins_medium, weight = FontWeight.Medium, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.poppins_bold, weight = FontWeight.Bold, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.SansSerif
}

// Map the main references to these robust premium typography selections
val AppFontFamily = ComicNeueFontFamily
val CursiveFontFamily = ComicNeueFontFamily
val MonospaceFontFamily = SpecialEliteFontFamily




