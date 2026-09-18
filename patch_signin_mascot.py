import re

with open('./app/src/main/java/com/example/ui/SignInScreen.kt', 'r') as f:
    content = f.read()

# ADD IMPORT FOR PomoPalMascot if not present
if "com.example.ui.components.PomoPalMascot" not in content:
    content = content.replace(
        "import com.example.ui.theme.blend",
        "import com.example.ui.theme.blend\nimport com.example.ui.components.PomoPalMascot\nimport com.example.ui.components.MascotExpression"
    )

target = """                // Core Mascot with crisp 3D styling
                Text(
                    text = "🍅",
                    fontSize = 54.sp
                )"""

replacement = """                // Core Mascot with crisp 3D styling
                PomoPalMascot(
                    expression = MascotExpression.HAPPY,
                    modifier = Modifier.size(76.dp)
                )"""

content = content.replace(target, replacement)

with open('./app/src/main/java/com/example/ui/SignInScreen.kt', 'w') as f:
    f.write(content)
