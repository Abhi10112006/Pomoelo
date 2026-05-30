package com.example.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val PoppinsFont = GoogleFont("Poppins")
val ComicNeueFont = GoogleFont("Comic Neue")
val TypewriterFont = GoogleFont("Special Elite")

val AppFontFamily = FontFamily(
    Font(googleFont = PoppinsFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = PoppinsFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = PoppinsFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = PoppinsFont, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = PoppinsFont, fontProvider = provider, weight = FontWeight.Black)
)
val CursiveFontFamily = FontFamily(
    Font(googleFont = ComicNeueFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = ComicNeueFont, fontProvider = provider, weight = FontWeight.Bold)
)
val MonospaceFontFamily = FontFamily(
    Font(googleFont = TypewriterFont, fontProvider = provider, weight = FontWeight.Normal)
)
