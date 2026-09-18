import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionScreen.kt', 'r') as f:
    content = f.read()

enum_def = """enum class PermissionState {
    READY,
    ACTION_REQUIRED,
    UNKNOWN
}"""

content = content.replace(enum_def, "")

with open('./app/src/main/java/com/example/ui/SystemProtectionScreen.kt', 'w') as f:
    f.write(content)
