import re

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

pattern = re.compile(r"val lifecycleOwner = androidx\.lifecycle\.compose\.LocalLifecycleOwner\.current.*?if \(!hasAccessibilityPermission\) \{.*?\} else \{.*?\}", re.DOTALL)

new_content = """val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                var accessibilityStatus by remember { mutableStateOf(com.example.service.AppBlockerManager.getAccessibilityStatus(context)) }
                
                DisposableEffect(lifecycleOwner) {
                    val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                        if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME || event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                            accessibilityStatus = com.example.service.AppBlockerManager.getAccessibilityStatus(context)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                if (accessibilityStatus == com.example.service.AppBlockerManager.AccessibilityServiceStatus.DISABLED) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = currentTheme.primary.copy(alpha = 0.1f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.primary.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(androidx.compose.material.icons.Icons.Rounded.Security, contentDescription = null, tint = currentTheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Focus Shield needs one small permission", fontWeight = FontWeight.Bold, color = currentTheme.primary, fontSize = 14.scaledSp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "To stop selected distracting apps during a focus session, PomoPal uses Android's Accessibility Service. This lets PomoPal detect when a protected app is opened and show the mindful pause screen.",
                                fontSize = 12.scaledSp,
                                color = currentTheme.textPrimary,
                                lineHeight = 16.scaledSp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { 
                                    try {
                                        context.startActivity(android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                    } catch (e: Exception) {}
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary, contentColor = Color.White),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Set Up Focus Shield →", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else if (accessibilityStatus == com.example.service.AppBlockerManager.AccessibilityServiceStatus.READY) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(androidx.compose.material.icons.Icons.Rounded.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Focus Shield is Ready ✓", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), fontSize = 14.scaledSp)
                        }
                    }
                } else if (accessibilityStatus == com.example.service.AppBlockerManager.AccessibilityServiceStatus.STALE || accessibilityStatus == com.example.service.AppBlockerManager.AccessibilityServiceStatus.UNKNOWN) {
                    val isUnknown = accessibilityStatus == com.example.service.AppBlockerManager.AccessibilityServiceStatus.UNKNOWN
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF57C00).copy(alpha = 0.1f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF57C00).copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = Color(0xFFF57C00), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (isUnknown) "Unable to verify Focus Shield" else "Focus Shield needs attention", fontWeight = FontWeight.Bold, color = Color(0xFFF57C00), fontSize = 14.scaledSp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "The accessibility service appears to be interrupted. Please check its status in Android Settings to ensure blocking works.",
                                fontSize = 12.scaledSp,
                                color = currentTheme.textPrimary,
                                lineHeight = 16.scaledSp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { accessibilityStatus = com.example.service.AppBlockerManager.getAccessibilityStatus(context) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = currentTheme.textPrimary)
                                ) {
                                    Text("Check Again", fontWeight = FontWeight.Bold, fontSize = 12.scaledSp)
                                }
                                Button(
                                    onClick = { 
                                        try {
                                            context.startActivity(android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                        } catch (e: Exception) {}
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00), contentColor = Color.White),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Open Settings", fontWeight = FontWeight.Bold, fontSize = 12.scaledSp)
                                }
                            }
                        }
                    }
                }"""

content = pattern.sub(new_content, content)

with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
