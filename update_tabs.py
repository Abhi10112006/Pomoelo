import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target = """                items(tabs.size) { index ->
                    val isSelected = selectedTabIndex == index
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = if (isSelected) 4.dp else 1.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = currentTheme.shadowColor
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) currentTheme.primary else currentTheme.surface)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) currentTheme.primary else currentTheme.cardBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { 
                                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                selectedTabIndex = index 
                            }
                    ) {
                        Text(
                            text = tabs[index],
                            color = if (isSelected) Color.White else currentTheme.textPrimary,"""

replacement = """                items(tabs.size) { index ->
                    val isSelected = selectedTabIndex == index
                    val tabBgColor by androidx.compose.animation.animateColorAsState(
                        targetValue = if (isSelected) currentTheme.primary else currentTheme.surface, 
                        animationSpec = androidx.compose.animation.core.spring(stiffness = androidx.compose.animation.core.Spring.StiffnessLow), label = ""
                    )
                    val tabTextColor by androidx.compose.animation.animateColorAsState(
                        targetValue = if (isSelected) Color.White else currentTheme.textPrimary, 
                        animationSpec = androidx.compose.animation.core.spring(stiffness = androidx.compose.animation.core.Spring.StiffnessLow), label = ""
                    )
                    val tabElevation by androidx.compose.animation.core.animateDpAsState(
                        targetValue = if (isSelected) 4.dp else 1.dp, 
                        animationSpec = androidx.compose.animation.core.spring(stiffness = androidx.compose.animation.core.Spring.StiffnessLow), label = ""
                    )
                    
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = tabElevation,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = currentTheme.shadowColor
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(tabBgColor)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) currentTheme.primary else currentTheme.cardBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { 
                                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                selectedTabIndex = index 
                            }
                    ) {
                        Text(
                            text = tabs[index],
                            color = tabTextColor,"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
