import re

with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "r") as f:
    content = f.read()

target = """    Box(
        modifier = modifier
            .pomoBouncyClick(
                shape = shape,
                elevation = elevation,
                shadowColor = actualShadow,
                enabled = enabled,
                onClick = onClick
            )
            .background(if (enabled) containerColor else containerColor.copy(alpha = 0.5f), shape)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    )"""

replacement = """    val topHighlight = androidx.compose.ui.graphics.Brush.verticalGradient(
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
    )"""

content = content.replace(target, replacement)

# Do same for PomoIconButton
target_icon = """    Box(
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
            .then(if (border != null) Modifier.border(border, shape) else Modifier),
        contentAlignment = Alignment.Center
    )"""

replacement_icon = """    val topHighlight = androidx.compose.ui.graphics.Brush.verticalGradient(
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
    )"""

content = content.replace(target_icon, replacement_icon)

with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "w") as f:
    f.write(content)
