package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min

val Int.scaledDp: Dp
    @Composable
    get() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val scaleFactor = min(max(screenWidth / 360f, 0.8f), 1.25f)
        return (this * scaleFactor).dp
    }

val Double.scaledDp: Dp
    @Composable
    get() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val scaleFactor = min(max(screenWidth / 360f, 0.8f), 1.25f)
        return (this.toFloat() * scaleFactor).dp
    }

val Float.scaledDp: Dp
    @Composable
    get() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val scaleFactor = min(max(screenWidth / 360f, 0.8f), 1.25f)
        return (this * scaleFactor).dp
    }
