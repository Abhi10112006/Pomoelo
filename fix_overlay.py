import sys

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

sig_target = "fun SettingsOverlay(onDismiss: () -> Unit, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit, currentFont: androidx.compose.ui.text.font.FontFamily, onFontChange: (androidx.compose.ui.text.font.FontFamily) -> Unit) {"
sig_replacement = "fun SettingsOverlay(onDismiss: () -> Unit, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit, currentFont: androidx.compose.ui.text.font.FontFamily, onFontChange: (androidx.compose.ui.text.font.FontFamily) -> Unit, initialTab: Int = 0) {"
content = content.replace(sig_target, sig_replacement)

tab_target = "var selectedTabIndex by remember { mutableStateOf(0) }"
tab_replacement = "var selectedTabIndex by remember { mutableStateOf(initialTab) }"
content = content.replace(tab_target, tab_replacement)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
