import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

target = """    // Premium feedback when state changes to READY
    var hasInitialized by remember { mutableStateOf(false) }
    
    androidx.compose.runtime.LaunchedEffect(notificationsState, batteryState, dndState) {
        if (hasInitialized) {
            if (notificationsState == PermissionState.READY || batteryState == PermissionState.READY || dndState == PermissionState.READY) {
                try { view.performHapticFeedback(16) } catch (e: Exception) {}
            }
        }
        hasInitialized = true
    }"""

replacement = """    // Premium feedback when state changes to READY
    var prevNotif by remember { mutableStateOf(notificationsState) }
    var prevBattery by remember { mutableStateOf(batteryState) }
    var prevDnd by remember { mutableStateOf(dndState) }
    
    androidx.compose.runtime.LaunchedEffect(notificationsState, batteryState, dndState) {
        val becameReady = 
            (prevNotif != PermissionState.READY && notificationsState == PermissionState.READY) ||
            (prevBattery != PermissionState.READY && batteryState == PermissionState.READY) ||
            (prevDnd != PermissionState.READY && dndState == PermissionState.READY)
            
        if (becameReady) {
            try { view.performHapticFeedback(16) } catch (e: Exception) {}
        }
        
        prevNotif = notificationsState
        prevBattery = batteryState
        prevDnd = dndState
    }"""
content = content.replace(target, replacement)

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)
