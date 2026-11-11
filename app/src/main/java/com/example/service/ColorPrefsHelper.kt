package com.example.service

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toLongValue(): Long = (this.toArgb().toLong() and 0xFFFFFFFFL)

fun safeParseColorArgb(colorLong: Long?, fallbackArgb: Int = 0xFFF28F75.toInt()): Int {
    if (colorLong == null || colorLong == 0L) return fallbackArgb or -0x1000000
    return try {
        val upper32 = (colorLong ushr 32).toInt()
        val lower32 = (colorLong and 0xFFFFFFFFL).toInt()
        val rawArgb = when {
            upper32 == 0 || upper32 == -1 -> {
                if (lower32 == 0) fallbackArgb else lower32
            }
            lower32 in 0..63 -> {
                upper32
            }
            (colorLong and 0x3FL) == 0L -> {
                Color(colorLong.toULong()).toArgb()
            }
            else -> {
                upper32
            }
        }
        if (rawArgb == 0) {
            fallbackArgb or -0x1000000
        } else {
            rawArgb or -0x1000000
        }
    } catch (e: Throwable) {
        fallbackArgb or -0x1000000
    }
}

fun safeParseColor(colorLong: Long?, fallback: Color = Color(0xFFF28F75)): Color {
    val fallbackArgb = try {
        fallback.toArgb()
    } catch (e: Throwable) {
        0xFFF28F75.toInt()
    }
    return Color(safeParseColorArgb(colorLong, fallbackArgb))
}

fun Long.toColor(fallback: Color = Color(0xFFF28F75)): Color {
    return safeParseColor(this, fallback)
}

