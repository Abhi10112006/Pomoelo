import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

sig_target = "fun SettingsOverlay(onDismiss: () -> Unit) {"
sig_replacement = "fun SettingsOverlay(onDismiss: () -> Unit, initialTab: Int = 0) {"
content = content.replace(sig_target, sig_replacement)

# Now fix the invocation
invoc_target = "SettingsOverlay(onDismiss = { viewModel.setSettingsOpen(false) })"
invoc_replacement = "SettingsOverlay(onDismiss = { viewModel.setSettingsOpen(false) }, initialTab = settingsInitialTab)"
content = content.replace(invoc_target, invoc_replacement)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
