import re

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'r') as f:
    content = f.read()

old_banner_bottom = """    if (showBanner && state.needsAttention) {
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
    }"""

new_banner_bottom = """    androidx.compose.animation.AnimatedVisibility(
        visible = showBanner && state.needsAttention,
        enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
        exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
    ) {
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
    }"""

content = content.replace(old_banner_bottom, new_banner_bottom)

with open('./app/src/main/java/com/example/ui/SystemProtectionState.kt', 'w') as f:
    f.write(content)
