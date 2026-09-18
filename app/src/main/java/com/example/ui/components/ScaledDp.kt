package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Int.scaledDp: Dp
    @Composable
    get() {
        val adaptiveScale = LocalAdaptiveDimensions.current.scale
        return (this * adaptiveScale).dp
    }

val Double.scaledDp: Dp
    @Composable
    get() {
        val adaptiveScale = LocalAdaptiveDimensions.current.scale
        return (this.toFloat() * adaptiveScale).dp
    }

val Float.scaledDp: Dp
    @Composable
    get() {
        val adaptiveScale = LocalAdaptiveDimensions.current.scale
        return (this * adaptiveScale).dp
    }
