import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Change AppControls signature
target_sig = "fun AppControls(state: TimerManager.TimerState, context: android.content.Context) {"
replace_sig = "fun AppControls(state: TimerManager.TimerState, context: android.content.Context, onStartAttempt: () -> Unit = {}) {"
content = content.replace(target_sig, replace_sig)

# Change the onClick for Play/Pause
target_onClick = """            onClick = {
                val action = if (isRunning) TimerService.ACTION_PAUSE else TimerService.ACTION_START
                val intent = Intent(context, TimerService::class.java).apply { this.action = action }
                context.startService(intent)
            }"""
replace_onClick = """            onClick = {
                if (isRunning) {
                    val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_PAUSE }
                    context.startService(intent)
                } else {
                    onStartAttempt()
                }
            }"""
content = content.replace(target_onClick, replace_onClick)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
