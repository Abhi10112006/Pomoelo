package com.example.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

/**
 * Unified PomoPal Material 3 Expressive Motion System (Phase 1)
 *
 * Implements M3 physics-based Spatial & Effects springs:
 * - Spatial specs govern movement, scale, bounds, and shape morphs (interruptible, natural velocity).
 * - Effects specs govern opacity, color, and elevation (critically damped, zero overshoot).
 */
object PomoMotion {
    const val DURATION_FAST = 180
    const val DURATION_STANDARD = 240
    const val DURATION_MEDIUM = 280
    const val DURATION_SLOW = 380

    val SmoothOutEasing: Easing = CubicBezierEasing(0.22f, 1.0f, 0.36f, 1.0f)
    val SmoothInOutEasing: Easing = FastOutSlowInEasing
    val StandardEasing: Easing = FastOutSlowInEasing

    // ── M3 Expressive Spatial Springs (position, scale, bounds, shape) ─────────────────────────
    fun <T> fastSpatial(visibilityThreshold: T? = null): SpringSpec<T> =
        spring(
            dampingRatio = 0.72f,
            stiffness = 1400f,
            visibilityThreshold = visibilityThreshold
        )

    fun <T> defaultSpatial(visibilityThreshold: T? = null): SpringSpec<T> =
        spring(
            dampingRatio = 0.80f,
            stiffness = 700f,
            visibilityThreshold = visibilityThreshold
        )

    fun <T> slowSpatial(visibilityThreshold: T? = null): SpringSpec<T> =
        spring(
            dampingRatio = 0.84f,
            stiffness = 320f,
            visibilityThreshold = visibilityThreshold
        )

    fun <T> expressiveSpatial(visibilityThreshold: T? = null): SpringSpec<T> =
        spring(
            dampingRatio = 0.62f,
            stiffness = 580f,
            visibilityThreshold = visibilityThreshold
        )

    // ── M3 Critically-Damped Effects Springs (opacity, color, elevation) ───────────────────────
    fun <T> fastEffects(visibilityThreshold: T? = null): SpringSpec<T> =
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = 2400f,
            visibilityThreshold = visibilityThreshold
        )

    fun <T> defaultEffects(visibilityThreshold: T? = null): SpringSpec<T> =
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = 1200f,
            visibilityThreshold = visibilityThreshold
        )

    fun <T> slowEffects(visibilityThreshold: T? = null): SpringSpec<T> =
        spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = 600f,
            visibilityThreshold = visibilityThreshold
        )

    // Convenience typed helpers for Compose layout/offset/size animations
    val intOffsetSpatial: SpringSpec<IntOffset> = defaultSpatial(IntOffset.VisibilityThreshold)
    val intOffsetFastSpatial: SpringSpec<IntOffset> = fastSpatial(IntOffset.VisibilityThreshold)
    val intSizeSpatial: SpringSpec<IntSize> = defaultSpatial(IntSize.VisibilityThreshold)
    val dpSpatial: SpringSpec<Dp> = defaultSpatial(Dp.VisibilityThreshold)
    val dpFastSpatial: SpringSpec<Dp> = fastSpatial(Dp.VisibilityThreshold)

    fun <T> smoothSpring(stiffness: Float = Spring.StiffnessMediumLow): SpringSpec<T> =
        spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = stiffness)

    fun <T> gentleSpring(stiffness: Float = Spring.StiffnessLow): SpringSpec<T> =
        spring(dampingRatio = 0.85f, stiffness = stiffness)

    val pressScaleSpec: FiniteAnimationSpec<Float> = fastSpatial()

    val elevationSpec: FiniteAnimationSpec<Dp> = fastEffects(Dp.VisibilityThreshold)

    val colorSpec: FiniteAnimationSpec<Color> = defaultEffects()

    val dpSpec: FiniteAnimationSpec<Dp> = dpSpatial

    val floatSpec: FiniteAnimationSpec<Float> = defaultSpatial()

    val expandSection: EnterTransition =
        expandVertically(animationSpec = intSizeSpatial) +
            fadeIn(animationSpec = defaultEffects())

    val collapseSection: ExitTransition =
        shrinkVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = 1000f,
                visibilityThreshold = IntSize.VisibilityThreshold
            )
        ) + fadeOut(animationSpec = fastEffects())

    /**
     * M3 Shared Axis X transition using Spatial springs for horizontal travel
     * and critically-damped Effects springs for opacity.
     */
    fun horizontalSlideTransition(forward: Boolean): ContentTransform {
        val direction = if (forward) 1 else -1
        return ContentTransform(
            targetContentEnter = slideInHorizontally(
                initialOffsetX = { fullWidth -> direction * (fullWidth / 5) },
                animationSpec = intOffsetSpatial
            ) + fadeIn(
                animationSpec = defaultEffects()
            ),
            initialContentExit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> -direction * (fullWidth / 6) },
                animationSpec = intOffsetFastSpatial
            ) + fadeOut(
                animationSpec = fastEffects()
            ),
            sizeTransform = SizeTransform(clip = false) { _, _ -> intSizeSpatial }
        )
    }

    /**
     * M3 Shared Axis Y transition for vertical state transitions.
     */
    fun verticalCrossfadeTransition(upward: Boolean = false): ContentTransform {
        val dir = if (upward) 1 else -1
        return ContentTransform(
            targetContentEnter = fadeIn(animationSpec = defaultEffects()) +
                slideInVertically(animationSpec = intOffsetSpatial) { dir * (it / 8) },
            initialContentExit = fadeOut(animationSpec = fastEffects()) +
                slideOutVertically(animationSpec = intOffsetFastSpatial) { -dir * (it / 10) },
            sizeTransform = SizeTransform(clip = false) { _, _ -> intSizeSpatial }
        )
    }

    /**
     * M3 Shared Axis Z (depth scale + fade) for drill-down or modal focus shifts.
     */
    fun sharedAxisZTransition(forward: Boolean): ContentTransform {
        val enterInitialScale = if (forward) 0.90f else 1.06f
        val exitTargetScale = if (forward) 1.06f else 0.92f
        return (scaleIn(
            initialScale = enterInitialScale,
            animationSpec = defaultSpatial()
        ) + fadeIn(
            animationSpec = defaultEffects()
        )).togetherWith(
            scaleOut(
                targetScale = exitTargetScale,
                animationSpec = fastSpatial()
            ) + fadeOut(
                animationSpec = fastEffects()
            )
        )
    }

    /**
     * Asymmetric Fade-Through Enter:
     * Delayed fade-in (~200ms after outgoing begins clearing) paired with pure depth settle (0.95f -> 1.0f),
     * preventing dense screens from overlapping at 50% opacity.
     */
    fun asymmetricFadeThroughEnter(): EnterTransition =
        fadeIn(
            animationSpec = tween(
                durationMillis = 200,
                delayMillis = 75,
                easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
            )
        ) + scaleIn(
            initialScale = 0.95f,
            animationSpec = tween(
                durationMillis = 230,
                delayMillis = 60,
                easing = SmoothOutEasing
            )
        )

    /**
     * Asymmetric Fade-Through Exit:
     * Fast critically damped fade-out (~95-110ms equivalent stiffness) paired with subtle scale-down (1.0f -> 0.96f).
     */
    fun asymmetricFadeThroughExit(): ExitTransition =
        fadeOut(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = 3400f
            )
        ) + scaleOut(
            targetScale = 0.96f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = 2600f
            )
        )

    /**
     * M3 Asymmetric Fade-Through ContentTransform for switching unrelated peer destinations or tabs.
     */
    fun asymmetricFadeThroughTransition(): ContentTransform =
        ContentTransform(
            targetContentEnter = asymmetricFadeThroughEnter(),
            initialContentExit = asymmetricFadeThroughExit(),
            sizeTransform = SizeTransform(clip = false) { _, _ -> intSizeSpatial }
        )

    /**
     * M3 Fade Through transition for switching peer destinations.
     */
    fun fadeThroughTransition(): ContentTransform = asymmetricFadeThroughTransition()
}
