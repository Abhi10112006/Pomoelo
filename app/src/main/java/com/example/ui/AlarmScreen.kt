package com.example.ui

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.components.scaledSp
import com.example.service.SettingsManager
import com.example.service.SquatSensorService
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(navController: NavController, bottomPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    val scrollState = rememberScrollState()

    // Load saved settings
    var squatAlarmEnabled by remember { mutableStateOf(SettingsManager.isSquatAlarmEnabled()) }
    var squatTarget by remember { mutableStateOf(SettingsManager.getSquatTarget()) }
    if (squatTarget <= 0) {
        squatTarget = 10 // safe default
    }

    // Interactive sensor test states
    var isSensorTesting by remember { mutableStateOf(false) }
    var testSquatCount by remember { mutableStateOf(0) }
    var sensorServiceInstance by remember { mutableStateOf<SquatSensorService?>(null) }
    var sensorFeedbackMessage by remember { mutableStateOf("Ready to calibrate. Step away and squat!") }

    // Typewriter tease effect message
    var teaseIndex by remember { mutableStateOf(0) }
    val teaseMessages = listOf(
        "💪 Did you know? Active breaks reduce executive fatigue by 42%!",
        "🔥 Coming Soon: Connect your wearable device to track real-time sweat metrics!",
        "🧘 Unlock premium cosmic achievements for consistency streaks!",
        "🏃 Physical checkpoints make it impossible to cheat your pomodoro goals."
    )
    val currentTeaseText = teaseMessages[teaseIndex]
    var visibleTeaseText by remember { mutableStateOf("") }

    // Animate typewriter typewriter effect
    LaunchedEffect(currentTeaseText) {
        visibleTeaseText = ""
        currentTeaseText.forEachIndexed { i, char ->
            visibleTeaseText += char
            delay(40)
        }
    }

    // Cycle tease messages every 8 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(10000)
            teaseIndex = (teaseIndex + 1) % teaseMessages.size
        }
    }

    // Permission States
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var hasExactAlarmPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                alarmManager?.canScheduleExactAlarms() ?: true
            } else {
                true
            }
        )
    }

    // Activates permission launcher for notifications
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    // Refresh states on resume
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            hasExactAlarmPermission = alarmManager?.canScheduleExactAlarms() ?: true
        }
    }

    // Cleanup sensor on dispose
    DisposableEffect(Unit) {
        onDispose {
            sensorServiceInstance?.stopTracking()
        }
    }

    Scaffold(
        containerColor = Color(0xFFFDF4F0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Smart Workout Alarm 🏋️",
                        fontFamily = CursiveFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.scaledSp,
                        color = Color(0xFF5D4037)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Interactive Hero Smart Alarm Switch Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE5D5D0).copy(alpha = 0.6f), RoundedCornerShape(28.dp))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(if (squatAlarmEnabled) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (squatAlarmEnabled) Icons.Filled.DirectionsRun else Icons.Filled.NotificationsActive,
                            contentDescription = "Active Indicator",
                            tint = if (squatAlarmEnabled) Color(0xFF4CAF50) else Color(0xFFFFB74D),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "Squat to Snooze",
                        fontSize = 20.scaledSp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = AppFontFamily,
                        color = Color(0xFF5D4037)
                    )

                    Text(
                        text = "Prevents premature snoozing! Alarms blare at full volume until you physically perform the selected amount of squats to authorize a break.",
                        fontSize = 12.scaledSp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.scaledSp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFFFF8F6))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Lock Workout Alarm",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.scaledSp,
                                color = Color(0xFF5D4037)
                            )
                            Text(
                                if (squatAlarmEnabled) "Armed via Accelerometer" else "Inactive",
                                fontSize = 11.scaledSp,
                                color = if (squatAlarmEnabled) Color(0xFF4CAF50) else Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Switch(
                            checked = squatAlarmEnabled,
                            onCheckedChange = { isChecked ->
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                                } catch (e: Exception) {}
                                squatAlarmEnabled = isChecked
                                SettingsManager.setSquatAlarmEnabled(isChecked)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4CAF50),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFE5D5D0)
                            )
                        )
                    }
                }
            }

            // Target Parameter Configuration Card (Unlocked dynamically)
            AnimatedVisibility(
                visible = squatAlarmEnabled,
                enter = expandVertically(animationSpec = spring(stiffness = Spring.StiffnessLow)) + fadeIn(),
                exit = shrinkVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium)) + fadeOut(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.border(1.dp, Color(0xFFE5D5D0).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        var squatInputText by remember(squatTarget) { mutableStateOf(squatTarget.toString()) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    "Target Squat Reps",
                                    color = Color(0xFF5D4037),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.scaledSp
                                )
                                Text(
                                    "Manually enter or slide (up to 120 reps)",
                                    color = Color.Gray,
                                    fontSize = 11.scaledSp
                                )
                            }

                            BasicTextField(
                                value = squatInputText,
                                onValueChange = { newVal ->
                                    val filtered = newVal.filter { it.isDigit() }
                                    squatInputText = filtered
                                    filtered.toIntOrNull()?.let { 
                                        val coerced = it.coerceIn(1, 120)
                                        squatTarget = coerced
                                        SettingsManager.setSquatTarget(coerced)
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = TextStyle(
                                    color = Color(0xFF5D4037),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.scaledSp,
                                    textAlign = TextAlign.Center,
                                    fontFamily = MonospaceFontFamily
                                ),
                                modifier = Modifier
                                    .width(62.dp)
                                    .background(Color(0xFFFFF3E0), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Slider(
                            value = squatTarget.toFloat().coerceIn(1f, 30f),
                            onValueChange = { 
                                val newVal = it.toInt()
                                if (newVal != squatTarget) {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
                                    } catch (e: Exception) {}
                                    squatTarget = newVal
                                    SettingsManager.setSquatTarget(newVal)
                                }
                            },
                            valueRange = 1f..30f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF4CAF50),
                                activeTrackColor = Color(0xFFA5D6A7),
                                inactiveTrackColor = Color(0xFFE8F5E9)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Live Calibration Test Arena
                        Text(
                            text = "⚡ Real-time Sensor Lab (LPF & Speed Filters)",
                            fontSize = 14.scaledSp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFE5D5D0).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .padding(16.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Gravity telemetry sensor status:",
                                        fontSize = 11.scaledSp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isSensorTesting) "● TRACKING ACTIVE" else "○ STANDBY",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.scaledSp,
                                        fontFamily = MonospaceFontFamily,
                                        color = if (isSensorTesting) Color(0xFF2E7D32) else Color.Gray
                                    )
                                }

                                // Interactive Dual-Trace LPF Graph Visualizer
                                SensorTelemetryScope(
                                    isTesting = isSensorTesting,
                                    repCount = testSquatCount,
                                    targetCount = squatTarget
                                )

                                if (isSensorTesting) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFFFF3E0).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(54.dp)
                                                .background(Color.White, CircleShape)
                                                .border(2.dp, Color(0xFF4CAF50), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "$testSquatCount/$squatTarget",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.scaledSp,
                                                fontFamily = MonospaceFontFamily,
                                                color = Color(0xFF5D4037)
                                            )
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Form Telemetry:",
                                                fontSize = 10.scaledSp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = sensorFeedbackMessage,
                                                fontSize = 11.scaledSp,
                                                color = Color(0xFF5D4037),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                testSquatCount++
                                                sensorFeedbackMessage = "Accels active: Simulated rep intercepted!"
                                                try {
                                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                                                } catch (e: Exception) {}
                                                if (testSquatCount >= squatTarget) {
                                                    sensorFeedbackMessage = "🏆 Complete! Workout alarm check passed."
                                                    isSensorTesting = false
                                                    sensorServiceInstance?.stopTracking()
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8F5E9)),
                                            modifier = Modifier.height(36.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Simulate 🦘", fontSize = 10.scaledSp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color(0xFFF9F9F9), RoundedCornerShape(12.dp))
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Info,
                                            contentDescription = "Calibration Info",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Tap calibrate, hold/pocket your device, and squat! Shakes are automatically filtered.",
                                            fontSize = 10.scaledSp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        if (isSensorTesting) {
                                            isSensorTesting = false
                                            sensorServiceInstance?.stopTracking()
                                        } else {
                                            isSensorTesting = true
                                            testSquatCount = 0
                                            sensorFeedbackMessage = "Form filters online. Perform a smooth, intentional squat!"
                                            val service = SquatSensorService(context)
                                            sensorServiceInstance = service
                                            service.startTracking(
                                                targetSquats = squatTarget,
                                                onComplete = {
                                                    isSensorTesting = false
                                                    sensorFeedbackMessage = "🏆 Complete! Physical test matched successfully!"
                                                    try {
                                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                                                    } catch (e: Exception) {}
                                                },
                                                onUpdate = { currentCount ->
                                                    testSquatCount = currentCount
                                                    sensorFeedbackMessage = "Perfect squat detected! Count: $currentCount"
                                                    try {
                                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                                    } catch (e: Exception) {}
                                                }
                                            )
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSensorTesting) Color(0xFFFFCC80) else Color(0xFFE8F5E9)
                                    ),
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (isSensorTesting) "Stop Hardware Integration Test" else "🎮 Calibrate Accelerometer Input",
                                        color = if (isSensorTesting) Color(0xFFE65100) else Color(0xFF2E7D32),
                                        fontSize = 13.scaledSp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // curation of typewriter teasers
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2).copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✨",
                        fontSize = 18.scaledSp
                    )
                    Text(
                        text = visibleTeaseText,
                        fontSize = 11.scaledSp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontFamily = MonospaceFontFamily,
                        color = Color(0xFFE65100),
                        lineHeight = 16.scaledSp
                    )
                }
            }

            // Permissions / Backend preparation card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE5D5D0).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "System permissions pre-flight check",
                        fontSize = 15.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Info icon",
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Fitness integrations require precise background hardware access. Please verify notification services and alarm timers are authorized.",
                            fontSize = 11.scaledSp,
                            color = Color.Gray,
                            lineHeight = 16.scaledSp
                        )
                    }

                    Divider(color = Color(0xFFF5F5F5), thickness = 1.dp)

                    // Notification Permission
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Push Notification Action",
                                fontSize = 13.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D4037)
                            )
                            if (hasNotificationPermission) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Active",
                                        tint = Color(0xFF66BB6A),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Active", color = Color(0xFF66BB6A), fontSize = 12.scaledSp, fontWeight = FontWeight.SemiBold)
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Pending",
                                        tint = Color(0xFFFF8A80),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Denied", color = Color(0xFFFF8A80), fontSize = 12.scaledSp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Button(
                                onClick = {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A80)),
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Grant Permission", fontSize = 12.scaledSp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Divider(color = Color(0xFFF5F5F5), thickness = 1.dp)

                    // Schedule Exact Alarms
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Android Exact Alarm Sync",
                                fontSize = 13.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5D4037)
                            )
                            if (hasExactAlarmPermission) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Active",
                                        tint = Color(0xFF66BB6A),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Active", color = Color(0xFF66BB6A), fontSize = 12.scaledSp, fontWeight = FontWeight.SemiBold)
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Warning,
                                        contentDescription = "Pending",
                                        tint = Color(0xFFFF8A80),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Denied", color = Color(0xFFFF8A80), fontSize = 12.scaledSp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        if (!hasExactAlarmPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Button(
                                onClick = {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                    try { context.startActivity(intent) } catch (e: Exception) {}
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81D4FA)),
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Grant Exact Alarm Integration", fontSize = 12.scaledSp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(bottomPadding + 88.dp))
        }
    }
}

@Composable
fun SensorTelemetryScope(
    isTesting: Boolean,
    repCount: Int,
    targetCount: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "telemetry")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * 3.1415927f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val neonColor = Color(0xFF2E7D32) // Sport green
    val rawColor = Color(0xFFFF7043) // Chaotic vibration orange
    val gridColor = Color(0xFFFFB74D).copy(alpha = 0.08f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF2B201D)) // Sophisticated deep athletic slate
            .border(1.dp, Color(0xFF5D4037).copy(alpha = 0.25f), RoundedCornerShape(14.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val fPI = 3.1415927f

            // 1. Draw grid lines for high-fidelity lab scope feel
            val cols = 14
            val rows = 6
            for (i in 1 until cols) {
                val x = (width / cols) * i
                drawLine(
                    color = gridColor,
                    start = androidx.compose.ui.geometry.Offset(x, 0f),
                    end = androidx.compose.ui.geometry.Offset(x, height),
                    strokeWidth = 1f
                )
            }
            for (i in 1 until rows) {
                val y = (height / rows) * i
                drawLine(
                    color = gridColor,
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(width, y),
                    strokeWidth = 1f
                )
            }

            // Zero line
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = androidx.compose.ui.geometry.Offset(0f, height / 2f),
                end = androidx.compose.ui.geometry.Offset(width, height / 2f),
                strokeWidth = 2f
            )

            if (isTesting) {
                val rawPath = androidx.compose.ui.graphics.Path()
                val lpfPath = androidx.compose.ui.graphics.Path()

                val points = 90
                for (i in 0..points) {
                    val x = (width / points) * i
                    val progress = i.toFloat() / points.toFloat()

                    // Trace A (Raw Shake Jitter - high frequency, small height spikes)
                    val rawShakeJitter = kotlin.math.sin(progress * 24f * fPI + phase * 6f) * 14f +
                            kotlin.math.cos(progress * 8f * fPI - phase) * 6f +
                            (if (i % 3 == 0) 4f else -4f) // Spiky noise jitter
                    val rawY = (height / 2f + rawShakeJitter).coerceIn(4f, height - 4f)

                    // Trace B (LPF Output - beautiful smooth human squat motion)
                    val smoothSquatCurve = kotlin.math.sin(progress * 3.5f * fPI - phase) * (height * 0.35f)
                    val cleanY = height / 2f + smoothSquatCurve

                    if (i == 0) {
                        rawPath.moveTo(x, rawY)
                        lpfPath.moveTo(x, cleanY)
                    } else {
                        rawPath.lineTo(x, rawY)
                        lpfPath.lineTo(x, cleanY)
                    }
                }

                // Draw Chaotic Noise (Trace A) representing shaking raw device stream
                drawPath(
                    path = rawPath,
                    color = rawColor.copy(alpha = 0.25f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 2f
                    )
                )

                // Draw Smooth Intended Motion (Trace B) showing the LPF in action
                drawPath(
                    path = lpfPath,
                    color = Color(0xFF66BB6A), // Emerald glow
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 5f,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                )

                // Draw pulsating target capture lights on intervals
                if (repCount > 0) {
                    val angleOffset = 0.75f * 3.5f * fPI - phase
                    val markerX = width * 0.75f
                    val markerY = height / 2f + kotlin.math.sin(angleOffset) * (height * 0.35f)
                    
                    drawCircle(
                        color = Color(0xFF66BB6A).copy(alpha = 0.3f),
                        radius = 24f,
                        center = androidx.compose.ui.geometry.Offset(markerX, markerY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 8f,
                        center = androidx.compose.ui.geometry.Offset(markerX, markerY)
                    )
                }
            } else {
                // Flat standby trace line with minor natural background thermal hum
                val idlePath = androidx.compose.ui.graphics.Path()
                idlePath.moveTo(0f, height / 2f)
                val points = 40
                for (i in 0..points) {
                    val x = (width / points) * i
                    val relativeX = i.toFloat() / points.toFloat()
                    val idleHum = kotlin.math.sin(relativeX * 10f * fPI + phase) * 2f
                    idlePath.lineTo(x, height / 2f + idleHum)
                }
                drawPath(
                    path = idlePath,
                    color = Color.White.copy(alpha = 0.15f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 3f
                    )
                )
            }
        }

        // Overlay status panel labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isTesting) "● LPF DUAL-STREAM TELEMETRY" else "○ IDLE CALIBRATION LOCK",
                fontFamily = MonospaceFontFamily,
                fontSize = 8.scaledSp,
                fontWeight = FontWeight.Bold,
                color = if (isTesting) Color(0xFF66BB6A) else Color.White.copy(alpha = 0.35f)
            )
            Text(
                text = "DSP MODE: 2-STAGE LPF (0.20)",
                fontFamily = MonospaceFontFamily,
                fontSize = 8.scaledSp,
                color = Color.White.copy(alpha = 0.4f)
            )
        }

        // Realtime legends
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(rawColor.copy(alpha = 0.6f)).clip(CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chaotic Shake (Rejected)", fontSize = 7.5.scaledSp, color = Color.White.copy(alpha = 0.5f), fontFamily = MonospaceFontFamily)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(Color(0xFF66BB6A)).clip(CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Smooth Squat (Authorized)", fontSize = 7.5.scaledSp, color = Color.White.copy(alpha = 0.5f), fontFamily = MonospaceFontFamily)
                }
            }
            Text(
                text = "SPEED LIMITER: ENGAGED (>1s)",
                fontSize = 7.5.scaledSp,
                fontFamily = MonospaceFontFamily,
                color = if (isTesting) Color(0xFFFFF59D) else Color.White.copy(alpha = 0.25f)
            )
        }
    }
}

