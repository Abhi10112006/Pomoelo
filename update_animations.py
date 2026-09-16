import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionScreen.kt', 'r') as f:
    content = f.read()

# Add remember for visible state
old_top = """
    val currentTheme = LocalAppTheme.current
    val allGranted = areNotificationsEnabled && isIgnoringBattery && isDNDGranted

    Column(
"""
new_top = """
    val currentTheme = LocalAppTheme.current
    val allGranted = areNotificationsEnabled && isIgnoringBattery && isDNDGranted
    
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
"""
content = content.replace(old_top, new_top)

# Wrap contents in AnimatedVisibility
# I will use a simple AnimatedVisibility for the whole column with slide & fade
old_col = """
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
"""

new_col = """
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + slideInVertically(tween(500, easing = DecelerateEasing), initialOffsetY = { it / 4 })
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
"""
content = content.replace(old_col, new_col)

# Close the AnimatedVisibility bracket
content = content.replace("        if (!isDNDGranted) {\n            SideloadedAppTip()\n        }\n    }\n}\n", "        if (!isDNDGranted) {\n            SideloadedAppTip()\n        }\n        }\n    }\n}\n")

with open('./app/src/main/java/com/example/ui/SystemProtectionScreen.kt', 'w') as f:
    f.write(content)
