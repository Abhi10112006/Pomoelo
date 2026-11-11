package com.example.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun Color.luminance(): Float {
    return try {
        ColorUtils.calculateLuminance(this.toArgb() or -0x1000000).toFloat()
    } catch (e: Throwable) {
        0.5f
    }
}

fun Color.blend(other: Color, ratio: Float = 0.5f): Color {
    val r = (this.red * (1f - ratio) + other.red * ratio).coerceIn(0f, 1f)
    val g = (this.green * (1f - ratio) + other.green * ratio).coerceIn(0f, 1f)
    val b = (this.blue * (1f - ratio) + other.blue * ratio).coerceIn(0f, 1f)
    val a = (this.alpha * (1f - ratio) + other.alpha * ratio).coerceIn(0f, 1f)
    return Color(r, g, b, a)
}

fun Color.lighten(amount: Float = 0.1f): Color {
    return try {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(this.toArgb() or -0x1000000, hsl)
        hsl[2] = (hsl[2] + amount).coerceIn(0f, 1f)
        Color(ColorUtils.HSLToColor(hsl) or -0x1000000)
    } catch (e: Throwable) {
        this
    }
}

fun Color.darken(amount: Float = 0.1f): Color {
    return try {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(this.toArgb() or -0x1000000, hsl)
        hsl[2] = (hsl[2] - amount).coerceIn(0f, 1f)
        Color(ColorUtils.HSLToColor(hsl) or -0x1000000)
    } catch (e: Throwable) {
        this
    }
}

fun ensureContrast(fg: Color, bg: Color, minContrast: Double = 3.0): Color {
    return try {
        var fgArgb = fg.toArgb() or -0x1000000
        val bgArgb = bg.toArgb() or -0x1000000
        var contrast = ColorUtils.calculateContrast(fgArgb, bgArgb)
        if (contrast >= minContrast) return Color(fgArgb)

        val isBgDark = ColorUtils.calculateLuminance(bgArgb) < 0.5
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(fgArgb, hsl)

        val step = if (isBgDark) 0.05f else -0.05f
        var iterations = 0
        while (contrast < minContrast && hsl[2] in 0f..1f && iterations < 25) {
            hsl[2] = (hsl[2] + step).coerceIn(0f, 1f)
            fgArgb = ColorUtils.HSLToColor(hsl) or -0x1000000
            contrast = ColorUtils.calculateContrast(fgArgb, bgArgb)
            iterations++
            if ((isBgDark && hsl[2] >= 1f) || (!isBgDark && hsl[2] <= 0f)) break
        }
        Color(fgArgb)
    } catch (e: Throwable) {
        fg
    }
}

