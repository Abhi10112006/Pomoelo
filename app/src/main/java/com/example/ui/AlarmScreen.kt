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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(navController: NavController, bottomPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    val scrollState = rememberScrollState()

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

    val alarmDao = remember { com.example.data.DatabaseProvider.getDatabase(context).alarmDao() }
    val coroutineScope = rememberCoroutineScope()
    val alarms by alarmDao.getAllAlarms().collectAsState(initial = emptyList())
    var showEditDialog by remember { mutableStateOf(false) }
    var activeEditAlarm by remember { mutableStateOf<com.example.data.AlarmItem?>(null) }
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Scaffold(
        containerColor = currentTheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                        Text(
                            text = "Somatic Alarms ⏰",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Black,
                            fontSize = 26.scaledSp,
                            color = currentTheme.textPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Stand up and squat to silence your morning alarm",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.scaledSp,
                            color = currentTheme.textSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    activeEditAlarm = null
                    showEditDialog = true
                },
                containerColor = currentTheme.primary,
                modifier = Modifier.padding(bottom = 88.dp, end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Alarm",
                    tint = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // MULTIPLE SAVED ALARMS SECTION
            if (showEditDialog) {
                EditAlarmDialog(
                    alarm = activeEditAlarm,
                    onDismiss = { showEditDialog = false },
                    onSave = { h, m, lbl, days, targetReps ->
                        if (activeEditAlarm == null) {
                            val newAlarm = com.example.data.AlarmItem(
                                hour = h,
                                minute = m,
                                label = lbl,
                                daysOfWeek = days,
                                isEnabled = true,
                                squatTarget = targetReps
                            )
                            coroutineScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                                val id = alarmDao.insertAlarm(newAlarm)
                                com.example.service.AlarmScheduler.scheduleAlarm(context, newAlarm.copy(id = id.toInt()))
                            }
                        } else {
                            val updated = activeEditAlarm!!.copy(
                                hour = h,
                                minute = m,
                                label = lbl,
                                daysOfWeek = days,
                                squatTarget = targetReps
                            )
                            coroutineScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                                alarmDao.updateAlarm(updated)
                                com.example.service.AlarmScheduler.scheduleAlarm(context, updated)
                            }
                        }
                        showEditDialog = false
                    }
                )
            }

            // Saved Alarms Card Container
            Card(
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Saved Wakeup Schedules  ⏰",
                                fontSize = 16.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary,
                                fontFamily = currentFont
                            )
                            Text(
                                text = "Active physical alarms configured",
                                fontSize = 11.scaledSp,
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont
                            )
                        }
                    }

                    if (alarms.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.NotificationsActive,
                                    contentDescription = "Empty Alarms",
                                    tint = currentTheme.textSecondary.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No wakeup alarms set. Complete with zero physical snooze constraints.",
                                    fontSize = 11.scaledSp,
                                    color = currentTheme.textSecondary,
                                    textAlign = TextAlign.Center,
                                    fontFamily = currentFont
                                )
                            }
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            alarms.forEach { item ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(currentTheme.backgroundSecondary)
                                        .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp))
                                        .clickable {
                                            activeEditAlarm = item
                                            showEditDialog = true
                                        }
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val pmAm = if (item.hour >= 12) "PM" else "AM"
                                        val displayHr = when {
                                            item.hour == 0 -> 12
                                            item.hour > 12 -> item.hour - 12
                                            else -> item.hour
                                        }
                                        val timeStr = String.format("%02d:%02d %s", displayHr, item.minute, pmAm)
                                        
                                        Text(
                                            text = timeStr,
                                            fontSize = 20.scaledSp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = currentTheme.textPrimary,
                                            fontFamily = MonospaceFontFamily,
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(currentTheme.primaryLight.copy(alpha = 0.25f))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "${item.squatTarget} squats",
                                                    fontSize = 9.scaledSp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = currentTheme.primaryDark,
                                                    fontFamily = MonospaceFontFamily
                                                )
                                            }

                                            Switch(
                                                checked = item.isEnabled,
                                                onCheckedChange = { isChecked ->
                                                    try {
                                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                                                    } catch (e: Exception) {}
                                                    val toggled = item.copy(isEnabled = isChecked)
                                                    coroutineScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                                                        alarmDao.updateAlarm(toggled)
                                                        if (isChecked) {
                                                            com.example.service.AlarmScheduler.scheduleAlarm(context, toggled)
                                                        } else {
                                                            com.example.service.AlarmScheduler.cancelAlarm(context, toggled.id)
                                                        }
                                                    }
                                                },
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                                                    checkedTrackColor = currentTheme.primary,
                                                    uncheckedThumbColor = currentTheme.surface,
                                                    uncheckedTrackColor = currentTheme.cardBorder
                                                )
                                            )

                                            IconButton(
                                                onClick = {
                                                    try {
                                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                                    } catch (e: Exception) {}
                                                    coroutineScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                                                        com.example.service.AlarmScheduler.cancelAlarm(context, item.id)
                                                        alarmDao.deleteAlarm(item)
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Delete,
                                                    contentDescription = "Delete Alarm",
                                                    tint = Color(0xFFE57373),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(2.dp))
                                    
                                    Text(
                                        text = "${item.label} • ${formatDaysOfWeekDisplay(item.daysOfWeek)}",
                                        fontSize = 11.scaledSp,
                                        color = currentTheme.textSecondary,
                                        fontFamily = currentFont
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Permissions / Backend preparation card
            Card(
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Somatic Hardware Pre-flight",
                        fontSize = 14.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Info icon",
                            tint = currentTheme.primary,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Text(
                            text = "To guarantee physical snooze bypass works securely on your hardware, both dynamic alarms & push triggers must be authorized.",
                            fontSize = 11.scaledSp,
                            color = currentTheme.textSecondary,
                            lineHeight = 16.scaledSp,
                            fontFamily = currentFont,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    HorizontalDivider(color = currentTheme.cardBorder, thickness = 0.5.dp)

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
                                text = "Push Service Status",
                                fontSize = 12.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary,
                                fontFamily = currentFont
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
                                colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary),
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Authorize Push Alarms",
                                    fontSize = 12.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = currentTheme.cardBorder, thickness = 0.5.dp)

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
                                color = currentTheme.textPrimary
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
                                colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary),
                                modifier = Modifier.fillMaxWidth().height(42.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Grant Exact Alarm Integration",
                                    fontSize = 12.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                                )
                            }
                        }
                        
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                navController.navigate("calibration")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = currentTheme.secondary),
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Calibrate Squat Sensor",
                                fontSize = 12.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentTheme.secondary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(bottomPadding + 88.dp))
        }
    }
}

fun formatDaysOfWeekDisplay(daysOfWeek: String): String {
    if (daysOfWeek == "Daily" || daysOfWeek.isBlank()) return "Daily"
    val daysList = daysOfWeek.split(",").mapNotNull { it.trim().toIntOrNull() }
    if (daysList.size == 7) return "Daily"
    val dayNames = listOf("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    return daysList.mapNotNull { if (it in 1..7) dayNames[it] else null }.joinToString(", ")
}

@Composable
fun EditAlarmDialog(
    alarm: com.example.data.AlarmItem?, // null means creating a new alarm
    onDismiss: () -> Unit,
    onSave: (hour: Int, minute: Int, label: String, daysOfWeek: String, squatTarget: Int) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val initialHour = alarm?.hour ?: 7
    var minute by remember { mutableStateOf(alarm?.minute ?: 0) }
    var label by remember { mutableStateOf(alarm?.label ?: "Morning Workout") }
    var squatTarget by remember { mutableStateOf(alarm?.squatTarget ?: 10) }
    
    // Convert 24-hr format into AM/PM state
    var isPm by remember { mutableStateOf(initialHour >= 12) }
    var amPmSelectedHour by remember {
        mutableStateOf(
            when {
                initialHour == 0 -> 12
                initialHour > 12 -> initialHour - 12
                else -> initialHour
            }
        )
    }

    // Parse selected days
    val initialDays = if (alarm == null || alarm.daysOfWeek == "Daily") {
        (1..7).toSet()
    } else {
        alarm.daysOfWeek.split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
    }
    var selectedDays by remember { mutableStateOf(initialDays) }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
                .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(24.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (alarm == null) "Add Workout Alarm" else "Edit Workout Alarm",
                    fontSize = 18.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Vertical digital text picker columns with adjacent AM/PM segmented toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Hour picker column
                    DigitalTimeStepper(
                        value = amPmSelectedHour,
                        range = 1..12,
                        label = "Hour",
                        onValueChange = { amPmSelectedHour = it },
                        modifier = Modifier.weight(1.2f)
                    )
                    
                    // Minute picker column
                    DigitalTimeStepper(
                        value = minute,
                        range = 0..59,
                        label = "Minute",
                        onValueChange = { minute = it },
                        modifier = Modifier.weight(1.2f)
                    )
                    
                    // Segmented AM/PM toggle adjacent
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1.1f)
                    ) {
                        Text(
                            text = "Period",
                            fontSize = 11.scaledSp,
                            color = currentTheme.textSecondary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        
                        Row(
                            modifier = Modifier
                                .height(54.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(currentTheme.backgroundSecondary)
                                .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp))
                                .padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val activePm = isPm
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (!activePm) currentTheme.primary else Color.Transparent)
                                    .clickable { isPm = false },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AM",
                                    fontSize = 11.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!activePm) {
                                        if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                                    } else currentTheme.textSecondary,
                                    fontFamily = currentFont
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (activePm) currentTheme.primary else Color.Transparent)
                                    .clickable { isPm = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "PM",
                                    fontSize = 11.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activePm) {
                                        if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                                    } else currentTheme.textSecondary,
                                    fontFamily = currentFont
                                )
                            }
                        }
                    }
                }

                // Selected Time formatted preview reflecting changes dynamically in real-time
                val previewAmPm = if (isPm) "PM" else "AM"
                Text(
                    text = String.format("Selected Time: %d:%02d %s", amPmSelectedHour, minute, previewAmPm),
                    fontSize = 15.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.primary,
                    fontFamily = MonospaceFontFamily,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Label Text Field
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Alarm Label") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = currentTheme.textPrimary,
                        unfocusedTextColor = currentTheme.textPrimary,
                        cursorColor = currentTheme.primary,
                        focusedBorderColor = currentTheme.primary,
                        unfocusedBorderColor = currentTheme.cardBorder,
                        focusedLabelColor = currentTheme.primary,
                        unfocusedLabelColor = currentTheme.textSecondary
                    ),
                    singleLine = true
                )

                // Squad Challenge Count
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Squat Reps Challenge", fontSize = 12.scaledSp, color = currentTheme.textSecondary, fontWeight = FontWeight.Bold, fontFamily = currentFont)
                        Text("$squatTarget Reps", fontSize = 13.scaledSp, color = currentTheme.primary, fontWeight = FontWeight.Bold, fontFamily = currentFont)
                    }
                    Slider(
                        value = squatTarget.toFloat(),
                        onValueChange = { squatTarget = it.toInt() },
                        valueRange = 3f..30f,
                        colors = SliderDefaults.colors(
                            thumbColor = currentTheme.primary,
                            activeTrackColor = currentTheme.primary,
                            inactiveTrackColor = currentTheme.cardBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Day selectors
                Text("Repeat Days", fontSize = 12.scaledSp, color = currentTheme.textSecondary, fontWeight = FontWeight.Bold, fontFamily = currentFont)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val dayPairs = listOf(
                        Pair("S", java.util.Calendar.SUNDAY),
                        Pair("M", java.util.Calendar.MONDAY),
                        Pair("T", java.util.Calendar.TUESDAY),
                        Pair("W", java.util.Calendar.WEDNESDAY),
                        Pair("T", java.util.Calendar.THURSDAY),
                        Pair("F", java.util.Calendar.FRIDAY),
                        Pair("S", java.util.Calendar.SATURDAY)
                    )
                    
                    dayPairs.forEach { (name, calVal) ->
                        val isSelected = selectedDays.contains(calVal)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) currentTheme.primary else currentTheme.backgroundSecondary)
                                .border(1.dp, if (isSelected) Color.Transparent else currentTheme.cardBorder, CircleShape)
                                .clickable {
                                    selectedDays = if (isSelected) {
                                        selectedDays - calVal
                                    } else {
                                        selectedDays + calVal
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                fontSize = 11.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) {
                                    if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                                } else currentTheme.textSecondary,
                                fontFamily = currentFont
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { selectedDays = (1..7).toSet() },
                        modifier = Modifier.weight(1f)
                    ) {
                        val isSelected = selectedDays == (1..7).toSet()
                        Text(
                            "Daily",
                            fontSize = 11.scaledSp,
                            color = currentTheme.primary.copy(alpha = if (isSelected) 1f else 0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = currentFont
                        )
                    }
                    TextButton(
                        onClick = { selectedDays = setOf(2, 3, 4, 5, 6) },
                        modifier = Modifier.weight(1f)
                    ) {
                        val isSelected = selectedDays == setOf(2, 3, 4, 5, 6)
                        Text(
                            "Weekdays",
                            fontSize = 11.scaledSp,
                            color = currentTheme.primary.copy(alpha = if (isSelected) 1f else 0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = currentFont
                        )
                    }
                    TextButton(
                        onClick = { selectedDays = setOf(1, 7) },
                        modifier = Modifier.weight(1f)
                    ) {
                        val isSelected = selectedDays == setOf(1, 7)
                        Text(
                            "Weekends",
                            fontSize = 11.scaledSp,
                            color = currentTheme.primary.copy(alpha = if (isSelected) 1f else 0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = currentFont
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = currentTheme.textSecondary, fontFamily = currentFont)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val computedHour = if (isPm) {
                                if (amPmSelectedHour == 12) 12 else amPmSelectedHour + 12
                            } else {
                                if (amPmSelectedHour == 12) 0 else amPmSelectedHour
                            }
                            val daysStr = if (selectedDays.size == 7) {
                                "Daily"
                            } else if (selectedDays.isEmpty()) {
                                "Daily"
                            } else {
                                selectedDays.sorted().joinToString(",")
                            }
                            onSave(computedHour, minute, label, daysStr, squatTarget)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Save",
                            color = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont
                        )
                    }
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun DigitalTimeStepper(
    value: Int,
    range: ClosedRange<Int>,
    label: String,
    onValueChange: (Int) -> Unit,
    format: String = "%02d",
    modifier: Modifier = Modifier
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val items = (range.start..range.endInclusive).toList()
    val itemsCount = items.size
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    
    val itemHeight = 36.dp
    
    val initialIndex = remember {
        val middleBase = (Int.MAX_VALUE / 2 / itemsCount) * itemsCount
        val offset = items.indexOf(value)
        middleBase + offset - 1
    }
    
    val listState = androidx.compose.foundation.lazy.rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior(lazyListState = listState)
    
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) {
                listState.firstVisibleItemIndex + 1
            } else {
                val center = layoutInfo.viewportEndOffset / 2
                val centerItem = layoutInfo.visibleItemsInfo.minByOrNull {
                    kotlin.math.abs((it.offset + it.size / 2) - center)
                }
                centerItem?.index ?: (listState.firstVisibleItemIndex + 1)
            }
        }
    }

    LaunchedEffect(centerIndex) {
        val itemVal = items[centerIndex % itemsCount]
        if (itemVal != value) {
            onValueChange(itemVal)
            try { 
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove) 
            } catch (e: Exception) {}
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 11.scaledSp,
            color = currentTheme.textSecondary,
            fontWeight = FontWeight.Bold,
            fontFamily = currentFont,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(currentTheme.backgroundSecondary)
                .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp))
                .padding(vertical = 4.dp, horizontal = 12.dp)
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(centerIndex)
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Increase",
                    tint = currentTheme.primary
                )
            }
            
            Box(
                modifier = Modifier.height(108.dp).width(64.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.lazy.LazyColumn(
                    state = listState,
                    flingBehavior = flingBehavior,
                    modifier = Modifier.height(108.dp)
                ) {
                    items(Int.MAX_VALUE) { index ->
                        val itemVal = items[index % itemsCount]
                        val isCenter = index == centerIndex
                        val alpha = if (isCenter) 1f else 0.3f
                        val textSize = if (isCenter) 24 else 18
                        val fontWeight = if (isCenter) FontWeight.ExtraBold else FontWeight.Medium
                        
                        Box(
                            modifier = Modifier
                                .height(itemHeight)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = String.format(format, itemVal),
                                fontSize = textSize.scaledSp,
                                fontWeight = fontWeight,
                                color = currentTheme.textPrimary.copy(alpha = alpha),
                                fontFamily = MonospaceFontFamily,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val target = if (centerIndex - 2 < 0) 0 else centerIndex - 2
                        listState.animateScrollToItem(target)
                    }
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Decrease",
                    tint = currentTheme.primary
                )
            }
        }
    }
}

