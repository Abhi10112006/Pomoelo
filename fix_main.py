import re

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace("Icons.Rounded.Security", "androidx.compose.material.icons.Icons.Rounded.Security")
content = content.replace("Icons.Rounded.CheckCircle", "androidx.compose.material.icons.Icons.Rounded.CheckCircle")
content = content.replace("currentTheme.onPrimary", "Color.White")
content = content.replace("import androidx.compose.material.icons.Icons", "import androidx.compose.material.icons.Icons\nimport androidx.compose.material.icons.rounded.Security\nimport androidx.compose.material.icons.rounded.CheckCircle")

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
