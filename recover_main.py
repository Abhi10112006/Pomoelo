import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# 1. Fix SettingsOverlay
# The corruption starts at the BoxWithConstraints inside SettingsOverlay.
# It's right after the Text(text = tabs[index] ... ) } } }
# Let's find:
#                         Text(
#                             text = tabs[index],
#                             color = tabTextColor,
#                             modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
#                             fontWeight = FontWeight.Bold,
#                             fontSize = 14.scaledSp
#                         )
#                     }
#                 }
#             }
search_str = """                        Text(
                            text = tabs[index],
                            color = tabTextColor,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.scaledSp
                        )
                    }
                }
            }"""

if search_str not in content:
    print("Could not find start of corruption!")
    exit(1)

start_idx = content.find(search_str) + len(search_str)

# The corruption ends right before @Composable fun TimerDisplay(
end_search_str = """@Composable
fun TimerDisplay("""

end_idx = content.find(end_search_str, start_idx)

if end_idx == -1:
    print("Could not find end of corruption!")
    exit(1)

# We replace everything between start_idx and end_idx with the proper SettingsOverlay Box
correct_box = """
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = selectedTabIndex,
                    transitionSpec = {
                        val isForward = targetState > initialState
                        val enterSpring = spring<androidx.compose.ui.unit.IntOffset>(stiffness = Spring.StiffnessLow)
                        val exitSpring = spring<androidx.compose.ui.unit.IntOffset>(stiffness = Spring.StiffnessMedium)
                        
                        if (isForward) {
                            (slideInHorizontally(animationSpec = enterSpring) { it } + fadeIn()) togetherWith 
                            (slideOutHorizontally(animationSpec = exitSpring) { -it } + fadeOut())
                        } else {
                            (slideInHorizontally(animationSpec = enterSpring) { -it } + fadeIn()) togetherWith 
                            (slideOutHorizontally(animationSpec = exitSpring) { it } + fadeOut())
                        }
                    },
                    label = "tabContent"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> com.example.ui.SettingsTabGeneral()
                        1 -> com.example.ui.SettingsTabTheme()
                        2 -> com.example.ui.SettingsTabAdvanced()
                    }
                }
            }
        }
    }
}

"""

new_content = content[:start_idx] + correct_box + content[end_idx:]

# 2. Append missing functions
missing_funcs = """
@Composable
fun TaskItemRow(
    task: com.example.data.TaskItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) currentTheme.surface else currentTheme.surface.copy(alpha = 0.5f)
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(task.categoryColor).copy(alpha = 0.5f)) else null
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.scaledDp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(task.categoryColor)))
                    Text(task.name, fontSize = 15.scaledSp, fontWeight = FontWeight.Bold, color = currentTheme.textPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(task.category, fontSize = 12.scaledSp, color = currentTheme.textSecondary)
            }
            if (isSelected) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Selected", tint = Color(task.categoryColor))
            } else {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = currentTheme.textSecondary)
                }
            }
        }
    }
}

@Composable
fun AddTaskCard(
    onSave: (String, String, Long) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val currentTheme = LocalAppTheme.current
    val colors = listOf(0xFF4CAF50, 0xFF2196F3, 0xFFFF9800, 0xFFE91E63, 0xFF9C27B0, 0xFF00BCD4)
    var selectedColor by remember { mutableStateOf(colors[0]) }

    androidx.compose.ui.window.Dialog(onDismissRequest = onCancel) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = currentTheme.surface)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("New Task", fontSize = 20.scaledSp, fontWeight = FontWeight.Bold, color = currentTheme.textPrimary)
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = currentTheme.primary,
                        focusedLabelColor = currentTheme.primary,
                        unfocusedTextColor = currentTheme.textPrimary,
                        focusedTextColor = currentTheme.textPrimary
                    )
                )
                
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = currentTheme.primary,
                        focusedLabelColor = currentTheme.primary,
                        unfocusedTextColor = currentTheme.textPrimary,
                        focusedTextColor = currentTheme.textPrimary
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    colors.forEach { colorVal ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(colorVal))
                                .clickable { selectedColor = colorVal }
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == colorVal) currentTheme.textPrimary else Color.Transparent,
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onCancel) { Text("Cancel", color = currentTheme.textSecondary) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { if (name.isNotBlank() && category.isNotBlank()) onSave(name, category, selectedColor) },
                        colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary)
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun AppControls(
    state: TimerManager.TimerState,
    context: android.content.Context,
    onStartAttempt: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isRunning = state == TimerManager.TimerState.RUNNING
        
        if (state != TimerManager.TimerState.STOPPED) {
            AnimatedScaleBox(
                enabled = true,
                shape = CircleShape,
                elevation = 2.dp,
                shadowColor = Color.Black.copy(alpha = 0.1f),
                onClick = {
                    val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_STOP }
                    context.startService(intent)
                }
            ) {
                Box(
                    modifier = Modifier.size(56.dp).background(currentTheme.surface, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Stop, contentDescription = "Stop", tint = currentTheme.textPrimary)
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
        }

        AnimatedScaleBox(
            enabled = true,
            shape = CircleShape,
            elevation = 6.dp,
            shadowColor = currentTheme.primary.copy(alpha = 0.4f),
            onClick = {
                if (state == TimerManager.TimerState.STOPPED) {
                    onStartAttempt()
                } else if (isRunning) {
                    val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_PAUSE }
                    context.startService(intent)
                } else {
                    val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                    context.startService(intent)
                }
            }
        ) {
            Box(
                modifier = Modifier.size(72.dp).background(currentTheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(targetState = isRunning, label = "playPause") { running ->
                    Icon(
                        if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (running) "Pause" else "Start",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedScaleBox(
    enabled: Boolean = true,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp),
    elevation: androidx.compose.ui.unit.Dp = 0.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.2f),
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .shadow(elevation, shape, spotColor = shadowColor)
            .clip(shape)
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            try {
                                awaitRelease()
                            } finally {
                                isPressed = false
                            }
                        },
                        onTap = { onClick() }
                    )
                }
            }
    ) {
        content()
    }
}

@Composable
fun SlidingTimer(timeRemaining: Int, fontSize: androidx.compose.ui.unit.TextUnit) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val m = timeRemaining / 60
    val s = timeRemaining % 60
    val timeStr = String.format("%02d:%02d", m, s)
    
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        timeStr.forEachIndexed { index, char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    val enterSpring = spring<androidx.compose.ui.unit.IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                    val exitSpring = spring<androidx.compose.ui.unit.IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
                    
                    if (targetState < initialState || (initialState == '0' && targetState == '9') || (initialState == '0' && targetState == '5')) {
                        (slideInVertically(enterSpring) { h -> -h } + fadeIn()) togetherWith 
                                (slideOutVertically(exitSpring) { h -> h } + fadeOut())
                    } else {
                        (slideInVertically(enterSpring) { h -> h } + fadeIn()) togetherWith 
                                (slideOutVertically(exitSpring) { h -> -h } + fadeOut())
                    }.using(SizeTransform(clip = false))
                },
                label = "digitAnimation"
            ) { targetDigit ->
                Text(
                    text = targetDigit.toString(),
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary
                )
            }
        }
    }
}
"""

new_content += missing_funcs

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(new_content)

