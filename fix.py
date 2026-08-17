import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    lines = f.readlines()

new_lines = []
in_composable = False
for i, line in enumerate(lines):
    new_lines.append(line)
    if "@Composable" in line:
        in_composable = True
    
    if in_composable and "{" in line and "fun " in line:
        new_lines.append("    val context = androidx.compose.ui.platform.LocalContext.current\n")
        in_composable = False
    elif in_composable and "fun " in line and "{" not in line:
        # function signature spans multiple lines
        pass
    elif in_composable and "{" in line:
        new_lines.append("    val context = androidx.compose.ui.platform.LocalContext.current\n")
        in_composable = False

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.writelines(new_lines)
