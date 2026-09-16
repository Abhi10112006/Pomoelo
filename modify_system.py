import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionScreen.kt', 'r') as f:
    content = f.read()

# We need to replace the local state and checkPermissions logic with rememberSystemProtectionStates()

# Find the start of SystemProtectionScreen
start_idx = content.find("fun SystemProtectionScreen() {")
if start_idx == -1:
    print("Could not find SystemProtectionScreen")
    sys.exit(1)

# Find the end of checkPermissions block and LaunchedEffect logic
end_idx = content.find("var visible by remember")
if end_idx == -1:
    print("Could not find var visible by remember")
    sys.exit(1)

new_code = """fun SystemProtectionScreen() {
    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentTheme = LocalAppTheme.current
    
    val protectionStates = rememberSystemProtectionStates()
    val notificationsState = protectionStates.notificationsState
    val batteryState = protectionStates.batteryState
    val dndState = protectionStates.dndState
    
    """

content = content[:start_idx] + new_code + content[end_idx:]

with open('./app/src/main/java/com/example/ui/SystemProtectionScreen.kt', 'w') as f:
    f.write(content)
print("Replaced logic in SystemProtectionScreen")
