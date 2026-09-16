import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Revert settingsInitialTab from PomoPalApp
target1 = """val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
    var settingsInitialTab by remember { mutableStateOf(0) }"""
replacement1 = """val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
    val settingsInitialTab by viewModel.settingsTab.collectAsState()"""
content = content.replace(target1, replacement1)

# Fix onClick in HomeScreen
target2 = """                    onFixClicked = {
                        settingsInitialTab = 3
                        viewModel.setSettingsOpen(true)
                    }"""
replacement2 = """                    onFixClicked = {
                        viewModel.setSettingsOpen(true, 3)
                    }"""
content = content.replace(target2, replacement2)

target3 = """                            settingsInitialTab = 0
                            viewModel.setSettingsOpen(true)"""
replacement3 = """                            viewModel.setSettingsOpen(true, 0)"""
content = content.replace(target3, replacement3)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
