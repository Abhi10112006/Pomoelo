import re

with open('app/src/main/java/com/example/ui/components/AppCustomizerScreen.kt', 'r') as f:
    text = f.read()

# Add imports for animations and haptics if missing
if 'import android.view.HapticFeedbackConstants' not in text:
    text = text.replace('import androidx.compose.ui.Modifier', 'import android.view.HapticFeedbackConstants\nimport androidx.compose.ui.Modifier')
if 'import androidx.compose.animation.core.*' not in text:
    text = text.replace('import androidx.compose.ui.Modifier', 'import androidx.compose.animation.core.*\nimport androidx.compose.ui.Modifier')
if 'import androidx.compose.ui.platform.LocalView' not in text:
    text = text.replace('import androidx.compose.ui.Modifier', 'import androidx.compose.ui.platform.LocalView\nimport androidx.compose.ui.Modifier')
if 'import androidx.compose.material3.CircularProgressIndicator' not in text:
    text = text.replace('import androidx.compose.ui.Modifier', 'import androidx.compose.material3.CircularProgressIndicator\nimport androidx.compose.ui.Modifier')
if 'import androidx.compose.ui.graphics.StrokeCap' not in text:
    text = text.replace('import androidx.compose.ui.Modifier', 'import androidx.compose.ui.graphics.StrokeCap\nimport androidx.compose.ui.Modifier')

# Find Live Preview area block
# Replace it with a new dynamic one

preview_old = """        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = currentTheme.background),
            border = BorderStroke(1.dp, currentTheme.cardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Fake Timer Ring
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(4.dp, currentTheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "25:00",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textPrimary
                        )
                    }
                    
                    // Fake Pills
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(currentTheme.secondary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Secondary", color = currentTheme.secondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .background(currentTheme.accent.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Accent", color = currentTheme.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }"""

preview_new = """        val infiniteTransition = rememberInfiniteTransition(label = "preview_pulse")
        val progress by infiniteTransition.animateFloat(
            initialValue = 0.75f,
            targetValue = 0.95f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "progress"
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = currentTheme.background),
            border = BorderStroke(1.dp, currentTheme.cardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Focus Session",
                        color = currentTheme.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    
                    // Animated Timer Arc
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.size(110.dp),
                            color = currentTheme.primary.copy(alpha = 0.15f),
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Round
                        )
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.size(110.dp),
                            color = currentTheme.primary,
                            strokeWidth = 8.dp,
                            strokeCap = StrokeCap.Round
                        )
                        Text(
                            text = "25:00",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textPrimary
                        )
                    }
                    
                    // Start Button
                    Box(
                        modifier = Modifier
                            .background(currentTheme.primary, RoundedCornerShape(20.dp))
                            .padding(horizontal = 28.dp, vertical = 12.dp)
                    ) {
                        Text("Start Focus", color = if (currentTheme.primary.luminance() > 0.5f) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    
                    // Mini Task Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .background(currentTheme.surface, RoundedCornerShape(12.dp))
                            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(16.dp).border(2.dp, currentTheme.secondary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Deep Work Phase", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = currentTheme.textPrimary)
                            Text("Productivity", fontSize = 9.sp, color = currentTheme.accent)
                        }
                    }
                }
            }
        }"""

if preview_old in text:
    text = text.replace(preview_old, preview_new)

# Add Haptics in AppearanceOption
app_opt_old = """    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { SettingsManager.setAppearanceMode(mode) }"""
app_opt_new = """    val view = LocalView.current
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { 
                try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                SettingsManager.setAppearanceMode(mode) 
            }"""

if app_opt_old in text:
    text = text.replace(app_opt_old, app_opt_new)

# Add Haptics in ColorPickerRow
color_pick_old = """@Composable
fun ColorPickerRow(
    options: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Row("""
color_pick_new = """@Composable
fun ColorPickerRow(
    options: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    val view = LocalView.current
    Row("""

color_box_old = """.clickable { onColorSelected(color) }"""
color_box_new = """.clickable { 
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        onColorSelected(color) 
                    }"""

if color_pick_old in text:
    text = text.replace(color_pick_old, color_pick_new)
    text = text.replace(color_box_old, color_box_new)


with open('app/src/main/java/com/example/ui/components/AppCustomizerScreen.kt', 'w') as f:
    f.write(text)

