with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "r") as f:
    content = f.read()

target = """        // Leaf shadow abstract blobs on the left edge
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
        }"""

replacement = """        // Leaf shadow abstract blobs on the left edge
        Box(modifier = Modifier.fillMaxSize().androidx.compose.ui.draw.drawWithCache {
            val shadowColor = currentTheme.textSecondary.copy(alpha = 0.08f)
            
            val brush1 = androidx.compose.ui.graphics.Brush.radialGradient(
                colors = listOf(shadowColor, Color.Transparent),
                center = androidx.compose.ui.geometry.Offset(-size.width * 0.2f, size.height * 0.3f),
                radius = size.width * 0.8f
            )
            
            val brush2 = androidx.compose.ui.graphics.Brush.radialGradient(
                colors = listOf(shadowColor, Color.Transparent),
                center = androidx.compose.ui.geometry.Offset(-size.width * 0.1f, size.height * 0.5f),
                radius = size.width * 0.6f
            )
            
            val brush3 = androidx.compose.ui.graphics.Brush.radialGradient(
                colors = listOf(shadowColor, Color.Transparent),
                center = androidx.compose.ui.geometry.Offset(-size.width * 0.3f, size.height * 0.7f),
                radius = size.width * 0.9f
            )
            
            onDrawBehind {
                drawCircle(
                    brush = brush1,
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.2f, size.height * 0.3f),
                    radius = size.width * 0.8f
                )
                drawCircle(
                    brush = brush2,
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.1f, size.height * 0.5f),
                    radius = size.width * 0.6f
                )
                drawCircle(
                    brush = brush3,
                    center = androidx.compose.ui.geometry.Offset(-size.width * 0.3f, size.height * 0.7f),
                    radius = size.width * 0.9f
                )
            }
        })"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "w") as f:
    f.write(content)
