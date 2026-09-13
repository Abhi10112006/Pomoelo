package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.rotate

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalAppTheme

/**
 * Applies a tactile shadow matching the EXACT geometry of the button (never square)
 */
fun Modifier.pomoShadow(
    shape: Shape,
    elevation: Dp = 6.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.20f),
    ambientColor: Color = Color.Black.copy(alpha = 0.10f),
    clip: Boolean = false
): Modifier = this.shadow(
    elevation = elevation,
    shape = shape,
    clip = clip,
    spotColor = shadowColor,
    ambientColor = ambientColor
)

/**
 * Premium tactile bouncy click modifier.
 * Ensures the shadow behind the button matches the button's shape exactly,
 * smoothly compresses the shadow and scales the button down when pressed,
 * clips the click ripple to the exact rounded contour (no square corners),
 * and triggers crisp haptic feedback.
 */
@Composable
fun Modifier.pomoBouncyClick(
    shape: Shape = RoundedCornerShape(20.dp),
    elevation: Dp = 6.dp,
    pressedElevation: Dp = 1.5.dp,
    shadowColor: Color = LocalAppTheme.current.shadowColor,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val view = LocalView.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.94f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pomoScale"
    )

    val currentElevation by animateDpAsState(
        targetValue = if (isPressed && enabled) pressedElevation else if (enabled) elevation else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "pomoElevation"
    )

    return this
        .scale(scale)
        .shadow(
            elevation = currentElevation,
            shape = shape,
            clip = false,
            spotColor = shadowColor,
            ambientColor = shadowColor.copy(alpha = shadowColor.alpha * 0.5f)
        )
        .clip(shape)
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = ripple(bounded = true),
            onClick = {
                try {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {}
                onClick()
            }
        )
}

/**
 * Reusable premium tactile button with perfectly matched shape shadows.
 */
@Composable
fun PomoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(20.dp),
    containerColor: Color = LocalAppTheme.current.primary,
    contentColor: Color = Color.White,
    elevation: Dp = 5.dp,
    shadowColor: Color = LocalAppTheme.current.shadowColor,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
) {
    PomoButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        shadowColor = shadowColor,
        border = border,
        contentPadding = contentPadding
    ) {
        androidx.compose.material3.Text(
            text = text,
            color = contentColor,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            fontFamily = com.example.ui.theme.LocalAppFont.current
        )
    }
}

/**
 * Reusable premium tactile button with perfectly matched shape shadows.
 */
@Composable
fun PomoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(20.dp),
    containerColor: Color = LocalAppTheme.current.primary,
    contentColor: Color = Color.White,
    elevation: Dp = 5.dp,
    shadowColor: Color = LocalAppTheme.current.shadowColor,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    content: @Composable RowScope.() -> Unit
) {
    val theme = LocalAppTheme.current
    val actualShadow = if (containerColor == Color.White) Color.Black.copy(alpha = 0.15f) else shadowColor

    val topHighlight = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
        startY = 0f,
        endY = 50f
    )
    val bottomShadow = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.1f)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
    Box(
        modifier = modifier
            .pomoBouncyClick(
                shape = shape,
                elevation = elevation,
                shadowColor = actualShadow,
                enabled = enabled,
                onClick = onClick
            )
            .background(if (enabled) containerColor else containerColor.copy(alpha = 0.5f), shape)
            .border(1.dp, topHighlight, shape)
            .border(1.dp, bottomShadow, shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

/**
 * Premium circular or rounded icon button with matching shape shadow.
 */
@Composable
fun PomoIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 56.dp,
    shape: Shape = CircleShape,
    containerColor: Color = LocalAppTheme.current.surface,
    elevation: Dp = 4.dp,
    shadowColor: Color = LocalAppTheme.current.shadowColor,
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    val actualShadow = if (containerColor == Color.White) Color.Black.copy(alpha = 0.15f) else shadowColor

    val topHighlight = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
        startY = 0f,
        endY = 50f
    )
    val bottomShadow = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.15f)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
    Box(
        modifier = modifier
            .size(size)
            .pomoBouncyClick(
                shape = shape,
                elevation = elevation,
                shadowColor = actualShadow,
                enabled = enabled,
                onClick = onClick
            )
            .background(if (enabled) containerColor else containerColor.copy(alpha = 0.5f), shape)
            .border(1.dp, topHighlight, shape)
            .border(1.dp, bottomShadow, shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun PremiumBackgroundOverlays() {
    val currentTheme = LocalAppTheme.current
    Box(modifier = Modifier.fillMaxSize()) {
        // Leaf shadow abstract blobs on the left edge
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val shadowColor = currentTheme.textSecondary.copy(alpha = 0.08f)
            
            // Draw a few large soft blobs on the middle-left
            drawCircle(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(shadowColor, Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.2f, size.height * 0.3f),
                    radius = size.width * 0.8f
                ),
                center = androidx.compose.ui.geometry.Offset(-size.width * 0.2f, size.height * 0.3f),
                radius = size.width * 0.8f
            )
            
            drawCircle(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(shadowColor, Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.1f, size.height * 0.5f),
                    radius = size.width * 0.6f
                ),
                center = androidx.compose.ui.geometry.Offset(-size.width * 0.1f, size.height * 0.5f),
                radius = size.width * 0.6f
            )

            drawCircle(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(shadowColor, Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.3f, size.height * 0.7f),
                    radius = size.width * 0.9f
                ),
                center = androidx.compose.ui.geometry.Offset(-size.width * 0.3f, size.height * 0.7f),
                radius = size.width * 0.9f
            )
        }
        
        // Cursive text on the top right
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 110.dp, end = 32.dp)
                .rotate(-8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.Text(
                text = "Small\nSteps",
                fontFamily = com.example.ui.theme.CursiveFontFamily,
                fontSize = 24.sp,
                color = currentTheme.textSecondary.copy(alpha = 0.5f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 24.sp
            )
            androidx.compose.material3.Text(
                text = "Big Progress",
                fontFamily = com.example.ui.theme.CursiveFontFamily,
                fontSize = 28.sp,
                color = currentTheme.textSecondary.copy(alpha = 0.5f),
            )
            androidx.compose.material3.Text(
                text = "♡",
                fontSize = 24.sp,
                color = currentTheme.textSecondary.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
