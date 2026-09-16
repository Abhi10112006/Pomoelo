import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = """    // Dynamic state trackers for system permissions
    val notificationManagerCompat = remember { androidx.core.app.NotificationManagerCompat.from(context) }
    var areNotificationsEnabled by remember { mutableStateOf(notificationManagerCompat.areNotificationsEnabled()) }
    
    val powerManager = remember { context.getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager }
    var isIgnoringBattery by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                powerManager.isIgnoringBatteryOptimizations(context.packageName)
            } else {
                true
            }
        )
    }
    
    val systemNotificationManager = remember { context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager }
    var isDNDGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemNotificationManager.isNotificationPolicyAccessGranted
            } else {
                true
            }
        )
    }
    
    // Live polling to refresh permission statuses instantly of background setting changes
    LaunchedEffect(Unit) {
        while (true) {
            areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            isIgnoringBattery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                powerManager.isIgnoringBatteryOptimizations(context.packageName)
            } else {
                true
            }
            isDNDGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemNotificationManager.isNotificationPolicyAccessGranted
            } else {
                true
            }
            kotlinx.coroutines.delay(1000)
        }
    }"""

if target in content:
    content = content.replace(target, "")
    with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
        f.write(content)
    print("Success replacing vars")
else:
    print("Target vars not found")
