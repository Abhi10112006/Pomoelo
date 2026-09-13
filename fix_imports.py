with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "r") as f:
    content = f.read()
    
content = content.replace("package com.example.ui.components\n", "package com.example.ui.components\n\nimport androidx.compose.foundation.layout.fillMaxSize\nimport androidx.compose.ui.unit.sp\nimport androidx.compose.ui.draw.rotate\n")

with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "w") as f:
    f.write(content)
