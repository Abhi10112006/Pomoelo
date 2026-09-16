import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

target = "view.performHapticFeedback(16) // Error/Confirm haptic to draw attention"
replace = "view.performHapticFeedback(17) // Reject haptic to draw attention"
content = content.replace(target, replace)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
