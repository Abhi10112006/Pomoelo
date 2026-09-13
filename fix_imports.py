with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "r") as f:
    content = f.read()

content = content.replace("import androidx.compose.ui.unit.sp", "import androidx.compose.ui.unit.sp\nimport androidx.compose.ui.draw.drawWithCache\nimport androidx.compose.runtime.remember")
content = content.replace(".androidx.compose.ui.draw.drawWithCache", ".drawWithCache")

with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "w") as f:
    f.write(content)
