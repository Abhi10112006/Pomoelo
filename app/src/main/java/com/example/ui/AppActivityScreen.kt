package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccessibilityNew
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.service.AppBlockerManager
import com.example.service.AppUsageLimitManager
import com.example.ui.components.ActivityTimeScope
import com.example.ui.components.AppActivityHeroBentoCard
import com.example.ui.components.AppActivityLoadingSkeleton
import com.example.ui.components.AppSevenDayLimitPreviewChart
import com.example.ui.components.SmartLimitSuggestionCard
import com.example.ui.components.StaggeredSectionReveal
import com.example.ui.components.TopFiveAppsCard
import com.example.ui.components.WeeklyActivityGraphCard
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import com.example.ui.theme.PomoMotion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.roundToInt

private enum class ActivityFilter(val label: String) {
    MOST_USED("All Apps"),
    LIMITED("With Limits"),
    NEAR_LIMIT("Near / Over")
}

@Immutable
private data class IndexedAppEntry(
    val app: AppUsageLimitManager.AppActivityEntry,
    val lowerLabel: String,
    val sharePercent: Int
)

private val RowCardShape = RoundedCornerShape(20.dp)
private val LimitBadgeShape = RoundedCornerShape(12.dp)
private val SearchCapsuleShape = RoundedCornerShape(16.dp)
private val WarnAmber = Color(0xFFF57C00)
private val OverRed = Color(0xFFE53935)
private val GraceBlue = Color(0xFF1E88E5)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AppActivityScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var summary by remember { mutableStateOf<AppUsageLimitManager.AppActivitySummary?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var activeFilter by remember { mutableStateOf(ActivityFilter.MOST_USED) }
    var timeScope by remember { mutableStateOf(ActivityTimeScope.TODAY) }
    var selectedDayIndex by remember { mutableIntStateOf(6) } // 6 = Today
    var editingApp by remember { mutableStateOf<AppUsageLimitManager.AppActivityEntry?>(null) }
    var accessibilityStatus by remember { mutableStateOf(AppBlockerManager.getAccessibilityStatus(context)) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    val limitsVersion by AppUsageLimitManager.limitsVersion.collectAsState()
    val searchFocusRequester = remember { FocusRequester() }

    // Remembered outside mode transitions so dashboard scroll position is preserved
    val dashboardListState = rememberLazyListState()
    val searchListState = rememberLazyListState()
    var hasAnchoredDashboardTop by remember { mutableStateOf(false) }

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

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME || event == Lifecycle.Event.ON_START) {
                accessibilityStatus = AppBlockerManager.getAccessibilityStatus(context)
                refreshTrigger++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(refreshTrigger, limitsVersion) {
        val loaded = withContext(Dispatchers.IO) {
            AppUsageLimitManager.loadAppActivitySummary(context)
        }
        summary = loaded
        if (loaded.weeklyDays.isNotEmpty()) {
            selectedDayIndex = selectedDayIndex.coerceIn(0, loaded.weeklyDays.lastIndex)
        }
        isRefreshing = false
    }

    // Guarantee initial dashboard mount lands right at the top Hero Summary & Graphs
    LaunchedEffect(summary != null) {
        if (summary != null && !hasAnchoredDashboardTop) {
            hasAnchoredDashboardTop = true
            dashboardListState.scrollToItem(0)
        }
    }

    // Auto-focus pinned search field smoothly when entering Search Mode
    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            delay(80)
            try {
                searchFocusRequester.requestFocus()
                keyboardController?.show()
            } catch (_: Exception) {}
        }
    }

    // Pre-index all apps once per load so scrolling & searching allocate zero lowercase/math per frame
    val indexedApps = remember(summary) {
        val s = summary ?: return@remember emptyList()
        val totalToday = s.totalScreenTimeMillis.coerceAtLeast(1L).toFloat()
        s.apps.map { app ->
            val share = if (app.usageMillisToday > 0L) {
                ((app.usageMillisToday.toFloat() / totalToday) * 100f).roundToInt().coerceIn(1, 100)
            } else {
                0
            }
            IndexedAppEntry(
                app = app,
                lowerLabel = app.label.lowercase(),
                sharePercent = share
            )
        }
    }

    val allApps = summary?.apps.orEmpty()
    val selectedDay = summary?.weeklyDays?.getOrNull(selectedDayIndex) ?: summary?.todayPoint

    val topFivePairList = remember(summary, timeScope, selectedDayIndex) {
        val s = summary ?: return@remember emptyList()
        if (timeScope == ActivityTimeScope.WEEK) {
            s.topFiveWeek.map { it to it.weeklyTotalMillis }
        } else {
            val dayMap = selectedDay?.appUsageMap ?: emptyMap()
            s.apps
                .map { app -> app to (dayMap[app.packageName] ?: 0L) }
                .filter { it.second > 0L }
                .sortedByDescending { it.second }
                .take(5)
        }
    }

    val topFiveScopeTotal = remember(summary, timeScope, selectedDay) {
        val s = summary ?: return@remember 0L
        if (timeScope == ActivityTimeScope.WEEK) s.weeklyTotalMillis
        else selectedDay?.totalMillis ?: s.totalScreenTimeMillis
    }

    val topFiveSubtitle = remember(timeScope, selectedDay) {
        when {
            timeScope == ActivityTimeScope.WEEK -> "Last 7 days · 4 AM cycle"
            selectedDay?.isToday == true -> "Today since 4:00 AM"
            selectedDay != null -> "${selectedDay.shortDayLabel}, ${selectedDay.fullDateLabel} · 4 AM – 4 AM"
            else -> "Today since 4:00 AM"
        }
    }

    val smartTipCandidate = remember(allApps) {
        allApps.firstOrNull { app ->
            app.dailyLimitMinutes == null &&
                (app.usageMillisToday >= 20 * 60_000L || app.weeklyAverageMillis >= 25 * 60_000L)
        }
    }
    val smartTipSuggestedMinutes = remember(smartTipCandidate) {
        val candidate = smartTipCandidate ?: return@remember 30
        val avgMins = (maxOf(candidate.weeklyAverageMillis, candidate.usageMillisToday) / 60_000L).toInt()
        val reduced = ((avgMins * 0.75f) / 5f).roundToInt() * 5
        reduced.coerceIn(15, 120)
    }

    // Dashboard filter (when not in search mode)
    val dashboardFilteredApps = remember(indexedApps, activeFilter) {
        when (activeFilter) {
            ActivityFilter.MOST_USED -> indexedApps
            ActivityFilter.LIMITED -> indexedApps.filter { it.app.dailyLimitMinutes != null }
            ActivityFilter.NEAR_LIMIT -> indexedApps.filter {
                it.app.dailyLimitMinutes != null && it.app.progressFraction >= 0.5f
            }
        }
    }

    // Instant prefix-ranked search results (startsWith floats above contains)
    val normalizedQuery = remember(searchQuery) { searchQuery.trim().lowercase() }
    val searchResults = remember(dashboardFilteredApps, normalizedQuery) {
        if (normalizedQuery.isEmpty()) {
            dashboardFilteredApps
        } else {
            val startsWithMatches = ArrayList<IndexedAppEntry>(16)
            val containsMatches = ArrayList<IndexedAppEntry>(32)
            for (item in dashboardFilteredApps) {
                val idx = item.lowerLabel.indexOf(normalizedQuery)
                if (idx == 0) {
                    startsWithMatches.add(item)
                } else if (idx > 0) {
                    containsMatches.add(item)
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

    // Keep top search result anchored right beneath the pinned search bar whenever query/filter changes
    LaunchedEffect(isSearchActive, normalizedQuery, activeFilter) {
        if (isSearchActive) {
            searchListState.scrollToItem(0)
        }
    }

    // Smoothly dismiss keyboard as soon as the user drags the search results list
    val dismissKeyboardOnScroll = remember(keyboardController, focusManager) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput && abs(available.y) > 4f) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
                return Offset.Zero
            }
        }
    }

    val resetInLabel = remember(summary?.nextResetMillis) {
        AppUsageLimitManager.formatTimeUntilReset()
    }

    val onOpenAppLimitSheet: (AppUsageLimitManager.AppActivityEntry) -> Unit = remember(view, keyboardController, focusManager) {
        { app ->
            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
            keyboardController?.hide()
            focusManager.clearFocus()
            editingApp = app
        }
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
            containerColor = currentTheme.background,
            contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
            topBar = {
                AnimatedContent(
                    targetState = isSearchActive,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(170)) togetherWith fadeOut(animationSpec = tween(120))
                    },
                    label = "AppActivityTopBarMode"
                ) { searching ->
                    if (searching) {
                        PinnedSearchTopBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onCloseSearch = exitSearchMode,
                            onSubmitSearch = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            focusRequester = searchFocusRequester
                        )
                    } else {
                        TopAppBar(
                            title = {
                                Column {
                                    Text(
                                        text = "App Activity",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontFamily = currentFont,
                                        color = currentTheme.textPrimary,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "4:00 AM cycle · Resets in $resetInLabel",
                                        fontSize = 11.5.sp,
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
                                    modifier = Modifier.testTag("app_activity_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = "Back",
                                        tint = currentTheme.textPrimary
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                        isSearchActive = true
                                    },
                                    modifier = Modifier.testTag("app_activity_top_search_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Search,
                                        contentDescription = "Search Apps",
                                        tint = currentTheme.textPrimary
                                    )
                                }
                                Surface(
                                    shape = CircleShape,
                                    color = currentTheme.primary.copy(alpha = 0.12f),
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                        val previewPkg = summary?.mostUsedApp?.packageName ?: "com.instagram.android"
                                        val previewLimit = summary?.mostUsedApp?.dailyLimitMinutes ?: 30
                                        val previewIntent = Intent(context, BlockedAppActivity::class.java).apply {
                                            putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, previewPkg)
                                            putExtra(BlockedAppActivity.EXTRA_PREVIEW_MODE, true)
                                            putExtra(BlockedAppActivity.EXTRA_BLOCK_MODE, BlockedAppActivity.MODE_DAILY_LIMIT)
                                            putExtra(BlockedAppActivity.EXTRA_LIMIT_MINUTES, previewLimit)
                                            putExtra(BlockedAppActivity.EXTRA_USED_MILLIS, previewLimit * 60_000L)
                                            putExtra(BlockedAppActivity.EXTRA_EXTENSIONS_USED, 0)
                                        }
                                        context.startActivity(previewIntent)
                                    },
                                    modifier = Modifier.testTag("app_activity_preview_limit_button")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Visibility,
                                            contentDescription = "Preview Lock Screen",
                                            tint = currentTheme.primary,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Preview",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = currentTheme.primary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                        isRefreshing = true
                                        refreshTrigger++
                                    },
                                    modifier = Modifier.testTag("app_activity_refresh_button")
                                ) {
                                    if (isRefreshing) {
                                        CircularProgressIndicator(
                                            color = currentTheme.primary,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.Refresh,
                                            contentDescription = "Refresh Usage",
                                            tint = currentTheme.textPrimary
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = currentTheme.background)
                        )
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                val currentSummary = summary
                AnimatedContent(
                    targetState = when {
                        currentSummary == null -> 0 // Loading skeleton
                        isSearchActive -> 2         // Dedicated keyboard-proof pinned search mode
                        else -> 1                   // Full analytics dashboard + installed apps
                    },
                    transitionSpec = { PomoMotion.asymmetricFadeThroughTransition() },
                    label = "AppActivityBodyMode"
                ) { bodyMode ->
                    when (bodyMode) {
                        0 -> AppActivityLoadingSkeleton()

                        2 -> {
                            val activeSummary = summary ?: return@AnimatedContent
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
                            ) {
                                // Filter Chips + Live Count pinned right below the top Search Bar
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    LazyRow(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(ActivityFilter.entries, key = { it.name }) { filter ->
                                            ActivityFilterChipItem(
                                                filter = filter,
                                                selected = activeFilter == filter,
                                                activeLimitsCount = activeSummary.activeLimitsCount,
                                                onSelect = {
                                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                    activeFilter = filter
                                                }
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

                                // 100% keyboard-safe search results list (shrinks above IME + scroll dismisses keyboard)
                                LazyColumn(
                                    state = searchListState,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .nestedScroll(dismissKeyboardOnScroll),
                                    contentPadding = PaddingValues(top = 4.dp, bottom = 36.dp)
                                ) {
                                    if (searchResults.isEmpty()) {
                                        item(key = "search_empty", contentType = "empty") {
                                            EmptyActivityState(
                                                activeFilter = activeFilter,
                                                query = normalizedQuery
                                            )
                                        }
                                    } else {
                                        items(
                                            items = searchResults,
                                            key = { it.app.packageName },
                                            contentType = { "activity_app_row" }
                                        ) { item ->
                                            AppActivityRowCard(
                                                app = item.app,
                                                sharePercent = item.sharePercent,
                                                hasUsagePermission = activeSummary.hasUsagePermission,
                                                onClick = { onOpenAppLimitSheet(item.app) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        else -> {
                            val activeSummary = summary ?: return@AnimatedContent
                            val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                            LazyColumn(
                                state = dashboardListState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(top = 4.dp, bottom = 52.dp + navBottom)
                            ) {
                                // 1. Usage Access Setup Card (when permission not yet granted)
                                if (!activeSummary.hasUsagePermission) {
                                    item(key = "usage_permission_banner", contentType = "permission_banner") {
                                        StaggeredSectionReveal(stageIndex = 0) {
                                            UsageAccessPermissionCard(
                                                onOpenSettings = {
                                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                    openUsageAccessSettings(context)
                                                }
                                            )
                                        }
                                    }
                                }

                                // 2. Accessibility Reminder when limits exist but shield service isn't active
                                if (
                                    activeSummary.activeLimitsCount > 0 &&
                                    accessibilityStatus != AppBlockerManager.AccessibilityServiceStatus.READY
                                ) {
                                    item(key = "accessibility_needed_banner", contentType = "accessibility_banner") {
                                        StaggeredSectionReveal(stageIndex = 0) {
                                            AccessibilityEnforcerBanner(
                                                onEnable = {
                                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                    try {
                                                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                                    } catch (_: Exception) {}
                                                }
                                            )
                                        }
                                    }
                                }

                                // 3. Premium Minimal Analytics Suite (Hero Bento + 7-Day & 24h Graphs + Top 5 + Smart Tip)
                                if (activeSummary.hasUsagePermission) {
                                    item(key = "hero_bento_card", contentType = "hero_bento") {
                                        StaggeredSectionReveal(stageIndex = 0) {
                                            AppActivityHeroBentoCard(
                                                summary = activeSummary,
                                                scope = timeScope,
                                                selectedDayIndex = selectedDayIndex,
                                                onScopeChange = { newScope ->
                                                    timeScope = newScope
                                                    if (newScope == ActivityTimeScope.TODAY && activeSummary.weeklyDays.isNotEmpty()) {
                                                        selectedDayIndex = activeSummary.weeklyDays.lastIndex
                                                    }
                                                }
                                            )
                                        }
                                    }

                                    item(key = "weekly_graph_card", contentType = "weekly_graph") {
                                        StaggeredSectionReveal(stageIndex = 1) {
                                            WeeklyActivityGraphCard(
                                                days = activeSummary.weeklyDays,
                                                weeklyAvgMillis = activeSummary.weeklyDailyAverageMillis,
                                                selectedDayIndex = selectedDayIndex,
                                                scope = timeScope,
                                                onSelectDay = { idx ->
                                                    if (timeScope == ActivityTimeScope.TODAY && selectedDayIndex == idx) {
                                                        selectedDayIndex = activeSummary.weeklyDays.lastIndex
                                                    } else {
                                                        timeScope = ActivityTimeScope.TODAY
                                                        selectedDayIndex = idx
                                                    }
                                                }
                                            )
                                        }
                                    }

                                    item(key = "top_five_card", contentType = "top_five") {
                                        StaggeredSectionReveal(stageIndex = 2) {
                                            TopFiveAppsCard(
                                                entries = topFivePairList,
                                                totalScopeMillis = topFiveScopeTotal,
                                                scopeSubtitle = topFiveSubtitle,
                                                onSelectAppToLimit = onOpenAppLimitSheet
                                            )
                                        }
                                    }

                                    item(key = "smart_limit_tip", contentType = "smart_tip") {
                                        AnimatedVisibility(
                                            visible = smartTipCandidate != null,
                                            enter = PomoMotion.expandSection,
                                            exit = PomoMotion.collapseSection
                                        ) {
                                            val candidate = smartTipCandidate
                                            if (candidate != null) {
                                                StaggeredSectionReveal(stageIndex = 3) {
                                                    SmartLimitSuggestionCard(
                                                        topUnlimitedApp = candidate,
                                                        suggestedMinutes = smartTipSuggestedMinutes,
                                                        onQuickApply = {
                                                            AppUsageLimitManager.setAppLimitMinutes(
                                                                context,
                                                                candidate.packageName,
                                                                smartTipSuggestedMinutes
                                                            )
                                                            refreshTrigger++
                                                            Toast.makeText(
                                                                context,
                                                                "Set ${AppUsageLimitManager.formatMinutesLabel(smartTipSuggestedMinutes)} daily limit for ${candidate.label}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // 4. All Apps Section Header + Filter Chips + Tappable Search Trigger Pill
                                item(key = "all_apps_controls", contentType = "filter_search") {
                                    StaggeredSectionReveal(stageIndex = if (activeSummary.hasUsagePermission) 3 else 1) {
                                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "All Installed Apps",
                                                    fontSize = 15.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = currentTheme.textPrimary,
                                                    fontFamily = currentFont
                                                )
                                                Text(
                                                    text = "${dashboardFilteredApps.size} apps",
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = currentTheme.textSecondary,
                                                    fontFamily = MonospaceFontFamily
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            LazyRow(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                contentPadding = PaddingValues(vertical = 2.dp)
                                            ) {
                                                items(ActivityFilter.entries, key = { it.name }) { filter ->
                                                    ActivityFilterChipItem(
                                                        filter = filter,
                                                        selected = activeFilter == filter,
                                                        activeLimitsCount = activeSummary.activeLimitsCount,
                                                        onSelect = {
                                                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                            activeFilter = filter
                                                        }
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Tapping opens Pinned Search Mode so results always sit cleanly above keyboard
                                            Surface(
                                                onClick = {
                                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                    isSearchActive = true
                                                },
                                                shape = SearchCapsuleShape,
                                                color = currentTheme.surface,
                                                border = BorderStroke(1.dp, currentTheme.cardBorder),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(50.dp)
                                                    .testTag("app_activity_search_field")
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(horizontal = 14.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Search,
                                                        contentDescription = null,
                                                        tint = currentTheme.textSecondary,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = "Search ${indexedApps.size} apps to inspect or limit...",
                                                        color = currentTheme.textSecondary,
                                                        fontFamily = currentFont,
                                                        fontSize = 13.5.sp,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // 5. All Installed Apps List (fast, stable, zero per-row animation bloat)
                                if (dashboardFilteredApps.isEmpty()) {
                                    item(key = "activity_empty", contentType = "empty") {
                                        EmptyActivityState(activeFilter = activeFilter, query = "")
                                    }
                                } else {
                                    items(
                                        items = dashboardFilteredApps,
                                        key = { it.app.packageName },
                                        contentType = { "activity_app_row" }
                                    ) { item ->
                                        AppActivityRowCard(
                                            app = item.app,
                                            sharePercent = item.sharePercent,
                                            hasUsagePermission = activeSummary.hasUsagePermission,
                                            onClick = { onOpenAppLimitSheet(item.app) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Subtle 28.dp Bottom Scrim Fade (only when not typing so keyboard sits flush)
                if (!isSearchActive) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(28.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, currentTheme.background)
                                )
                            )
                    )
                }
            }
        }
    }

    // Material 3 Daily Limit Editor Bottom Sheet with Live 7-Day Threshold Preview
    val appToEdit = editingApp
    if (appToEdit != null) {
        val dayLabels = remember(summary?.weeklyDays) {
            summary?.weeklyDays?.map { it.shortDayLabel } ?: emptyList()
        }
        AppDailyLimitBottomSheet(
            app = appToEdit,
            dayLabels = dayLabels,
            onDismiss = { editingApp = null },
            onSaveLimit = { limitMins ->
                AppUsageLimitManager.setAppLimitMinutes(context, appToEdit.packageName, limitMins)
                editingApp = null
                refreshTrigger++
            },
            onRemoveLimit = {
                AppUsageLimitManager.setAppLimitMinutes(context, appToEdit.packageName, null)
                editingApp = null
                refreshTrigger++
            },
            onPreviewBlockScreen = { previewLimitMins ->
                val previewIntent = Intent(context, BlockedAppActivity::class.java).apply {
                    putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, appToEdit.packageName)
                    putExtra(BlockedAppActivity.EXTRA_PREVIEW_MODE, true)
                    putExtra(BlockedAppActivity.EXTRA_BLOCK_MODE, BlockedAppActivity.MODE_DAILY_LIMIT)
                    putExtra(BlockedAppActivity.EXTRA_LIMIT_MINUTES, previewLimitMins)
                    putExtra(
                        BlockedAppActivity.EXTRA_USED_MILLIS,
                        maxOf(appToEdit.usageMillisToday, previewLimitMins * 60_000L)
                    )
                    putExtra(BlockedAppActivity.EXTRA_EXTENSIONS_USED, appToEdit.extensionsUsedToday)
                }
                context.startActivity(previewIntent)
            }
        )
    }
}

@Composable
private fun PinnedSearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onCloseSearch: () -> Unit,
    onSubmitSearch: () -> Unit,
    focusRequester: FocusRequester
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Surface(
        color = currentTheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onCloseSearch,
                modifier = Modifier.testTag("app_activity_search_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Exit Search",
                    tint = currentTheme.textPrimary
                )
            }

            Surface(
                shape = SearchCapsuleShape,
                color = currentTheme.surface,
                border = BorderStroke(1.2.dp, currentTheme.primary.copy(alpha = 0.55f)),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 14.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = currentTheme.primary,
                        modifier = Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = "Search installed apps...",
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = query,
                            onValueChange = onQueryChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = currentTheme.textPrimary,
                                fontFamily = currentFont,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            cursorBrush = SolidColor(currentTheme.primary),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { onSubmitSearch() }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .testTag("app_activity_pinned_search_input")
                        )
                    }
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Clear search",
                                tint = currentTheme.textSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityFilterChipItem(
    filter: ActivityFilter,
    selected: Boolean,
    activeLimitsCount: Int,
    onSelect: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val labelText = remember(filter, activeLimitsCount) {
        if (filter == ActivityFilter.LIMITED && activeLimitsCount > 0) {
            "${filter.label} ($activeLimitsCount)"
        } else {
            filter.label
        }
    }

    FilterChip(
        selected = selected,
        onClick = onSelect,
        label = {
            Text(
                text = labelText,
                fontFamily = currentFont,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = currentTheme.primary.copy(alpha = 0.16f),
            selectedLabelColor = currentTheme.primary,
            containerColor = currentTheme.surface,
            labelColor = currentTheme.textPrimary
        ),
        modifier = Modifier.testTag("activity_filter_${filter.name.lowercase()}")
    )
}

@Composable
private fun EmptyActivityState(
    activeFilter: ActivityFilter,
    query: String
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.SearchOff,
            contentDescription = null,
            tint = currentTheme.textSecondary,
            modifier = Modifier.size(38.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = when {
                query.isNotEmpty() -> "No apps matching \"$query\""
                activeFilter == ActivityFilter.LIMITED -> "No daily app limits set yet."
                activeFilter == ActivityFilter.NEAR_LIMIT -> "No apps are near or over their daily limit."
                else -> "No matching apps found."
            },
            fontFamily = currentFont,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Medium,
            color = currentTheme.textSecondary
        )
    }
}

@Composable
private fun UsageAccessPermissionCard(onOpenSettings: () -> Unit) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.primary.copy(alpha = 0.09f)),
        border = BorderStroke(1.dp, currentTheme.primary.copy(alpha = 0.28f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(currentTheme.primary.copy(alpha = 0.16f)),
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
                        text = "Allow Usage Access",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont
                    )
                    Text(
                        text = "Unlock your 7-day graphs, top 5 apps, hourly rhythm, and 4:00 AM daily limits.",
                        fontSize = 12.sp,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onOpenSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("grant_usage_access_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = currentTheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Grant Usage Access",
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont
                )
            }
        }
    }
}

@Composable
private fun AccessibilityEnforcerBanner(onEnable: () -> Unit) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RowCardShape,
        colors = CardDefaults.cardColors(containerColor = WarnAmber.copy(alpha = 0.09f)),
        border = BorderStroke(1.dp, WarnAmber.copy(alpha = 0.30f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.AccessibilityNew,
                contentDescription = null,
                tint = WarnAmber,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Enable Focus Shield to Auto-Lock",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = WarnAmber,
                    fontFamily = currentFont
                )
                Text(
                    text = "Turn on Accessibility so PomoPal can lock apps & send 5-min warnings.",
                    fontSize = 11.5.sp,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont,
                    lineHeight = 15.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledTonalButton(
                onClick = onEnable,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = LimitBadgeShape,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = WarnAmber,
                    contentColor = Color.White
                )
            ) {
                Text("Enable", fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = currentFont)
            }
        }
    }
}

/**
 * Lightweight, allocation-minimal installed app row optimized for buttery 120fps scrolling
 * across long lists (100+ apps). Uses cached AppIcon bitmaps and memoized copy strings.
 */
@Composable
private fun AppActivityRowCard(
    app: AppUsageLimitManager.AppActivityEntry,
    sharePercent: Int,
    hasUsagePermission: Boolean,
    onClick: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val isOver = app.isLimitReached
    val isInGrace = app.isInGraceWindow
    val isNear = !isOver && app.dailyLimitMinutes != null &&
        ((app.remainingMillis ?: Long.MAX_VALUE) <= AppUsageLimitManager.FIVE_MINUTES_MS || app.progressFraction >= 0.75f)

    val statusColor = when {
        isInGrace -> GraceBlue
        isOver -> OverRed
        isNear -> WarnAmber
        app.dailyLimitMinutes != null -> currentTheme.primary
        else -> currentTheme.textSecondary
    }

    val borderColor = when {
        isOver -> OverRed.copy(alpha = 0.35f)
        app.dailyLimitMinutes != null -> currentTheme.primary.copy(alpha = 0.24f)
        else -> currentTheme.cardBorder.copy(alpha = 0.58f)
    }

    val usageLine = remember(app.usageMillisToday, app.weeklyTotalMillis, sharePercent, hasUsagePermission) {
        when {
            !hasUsagePermission -> "Tap to set a daily limit"
            app.usageMillisToday > 0L -> buildString {
                append("${AppUsageLimitManager.formatDurationCompact(app.usageMillisToday)} today")
                if (sharePercent > 0) append(" · $sharePercent%")
                if (app.weeklyTotalMillis > app.usageMillisToday) {
                    append(" · ${AppUsageLimitManager.formatDurationCompact(app.weeklyTotalMillis)}/wk")
                }
            }
            app.weeklyTotalMillis > 0L -> "${AppUsageLimitManager.formatDurationCompact(app.weeklyTotalMillis)} this week · 0m today"
            else -> "Not used recently"
        }
    }

    val limitBadgeText = remember(app.dailyLimitMinutes) {
        app.dailyLimitMinutes?.let { AppUsageLimitManager.formatMinutesLabel(it) } ?: "Limit"
    }

    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("app_activity_row_${app.packageName}"),
        shape = RowCardShape,
        color = currentTheme.surface,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIcon(packageName = app.packageName)

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = usageLine,
                        fontSize = 11.5.sp,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = LimitBadgeShape,
                    color = if (app.dailyLimitMinutes != null) {
                        statusColor.copy(alpha = 0.14f)
                    } else {
                        currentTheme.background
                    },
                    border = BorderStroke(
                        1.dp,
                        if (app.dailyLimitMinutes != null) statusColor.copy(alpha = 0.35f)
                        else currentTheme.cardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Timer,
                            contentDescription = null,
                            tint = if (app.dailyLimitMinutes != null) statusColor else currentTheme.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = limitBadgeText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (app.dailyLimitMinutes != null) statusColor else currentTheme.textPrimary,
                            fontFamily = currentFont
                        )
                    }
                }
            }

            if (app.dailyLimitMinutes != null) {
                Spacer(modifier = Modifier.height(10.dp))
                val safeFraction = app.progressFraction.coerceIn(
                    if (app.usageMillisToday > 0L) 0.03f else 0f,
                    1f
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.14f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(safeFraction)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .width(2.dp)
                            .fillMaxHeight()
                            .background(currentTheme.surface.copy(alpha = 0.85f))
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusCaption = remember(isInGrace, isOver, app.activeGraceExpiryMs, app.remainingMillis) {
                        when {
                            isInGrace -> {
                                val minsLeft = (((app.activeGraceExpiryMs - System.currentTimeMillis()) / 60_000L) + 1)
                                    .coerceIn(1, 5)
                                "5-min grace pass active (~${minsLeft}m left)"
                            }
                            isOver -> "Daily limit reached · Locked until 4:00 AM"
                            else -> "${AppUsageLimitManager.formatDurationCompact(app.remainingMillis ?: 0L)} remaining today"
                        }
                    }
                    Text(
                        text = statusCaption,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor,
                        fontFamily = currentFont
                    )
                    val pct = (app.progressFraction * 100f).roundToInt().coerceAtMost(100)
                    Text(
                        text = "$pct%",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.textSecondary,
                        fontFamily = MonospaceFontFamily
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppDailyLimitBottomSheet(
    app: AppUsageLimitManager.AppActivityEntry,
    dayLabels: List<String>,
    onDismiss: () -> Unit,
    onSaveLimit: (Int) -> Unit,
    onRemoveLimit: () -> Unit,
    onPreviewBlockScreen: (Int) -> Unit
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val presets = remember { listOf(15, 30, 45, 60, 90, 120) }
    var selectedMinutes by remember(app.packageName) {
        mutableIntStateOf((app.dailyLimitMinutes ?: 30).coerceIn(5, 240))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = currentTheme.surface,
        contentColor = currentTheme.textPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp)
                .padding(bottom = 22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppIcon(packageName = app.packageName)
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.label,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = buildString {
                            append("Today ${AppUsageLimitManager.formatDurationCompact(app.usageMillisToday)}")
                            if (app.weeklyTotalMillis > 0L) {
                                append(" · Avg ${AppUsageLimitManager.formatDurationCompact(app.weeklyAverageMillis)}/day")
                            }
                        },
                        fontSize = 12.sp,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont
                    )
                }
                IconButton(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                        onPreviewBlockScreen(selectedMinutes)
                    },
                    modifier = Modifier.testTag("sheet_preview_limit_block_button")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Visibility,
                        contentDescription = "Preview Lock Screen",
                        tint = currentTheme.primary
                    )
                }
            }

            if (app.weeklyDailyMillis.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                AppSevenDayLimitPreviewChart(
                    weeklyDailyMillis = app.weeklyDailyMillis,
                    dayLabels = dayLabels,
                    selectedLimitMinutes = selectedMinutes
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "Daily Time Limit",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont
                )
                Text(
                    text = AppUsageLimitManager.formatMinutesLabel(selectedMinutes),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = currentTheme.primary,
                    fontFamily = MonospaceFontFamily
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Slider(
                value = selectedMinutes.toFloat(),
                onValueChange = { raw ->
                    val snapped = ((raw / 5f).roundToInt() * 5).coerceIn(5, 240)
                    if (snapped != selectedMinutes) {
                        selectedMinutes = snapped
                    }
                },
                valueRange = 5f..240f,
                steps = 46,
                colors = SliderDefaults.colors(
                    thumbColor = currentTheme.primary,
                    activeTrackColor = currentTheme.primary,
                    inactiveTrackColor = currentTheme.cardBorder.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_limit_slider")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presets) { presetMins ->
                    val isPicked = selectedMinutes == presetMins
                    FilterChip(
                        selected = isPicked,
                        onClick = {
                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                            selectedMinutes = presetMins
                        },
                        label = {
                            Text(
                                text = AppUsageLimitManager.formatMinutesLabel(presetMins),
                                fontFamily = currentFont,
                                fontWeight = if (isPicked) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.5.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = currentTheme.primary,
                            selectedLabelColor = Color.White,
                            containerColor = currentTheme.background,
                            labelColor = currentTheme.textPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = currentTheme.background.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Rounded.NotificationsActive,
                        contentDescription = null,
                        tint = currentTheme.primary,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    val halfMins = (selectedMinutes / 2).coerceAtLeast(1)
                    val warnAtMins = (selectedMinutes - 5).coerceAtLeast(0)
                    val summaryText = if (selectedMinutes > 5) {
                        "Auto-nudges at ${AppUsageLimitManager.formatMinutesLabel(halfMins)} (50% used) & ${AppUsageLimitManager.formatMinutesLabel(warnAtMins)} (5 mins left). Locks at ${AppUsageLimitManager.formatMinutesLabel(selectedMinutes)} with a \"Just 5 mins more\" option. Resets 4:00 AM."
                    } else {
                        "Locks at 5m with a 1-minute heads-up and a \"Just 5 mins more\" option. Resets daily at 4:00 AM."
                    }
                    Text(
                        text = summaryText,
                        fontSize = 11.5.sp,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (app.dailyLimitMinutes != null) {
                    OutlinedButton(
                        onClick = {
                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                            onRemoveLimit()
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("remove_daily_limit_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.45f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remove", fontWeight = FontWeight.Bold, fontFamily = currentFont)
                    }
                }

                Button(
                    onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                        onSaveLimit(selectedMinutes)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("save_daily_limit_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentTheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Apply ${AppUsageLimitManager.formatMinutesLabel(selectedMinutes)} Limit",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = currentFont
                    )
                }
            }
        }
    }
}

private fun openUsageAccessSettings(context: Context) {
    try {
        val packageSpecific = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
        }
        context.startActivity(packageSpecific)
    } catch (_: Exception) {
        try {
            context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } catch (_: Exception) {
            Toast.makeText(context, "Usage Access settings unavailable on this device", Toast.LENGTH_SHORT).show()
        }
    }
}
