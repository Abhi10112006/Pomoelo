package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val Int.scaledSp: TextUnit
    @Composable
    get() {
        val adaptiveScale = LocalAdaptiveDimensions.current.scale
        return (this * adaptiveScale).sp
    }

val Double.scaledSp: TextUnit
    @Composable
    get() {
        val adaptiveScale = LocalAdaptiveDimensions.current.scale
        return (this.toFloat() * adaptiveScale).sp
    }

val Float.scaledSp: TextUnit
    @Composable
    get() {
        val adaptiveScale = LocalAdaptiveDimensions.current.scale
        return (this * adaptiveScale).sp
    }
