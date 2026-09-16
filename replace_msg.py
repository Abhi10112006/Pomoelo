import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

target = """@Composable
fun ContextualSetupMessage(
    state: SystemProtectionStates,
    onFixClicked: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    if (state.notificationsState == PermissionState.ACTION_REQUIRED) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (currentTheme.isDark) Color(0xFF3E2723) else Color(0xFFFFF3E0))
                .border(1.dp, if (currentTheme.isDark) Color(0xFF5D4037) else Color(0xFFFFCC80), RoundedCornerShape(12.dp))
                .clickable { onFixClicked() }
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
                    text = "PomoPal needs notification access",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentTheme.isDark) Color(0xFFFFE0B2) else Color(0xFFE65100)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Allow notifications so PomoPal can let you know when your focus session is complete.",
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
}"""

replacement = """@Composable
fun ContextualSetupMessage(
    state: SystemProtectionStates,
    onSetup: (String) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    
    val title: String
    val desc: String
    val actionId: String
    
    when {
        state.notificationsState == PermissionState.ACTION_REQUIRED -> {
            title = "PomoPal needs notification access"
            desc = "Allow notifications so PomoPal can let you know when your focus session is complete."
            actionId = "NOTIFICATIONS"
        }
        state.dndState == PermissionState.ACTION_REQUIRED -> {
            title = "PomoPal needs Do Not Disturb access"
            desc = "Allow Do Not Disturb access so PomoPal can silence distractions during your focus session."
            actionId = "DND"
        }
        state.batteryState == PermissionState.ACTION_REQUIRED -> {
            title = "PomoPal needs background reliability"
            desc = "Disable battery optimization so your timer doesn't stop unexpectedly when the screen is off."
            actionId = "BATTERY"
        }
        else -> return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
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
}"""

if target in content:
    content = content.replace(target, replacement)
    with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
        f.write(content)
    print("Replaced ContextualSetupMessage")
else:
    print("Could not find target block")

