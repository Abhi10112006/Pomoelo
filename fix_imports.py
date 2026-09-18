import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

# Separate imports and body
lines = content.split('\n')
imports = []
rest = []
for line in lines:
    if line.startswith('import '):
        if line not in imports:
            imports.append(line)
    else:
        rest.append(line)

final_content = ""
for line in rest:
    if line.startswith('package '):
        final_content += line + "\n\n"
        final_content += "\n".join(imports) + "\n\n"
    else:
        final_content += line + "\n"

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(final_content)
