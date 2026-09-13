import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("                PomoPalApp(viewModel)",
"""                Box(modifier = Modifier.fillMaxSize().background(currentTheme.background)) {
                    com.example.ui.components.PaperTextureOverlay()
                    PomoPalApp(viewModel)
                }""")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
