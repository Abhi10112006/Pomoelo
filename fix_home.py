import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# 1. Add val protectionStates in HomeScreen
target_1 = """    val settingsInitialTab by viewModel.settingsTab.collectAsState()
    var showFullScreenSeriousness by remember { mutableStateOf(false) }"""
replace_1 = """    val settingsInitialTab by viewModel.settingsTab.collectAsState()
    var showFullScreenSeriousness by remember { mutableStateOf(false) }
    val protectionStates = com.example.ui.rememberSystemProtectionStates()"""
content = content.replace(target_1, replace_1)

# 2. Inject ContextualSetupMessage and Banner inside Column
target_2 = """            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row("""
replace_2 = """            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            com.example.ui.ContextualSetupMessage(
                state = protectionStates,
                onFixClicked = {
                    viewModel.setSettingsOpen(true, 3)
                }
            )
            if (protectionStates.notificationsState == com.example.ui.PermissionState.READY && protectionStates.needsAttention) {
                com.example.ui.SystemProtectionBanner(
                    state = protectionStates,
                    onFixClicked = { viewModel.setSettingsOpen(true, 3) }
                )
            }

            Row("""
content = content.replace(target_2, replace_2)

# 3. Update AppControls call
target_3 = "AppControls(state = timerState, context = context)"
replace_3 = """AppControls(
                state = timerState, 
                context = context,
                onStartAttempt = {
                    if (protectionStates.needsAttention) {
                        try {
                            view.performHapticFeedback(16) // Error/Confirm haptic to draw attention
                        } catch(e: Exception) {}
                        viewModel.setSettingsOpen(true, 3) // Open system settings tab
                    } else {
                        val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                        context.startService(intent)
                    }
                }
            )"""
content = content.replace(target_3, replace_3)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
