import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# I will inject the protection state into PomoPalApp
hook_target = "val currentTheme = com.example.ui.theme.LocalAppTheme.current"
hook = """val currentTheme = com.example.ui.theme.LocalAppTheme.current
    val protectionStates = com.example.ui.rememberSystemProtectionStates()"""
content = content.replace(hook_target, hook)

# I will insert the ContextualSetupMessage and SystemProtectionBanner
ui_target = """            Spacer(modifier = Modifier.height(verticalSpacing))
            // Timer display"""

ui_replacement = """            Spacer(modifier = Modifier.height(verticalSpacing))
            
            // System Protection setup messages
            androidx.compose.animation.AnimatedVisibility(
                visible = protectionStates.notificationsState == com.example.ui.PermissionState.ACTION_REQUIRED,
                enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
            ) {
                com.example.ui.ContextualSetupMessage(
                    state = protectionStates,
                    onFixClicked = {
                        viewModel.setSettingsOpen(true)
                        // Wait, we need a way to open the System settings tab specifically. 
                        // SettingsOverlay has its own internal state. I will just open settings.
                    }
                )
            }
            
            androidx.compose.animation.AnimatedVisibility(
                visible = protectionStates.notificationsState != com.example.ui.PermissionState.ACTION_REQUIRED && protectionStates.needsAttention,
                enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
            ) {
                com.example.ui.SystemProtectionBanner(
                    state = protectionStates,
                    onFixClicked = {
                        viewModel.setSettingsOpen(true)
                    }
                )
            }
            
            // Timer display"""

content = content.replace(ui_target, ui_replacement)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
