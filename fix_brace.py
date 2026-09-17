import re

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Let's find "Icon(androidx.compose.material.icons.Icons.Filled.Settings, contentDescription = "Manage", tint = currentTheme.textPrimary)" and check the braces after it.
pattern = re.compile(r'(Icon\(androidx\.compose\.material\.icons\.Icons\.Filled\.Settings, contentDescription = "Manage", tint = currentTheme\.textPrimary\)\s*\}\s*\})(\s*if \(tabIndex == 3\) \{)')

content = pattern.sub(r'\1\n            } // Close if (tabIndex == 2)\n\2', content)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
