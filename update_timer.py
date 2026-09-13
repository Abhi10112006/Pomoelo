with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(currentTheme.surface.copy(alpha = 0.95f), currentTheme.surface.copy(alpha = 0.7f))
                    )
                )"""

replacement = """                .background(
                    brush = remember(currentTheme) {
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(currentTheme.surface.copy(alpha = 0.95f), currentTheme.surface.copy(alpha = 0.7f))
                        )
                    }
                )"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
