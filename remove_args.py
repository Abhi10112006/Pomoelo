import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = """            if (tabIndex == 3) {
                com.example.ui.SystemProtectionScreen(
                    areNotificationsEnabled = areNotificationsEnabled,
                    isIgnoringBattery = isIgnoringBattery,
                    isDNDGranted = isDNDGranted,
                    context = context,
                    view = view
                )
            } // Close if (selectedTabIndex == 3)"""

replacement = """            if (tabIndex == 3) {
                com.example.ui.SystemProtectionScreen()
            } // Close if (selectedTabIndex == 3)"""

if target in content:
    content = content.replace(target, replacement)
    with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
        f.write(content)
    print("Success replacing args")
else:
    print("Target args not found")
