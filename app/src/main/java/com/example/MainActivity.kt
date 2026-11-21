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
import androidx.activity.BackEventCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.PredictiveBackHandler
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import com.example.ui.components.AddTaskCard
import com.example.ui.components.LocalMascotRenderMode
import com.example.ui.components.MascotRenderMode
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
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
    private var isAppReady by mutableStateOf(false)
    private var isSplashExiting by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        SettingsManager.init(applicationContext)

        requestPermissions()

        database = DatabaseProvider.getDatabase(applicationContext)

        splashScreen.setKeepOnScreenCondition { !isAppReady }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            isSplashExiting = true
            var splashRemoved = false
            fun removeSplashSafely() {
                if (!splashRemoved) {
                    splashRemoved = true
                    runCatching { splashScreenView.remove() }
                }
            }

            // Critically damped Effects spring (dampingRatio = 1.0f) via AndroidX DynamicAnimation
            val viewAlphaAnim = SpringAnimation(splashScreenView.view, DynamicAnimation.ALPHA, 0f).apply {
                setStartValue(1f)
                spring = SpringForce(0f).apply {
                    dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    stiffness = 1200f
                }
                addEndListener { _, _, _, _ ->
                    removeSplashSafely()
                }
            }

            // OEM-safe null guard for iconView (prevents NPE on warm launch / notification launch)
            val iconView = runCatching { splashScreenView.iconView }.getOrNull()
            if (iconView != null) {
                SpringAnimation(iconView, DynamicAnimation.SCALE_X, 1.08f).apply {
                    setStartValue(1f)
                    spring = SpringForce(1.08f).apply {
                        dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                        stiffness = 1200f
                    }
                }.start()

                SpringAnimation(iconView, DynamicAnimation.SCALE_Y, 1.08f).apply {
                    setStartValue(1f)
                    spring = SpringForce(1.08f).apply {
                        dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                        stiffness = 1200f
                    }
                }.start()

                SpringAnimation(iconView, DynamicAnimation.ALPHA, 0f).apply {
                    setStartValue(1f)
                    spring = SpringForce(0f).apply {
                        dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                        stiffness = 1200f
                    }
                }.start()
            }

            viewAlphaAnim.start()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withTimeoutOrNull(120L) {
                    database.taskDao().getAllTasks().first()
                }
            } catch (_: Throwable) {}
            withContext(Dispatchers.Main) {
                isAppReady = true
            }
        }

        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            val currentTheme by SettingsManager.themeState.collectAsState()
            val currentFont by SettingsManager.fontState.collectAsState()

            LaunchedEffect(isAppReady) {
                if (isAppReady && !isSplashExiting) {
                    delay(80L)
                    isSplashExiting = true
                }
            }

            val entryAlpha by animateFloatAsState(
                targetValue = if (isSplashExiting) 1f else 0f,
                animationSpec = PomoMotion.defaultEffects(),
                label = "coldStartAlpha"
            )
            val entryScale by animateFloatAsState(
                targetValue = if (isSplashExiting) 1f else 0.96f,
                animationSpec = PomoMotion.defaultEffects(),
                label = "coldStartScale"
            )

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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = entryAlpha
                                scaleX = entryScale
                                scaleY = entryScale
                                compositingStrategy = if (entryAlpha < 0.999f) {
                                    CompositingStrategy.Offscreen
                                } else {
                                    CompositingStrategy.Auto
                                }
                            }
                    ) {
                        PomoPalApp(viewModel)
                    }
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
    var isTasksNavCollapsed by remember { mutableStateOf(false) }
    var tasksScrollToTopTrigger by remember { mutableIntStateOf(0) }
    var isHistoryNavCollapsed by remember { mutableStateOf(false) }
    var historyScrollToTopTrigger by remember { mutableIntStateOf(0) }
    val view = androidx.compose.ui.platform.LocalView.current

    LaunchedEffect(currentRoute) {
        if (currentRoute != "tasks") {
            isTasksNavCollapsed = false
        }
        if (currentRoute != "history") {
            isHistoryNavCollapsed = false
        }
    }

    val currentTheme = com.example.ui.theme.LocalAppTheme.current
    LaunchedEffect(Unit) {
        val url = UpdateChecker.checkForUpdates()
        if (url != null) {
            updateUrl = url
        }
    }

    if (updateUrl != null) {
        var updateAnimateIn by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { updateAnimateIn = true }
        val updateScale by animateFloatAsState(
            targetValue = if (updateAnimateIn) 1f else 0.88f,
            animationSpec = PomoMotion.expressiveSpatial(),
            label = "updateDialogScale"
        )
        val updateAlpha by animateFloatAsState(
            targetValue = if (updateAnimateIn) 1f else 0f,
            animationSpec = PomoMotion.defaultEffects(),
            label = "updateDialogAlpha"
        )

        AlertDialog(
            onDismissRequest = { updateUrl = null },
            modifier = Modifier.graphicsLayer {
                scaleX = updateScale
                scaleY = updateScale
                alpha = updateAlpha
                compositingStrategy = if (updateAlpha < 0.999f) {
                    CompositingStrategy.Offscreen
                } else {
                    CompositingStrategy.Auto
                }
            },
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
                    animationSpec = PomoMotion.intOffsetSpatial
                ) + fadeIn(animationSpec = PomoMotion.defaultEffects()) +
                    scaleIn(initialScale = 0.92f, animationSpec = PomoMotion.fastSpatial()),
                exit = slideOutVertically(
                    targetOffsetY = { it * 2 },
                    animationSpec = PomoMotion.intOffsetFastSpatial
                ) + fadeOut(animationSpec = PomoMotion.fastEffects()) +
                    scaleOut(targetScale = 0.92f, animationSpec = PomoMotion.fastSpatial()),
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = 16.dp, top = 8.dp)
            ) {
                val currentTheme = com.example.ui.theme.LocalAppTheme.current
                val currentFont = com.example.ui.theme.LocalAppFont.current
                val collapsedInTasks = isTasksNavCollapsed && currentRoute == "tasks"
                val collapsedInHistory = isHistoryNavCollapsed && currentRoute == "history"
                val isAnyNavCollapsed = collapsedInTasks || collapsedInHistory
                val navHorizontalBias by animateFloatAsState(
                    targetValue = if (isAnyNavCollapsed) -1f else 0f,
                    animationSpec = PomoMotion.smoothSpring(stiffness = 560f),
                    label = "navHorizontalBias"
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = BiasAlignment(
                        horizontalBias = navHorizontalBias.coerceIn(-1f, 1f),
                        verticalBias = 0f
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = currentTheme.shadowColor)
                            .clip(RoundedCornerShape(32.dp))
                            .background(currentTheme.surface)
                            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(32.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1. Timer Tab (collapses when docked on either Tasks or History)
                        AnimatedVisibility(
                            visible = !isAnyNavCollapsed,
                            enter = expandHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                expandFrom = Alignment.Start
                            ) + fadeIn(animationSpec = PomoMotion.defaultEffects()),
                            exit = shrinkHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                shrinkTowards = Alignment.Start
                            ) + fadeOut(animationSpec = PomoMotion.fastEffects())
                        ) {
                            Row(
                                modifier = Modifier.padding(end = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val isHome = currentRoute == "home"
                                val homeInteraction = remember { MutableInteractionSource() }
                                val isHomePressed by homeInteraction.collectIsPressedAsState()
                                val homeScale by animateFloatAsState(
                                    targetValue = if (isHomePressed) 0.93f else 1f,
                                    animationSpec = PomoMotion.pressScaleSpec,
                                    label = "homePillScale"
                                )
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
                                        .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                                        .graphicsLayer {
                                            scaleX = homeScale
                                            scaleY = homeScale
                                        }
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(homeBg)
                                        .clickable(interactionSource = homeInteraction, indication = null) {
                                            try {
                                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                            } catch (e: Exception) {}
                                            if (!isHome) {
                                                navController.navigate("home") {
                                                    popUpTo(0)
                                                    launchSingleTop = true
                                                }
                                            }
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                        .testTag("nav_tab_home"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Timer",
                                        tint = homeTint,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    AnimatedVisibility(
                                        visible = isHome,
                                        enter = expandHorizontally(animationSpec = PomoMotion.intSizeSpatial) +
                                            fadeIn(animationSpec = PomoMotion.defaultEffects()),
                                        exit = shrinkHorizontally(animationSpec = PomoMotion.intSizeSpatial) +
                                            fadeOut(animationSpec = PomoMotion.fastEffects())
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Spacer(modifier = Modifier.width(6.dp))
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
                            }
                        }

                        // 2. Tasks Tab (remains docked on Tasks collapse; hides on History collapse)
                        AnimatedVisibility(
                            visible = !collapsedInHistory,
                            enter = expandHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                expandFrom = Alignment.Start
                            ) + fadeIn(animationSpec = PomoMotion.defaultEffects()),
                            exit = shrinkHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                shrinkTowards = Alignment.Start
                            ) + fadeOut(animationSpec = PomoMotion.fastEffects())
                        ) {
                            val isTasks = currentRoute == "tasks"
                            val tasksInteraction = remember { MutableInteractionSource() }
                            val isTasksPressed by tasksInteraction.collectIsPressedAsState()
                            val tasksScale by animateFloatAsState(
                                targetValue = if (isTasksPressed) 0.93f else 1f,
                                animationSpec = PomoMotion.pressScaleSpec,
                                label = "tasksPillScale"
                            )
                            val tasksBg by animateColorAsState(
                                targetValue = if (isTasks) currentTheme.pillActiveBg else Color.Transparent,
                                animationSpec = PomoMotion.colorSpec,
                                label = "tasksPillBg"
                            )
                            val tasksTint by animateColorAsState(
                                targetValue = if (isTasks) currentTheme.primary else currentTheme.textSecondary.copy(alpha = 0.55f),
                                animationSpec = PomoMotion.colorSpec,
                                label = "tasksPillTint"
                            )

                            Row(
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                                    .graphicsLayer {
                                        scaleX = tasksScale
                                        scaleY = tasksScale
                                    }
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(tasksBg)
                                    .clickable(interactionSource = tasksInteraction, indication = null) {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        if (!isTasks) {
                                            navController.navigate("tasks") {
                                                popUpTo(0)
                                                launchSingleTop = true
                                            }
                                        } else if (collapsedInTasks) {
                                            isTasksNavCollapsed = false
                                            tasksScrollToTopTrigger++
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("nav_tab_tasks"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.TaskAlt,
                                    contentDescription = "Tasks",
                                    tint = tasksTint,
                                    modifier = Modifier.size(22.dp)
                                )
                                AnimatedVisibility(
                                    visible = isTasks,
                                    enter = expandHorizontally(animationSpec = PomoMotion.intSizeSpatial) +
                                        fadeIn(animationSpec = PomoMotion.defaultEffects()),
                                    exit = shrinkHorizontally(animationSpec = PomoMotion.intSizeSpatial) +
                                        fadeOut(animationSpec = PomoMotion.fastEffects())
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Tasks",
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

                        // Spacer between Tasks and History only when full 3-tab pill is expanded
                        AnimatedVisibility(
                            visible = !isAnyNavCollapsed,
                            enter = expandHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                expandFrom = Alignment.Start
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                shrinkTowards = Alignment.Start
                            )
                        ) {
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        // 3. History Tab (remains docked on History collapse; hides on Tasks collapse)
                        AnimatedVisibility(
                            visible = !collapsedInTasks,
                            enter = expandHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                expandFrom = Alignment.Start
                            ) + fadeIn(animationSpec = PomoMotion.defaultEffects()),
                            exit = shrinkHorizontally(
                                animationSpec = PomoMotion.intSizeSpatial,
                                shrinkTowards = Alignment.Start
                            ) + fadeOut(animationSpec = PomoMotion.fastEffects())
                        ) {
                            val isHistory = currentRoute == "history"
                            val historyInteraction = remember { MutableInteractionSource() }
                            val isHistoryPressed by historyInteraction.collectIsPressedAsState()
                            val historyScale by animateFloatAsState(
                                targetValue = if (isHistoryPressed) 0.93f else 1f,
                                animationSpec = PomoMotion.pressScaleSpec,
                                label = "historyPillScale"
                            )
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
                                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                                    .graphicsLayer {
                                        scaleX = historyScale
                                        scaleY = historyScale
                                    }
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(historyBg)
                                    .clickable(interactionSource = historyInteraction, indication = null) {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        if (!isHistory) {
                                            navController.navigate("history") {
                                                popUpTo(0)
                                                launchSingleTop = true
                                            }
                                        } else if (collapsedInHistory) {
                                            isHistoryNavCollapsed = false
                                            historyScrollToTopTrigger++
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("nav_tab_history"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.List,
                                    contentDescription = "History",
                                    tint = historyTint,
                                    modifier = Modifier.size(22.dp)
                                )
                                AnimatedVisibility(
                                    visible = isHistory,
                                    enter = expandHorizontally(animationSpec = PomoMotion.intSizeSpatial) +
                                        fadeIn(animationSpec = PomoMotion.defaultEffects()),
                                    exit = shrinkHorizontally(animationSpec = PomoMotion.intSizeSpatial) +
                                        fadeOut(animationSpec = PomoMotion.fastEffects())
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(6.dp))
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(currentTheme.background)
        ) {
            com.example.ui.components.PaperTextureOverlay()
            NavHost(
                navController = navController,
                startDestination = startDest,
                modifier = Modifier.fillMaxSize(),
            enterTransition = {
                val fromSignIn = initialState.destination.route == "signin"
                if (fromSignIn) {
                    fadeIn(animationSpec = PomoMotion.defaultEffects()) +
                        scaleIn(initialScale = 0.94f, animationSpec = PomoMotion.defaultSpatial()) +
                        slideInHorizontally(animationSpec = PomoMotion.intOffsetSpatial) { width -> width / 6 }
                } else {
                    PomoMotion.asymmetricFadeThroughEnter()
                }
            },
            exitTransition = {
                val fromSignIn = initialState.destination.route == "signin"
                if (fromSignIn) {
                    fadeOut(animationSpec = PomoMotion.fastEffects()) +
                        slideOutHorizontally(animationSpec = PomoMotion.intOffsetFastSpatial) { width -> -width / 6 }
                } else {
                    PomoMotion.asymmetricFadeThroughExit()
                }
            },
            popEnterTransition = {
                PomoMotion.asymmetricFadeThroughEnter()
            },
            popExitTransition = {
                PomoMotion.asymmetricFadeThroughExit()
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
            composable(
                route = "home",
                enterTransition = {
                    val fromSignIn = initialState.destination.route == "signin"
                    if (fromSignIn) {
                        fadeIn(animationSpec = PomoMotion.defaultEffects()) +
                            scaleIn(initialScale = 0.94f, animationSpec = PomoMotion.defaultSpatial()) +
                            slideInHorizontally(animationSpec = PomoMotion.intOffsetSpatial) { width -> width / 8 }
                    } else {
                        fadeIn(
                            animationSpec = tween(
                                durationMillis = 220,
                                delayMillis = 30,
                                easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
                            )
                        ) + scaleIn(
                            initialScale = 0.96f,
                            animationSpec = PomoMotion.smoothSpring(stiffness = 460f)
                        ) + slideInHorizontally(
                            animationSpec = PomoMotion.smoothSpring(stiffness = 460f)
                        ) { width -> -width / 18 }
                    }
                },
                exitTransition = {
                    fadeOut(animationSpec = PomoMotion.fastEffects()) +
                        scaleOut(targetScale = 0.97f, animationSpec = PomoMotion.fastEffects())
                },
                popEnterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = 220,
                            delayMillis = 30,
                            easing = CubicBezierEasing(0.0f, 0.0f, 0.2f, 1.0f)
                        )
                    ) + scaleIn(
                        initialScale = 0.96f,
                        animationSpec = PomoMotion.smoothSpring(stiffness = 460f)
                    ) + slideInHorizontally(
                        animationSpec = PomoMotion.smoothSpring(stiffness = 460f)
                    ) { width -> -width / 18 }
                },
                popExitTransition = {
                    fadeOut(animationSpec = PomoMotion.fastEffects()) +
                        scaleOut(targetScale = 0.97f, animationSpec = PomoMotion.fastEffects())
                }
            ) {
                HomeScreen(viewModel, navController, bottomPadding)
            }
            composable(route = "tasks") {
                com.example.ui.TasksScreen(
                    viewModel = viewModel,
                    navController = navController,
                    bottomPadding = bottomPadding,
                    onBottomNavCollapseChange = { collapsed ->
                        isTasksNavCollapsed = collapsed
                    },
                    scrollToTopTrigger = tasksScrollToTopTrigger
                )
            }
            composable("history") {
                com.example.ui.HistoryScreen(
                    viewModel = viewModel,
                    navController = navController,
                    bottomPadding = bottomPadding,
                    onBottomNavCollapseChange = { collapsed ->
                        isHistoryNavCollapsed = collapsed
                    },
                    scrollToTopTrigger = historyScrollToTopTrigger
                )
            }
        }
        }
    }

    if (isAddingTask) {
        AddTaskCard(
            onSave = { name, cat, color, isBreakCategory ->
                viewModel.saveTask(name, cat, color, isBreakCategory) { newId ->
                    viewModel.setTask(newId, name, color, isBreakCategory)
                }
                viewModel.setAddingTask(false)
            },
            onCancel = { viewModel.setAddingTask(false) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(viewModel: TimerViewModel, navController: androidx.navigation.NavController, bottomPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val timerState by viewModel.timerState.collectAsState()
    val isBreakMode by TimerManager.isBreakMode.collectAsState()
    val currentQuote by viewModel.currentQuote.collectAsState()
    val timeRemaining by viewModel.timeRemainingSeconds.collectAsState()
    val currentTaskName by viewModel.currentTaskName.collectAsState()
    val currentTaskId by com.example.service.TimerManager.currentTaskId.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current
    val view = androidx.compose.ui.platform.LocalView.current

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

    // Choreographed 3-wave GPU-composited entrance (header -> centerpiece dial -> controls)
    val headerEntrance = remember { Animatable(0f) }
    val dialEntrance = remember { Animatable(0f) }
    val controlsEntrance = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        launch {
            headerEntrance.animateTo(1f, animationSpec = PomoMotion.smoothSpring(stiffness = 460f))
        }
        launch {
            delay(35)
            dialEntrance.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
        }
        launch {
            delay(85)
            controlsEntrance.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
        }
    }

    val stateColor by animateColorAsState(
        targetValue = when {
            isBreakMode -> currentTheme.breakBg
            timerState == TimerManager.TimerState.RUNNING -> currentTheme.runningBg
            else -> currentTheme.background
        },
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "bgColor"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .background(stateColor.copy(alpha = 0.85f))
    ) {
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

        SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
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
                enter = PomoMotion.expandSection,
                exit = PomoMotion.collapseSection
            ) {
                com.example.ui.SystemProtectionBanner(
                    state = protectionStates,
                    onFixClicked = {
                        showJustInTimeSetup = true
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.scaledDp)
                    .graphicsLayer {
                        val p = headerEntrance.value
                        alpha = p.coerceIn(0f, 1f)
                        translationY = -(1f - p) * 12.dp.toPx()
                        compositingStrategy = if (p < 0.999f) {
                            CompositingStrategy.Offscreen
                        } else {
                            CompositingStrategy.Auto
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(3.dp, RoundedCornerShape(16.dp), spotColor = currentTheme.shadowColor)
                            .clip(RoundedCornerShape(16.dp))
                            .background(currentTheme.surface)
                            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        com.example.ui.components.JudyAvatarBadge(
                            modifier = Modifier.size(36.dp),
                            isBreathing = true
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
                
                val settingsScale by animateFloatAsState(
                    targetValue = if (isSettingsPressed) 0.90f else 1f,
                    animationSpec = PomoMotion.pressScaleSpec,
                    label = "settingsScale"
                )

                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !showSettings,
                        enter = fadeIn(PomoMotion.fastEffects()) +
                            scaleIn(initialScale = 0.85f, animationSpec = PomoMotion.fastSpatial()),
                        exit = fadeOut(PomoMotion.fastEffects())
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "settings_overlay_bounds"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    boundsTransform = { _, _ -> PomoMotion.defaultSpatial() },
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                )
                                .graphicsLayer {
                                    scaleX = settingsScale
                                    scaleY = settingsScale
                                }
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
                }
            }
            Spacer(modifier = Modifier.height(verticalSpacing))

            if (timerState == TimerManager.TimerState.STOPPED) {
                Spacer(modifier = Modifier.weight(0.32f))
            }

            // Clean centered timer display with smooth spatial bloom entrance
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val p = dialEntrance.value
                        alpha = p.coerceIn(0f, 1f)
                        val s = 0.90f + (0.10f * p)
                        scaleX = s
                        scaleY = s
                        translationY = (1f - p) * 16.dp.toPx()
                        compositingStrategy = if (p < 0.999f) {
                            CompositingStrategy.Offscreen
                        } else {
                            CompositingStrategy.Auto
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                TimerDisplay(
                    timeRemaining = timeRemaining,
                    totalTime = if (isBreakMode) SettingsManager.getBreakTimeMins() * 60 else SettingsManager.getFocusTimeMins() * 60,
                    state = timerState,
                    isBreakMode = isBreakMode,
                    taskName = currentTaskName,
                    currentTask = allTasks.find { it.id == currentTaskId },
                    context = context,
                    circleSize = circleSize,
                    onChooseTaskClick = {
                        navController.navigate("tasks") {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(verticalSpacing))

            // Controls with smooth upward spring settle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val p = controlsEntrance.value
                        alpha = p.coerceIn(0f, 1f)
                        val s = 0.93f + (0.07f * p)
                        scaleX = s
                        scaleY = s
                        translationY = (1f - p) * 20.dp.toPx()
                        compositingStrategy = if (p < 0.999f) {
                            CompositingStrategy.Offscreen
                        } else {
                            CompositingStrategy.Auto
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                AppControls(
                    state = timerState,
                    context = context,
                    onStartAttempt = {
                        if (protectionStates.needsAttention) {
                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                            } catch (e: Exception) {}
                            showJustInTimeSetup = true
                        } else {
                            try {
                                val intent = android.content.Intent(context, com.example.service.TimerService::class.java).apply { action = com.example.service.TimerService.ACTION_START }
                                androidx.core.content.ContextCompat.startForegroundService(context, intent)
                            } catch (_: Exception) {}
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(verticalSpacing))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = timerState != TimerManager.TimerState.STOPPED,
                    enter = fadeIn(PomoMotion.defaultEffects()) +
                        slideInVertically(animationSpec = PomoMotion.intOffsetSpatial) { it / 6 },
                    exit = fadeOut(PomoMotion.fastEffects()) +
                        slideOutVertically(animationSpec = PomoMotion.intOffsetFastSpatial) { it / 8 }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                            AnimatedQuoteCard(quote = currentQuote, isBreak = isBreakMode)
                            
                            androidx.compose.animation.AnimatedVisibility(
                                visible = !showFullScreenSeriousness && !isBreakMode && timerState == TimerManager.TimerState.RUNNING,
                                enter = fadeIn(PomoMotion.defaultEffects()) +
                                    scaleIn(initialScale = 0.92f, animationSpec = PomoMotion.defaultSpatial()),
                                exit = fadeOut(PomoMotion.fastEffects())
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                            showFullScreenSeriousness = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier
                                            .height(48.dp)
                                            .sharedBounds(
                                                sharedContentState = rememberSharedContentState(key = "serious_mode_bounds"),
                                                animatedVisibilityScope = this@AnimatedVisibility,
                                                boundsTransform = { _, _ -> PomoMotion.defaultSpatial() },
                                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                            )
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
                            }
                            Spacer(modifier = Modifier.height(bottomPadding + 88.dp))
                        }
                }
            }
        }
    }
    
    AnimatedVisibility(
        visible = showFullScreenSeriousness,
        enter = fadeIn(animationSpec = PomoMotion.defaultEffects()),
        exit = fadeOut(animationSpec = PomoMotion.fastEffects()),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "serious_mode_bounds"),
                    animatedVisibilityScope = this@AnimatedVisibility,
                    boundsTransform = { _, _ -> PomoMotion.defaultSpatial() },
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(24.dp))
                )
        ) {
            SeriousFullscreenOverlay(
                quote = currentQuote,
                taskName = currentTaskName,
                timeRemaining = timeRemaining,
                onDismiss = { showFullScreenSeriousness = false }
            )
        }
    }
        AnimatedVisibility(
            visible = showSettings,
            enter = PomoMotion.asymmetricFadeThroughEnter(),
            exit = PomoMotion.asymmetricFadeThroughExit(),
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "settings_overlay_bounds"),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        boundsTransform = { _, _ -> PomoMotion.defaultSpatial() },
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        ),
                        clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(28.dp))
                    )
            ) {
                SettingsOverlay(onDismiss = { viewModel.setSettingsOpen(false) }, initialTab = settingsInitialTab)
            }
        }
        } // closes SharedTransitionLayout
        } // closes ProvideAdaptiveDimensions
    } // closes the Box Wrapping the Scaffold

    if (showJustInTimeSetup) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showJustInTimeSetup = false }) {
            var jitAnimateIn by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { jitAnimateIn = true }
            val jitScale by animateFloatAsState(
                targetValue = if (jitAnimateIn) 1f else 0.88f,
                animationSpec = PomoMotion.expressiveSpatial(),
                label = "jitDialogScale"
            )
            val jitAlpha by animateFloatAsState(
                targetValue = if (jitAnimateIn) 1f else 0f,
                animationSpec = PomoMotion.defaultEffects(),
                label = "jitDialogAlpha"
            )

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = jitScale
                        scaleY = jitScale
                        alpha = jitAlpha
                        compositingStrategy = if (jitAlpha < 0.999f) {
                            CompositingStrategy.Offscreen
                        } else {
                            CompositingStrategy.Auto
                        }
                    }
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
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SettingsOverlay(onDismiss: () -> Unit, initialTab: Int = 0) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    val tabs = remember { listOf("Timer & Sound", "Theme & Font", "App Blocker", "System", "About") }

    var selectedTabIndex by androidx.compose.runtime.saveable.rememberSaveable { 
        mutableIntStateOf(initialTab.coerceIn(0, tabs.size - 1)) 
    }
    val tabBackStack = remember { mutableStateListOf<Int>() }
    // 0 = Settings Tabs, 1 = BlockedAppsScreen, 2 = AppActivityScreen
    var settingsSubScreen by androidx.compose.runtime.saveable.rememberSaveable { mutableIntStateOf(0) }

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
    
    val backProgress = remember { Animatable(0f) }
    var backEdge by remember { mutableIntStateOf(BackEventCompat.EDGE_LEFT) }

    PredictiveBackHandler(enabled = settingsSubScreen == 0) { progressFlow ->
        try {
            progressFlow.collect { event ->
                backEdge = event.swipeEdge
                backProgress.snapTo(event.progress.coerceIn(0f, 1f))
            }
            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
            if (tabBackStack.isNotEmpty()) {
                val prevTab = tabBackStack.removeAt(tabBackStack.lastIndex)
                selectedTabIndex = prevTab
                backProgress.snapTo(0f)
            } else {
                com.example.service.SoundPlayer.stop()
                onDismiss()
            }
        } catch (e: kotlinx.coroutines.CancellationException) {
            withContext(kotlinx.coroutines.NonCancellable) {
                backProgress.animateTo(0f, animationSpec = PomoMotion.fastSpatial())
            }
            throw e
        }
    }

    val isPoppingTab = tabBackStack.isNotEmpty()
    val isPredictiveDragging by remember {
        derivedStateOf { backProgress.value > 0.001f }
    }
    val mascotRenderMode = when {
        settingsSubScreen != 0 -> MascotRenderMode.PAUSED
        isPredictiveDragging -> MascotRenderMode.THROTTLED_30FPS
        else -> MascotRenderMode.FULL
    }

    CompositionLocalProvider(LocalMascotRenderMode provides mascotRenderMode) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val p = backProgress.value
                if (p > 0f) {
                    drawRect(Color.Black.copy(alpha = (p * 0.25f).coerceIn(0f, 0.25f)))
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val p = backProgress.value
                    val edgeDirection = if (backEdge == BackEventCompat.EDGE_LEFT) 1f else -1f
                    val overlayScale = if (isPoppingTab) 1f - (p * 0.03f) else 1f - (p * 0.10f)
                    val overlayCornerDp = if (isPoppingTab) 0.dp else (p * 28f).dp
                    scaleX = overlayScale
                    scaleY = overlayScale
                    translationX = edgeDirection * p * (if (isPoppingTab) 32.dp.toPx() else 20.dp.toPx())
                    shape = RoundedCornerShape(overlayCornerDp)
                    clip = overlayCornerDp > 0.dp
                    compositingStrategy = CompositingStrategy.Offscreen
                }
        ) {
    SharedTransitionLayout {
        AnimatedContent(
            targetState = settingsSubScreen,
            transitionSpec = {
                val isActivityTransition = targetState == 2 || initialState == 2
                if (isActivityTransition) {
                    PomoMotion.drillDownTransition(forward = targetState > initialState)
                } else {
                    PomoMotion.horizontalSlideTransition(forward = targetState > initialState)
                }
            },
            label = "SettingsSubScreenTransition"
        ) { activeSubScreen ->
            val subScreenVisibilityScope = this
            val blockedBoundsModifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key = "blocked_apps_bounds"),
                animatedVisibilityScope = subScreenVisibilityScope,
                boundsTransform = { _, _ -> PomoMotion.expressiveSpatial() }
            )
            if (activeSubScreen == 1) {
                com.example.ui.BlockedAppsScreen(
                    onBack = { settingsSubScreen = 0 },
                    modifier = blockedBoundsModifier
                )
            } else if (activeSubScreen == 2) {
                com.example.ui.AppActivityScreen(
                    onBack = { settingsSubScreen = 0 }
                )
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
                            val tabInteraction = remember { MutableInteractionSource() }
                            val isTabPressed by tabInteraction.collectIsPressedAsState()
                            val tabScale by animateFloatAsState(
                                targetValue = if (isTabPressed) 0.93f else (if (isSelected) 1.02f else 1f),
                                animationSpec = PomoMotion.expressiveSpatial(),
                                label = "tabScale"
                            )
                            val tabBgColor by animateColorAsState(
                                targetValue = if (isSelected) currentTheme.primary else currentTheme.surface, 
                                animationSpec = PomoMotion.colorSpec, label = "tabBg"
                            )
                            val tabTextColor by animateColorAsState(
                                targetValue = if (isSelected) onPrimaryColor else currentTheme.textPrimary, 
                                animationSpec = PomoMotion.colorSpec, label = "tabText"
                            )
                            val tabElevation by animateDpAsState(
                                targetValue = if (isTabPressed) 0.dp else (if (isSelected) 4.dp else 1.dp), 
                                animationSpec = PomoMotion.elevationSpec, label = "tabElev"
                            )
                            
                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = tabScale
                                        scaleY = tabScale
                                    }
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
                                    .clickable(
                                        interactionSource = tabInteraction,
                                        indication = ripple(bounded = true)
                                    ) { 
                                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
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
                        modifier = Modifier
                            .fillMaxSize()
                            .background(currentTheme.background)
                    ) {
                        androidx.compose.animation.AnimatedContent(
                            targetState = selectedTabIndex,
                            transitionSpec = {
                                PomoMotion.asymmetricFadeThroughTransition()
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    compositingStrategy = CompositingStrategy.Offscreen
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
                                                BoxWithConstraints(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(currentTheme.textPrimary.copy(alpha = 0.05f))
                                                        .padding(4.dp)
                                                ) {
                                                    val compOptions = remember { listOf("Get Up", "Alarm", "Ring", "Custom") }
                                                    val onSelectedCompColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                                                    val segWidth = maxWidth / compOptions.size
                                                    val compIndicatorOffset by animateDpAsState(
                                                        targetValue = segWidth * localCompletion.coerceIn(0, compOptions.lastIndex),
                                                        animationSpec = PomoMotion.expressiveSpatial(androidx.compose.ui.unit.Dp.VisibilityThreshold),
                                                        label = "compIndicatorOffset"
                                                    )

                                                    Box(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                                                        Box(
                                                            modifier = Modifier
                                                                .offset(x = compIndicatorOffset)
                                                                .width(segWidth)
                                                                .fillMaxHeight()
                                                                .shadow(2.dp, RoundedCornerShape(10.dp), spotColor = currentTheme.primary.copy(alpha = 0.35f))
                                                                .clip(RoundedCornerShape(10.dp))
                                                                .background(currentTheme.primary)
                                                        )

                                                        Row(modifier = Modifier.fillMaxWidth()) {
                                                            compOptions.forEachIndexed { index, name ->
                                                                val isSelectedComp = localCompletion == index
                                                                val segInteraction = remember { MutableInteractionSource() }
                                                                val isSegPressed by segInteraction.collectIsPressedAsState()
                                                                val segScale by animateFloatAsState(
                                                                    targetValue = if (isSegPressed) 0.93f else 1f,
                                                                    animationSpec = PomoMotion.pressScaleSpec,
                                                                    label = "compSegScale_$index"
                                                                )
                                                                val compTextColor by animateColorAsState(
                                                                    targetValue = if (isSelectedComp) onSelectedCompColor else currentTheme.textPrimary,
                                                                    animationSpec = PomoMotion.colorSpec,
                                                                    label = "compTextColor_$index"
                                                                )
                                                                Box(
                                                                    modifier = Modifier
                                                                        .weight(1f)
                                                                        .graphicsLayer {
                                                                            scaleX = segScale
                                                                            scaleY = segScale
                                                                        }
                                                                        .clip(RoundedCornerShape(10.dp))
                                                                        .clickable(
                                                                            interactionSource = segInteraction,
                                                                            indication = ripple(bounded = true)
                                                                        ) {
                                                                            try {
                                                                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                                                            } catch (_: Exception) {}
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
                                                                        color = compTextColor,
                                                                        fontSize = 11.scaledSp,
                                                                        fontWeight = if (isSelectedComp) FontWeight.Bold else FontWeight.Medium,
                                                                        maxLines = 1
                                                                    )
                                                                }
                                                            }
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
                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                        settingsSubScreen = 1 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(blockedBoundsModifier)
                        .graphicsLayer {
                            scaleX = blockedScale
                            scaleY = blockedScale
                        },
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

                Spacer(modifier = Modifier.height(12.dp))

                // App Activity Details & Daily Time Limits Option (fixed static copy like App Blocker)
                val activityInteraction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                val isActivityPressed by activityInteraction.collectIsPressedAsState()
                val activityScale by androidx.compose.animation.core.animateFloatAsState(
                    targetValue = if (isActivityPressed) 0.96f else 1f,
                    animationSpec = PomoMotion.pressScaleSpec,
                    label = "activityScale"
                )

                Card(
                    onClick = {
                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                        settingsSubScreen = 2
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            scaleX = activityScale
                            scaleY = activityScale
                        }
                        .testTag("settings_app_activity_card"),
                    shape = RoundedCornerShape(16.dp),
                    interactionSource = activityInteraction,
                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp, pressedElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(currentTheme.primary.copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.QueryStats,
                                contentDescription = null,
                                tint = currentTheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "App Activity Details",
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "View weekly usage graphs, top apps & set daily limits (4 AM–4 AM).",
                                fontSize = 12.scaledSp,
                                color = currentTheme.textSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Open App Activity Details",
                            tint = currentTheme.textSecondary
                        )
                    }
                }
            } // Close if (tabIndex == 2)

            
            if (tabIndex == 3) {
                com.example.ui.SystemProtectionScreen()
            } // Close if (selectedTabIndex == 3)            
            Spacer(modifier = Modifier.height(100.dp))
                        } // closes Column
                    } // closes else
                } // closes SettingsTabTransition AnimatedContent
            } // closes Box
        } // closes Column
    } // closes Scaffold
} // closes else
        } // closes BlockedAppsTransition AnimatedContent
    } // closes SharedTransitionLayout
        } // closes predictive back content Box
    } // closes predictive back scrim Box
    } // closes CompositionLocalProvider(LocalMascotRenderMode)
} // closes SettingsOverlay

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
    val containerColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) {
            taskColor.copy(alpha = 0.08f).compositeOver(currentTheme.surface)
        } else {
            currentTheme.surface
        },
        animationSpec = PomoMotion.colorSpec,
        label = "taskContainerColor"
    )
    val rowInteraction = remember { MutableInteractionSource() }
    val isRowPressed by rowInteraction.collectIsPressedAsState()
    val rowScale by animateFloatAsState(
        targetValue = when {
            isRowPressed -> 0.97f
            isSelected -> 1.015f
            else -> 1f
        },
        animationSpec = PomoMotion.pressScaleSpec,
        label = "taskRowScale"
    )
    
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = rowScale
                scaleY = rowScale
            }
            .shadow(elevation = if (isSelected) 5.dp else 3.dp, shape = RoundedCornerShape(24.dp), spotColor = currentTheme.shadowColor)
            .border(borderWidth, borderColor, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = rowInteraction,
                indication = androidx.compose.material3.ripple(bounded = true)
            ) {
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
                enter = androidx.compose.animation.scaleIn(PomoMotion.expressiveSpatial()) +
                    androidx.compose.animation.fadeIn(PomoMotion.fastEffects()),
                exit = androidx.compose.animation.scaleOut(PomoMotion.fastSpatial()) +
                    androidx.compose.animation.fadeOut(PomoMotion.fastEffects())
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
        val playButtonCorner by animateDpAsState(
            targetValue = if (isRunning) 24.dp else 40.dp,
            animationSpec = PomoMotion.expressiveSpatial(androidx.compose.ui.unit.Dp.VisibilityThreshold),
            label = "playButtonCorner"
        )
        val playButtonShape = RoundedCornerShape(playButtonCorner)

        AnimatedScaleBox(
            shape = playButtonShape,
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
                    .clip(playButtonShape)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(currentTheme.primaryLight, currentTheme.primary)
                        )
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.8f), playButtonShape),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isRunning,
                    transitionSpec = {
                        (fadeIn(PomoMotion.fastEffects()) +
                            scaleIn(initialScale = 0.75f, animationSpec = PomoMotion.expressiveSpatial())) togetherWith
                            (fadeOut(PomoMotion.fastEffects()) +
                                scaleOut(targetScale = 0.75f, animationSpec = PomoMotion.fastSpatial()))
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
        val syncRotation by animateFloatAsState(
            targetValue = if (isBreakMode) 180f else 0f,
            animationSpec = PomoMotion.expressiveSpatial(),
            label = "syncModeRotation"
        )
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
                    modifier = Modifier
                        .size(28.dp)
                        .graphicsLayer { rotationZ = syncRotation }
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
                        if (targetState < initialState || (initialState == '0' && targetState == '9') || (initialState == '0' && targetState == '5')) {
                            // Counting down (Shared Axis Y downward roll)
                            (slideInVertically(PomoMotion.intOffsetSpatial) { height -> -height / 2 } +
                                fadeIn(PomoMotion.defaultEffects())) togetherWith
                                (slideOutVertically(PomoMotion.intOffsetFastSpatial) { height -> height / 2 } +
                                    fadeOut(PomoMotion.fastEffects()))
                        } else {
                            // Counting up or reset (Shared Axis Y upward roll)
                            (slideInVertically(PomoMotion.intOffsetSpatial) { height -> height / 2 } +
                                fadeIn(PomoMotion.defaultEffects())) togetherWith
                                (slideOutVertically(PomoMotion.intOffsetFastSpatial) { height -> -height / 2 } +
                                    fadeOut(PomoMotion.fastEffects()))
                        }.using(SizeTransform(clip = false) { _, _ -> PomoMotion.intSizeSpatial })
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
    circleSize: androidx.compose.ui.unit.Dp = 260.dp,
    onChooseTaskClick: (() -> Unit)? = null
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val view = androidx.compose.ui.platform.LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    var animatedText by remember { mutableStateOf("") }
    
    // Snappy typing effect for Task Name
    LaunchedEffect(taskName) {
        animatedText = ""
        for (i in taskName.indices) {
            animatedText += taskName[i]
            delay(26)
        }
    }

    val progress = if (totalTime > 0) timeRemaining.toFloat() / totalTime.toFloat() else 0f
    val isTimerRunning = state == TimerManager.TimerState.RUNNING
    val animatedProgress = remember { Animatable(progress) }
    val ringSweepReveal = remember { Animatable(if (isTimerRunning) 0.78f else 0f) }

    LaunchedEffect(Unit) {
        delay(55)
        ringSweepReveal.animateTo(1f, animationSpec = PomoMotion.slowSpatial())
    }

    LaunchedEffect(progress, isTimerRunning) {
        if (isTimerRunning) {
            animatedProgress.animateTo(progress, animationSpec = tween(950, easing = LinearEasing))
        } else {
            animatedProgress.animateTo(progress, animationSpec = PomoMotion.defaultSpatial())
        }
    }

    val waveAmplitudeState = animateFloatAsState(
        targetValue = if (isTimerRunning) 1f else 0f,
        animationSpec = PomoMotion.defaultSpatial(),
        label = "waveAmplitudeRatio"
    )
    val wavePhaseTransition = rememberInfiniteTransition(label = "wavyArcPhase")
    val wavePhaseState = wavePhaseTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * kotlin.math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )
    val activeArcColorState = animateColorAsState(
        targetValue = if (isBreakMode) currentTheme.secondary else currentTheme.primary,
        animationSpec = PomoMotion.colorSpec,
        label = "activeArcColor"
    )
    val wavePath = remember { androidx.compose.ui.graphics.Path() }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.70f)
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
                val strokeWidthPx = if (dynamicCircleSize < 200.dp) 4.dp.toPx() else 8.dp.toPx()
                val maxWaveAmpPx = if (dynamicCircleSize < 200.dp) 2.dp.toPx() else 3.2.dp.toPx()
                val currentWaveAmpPx = maxWaveAmpPx * waveAmplitudeState.value
                val wavePhase = wavePhaseState.value
                val activeArcColor = activeArcColorState.value
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val baseRadius = (minOf(size.width, size.height) - strokeWidthPx) / 2f - maxWaveAmpPx

                drawCircle(
                    color = currentTheme.cardBorder.copy(alpha = 0.5f),
                    radius = baseRadius,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )

                val sweepDeg = (animatedProgress.value * ringSweepReveal.value.coerceIn(0f, 1f) * 360f).coerceIn(0f, 360f)
                if (sweepDeg > 0.5f) {
                    if (currentWaveAmpPx < 0.15f) {
                        val inset = maxWaveAmpPx + strokeWidthPx / 2f
                        drawArc(
                            color = activeArcColor,
                            startAngle = -90f,
                            sweepAngle = sweepDeg,
                            useCenter = false,
                            topLeft = Offset(inset, inset),
                            size = Size(size.width - inset * 2f, size.height - inset * 2f),
                            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                        )
                    } else {
                        wavePath.reset()
                        val stepDeg = 2.5f
                        val waveCycles = 12f
                        var deg = 0f
                        var isFirst = true
                        while (deg <= sweepDeg) {
                            val angleRad = Math.toRadians((-90f + deg).toDouble())
                            val r = baseRadius + currentWaveAmpPx * kotlin.math.sin(
                                waveCycles * angleRad + wavePhase
                            ).toFloat()
                            val x = centerX + r * kotlin.math.cos(angleRad).toFloat()
                            val y = centerY + r * kotlin.math.sin(angleRad).toFloat()
                            if (isFirst) {
                                wavePath.moveTo(x, y)
                                isFirst = false
                            } else {
                                wavePath.lineTo(x, y)
                            }
                            deg += stepDeg
                        }
                        if (deg - stepDeg < sweepDeg) {
                            val endRad = Math.toRadians((-90f + sweepDeg).toDouble())
                            val r = baseRadius + currentWaveAmpPx * kotlin.math.sin(
                                waveCycles * endRad + wavePhase
                            ).toFloat()
                            wavePath.lineTo(
                                centerX + r * kotlin.math.cos(endRad).toFloat(),
                                centerY + r * kotlin.math.sin(endRad).toFloat()
                            )
                        }
                        drawPath(
                            path = wavePath,
                            color = activeArcColor,
                            style = Stroke(
                                width = strokeWidthPx,
                                cap = StrokeCap.Round,
                                join = androidx.compose.ui.graphics.StrokeJoin.Round
                            )
                        )
                    }
                }
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val isLongFormat = (timeRemaining / 60) > 99
                val scaleFactor = if (isLongFormat) 0.16f else 0.22f
                val baseFontSize = dynamicCircleSize.value * scaleFactor
                val timerFontSize = baseFontSize.coerceAtMost(if (isLongFormat) 38f else 54f).sp
                SlidingTimer(timeRemaining = timeRemaining, fontSize = timerFontSize)
                Spacer(modifier = Modifier.height(if (dynamicCircleSize < 200.dp) 2.dp else 4.dp))
                val stateBg by animateColorAsState(
                    targetValue = if (isBreakMode) currentTheme.secondary.copy(alpha = 0.18f) else currentTheme.primary.copy(alpha = 0.18f),
                    animationSpec = PomoMotion.colorSpec,
                    label = "modePillBg"
                )
                val stateTextCol by animateColorAsState(
                    targetValue = if (isBreakMode) currentTheme.secondary else currentTheme.primary,
                    animationSpec = PomoMotion.colorSpec,
                    label = "modePillTextCol"
                )
                val pillAlphaState = animateFloatAsState(
                    targetValue = if (isTimerRunning) 0.55f else 1f,
                    animationSpec = PomoMotion.fastEffects(),
                    label = "modePillAlpha"
                )
                val stateText = if (isBreakMode) "BREAK TIME" else "WORK TIME"
                Box(
                    modifier = Modifier
                        .graphicsLayer { alpha = pillAlphaState.value }
                        .clip(CircleShape)
                        .background(stateBg)
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
                    AnimatedContent(
                        targetState = stateText,
                        transitionSpec = { PomoMotion.verticalCrossfadeTransition(upward = isBreakMode) },
                        label = "modePillText"
                    ) { labelText ->
                        Text(
                            text = labelText,
                            fontSize = if (dynamicCircleSize < 200.dp) 9.scaledSp else 12.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = LocalAppFont.current,
                            color = stateTextCol,
                            letterSpacing = 1.scaledSp
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(if (circleSize < 200.dp) 16.dp else if (circleSize < 240.dp) 24.dp else 40.dp))
        
        AnimatedContent(
            targetState = currentTask,
            transitionSpec = { PomoMotion.verticalCrossfadeTransition(upward = targetState != null) },
            label = "selectedTaskBadge"
        ) { activeTask ->
            if (activeTask != null) {
                val activeTaskColor = com.example.service.safeParseColor(activeTask.categoryColor, currentTheme.primary)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp))
                        .clickable { 
                            try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch(e: Exception){}
                            TimerManager.setTask(-1, if (isBreakMode) "Break Time!" else "Focus Time!")
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
                                text = activeTask.name,
                                fontSize = 15.scaledSp,
                                color = currentTheme.textPrimary,
                                fontWeight = FontWeight.Bold,
                                fontFamily = LocalAppFont.current
                            )
                            Text(
                                text = "${activeTask.categoryName} • ${if (activeTask.isBreakCategory) "Break Timer" else "Work Timer"}",
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(
                            enabled = state == TimerManager.TimerState.STOPPED && onChooseTaskClick != null
                        ) {
                            try {
                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (_: Exception) {}
                            onChooseTaskClick?.invoke()
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("home_choose_task_badge")
                ) {
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
                    transitionSpec = { PomoMotion.verticalCrossfadeTransition(upward = true) },
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
