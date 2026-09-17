import re

with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'r') as f:
    content = f.read()

# Fix AmbientDoodles signature
content = content.replace("fun AmbientDoodles(step: Int, theme: com.example.ui.theme.AppColors)", "fun AmbientDoodles(step: Int, theme: com.example.ui.theme.ThemeOption)")

# Add missing Canvas import if not present
if "import androidx.compose.foundation.Canvas" not in content:
    content = content.replace("import androidx.compose.foundation.background", "import androidx.compose.foundation.Canvas\nimport androidx.compose.foundation.background")

with open('./app/src/main/java/com/example/ui/components/AboutScreen.kt', 'w') as f:
    f.write(content)
