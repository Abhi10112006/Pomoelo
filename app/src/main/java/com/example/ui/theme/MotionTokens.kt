package com.example.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Unified PomoPal Motion System
 * Provides clean, minimal, smooth, non-overshooting animations for logical continuity
 * across Home, History, Settings, Customizer, and Dialogs.
 */
object PomoMotion {
    const val DURATION_FAST = 180
    const val DURATION_STANDARD = 240
    const val DURATION_MEDIUM = 280
    const val DURATION_SLOW = 380

    val SmoothOutEasing: Easing = CubicBezierEasing(0.22f, 1.0f, 0.36f, 1.0f)
    val SmoothInOutEasing: Easing = FastOutSlowInEasing
    val StandardEasing: Easing = FastOutSlowInEasing

    fun <T> smoothSpring(stiffness: Float = Spring.StiffnessMediumLow): SpringSpec<T> =
        spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = stiffness)

    fun <T> gentleSpring(stiffness: Float = Spring.StiffnessLow): SpringSpec<T> =
        spring(dampingRatio = 0.85f, stiffness = stiffness)

    val pressScaleSpec: FiniteAnimationSpec<Float> =
        spring(dampingRatio = 0.82f, stiffness = Spring.StiffnessMedium)

    val elevationSpec: FiniteAnimationSpec<Dp> =
        tween(durationMillis = DURATION_FAST, easing = SmoothOutEasing)

    val colorSpec: FiniteAnimationSpec<Color> =
        tween(durationMillis = DURATION_MEDIUM, easing = SmoothOutEasing)

    val dpSpec: FiniteAnimationSpec<Dp> =
        tween(durationMillis = DURATION_MEDIUM, easing = SmoothOutEasing)

    val floatSpec: FiniteAnimationSpec<Float> =
        tween(durationMillis = DURATION_MEDIUM, easing = SmoothOutEasing)

    val expandSection: EnterTransition =
        expandVertically(
            animationSpec = tween(DURATION_MEDIUM, easing = SmoothOutEasing)
        ) + fadeIn(
            animationSpec = tween(DURATION_MEDIUM, easing = SmoothOutEasing)
        )

    val collapseSection: ExitTransition =
        shrinkVertically(
            animationSpec = tween(DURATION_FAST + 40, easing = StandardEasing)
        ) + fadeOut(
            animationSpec = tween(DURATION_FAST, easing = StandardEasing)
        )

    fun horizontalSlideTransition(forward: Boolean): ContentTransform {
        val direction = if (forward) 1 else -1
        return (slideInHorizontally(
            initialOffsetX = { fullWidth -> direction * (fullWidth / 7) },
            animationSpec = tween(DURATION_MEDIUM, easing = SmoothOutEasing)
        ) + fadeIn(
            animationSpec = tween(DURATION_MEDIUM, delayMillis = 30, easing = SmoothOutEasing)
        )).togetherWith(
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -direction * (fullWidth / 9) },
                animationSpec = tween(DURATION_FAST + 40, easing = StandardEasing)
            ) + fadeOut(
                animationSpec = tween(DURATION_FAST, easing = StandardEasing)
            )
        )
    }

    fun verticalCrossfadeTransition(upward: Boolean = false): ContentTransform {
        val dir = if (upward) 1 else -1
        return (fadeIn(tween(DURATION_MEDIUM, easing = SmoothOutEasing)) +
            slideInVertically(tween(DURATION_MEDIUM, easing = SmoothOutEasing)) { dir * (it / 10) })
            .togetherWith(
                fadeOut(tween(DURATION_FAST, easing = StandardEasing)) +
                    slideOutVertically(tween(DURATION_FAST, easing = StandardEasing)) { -dir * (it / 12) }
            )
    }
}
