import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

content = content.replace("android.view.HapticFeedbackConstants.CONFIRM", "16 /* CONFIRM api 30+ */")

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)
