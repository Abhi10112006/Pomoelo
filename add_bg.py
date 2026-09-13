import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace("    Scaffold(\n        modifier = Modifier.fillMaxSize(),\n        containerColor = Color.Transparent,",
"""    Box(modifier = Modifier.fillMaxSize().background(com.example.ui.theme.LocalAppTheme.current.background)) {
        com.example.ui.components.PaperTextureOverlay()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,""")

# We need to add `}` after the scaffold ends.
# Scaffold ends with:
#             composable("workout") {
#                 com.example.ui.WorkoutScreen(bottomPadding)
#             }
#         }
#     } // closes the Scaffold padding block? No, closes NavHost and Scaffold.
