package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform

/**
 * Crisp 2D Judy Hopps pajama portrait used for compact icon badges (<= 56.dp),
 * loading placeholders, and non-native JVM preview/test environments.
 */
@Composable
fun JudyAvatarBadge(
    modifier: Modifier = Modifier,
    isBreathing: Boolean = false
) {
    val wiggle = if (isBreathing) {
        val infiniteTransition = rememberInfiniteTransition(label = "judyBadgeBreath")
        val animatedWiggle by infiniteTransition.animateFloat(
            initialValue = -2.5f,
            targetValue = 2.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(2200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "earWiggle"
        )
        animatedWiggle
    } else {
        0f
    }

    val nosePath = remember { Path() }

    Canvas(modifier = modifier) {
        if (size.width <= 0f || size.height <= 0f) return@Canvas
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.58f

        // Soft floor shadow
        drawOval(
            color = Color.Black.copy(alpha = 0.14f),
            topLeft = Offset(cx - w * 0.28f, h * 0.88f),
            size = Size(w * 0.56f, h * 0.08f)
        )

        // Bunny Ears (Left & Right)
        drawBunnyEar(cx - w * 0.14f, cy - h * 0.18f, w * 0.13f, h * 0.36f, -14f + wiggle)
        drawBunnyEar(cx + w * 0.14f, cy - h * 0.18f, w * 0.13f, h * 0.36f, 14f - wiggle)

        // Cozy Pajama Shoulders
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFF6A5C0), Color(0xFFE582A5))
            ),
            topLeft = Offset(cx - w * 0.24f, cy + h * 0.16f),
            size = Size(w * 0.48f, h * 0.22f),
            cornerRadius = CornerRadius(w * 0.16f, w * 0.16f)
        )

        // Head (Soft bunny grey-lavender gradient)
        val headRx = w * 0.27f
        val headRy = h * 0.23f
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFD6DAE4), Color(0xFFA9B0C0), Color(0xFF838B9E)),
                center = Offset(cx - headRx * 0.25f, cy - headRy * 0.3f),
                radius = headRx * 1.6f
            ),
            topLeft = Offset(cx - headRx, cy - headRy),
            size = Size(headRx * 2f, headRy * 2f)
        )

        // Cream cheeks / muzzle patch
        drawOval(
            color = Color(0xFFF3F1F6),
            topLeft = Offset(cx - headRx * 0.72f, cy + headRy * 0.05f),
            size = Size(headRx * 1.44f, headRy * 0.88f)
        )

        // Rosy blush
        drawOval(
            color = Color(0xFFF48FB1).copy(alpha = 0.45f),
            topLeft = Offset(cx - headRx * 0.78f, cy + headRy * 0.18f),
            size = Size(headRx * 0.34f, headRy * 0.22f)
        )
        drawOval(
            color = Color(0xFFF48FB1).copy(alpha = 0.45f),
            topLeft = Offset(cx + headRx * 0.44f, cy + headRy * 0.18f),
            size = Size(headRx * 0.34f, headRy * 0.22f)
        )

        // Expressive Purple-Blue Eyes
        val eyeOffsetX = headRx * 0.40f
        val eyeY = cy - headRy * 0.08f
        val eyeR = w * 0.055f
        listOf(cx - eyeOffsetX, cx + eyeOffsetX).forEach { ex ->
            drawCircle(color = Color(0xFF6B5CE7), radius = eyeR, center = Offset(ex, eyeY))
            drawCircle(color = Color(0xFF28204A), radius = eyeR * 0.56f, center = Offset(ex, eyeY))
            drawCircle(
                color = Color.White,
                radius = eyeR * 0.30f,
                center = Offset(ex - eyeR * 0.28f, eyeY - eyeR * 0.28f)
            )
        }

        // Cute Pink Nose
        nosePath.reset()
        nosePath.moveTo(cx - w * 0.035f, cy + headRy * 0.16f)
        nosePath.lineTo(cx + w * 0.035f, cy + headRy * 0.16f)
        nosePath.lineTo(cx, cy + headRy * 0.30f)
        nosePath.close()
        drawPath(nosePath, Color(0xFFF06292))

        // Gentle Smile
        drawArc(
            color = Color(0xFF4A3F55),
            startAngle = 15f,
            sweepAngle = 150f,
            useCenter = false,
            topLeft = Offset(cx - w * 0.07f, cy + headRy * 0.28f),
            size = Size(w * 0.14f, h * 0.06f),
            style = Stroke(width = w * 0.022f, cap = StrokeCap.Round)
        )
    }
}

private fun DrawScope.drawBunnyEar(
    pivotX: Float,
    pivotY: Float,
    earWidth: Float,
    earHeight: Float,
    angleDeg: Float
) {
    withTransform({
        rotate(angleDeg, pivot = Offset(pivotX, pivotY))
    }) {
        drawOval(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF7A8296), Color(0xFFB5BCCB))
            ),
            topLeft = Offset(pivotX - earWidth / 2f, pivotY - earHeight),
            size = Size(earWidth, earHeight)
        )
        drawOval(
            color = Color(0xFFF4A9BE),
            topLeft = Offset(pivotX - earWidth * 0.26f, pivotY - earHeight * 0.84f),
            size = Size(earWidth * 0.52f, earHeight * 0.68f)
        )
    }
}
