import re

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

target = r"@Composable\s+fun ContextualSetupMessage[\s\S]*"

new_content = """@Composable
fun ContextualSetupMessage(
    state: SystemProtectionStates,
    onSetup: (String) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    
    val items = mutableListOf<Triple<String, String, String>>()
    if (state.notificationsState == PermissionState.ACTION_REQUIRED) {
        items.add(Triple("PomoPal needs notification access", "Allow notifications so PomoPal can let you know when your focus session is complete.", "NOTIFICATIONS"))
    }
    if (state.dndState == PermissionState.ACTION_REQUIRED) {
        items.add(Triple("PomoPal needs Do Not Disturb access", "Allow Do Not Disturb access so PomoPal can silence distractions during your focus session.", "DND"))
    }
    if (state.batteryState == PermissionState.ACTION_REQUIRED) {
        items.add(Triple("PomoPal needs background reliability", "Disable battery optimization so your timer doesn't stop unexpectedly when the screen is off.", "BATTERY"))
    }

    Column {
        items.forEach { (title, desc, actionId) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (currentTheme.isDark) Color(0xFF3E2723) else Color(0xFFFFF3E0))
                    .border(1.dp, if (currentTheme.isDark) Color(0xFF5D4037) else Color(0xFFFFCC80), RoundedCornerShape(12.dp))
                    .clickable { onSetup(actionId) }
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = if (currentTheme.isDark) Color(0xFFFFB74D) else Color(0xFFF57C00),
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 13.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = if (currentTheme.isDark) Color(0xFFFFE0B2) else Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        fontSize = 11.scaledSp,
                        color = if (currentTheme.isDark) Color(0xFFFFCC80) else Color(0xFFEF6C00),
                        lineHeight = 16.scaledSp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Set Up",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentTheme.isDark) Color(0xFFFFB74D) else Color(0xFFF57C00),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}"""

content = re.sub(target, new_content, content)

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)

