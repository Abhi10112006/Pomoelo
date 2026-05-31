package com.example

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Settings
import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.ui.theme.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.scaledSp
import androidx.core.app.ActivityCompat
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.data.AppDatabase
import com.example.data.DatabaseProvider
import com.example.service.SettingsManager
import com.example.service.TimerManager
import com.example.service.TimerService
import com.example.ui.TimerViewModel
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Show over lockscreen and keep screen on even if locked
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        SettingsManager.init(applicationContext)

        requestPermissions()

        database = DatabaseProvider.getDatabase(applicationContext)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: TimerViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return TimerViewModel(database) as T
                        }
                    }
                )
                PomoPalApp(viewModel)
            }
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (TimerManager.timerState.value != TimerManager.TimerState.RUNNING) {
            com.example.service.InactivityScheduler.schedule(applicationContext)
        }
    }
}

@Composable
fun PomoPalApp(viewModel: TimerViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"
    val isAddingTask by viewModel.isAddingTask.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
    var updateUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current

    LaunchedEffect(Unit) {
        val url = UpdateChecker.checkForUpdates()
        if (url != null) {
            updateUrl = url
        }
    }

    if (updateUrl != null) {
        AlertDialog(
            onDismissRequest = { updateUrl = null },
            title = { Text("Update Available", color = Color(0xFF5D4037), fontWeight = FontWeight.Bold) },
            text = { Text("A new version of PomoPal is available! Please download it to enjoy the latest features and bug fixes.", color = Color(0xFF5D4037)) },
            confirmButton = {
                TextButton(onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(updateUrl))
                    context.startActivity(intent)
                    updateUrl = null
                }) {
                    Text("Download", color = Color(0xFFFF8A80), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    updateUrl = null
                }) {
                    Text("Later", color = Color.Gray)
                }
            },
            containerColor = Color(0xFFFFF0EC),
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            // Floating Bottom Pill Navigation
            AnimatedVisibility(
                visible = !isAddingTask && timerState == TimerManager.TimerState.STOPPED && !isSettingsOpen,
                enter = slideInVertically(initialOffsetY = { it * 2 }, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessVeryLow)) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it * 2 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) + fadeOut(),
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = 16.dp, top = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val navSpring = spring<Float>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                        val isHome = currentRoute == "home"
                        val homeWeight by animateFloatAsState(targetValue = if (isHome) 1f else 0f, animationSpec = navSpring)
                        
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isHome) Color(0xFFFFEBEE) else Color.Transparent)
                                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    if (!isHome) navController.navigate("home") { popUpTo(0) }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home",
                                tint = if (isHome) Color(0xFFFF8A80) else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            if (homeWeight > 0.1f) {
                                Spacer(modifier = Modifier.width(8.dp * homeWeight))
                                Text(
                                    text = "Timer",
                                    color = Color(0xFFFF8A80),
                                    fontSize = (14 * homeWeight).sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }

                        val isHistory = currentRoute == "history"
                        val historyWeight by animateFloatAsState(targetValue = if (isHistory) 1f else 0f, animationSpec = navSpring)

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isHistory) Color(0xFFE1F5FE) else Color.Transparent)
                                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    if (!isHistory) navController.navigate("history") { popUpTo(0) }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "History",
                                tint = if (isHistory) Color(0xFF81D4FA) else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            if (historyWeight > 0.1f) {
                                Spacer(modifier = Modifier.width(8.dp * historyWeight))
                                Text(
                                    text = "History",
                                    color = Color(0xFF81D4FA),
                                    fontSize = (14 * historyWeight).sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }

                        val isAlarm = currentRoute == "alarm"
                        val alarmWeight by animateFloatAsState(targetValue = if (isAlarm) 1f else 0f, animationSpec = navSpring)

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isAlarm) Color(0xFFFFF3E0) else Color.Transparent)
                                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    if (!isAlarm) navController.navigate("alarm") { popUpTo(0) }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Alarm",
                                tint = if (isAlarm) Color(0xFFFFB74D) else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            if (alarmWeight > 0.1f) {
                                Spacer(modifier = Modifier.width(8.dp * alarmWeight))
                                Text(
                                    text = "Alarm",
                                    color = Color(0xFFFFB74D),
                                    fontSize = (14 * alarmWeight).sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.fillMaxSize()
        ) {
            val bottomPadding = paddingValues.calculateBottomPadding()
            composable("home") {
                HomeScreen(viewModel, navController, bottomPadding)
            }
            composable("history") {
                com.example.ui.HistoryScreen(viewModel, navController, bottomPadding)
            }
            composable("alarm") {
                com.example.ui.AlarmScreen(navController, bottomPadding)
            }
        }
    }

    if (com.example.service.AlarmState.isAlarmRinging) {
        AlarmRingingLockScreen(
            label = com.example.service.AlarmState.activeAlarmLabel,
            targetCount = com.example.service.AlarmState.activeAlarmSquatTarget
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TimerViewModel, navController: androidx.navigation.NavController, bottomPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    LaunchedEffect(Unit) {
        viewModel.autoCleanupIfNeeded()
    }
    val timerState by viewModel.timerState.collectAsState()
    val isBreakMode by TimerManager.isBreakMode.collectAsState()
    val currentQuote by viewModel.currentQuote.collectAsState()
    val timeRemaining by viewModel.timeRemainingSeconds.collectAsState()
    val currentTaskName by viewModel.currentTaskName.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val isAddingTask by viewModel.isAddingTask.collectAsState()
    val context = LocalContext.current
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val view = androidx.compose.ui.platform.LocalView.current

    val showSettings by viewModel.isSettingsOpen.collectAsState()
    var showFullScreenSeriousness by remember { mutableStateOf(false) }

    LaunchedEffect(timerState, isBreakMode) {
        if (timerState == TimerManager.TimerState.STOPPED || isBreakMode) {
            showFullScreenSeriousness = false
        }
    }

    val stateColor by animateColorAsState(
        targetValue = when {
            isBreakMode -> Color(0xFFE8F5E9)
            timerState == TimerManager.TimerState.RUNNING -> Color(0xFFFFF0EC)
            else -> Color(0xFFFCEBEE)
        },
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "bgColor"
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(stateColor)) {
        val availableHeight = maxHeight
        val circleSize = when {
            availableHeight < 640.dp -> 180.dp
            availableHeight < 740.dp -> 220.dp
            else -> 260.dp
        }
        val verticalSpacing = when {
            availableHeight < 640.dp -> 12.dp
            availableHeight < 740.dp -> 16.dp
            else -> 24.dp
        }

        Scaffold(
            modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }.imePadding(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0.dp),
            floatingActionButton = {
            AnimatedVisibility(
                visible = !isAddingTask && timerState == TimerManager.TimerState.STOPPED,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier.offset(y = -(bottomPadding + 16.dp))
            ) {
                FloatingActionButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        viewModel.setAddingTask(true)
                    },
                    containerColor = Color(0xFFFF8A80),
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.8f))
                            .border(1.dp, Color.White, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🍅", fontSize = 24.scaledSp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "PomoPal",
                            fontSize = 24.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = AppFontFamily,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        Text(
                            text = "by Abhi",
                            fontSize = 12.scaledSp,
                            fontFamily = AppFontFamily,
                            color = Color(0xFF5D4037).copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 0.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.4f))
                        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                        .clickable {
                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (e: Exception) {}
                            viewModel.setSettingsOpen(true)
                        },
                     contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color(0xFF5D4037))
                }
            }
            Spacer(modifier = Modifier.height(verticalSpacing))

            // Timer display
            TimerDisplay(
                timeRemaining = timeRemaining,
                totalTime = if (isBreakMode) SettingsManager.getBreakTimeMins() * 60 else SettingsManager.getFocusTimeMins() * 60,
                state = timerState,
                isBreakMode = isBreakMode,
                taskName = currentTaskName,
                context = context,
                circleSize = circleSize
            )

            Spacer(modifier = Modifier.height(verticalSpacing))

            // Controls
            AppControls(state = timerState, context = context)

            Spacer(modifier = Modifier.height(verticalSpacing))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = timerState == TimerManager.TimerState.STOPPED,
                    transitionSpec = {
                        val enterSpring = spring<androidx.compose.ui.unit.IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                        val exitSpring = spring<androidx.compose.ui.unit.IntOffset>(stiffness = Spring.StiffnessMedium)
                        (fadeIn(spring(stiffness = Spring.StiffnessLow)) + slideInVertically(animationSpec = enterSpring) { 100 }) togetherWith 
                                (fadeOut(spring(stiffness = Spring.StiffnessMedium)) + slideOutVertically(animationSpec = exitSpring) { -100 })
                    },
                    label = "bottomContent"
                ) { stopped ->
                    if (stopped) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = bottomPadding + 88.dp)
                        ) {
                            item {
                                Text(
                                    text = "Your Tasks",
                                    fontSize = 20.scaledSp,
                                    fontFamily = AppFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF5D4037),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                            items(allTasks, key = { it.id }) { task ->
                                TaskItemRow(
                                    task = task,
                                    onSelect = { viewModel.setTask(task.id, task.name) },
                                    onDelete = { viewModel.deleteTask(task) }
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AnimatedQuoteCard(quote = currentQuote, isBreak = isBreakMode)
                            
                            if (!isBreakMode && timerState == TimerManager.TimerState.RUNNING) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        showFullScreenSeriousness = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Lock,
                                        contentDescription = "Serious Mode Lock",
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Lock Screen in Serious Mode", fontSize = 14.scaledSp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.height(bottomPadding + 88.dp))
                        }
                    }
                }
            }
        }
    }
    
    AnimatedVisibility(
        visible = showFullScreenSeriousness,
        enter = fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.95f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.95f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
    ) {
        SeriousFullscreenOverlay(
            quote = currentQuote,
            taskName = currentTaskName,
            timeRemaining = timeRemaining,
            onDismiss = { showFullScreenSeriousness = false }
        )
    }
        AnimatedVisibility(
            visible = showSettings,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow)) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) + fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsOverlay(onDismiss = { viewModel.setSettingsOpen(false) })
        }
    } // closes the Box Wrapping the Scaffold

    if (isAddingTask) {
        AddTaskCard(
            onSave = { name, cat, color ->
                viewModel.saveTask(name, cat, color)
                viewModel.setAddingTask(false)
                viewModel.setTask(-1, name)
            },
            onCancel = { viewModel.setAddingTask(false) }
        )
    }
}

@Composable
fun SettingsOverlay(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    var localFocus by remember { mutableStateOf(SettingsManager.getFocusTimeMins().toFloat()) }
    var localBreak by remember { mutableStateOf(SettingsManager.getBreakTimeMins().toFloat()) }
    var localCompletion by remember { mutableStateOf(SettingsManager.getCompletionSound()) }
    var localCompletionDuration by remember { mutableStateOf(SettingsManager.getCompletionDurationSec().toFloat()) }
    
    val audioManager = context.getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
    val maxVolume = audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC).toFloat()
    var localVolume by remember { mutableStateOf(audioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC).toFloat()) }
    
    val completionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(androidx.activity.result.contract.ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            SettingsManager.setCustomCompletionUri(uri.toString())
            localCompletion = 3 // Custom
            com.example.service.SoundPlayer.playCompletion(context, localCompletion)
        }
    }
    
    androidx.activity.compose.BackHandler(enabled = true) {
        com.example.service.SoundPlayer.stop()
        onDismiss()
    }

    // Dynamic state trackers for system permissions
    val notificationManagerCompat = remember { androidx.core.app.NotificationManagerCompat.from(context) }
    var areNotificationsEnabled by remember { mutableStateOf(notificationManagerCompat.areNotificationsEnabled()) }
    
    val powerManager = remember { context.getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager }
    var isIgnoringBattery by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                powerManager.isIgnoringBatteryOptimizations(context.packageName)
            } else {
                true
            }
        )
    }
    
    val systemNotificationManager = remember { context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager }
    var isDNDGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemNotificationManager.isNotificationPolicyAccessGranted
            } else {
                true
            }
        )
    }
    
    // Live polling to refresh permission statuses instantly of background setting changes
    LaunchedEffect(Unit) {
        while (true) {
            areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled()
            isIgnoringBattery = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                powerManager.isIgnoringBatteryOptimizations(context.packageName)
            } else {
                true
            }
            isDNDGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                systemNotificationManager.isNotificationPolicyAccessGranted
            } else {
                true
            }
            kotlinx.coroutines.delay(1000)
        }
    }
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFF0EC),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Settings",
                    fontFamily = CursiveFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.scaledSp,
                    color = Color(0xFF5D4037)
                )

                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        com.example.service.SoundPlayer.stop()
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cancel & Close",
                        tint = Color(0xFF5D4037)
                    )
                }
            }
        },
        bottomBar = {
            // Elegant sticky bottom actions panel
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD180).copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                com.example.service.SoundPlayer.stop()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFEBE9)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF5D4037).copy(alpha = 0.15f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Cancel", color = Color(0xFF5D4037), fontWeight = FontWeight.Bold, fontSize = 15.scaledSp)
                        }

                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                com.example.service.SoundPlayer.stop()
                                SettingsManager.setFocusTimeMins(localFocus.toInt())
                                SettingsManager.setBreakTimeMins(localBreak.toInt())
                                SettingsManager.setCompletionSound(localCompletion)
                                SettingsManager.setCompletionDurationSec(localCompletionDuration.toInt())
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A80)),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(50.dp),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Save Settings", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.scaledSp)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            var focusInput by remember(localFocus) { mutableStateOf(localFocus.toInt().toString()) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Focus Time (min):", color = Color(0xFF5D4037), fontWeight = FontWeight.Bold)
                androidx.compose.foundation.text.BasicTextField(
                    value = focusInput,
                    onValueChange = { newVal ->
                        val filtered = newVal.filter { it.isDigit() }
                        focusInput = filtered
                        filtered.toFloatOrNull()?.let { 
                            localFocus = it.coerceIn(1f, 200f) 
                        }
                    },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color(0xFF5D4037), fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.End),
                    modifier = Modifier.width(60.dp).background(Color.Black.copy(alpha=0.05f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                    singleLine = true
                )
            }
            Slider(
                value = localFocus.coerceIn(1f, 60f),
                onValueChange = { 
                    val oldVal = localFocus.toInt()
                    val newVal = it.toInt()
                    if (oldVal != newVal) {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
                        } catch (e: Exception) {}
                    }
                    localFocus = it 
                },
                valueRange = 1f..60f,
                colors = SliderDefaults.colors(thumbColor = Color(0xFFFF8A80), activeTrackColor = Color(0xFFFFCC80))
            )
            Spacer(modifier = Modifier.height(16.dp))
            var breakInput by remember(localBreak) { mutableStateOf(localBreak.toInt().toString()) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Break Time (min):", color = Color(0xFF5D4037), fontWeight = FontWeight.Bold)
                androidx.compose.foundation.text.BasicTextField(
                    value = breakInput,
                    onValueChange = { newVal ->
                        val filtered = newVal.filter { it.isDigit() }
                        breakInput = filtered
                        filtered.toFloatOrNull()?.let { 
                            localBreak = it.coerceIn(1f, 120f) 
                        }
                    },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color(0xFF5D4037), fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.End),
                    modifier = Modifier.width(60.dp).background(Color.Black.copy(alpha=0.05f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                    singleLine = true
                )
            }
            Slider(
                value = localBreak.coerceIn(1f, 30f),
                onValueChange = { 
                    val oldVal = localBreak.toInt()
                    val newVal = it.toInt()
                    if (oldVal != newVal) {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
                        } catch (e: Exception) {}
                    }
                    localBreak = it 
                },
                valueRange = 1f..30f,
                colors = SliderDefaults.colors(thumbColor = Color(0xFF81D4FA), activeTrackColor = Color(0xFFB3E5FC))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Volume", color = Color(0xFF5D4037), fontWeight = FontWeight.Bold)
            Slider(
                value = localVolume,
                onValueChange = { 
                    val oldVal = localVolume.toInt()
                    val newVal = it.toInt()
                    if (oldVal != newVal) {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
                        } catch (e: Exception) {}
                    }
                    localVolume = it
                    audioManager.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, it.toInt(), 0)
                },
                valueRange = 0f..maxVolume,
                steps = (maxVolume.toInt() - 1).takeIf { it > 0 } ?: 0,
                colors = SliderDefaults.colors(thumbColor = Color(0xFFCE93D8), activeTrackColor = Color(0xFFE1BEE7))
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Completion Sound", color = Color(0xFF5D4037), fontSize = 14.scaledSp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.Black.copy(alpha=0.05f))) {
                val compOptions = listOf("Beep", "Alarm", "Ring", "Custom")
                compOptions.forEachIndexed { index, name ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (localCompletion == index) Color(0xFFFF8A80) else Color.Transparent)
                            .clickable { 
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                if (index == 3) {
                                    completionLauncher.launch(arrayOf("audio/*"))
                                } else {
                                    localCompletion = index
                                    com.example.service.SoundPlayer.playCompletion(context, index)
                                }
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(name, color = if (localCompletion == index) Color.White else Color(0xFF5D4037), fontSize = 11.scaledSp, maxLines = 1) }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Completion Sound Duration: ${localCompletionDuration.toInt()} sec", color = Color(0xFF5D4037), fontSize = 14.scaledSp, fontWeight = FontWeight.Bold)
            Slider(
                value = localCompletionDuration,
                onValueChange = { 
                    val oldVal = localCompletionDuration.toInt()
                    val newVal = it.toInt()
                    if (oldVal != newVal) {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
                        } catch (e: Exception) {}
                    }
                    localCompletionDuration = it 
                },
                valueRange = 2f..20f,
                colors = SliderDefaults.colors(thumbColor = Color(0xFF81D4FA), activeTrackColor = Color(0xFFB3E5FC))
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCC80).copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            tint = Color(0xFFFF8A80),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "System Alarm & Lock Screen Settings",
                            fontSize = 14.scaledSp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5D4037)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "To make sure completion alarms show on the lock screen immediately and bypass battery saving background limits, configure these parameters:",
                        fontSize = 11.scaledSp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                val intent = Intent().apply {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        action = android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                        putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                                    } else {
                                        action = "android.settings.APP_NOTIFICATION_SETTINGS"
                                        putExtra("app_package", context.packageName)
                                        putExtra("app_uid", context.applicationInfo.uid)
                                    }
                                }
                                try { context.startActivity(intent) } catch (e: Exception) {}
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (areNotificationsEnabled) Color(0xFF81C784) else Color(0xFFFF8A80)
                             ),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (areNotificationsEnabled) {
                                    Icon(Icons.Filled.Settings, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Notifications Status: ENABLED ✅", fontSize = 11.scaledSp, color = Color.White, fontWeight = FontWeight.Bold)
                                } else {
                                    Text("Configure Notifications 🚨", fontSize = 11.scaledSp, color = Color.White)
                                }
                            }
                        }
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    val intent = Intent().apply {
                                        val pm = context.getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager
                                        if (pm.isIgnoringBatteryOptimizations(context.packageName)) {
                                            action = android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                                        } else {
                                            action = android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                                            data = android.net.Uri.parse("package:${context.packageName}")
                                        }
                                    }
                                    try { context.startActivity(intent) } catch (e: Exception) {}
                                }
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isIgnoringBattery) Color(0xFF81C784) else Color(0xFF81D4FA)
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (isIgnoringBattery) {
                                    Icon(Icons.Filled.Settings, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Battery Optimizations: BYPASSED ✅", fontSize = 11.scaledSp, color = Color.White, fontWeight = FontWeight.Bold)
                                } else {
                                    Text("Configure Battery Exceptions Limit", fontSize = 11.scaledSp, color = Color.White)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (e: Exception) {}
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                                try { context.startActivity(intent) } catch (e: Exception) {}
                            }
                        },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDNDGranted) Color(0xFF81C784) else Color(0xFFCE93D8)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (isDNDGranted) {
                                Icon(Icons.Filled.Settings, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("DND Access Granted ✅", fontSize = 11.scaledSp, color = Color.White, fontWeight = FontWeight.Bold)
                            } else {
                                Text("Grant DND (Blocks Other Apps' Notifications)", fontSize = 11.scaledSp, color = Color.White)
                            }
                        }
                    }
                    
                    // Live Side-loaded App restricted setting guidelines for users on Android 13+
                    if (!isDNDGranted) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFF9C4), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFFBC02D).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "💡 Side-loaded App Tip:\nIf PomoPal is not listed on the DND screen, please go to your phone's Settings > Apps > PomoPal > click the (⋮) menu in the top-right corner > select 'Allow restricted settings'. Then return here to grant access.",
                                fontSize = 11.scaledSp,
                                color = Color(0xFF5D4037),
                                fontWeight = FontWeight.Medium,
                                lineHeight = 16.scaledSp
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // The user specified credits: PomoPal v1.0, Designed & Developed by Abhinav Yaduvanshi
            Text(
                text = "PomoPal v1.0",
                fontSize = 13.scaledSp,
                color = Color(0xFF5D4037).copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Designed & Developed by Abhinav Yaduvanshi",
                fontSize = 11.scaledSp,
                color = Color(0xFF5D4037).copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TaskItemRow(task: com.example.data.TaskItem, onSelect: (com.example.data.TaskItem) -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    val view = androidx.compose.ui.platform.LocalView.current
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                try {
                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {}
                onSelect(task)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color(task.categoryColor.toULong()))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.name, fontWeight = FontWeight.Bold, fontFamily = AppFontFamily)
                Text(text = task.categoryName, fontSize = 12.scaledSp, color = Color.Gray)
            }
            if (task.completedPomodoros > 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (task.completedPomodoros <= 5) {
                        repeat(task.completedPomodoros) {
                            Text("🍅", fontSize = 16.scaledSp)
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                    } else {
                        Text("🍅", fontSize = 16.scaledSp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "x${task.completedPomodoros}", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            IconButton(onClick = {
                try {
                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {}
                onDelete()
            }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color(0xFFEF9A9A))
            }
        }
    }
}

@Composable
fun PremiumJumpingTextPreview(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(44.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (text.isEmpty()) {
            Text(
                text = "Type task name...",
                fontSize = 18.scaledSp,
                color = Color.Gray.copy(alpha = 0.5f),
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Medium
            )
        } else {
            text.forEachIndexed { index, char ->
                val animatedOffset = remember(index) { androidx.compose.animation.core.Animatable(-30f) }
                val animatedScale = remember(index) { androidx.compose.animation.core.Animatable(0.4f) }
                val animatedAlpha = remember(index) { androidx.compose.animation.core.Animatable(0f) }
                
                LaunchedEffect(key1 = index) {
                    animatedOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                            stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                        )
                    )
                }
                LaunchedEffect(key1 = index) {
                    animatedScale.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy)
                    )
                }
                LaunchedEffect(key1 = index) {
                    animatedAlpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 250)
                    )
                }
                
                Text(
                    text = if (char == ' ') " " else char.toString(),
                    fontSize = 24.scaledSp,
                    fontFamily = AppFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037),
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = animatedOffset.value
                            scaleX = animatedScale.value
                            scaleY = animatedScale.value
                            this.alpha = animatedAlpha.value
                        }
                        .padding(horizontal = 1.dp)
                )
            }
        }
    }
}

@Composable
fun AddTaskCard(onSave: (String, String, Long) -> Unit, onCancel: () -> Unit) {
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    androidx.compose.ui.window.Dialog(
        onDismissRequest = {}, // Empty lambda to disable platform click outside dismiss
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true // Support system back button for convenience, or false if strictly button only
        )
    ) {
        var animateIn by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            animateIn = true
        }

        val scale by animateFloatAsState(
            targetValue = if (animateIn) 1f else 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "dialogScale"
        )
        val alpha by animateFloatAsState(
            targetValue = if (animateIn) 1f else 0f,
            animationSpec = tween(durationMillis = 250),
            label = "dialogAlpha"
        )

        val view = androidx.compose.ui.platform.LocalView.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f * alpha))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0EC)),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus() })
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                var title by remember { mutableStateOf("") }
                
                val categories = listOf(
                    Pair("Study", Color(0xFF81D4FA)),
                    Pair("Work", Color(0xFFA5D6A7)),
                    Pair("Read", Color(0xFFCE93D8)),
                    Pair("Rest", Color(0xFFFFAB91))
                )
                var selectedCategory by remember { mutableStateOf(categories[0]) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        }
                        .padding(24.dp)
                ) {
                    Text(
                        text = "New Task",
                        fontFamily = CursiveFontFamily,
                        fontSize = 28.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    PremiumJumpingTextPreview(text = title)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Task Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedBorderColor = Color(0xFF5D4037),
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = Color(0xFF5D4037)
                        ),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = androidx.compose.ui.text.input.ImeAction.Done
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                onSave(title.ifEmpty { "Do nothing" }, selectedCategory.first, selectedCategory.second.value.toLong())
                            }
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Category", fontSize = 14.scaledSp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { cat ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedCategory == cat) cat.second else cat.second.copy(alpha = 0.3f))
                                    .border(if (selectedCategory == cat) 2.dp else 0.dp, Color.Black, CircleShape)
                                    .clickable {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        selectedCategory = cat
                                    }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        TextButton(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                onCancel()
                            }
                        ) {
                            Text("Cancel", color = Color.Gray, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                onSave(title.ifEmpty { "Do nothing" }, selectedCategory.first, selectedCategory.second.value.toLong())
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D4037)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedScaleBox(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val view = androidx.compose.ui.platform.LocalView.current
    
    Box(
        modifier = modifier
            .scale(scale)
            .alpha(if (enabled) 1f else 0.5f)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun AppControls(state: TimerManager.TimerState, context: android.content.Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedScaleBox(
            onClick = {
                val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_STOP }
                context.startService(intent)
            }
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Stop, contentDescription = "Stop", tint = Color(0xFF5D4037), modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        val isRunning = state == TimerManager.TimerState.RUNNING
        AnimatedScaleBox(
            onClick = {
                val action = if (isRunning) TimerService.ACTION_PAUSE else TimerService.ACTION_START
                val intent = Intent(context, TimerService::class.java).apply { this.action = action }
                context.startService(intent)
            }
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF8A80)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(targetState = isRunning, label = "playPause") { running ->
                    Icon(
                        if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow, 
                        contentDescription = if (running) "Pause" else "Start", 
                        tint = Color.White, 
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        val isBreakMode by TimerManager.isBreakMode.collectAsState()
        AnimatedScaleBox(
            enabled = !isRunning,
            onClick = {
                val isServiceActive = state != TimerManager.TimerState.STOPPED
                if (isServiceActive) {
                    val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_TOGGLE_MODE }
                    context.startService(intent)
                } else {
                    val newIsBreak = !isBreakMode
                    TimerManager.setBreakMode(newIsBreak)
                    TimerManager.updateState(TimerManager.TimerState.STOPPED)
                    TimerManager.updateTime(if (newIsBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value)
                    TimerManager.setTask(-1, if (newIsBreak) "Break Time!" else "Focus Time!")
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = "Switch Mode",
                    tint = Color(0xFF5D4037),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun SlidingTimer(timeRemaining: Int, fontSize: androidx.compose.ui.unit.TextUnit = 64.scaledSp) {
    val timeString = "%02d:%02d".format(timeRemaining / 60, timeRemaining % 60)
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        timeString.forEachIndexed { index, char ->
            if (char == ':') {
                Text(
                    text = ":",
                    fontSize = fontSize,
                    fontWeight = FontWeight.Black,
                    fontFamily = MonospaceFontFamily,
                    color = Color(0xFF5D4037),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            } else {
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        val enterSpring = spring<androidx.compose.ui.unit.IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                        val exitSpring = spring<androidx.compose.ui.unit.IntOffset>(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
                        val alphaEnter = spring<Float>(stiffness = Spring.StiffnessLow)
                        val alphaExit = spring<Float>(stiffness = Spring.StiffnessMedium)
                        
                        if (targetState < initialState || (initialState == '0' && targetState == '9') || (initialState == '0' && targetState == '5')) {
                            // Counting down
                            (slideInVertically(enterSpring) { height -> -height } + fadeIn(alphaEnter)) togetherWith
                                    (slideOutVertically(exitSpring) { height -> height } + fadeOut(alphaExit))
                        } else {
                            // Counting up or reset
                            (slideInVertically(enterSpring) { height -> height } + fadeIn(alphaEnter)) togetherWith
                                    (slideOutVertically(exitSpring) { height -> -height } + fadeOut(alphaExit))
                        }.using(SizeTransform(clip = false))
                    },
                    label = "digitAnimation_$index"
                ) { targetDigit ->
                    Text(
                        text = targetDigit.toString(),
                        fontSize = fontSize,
                        fontWeight = FontWeight.Black,
                        fontFamily = MonospaceFontFamily,
                        color = Color(0xFF5D4037)
                    )
                }
            }
        }
    }
}

@Composable
fun TimerDisplay(
    timeRemaining: Int,
    totalTime: Int,
    state: TimerManager.TimerState,
    isBreakMode: Boolean,
    taskName: String,
    context: android.content.Context,
    circleSize: androidx.compose.ui.unit.Dp = 260.dp
) {
    val view = androidx.compose.ui.platform.LocalView.current
    var animatedText by remember { mutableStateOf("") }
    
    // Typing effect for Task Name
    LaunchedEffect(taskName) {
        animatedText = ""
        for (i in taskName.indices) {
            animatedText += taskName[i]
            delay(50)
        }
    }

    val progress = if (totalTime > 0) timeRemaining.toFloat() / totalTime.toFloat() else 0f
    val animatedProgress = remember { Animatable(1f) }
    
    LaunchedEffect(progress) {
        animatedProgress.animateTo(progress, animationSpec = tween(1000, easing = LinearEasing))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(Color.White.copy(alpha = 0.6f), Color.White.copy(alpha = 0.2f))
                    )
                )
                .border(4.dp, Color.White, CircleShape)
        ) {
            val dynamicCircleSize = maxWidth
            Canvas(modifier = Modifier
                .fillMaxSize()
                .padding(if (dynamicCircleSize < 200.dp) 8.dp else 16.dp)) {
                drawArc(
                    color = Color.White.copy(alpha = 0.2f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = if (dynamicCircleSize < 200.dp) 4.dp.toPx() else 8.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = if (isBreakMode) Color(0xFF81D4FA) else Color(0xFFFF8A80),
                    startAngle = -90f,
                    sweepAngle = animatedProgress.value * 360f,
                    useCenter = false,
                    style = Stroke(width = if (dynamicCircleSize < 200.dp) 4.dp.toPx() else 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val isLongFormat = (timeRemaining / 60) > 99
                val scaleFactor = if (isLongFormat) 0.16f else 0.22f
                val baseFontSize = dynamicCircleSize.value * scaleFactor
                val timerFontSize = baseFontSize.coerceAtMost(if (isLongFormat) 38f else 54f).sp
                SlidingTimer(timeRemaining = timeRemaining, fontSize = timerFontSize)
                Spacer(modifier = Modifier.height(if (dynamicCircleSize < 200.dp) 2.dp else 4.dp))
                val stateBg = if (isBreakMode) Color(0xFFE1F5FE) else Color(0xFFFFCDD2)
                val stateTextCol = if (isBreakMode) Color(0xFF0288D1) else Color(0xFFD32F2F)
                val stateText = if (isBreakMode) "BREAK TIME" else "WORK TIME"
                val isTimerRunning = state == TimerManager.TimerState.RUNNING
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(stateBg)
                        .alpha(if (isTimerRunning) 0.5f else 1f)
                        .clickable(enabled = !isTimerRunning) {
                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (e: Exception) {}
                            val isServiceActive = state != TimerManager.TimerState.STOPPED
                            if (isServiceActive) {
                                val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_TOGGLE_MODE }
                                context.startService(intent)
                            } else {
                                val newIsBreak = !isBreakMode
                                TimerManager.setBreakMode(newIsBreak)
                                TimerManager.updateState(TimerManager.TimerState.STOPPED)
                                TimerManager.updateTime(if (newIsBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value)
                                TimerManager.setTask(-1, if (newIsBreak) "Break Time!" else "Focus Time!")
                            }
                        }
                        .padding(horizontal = if (dynamicCircleSize < 200.dp) 8.dp else 12.dp, vertical = if (dynamicCircleSize < 200.dp) 2.dp else 4.dp)
                ) {
                    Text(
                        text = stateText,
                        fontSize = if (dynamicCircleSize < 200.dp) 9.scaledSp else 12.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = stateTextCol,
                        letterSpacing = 1.scaledSp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(if (circleSize < 200.dp) 16.dp else if (circleSize < 240.dp) 24.dp else 40.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF81C784))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Current Task",
                fontSize = 14.scaledSp,
                color = Color(0xFF5D4037).copy(alpha = 0.7f),
                fontFamily = AppFontFamily
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = animatedText,
                fontSize = 24.scaledSp,
                fontFamily = CursiveFontFamily,
                color = Color(0xFF5D4037),
                fontWeight = FontWeight.Bold
            )
            val cursorAlpha = rememberInfiniteTransition().animateFloat(
                initialValue = 1f, targetValue = 0f, 
                animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF8A80).copy(alpha = cursorAlpha.value))
            )
        }
    }
}

@Composable
fun AnimatedQuoteCard(quote: String, isBreak: Boolean) {
    val quoteColor = if (isBreak) Color(0xFF2E7D32) else Color(0xFF8D6E63)
    val accentColor = if (isBreak) Color(0xFF81C784) else Color(0xFFFF8A80)
    
    // Create soft pulse animation for an artistic breathing effect
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 24.dp)
            .graphicsLayer {
                scaleX = pulseScale
                scaleY = pulseScale
            }
            .border(
                width = 1.5.dp,
                color = accentColor.copy(alpha = pulseAlpha),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Background quotation mark top-left
            Text(
                text = "“",
                fontSize = 80.scaledSp,
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Bold,
                color = accentColor.copy(alpha = 0.15f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-8).dp, y = (-35).dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated text change
                AnimatedContent(
                    targetState = quote,
                    transitionSpec = {
                        (fadeIn(tween(600, easing = FastOutSlowInEasing)) + slideInVertically(animationSpec = tween(600)) { 20 }).togetherWith(
                            fadeOut(tween(400, easing = FastOutSlowInEasing)) + slideOutVertically(animationSpec = tween(400)) { -20 }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = "quoteContent"
                ) { targetQuote ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = targetQuote.ifEmpty { "Stay present and fully engaged." },
                            fontSize = 18.scaledSp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontFamily = AppFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = quoteColor,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 28.scaledSp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Visual tiny divider dot
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(accentColor.copy(alpha = 0.6f))
                        )
                    }
                }
            }

            // Background quotation mark bottom-right
            Text(
                text = "”",
                fontSize = 80.scaledSp,
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Bold,
                color = accentColor.copy(alpha = 0.15f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 15.dp)
            )
        }
    }
}

@Composable
fun SeriousFullscreenOverlay(
    quote: String,
    taskName: String,
    timeRemaining: Int,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity
    
    // Prevent exiting using system back gestures/keys
    androidx.activity.compose.BackHandler(enabled = true) {
        // Intercept back button and do absolutely nothing to block exit
    }

    // Handle Lock Task (Screen Pinning) & Do Not Disturb toggles in lock lifecycle
    DisposableEffect(Unit) {
        // 1. Lock Task (Pins screens to block navigation, status bar drag, and other apps opening)
        try {
            activity?.startLockTask()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. Do Not Disturb (Silence apps / distractions)
        val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as? android.app.NotificationManager
        var originalInterruptionFilter = -1
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && notificationManager != null) {
                if (notificationManager.isNotificationPolicyAccessGranted) {
                    originalInterruptionFilter = notificationManager.currentInterruptionFilter
                    notificationManager.setInterruptionFilter(android.app.NotificationManager.INTERRUPTION_FILTER_NONE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            // Restore normal device navigation & notification state
            try {
                activity?.stopLockTask()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && notificationManager != null && originalInterruptionFilter != -1) {
                    if (notificationManager.isNotificationPolicyAccessGranted) {
                        notificationManager.setInterruptionFilter(originalInterruptionFilter)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "seriousness")
    
    val glowColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF1E0E0E),
        targetValue = Color(0xFF0F0505),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowColor"
    )

    val scaleGlow by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleGlow"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(glowColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {} // Consume clicks to make it fully immersive
    ) {
        val availableHeight = maxHeight
        val isSmallScreen = availableHeight < 640.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(if (isSmallScreen) 16.dp else 24.dp)
                .then(if (isSmallScreen) Modifier.verticalScroll(rememberScrollState()) else Modifier),
            verticalArrangement = if (isSmallScreen) Arrangement.Top else Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF5252))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ULTRA FOCUS ACTIVE",
                        color = Color(0xFFFF8A80),
                        fontSize = 13.scaledSp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.scaledSp
                    )
                }
                
                // Ticking Countdown Timer display
                val mins = timeRemaining / 60
                val secs = timeRemaining % 60
                val timerText = "%02d:%02d".format(mins, secs)
                Text(
                    text = timerText,
                    color = Color.White,
                    fontSize = 52.scaledSp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = MonospaceFontFamily,
                    letterSpacing = 2.scaledSp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
                val targetText = if (taskName.isNotBlank() && taskName != "Focus Time!" && taskName != "Break Time!") {
                    "Working on: $taskName"
                } else {
                    "Your study blocks generate your empire."
                }
                Text(
                    text = targetText,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.scaledSp,
                    fontWeight = FontWeight.Medium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Central Massive Seriousness Quote Block
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (isSmallScreen) Modifier.padding(vertical = 16.dp) else Modifier.weight(1f)),
                verticalArrangement = Arrangement.Center
            ) {
                // Giant quotation mark
                Text(
                    text = "“",
                    fontSize = 110.scaledSp,
                    color = Color(0xFFFF5252).copy(alpha = 0.12f),
                    fontFamily = AppFontFamily,
                    modifier = Modifier.offset(y = 25.dp)
                )

                // The hard-hitting Quote
                AnimatedContent(
                    targetState = quote,
                    transitionSpec = {
                        (fadeIn(tween(800)) + scaleIn()).togetherWith(fadeOut(tween(500)) + scaleOut())
                    },
                    modifier = Modifier.padding(horizontal = 8.dp),
                    label = "fullscreenQuote"
                ) { targetQuote ->
                    Text(
                        text = targetQuote.ifEmpty { "Stay present and fully engaged." },
                        color = Color(0xFFFFEBEE),
                        fontSize = 24.scaledSp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = AppFontFamily,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 36.scaledSp,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scaleGlow
                            scaleY = scaleGlow
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // High-conviction message
                Text(
                    text = "🚨 FOCUS NOW • STOP WATCHING THE TIMER. YOUR DREAMS REQUIRE DISCIPLINE, NOT DOPAMINE SWIPES.",
                    color = Color(0xFFFF8A80),
                    fontSize = 12.scaledSp,
                    fontWeight = FontWeight.Black,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    letterSpacing = 1.scaledSp,
                    lineHeight = 18.scaledSp,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(
                            color = Color(0xFFFF5252).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // Footer action
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                val totalFocus = SettingsManager.getFocusTimeMins() * 60
                val elapsed = (totalFocus - timeRemaining).coerceAtLeast(0)
                val requiredElapsedToExit = (totalFocus * 0.75f).toInt()
                val canExit = elapsed >= requiredElapsedToExit
                
                val progressToExit = if (requiredElapsedToExit > 0) {
                    (elapsed.toFloat() / requiredElapsedToExit.toFloat()).coerceIn(0f, 1f)
                } else {
                    1f
                }

                if (!canExit) {
                    val timeUntilUnlock = (requiredElapsedToExit - elapsed).coerceAtLeast(0)
                    val minsLeft = timeUntilUnlock / 60
                    val secsLeft = timeUntilUnlock % 60
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(Color(0xFF2D1414), RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0xFFE57373).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Locked",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Emergency Exit Locked",
                                color = Color(0xFFFFCDD2),
                                fontSize = 12.scaledSp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.scaledSp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Micro progress bar towards 75% unlock
                        LinearProgressIndicator(
                            progress = { progressToExit },
                            color = Color(0xFFFF5252),
                            trackColor = Color(0xFFFF5252).copy(alpha = 0.15f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Text(
                            text = "Unlocks at 75% focus block complete: ${(progressToExit * 100).toInt()}% completed",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.scaledSp,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Focus for another ${if (minsLeft > 0) "${minsLeft}m " else ""}${secsLeft}s",
                            color = Color(0xFFFF8A80),
                            fontSize = 13.scaledSp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = MonospaceFontFamily
                        )
                    }
                } else {
                    // Pulsing Glow Exit trigger
                    val animatedAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.8f,
                        targetValue = 1.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "activeButtonAlpha"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .background(Color(0xFF1B5E20).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF81C784).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "🔓",
                                fontSize = 16.scaledSp,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = "Focus standards met! Exit is now authorized.",
                                color = Color(0xFF81C784),
                                fontSize = 12.scaledSp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.scaledSp
                            )
                        }
                        
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32).copy(alpha = 0.2f)),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF81C784).copy(alpha = animatedAlpha)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.height(48.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Exit Serious Mode",
                                tint = Color(0xFFE8F5E9),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Exit Serious Mode",
                                color = Color(0xFFE8F5E9),
                                fontSize = 13.scaledSp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlarmRingingLockScreen(
    label: String,
    targetCount: Int
) {
    val context = LocalContext.current
    var currentCount by remember { mutableStateOf(0) }
    var shakeWarning by remember { mutableStateOf(false) }
    var sensorMessage by remember { mutableStateOf("Ready to register. Put the device in your pocket and begin!") }
    var isFinished by remember { mutableStateOf(false) }

    // Intercept back button to prevent escaping the workout alarm
    androidx.activity.compose.BackHandler(enabled = true) {
        // Intercept back actions and do nothing
    }

    // Register Squat sensor service for real-time secure tracking
    DisposableEffect(Unit) {
        val sensorService = com.example.service.SquatSensorService(context)
        sensorService.startTracking(
            targetSquats = targetCount,
            onComplete = {
                com.example.service.SoundPlayer.stopContinuousAlarm()
                com.example.service.AlarmReceiver.dismissNotification(context)
                isFinished = true
            },
            onUpdate = { count ->
                currentCount = count
                sensorMessage = "Awesome! Perfect squat registered."
            },
            onShakeWarning = { isShaking ->
                shakeWarning = isShaking
                if (isShaking) {
                    sensorMessage = "Locked: Please stop shaking the phone!"
                } else {
                    sensorMessage = "Standing stable. Keep performing smooth physical squats."
                }
            }
        )

        onDispose {
            sensorService.stopTracking()
        }
    }

    // Fullscreen Dynamic Dawn Sunrise / Dark Premium Lock Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0E11)) // Midnight premium ash
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isFinished) {
                // Celebration screen - transitioning to study state
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E676).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Sleep Defeated! 🧠🔥",
                    fontSize = 26.scaledSp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = AppFontFamily
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your neuromuscular system is fully activated. Oxygenated blood flow has rushed to your brain, washing sleep inertia away instantly.\n\nYou are chemically primed for hyper-focus studying!",
                    fontSize = 13.scaledSp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.scaledSp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        try {
                            val view = context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? android.os.Vibrator
                            view?.vibrate(100)
                        } catch (e: Exception) {}
                        com.example.service.AlarmState.isAlarmRinging = false
                        com.example.service.AlarmReceiver.dismissNotification(context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "LAUNCH FOCUS SESSION 🚀",
                        fontSize = 14.scaledSp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0D0E11),
                        letterSpacing = 1.scaledSp
                    )
                }
            } else {
                // Active Alarm Screaming & Squat tracking
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "alarmScale"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676)) // Blink green indicator showing sensory active
                    )
                    Text(
                        text = "KINETIC G-SENSOR SYNCHRONIZED",
                        fontSize = 10.scaledSp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF00E676),
                        letterSpacing = 1.5.scaledSp,
                        fontFamily = MonospaceFontFamily
                    )
                }

                Text(
                    text = label,
                    fontSize = 24.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = AppFontFamily
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "SOMATIC WAKE-UP AUDIT",
                    fontSize = 11.scaledSp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFFB300),
                    letterSpacing = 2.scaledSp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Beautiful Circular Wakeup Activity Ring
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .scale(scale),
                    contentAlignment = Alignment.Center
                ) {
                    val progress = if (targetCount > 0) currentCount.toFloat() / targetCount.toFloat() else 0f
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "progress"
                    )
                    
                    // Shadow Ring (Background)
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White.copy(alpha = 0.05f),
                        strokeWidth = 14.dp,
                        strokeCap = StrokeCap.Round
                    )

                    // Energized Ring (Foreground)
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF00E676),
                        strokeWidth = 14.dp,
                        strokeCap = StrokeCap.Round
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$currentCount",
                            fontSize = 60.scaledSp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontFamily = MonospaceFontFamily
                        )
                        Text(
                            text = "/ $targetCount SQUATS",
                            fontSize = 11.scaledSp,
                            fontWeight = FontWeight.Black,
                            color = Color.White.copy(alpha = 0.5f),
                            letterSpacing = 1.scaledSp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Real-Time Waveform Generator indicating the body works
                val wavePhase = rememberInfiniteTransition(label = "wave")
                val waveOffset by wavePhase.animateFloat(
                    initialValue = 0f,
                    targetValue = 2f * 3.14159f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "waveOffset"
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(horizontal = 32.dp)
                ) {
                    val path = androidx.compose.ui.graphics.Path()
                    val width = size.width
                    val height = size.height
                    val midY = height / 2f
                    val amplitude = if (shakeWarning) 16f else 6f
                    val frequency = 3.5f
                    
                    path.moveTo(0f, midY)
                    for (x in 0..width.toInt() step 6) {
                        val relativeX = x.toFloat() / width
                        val sineVal = kotlin.math.sin(relativeX * frequency * 2f * 3.14159f + waveOffset)
                        val y = midY + (sineVal * amplitude)
                        path.lineTo(x.toFloat(), y)
                    }
                    
                    drawPath(
                        path = path,
                        color = if (shakeWarning) Color(0xFFFF5252).copy(alpha = 0.8f) else Color(0xFF00E676).copy(alpha = 0.5f),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = sensorMessage,
                    fontSize = 12.scaledSp,
                    fontWeight = FontWeight.Medium,
                    color = if (shakeWarning) Color(0xFFFF8A80) else Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Chaotic Shake Warning Indicator
                AnimatedVisibility(
                    visible = shakeWarning,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFD32F2F).copy(alpha = 0.15f))
                            .border(1.dp, Color(0xFFD32F2F).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Lock,
                            contentDescription = "Lock State Alert",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Chaotic Shaking Detected! Stand still and perform smooth, deep squats to pass.",
                            fontSize = 12.scaledSp,
                            color = Color(0xFFFF8A80),
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.scaledSp
                        )
                    }
                }

                // Normal instructions if not shaking
                if (!shakeWarning) {
                    Text(
                        text = "Phone loops sensory ringtones at full volume. No snooze, no exit! Pocket the device or hold it, then complete squats to unlock.",
                        fontSize = 11.scaledSp,
                        color = Color.White.copy(alpha = 0.35f),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.scaledSp,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}
