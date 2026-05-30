package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

val Int.scaledSp: TextUnit
    @Composable
    get() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        // Scale proportionally relative to a standard 360dp width screen.
        // Cap the scaling factor to avoid excessively huge/tiny text.
        val scaleFactor = min(max(screenWidth / 360f, 0.8f), 1.4f)
        return (this * scaleFactor).sp
    }

val Double.scaledSp: TextUnit
    @Composable
    get() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val scaleFactor = min(max(screenWidth / 360f, 0.8f), 1.4f)
        return (this.toFloat() * scaleFactor).sp
    }

val Float.scaledSp: TextUnit
    @Composable
    get() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val scaleFactor = min(max(screenWidth / 360f, 0.8f), 1.4f)
        return (this * scaleFactor).sp
    }
