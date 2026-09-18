import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = "val showSettings by viewModel.isSettingsOpen.collectAsState()"
replacement = """val showSettings by viewModel.isSettingsOpen.collectAsState()
    val settingsInitialTab by viewModel.settingsTab.collectAsState()"""

content = content.replace(target, replacement)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
