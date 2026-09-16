import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

target = """@Composable
fun ContextualSetupMessage(
    state: SystemProtectionStates,
    onFixClicked: () -> Unit
) {"""

# We'll replace the whole ContextualSetupMessage function
end_target = "}\n}\n"

start_idx = content.find(target)
if start_idx == -1:
    print("Could not find ContextualSetupMessage")
    sys.exit(1)
end_idx = content.find("}", start_idx + len(target))
while True:
    next_close = content.find("}", end_idx + 1)
    if next_close == -1 or next_close - end_idx > 500:
        break
    end_idx = next_close
# Wait, this is tricky to replace cleanly. Let's use regex or just slice manually.
