package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform

enum class MascotExpression {
    IDLE, FOCUS, RELAX, HAPPY
}

@Composable
fun PomoPalMascot(
    expression: MascotExpression,
    modifier: Modifier = Modifier,
    isBreathing: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    
    val breatheY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breatheY"
    )

    val blink by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                1f at 3800 // Stay open
                0f at 3900 // Close eyes
                1f at 4000 // Open eyes
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "blink"
    )

    // Expressions driving animations
    val targetSquash = if (expression == MascotExpression.HAPPY) 0.9f else 1f
    val squash by animateFloatAsState(
        targetValue = targetSquash,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow),
        label = "squash"
    )

    // Hand Animation Targets
    val targetLeftHandX = when (expression) {
        MascotExpression.HAPPY -> -0.45f
        MascotExpression.FOCUS -> -0.22f
        MascotExpression.RELAX -> -0.4f
        else -> -0.45f // IDLE
    }
    val targetLeftHandY = when (expression) {
        MascotExpression.HAPPY -> -0.3f
        MascotExpression.FOCUS -> 0.05f
        MascotExpression.RELAX -> 0.35f
        else -> 0.2f // IDLE
    }
    
    val leftHandX by animateFloatAsState(targetLeftHandX, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "lhX")
    val leftHandY by animateFloatAsState(targetLeftHandY, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "lhY")

    Canvas(modifier = modifier) {
        if (size.width <= 0f || size.height <= 0f) return@Canvas
        val yOffset = if (isBreathing) breatheY * (size.height / 100f) else 0f
        
        // 1. Draw Floor Shadow (Anchors the 3D object, stays still while object floats)
        drawFloorShadow(yOffset)
        
        // Translate the whole character for breathing
        withTransform({
            translate(top = yOffset)
            scale(scaleX = 1f, scaleY = squash, pivot = Offset(size.width / 2f, size.height))
        }) {
            // 2. Draw Back elements (stem/leaves)
            drawStemAndLeaves()
            
            // 3. Draw Main Body (Premium 3D clay look)
            drawTomatoBody()
            
            // 4. Draw Hands
            drawHands(leftHandX, leftHandY, -leftHandX, leftHandY)
            
            // 5. Draw Face (Eyes, mouth, accessories)
            drawFace(expression, if (expression == MascotExpression.RELAX) 0f else blink)
        }
    }
}

private fun DrawScope.drawFloorShadow(yOffset: Float) {
    val centerX = size.width / 2f
    val shadowY = size.height * 0.9f
    val shadowRadiusX = size.width * 0.35f
    val shadowRadiusY = size.height * 0.08f
    
    // Shadow scales down when the character floats up
    val liftFactor = (yOffset / (size.height / 100f) + 3f) / 6f // 0 to 1 based on breatheY
    val shadowScale = 1f - (liftFactor * 0.2f)
    val shadowAlpha = 0.4f - (liftFactor * 0.15f)

    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(Color.Black.copy(alpha = shadowAlpha), Color.Transparent),
            center = Offset(centerX, shadowY),
            radius = shadowRadiusX
        ),
        topLeft = Offset(centerX - shadowRadiusX * shadowScale, shadowY - shadowRadiusY * shadowScale),
        size = Size(shadowRadiusX * 2f * shadowScale, shadowRadiusY * 2f * shadowScale)
    )
}

private fun DrawScope.drawTomatoBody() {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val radiusX = size.width * 0.42f
    val radiusY = size.height * 0.38f

    // 1. Main body 3D gradient (Highlight -> Base -> Core Shadow -> Edge Shadow)
    val bodyGradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFF9494), // Bright highlight (top left)
            Color(0xFFE03131), // Base rich red
            Color(0xFFA11B1B), // Core shadow
            Color(0xFF5C0707)  // Deep edge shadow
        ),
        center = Offset(centerX - radiusX * 0.3f, centerY - radiusY * 0.3f),
        radius = radiusX * 1.6f
    )
    drawOval(
        brush = bodyGradient,
        topLeft = Offset(centerX - radiusX, centerY - radiusY),
        size = Size(radiusX * 2f, radiusY * 2f)
    )

    // 2. Glossy Specular Highlight (The "shiny clay" reflection)
    val highlightPath = Path().apply {
        addOval(Rect(
            Offset(centerX - radiusX * 0.6f, centerY - radiusY * 0.65f),
            Size(radiusX * 0.6f, radiusY * 0.4f)
        ))
    }
    withTransform({
        rotate(-25f, Offset(centerX - radiusX * 0.5f, centerY - radiusY * 0.65f))
    }) {
        drawPath(
            path = highlightPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color.White.copy(alpha = 0.7f), Color.White.copy(alpha = 0.0f)),
                startY = centerY - radiusY * 0.65f,
                endY = centerY - radiusY * 0.25f
            )
        )
    }

    // 3. Bounce Light / Rim Light (Bottom right edge reflection)
    val bounceLightGradient = Brush.radialGradient(
        colors = listOf(Color(0xFFFFD4D4).copy(alpha = 0.35f), Color.Transparent),
        center = Offset(centerX + radiusX * 0.6f, centerY + radiusY * 0.7f),
        radius = radiusX * 0.9f
    )
    drawOval(
        brush = bounceLightGradient,
        topLeft = Offset(centerX - radiusX, centerY - radiusY),
        size = Size(radiusX * 2f, radiusY * 2f)
    )
}

private fun DrawScope.drawStemAndLeaves() {
    val centerX = size.width / 2f
    val centerY = size.height * 0.18f
    
    // Rich green 3D gradient
    val leafGradient = Brush.radialGradient(
        colors = listOf(Color(0xFF8CE99A), Color(0xFF2B8A3E), Color(0xFF0B4215)),
        center = Offset(centerX - size.width * 0.05f, centerY - size.height * 0.05f),
        radius = size.width * 0.3f
    )

    // Draw central stem
    val stemPath = Path().apply {
        moveTo(centerX - size.width * 0.02f, centerY)
        quadraticTo(
            centerX - size.width * 0.05f, centerY - size.height * 0.08f,
            centerX + size.width * 0.02f, centerY - size.height * 0.1f
        )
        quadraticTo(
            centerX + size.width * 0.05f, centerY - size.height * 0.04f,
            centerX + size.width * 0.02f, centerY
        )
        close()
    }
    drawPath(path = stemPath, brush = leafGradient)

    // Draw 3D-looking leaves
    val drawLeaf = { angle: Float, scale: Float ->
        withTransform({
            translate(centerX, centerY)
            rotate(angle)
            scale(scale, scale)
        }) {
            val leaf = Path().apply {
                moveTo(0f, 0f)
                quadraticTo(
                    size.width * 0.1f, -size.height * 0.04f,
                    size.width * 0.22f, 0f
                )
                quadraticTo(
                    size.width * 0.1f, size.height * 0.04f,
                    0f, 0f
                )
                close()
            }
            drawPath(path = leaf, brush = leafGradient)
            // Leaf center crease
            drawLine(
                color = Color(0xFF19712B),
                start = Offset(0f, 0f),
                end = Offset(size.width * 0.18f, 0f),
                strokeWidth = size.width * 0.005f
            )
        }
    }
    
    drawLeaf(-25f, 1f)
    drawLeaf(45f, 0.9f)
    drawLeaf(160f, 0.8f)
    drawLeaf(210f, 0.85f)
}

private fun DrawScope.drawHands(lx: Float, ly: Float, rx: Float, ry: Float) {
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val handRadius = size.width * 0.08f

    val handGradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFFFF9494), // Highlight
            Color(0xFFE03131), // Base
            Color(0xFFA11B1B)  // Shadow
        ),
        radius = handRadius * 1.5f
    )

    // Left Hand
    val leftCenter = Offset(centerX + lx * size.width, centerY + ly * size.height)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFF9494), Color(0xFFE03131), Color(0xFFA11B1B)),
            center = Offset(leftCenter.x - handRadius * 0.3f, leftCenter.y - handRadius * 0.3f),
            radius = handRadius * 1.5f
        ),
        radius = handRadius,
        center = leftCenter
    )

    // Right Hand
    val rightCenter = Offset(centerX + rx * size.width, centerY + ry * size.height)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFF9494), Color(0xFFE03131), Color(0xFFA11B1B)),
            center = Offset(rightCenter.x - handRadius * 0.3f, rightCenter.y - handRadius * 0.3f),
            radius = handRadius * 1.5f
        ),
        radius = handRadius,
        center = rightCenter
    )
}

private fun DrawScope.drawFace(expression: MascotExpression, blinkScale: Float) {
    val centerX = size.width / 2f
    val centerY = size.height * 0.55f
    
    val eyeColor = Color(0xFF210F04) // Very dark brown (warmer than pure black)
    val eyeRadius = size.width * 0.045f
    val eyeSpacing = size.width * 0.16f
    
    val leftEyeCenter = Offset(centerX - eyeSpacing / 2f, centerY)
    val rightEyeCenter = Offset(centerX + eyeSpacing / 2f, centerY)

    if (expression == MascotExpression.RELAX) {
        // Draw closed happy eyes (arcs)
        val arcWidth = eyeRadius * 2.2f
        drawArc(
            color = eyeColor,
            startAngle = 180f,
            sweepAngle = -180f,
            useCenter = false,
            topLeft = Offset(leftEyeCenter.x - arcWidth/2, leftEyeCenter.y),
            size = Size(arcWidth, arcWidth/2),
            style = Stroke(width = size.width * 0.015f, cap = StrokeCap.Round)
        )
        drawArc(
            color = eyeColor,
            startAngle = 180f,
            sweepAngle = -180f,
            useCenter = false,
            topLeft = Offset(rightEyeCenter.x - arcWidth/2, rightEyeCenter.y),
            size = Size(arcWidth, arcWidth/2),
            style = Stroke(width = size.width * 0.015f, cap = StrokeCap.Round)
        )
        
        // Relaxed small smile
        drawArc(
            color = eyeColor,
            startAngle = 10f,
            sweepAngle = 160f,
            useCenter = false,
            topLeft = Offset(centerX - eyeSpacing * 0.25f, centerY + size.height * 0.05f),
            size = Size(eyeSpacing * 0.5f, size.height * 0.03f),
            style = Stroke(width = size.width * 0.015f, cap = StrokeCap.Round)
        )
        
    } else {
        // Blinking logic for open eyes
        withTransform({
            scale(1f, blinkScale, leftEyeCenter)
        }) {
            // Base eye
            drawOval(
                color = eyeColor,
                topLeft = Offset(leftEyeCenter.x - eyeRadius, leftEyeCenter.y - eyeRadius * (if (expression == MascotExpression.FOCUS) 0.8f else 1.2f)),
                size = Size(eyeRadius * 2f, eyeRadius * (if (expression == MascotExpression.FOCUS) 1.6f else 2.4f))
            )
            // Primary Catch-light (Cute 3D reflection)
            drawOval(
                color = Color.White,
                topLeft = Offset(leftEyeCenter.x - eyeRadius * 0.4f, leftEyeCenter.y - eyeRadius * 0.6f),
                size = Size(eyeRadius * 0.8f, eyeRadius * 0.8f)
            )
            // Secondary tiny Catch-light
            drawOval(
                color = Color.White,
                topLeft = Offset(leftEyeCenter.x + eyeRadius * 0.3f, leftEyeCenter.y + eyeRadius * 0.2f),
                size = Size(eyeRadius * 0.3f, eyeRadius * 0.3f)
            )
        }
        
        withTransform({
            scale(1f, blinkScale, rightEyeCenter)
        }) {
            // Base eye
            drawOval(
                color = eyeColor,
                topLeft = Offset(rightEyeCenter.x - eyeRadius, rightEyeCenter.y - eyeRadius * (if (expression == MascotExpression.FOCUS) 0.8f else 1.2f)),
                size = Size(eyeRadius * 2f, eyeRadius * (if (expression == MascotExpression.FOCUS) 1.6f else 2.4f))
            )
            // Primary Catch-light
            drawOval(
                color = Color.White,
                topLeft = Offset(rightEyeCenter.x - eyeRadius * 0.4f, rightEyeCenter.y - eyeRadius * 0.6f),
                size = Size(eyeRadius * 0.8f, eyeRadius * 0.8f)
            )
            // Secondary tiny Catch-light
            drawOval(
                color = Color.White,
                topLeft = Offset(rightEyeCenter.x + eyeRadius * 0.3f, rightEyeCenter.y + eyeRadius * 0.2f),
                size = Size(eyeRadius * 0.3f, eyeRadius * 0.3f)
            )
        }
        
        // Face Accessories and Mouths
        if (expression == MascotExpression.FOCUS) {
            // Cute reading glasses
            val glassesWidth = eyeRadius * 4.5f
            val glassesHeight = eyeRadius * 3.5f
            
            // Glass tint
            val glassTint = Color(0x334DABF7) // Semi-transparent blue
            drawRoundRect(
                color = glassTint,
                topLeft = Offset(leftEyeCenter.x - glassesWidth/2, leftEyeCenter.y - glassesHeight/2),
                size = Size(glassesWidth, glassesHeight),
                cornerRadius = CornerRadius(16f)
            )
            drawRoundRect(
                color = glassTint,
                topLeft = Offset(rightEyeCenter.x - glassesWidth/2, rightEyeCenter.y - glassesHeight/2),
                size = Size(glassesWidth, glassesHeight),
                cornerRadius = CornerRadius(16f)
            )
            
            // Frames
            drawRoundRect(
                color = Color(0xFF343A40),
                topLeft = Offset(leftEyeCenter.x - glassesWidth/2, leftEyeCenter.y - glassesHeight/2),
                size = Size(glassesWidth, glassesHeight),
                cornerRadius = CornerRadius(16f),
                style = Stroke(width = size.width * 0.015f)
            )
            drawRoundRect(
                color = Color(0xFF343A40),
                topLeft = Offset(rightEyeCenter.x - glassesWidth/2, rightEyeCenter.y - glassesHeight/2),
                size = Size(glassesWidth, glassesHeight),
                cornerRadius = CornerRadius(16f),
                style = Stroke(width = size.width * 0.015f)
            )
            // Bridge
            drawLine(
                color = Color(0xFF343A40),
                start = Offset(leftEyeCenter.x + glassesWidth/2, leftEyeCenter.y),
                end = Offset(rightEyeCenter.x - glassesWidth/2, rightEyeCenter.y),
                strokeWidth = size.width * 0.015f
            )
            
            // Focused mouth (tiny 'o')
            drawOval(
                color = eyeColor,
                topLeft = Offset(centerX - eyeRadius*0.5f, centerY + size.height * 0.06f),
                size = Size(eyeRadius, eyeRadius)
            )
        } else if (expression == MascotExpression.HAPPY) {
            // Big open happy mouth
            val mouthPath = Path().apply {
                moveTo(centerX - eyeSpacing * 0.4f, centerY + size.height * 0.04f)
                quadraticTo(
                    centerX, centerY + size.height * 0.16f,
                    centerX + eyeSpacing * 0.4f, centerY + size.height * 0.04f
                )
                close()
            }
            drawPath(path = mouthPath, color = eyeColor)
            
            // Tongue
            val tonguePath = Path().apply {
                moveTo(centerX - eyeSpacing * 0.25f, centerY + size.height * 0.08f)
                quadraticTo(
                    centerX, centerY + size.height * 0.13f,
                    centerX + eyeSpacing * 0.25f, centerY + size.height * 0.08f
                )
                close()
            }
            drawPath(path = tonguePath, color = Color(0xFFFF8787))
        } else {
            // Idle smile
            drawArc(
                color = eyeColor,
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(centerX - eyeSpacing * 0.25f, centerY + size.height * 0.04f),
                size = Size(eyeSpacing * 0.5f, size.height * 0.03f),
                style = Stroke(width = size.width * 0.015f, cap = StrokeCap.Round)
            )
        }
    }
}
