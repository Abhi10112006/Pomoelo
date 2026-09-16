import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# 1. We will add a state `var showJustInTimeSetup by remember { mutableStateOf(false) }` to HomeScreen
target_state = "var showFullScreenSeriousness by remember { mutableStateOf(false) }"
replace_state = """var showFullScreenSeriousness by remember { mutableStateOf(false) }
    var showJustInTimeSetup by remember { mutableStateOf(false) }"""
content = content.replace(target_state, replace_state)

# 2. Revert the inline ContextualSetupMessage and put a Small Global Indicator
target_layout = """            // Just-in-time setup messages
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
            }"""

replace_layout = """            // Small Global Indicator
            androidx.compose.animation.AnimatedVisibility(
                visible = protectionStates.needsAttention && timerState == TimerManager.TimerState.STOPPED,
                enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
            ) {
                com.example.ui.SystemProtectionBanner(
                    state = protectionStates,
                    onFixClicked = {
                        showJustInTimeSetup = true
                    }
                )
            }"""
content = content.replace(target_layout, replace_layout)

# 3. Update AppControls to show the dialog instead of opening Settings tab
target_controls = """            AppControls(
                state = timerState, 
                context = context,
                onStartAttempt = {
                    if (protectionStates.needsAttention) {
                        try {
                            view.performHapticFeedback(17) // Reject haptic to draw attention
                        } catch(e: Exception) {}
                        viewModel.setSettingsOpen(true, 3) // Open system settings tab
                    } else {
                        val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                        context.startService(intent)
                    }
                }
            )"""

replace_controls = """            AppControls(
                state = timerState, 
                context = context,
                onStartAttempt = {
                    if (protectionStates.needsAttention) {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS) 
                        } catch(e: Exception) {}
                        showJustInTimeSetup = true
                    } else {
                        val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                        context.startService(intent)
                    }
                }
            )"""
content = content.replace(target_controls, replace_controls)

# 4. Add the Dialog at the bottom of HomeScreen
target_dialog = """    if (isAddingTask) {
        AddTaskCard("""

replace_dialog = """    if (showJustInTimeSetup) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showJustInTimeSetup = false }) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(currentTheme.surface)
                    .padding(24.dp)
            ) {
                Column {
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
                                viewModel.setSettingsOpen(true, 3)
                            }
                            showJustInTimeSetup = false
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showJustInTimeSetup = false
                            // Allow them to continue anyway if they really want to ignore it
                            val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                            context.startService(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue without protection", color = currentTheme.textSecondary)
                    }
                }
            }
        }
    }

    if (isAddingTask) {
        AddTaskCard("""
content = content.replace(target_dialog, replace_dialog)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
