with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "r") as f:
    content = f.read()

target1 = """    val topHighlight = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
        startY = 0f,
        endY = 50f
    )
    val bottomShadow = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.1f)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )"""

replacement1 = """    val topHighlight = remember {
        androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.2f), Color.Transparent),
            startY = 0f,
            endY = 50f
        )
    }
    val bottomShadow = remember {
        androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.1f)),
            startY = 0f,
            endY = Float.POSITIVE_INFINITY
        )
    }"""

content = content.replace(target1, replacement1)

target2 = """    val topHighlight = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
        startY = 0f,
        endY = 50f
    )
    val bottomShadow = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.15f)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )"""

replacement2 = """    val topHighlight = remember {
        androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.3f), Color.Transparent),
            startY = 0f,
            endY = 50f
        )
    }
    val bottomShadow = remember {
        androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.15f)),
            startY = 0f,
            endY = Float.POSITIVE_INFINITY
        )
    }"""

content = content.replace(target2, replacement2)

with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "w") as f:
    f.write(content)
