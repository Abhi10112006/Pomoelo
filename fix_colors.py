import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    text = f.read()

# Replace hardcoded colors in Cards inside SettingsOverlay
text = re.sub(r'colors = CardDefaults.cardColors\(containerColor = Color\.White(?:.copy\(alpha = [0-9.]+\))?\),', 
              r'colors = CardDefaults.cardColors(containerColor = currentTheme.surface),', text)

text = re.sub(r'border = androidx\.compose\.foundation\.BorderStroke\(1\.dp, Color\(0xFF[0-9A-F]+\)\.copy\(alpha = [0-9.]+\)\)',
              r'border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder)', text)

# Text colors for headers
text = re.sub(r'color = Color\(0xFF5D4037\)', 'color = currentTheme.textPrimary', text)
text = re.sub(r'color = Color\.DarkGray', 'color = currentTheme.textSecondary', text)
text = re.sub(r'color = Color\.Gray', 'color = currentTheme.textSecondary', text)
text = re.sub(r'color = Color\.Black\.copy\(alpha=0\.05f\)', 'color = currentTheme.textPrimary.copy(alpha = 0.1f)', text)
text = re.sub(r'Color\.Black\.copy\(alpha=0\.05f\)', 'currentTheme.textPrimary.copy(alpha=0.05f)', text)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(text)
