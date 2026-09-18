import re

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

if "import com.example.service.SettingsManager" not in content:
    content = content.replace("import com.example.ui.theme.LocalAppTheme", "import com.example.ui.theme.LocalAppTheme\nimport com.example.service.SettingsManager\nimport androidx.compose.material.icons.rounded.Security")

old_banner = """@Composable
fun SystemProtectionBanner(
    state: SystemProtectionStates,
    onFixClicked: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    if (state.needsAttention) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(currentTheme.surface)
                .border(1.dp, Color(0xFFE53935).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable { onFixClicked() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = null,
                tint = Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "System protection needs attention",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
            }
            Text(
                text = "Fix",
                fontSize = 13.scaledSp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53935)
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

content = content.replace(old_banner, new_banner)

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)
