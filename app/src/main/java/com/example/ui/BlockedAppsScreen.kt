package com.example.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import android.view.HapticFeedbackConstants
import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.service.AppBlockerManager
import com.example.service.SettingsManager
import com.example.service.TimerManager
import com.example.ui.components.PinnedSearchHeader
import com.example.ui.components.SearchTriggerPill
import com.example.ui.components.pomoShadow
import com.example.ui.components.rememberDismissKeyboardOnScrollConnection
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import com.example.ui.theme.PomoMotion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Immutable
data class AppItem(
    val packageName: String,
    val label: String,
    val isBlocked: Boolean,
    val lowerLabel: String = label.lowercase()
)

private val BlockedRowShape = RoundedCornerShape(16.dp)

private val POPULAR_DISTRACTION_PACKAGES = setOf(
    "com.instagram.android",
    "com.google.android.youtube",
    "com.zhiliaoapp.musically",
    "com.twitter.android",
    "com.snapchat.android",
    "com.reddit.frontpage",
    "com.netflix.mediaclient",
    "tv.twitch.android.app",
    "com.discord",
    "com.facebook.katana",
    "com.pinterest",
    "com.whatsapp",
    "org.telegram.messenger",
    "com.valvesoftware.android.steam.community",
    "com.riotgames.league.wildrift",
    "com.supercell.clashofclans",
    "com.supercell.clashroyale",
    "com.pubg.imobile",
    "com.dts.freefireth"
)

private val AppIconShape = RoundedCornerShape(12.dp)

object AppIconCache {
    private val cache = object : android.util.LruCache<String, ImageBitmap>(220) {}
    private val failedPackages = java.util.Collections.synchronizedSet(HashSet<String>())

    fun get(packageName: String): ImageBitmap? = cache.get(packageName)

    fun put(packageName: String, bitmap: ImageBitmap) {
        cache.put(packageName, bitmap)
    }

    fun markFailed(packageName: String) {
        failedPackages.add(packageName)
    }

    fun hasFailed(packageName: String): Boolean = failedPackages.contains(packageName)
}

@Composable
fun AppIcon(packageName: String) {
    val context = LocalContext.current
    var bitmap by remember(packageName) { mutableStateOf(AppIconCache.get(packageName)) }

    if (bitmap == null && !AppIconCache.hasFailed(packageName)) {
        LaunchedEffect(packageName) {
            val loaded = withContext(Dispatchers.IO) {
                try {
                    val cached = AppIconCache.get(packageName)
                    if (cached != null) return@withContext cached
                    val drawable = context.packageManager.getApplicationIcon(packageName)
                    val bmp = drawable.toBitmap(width = 96, height = 96)
                    val imageBitmap = bmp.asImageBitmap()
                    AppIconCache.put(packageName, imageBitmap)
                    imageBitmap
                } catch (_: Exception) {
                    AppIconCache.markFailed(packageName)
                    null
                }
            }
            if (loaded != null) {
                bitmap = loaded
            }
        }
    }

    val currentBitmap = bitmap
    if (currentBitmap != null) {
        Image(
            bitmap = currentBitmap,
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .clip(AppIconShape)
        )
    } else {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(Color.Gray.copy(alpha = 0.14f), AppIconShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Apps, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(22.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAppsScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val view = LocalView.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var appList by remember { mutableStateOf<List<AppItem>>(emptyList()) }
    var blockedSet by remember { mutableStateOf(SettingsManager.getBlockedApps()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyBlocked by remember { mutableStateOf(false) }

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val timerState by TimerManager.timerState.collectAsState()
    val isBreakMode by TimerManager.isBreakMode.collectAsState()

    var isStrictMode by remember { mutableStateOf(AppBlockerManager.isStrictMode(context)) }
    var isVibrateEnabled by remember { mutableStateOf(AppBlockerManager.isVibrateEnabled(context)) }
    var blockedAttemptsToday by remember { mutableIntStateOf(AppBlockerManager.getBlockAttemptsToday(context)) }

    val searchFocusRequester = remember { FocusRequester() }
    val dashboardListState = rememberLazyListState()
    val searchListState = rememberLazyListState()
    val dismissKeyboardOnScroll = rememberDismissKeyboardOnScrollConnection()

    val backProgress = remember { Animatable(0f) }
    var backEdge by remember { mutableIntStateOf(BackEventCompat.EDGE_LEFT) }

    val exitSearchMode: () -> Unit = {
        keyboardController?.hide()
        focusManager.clearFocus()
        searchQuery = ""
        isSearchActive = false
    }

    PredictiveBackHandler { progressFlow ->
        try {
            progressFlow.collect { event ->
                backEdge = event.swipeEdge
                backProgress.snapTo(event.progress.coerceIn(0f, 1f))
            }
            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
            if (isSearchActive) {
                exitSearchMode()
                backProgress.snapTo(0f)
            } else {
                onBack()
            }
        } catch (e: CancellationException) {
            withContext(NonCancellable) {
                backProgress.animateTo(0f, animationSpec = PomoMotion.fastSpatial())
            }
            throw e
        }
    }

    // Check accessibility status
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var accessibilityStatus by remember { mutableStateOf(AppBlockerManager.getAccessibilityStatus(context)) }

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME || event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                accessibilityStatus = AppBlockerManager.getAccessibilityStatus(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        blockedAttemptsToday = AppBlockerManager.getBlockAttemptsToday(context)
        withContext(Dispatchers.IO) {
            val buildList = mutableListOf<AppItem>()
            try {
                val pm = context.packageManager
                val intent = Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
                val resolveInfos = pm.queryIntentActivities(intent, 0)

                val addedPackages = mutableSetOf<String>()

                for (resolveInfo in resolveInfos) {
                    val packageName = resolveInfo.activityInfo?.packageName ?: continue
                    if (packageName == context.packageName) continue
                    if (addedPackages.contains(packageName)) continue

                    val label = try {
                        resolveInfo.loadLabel(pm).toString()
                    } catch (_: Exception) {
                        packageName
                    }
                    buildList.add(AppItem(packageName, label, blockedSet.contains(packageName), label.lowercase()))
                    addedPackages.add(packageName)
                }
                buildList.sortBy { it.lowerLabel }
            } catch (_: Exception) {}
            withContext(Dispatchers.Main) {
                appList = buildList
                isLoading = false
            }
        }
    }

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            delay(80)
            try {
                searchFocusRequester.requestFocus()
                keyboardController?.show()
            } catch (_: Exception) {}
        }
    }

    // Strictly keyed on raw app list and filter controls (never timer/ViewModel ticks)
    val baseFilteredList = remember(appList, showOnlyBlocked) {
        if (!showOnlyBlocked) appList else appList.filter { it.isBlocked }
    }

    // Instant prefix-ranked search results (startsWith floats above contains)
    val normalizedQuery = remember(searchQuery) { searchQuery.trim().lowercase() }
    val searchResults = remember(baseFilteredList, normalizedQuery) {
        if (normalizedQuery.isEmpty()) {
            baseFilteredList
        } else {
            val startsWithMatches = ArrayList<AppItem>(16)
            val containsMatches = ArrayList<AppItem>(32)
            for (app in baseFilteredList) {
                val idx = app.lowerLabel.indexOf(normalizedQuery)
                if (idx == 0) {
                    startsWithMatches.add(app)
                } else if (idx > 0) {
                    containsMatches.add(app)
                }
            }
            if (containsMatches.isEmpty()) {
                startsWithMatches
            } else {
                startsWithMatches.addAll(containsMatches)
                startsWithMatches
            }
        }
    }

    LaunchedEffect(isSearchActive, normalizedQuery, showOnlyBlocked) {
        if (isSearchActive) {
            searchListState.scrollToItem(0)
        }
    }

    val toggleAppBlocked: (String) -> Unit = remember(view) {
        { packageName ->
            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
            val currentSet = blockedSet.toMutableSet()
            val nowBlocked = if (currentSet.contains(packageName)) {
                currentSet.remove(packageName)
                false
            } else {
                currentSet.add(packageName)
                true
            }
            blockedSet = currentSet
            SettingsManager.setBlockedApps(currentSet)
            appList = appList.map {
                if (it.packageName == packageName) it.copy(isBlocked = nowBlocked) else it
            }
        }
    }

    val blockPopularPreset: () -> Unit = {
        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
        val newSet = blockedSet.toMutableSet()
        appList.forEach { app ->
            if (POPULAR_DISTRACTION_PACKAGES.contains(app.packageName)) {
                newSet.add(app.packageName)
            }
        }
        blockedSet = newSet
        SettingsManager.setBlockedApps(newSet)
        appList = appList.map { it.copy(isBlocked = newSet.contains(it.packageName)) }
    }

    val clearAllBlocked: () -> Unit = {
        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
        blockedSet = emptySet()
        SettingsManager.setBlockedApps(emptySet())
        appList = appList.map { it.copy(isBlocked = false) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val p = backProgress.value
                if (p > 0f) {
                    drawRect(Color.Black.copy(alpha = (p * 0.22f).coerceIn(0f, 0.22f)))
                }
            }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val p = backProgress.value
                    val edgeDirection = if (backEdge == BackEventCompat.EDGE_LEFT) 1f else -1f
                    val gestureScale = if (isSearchActive) 1f - (p * 0.03f) else 1f - (p * 0.09f)
                    val gestureCornerDp = if (isSearchActive) 0.dp else (p * 28f).dp
                    scaleX = gestureScale
                    scaleY = gestureScale
                    translationX = edgeDirection * p * (if (isSearchActive) 28.dp.toPx() else 20.dp.toPx())
                    shape = RoundedCornerShape(gestureCornerDp)
                    clip = gestureCornerDp > 0.dp
                    compositingStrategy = CompositingStrategy.Offscreen
                },
            contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
            topBar = {
                AnimatedContent(
                    targetState = isSearchActive,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(170)) togetherWith fadeOut(animationSpec = tween(120))
                    },
                    label = "BlockerTopBarMode"
                ) { searching ->
                    if (searching) {
                        PinnedSearchHeader(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onCloseSearch = exitSearchMode,
                            onSubmitSearch = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            focusRequester = searchFocusRequester,
                            placeholder = "Search apps to block...",
                            backTestTag = "blocker_search_back_button",
                            inputTestTag = "blocker_pinned_search_input",
                            clearTestTag = "blocker_search_clear_button"
                        )
                    } else {
                        TopAppBar(
                            title = {
                                Column {
                                    Text(
                                        text = "Distraction Shield",
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = currentFont,
                                        color = currentTheme.textPrimary,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "${blockedSet.size} apps protected",
                                        fontSize = 12.sp,
                                        fontFamily = currentFont,
                                        color = currentTheme.textSecondary
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                        onBack()
                                    },
                                    modifier = Modifier.testTag("blocker_back_button")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = currentTheme.textPrimary)
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                        isSearchActive = true
                                    },
                                    modifier = Modifier.testTag("blocker_top_search_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Search,
                                        contentDescription = "Search Apps",
                                        tint = currentTheme.textPrimary
                                    )
                                }
                                IconButton(onClick = {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                    val previewIntent = Intent(context, BlockedAppActivity::class.java).apply {
                                        putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, "com.instagram.android")
                                        putExtra(BlockedAppActivity.EXTRA_PREVIEW_MODE, true)
                                    }
                                    context.startActivity(previewIntent)
                                }) {
                                    Icon(
                                        Icons.Filled.Visibility,
                                        contentDescription = "Preview Blocker",
                                        tint = currentTheme.primary
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = currentTheme.background)
                        )
                    }
                }
            },
            containerColor = currentTheme.background
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                AnimatedContent(
                    targetState = isSearchActive,
                    transitionSpec = { PomoMotion.asymmetricFadeThroughTransition() },
                    label = "BlockerBodyMode"
                ) { searching ->
                    if (searching) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                LazyRow(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    item(key = "search_chip_blocked_only") {
                                        FilterChip(
                                            selected = showOnlyBlocked,
                                            onClick = {
                                                try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                showOnlyBlocked = !showOnlyBlocked
                                            },
                                            label = { Text("Blocked Only (${blockedSet.size})", fontFamily = currentFont, fontSize = 12.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = currentTheme.primary.copy(alpha = 0.15f),
                                                selectedLabelColor = currentTheme.primary,
                                                containerColor = currentTheme.surface,
                                                labelColor = currentTheme.textPrimary
                                            )
                                        )
                                    }
                                    item(key = "search_chip_preset") {
                                        FilterChip(
                                            selected = false,
                                            onClick = blockPopularPreset,
                                            label = { Text("⚡ Social & Games", fontFamily = currentFont, fontSize = 12.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                containerColor = currentTheme.surface,
                                                labelColor = currentTheme.textPrimary
                                            )
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${searchResults.size} found",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = currentTheme.textSecondary,
                                    fontFamily = MonospaceFontFamily
                                )
                            }

                            LazyColumn(
                                state = searchListState,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .nestedScroll(dismissKeyboardOnScroll),
                                contentPadding = PaddingValues(top = 4.dp, bottom = 36.dp)
                            ) {
                                if (isLoading) {
                                    item(key = "blocker_search_loading") {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(180.dp)) {
                                            CircularProgressIndicator(color = currentTheme.primary)
                                        }
                                    }
                                } else if (searchResults.isEmpty()) {
                                    item(key = "blocker_search_empty") {
                                        BlockerEmptyState(
                                            text = if (normalizedQuery.isNotEmpty()) {
                                                "No apps matching \"$normalizedQuery\""
                                            } else if (showOnlyBlocked) {
                                                "No apps currently blocked."
                                            } else {
                                                "No matching apps found."
                                            }
                                        )
                                    }
                                } else {
                                    items(
                                        items = searchResults,
                                        key = { it.packageName },
                                        contentType = { "app_row" }
                                    ) { app ->
                                        BlockedAppRowItem(
                                            app = app,
                                            onToggle = { toggleAppBlocked(app.packageName) }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                        LazyColumn(
                            state = dashboardListState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 32.dp + navBottom)
                        ) {

                // 2. Real-Time Status & Daily Stats Dashboard
                item(key = "blocker_status_dashboard", contentType = "status_dashboard") {
                    val statusText: String
                    val statusColor: Color
                    val statusIcon = when {
                        accessibilityStatus == AppBlockerManager.AccessibilityServiceStatus.DISABLED -> {
                            statusText = "Permission Needed"
                            statusColor = Color(0xFFE53935)
                            Icons.Filled.ErrorOutline
                        }
                        accessibilityStatus == AppBlockerManager.AccessibilityServiceStatus.STALE || accessibilityStatus == AppBlockerManager.AccessibilityServiceStatus.UNKNOWN -> {
                            statusText = "Service Interrupted"
                            statusColor = Color(0xFFF57C00)
                            Icons.Filled.ErrorOutline
                        }
                        isBreakMode -> {
                            statusText = "Break Time • Unlocked"
                            statusColor = Color(0xFF1E88E5)
                            Icons.Filled.Coffee
                        }
                        timerState == TimerManager.TimerState.RUNNING -> {
                            statusText = "Active & Guarding Focus"
                            statusColor = Color(0xFF43A047)
                            Icons.Filled.Shield
                        }
                        timerState == TimerManager.TimerState.PAUSED -> {
                            if (isStrictMode) {
                                statusText = "Strict Mode Active (Paused)"
                                statusColor = Color(0xFF43A047)
                                Icons.Filled.Shield
                            } else {
                                statusText = "Timer Paused"
                                statusColor = Color(0xFFFB8C00)
                                Icons.Filled.PauseCircle
                            }
                        }
                        else -> {
                            statusText = "Standby (Ready to Focus)"
                            statusColor = currentTheme.textSecondary
                            Icons.Filled.CheckCircleOutline
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .pomoShadow(
                                shape = RoundedCornerShape(20.dp),
                                elevation = 3.dp,
                                shadowColor = currentTheme.shadowColor
                            ),
                        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(statusColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = statusText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = currentTheme.textPrimary,
                                    fontFamily = currentFont
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                // Test / Preview Pill
                                val previewInteraction = remember { MutableInteractionSource() }
                                val isPreviewPressed by previewInteraction.collectIsPressedAsState()
                                val previewScale by animateFloatAsState(
                                    targetValue = if (isPreviewPressed) 0.93f else 1f,
                                    animationSpec = PomoMotion.pressScaleSpec,
                                    label = "previewScale"
                                )
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = currentTheme.primary.copy(alpha = 0.12f),
                                    modifier = Modifier.graphicsLayer {
                                        scaleX = previewScale
                                        scaleY = previewScale
                                    },
                                    interactionSource = previewInteraction,
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                        val previewIntent = Intent(context, BlockedAppActivity::class.java).apply {
                                            putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, "com.instagram.android")
                                            putExtra(BlockedAppActivity.EXTRA_PREVIEW_MODE, true)
                                        }
                                        context.startActivity(previewIntent)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Filled.PlayArrow,
                                            contentDescription = null,
                                            tint = currentTheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "Preview 5s",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = currentTheme.primary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Stats row: Blocked count & Distractions resisted
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = currentTheme.background.copy(alpha = 0.7f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${blockedSet.size}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = currentTheme.primary,
                                            fontFamily = currentFont
                                        )
                                        Text(
                                            text = "Apps Blocked",
                                            fontSize = 11.sp,
                                            color = currentTheme.textSecondary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = currentTheme.background.copy(alpha = 0.7f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "$blockedAttemptsToday",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = currentTheme.secondary,
                                            fontFamily = currentFont
                                        )
                                        Text(
                                            text = "Resisted Today",
                                            fontSize = 11.sp,
                                            color = currentTheme.textSecondary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = currentTheme.background.copy(alpha = 0.7f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "5s",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = currentTheme.textPrimary,
                                            fontFamily = currentFont
                                        )
                                        Text(
                                            text = "Mindful Pause",
                                            fontSize = 11.sp,
                                            color = currentTheme.textSecondary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Strict Mode & Haptic Settings Card
                item(key = "blocker_strict_settings", contentType = "settings_card") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .pomoShadow(
                                shape = RoundedCornerShape(20.dp),
                                elevation = 2.dp,
                                shadowColor = currentTheme.shadowColor
                            ),
                        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            // Strict Mode Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Strict Mode",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = currentTheme.textPrimary,
                                        fontFamily = currentFont
                                    )
                                    Text(
                                        "Keep blocking active even when timer is paused",
                                        fontSize = 12.sp,
                                        color = currentTheme.textSecondary,
                                        fontFamily = currentFont
                                    )
                                }
                                Switch(
                                    checked = isStrictMode,
                                    onCheckedChange = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        isStrictMode = it
                                        AppBlockerManager.setStrictMode(context, it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = currentTheme.surface,
                                        checkedTrackColor = currentTheme.primary
                                    )
                                )
                            }

                            HorizontalDivider(
                                color = currentTheme.cardBorder.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Haptic Feedback Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Tactile Awareness",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = currentTheme.textPrimary,
                                        fontFamily = currentFont
                                    )
                                    Text(
                                        "Gentle haptic vibration when an app is intercepted",
                                        fontSize = 12.sp,
                                        color = currentTheme.textSecondary,
                                        fontFamily = currentFont
                                    )
                                }
                                Switch(
                                    checked = isVibrateEnabled,
                                    onCheckedChange = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        isVibrateEnabled = it
                                        AppBlockerManager.setVibrateEnabled(context, it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = currentTheme.surface,
                                        checkedTrackColor = currentTheme.primary
                                    )
                                )
                            }
                        }
                    }
                }

                // 4. Quick Selection Chips & Search Trigger Pill
                item(key = "blocker_search_controls", contentType = "search_controls") {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = false,
                                onClick = blockPopularPreset,
                                label = { Text("⚡ Block Social & Games", fontFamily = currentFont, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = currentTheme.surface,
                                    labelColor = currentTheme.textPrimary
                                )
                            )

                            FilterChip(
                                selected = showOnlyBlocked,
                                onClick = {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                    showOnlyBlocked = !showOnlyBlocked
                                },
                                label = { Text("Blocked Only (${blockedSet.size})", fontFamily = currentFont, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = currentTheme.primary.copy(alpha = 0.15f),
                                    selectedLabelColor = currentTheme.primary,
                                    containerColor = currentTheme.surface,
                                    labelColor = currentTheme.textPrimary
                                )
                            )

                            if (blockedSet.isNotEmpty()) {
                                IconButton(
                                    onClick = clearAllBlocked,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.ClearAll,
                                        contentDescription = "Clear All Blocked",
                                        tint = currentTheme.textSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tapping opens Pinned Search Mode so results always sit cleanly above the keyboard
                        SearchTriggerPill(
                            placeholder = if (appList.isNotEmpty()) "Search ${appList.size} apps to block..." else "Search apps...",
                            onClick = {
                                try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                isSearchActive = true
                            },
                            testTagName = "blocker_search_field"
                        )
                    }
                }

                // 5. Apps List (lightweight, cached icons, 120fps scrolling)
                if (isLoading) {
                    item(key = "blocker_loading", contentType = "loading_state") {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(200.dp)) {
                            CircularProgressIndicator(color = currentTheme.primary)
                        }
                    }
                } else {
                    if (baseFilteredList.isEmpty()) {
                        item(key = "blocker_empty", contentType = "empty_state") {
                            BlockerEmptyState(
                                text = if (showOnlyBlocked) "No apps currently blocked." else "No matching apps found."
                            )
                        }
                    } else {
                        items(
                            items = baseFilteredList,
                            key = { it.packageName },
                            contentType = { "app_row" }
                        ) { app ->
                            BlockedAppRowItem(
                                app = app,
                                onToggle = { toggleAppBlocked(app.packageName) }
                            )
                        }
                    }
                }

                // 6. Mindful Philosophy Explainer Card
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .pomoShadow(
                                shape = RoundedCornerShape(18.dp),
                                elevation = 1.dp,
                                shadowColor = currentTheme.shadowColor
                            ),
                        colors = CardDefaults.cardColors(containerColor = currentTheme.surface.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Lightbulb,
                                    contentDescription = null,
                                    tint = currentTheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Mindful Focus Philosophy",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = currentTheme.textPrimary,
                                    fontFamily = currentFont
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "When you open a blocked app, PomoPal shows a serene 5-second pause with student-focused motivations. This brief moment interrupts the compulsive dopamine loop, grounds your intention, and gracefully returns you to your study session.",
                                fontSize = 12.sp,
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
                    }
                }
            }
        }
    }
}

@Composable
private fun BlockerEmptyState(text: String) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.SearchOff,
                contentDescription = null,
                tint = currentTheme.textSecondary,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = currentTheme.textSecondary,
                fontFamily = currentFont,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Lightweight, allocation-minimal row for BlockedAppsScreen so long lists of 100+ apps
 * scroll smoothly without per-row Animatable / LaunchedEffect / color-spring churn.
 */
@Composable
private fun BlockedAppRowItem(
    app: AppItem,
    onToggle: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val isSelected = app.isBlocked

    Surface(
        onClick = onToggle,
        shape = BlockedRowShape,
        color = if (isSelected) currentTheme.primary.copy(alpha = 0.10f) else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.5.dp)
            .testTag("blocker_app_row_${app.packageName}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(packageName = app.packageName)
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.label,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 15.sp,
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (isSelected) "Distraction Shield Active" else "Allowed during study",
                    fontSize = 12.sp,
                    fontFamily = currentFont,
                    color = if (isSelected) currentTheme.primary else currentTheme.textSecondary
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (isSelected) currentTheme.primary else currentTheme.cardBorder.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isSelected,
                    enter = scaleIn(
                        initialScale = 0.4f,
                        animationSpec = PomoMotion.fastSpatial()
                    ) + fadeIn(PomoMotion.fastEffects()),
                    exit = scaleOut(
                        targetScale = 0.4f,
                        animationSpec = PomoMotion.fastSpatial()
                    ) + fadeOut(PomoMotion.fastEffects())
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Blocked",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

fun isAccessibilityServiceEnabled(context: Context): Boolean {
    var accessibilityEnabled = 0
    val service = context.packageName + "/com.example.service.AppBlockerService"
    try {
        accessibilityEnabled = Settings.Secure.getInt(
            context.applicationContext.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        )
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }
    val mStringColonSplitter = android.text.TextUtils.SimpleStringSplitter(':')
    if (accessibilityEnabled == 1) {
        val settingValue = Settings.Secure.getString(
            context.applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
    }
    return false
}
