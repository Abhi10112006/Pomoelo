import re

with open('./app/src/main/java/com/example/ui/BlockedAppsScreen.kt', 'r') as f:
    content = f.read()

pattern1 = re.compile(r"    // Check accessibility permission\n    var hasAccessibilityPermission by remember \{ mutableStateOf\(isAccessibilityServiceEnabled\(context\)\) \}\n\n    LaunchedEffect\(Unit\) \{\n        hasAccessibilityPermission = isAccessibilityServiceEnabled\(context\)")

replacement1 = """    // Check accessibility status
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var accessibilityStatus by remember { mutableStateOf(AppBlockerManager.getAccessibilityStatus(context)) }

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME || event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                accessibilityStatus = AppBlockerManager.getAccessibilityStatus(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {"""

content = pattern1.sub(replacement1, content)

pattern2 = re.compile(r"                    val statusIcon = when \{\n                        !hasAccessibilityPermission -> \{\n                            statusText = \"Permission Needed\"\n                            statusColor = Color\(0xFFE53935\)\n                            Icons\.Filled\.ErrorOutline\n                        \}")

replacement2 = """                    val statusIcon = when {
                        accessibilityStatus == AppBlockerManager.AccessibilityServiceStatus.DISABLED -> {
                            statusText = "Permission Needed"
                            statusColor = Color(0xFFE53935)
                            Icons.Filled.ErrorOutline
                        }
                        accessibilityStatus == AppBlockerManager.AccessibilityServiceStatus.STALE || accessibilityStatus == AppBlockerManager.AccessibilityServiceStatus.UNKNOWN -> {
                            statusText = "Service Interrupted"
                            statusColor = Color(0xFFF57C00)
                            Icons.Filled.ErrorOutline
                        }"""

content = pattern2.sub(replacement2, content)

with open('./app/src/main/java/com/example/ui/BlockedAppsScreen.kt', 'w') as f:
    f.write(content)
