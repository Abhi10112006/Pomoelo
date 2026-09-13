package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect

@Composable
fun PaperTextureOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // Draw very subtle diagonal lines to simulate a textured paper surface
        val step = 30f
        val maxDim = maxOf(width, height) * 2f
        val linePaintColor = Color.Black.copy(alpha = 0.02f)
        
        var i = 0f
        while (i < maxDim) {
            drawLine(
                color = linePaintColor,
                start = Offset(0f, i),
                end = Offset(i, 0f),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 15f), 0f)
            )
            i += step
        }
        
        var j = 0f
        while (j < maxDim) {
            drawLine(
                color = linePaintColor,
                start = Offset(j, height),
                end = Offset(width, height - (width - j)),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 15f), 5f)
            )
            j += step
        }
    }
}
