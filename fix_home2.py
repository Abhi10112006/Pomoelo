import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Let's find the exact place in HomeScreen
target = """            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row("""

replacement = """            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Just-in-time setup messages
            androidx.compose.animation.AnimatedVisibility(
                visible = protectionStates.needsAttention,
                enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
            ) {
                com.example.ui.ContextualSetupMessage(
                    state = protectionStates,
                    onSetup = { actionId ->
                        try {
                            when (actionId) {
                                "NOTIFICATIONS" -> {
                                    val intent = android.content.Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                        putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                                    }
                                    context.startActivity(intent)
                                }
                                "DND" -> {
                                    val intent = android.content.Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                                    context.startActivity(intent)
                                }
                                "BATTERY" -> {
                                    val intent = android.content.Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                                    context.startActivity(intent)
                                }
                            }
                        } catch (e: Exception) {
                            // Fallback to our internal settings if intent fails
                            viewModel.setSettingsOpen(true, 3)
                        }
                    }
                )
            }

            Row("""

if target in content:
    content = content.replace(target, replacement)
    print("Replaced layout successfully")
else:
    print("Could not find target layout")

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
