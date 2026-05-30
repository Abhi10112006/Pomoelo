package com.example.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontWeight
import com.example.R

// Local, crash-proof, 100% offline-first Poppins font family
val PoppinsFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.poppins_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.poppins_medium, weight = FontWeight.Medium, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.poppins_bold, weight = FontWeight.Bold, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.SansSerif
}

// Local, crash-proof, 100% offline-first Comic Neue font family (for customized cursive text styles)
val ComicNeueFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.comic_neue_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal),
        Font(resId = R.font.comic_neue_bold, weight = FontWeight.Bold, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Cursive
}

// Local, crash-proof, 100% offline-first Special Elite font family (for logs, system countdown, retro timers)
val SpecialEliteFontFamily: FontFamily = try {
    FontFamily(
        Font(resId = R.font.special_elite_regular, weight = FontWeight.Normal, loadingStrategy = FontLoadingStrategy.OptionalLocal)
    )
} catch (e: Throwable) {
    FontFamily.Monospace
}

// Map the main references to these robust premium typography selections
val AppFontFamily: FontFamily = ComicNeueFontFamily
val CursiveFontFamily: FontFamily = ComicNeueFontFamily
val MonospaceFontFamily: FontFamily = SpecialEliteFontFamily




