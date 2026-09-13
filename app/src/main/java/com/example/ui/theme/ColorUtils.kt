package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun Color.luminance(): Float {
    return ColorUtils.calculateLuminance(this.toArgb()).toFloat()
}

fun Color.blend(other: Color, ratio: Float = 0.5f): Color {
    val r = this.red * (1f - ratio) + other.red * ratio
    val g = this.green * (1f - ratio) + other.green * ratio
    val b = this.blue * (1f - ratio) + other.blue * ratio
    val a = this.alpha * (1f - ratio) + other.alpha * ratio
    return Color(r, g, b, a)
}

fun Color.lighten(amount: Float = 0.1f): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    hsl[2] = (hsl[2] + amount).coerceIn(0f, 1f)
    return Color(ColorUtils.HSLToColor(hsl))
}

fun Color.darken(amount: Float = 0.1f): Color {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    hsl[2] = (hsl[2] - amount).coerceIn(0f, 1f)
    return Color(ColorUtils.HSLToColor(hsl))
}

fun ensureContrast(fg: Color, bg: Color, minContrast: Double = 3.0): Color {
    var fgArgb = fg.toArgb()
    val bgArgb = bg.toArgb()
    var contrast = ColorUtils.calculateContrast(fgArgb, bgArgb)
    if (contrast >= minContrast) return fg

    val isBgDark = ColorUtils.calculateLuminance(bgArgb) < 0.5
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(fgArgb, hsl)
    
    val step = if (isBgDark) 0.05f else -0.05f
    while (contrast < minContrast && hsl[2] >= 0f && hsl[2] <= 1f) {
        hsl[2] = (hsl[2] + step).coerceIn(0f, 1f)
        fgArgb = ColorUtils.HSLToColor(hsl)
        contrast = ColorUtils.calculateContrast(fgArgb, bgArgb)
        if ((isBgDark && hsl[2] >= 1f) || (!isBgDark && hsl[2] <= 0f)) break
    }
    return Color(fgArgb)
}
