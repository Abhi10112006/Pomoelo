import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

target = "    return SystemProtectionStates(notificationsState, batteryState, dndState)\n}"
replacement = """    val view = androidx.compose.ui.platform.LocalView.current
    
    // Premium feedback when state changes to READY
    androidx.compose.runtime.LaunchedEffect(notificationsState) {
        if (notificationsState == PermissionState.READY) {
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM) } catch (e: Exception) {}
        }
    }
    androidx.compose.runtime.LaunchedEffect(batteryState) {
        if (batteryState == PermissionState.READY) {
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM) } catch (e: Exception) {}
        }
    }
    androidx.compose.runtime.LaunchedEffect(dndState) {
        if (dndState == PermissionState.READY) {
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM) } catch (e: Exception) {}
        }
    }

    return SystemProtectionStates(notificationsState, batteryState, dndState)
}"""
content = content.replace(target, replacement)

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)
