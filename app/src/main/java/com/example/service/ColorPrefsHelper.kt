package com.example.service

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toLongValue(): Long = (this.toArgb().toLong() and 0xFFFFFFFFL)

fun Long.toColor(): Color {
    val upper32 = (this ushr 32).toInt()
    val lower32 = (this and 0xFFFFFFFFL).toInt()
    val argb = if (upper32 != 0 && lower32 in 0..60) {
        upper32
    } else {
        lower32
    }
    val safeArgb = if ((argb and -0x1000000) == 0 && argb != 0) {
        argb or -0x1000000
    } else {
        argb
    }
    return Color(safeArgb)
}
