package com.example

import androidx.compose.ui.graphics.toArgb
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
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
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.CheckCircle
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
import androidx.compose.material.icons.filled.CloudUpload
import kotlinx.coroutines.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
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
import com.example.ui.components.scaledDp
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
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        SettingsManager.init(applicationContext)

        requestPermissions()

        database = DatabaseProvider.getDatabase(applicationContext)

        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            val currentTheme by SettingsManager.themeState.collectAsState()
            val currentFont by SettingsManager.fontState.collectAsState()
            MyApplicationTheme(
                themeOption = currentTheme,
                fontOption = currentFont
            ) {
                val viewModel: TimerViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return TimerViewModel(database) as T
                        }
                    }
                )
                val activeTheme = LocalAppTheme.current
                Box(modifier = Modifier.fillMaxSize().background(activeTheme.background)) {
                    com.example.ui.components.PaperTextureOverlay()
                    PomoPalApp(viewModel)
                }
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.getBooleanExtra("RETURNED_FROM_BLOCKER", false)) {
            android.widget.Toast.makeText(this, "Focus session protected. Welcome back! 🌿", android.widget.Toast.LENGTH_SHORT).show()
        } else if (intent.hasExtra("BLOCKED_APP")) {
            android.widget.Toast.makeText(this, "Focus session protected. Welcome back! 🌿", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun PomoPalApp(viewModel: TimerViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val startDest = if (com.example.service.SettingsManager.getUserName() == null) "signin" else "home"
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: startDest
    val isAddingTask by viewModel.isAddingTask.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val isSettingsOpen by viewModel.isSettingsOpen.collectAsState()
    val settingsInitialTab by viewModel.settingsTab.collectAsState()
    var updateUrl by remember { mutableStateOf<String?>(null) }
    val view = androidx.compose.ui.platform.LocalView.current

    val currentTheme = com.example.ui.theme.LocalAppTheme.current
    LaunchedEffect(Unit) {
        val url = UpdateChecker.checkForUpdates()
        if (url != null) {
            updateUrl = url
        }
    }

    if (updateUrl != null) {
        AlertDialog(
            onDismissRequest = { updateUrl = null },
            title = { Text("Update Available", color = currentTheme.textPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("A new version of PomoPal is available! Please download it to enjoy the latest features and bug fixes.", color = currentTheme.textPrimary) },
            confirmButton = {
                TextButton(onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(updateUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {}
                    updateUrl = null
                }) {
                    Text("Download", color = currentTheme.primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    updateUrl = null
                }) {
                    Text("Later", color = currentTheme.textSecondary)
                }
            },
            containerColor = currentTheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            // Floating Bottom Pill Navigation
            val isSignInRoute = currentRoute == "signin" || (navBackStackEntry?.destination?.route == "signin") || (navBackStackEntry == null && startDest == "signin")
            AnimatedVisibility(
                visible = !isSignInRoute && !isAddingTask && timerState == TimerManager.TimerState.STOPPED && !isSettingsOpen,
                enter = slideInVertically(
                    initialOffsetY = { it * 2 },
                    animationSpec = tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)
                ) + fadeIn(animationSpec = tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)),
                exit = slideOutVertically(
                    targetOffsetY = { it * 2 },
                    animationSpec = tween(PomoMotion.DURATION_FAST, easing = PomoMotion.SmoothInOutEasing)
                ) + fadeOut(animationSpec = tween(PomoMotion.DURATION_FAST)),
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = 16.dp, top = 8.dp)
            ) {
                val currentTheme = com.example.ui.theme.LocalAppTheme.current
                val currentFont = com.example.ui.theme.LocalAppFont.current
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = currentTheme.shadowColor)
                            .clip(RoundedCornerShape(32.dp))
                            .background(currentTheme.surface)
                            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(32.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .animateContentSize(animationSpec = tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothOutEasing)),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isHome = currentRoute == "home"
                        val homeBg by animateColorAsState(
                            targetValue = if (isHome) currentTheme.pillActiveBg else Color.Transparent,
                            animationSpec = PomoMotion.colorSpec,
                            label = "homePillBg"
                        )
                        val homeTint by animateColorAsState(
                            targetValue = if (isHome) currentTheme.primary else currentTheme.textSecondary.copy(alpha = 0.55f),
                            animationSpec = PomoMotion.colorSpec,
                            label = "homePillTint"
                        )

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(homeBg)
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
                                tint = homeTint,
                                modifier = Modifier.size(22.dp)
                            )
                            AnimatedVisibility(
                                visible = isHome,
                                enter = expandHorizontally(tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothOutEasing)) +
                                    fadeIn(tween(PomoMotion.DURATION_FAST)),
                                exit = shrinkHorizontally(tween(PomoMotion.DURATION_FAST, easing = PomoMotion.SmoothInOutEasing)) +
                                    fadeOut(tween(150))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Timer",
                                        color = currentTheme.primary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = currentFont,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        val isHistory = currentRoute == "history"
                        val historyBg by animateColorAsState(
                            targetValue = if (isHistory) currentTheme.pillActiveBg else Color.Transparent,
                            animationSpec = PomoMotion.colorSpec,
                            label = "historyPillBg"
                        )
                        val historyTint by animateColorAsState(
                            targetValue = if (isHistory) currentTheme.primary else currentTheme.textSecondary.copy(alpha = 0.55f),
                            animationSpec = PomoMotion.colorSpec,
                            label = "historyPillTint"
                        )

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(historyBg)
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
                                tint = historyTint,
                                modifier = Modifier.size(22.dp)
                            )
                            AnimatedVisibility(
                                visible = isHistory,
                                enter = expandHorizontally(tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothOutEasing)) +
                                    fadeIn(tween(PomoMotion.DURATION_FAST)),
                                exit = shrinkHorizontally(tween(PomoMotion.DURATION_FAST, easing = PomoMotion.SmoothInOutEasing)) +
                                    fadeOut(tween(150))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "History",
                                        color = currentTheme.primary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = currentFont,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDest,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                val fromSignIn = initialState.destination.route == "signin"
                val toHistory = targetState.destination.route == "history"
                if (fromSignIn) {
                    fadeIn(tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)) +
                        scaleIn(initialScale = 0.96f, animationSpec = tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing))
                } else {
                    fadeIn(tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothOutEasing)) +
                        slideInHorizontally(tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)) { width ->
                            if (toHistory) width / 6 else -width / 6
                        }
                }
            },
            exitTransition = {
                val fromSignIn = initialState.destination.route == "signin"
                val toHistory = targetState.destination.route == "history"
                if (fromSignIn) {
                    fadeOut(tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothInOutEasing)) +
                        scaleOut(targetScale = 0.94f, animationSpec = tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothInOutEasing))
                } else {
                    fadeOut(tween(PomoMotion.DURATION_FAST, easing = PomoMotion.SmoothInOutEasing)) +
                        slideOutHorizontally(tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothInOutEasing)) { width ->
                            if (toHistory) -width / 8 else width / 8
                        }
                }
            },
            popEnterTransition = {
                fadeIn(tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothOutEasing)) +
                    slideInHorizontally(tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)) { width -> -width / 6 }
            },
            popExitTransition = {
                fadeOut(tween(PomoMotion.DURATION_FAST, easing = PomoMotion.SmoothInOutEasing)) +
                    slideOutHorizontally(tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothInOutEasing)) { width -> width / 8 }
            }
        ) {
            val bottomPadding = paddingValues.calculateBottomPadding()
            composable(route = "signin") {
                com.example.ui.SignInScreen(navController = navController, onSignInSuccess = {
                    navController.navigate("home") {
                        popUpTo("signin") { inclusive = true }
                    }
                })
            }
            composable(route = "home") {
                HomeScreen(viewModel, navController, bottomPadding)
            }
            composable("history") {
                com.example.ui.HistoryScreen(viewModel, navController, bottomPadding)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TimerViewModel, navController: androidx.navigation.NavController, bottomPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.autoCleanupIfNeeded()
    }
    val timerState by viewModel.timerState.collectAsState()
    val isBreakMode by TimerManager.isBreakMode.collectAsState()
    val currentQuote by viewModel.currentQuote.collectAsState()
    val timeRemaining by viewModel.timeRemainingSeconds.collectAsState()
    val currentTaskName by viewModel.currentTaskName.collectAsState()
    val currentTaskId by com.example.service.TimerManager.currentTaskId.collectAsState()
    val currentTaskColor by com.example.service.TimerManager.currentTaskColor.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val isAddingTask by viewModel.isAddingTask.collectAsState()
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val view = androidx.compose.ui.platform.LocalView.current
    var showAllTasksSheet by remember { mutableStateOf(false) }

    val showSettings by viewModel.isSettingsOpen.collectAsState()
    val settingsInitialTab by viewModel.settingsTab.collectAsState()
    var showFullScreenSeriousness by remember { mutableStateOf(false) }
    var showJustInTimeSetup by remember { mutableStateOf(false) }
    val protectionStates = com.example.ui.rememberSystemProtectionStates()
    

    LaunchedEffect(showSettings) {
        if (!showSettings) {

        }
    }
    val userName = com.example.service.SettingsManager.getUserName() ?: "Friend"

    LaunchedEffect(timerState, isBreakMode) {
        if (timerState == TimerManager.TimerState.STOPPED || isBreakMode) {
            showFullScreenSeriousness = false
        }
    }

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val stateColor by animateColorAsState(
        targetValue = when {
            isBreakMode -> currentTheme.breakBg
            timerState == TimerManager.TimerState.RUNNING -> currentTheme.runningBg
            else -> currentTheme.background
        },
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "bgColor"
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(stateColor.copy(alpha = 0.85f))) {
        if (currentTheme.id == "premium" && !isBreakMode) {
            com.example.ui.components.PremiumBackgroundOverlays()
        }
        val hasBanner = protectionStates.needsAttention && timerState == TimerManager.TimerState.STOPPED
        com.example.ui.components.ProvideAdaptiveDimensions(
            availableWidth = maxWidth,
            availableHeight = maxHeight,
            hasBanner = hasBanner
        ) {
            val adaptiveDimensions = com.example.ui.components.LocalAdaptiveDimensions.current
            
            val circleSize = when {
                adaptiveDimensions.isVeryCompact -> 180.dp
                adaptiveDimensions.isCompact -> 220.dp
                else -> 260.dp
            } * adaptiveDimensions.scale
            
            val verticalSpacing = when {
                adaptiveDimensions.isVeryCompact -> 12.dp
                adaptiveDimensions.isCompact -> 16.dp
                else -> 24.dp
            } * adaptiveDimensions.scale

        Scaffold(
            modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }.imePadding(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0.dp)
        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 24.scaledDp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.scaledDp))
            
            // Small Global Indicator
            androidx.compose.animation.AnimatedVisibility(
                visible = protectionStates.needsAttention && timerState == TimerManager.TimerState.STOPPED,
                enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
            ) {
                com.example.ui.SystemProtectionBanner(
                    state = protectionStates,
                    onFixClicked = {
                        showJustInTimeSetup = true
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.scaledDp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .shadow(3.dp, RoundedCornerShape(16.dp), spotColor = currentTheme.shadowColor)
                            .clip(RoundedCornerShape(16.dp))
                            .background(currentTheme.surface)
                            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        com.example.ui.components.PomoPalMascot(
                            expression = com.example.ui.components.MascotExpression.IDLE,
                            modifier = Modifier.size(32.dp).padding(top = 2.dp),
                            isBreathing = false
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "PomoPal",
                            fontSize = 24.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            color = currentTheme.textPrimary,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        Text(
                            text = "Hi, $userName",
                            fontSize = 12.scaledSp,
                            fontFamily = currentFont,
                            color = currentTheme.textSecondary,
                            modifier = Modifier.padding(top = 0.dp)
                        )
                    }
                }
                
                val settingsInteraction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                val isSettingsPressed by settingsInteraction.collectIsPressedAsState()
                val settingsRotation by androidx.compose.animation.core.animateFloatAsState(
                    targetValue = if (isSettingsPressed) 45f else 0f,
                    animationSpec = PomoMotion.floatSpec,
                    label = "settingsRotation"
                )
                
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .shadow(3.dp, CircleShape, spotColor = currentTheme.shadowColor)
                        .clip(CircleShape)
                        .background(currentTheme.surface)
                        .border(1.dp, currentTheme.cardBorder, CircleShape)
                        .clickable(
                            interactionSource = settingsInteraction,
                            indication = androidx.compose.material3.ripple()
                        ) {
                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (e: Exception) {}
                            viewModel.setSettingsOpen(true, 0)
                        },
                     contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = currentTheme.textPrimary,
                        modifier = Modifier.graphicsLayer { rotationZ = settingsRotation }
                    )
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
                currentTask = allTasks.find { it.id == currentTaskId },
                context = context,
                circleSize = circleSize
            )

            Spacer(modifier = Modifier.height(verticalSpacing))

            // Controls
            AppControls(
                state = timerState, 
                context = context,
                onStartAttempt = {
                    if (protectionStates.needsAttention) {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS) 
                        } catch(e: Exception) {}
                        showJustInTimeSetup = true
                    } else {
                        try {
                            val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                            androidx.core.content.ContextCompat.startForegroundService(context, intent)
                        } catch (_: Exception) {}
                    }
                }
            )

            Spacer(modifier = Modifier.height(verticalSpacing))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AnimatedContent(
                    targetState = timerState == TimerManager.TimerState.STOPPED,
                    transitionSpec = {
                        PomoMotion.verticalCrossfadeTransition(upward = !targetState)
                    },
                    label = "bottomContent"
                ) { stopped ->
                    if (stopped) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = bottomPadding + 88.dp)
                        ) {
                            val availableHeight = maxHeight
                            val headerSpace = 48.dp
                            val buttonSpace = 56.dp
                            val taskSpace = 84.dp
                            
                            val usableHeightForTasks = availableHeight - headerSpace - buttonSpace
                            val calculatedFit = (usableHeightForTasks / taskSpace).toInt()
                            val maxPreviewTasks = calculatedFit.coerceIn(1, 3)

                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.scaledDp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.scaledDp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Your Tasks",
                                        fontSize = 20.scaledSp,
                                        fontFamily = AppFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = currentTheme.textPrimary
                                    )
                                    IconButton(onClick = { viewModel.setAddingTask(true) }, modifier = Modifier.size(36.dp)) {
                                        Icon(Icons.Filled.Add, contentDescription = "Add Task", tint = currentTheme.textPrimary)
                                    }
                                }
                                
                                val previewTasks = allTasks.take(maxPreviewTasks)
                                previewTasks.forEach { task ->
                                    TaskItemRow(
                                        task = task,
                                        isSelected = currentTaskId == task.id,
                                        onSelect = { viewModel.setTask(task.id, task.name, task.categoryColor) },
                                        onDelete = { viewModel.deleteTask(task) }
                                    )
                                }
                                
                                if (allTasks.size > maxPreviewTasks) {
                                    TextButton(
                                        onClick = { showAllTasksSheet = true },
                                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
                                    ) {
                                        Text("View all tasks →", color = currentTheme.textSecondary, fontWeight = FontWeight.Bold)
                                    }
                                }
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
        enter = fadeIn(animationSpec = tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)) +
            scaleIn(initialScale = 0.96f, animationSpec = tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)),
        exit = fadeOut(animationSpec = tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothInOutEasing)) +
            scaleOut(targetScale = 0.96f, animationSpec = tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothInOutEasing))
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
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(PomoMotion.DURATION_MEDIUM, easing = PomoMotion.SmoothOutEasing)
            ) + fadeIn(tween(PomoMotion.DURATION_STANDARD)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothInOutEasing)
            ) + fadeOut(tween(PomoMotion.DURATION_FAST)),
            modifier = Modifier.fillMaxSize().background(currentTheme.background)
        ) {
            SettingsOverlay(onDismiss = { viewModel.setSettingsOpen(false) }, initialTab = settingsInitialTab)
        }
        } // closes ProvideAdaptiveDimensions
    } // closes the Box Wrapping the Scaffold

    if (showJustInTimeSetup) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showJustInTimeSetup = false }) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(currentTheme.surface)
                    .padding(24.dp)
            ) {
                Column {
                    com.example.ui.ContextualSetupMessage(
                        state = protectionStates,
                        onSetup = { actionId ->
                            try {
                                when (actionId) {
                                    "NOTIFICATIONS" -> {
                                        val intent = android.content.Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                            putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                                        }
                                        context.startActivity(intent)
                                    }
                                    "DND" -> {
                                        val intent = android.content.Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                                        context.startActivity(intent)
                                    }
                                    "BATTERY" -> {
                                        val intent = android.content.Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                                        context.startActivity(intent)
                                    }
                                }
                            } catch (e: Exception) {
                                viewModel.setSettingsOpen(true, 3)
                            }
                            showJustInTimeSetup = false
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showJustInTimeSetup = false
                            // Allow them to continue anyway if they really want to ignore it
                            try {
                                val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                                androidx.core.content.ContextCompat.startForegroundService(context, intent)
                            } catch (_: Exception) {}
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continue without protection", color = currentTheme.textSecondary)
                    }
                }
            }
        }
    }

    if (isAddingTask) {
        AddTaskCard(
            onSave = { name, cat, color ->
                viewModel.saveTask(name, cat, color) { newId ->
                    viewModel.setTask(newId, name, color)
                }
                viewModel.setAddingTask(false)
            },
            onCancel = { viewModel.setAddingTask(false) }
        )
    }

    if (showAllTasksSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAllTasksSheet = false },
            containerColor = currentTheme.background,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "All Tasks",
                    fontSize = 24.scaledSp,
                    fontFamily = AppFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(allTasks, key = { it.id }) { task ->
                        TaskItemRow(
                            task = task,
                            isSelected = currentTaskId == task.id,
                            onSelect = { 
                                viewModel.setTask(task.id, task.name, task.categoryColor)
                                showAllTasksSheet = false
                            },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsOverlay(onDismiss: () -> Unit, initialTab: Int = 0) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    val tabs = remember { listOf("Timer & Sound", "Theme & Font", "App Blocker", "System", "About") }

    var selectedTabIndex by androidx.compose.runtime.saveable.rememberSaveable { 
        mutableIntStateOf(initialTab.coerceIn(0, tabs.size - 1)) 
    }
    val tabBackStack = remember { mutableStateListOf<Int>() }
    var showBlockedApps by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(initialTab) {
        if (initialTab in tabs.indices && initialTab != selectedTabIndex) {
            selectedTabIndex = initialTab
        }
    }

    var localFocus by remember { mutableStateOf(SettingsManager.getFocusTimeMins().toFloat()) }
    var localBreak by remember { mutableStateOf(SettingsManager.getBreakTimeMins().toFloat()) }
    var localCompletion by remember { mutableStateOf(SettingsManager.getCompletionSound()) }
    var localCompletionDuration by remember { mutableStateOf(SettingsManager.getCompletionDurationSec().toFloat()) }
    
    val audioManager = context.getSystemService(android.content.Context.AUDIO_SERVICE) as android.media.AudioManager
    val maxVolume = remember {
        try {
            audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC).toFloat().coerceAtLeast(1f)
        } catch (_: Exception) {
            15f
        }
    }
    var localVolume by remember {
        mutableStateOf(
            try {
                audioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC).toFloat().coerceIn(0f, maxVolume)
            } catch (_: Exception) {
                maxVolume * 0.5f
            }
        )
    }
    
    LaunchedEffect(localFocus, localBreak, localCompletion, localCompletionDuration) {
        SettingsManager.setFocusTimeMins(localFocus.toInt())
        SettingsManager.setBreakTimeMins(localBreak.toInt())
        SettingsManager.setCompletionSound(localCompletion)
        SettingsManager.setCompletionDurationSec(localCompletionDuration.toInt())
    }

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
        if (showBlockedApps) {
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
            showBlockedApps = false
        } else if (tabBackStack.isNotEmpty()) {
            val prevTab = tabBackStack.removeAt(tabBackStack.lastIndex)
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
            selectedTabIndex = prevTab
        } else {
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
            com.example.service.SoundPlayer.stop()
            onDismiss()
        }
    }

    AnimatedContent(
        targetState = showBlockedApps,
        transitionSpec = { PomoMotion.horizontalSlideTransition(forward = targetState) },
        label = "BlockedAppsTransition"
    ) { isBlockedAppsVisible ->
        if (isBlockedAppsVisible) {
            com.example.ui.BlockedAppsScreen(onBack = { showBlockedApps = false })
        } else {
            val tabListState = androidx.compose.foundation.lazy.rememberLazyListState(
                initialFirstVisibleItemIndex = selectedTabIndex.coerceIn(0, tabs.size - 1)
            )

            LaunchedEffect(selectedTabIndex) {
                kotlinx.coroutines.delay(60)
                try {
                    tabListState.animateScrollToItem(selectedTabIndex.coerceIn(0, tabs.size - 1))
                } catch (_: Exception) {}
            }

            val currentTheme = LocalAppTheme.current
            Scaffold(
                modifier = Modifier.fillMaxSize().background(currentTheme.background),
                containerColor = currentTheme.background,
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
                            color = currentTheme.textPrimary
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
                                contentDescription = "Close",
                                tint = currentTheme.textPrimary
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    val currentTheme = LocalAppTheme.current
                    androidx.compose.foundation.lazy.LazyRow(
                        state = tabListState,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        items(tabs.size) { index ->
                            val isSelected = selectedTabIndex == index
                            val onPrimaryColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                            val tabBgColor by androidx.compose.animation.animateColorAsState(
                                targetValue = if (isSelected) currentTheme.primary else currentTheme.surface, 
                                animationSpec = PomoMotion.colorSpec, label = "tabBg"
                            )
                            val tabTextColor by androidx.compose.animation.animateColorAsState(
                                targetValue = if (isSelected) onPrimaryColor else currentTheme.textPrimary, 
                                animationSpec = PomoMotion.colorSpec, label = "tabText"
                            )
                            val tabElevation by androidx.compose.animation.core.animateDpAsState(
                                targetValue = if (isSelected) 4.dp else 1.dp, 
                                animationSpec = PomoMotion.elevationSpec, label = "tabElev"
                            )
                            
                            Box(
                                modifier = Modifier
                                    .shadow(
                                        elevation = tabElevation.coerceAtLeast(0.dp),
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
                                        if (selectedTabIndex != index) {
                                            tabBackStack.add(selectedTabIndex)
                                            selectedTabIndex = index 
                                        }
                                    }
                            ) {
                                Text(
                                    text = tabs[index],
                                    color = tabTextColor,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.scaledSp
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        androidx.compose.animation.AnimatedContent(
                            targetState = selectedTabIndex,
                            transitionSpec = {
                                PomoMotion.horizontalSlideTransition(forward = targetState > initialState)
                            },
                            label = "SettingsTabTransition"
                        ) { tabIndex ->
                            if (tabIndex == 4) {
                                com.example.ui.components.InteractiveAboutScreen()
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 24.dp)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    if (tabIndex == 0) {
                                        var focusInput by remember(localFocus) { mutableStateOf(localFocus.toInt().toString()) }
                                        var breakInput by remember(localBreak) { mutableStateOf(localBreak.toInt().toString()) }

                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder)
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text("Timer & Sound", fontSize = 16.scaledSp, fontWeight = FontWeight.Bold, color = currentTheme.textPrimary)
                                                Spacer(modifier = Modifier.height(16.dp))
                                                
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                                    Text("Focus Time (min):", color = currentTheme.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.scaledSp)
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
                                                        textStyle = androidx.compose.ui.text.TextStyle(color = currentTheme.textPrimary, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.End),
                                                        modifier = Modifier.width(60.dp).background(currentTheme.textPrimary.copy(alpha=0.05f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
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
                                                    colors = SliderDefaults.colors(
                                                        thumbColor = currentTheme.primary,
                                                        activeTrackColor = currentTheme.primary.copy(alpha = 0.7f)
                                                    )
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                                    Text("Break Time (min):", color = currentTheme.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.scaledSp)
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
                                                        textStyle = androidx.compose.ui.text.TextStyle(color = currentTheme.textPrimary, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.End),
                                                        modifier = Modifier.width(60.dp).background(currentTheme.textPrimary.copy(alpha=0.05f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
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
                                                    colors = SliderDefaults.colors(
                                                        thumbColor = currentTheme.secondary,
                                                        activeTrackColor = currentTheme.secondary.copy(alpha = 0.7f)
                                                    )
                                                )
                                                
                                                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = currentTheme.textPrimary.copy(alpha = 0.1f))
                                                
                                                Text("Volume", color = currentTheme.textPrimary, fontWeight = FontWeight.Bold, fontSize = 14.scaledSp)
                                                Slider(
                                                    value = localVolume.coerceIn(0f, maxVolume),
                                                    onValueChange = { 
                                                        val oldVal = localVolume.toInt()
                                                        val newVal = it.toInt()
                                                        if (oldVal != newVal) {
                                                            try {
                                                                view.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
                                                            } catch (e: Exception) {}
                                                        }
                                                        localVolume = it
                                                        try {
                                                            audioManager.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, it.toInt(), 0)
                                                        } catch (_: Exception) {}
                                                    },
                                                    valueRange = 0f..maxVolume,
                                                    steps = (maxVolume.toInt() - 1).takeIf { it > 0 } ?: 0,
                                                    colors = SliderDefaults.colors(
                                                        thumbColor = currentTheme.accent,
                                                        activeTrackColor = currentTheme.accent.copy(alpha = 0.7f)
                                                    )
                                                )
                                                Spacer(modifier = Modifier.height(16.dp))
                                                
                                                Text("Completion Sound", color = currentTheme.textPrimary, fontSize = 14.scaledSp, fontWeight = FontWeight.Bold)
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(currentTheme.textPrimary.copy(alpha=0.05f))) {
                                                    val compOptions = listOf("Beep", "Alarm", "Ring", "Custom")
                                                    val onSelectedCompColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                                                    compOptions.forEachIndexed { index, name ->
                                                        val isSelectedComp = localCompletion == index
                                                        val compBg by animateColorAsState(
                                                            targetValue = if (isSelectedComp) currentTheme.primary else Color.Transparent,
                                                            animationSpec = PomoMotion.colorSpec,
                                                            label = "compBg_$index"
                                                        )
                                                        Box(
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .clip(RoundedCornerShape(12.dp))
                                                                .background(compBg)
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
                                                        ) {
                                                            Text(
                                                                name,
                                                                color = if (isSelectedComp) onSelectedCompColor else currentTheme.textPrimary,
                                                                fontSize = 11.scaledSp,
                                                                fontWeight = if (isSelectedComp) FontWeight.Bold else FontWeight.Medium,
                                                                maxLines = 1
                                                            )
                                                        }
                                                    }
                                                }
                                                
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Text("Completion Sound Duration: ${localCompletionDuration.toInt()} sec", color = currentTheme.textPrimary, fontSize = 14.scaledSp, fontWeight = FontWeight.Bold)
                                                Slider(
                                                    value = localCompletionDuration.coerceIn(2f, 20f),
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
                                                    colors = SliderDefaults.colors(
                                                        thumbColor = currentTheme.secondary,
                                                        activeTrackColor = currentTheme.secondary.copy(alpha = 0.7f)
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    
                                    if (tabIndex == 1) {
                                        com.example.ui.components.ThemeFontCustomizer()
                                    }
                                    
                                    if (tabIndex == 2) {
                                        val blockedInteraction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                                        val isBlockedPressed by blockedInteraction.collectIsPressedAsState()
                                        val blockedScale by androidx.compose.animation.core.animateFloatAsState(
                                            targetValue = if (isBlockedPressed) 0.96f else 1f,
                                            animationSpec = PomoMotion.pressScaleSpec,
                                            label = "blockedScale"
                                        )

                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
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
                                Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFFF57C00), modifier = Modifier.size(20.dp))
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
                }

                Card(
                    onClick = { 
                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        showBlockedApps = true 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(blockedScale),
                    shape = RoundedCornerShape(16.dp),
                    interactionSource = blockedInteraction,
                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp, pressedElevation = 0.dp)
                ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("App Blocker", fontWeight = FontWeight.Bold, color = currentTheme.textPrimary)
                        Text("Select distracting apps to block during focus sessions.", fontSize = 12.scaledSp, color = currentTheme.textSecondary)
                    }
                    Icon(androidx.compose.material.icons.Icons.Filled.Settings, contentDescription = "Manage", tint = currentTheme.textPrimary)
                }
            }
            } // Close if (tabIndex == 2)

            
            if (tabIndex == 3) {
                com.example.ui.SystemProtectionScreen()
            } // Close if (selectedTabIndex == 3)            
            Spacer(modifier = Modifier.height(100.dp))
                        } // closes Column
                    } // closes else
                } // closes AnimatedContent
            }
    }
}
        }
    }
}

@Composable
fun TaskItemRow(task: com.example.data.TaskItem, isSelected: Boolean = false, onSelect: (com.example.data.TaskItem) -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    
    val taskColor = com.example.service.safeParseColor(task.categoryColor, currentTheme.primary)
    val borderColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) taskColor else currentTheme.cardBorder,
        animationSpec = PomoMotion.colorSpec,
        label = "borderColor"
    )
    val borderWidth by androidx.compose.animation.core.animateDpAsState(
        targetValue = if (isSelected) 1.5.dp else 1.dp,
        animationSpec = PomoMotion.dpSpec,
        label = "borderWidth"
    )
    
    val containerColor = if (isSelected) {
        taskColor.copy(alpha = 0.06f).compositeOver(currentTheme.surface)
    } else {
        currentTheme.surface
    }
    
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(24.dp), spotColor = currentTheme.shadowColor)
            .border(borderWidth, borderColor, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                try {
                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {}
                onSelect(task)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.scaledDp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.scaledDp)
                    .clip(CircleShape)
                    .background(taskColor)
            )
            Spacer(modifier = Modifier.width(12.scaledDp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name, 
                    fontWeight = FontWeight.Bold, 
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary,
                    fontSize = 15.scaledSp
                )
                Text(
                    text = task.categoryName, 
                    fontSize = 12.scaledSp, 
                    color = currentTheme.textSecondary,
                    fontFamily = currentFont
                )
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
                        Text(text = "x${task.completedPomodoros}", fontWeight = FontWeight.Bold, color = currentTheme.textPrimary, fontFamily = currentFont)
                    }
                }
                Spacer(modifier = Modifier.width(16.scaledDp))
            }
            
            androidx.compose.animation.AnimatedVisibility(
                visible = isSelected,
                enter = androidx.compose.animation.scaleIn(PomoMotion.floatSpec) + androidx.compose.animation.fadeIn(tween(180)),
                exit = androidx.compose.animation.scaleOut(PomoMotion.floatSpec) + androidx.compose.animation.fadeOut(tween(150))
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = taskColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            if (!isSelected) {
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
}

@Composable
fun PremiumJumpingTextPreview(text: String) {
    val currentTheme = com.example.ui.theme.LocalAppTheme.current
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
                color = currentTheme.textSecondary.copy(alpha = 0.5f),
                fontFamily = AppFontFamily,
                fontWeight = FontWeight.Medium
            )
        } else {
            text.forEachIndexed { index, char ->
                val animatedOffset = remember(index) { androidx.compose.animation.core.Animatable(-16f) }
                val animatedScale = remember(index) { androidx.compose.animation.core.Animatable(0.75f) }
                val animatedAlpha = remember(index) { androidx.compose.animation.core.Animatable(0f) }
                
                LaunchedEffect(key1 = index) {
                    animatedOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = PomoMotion.floatSpec
                    )
                }
                LaunchedEffect(key1 = index) {
                    animatedScale.animateTo(
                        targetValue = 1f,
                        animationSpec = PomoMotion.floatSpec
                    )
                }
                LaunchedEffect(key1 = index) {
                    animatedAlpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 200)
                    )
                }
                
                Text(
                    text = if (char == ' ') " " else char.toString(),
                    fontSize = 24.scaledSp,
                    fontFamily = AppFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary,
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
    val keyboardController = androidx.compose.ui.platform.LocalSoftwareKeyboardController.current
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onCancel,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )
    ) {
        var animateIn by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            animateIn = true
        }
        val scale by animateFloatAsState(
            targetValue = if (animateIn) 1f else 0.92f,
            animationSpec = PomoMotion.floatSpec,
            label = "dialogScale"
        )
        val alpha by animateFloatAsState(
            targetValue = if (animateIn) 1f else 0f,
            animationSpec = tween(durationMillis = PomoMotion.DURATION_STANDARD, easing = PomoMotion.SmoothOutEasing),
            label = "dialogAlpha"
        )

        val view = androidx.compose.ui.platform.LocalView.current
        val currentTheme = LocalAppTheme.current
        val currentFont = LocalAppFont.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f * alpha))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(28.dp), spotColor = currentTheme.shadowColor)
                    .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(28.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                            detectTapGestures(onTap = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            })
                        }
                        .padding(24.dp)
                ) {
                    Text(
                        text = "New Task",
                        fontFamily = CursiveFontFamily,
                        fontSize = 28.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.textPrimary
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
                            focusedTextColor = currentTheme.textPrimary,
                            unfocusedTextColor = currentTheme.textPrimary,
                            cursorColor = currentTheme.primary,
                            focusedBorderColor = currentTheme.primary,
                            unfocusedBorderColor = currentTheme.cardBorder,
                            focusedLabelColor = currentTheme.primary,
                            unfocusedLabelColor = currentTheme.textSecondary
                        ),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = androidx.compose.ui.text.input.ImeAction.Done
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onSave(title.ifEmpty { "Focus Task" }, selectedCategory.first, (selectedCategory.second.toArgb().toLong() and 0xFFFFFFFFL))
                            }
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Category", fontSize = 14.scaledSp, color = currentTheme.textSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { cat ->
                            val isCatSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isCatSelected) cat.second else cat.second.copy(alpha = 0.3f))
                                    .border(if (isCatSelected) 2.dp else 1.dp, if (isCatSelected) currentTheme.textPrimary else currentTheme.cardBorder, CircleShape)
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
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onCancel()
                            }
                        ) {
                            Text("Cancel", color = currentTheme.textSecondary, fontWeight = FontWeight.Medium, fontFamily = currentFont)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        com.example.ui.components.PomoButton(
                            text = "Save Task",
                            onClick = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onSave(title.ifEmpty { "Focus Task" }, selectedCategory.first, (selectedCategory.second.toArgb().toLong() and 0xFFFFFFFFL))
                            },
                            containerColor = currentTheme.primary,
                            contentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                            shape = RoundedCornerShape(16.dp),
                            elevation = 4.dp
                        )
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
    shape: androidx.compose.ui.graphics.Shape = CircleShape,
    elevation: androidx.compose.ui.unit.Dp = 6.dp,
    shadowColor: androidx.compose.ui.graphics.Color = Color.Black.copy(alpha = 0.2f),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.92f else 1f,
        animationSpec = PomoMotion.pressScaleSpec,
        label = "scale"
    )
    val currentElevation by animateDpAsState(
        targetValue = if (isPressed && enabled) 1.5.dp else if (enabled) elevation else 0.dp,
        animationSpec = PomoMotion.elevationSpec,
        label = "elevation"
    )
    val view = androidx.compose.ui.platform.LocalView.current
    
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                alpha = if (enabled) 1f else 0.5f
            }
            .shadow(
                elevation = currentElevation.coerceAtLeast(0.dp),
                shape = shape,
                clip = false,
                spotColor = shadowColor,
                ambientColor = shadowColor.copy(alpha = shadowColor.alpha * 0.4f)
            )
            .clip(shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(bounded = true),
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
fun AppControls(state: TimerManager.TimerState, context: android.content.Context, onStartAttempt: () -> Unit = {}) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val currentTheme = LocalAppTheme.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedScaleBox(
            enabled = state != TimerManager.TimerState.STOPPED,
            shape = CircleShape,
            elevation = 5.dp,
            shadowColor = Color.Black.copy(alpha = 0.15f),
            onClick = {
                try {
                    val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_STOP }
                    context.startService(intent)
                } catch (_: Exception) {}
            }
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(currentTheme.surface)
                    .border(1.5.dp, currentTheme.cardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Stop, contentDescription = "Stop", tint = currentTheme.textPrimary, modifier = Modifier.size(28.dp))
            }
        }

        Spacer(modifier = Modifier.width(26.dp))

        val isRunning = state == TimerManager.TimerState.RUNNING
        AnimatedScaleBox(
            shape = CircleShape,
            elevation = 8.dp,
            shadowColor = currentTheme.shadowColor,
            onClick = {
                if (isRunning) {
                    try {
                        val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_PAUSE }
                        context.startService(intent)
                    } catch (_: Exception) {}
                } else {
                    onStartAttempt()
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(currentTheme.primaryLight, currentTheme.primary)
                        )
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isRunning,
                    transitionSpec = {
                        (fadeIn(tween(180)) + scaleIn(initialScale = 0.85f, animationSpec = tween(180, easing = PomoMotion.SmoothOutEasing))) togetherWith
                            (fadeOut(tween(150)) + scaleOut(targetScale = 0.85f, animationSpec = tween(150)))
                    },
                    label = "playPause"
                ) { running ->
                    Icon(
                        if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow, 
                        contentDescription = if (running) "Pause" else "Start", 
                        tint = Color.White, 
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(26.dp))

        val isBreakMode by TimerManager.isBreakMode.collectAsState()
        AnimatedScaleBox(
            enabled = !isRunning,
            shape = CircleShape,
            elevation = 5.dp,
            shadowColor = Color.Black.copy(alpha = 0.15f),
            onClick = {
                val isServiceActive = state != TimerManager.TimerState.STOPPED
                if (isServiceActive) {
                    try {
                        val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_TOGGLE_MODE }
                        context.startService(intent)
                    } catch (_: Exception) {}
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
                    .background(currentTheme.surface)
                    .border(1.5.dp, currentTheme.cardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = "Switch Mode",
                    tint = currentTheme.textPrimary,
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
                    fontFamily = LocalAppFont.current,
                    color = LocalAppTheme.current.textPrimary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            } else {
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        val enterSpec = tween<androidx.compose.ui.unit.IntOffset>(220, easing = PomoMotion.SmoothOutEasing)
                        val exitSpec = tween<androidx.compose.ui.unit.IntOffset>(180, easing = PomoMotion.SmoothInOutEasing)
                        val alphaEnter = tween<Float>(200, easing = PomoMotion.SmoothOutEasing)
                        val alphaExit = tween<Float>(160, easing = PomoMotion.SmoothInOutEasing)
                        
                        if (targetState < initialState || (initialState == '0' && targetState == '9') || (initialState == '0' && targetState == '5')) {
                            // Counting down
                            (slideInVertically(enterSpec) { height -> -height / 2 } + fadeIn(alphaEnter)) togetherWith
                                    (slideOutVertically(exitSpec) { height -> height / 2 } + fadeOut(alphaExit))
                        } else {
                            // Counting up or reset
                            (slideInVertically(enterSpec) { height -> height / 2 } + fadeIn(alphaEnter)) togetherWith
                                    (slideOutVertically(exitSpec) { height -> -height / 2 } + fadeOut(alphaExit))
                        }.using(SizeTransform(clip = false))
                    },
                    label = "digitAnimation_$index"
                ) { targetDigit ->
                    Text(
                        text = targetDigit.toString(),
                        fontSize = fontSize,
                        fontWeight = FontWeight.Black,
                        fontFamily = LocalAppFont.current,
                        color = LocalAppTheme.current.textPrimary
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
    currentTask: com.example.data.TaskItem? = null,
    context: android.content.Context,
    circleSize: androidx.compose.ui.unit.Dp = 260.dp
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    val currentTheme = LocalAppTheme.current
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
                .shadow(10.dp, CircleShape, spotColor = currentTheme.shadowColor)
                .clip(CircleShape)
                .background(
                    brush = remember(currentTheme) {
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(currentTheme.surface.copy(alpha = 0.95f), currentTheme.surface.copy(alpha = 0.7f))
                        )
                    }
                )
                .border(4.dp, currentTheme.cardBorder, CircleShape)
        ) {
            val dynamicCircleSize = maxWidth
            Canvas(modifier = Modifier
                .fillMaxSize()
                .padding(if (dynamicCircleSize < 200.dp) 8.dp else 16.dp)) {
                drawArc(
                    color = currentTheme.cardBorder.copy(alpha = 0.5f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = if (dynamicCircleSize < 200.dp) 4.dp.toPx() else 8.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = if (isBreakMode) currentTheme.secondary else currentTheme.primary,
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
                val stateBg = if (isBreakMode) currentTheme.secondary.copy(alpha = 0.18f) else currentTheme.primary.copy(alpha = 0.18f)
                val stateTextCol = if (isBreakMode) currentTheme.secondary else currentTheme.primary
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
                                try {
                                    val intent = Intent(context, TimerService::class.java).apply { action = TimerService.ACTION_TOGGLE_MODE }
                                    context.startService(intent)
                                } catch (_: Exception) {}
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
                        fontFamily = LocalAppFont.current,
                        color = stateTextCol,
                        letterSpacing = 1.scaledSp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(if (circleSize < 200.dp) 16.dp else if (circleSize < 240.dp) 24.dp else 40.dp))
        
        if (currentTask != null) {
            val activeTaskColor = com.example.service.safeParseColor(currentTask.categoryColor, currentTheme.primary)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface.copy(alpha = 0.5f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp))
                    .clickable { 
                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch(e: Exception){}
                        TimerManager.setTask(-1, "Focus Time!")
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(activeTaskColor)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = currentTask.name,
                            fontSize = 15.scaledSp,
                            color = currentTheme.textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = LocalAppFont.current
                        )
                        Text(
                            text = currentTask.categoryName,
                            fontSize = 12.scaledSp,
                            color = currentTheme.textSecondary,
                            fontFamily = LocalAppFont.current
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Deselect Task",
                        tint = currentTheme.textSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(currentTheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBreakMode) "Break Time!" else "Focus Time!",
                    fontSize = 16.scaledSp,
                    color = currentTheme.textSecondary,
                    fontFamily = LocalAppFont.current,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Choose a task",
                    fontSize = 24.scaledSp,
                    fontFamily = LocalAppFont.current,
                    color = currentTheme.textPrimary,
                    fontWeight = FontWeight.Bold
                )
                val cursorAlpha = rememberInfiniteTransition(label = "cursor").animateFloat(
                    initialValue = 1f, targetValue = 0f, 
                    animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
                    label = "cursorAlpha"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(28.dp)
                        .clip(CircleShape)
                        .background(currentTheme.primary.copy(alpha = cursorAlpha.value))
                )
            }
        }
    }
}

@Composable
fun AnimatedQuoteCard(quote: String, isBreak: Boolean) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val quoteColor = if (isBreak) currentTheme.secondary else currentTheme.textPrimary
    val accentColor = if (isBreak) currentTheme.secondary else currentTheme.primary
    
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
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 24.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = currentTheme.shadowColor
            )
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
                fontFamily = currentFont,
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
                            fontFamily = currentFont,
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
                fontFamily = currentFont,
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
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? android.app.Activity
    val view = androidx.compose.ui.platform.LocalView.current
    
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
                            onClick = {
                                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                onDismiss()
                            },
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
