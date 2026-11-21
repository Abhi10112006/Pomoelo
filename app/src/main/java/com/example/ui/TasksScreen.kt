package com.example.ui

import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.data.TaskItem
import com.example.service.TimerManager
import com.example.service.safeParseColor
import com.example.ui.components.PinnedSearchHeader
import com.example.ui.components.PomoButton
import com.example.ui.components.SearchTriggerPill
import com.example.ui.components.rememberDismissKeyboardOnScrollConnection
import com.example.ui.components.resolveCategoryIcon
import com.example.ui.components.scaledSp
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import com.example.ui.theme.PomoMotion
import com.example.ui.theme.ensureContrast
import com.example.ui.theme.luminance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val LOGICAL_DAY_CUTOFF_HOUR = 4

@Immutable
private data class LogicalDayWindow(
    val startMillis: Long,
    val endMillis: Long
)

@Immutable
private data class CategoryChipSummary(
    val name: String,
    val count: Int,
    val colorLong: Long?
)

@Immutable
private data class TasksDashboardSummary(
    val totalTasks: Int,
    val completedTodayTotal: Int,
    val dailyCountByTaskId: Map<Int, Int>,
    val categories: List<CategoryChipSummary>
)

@Immutable
private data class IndexedTaskEntry(
    val task: TaskItem,
    val lowerName: String,
    val lowerCategory: String
)

private fun computeLogicalDayWindow(
    nowMillis: Long = System.currentTimeMillis(),
    cutoffHour: Int = LOGICAL_DAY_CUTOFF_HOUR
): LogicalDayWindow {
    val cal = Calendar.getInstance().apply {
        timeInMillis = nowMillis
        if (get(Calendar.HOUR_OF_DAY) < cutoffHour) {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        set(Calendar.HOUR_OF_DAY, cutoffHour)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startMillis = cal.timeInMillis
    cal.add(Calendar.DAY_OF_YEAR, 1)
    val endMillis = cal.timeInMillis
    return LogicalDayWindow(startMillis = startMillis, endMillis = endMillis)
}

private fun formatTaskAgeLabel(timestamp: Long, logicalDayStartMillis: Long): String {
    val dayInMillis = 24 * 60 * 60 * 1000L
    return when {
        timestamp >= logicalDayStartMillis -> "Today"
        timestamp >= logicalDayStartMillis - dayInMillis -> "Yesterday"
        else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(timestamp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScreen(
    viewModel: TimerViewModel,
    navController: NavController,
    bottomPadding: Dp = 0.dp,
    onBottomNavCollapseChange: (Boolean) -> Unit = {},
    scrollToTopTrigger: Int = 0
) {
    val view = LocalView.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val coroutineScope = rememberCoroutineScope()

    val allTasks by viewModel.allTasks.collectAsState()
    val allSessions by viewModel.allSessions.collectAsState()
    val currentTaskId by TimerManager.currentTaskId.collectAsState()

    var selectedTimerFilter by remember { mutableStateOf("All") }
    var selectedCategory by remember { mutableStateOf("All") }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val searchFocusRequester = remember { FocusRequester() }
    val searchListState = rememberLazyListState()
    val dismissKeyboardOnScroll = rememberDismissKeyboardOnScrollConnection()

    val snackbarHostState = remember { SnackbarHostState() }

    // 4:00 AM -> next 4:00 AM logical day boundary with live auto-rollover tick
    var logicalDayTick by remember { mutableIntStateOf(0) }
    val logicalDayWindow = remember(logicalDayTick, allSessions) {
        computeLogicalDayWindow()
    }
    LaunchedEffect(logicalDayWindow.endMillis) {
        val waitMs = (logicalDayWindow.endMillis - System.currentTimeMillis() + 250L)
            .coerceAtLeast(1_000L)
        delay(waitMs)
        logicalDayTick++
    }

    val spotlightEntrance = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        spotlightEntrance.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
    }

    // Compute per-task completion counts strictly within [4:00 AM .. next 4:00 AM)
    val dashboardSummary = remember(allTasks, allSessions, logicalDayWindow) {
        val sessionCountsByKey = HashMap<Pair<String, Boolean>, Int>()
        for (session in allSessions) {
            val completionTime = if (session.endTime > 0L) session.endTime else session.startTime
            if (completionTime in logicalDayWindow.startMillis until logicalDayWindow.endMillis) {
                val normalizedName = session.taskName.trim().lowercase(Locale.getDefault())
                if (normalizedName.isNotEmpty()) {
                    val key = normalizedName to session.isBreak
                    sessionCountsByKey[key] = (sessionCountsByKey[key] ?: 0) + 1
                }
            }
        }

        val dailyCountByTaskId = HashMap<Int, Int>(allTasks.size)
        val grouped = LinkedHashMap<String, MutableList<TaskItem>>()
        val countedKeys = HashSet<Pair<String, Boolean>>()
        var totalCompletedToday = 0

        for (task in allTasks) {
            val normalizedName = task.name.trim().lowercase(Locale.getDefault())
            val key = normalizedName to task.isBreakCategory
            val todayCount = sessionCountsByKey[key] ?: 0
            dailyCountByTaskId[task.id] = todayCount
            if (countedKeys.add(key)) {
                totalCompletedToday += todayCount
            }
            grouped.getOrPut(task.categoryName) { mutableListOf() }.add(task)
        }

        val chips = buildList {
            add(CategoryChipSummary(name = "All", count = allTasks.size, colorLong = null))
            grouped.forEach { (cat, items) ->
                add(
                    CategoryChipSummary(
                        name = cat,
                        count = items.size,
                        colorLong = items.firstOrNull()?.categoryColor
                    )
                )
            }
        }

        TasksDashboardSummary(
            totalTasks = allTasks.size,
            completedTodayTotal = totalCompletedToday,
            dailyCountByTaskId = dailyCountByTaskId,
            categories = chips
        )
    }

    // Reset category filter cleanly if the last task in that category was deleted
    LaunchedEffect(dashboardSummary.categories) {
        if (selectedCategory != "All" && dashboardSummary.categories.none { it.name == selectedCategory }) {
            selectedCategory = "All"
        }
    }

    val activeTask = remember(allTasks, currentTaskId) {
        allTasks.find { it.id == currentTaskId }
    }

    val indexedTasks = remember(allTasks) {
        allTasks.map { task ->
            IndexedTaskEntry(
                task = task,
                lowerName = task.name.lowercase(Locale.getDefault()),
                lowerCategory = task.categoryName.lowercase(Locale.getDefault())
            )
        }
    }

    val baseFilteredTasks = remember(indexedTasks, selectedTimerFilter, selectedCategory) {
        indexedTasks.filter { entry ->
            val matchesMode = when (selectedTimerFilter) {
                "Work" -> !entry.task.isBreakCategory
                "Break" -> entry.task.isBreakCategory
                else -> true
            }
            val matchesCategory = selectedCategory == "All" || entry.task.categoryName == selectedCategory
            matchesMode && matchesCategory
        }
    }

    // Prefix-first ranked search results (startsWith name -> contains name -> category match)
    val normalizedQuery = remember(searchQuery) { searchQuery.trim().lowercase(Locale.getDefault()) }
    val searchResults = remember(baseFilteredTasks, normalizedQuery) {
        if (normalizedQuery.isEmpty()) {
            baseFilteredTasks.map { it.task }
        } else {
            val startsWithMatches = ArrayList<TaskItem>(12)
            val containsNameMatches = ArrayList<TaskItem>(16)
            val categoryMatches = ArrayList<TaskItem>(12)
            for (entry in baseFilteredTasks) {
                val nameIdx = entry.lowerName.indexOf(normalizedQuery)
                when {
                    nameIdx == 0 -> startsWithMatches.add(entry.task)
                    nameIdx > 0 -> containsNameMatches.add(entry.task)
                    entry.lowerCategory.contains(normalizedQuery) -> categoryMatches.add(entry.task)
                }
            }
            if (containsNameMatches.isEmpty() && categoryMatches.isEmpty()) {
                startsWithMatches
            } else {
                startsWithMatches.addAll(containsNameMatches)
                startsWithMatches.addAll(categoryMatches)
                startsWithMatches
            }
        }
    }

    // Scroll-driven Bottom Nav Collapse (Silent, Bidirectional, with Bottom-of-List Bounce Guard)
    val listState = rememberLazyListState()
    val latestCollapseCallback by rememberUpdatedState(onBottomNavCollapseChange)

    val exitSearchMode: () -> Unit = {
        keyboardController?.hide()
        focusManager.clearFocus()
        searchQuery = ""
        isSearchActive = false
        latestCollapseCallback(false)
    }

    BackHandler(enabled = isSearchActive) {
        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
        exitSearchMode()
    }

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            latestCollapseCallback(true)
            delay(80)
            try {
                searchFocusRequester.requestFocus()
                keyboardController?.show()
            } catch (_: Exception) {}
        }
    }

    LaunchedEffect(isSearchActive, normalizedQuery, selectedTimerFilter, selectedCategory) {
        if (isSearchActive) {
            searchListState.scrollToItem(0)
        }
    }

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            try {
                listState.animateScrollToItem(0)
            } catch (_: Exception) {}
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 }
            .collect { atTop ->
                if (atTop) {
                    latestCollapseCallback(false)
                }
            }
    }

    var downScrollAccum by remember { mutableFloatStateOf(0f) }
    var upScrollAccum by remember { mutableFloatStateOf(0f) }

    val tasksNestedScroll = remember(listState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val dy = available.y
                if (dy < 0f) {
                    upScrollAccum = 0f
                    downScrollAccum += -dy
                    val movedAwayFromTop = listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 24
                    if (downScrollAccum > 28f && movedAwayFromTop) {
                        latestCollapseCallback(true)
                    }
                } else if (dy > 0f) {
                    downScrollAccum = 0f
                    upScrollAccum += dy
                    // Bottom-of-list overscroll bounce guard: ignore small reverse bounce when !canScrollForward
                    val upThreshold = if (!listState.canScrollForward) 48f else 16f
                    if (upScrollAccum > upThreshold) {
                        latestCollapseCallback(false)
                    }
                }
                return Offset.Zero
            }
        }
    }

    val snackbarContentColor = if (currentTheme.primary.luminance() > 0.55f) Color(0xFF1E1E1E) else Color.White
    val primaryContentColor = if (currentTheme.primary.luminance() > 0.55f) Color(0xFF1E1E1E) else Color.White

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = currentFont)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.padding(bottom = if (isSearchActive) 16.dp else bottomPadding + 84.dp)
                    ) { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = currentTheme.primaryDark,
                            contentColor = snackbarContentColor,
                            actionColor = currentTheme.accent,
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    AnimatedContent(
                        targetState = isSearchActive,
                        transitionSpec = { PomoMotion.asymmetricFadeThroughTransition() },
                        label = "TasksSearchModeTransition"
                    ) { searching ->
                        if (searching) {
                            Column(
                                modifier = Modifier
                                    .widthIn(max = 600.dp)
                                    .fillMaxSize()
                                    .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
                            ) {
                                PinnedSearchHeader(
                                    query = searchQuery,
                                    onQueryChange = { searchQuery = it },
                                    onCloseSearch = exitSearchMode,
                                    onSubmitSearch = {
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    },
                                    focusRequester = searchFocusRequester,
                                    placeholder = "Search tasks or categories...",
                                    applyStatusBarPadding = false,
                                    backTestTag = "tasks_search_back_button",
                                    inputTestTag = "tasks_pinned_search_input",
                                    clearTestTag = "tasks_search_clear"
                                )

                                // Compact Filter Strip + Live Count right beneath the Pinned Search Bar
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(modifier = Modifier.weight(1f)) {
                                            TaskTimerModeFilterRow(
                                                selectedMode = selectedTimerFilter,
                                                onSelectMode = { mode ->
                                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                    selectedTimerFilter = mode
                                                }
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "${searchResults.size} found",
                                            fontSize = 11.5.scaledSp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = currentTheme.textSecondary,
                                            fontFamily = MonospaceFontFamily
                                        )
                                    }

                                    if (dashboardSummary.categories.size > 1) {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            contentPadding = PaddingValues(horizontal = 2.dp)
                                        ) {
                                            items(
                                                items = dashboardSummary.categories,
                                                key = { it.name },
                                                contentType = { "category_chip" }
                                            ) { chip ->
                                                CategoryFilterChip(
                                                    chip = chip,
                                                    isSelected = selectedCategory == chip.name,
                                                    onClick = {
                                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                        selectedCategory = chip.name
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }

                                // Keyboard-proof search results list (sits above IME + scroll dismisses keyboard)
                                LazyColumn(
                                    state = searchListState,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .nestedScroll(dismissKeyboardOnScroll),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(
                                        start = 24.dp,
                                        end = 24.dp,
                                        top = 8.dp,
                                        bottom = 36.dp
                                    )
                                ) {
                                    if (searchResults.isEmpty()) {
                                        item(key = "tasks_search_empty", contentType = "empty_filter_state") {
                                            TasksEmptyState(
                                                isFilterEmpty = true,
                                                onPrimaryAction = {
                                                    selectedTimerFilter = "All"
                                                    selectedCategory = "All"
                                                    searchQuery = ""
                                                }
                                            )
                                        }
                                    } else {
                                        items(
                                            items = searchResults,
                                            key = { task -> task.id },
                                            contentType = { "task_card" }
                                        ) { task ->
                                            val todayCount = dashboardSummary.dailyCountByTaskId[task.id] ?: 0
                                            PremiumTaskCard(
                                                task = task,
                                                todayCompletedCount = todayCount,
                                                ageLabel = formatTaskAgeLabel(task.timestamp, logicalDayWindow.startMillis),
                                                isSelected = currentTaskId == task.id,
                                                onSelect = {
                                                    keyboardController?.hide()
                                                    focusManager.clearFocus()
                                                    viewModel.setTask(task.id, task.name, task.categoryColor, task.isBreakCategory)
                                                },
                                                onFocusNow = {
                                                    keyboardController?.hide()
                                                    focusManager.clearFocus()
                                                    latestCollapseCallback(false)
                                                    viewModel.setTask(task.id, task.name, task.categoryColor, task.isBreakCategory)
                                                    navController.navigate("home") {
                                                        popUpTo(0)
                                                        launchSingleTop = true
                                                    }
                                                },
                                                onDelete = {
                                                    val deletedTask = task
                                                    viewModel.deleteTask(deletedTask)
                                                    coroutineScope.launch {
                                                        snackbarHostState.currentSnackbarData?.dismiss()
                                                        val res = snackbarHostState.showSnackbar(
                                                            message = "Removed \"${deletedTask.name}\"",
                                                            actionLabel = "Undo",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                        if (res == SnackbarResult.ActionPerformed) {
                                                            viewModel.restoreTask(deletedTask)
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .widthIn(max = 600.dp)
                                    .fillMaxSize()
                                    .nestedScroll(tasksNestedScroll),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(
                                    start = 24.dp,
                                    end = 24.dp,
                                    top = 24.dp,
                                    bottom = bottomPadding + 96.dp
                                )
                            ) {
                                // 1. Editorial Header + Quick Search + Rounded M3 New Task CTA
                                item(key = "tasks_hero_header", contentType = "hero_header") {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 4.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Focus Tasks",
                                                fontSize = 28.scaledSp,
                                                fontFamily = currentFont,
                                                fontWeight = FontWeight.Bold,
                                                color = currentTheme.textPrimary
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            if (dashboardSummary.totalTasks == 0) {
                                                Text(
                                                    text = "Organize your goals cleanly",
                                                    fontSize = 14.scaledSp,
                                                    fontFamily = currentFont,
                                                    color = currentTheme.textSecondary
                                                )
                                            } else {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = "${dashboardSummary.totalTasks} saved",
                                                        fontSize = 13.scaledSp,
                                                        fontFamily = currentFont,
                                                        color = currentTheme.textSecondary
                                                    )
                                                    Text(
                                                        text = "•",
                                                        fontSize = 13.scaledSp,
                                                        color = currentTheme.textSecondary.copy(alpha = 0.6f)
                                                    )
                                                    Icon(
                                                        imageVector = Icons.Rounded.CheckCircle,
                                                        contentDescription = null,
                                                        tint = ensureContrast(currentTheme.secondary, currentTheme.background, minContrast = 3.0),
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                    Text(
                                                        text = "${dashboardSummary.completedTodayTotal} today",
                                                        fontSize = 13.scaledSp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontFamily = currentFont,
                                                        color = currentTheme.textSecondary
                                                    )
                                                }
                                            }
                                        }

                                        if (allTasks.isNotEmpty()) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Surface(
                                                onClick = {
                                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                    isSearchActive = true
                                                },
                                                shape = CircleShape,
                                                color = currentTheme.surface,
                                                border = BorderStroke(1.dp, currentTheme.cardBorder),
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .testTag("tasks_top_search_button")
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Search,
                                                        contentDescription = "Search tasks",
                                                        tint = currentTheme.textPrimary,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        PomoButton(
                                            onClick = { viewModel.setAddingTask(true) },
                                            modifier = Modifier.testTag("tasks_add_button"),
                                            containerColor = currentTheme.primary,
                                            contentColor = primaryContentColor,
                                            shape = RoundedCornerShape(20.dp),
                                            elevation = 5.dp,
                                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Add,
                                                contentDescription = null,
                                                tint = primaryContentColor,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "New Task",
                                                color = primaryContentColor,
                                                fontSize = 14.scaledSp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = currentFont
                                            )
                                        }
                                    }
                                }

                                // 2. Active Task Spotlight & Today's Overview Card
                                item(key = "tasks_spotlight", contentType = "spotlight_card") {
                                    Box(modifier = Modifier.padding(top = 4.dp)) {
                                        ActiveTaskSpotlightCard(
                                            activeTask = activeTask,
                                            activeTaskTodayCount = activeTask?.let { dashboardSummary.dailyCountByTaskId[it.id] ?: 0 } ?: 0,
                                            summary = dashboardSummary,
                                            entranceProgress = { spotlightEntrance.value },
                                            onGoToTimer = {
                                                try {
                                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                                } catch (_: Exception) {}
                                                navController.navigate("home") {
                                                    popUpTo(0)
                                                    launchSingleTop = true
                                                }
                                            },
                                            onClearActive = {
                                                try {
                                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                                } catch (_: Exception) {}
                                                val isBreak = TimerManager.isBreakMode.value
                                                TimerManager.setTask(-1, if (isBreak) "Break Time!" else "Focus Time!")
                                            }
                                        )
                                    }
                                }

                                // 3. Mode Filter Strip, Category Chips & Search Trigger Pill (scrolls cleanly with list)
                                if (allTasks.isNotEmpty()) {
                                    item(key = "tasks_mode_filter", contentType = "mode_filter") {
                                        TaskTimerModeFilterRow(
                                            selectedMode = selectedTimerFilter,
                                            onSelectMode = { mode ->
                                                try {
                                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                                } catch (_: Exception) {}
                                                selectedTimerFilter = mode
                                            }
                                        )
                                    }

                                    item(key = "tasks_category_chips", contentType = "category_chips_row") {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            contentPadding = PaddingValues(horizontal = 2.dp)
                                        ) {
                                            items(
                                                items = dashboardSummary.categories,
                                                key = { it.name },
                                                contentType = { "category_chip" }
                                            ) { chip ->
                                                CategoryFilterChip(
                                                    chip = chip,
                                                    isSelected = selectedCategory == chip.name,
                                                    onClick = {
                                                        try {
                                                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                                        } catch (_: Exception) {}
                                                        selectedCategory = chip.name
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    item(key = "tasks_search_bar", contentType = "search_bar") {
                                        SearchTriggerPill(
                                            placeholder = "Search ${allTasks.size} saved tasks...",
                                            onClick = {
                                                try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                                isSearchActive = true
                                            },
                                            testTagName = "tasks_search_input"
                                        )
                                    }
                                }

                                // 4. Persistent Task Rows with PomoMotion.defaultSpatial Insertion & Reordering
                                if (allTasks.isEmpty()) {
                                    item(key = "tasks_empty", contentType = "empty_state") {
                                        TasksEmptyState(
                                            isFilterEmpty = false,
                                            onPrimaryAction = { viewModel.setAddingTask(true) }
                                        )
                                    }
                                } else if (baseFilteredTasks.isEmpty()) {
                                    item(key = "tasks_filter_empty", contentType = "empty_filter_state") {
                                        TasksEmptyState(
                                            isFilterEmpty = true,
                                            onPrimaryAction = {
                                                selectedTimerFilter = "All"
                                                selectedCategory = "All"
                                                searchQuery = ""
                                            }
                                        )
                                    }
                                } else {
                                    items(
                                        items = baseFilteredTasks,
                                        key = { entry -> entry.task.id },
                                        contentType = { "task_card" }
                                    ) { entry ->
                                        val task = entry.task
                                        val todayCount = dashboardSummary.dailyCountByTaskId[task.id] ?: 0
                                        PremiumTaskCard(
                                            task = task,
                                            todayCompletedCount = todayCount,
                                            ageLabel = formatTaskAgeLabel(task.timestamp, logicalDayWindow.startMillis),
                                            isSelected = currentTaskId == task.id,
                                            onSelect = {
                                                viewModel.setTask(task.id, task.name, task.categoryColor, task.isBreakCategory)
                                            },
                                            onFocusNow = {
                                                viewModel.setTask(task.id, task.name, task.categoryColor, task.isBreakCategory)
                                                navController.navigate("home") {
                                                    popUpTo(0)
                                                    launchSingleTop = true
                                                }
                                            },
                                            onDelete = {
                                                val deletedTask = task
                                                viewModel.deleteTask(deletedTask)
                                                coroutineScope.launch {
                                                    snackbarHostState.currentSnackbarData?.dismiss()
                                                    val res = snackbarHostState.showSnackbar(
                                                        message = "Removed \"${deletedTask.name}\"",
                                                        actionLabel = "Undo",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                    if (res == SnackbarResult.ActionPerformed) {
                                                        viewModel.restoreTask(deletedTask)
                                                    }
                                                }
                                            },
                                            modifier = Modifier.animateItem(
                                                fadeInSpec = PomoMotion.defaultSpatial(),
                                                placementSpec = PomoMotion.intOffsetSpatial,
                                                fadeOutSpec = PomoMotion.fastEffects()
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Subtle 32.dp Bottom Scrim so scrolled task cards dissolve cleanly behind the floating pill
            if (!isSearchActive) {
                val bottomScrimBrush = remember(currentTheme.background) {
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, currentTheme.background)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(bottomScrimBrush)
                )
            }
        }
    }
}

@Composable
private fun ActiveTaskSpotlightCard(
    activeTask: TaskItem?,
    activeTaskTodayCount: Int,
    summary: TasksDashboardSummary,
    entranceProgress: () -> Float,
    onGoToTimer: () -> Unit,
    onClearActive: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val rawActiveColor = activeTask?.let { safeParseColor(it.categoryColor, currentTheme.primary) }
        ?: currentTheme.primary
    val activeColor = ensureContrast(rawActiveColor, currentTheme.surface, minContrast = 3.0)

    val cardBg by animateColorAsState(
        targetValue = if (activeTask != null) {
            activeColor.copy(alpha = if (currentTheme.isDark) 0.16f else 0.09f)
                .compositeOver(currentTheme.surface)
        } else {
            currentTheme.surface
        },
        animationSpec = PomoMotion.colorSpec,
        label = "spotlightBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (activeTask != null) activeColor.copy(alpha = 0.5f) else currentTheme.cardBorder,
        animationSpec = PomoMotion.colorSpec,
        label = "spotlightBorder"
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                val p = entranceProgress()
                alpha = p.coerceIn(0f, 1f)
                val s = 0.95f + (0.05f * p)
                scaleX = s
                scaleY = s
                translationY = (1f - p) * 12.dp.toPx()
                compositingStrategy = if (p < 0.999f) CompositingStrategy.Offscreen else CompositingStrategy.Auto
            }
            .shadow(10.dp, RoundedCornerShape(24.dp), spotColor = currentTheme.shadowColor, clip = false)
    ) {
        AnimatedContent(
            targetState = activeTask,
            transitionSpec = { PomoMotion.verticalCrossfadeTransition(upward = targetState != null) },
            label = "activeSpotlightContent"
        ) { armedTask ->
            if (armedTask != null) {
                val rawTaskAccent = safeParseColor(armedTask.categoryColor, currentTheme.primary)
                val taskAccent = ensureContrast(rawTaskAccent, cardBg, minContrast = 3.0)
                val accentContent = if (rawTaskAccent.luminance() > 0.55f) Color(0xFF1E1E1E) else Color.White
                val modeTint = ensureContrast(
                    if (armedTask.isBreakCategory) currentTheme.secondary else rawTaskAccent,
                    cardBg,
                    minContrast = 3.0
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(taskAccent.copy(alpha = 0.18f))
                            .border(1.dp, taskAccent.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = resolveCategoryIcon(armedTask.categoryName),
                            contentDescription = null,
                            tint = taskAccent,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (armedTask.isBreakCategory) Icons.Rounded.Coffee else Icons.Rounded.Timer,
                                contentDescription = null,
                                tint = modeTint,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (armedTask.isBreakCategory) "ARMED FOR BREAK TIMER" else "ARMED FOR WORK TIMER",
                                fontSize = 10.scaledSp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp,
                                fontFamily = currentFont,
                                color = modeTint,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = armedTask.name,
                            fontSize = 17.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            color = currentTheme.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = armedTask.categoryName,
                                fontSize = 12.scaledSp,
                                fontFamily = currentFont,
                                color = currentTheme.textSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "•",
                                fontSize = 12.scaledSp,
                                color = currentTheme.textSecondary.copy(alpha = 0.6f)
                            )
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = taskAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "$activeTaskTodayCount today",
                                fontSize = 12.scaledSp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = currentFont,
                                color = currentTheme.textSecondary
                            )
                        }
                    }
                    IconButton(
                        onClick = onClearActive,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("tasks_clear_active_button")
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Clear active task",
                            tint = currentTheme.textSecondary
                        )
                    }
                    PomoButton(
                        onClick = onGoToTimer,
                        modifier = Modifier.testTag("tasks_go_to_timer_button"),
                        containerColor = rawTaskAccent,
                        contentColor = accentContent,
                        shape = RoundedCornerShape(16.dp),
                        elevation = 3.dp,
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Timer",
                            fontSize = 13.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            color = accentContent
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = accentContent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                val primaryMetricColor = ensureContrast(currentTheme.primary, cardBg, minContrast = 3.0)
                val secondaryMetricColor = ensureContrast(currentTheme.secondary, cardBg, minContrast = 3.0)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "DAILY FOCUS TRACKER",
                            fontSize = 10.scaledSp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            fontFamily = currentFont,
                            color = primaryMetricColor
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (summary.totalTasks == 0) "No tasks saved yet" else "Tap a task to arm timer",
                            fontSize = 16.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            color = currentTheme.textPrimary
                        )
                        Text(
                            text = "Daily counts reset at 4:00 AM",
                            fontSize = 12.scaledSp,
                            fontFamily = currentFont,
                            color = currentTheme.textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${summary.totalTasks}",
                                fontSize = 22.scaledSp,
                                fontWeight = FontWeight.Black,
                                fontFamily = MonospaceFontFamily,
                                color = primaryMetricColor
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Checklist,
                                    contentDescription = null,
                                    tint = currentTheme.textSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "TASKS",
                                    fontSize = 10.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp,
                                    fontFamily = currentFont,
                                    color = currentTheme.textSecondary
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(30.dp)
                                .background(currentTheme.cardBorder)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${summary.completedTodayTotal}",
                                fontSize = 22.scaledSp,
                                fontWeight = FontWeight.Black,
                                fontFamily = MonospaceFontFamily,
                                color = secondaryMetricColor
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = secondaryMetricColor,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "TODAY",
                                    fontSize = 10.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp,
                                    fontFamily = currentFont,
                                    color = currentTheme.textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Immutable
private data class TaskModeOption(
    val key: String,
    val label: String,
    val icon: ImageVector
)

@Composable
private fun TaskTimerModeFilterRow(
    selectedMode: String,
    onSelectMode: (String) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val options = remember {
        listOf(
            TaskModeOption("All", "All", Icons.Rounded.Apps),
            TaskModeOption("Work", "Work", Icons.Rounded.Timer),
            TaskModeOption("Break", "Break", Icons.Rounded.Coffee)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(currentTheme.backgroundSecondary.copy(alpha = 0.72f))
            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { option ->
            val selected = selectedMode == option.key
            val pillAccent = when (option.key) {
                "Break" -> ensureContrast(currentTheme.secondary, currentTheme.surface, minContrast = 3.0)
                "Work" -> ensureContrast(currentTheme.primary, currentTheme.surface, minContrast = 3.0)
                else -> currentTheme.textPrimary
            }
            val bgColor by animateColorAsState(
                targetValue = if (selected) currentTheme.surface else Color.Transparent,
                animationSpec = PomoMotion.colorSpec,
                label = "taskModeFilterBg_${option.key}"
            )
            val borderCol by animateColorAsState(
                targetValue = if (selected) pillAccent.copy(alpha = 0.45f) else Color.Transparent,
                animationSpec = PomoMotion.colorSpec,
                label = "taskModeFilterBorder_${option.key}"
            )
            val iconTint by animateColorAsState(
                targetValue = if (selected) pillAccent else currentTheme.textSecondary,
                animationSpec = PomoMotion.colorSpec,
                label = "taskModeFilterTint_${option.key}"
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true),
                        onClick = { onSelectMode(option.key) }
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .testTag("tasks_mode_filter_${option.key}"),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = option.label,
                    fontSize = 12.scaledSp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    fontFamily = currentFont,
                    color = if (selected) currentTheme.textPrimary else currentTheme.textSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun CategoryFilterChip(
    chip: CategoryChipSummary,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val rawChipColor = chip.colorLong?.let { safeParseColor(it, currentTheme.primary) } ?: currentTheme.primary
    val chipColor = ensureContrast(rawChipColor, currentTheme.surface, minContrast = 3.0)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = PomoMotion.pressScaleSpec,
        label = "chipScale"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) {
            chipColor.copy(alpha = if (currentTheme.isDark) 0.24f else 0.16f).compositeOver(currentTheme.surface)
        } else {
            currentTheme.surface
        },
        animationSpec = PomoMotion.colorSpec,
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) chipColor else currentTheme.cardBorder,
        animationSpec = PomoMotion.colorSpec,
        label = "chipBorder"
    )

    Row(
        modifier = Modifier
            .height(40.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 13.dp)
            .testTag("task_filter_${chip.name}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = if (chip.colorLong == null) Icons.Rounded.GridView else resolveCategoryIcon(chip.name),
            contentDescription = null,
            tint = if (isSelected || chip.colorLong != null) chipColor else currentTheme.textSecondary,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = chip.name,
            fontSize = 13.scaledSp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontFamily = currentFont,
            color = if (isSelected) currentTheme.textPrimary else currentTheme.textSecondary
        )
        Text(
            text = "${chip.count}",
            fontSize = 11.scaledSp,
            fontWeight = FontWeight.Bold,
            fontFamily = MonospaceFontFamily,
            color = if (isSelected) chipColor else currentTheme.textSecondary.copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PremiumTaskCard(
    task: TaskItem,
    todayCompletedCount: Int,
    ageLabel: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onFocusNow: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val rawTaskColor = safeParseColor(task.categoryColor, currentTheme.primary)
    val taskColor = ensureContrast(rawTaskColor, currentTheme.surface, minContrast = 3.0)
    val modeColor = ensureContrast(
        if (task.isBreakCategory) currentTheme.secondary else currentTheme.primary,
        currentTheme.surface,
        minContrast = 3.0
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) taskColor else currentTheme.cardBorder,
        animationSpec = PomoMotion.colorSpec,
        label = "taskBorder"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 1.5.dp else 1.dp,
        animationSpec = PomoMotion.dpSpec,
        label = "taskBorderW"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) {
            taskColor.copy(alpha = if (currentTheme.isDark) 0.16f else 0.08f).compositeOver(currentTheme.surface)
        } else {
            currentTheme.surface
        },
        animationSpec = PomoMotion.colorSpec,
        label = "taskContainer"
    )
    val rowInteraction = remember { MutableInteractionSource() }
    val isPressed by rowInteraction.collectIsPressedAsState()
    val rowScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = PomoMotion.pressScaleSpec,
        label = "taskCardScale"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = rowScale
                scaleY = rowScale
            }
            .shadow(
                elevation = if (isSelected) 6.dp else 3.dp,
                shape = RoundedCornerShape(22.dp),
                spotColor = currentTheme.shadowColor
            )
            .border(borderWidth, borderColor, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .clickable(
                interactionSource = rowInteraction,
                indication = ripple(bounded = true)
            ) {
                try {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (_: Exception) {}
                onSelect()
            }
            .testTag("task_card_${task.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rounded filled category icon + color mark badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(taskColor.copy(alpha = 0.16f))
                    .border(1.dp, taskColor.copy(alpha = 0.35f), RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = resolveCategoryIcon(task.categoryName),
                    contentDescription = null,
                    tint = taskColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    fontSize = 15.scaledSp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont,
                    color = currentTheme.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = taskColor.copy(alpha = 0.14f),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = task.categoryName,
                            fontSize = 11.scaledSp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = currentFont,
                            color = currentTheme.textPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = modeColor.copy(alpha = 0.14f),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (task.isBreakCategory) Icons.Rounded.Coffee else Icons.Rounded.Timer,
                                contentDescription = null,
                                tint = modeColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (task.isBreakCategory) "Break" else "Work",
                                fontSize = 10.scaledSp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont,
                                color = modeColor
                            )
                        }
                    }
                    if (todayCompletedCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = taskColor.copy(alpha = if (currentTheme.isDark) 0.24f else 0.16f),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .testTag("task_today_count_${task.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CheckCircle,
                                    contentDescription = "Completed $todayCompletedCount times today",
                                    tint = taskColor,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "×$todayCompletedCount",
                                    fontSize = 11.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MonospaceFontFamily,
                                    color = currentTheme.textPrimary
                                )
                            }
                        }
                    }
                    Text(
                        text = ageLabel,
                        fontSize = 11.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textSecondary,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            // Quick launch to timer or active state badge + delete
            IconButton(
                onClick = {
                    try {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (_: Exception) {}
                    onFocusNow()
                },
                modifier = Modifier
                    .size(48.dp)
                    .testTag("task_focus_${task.id}")
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.PlayArrow,
                    contentDescription = if (isSelected) {
                        "Active task, open timer"
                    } else if (task.isBreakCategory) {
                        "Select and start Break Timer"
                    } else {
                        "Select and start Work Timer"
                    },
                    tint = if (isSelected) taskColor else modeColor,
                    modifier = Modifier.size(23.dp)
                )
            }

            if (!isSelected) {
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (_: Exception) {}
                        onDelete()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("task_delete_${task.id}")
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete task",
                        tint = ensureContrast(Color(0xFFE57373), containerColor, minContrast = 3.0),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TasksEmptyState(
    isFilterEmpty: Boolean,
    onPrimaryAction: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val primaryContentColor = if (currentTheme.primary.luminance() > 0.55f) {
        Color(0xFF1E1E1E)
    } else {
        Color.White
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface.copy(alpha = 0.86f)),
        border = BorderStroke(1.dp, currentTheme.cardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                currentTheme.primary.copy(alpha = 0.22f),
                                currentTheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFilterEmpty) Icons.Rounded.SearchOff else Icons.Rounded.TaskAlt,
                    contentDescription = null,
                    tint = currentTheme.primary,
                    modifier = Modifier.size(34.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isFilterEmpty) "No matching tasks" else "Your task canvas is clear",
                fontSize = 18.scaledSp,
                fontWeight = FontWeight.Bold,
                fontFamily = currentFont,
                color = currentTheme.textPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (isFilterEmpty) {
                    "Try clearing your search or switching category filters."
                } else {
                    "Create focus tasks here anytime — they stay saved across days and track daily completions from 4:00 AM to 4:00 AM."
                },
                fontSize = 13.scaledSp,
                fontFamily = currentFont,
                color = currentTheme.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (isFilterEmpty) {
                TextButton(
                    onClick = onPrimaryAction,
                    modifier = Modifier.testTag("tasks_reset_filters_button")
                ) {
                    Text(
                        text = "Reset Filters",
                        color = currentTheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont
                    )
                }
            } else {
                PomoButton(
                    text = "Create First Task",
                    onClick = onPrimaryAction,
                    modifier = Modifier.testTag("tasks_empty_add_button"),
                    containerColor = currentTheme.primary,
                    contentColor = primaryContentColor,
                    shape = RoundedCornerShape(18.dp),
                    elevation = 4.dp
                )
            }
        }
    }
}
