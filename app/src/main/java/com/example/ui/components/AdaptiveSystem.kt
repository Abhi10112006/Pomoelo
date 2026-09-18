package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

data class AdaptiveDimensions(
    val scale: Float,
    val isVeryCompact: Boolean,
    val isCompact: Boolean,
    val effectiveHeight: Dp,
    val effectiveWidth: Dp
)

val LocalAdaptiveDimensions = compositionLocalOf { 
    AdaptiveDimensions(1f, false, false, 800.dp, 360.dp) 
}

@Composable
fun ProvideAdaptiveDimensions(
    availableWidth: Dp,
    availableHeight: Dp,
    hasBanner: Boolean,
    content: @Composable () -> Unit
) {
    // Banner consumes roughly 72dp vertically when visible (estimated)
    val effectiveHeight = availableHeight - if (hasBanner) 72.dp else 0.dp
    
    // Width scale based on a 360dp baseline
    val widthScale = min(1.0f, max(0.84f, availableWidth.value / 360f))
    
    // Height scale
    val heightScale = when {
        effectiveHeight < 640.dp -> 0.84f
        effectiveHeight < 740.dp -> 0.94f
        else -> 1.0f
    }
    
    // Global PomoPal scale is the tighter of the two constraints
    val finalScale = min(widthScale, heightScale)
    
    val adaptiveDimensions = AdaptiveDimensions(
        scale = finalScale,
        isVeryCompact = effectiveHeight < 640.dp,
        isCompact = effectiveHeight < 740.dp,
        effectiveHeight = effectiveHeight,
        effectiveWidth = availableWidth
    )
    
    CompositionLocalProvider(
        LocalAdaptiveDimensions provides adaptiveDimensions,
        content = content
    )
}
