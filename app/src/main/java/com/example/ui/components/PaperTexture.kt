package com.example.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.drawWithCache

@Composable
fun PaperTextureOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                // Highly optimized repeated shader pattern instead of thousands of dashed lines
                val textureBrush = Brush.linearGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.015f), 
                        Color.Transparent,
                        Color.White.copy(alpha = 0.015f),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(8f, 8f),
                    tileMode = TileMode.Repeated
                )
                
                val crossBrush = Brush.linearGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.01f), 
                        Color.Transparent
                    ),
                    start = Offset(8f, 0f),
                    end = Offset(0f, 8f),
                    tileMode = TileMode.Repeated
                )
                
                onDrawBehind {
                    drawRect(brush = textureBrush)
                    drawRect(brush = crossBrush)
                }
            }
    )
}
