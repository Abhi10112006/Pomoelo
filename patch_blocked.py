import re

with open('./app/src/main/java/com/example/ui/BlockedAppsScreen.kt', 'r') as f:
    content = f.read()

# We want to remove the block:
#                 // 1. Accessibility Service Warning Banner (if disabled)
#                 if (!hasAccessibilityPermission) { ... }
# Up to // 2. Real-Time Status & Daily Stats Dashboard

pattern = re.compile(r"(\s*// 1\. Accessibility Service Warning Banner \(if disabled\).*?)(?=\s*// 2\. Real-Time Status & Daily Stats Dashboard)", re.DOTALL)
content, num_subs = pattern.subn("", content)

if num_subs > 0:
    print(f"Successfully removed {num_subs} banner block(s).")
    with open('./app/src/main/java/com/example/ui/BlockedAppsScreen.kt', 'w') as f:
        f.write(content)
else:
    print("Could not find banner block to remove.")

