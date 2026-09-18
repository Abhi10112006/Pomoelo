import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Add a state for initial tab
state_target = "val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()"
state_replacement = """val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
    var settingsInitialTab by remember { mutableStateOf(0) }"""
content = content.replace(state_target, state_replacement)

# Update onClick
click_target1 = """                    onFixClicked = {
                        viewModel.setSettingsOpen(true)
                        // Wait, we need a way to open the System settings tab specifically. 
                        // SettingsOverlay has its own internal state. I will just open settings.
                    }"""
click_replacement1 = """                    onFixClicked = {
                        settingsInitialTab = 3
                        viewModel.setSettingsOpen(true)
                    }"""
content = content.replace(click_target1, click_replacement1)

click_target2 = """                    onFixClicked = {
                        viewModel.setSettingsOpen(true)
                    }"""
click_replacement2 = """                    onFixClicked = {
                        settingsInitialTab = 3
                        viewModel.setSettingsOpen(true)
                    }"""
content = content.replace(click_target2, click_replacement2)

# Also update the gear icon in the header to reset initialTab to 0
gear_target = """                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (e: Exception) {}
                            viewModel.setSettingsOpen(true)"""
gear_replacement = """                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (e: Exception) {}
                            settingsInitialTab = 0
                            viewModel.setSettingsOpen(true)"""
content = content.replace(gear_target, gear_replacement)

# Update the SettingsOverlay invocation
overlay_target = """            SettingsOverlay(
                onDismiss = { viewModel.setSettingsOpen(false) },
                isDarkTheme = isDarkTheme,
                onThemeChange = { isDark ->
                    scope.launch { com.example.service.SettingsManager.saveTheme(isDark) }
                },
                currentFont = AppFontFamily,
                onFontChange = { } // Assuming fonts are global/static now for simplicity, but can be updated
            )"""
overlay_replacement = """            SettingsOverlay(
                onDismiss = { viewModel.setSettingsOpen(false) },
                isDarkTheme = isDarkTheme,
                onThemeChange = { isDark ->
                    scope.launch { com.example.service.SettingsManager.saveTheme(isDark) }
                },
                currentFont = AppFontFamily,
                onFontChange = { },
                initialTab = settingsInitialTab
            )"""
content = content.replace(overlay_target, overlay_replacement)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
