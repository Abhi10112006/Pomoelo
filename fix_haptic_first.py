import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

target = """    // Premium feedback when state changes to READY
    androidx.compose.runtime.LaunchedEffect(notificationsState) {
        if (notificationsState == PermissionState.READY) {
            try { view.performHapticFeedback(16 /* CONFIRM api 30+ */) } catch (e: Exception) {}
        }
    }
    androidx.compose.runtime.LaunchedEffect(batteryState) {
        if (batteryState == PermissionState.READY) {
            try { view.performHapticFeedback(16 /* CONFIRM api 30+ */) } catch (e: Exception) {}
        }
    }
    androidx.compose.runtime.LaunchedEffect(dndState) {
        if (dndState == PermissionState.READY) {
            try { view.performHapticFeedback(16 /* CONFIRM api 30+ */) } catch (e: Exception) {}
        }
    }"""

replacement = """    // Premium feedback when state changes to READY
    var hasInitialized by remember { mutableStateOf(false) }
    
    androidx.compose.runtime.LaunchedEffect(notificationsState, batteryState, dndState) {
        if (hasInitialized) {
            if (notificationsState == PermissionState.READY || batteryState == PermissionState.READY || dndState == PermissionState.READY) {
                try { view.performHapticFeedback(16) } catch (e: Exception) {}
            }
        }
        hasInitialized = true
    }"""
content = content.replace(target, replacement)

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)
