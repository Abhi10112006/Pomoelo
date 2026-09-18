import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

with open('target.txt', 'r') as f:
    target = f.read()

replacement = """            if (tabIndex == 3) {
                com.example.ui.SystemProtectionScreen(
                    areNotificationsEnabled = areNotificationsEnabled,
                    isIgnoringBattery = isIgnoringBattery,
                    isDNDGranted = isDNDGranted,
                    context = context,
                    view = view
                )
            } // Close if (selectedTabIndex == 3)"""

if target in content:
    content = content.replace(target, replacement)
    with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
        f.write(content)
    print("Success")
else:
    print("Target not found")
