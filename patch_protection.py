import re

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

old_banner = """@Composable
fun SystemProtectionBanner(
    state: SystemProtectionStates,
    onFixClicked: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val context = LocalContext.current
    var hasAchievedFullProtection by remember {
        mutableStateOf(SettingsManager.hasAchievedFullProtection(context))
    }

    LaunchedEffect(state.needsAttention) {
        if (!state.needsAttention && !hasAchievedFullProtection) {
            hasAchievedFullProtection = true
            SettingsManager.setHasAchievedFullProtection(context, true)
        }
    }

    if (state.needsAttention) {
        val isFirstTime = !hasAchievedFullProtection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isFirstTime) currentTheme.primary.copy(alpha = 0.1f) else currentTheme.surface)
                .border(1.dp, if (isFirstTime) currentTheme.primary.copy(alpha = 0.3f) else Color(0xFFE53935).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable { onFixClicked() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isFirstTime) Icons.Rounded.Security else Icons.Rounded.Warning,
                contentDescription = null,
                tint = if (isFirstTime) currentTheme.primary else Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isFirstTime) "Supercharge your focus" else "System protection needs attention",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
                if (isFirstTime) {
                    Text(
                        text = "Set up protection to block distractions",
                        fontSize = 11.scaledSp,
                        color = currentTheme.textSecondary
                    )
                }
            }
            Text(
                text = if (isFirstTime) "Set Up" else "Fix",
                fontSize = 13.scaledSp,
                fontWeight = FontWeight.Bold,
                color = if (isFirstTime) currentTheme.primary else Color(0xFFE53935)
            )
        }
    }
}"""

new_banner = """@Composable
fun SystemProtectionBanner(
    state: SystemProtectionStates,
    onFixClicked: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val context = LocalContext.current
    var hasAchievedFullProtection by remember {
        mutableStateOf(SettingsManager.hasAchievedFullProtection(context))
    }

    LaunchedEffect(state.needsAttention) {
        if (!state.needsAttention && !hasAchievedFullProtection) {
            hasAchievedFullProtection = true
            SettingsManager.setHasAchievedFullProtection(context, true)
        }
    }

    var showBanner by remember { mutableStateOf(false) }

    LaunchedEffect(hasAchievedFullProtection, state.needsAttention) {
        if (state.needsAttention) {
            if (!hasAchievedFullProtection) {
                kotlinx.coroutines.delay(2500)
                showBanner = true
            } else {
                showBanner = true
            }
        } else {
            showBanner = false
        }
    }

    if (showBanner && state.needsAttention) {
        val isFirstTime = !hasAchievedFullProtection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isFirstTime) currentTheme.primary.copy(alpha = 0.1f) else currentTheme.surface)
                .border(1.dp, if (isFirstTime) currentTheme.primary.copy(alpha = 0.3f) else Color(0xFFE53935).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable { onFixClicked() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isFirstTime) Icons.Rounded.Security else Icons.Rounded.Warning,
                contentDescription = null,
                tint = if (isFirstTime) currentTheme.primary else Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isFirstTime) "Supercharge your focus" else "System protection needs attention",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
                if (isFirstTime) {
                    Text(
                        text = "Set up protection to block distractions",
                        fontSize = 11.scaledSp,
                        color = currentTheme.textSecondary
                    )
                }
            }
            Text(
                text = if (isFirstTime) "Set Up" else "Fix",
                fontSize = 13.scaledSp,
                fontWeight = FontWeight.Bold,
                color = if (isFirstTime) currentTheme.primary else Color(0xFFE53935)
            )
        }
    }
}"""

content = content.replace(old_banner, new_banner)

old_contextual = """@Composable
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

new_contextual = """@Composable
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

content = content.replace(old_contextual, new_contextual)

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)
