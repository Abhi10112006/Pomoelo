package com.example.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.data.TaskItem
import com.example.data.TimerSession
import com.example.service.SettingsManager
import com.example.ui.components.CategoryShareSlice
import com.example.ui.components.DailyFocusGoalDialog
import com.example.ui.components.DailyPulseBar
import com.example.ui.components.HistoryDailyDashboardCard
import com.example.ui.components.HistoryDashboardTelemetry
import com.example.ui.components.formatCompactMinutes
import com.example.ui.components.scaledSp
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import com.example.ui.theme.PomoMotion
import com.example.ui.theme.ThemeOption
import com.example.ui.theme.ensureContrast
import com.example.ui.theme.luminance
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private enum class HistoryModeFilter(val label: String) {
    ALL("All"),
    FOCUS("Focus"),
    REST("Rest")
}

@Immutable
private data class HistoryTimelineData(
    val allSessionCount: Int,
    val focusSessionCount: Int,
    val restSessionCount: Int,
    val sessionsByDay: Map<Long, List<TimerSession>>,
    val recentDays: List<Long>,
    val recentGroupedEntries: Map<Long, List<Pair<String, List<TimerSession>>>>,
    val pastMonthDays: List<Long>,
    val archiveEntries: List<Pair<String, List<TimerSession>>>
)

fun getStartOfDay(timeStr: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timeStr
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

fun getDayLabel(startTime: Long, todayStart: Long, dayInMillis: Long): String {
    val startOfDay = getStartOfDay(startTime)
    val diffDays = ((todayStart - startOfDay) / dayInMillis).toInt()
    val dateStr = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(startTime))
    return when (diffDays) {
        0 -> "Today ($dateStr)"
        1 -> "Yesterday ($dateStr)"
        else -> dateStr
    }
}

fun Modifier.historyCardShadow(
    cornerRadius: Dp = 24.dp,
    shadowColor: Color
): Modifier = this.shadow(
    elevation = 12.dp,
    shape = RoundedCornerShape(cornerRadius),
    ambientColor = shadowColor,
    spotColor = shadowColor,
    clip = false
)

fun safeParseColor(colorLong: Long?, fallback: Color): Color {
    return com.example.service.safeParseColor(colorLong, fallback)
}

fun resolveTaskColor(
    taskName: String,
    sessions: List<TimerSession>,
    allTasks: List<TaskItem>,
    currentTheme: ThemeOption
): Color {
    val isDefaultTask = taskName.isBlank() || taskName == "Focus Time!" || taskName == "Deep Focus Session"
    if (isDefaultTask) return ensureContrast(currentTheme.primary, currentTheme.surface, minContrast = 3.0)

    val isDefaultBreak = taskName == "Break Time!" || taskName == "Break" || taskName == "Long Break"
    if (isDefaultBreak) return ensureContrast(currentTheme.secondary, currentTheme.surface, minContrast = 3.0)

    val targetSession = sessions
        .filter { it.taskName == taskName && it.taskColor != null }
        .maxByOrNull { it.startTime }

    val rawColor = when {
        targetSession?.taskColor != null -> safeParseColor(targetSession.taskColor, currentTheme.primary)
        else -> allTasks.find { it.name == taskName }
            ?.let { safeParseColor(it.categoryColor, currentTheme.textSecondary) }
            ?: currentTheme.textSecondary
    }
    return ensureContrast(rawColor, currentTheme.surface, minContrast = 3.0)
}

private fun resolveTaskColorLong(
    taskName: String,
    sessions: List<TimerSession>,
    allTasks: List<TaskItem>
): Long? {
    val sessionColor = sessions
        .filter { it.taskName == taskName && it.taskColor != null }
        .maxByOrNull { it.startTime }
        ?.taskColor
    if (sessionColor != null) return sessionColor
    return allTasks.find { it.name == taskName }?.categoryColor
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    viewModel: TimerViewModel,
    navController: NavController,
    bottomPadding: Dp = 0.dp,
    onBottomNavCollapseChange: (Boolean) -> Unit = {},
    scrollToTopTrigger: Int = 0
) {
    val view = LocalView.current
    val allSessions by viewModel.allSessions.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    val dailyGoalMinutes by SettingsManager.dailyFocusGoalState.collectAsState()

    val dayInMillis = 24 * 60 * 60 * 1000L
    val todayStart = remember(allSessions) { getStartOfDay(System.currentTimeMillis()) }

    val snackbarHostState = remember { SnackbarHostState() }
    var temporarilyDeletedGroupKeys by remember { mutableStateOf(setOf<String>()) }
    var selectedFilterMode by remember { mutableStateOf(HistoryModeFilter.ALL) }
    var showGoalDialog by remember { mutableStateOf(false) }

    // Tier 2 Deletion States
    var showTier2Dialog by remember { mutableStateOf(false) }
    var tier2Title by remember { mutableStateOf("") }
    var tier2SessionsToDelete by remember { mutableStateOf<List<TimerSession>>(emptyList()) }

    // Tier 3 Deletion States
    var showTier3Dialog by remember { mutableStateOf(false) }
    var tier3MonthStr by remember { mutableStateOf("") }
    var tier3SessionsToDelete by remember { mutableStateOf<List<TimerSession>>(emptyList()) }
    var tier3InputText by remember { mutableStateOf("") }

    // 1. Dashboard Telemetry (strictly independent of filter mode & timer ticks)
    val dashboardTelemetry = remember(allSessions, temporarilyDeletedGroupKeys, allTasks, todayStart) {
        val visible = allSessions.filter { session ->
            val dateMillis = getStartOfDay(session.startTime)
            val groupKey = "${session.taskName}|$dateMillis"
            !temporarilyDeletedGroupKeys.contains(groupKey)
        }

        val byDay = visible.groupBy { getStartOfDay(it.startTime) }
        val todaySessions = byDay[todayStart].orEmpty()
        val yesterdayStart = todayStart - dayInMillis
        val yesterdaySessions = byDay[yesterdayStart].orEmpty()

        val todayPomos = todaySessions.count { !it.isBreak }
        val todayFocusMins = todaySessions.filter { !it.isBreak }.sumOf { it.durationMinutes }
        val todayBreakMins = todaySessions.filter { it.isBreak }.sumOf { it.durationMinutes }
        val yesterdayFocusMins = yesterdaySessions.filter { !it.isBreak }.sumOf { it.durationMinutes }

        // Trailing 7 days ending Today
        val shortFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val fullFormat = SimpleDateFormat("MMM d", Locale.getDefault())
        val weeklyList = (6 downTo 0).map { offset ->
            val dayStart = todayStart - offset * dayInMillis
            val daySessions = byDay[dayStart].orEmpty()
            DailyPulseBar(
                dayStartMillis = dayStart,
                shortDayLabel = shortFormat.format(Date(dayStart)),
                fullDateLabel = fullFormat.format(Date(dayStart)),
                focusMinutes = daySessions.filter { !it.isBreak }.sumOf { it.durationMinutes },
                restMinutes = daySessions.filter { it.isBreak }.sumOf { it.durationMinutes },
                sessionCount = daySessions.count { !it.isBreak },
                isToday = offset == 0
            )
        }
        val weeklyFocusTotal = weeklyList.sumOf { it.focusMinutes }
        val weeklyAvg = weeklyFocusTotal / 7

        // Active Focus Streak (consecutive days with >= 1 focus session)
        val focusDaySet = visible.asSequence()
            .filter { !it.isBreak }
            .map { getStartOfDay(it.startTime) }
            .toHashSet()
        var streak = 0
        var cursorDay = if (focusDaySet.contains(todayStart)) todayStart else yesterdayStart
        while (focusDaySet.contains(cursorDay)) {
            streak++
            cursorDay -= dayInMillis
        }

        // Today's task/category distribution
        val shares = todaySessions
            .groupBy { it.taskName.ifBlank { if (it.isBreak) "Break Time!" else "Focus Time!" } }
            .entries
            .map { (name, list) ->
                CategoryShareSlice(
                    label = name,
                    minutes = list.sumOf { it.durationMinutes },
                    colorLong = resolveTaskColorLong(name, list, allTasks),
                    isBreak = list.all { it.isBreak }
                )
            }
            .filter { it.minutes > 0 }
            .sortedByDescending { it.minutes }

        HistoryDashboardTelemetry(
            todayPomodoros = todayPomos,
            todayFocusMinutes = todayFocusMins,
            todayBreakMinutes = todayBreakMins,
            yesterdayFocusMinutes = yesterdayFocusMins,
            deltaVsYesterdayMinutes = todayFocusMins - yesterdayFocusMins,
            weeklyFocusMinutes = weeklyFocusTotal,
            weeklyDailyAvgMinutes = weeklyAvg,
            currentStreakDays = streak,
            weeklyDays = weeklyList,
            categoryShares = shares
        )
    }

    // 2. Filtered Timeline Pipeline
    val historyTimeline = remember(allSessions, temporarilyDeletedGroupKeys, selectedFilterMode, todayStart) {
        val visible = allSessions.filter { session ->
            val dateMillis = getStartOfDay(session.startTime)
            val groupKey = "${session.taskName}|$dateMillis"
            !temporarilyDeletedGroupKeys.contains(groupKey)
        }
        val focusCount = visible.count { !it.isBreak }
        val restCount = visible.count { it.isBreak }

        val filtered = when (selectedFilterMode) {
            HistoryModeFilter.ALL -> visible
            HistoryModeFilter.FOCUS -> visible.filter { !it.isBreak }
            HistoryModeFilter.REST -> visible.filter { it.isBreak }
        }

        val byDay = filtered.sortedByDescending { it.startTime }.groupBy { getStartOfDay(it.startTime) }
        val recent = mutableListOf<Long>()
        val pastMonth = mutableListOf<Long>()
        val archives = LinkedHashMap<String, MutableList<TimerSession>>()
        val monthFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

        byDay.forEach { (startOfDay, sessions) ->
            val diffDays = ((todayStart - startOfDay) / dayInMillis).toInt()
            if (diffDays <= 7) {
                recent.add(startOfDay)
            } else if (diffDays <= 30) {
                pastMonth.add(startOfDay)
            } else {
                val monthStr = monthFormat.format(Date(startOfDay))
                archives.getOrPut(monthStr) { mutableListOf() }.addAll(sessions)
            }
        }

        val groupedByDayTask = recent.associateWith { dateMillis ->
            (byDay[dateMillis] ?: emptyList())
                .groupBy { it.taskName }
                .entries
                .map { it.key to it.value }
        }

        HistoryTimelineData(
            allSessionCount = visible.size,
            focusSessionCount = focusCount,
            restSessionCount = restCount,
            sessionsByDay = byDay,
            recentDays = recent,
            recentGroupedEntries = groupedByDayTask,
            pastMonthDays = pastMonth,
            archiveEntries = archives.entries.map { it.key to it.value.toList() }
        )
    }

    // 3. Scroll-driven Bottom Nav Collapse (Silent, Bidirectional, with Bottom-of-List Bounce Guard)
    val listState = rememberLazyListState()
    val latestCollapseCallback by rememberUpdatedState(onBottomNavCollapseChange)

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

    val historyNestedScroll = remember(listState) {
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

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val todayHeaderDate = remember(todayStart) {
        SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date(todayStart))
    }

    val snackbarContentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
    val headerAccentBrush = remember(currentTheme.primary, currentTheme.secondary, currentTheme.accent) {
        Brush.horizontalGradient(
            colors = listOf(currentTheme.primary, currentTheme.secondary, currentTheme.accent)
        )
    }

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
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(bottom = bottomPadding + 16.dp)
                ) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = currentTheme.primary,
                        contentColor = snackbarContentColor,
                        actionColor = snackbarContentColor
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
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxSize()
                        .nestedScroll(historyNestedScroll),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = bottomPadding + 96.dp
                    )
                ) {
                    // Editorial Header
                    item(key = "hero_header", contentType = "hero_header") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Focus Journal",
                                fontSize = 28.scaledSp,
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(4.dp)
                                        .clip(CircleShape)
                                        .background(headerAccentBrush)
                                )
                                Text(
                                    text = todayHeaderDate,
                                    fontSize = 14.scaledSp,
                                    fontFamily = currentFont,
                                    color = currentTheme.textSecondary
                                )
                            }
                        }
                    }

                    // Dynamic Focus-vs-Rest Dual-Arc Daily Dashboard
                    item(key = "daily_dashboard", contentType = "daily_dashboard") {
                        HistoryDailyDashboardCard(
                            telemetry = dashboardTelemetry,
                            dailyGoalMinutes = dailyGoalMinutes,
                            onEditGoalClick = { showGoalDialog = true },
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                    }

                    // Mode Filter Chips
                    item(key = "mode_filter_strip", contentType = "filter_strip") {
                        HistoryModeFilterRow(
                            selectedMode = selectedFilterMode,
                            allCount = historyTimeline.allSessionCount,
                            focusCount = historyTimeline.focusSessionCount,
                            restCount = historyTimeline.restSessionCount,
                            onSelectMode = { mode ->
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                selectedFilterMode = mode
                            }
                        )
                    }

                    val sessionsByDay = historyTimeline.sessionsByDay
                    val recentDays = historyTimeline.recentDays
                    val pastMonthDays = historyTimeline.pastMonthDays
                    val archiveEntries = historyTimeline.archiveEntries

                    if (sessionsByDay.isEmpty()) {
                        item(key = "history_empty", contentType = "empty_state") {
                            HistoryEmptyState(
                                isFilterEmpty = historyTimeline.allSessionCount > 0 && selectedFilterMode != HistoryModeFilter.ALL,
                                activeFilterLabel = selectedFilterMode.label,
                                onResetFilter = {
                                    try {
                                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (_: Exception) {}
                                    selectedFilterMode = HistoryModeFilter.ALL
                                }
                            )
                        }
                    } else {
                        // 1. Recent Days (0-7 days ago)
                        recentDays.forEach { dateMillis ->
                            stickyHeader(key = "hdr_$dateMillis", contentType = "recent_header") {
                                val dateSessions = sessionsByDay[dateMillis] ?: emptyList()
                                val dayFocusMins = dateSessions.filter { !it.isBreak }.sumOf { it.durationMinutes }
                                val dayRestMins = dateSessions.filter { it.isBreak }.sumOf { it.durationMinutes }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(currentTheme.background)
                                        .padding(vertical = 10.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            if (dateMillis == todayStart) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(7.dp)
                                                        .clip(CircleShape)
                                                        .background(currentTheme.primary)
                                                )
                                            }
                                            Text(
                                                text = getDayLabel(dateMillis, todayStart, dayInMillis).uppercase(),
                                                color = if (dateMillis == todayStart) currentTheme.textPrimary else currentTheme.textSecondary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.scaledSp,
                                                letterSpacing = 1.sp,
                                                fontFamily = currentFont
                                            )
                                        }
                                        if (dayFocusMins > 0 || dayRestMins > 0) {
                                            Text(
                                                text = buildString {
                                                    if (dayFocusMins > 0) append("${formatCompactMinutes(dayFocusMins)} focus")
                                                    if (dayFocusMins > 0 && dayRestMins > 0) append(" • ")
                                                    if (dayRestMins > 0) append("${formatCompactMinutes(dayRestMins)} rest")
                                                },
                                                color = currentTheme.textSecondary.copy(alpha = 0.72f),
                                                fontSize = 11.scaledSp,
                                                fontFamily = MonospaceFontFamily
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            try {
                                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                            } catch (_: Exception) {}
                                            showTier2Dialog = true
                                            tier2Title = "Delete Day's Progress?"
                                            tier2SessionsToDelete = dateSessions
                                        },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Day",
                                            tint = currentTheme.textSecondary.copy(alpha = 0.45f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            val groupedEntries = historyTimeline.recentGroupedEntries[dateMillis] ?: emptyList()
                            itemsIndexed(
                                items = groupedEntries,
                                key = { _, e -> "item_${dateMillis}_${e.first}" },
                                contentType = { _, _ -> "session_pill" }
                            ) { groupIndex, (taskName, sessions) ->
                                HistorySessionPill(
                                    taskName = taskName,
                                    sessions = sessions,
                                    viewModel = viewModel,
                                    allTasks = allTasks,
                                    staggerIndex = groupIndex.coerceAtMost(8),
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = PomoMotion.defaultEffects(),
                                        placementSpec = PomoMotion.intOffsetSpatial,
                                        fadeOutSpec = PomoMotion.fastEffects()
                                    ),
                                    onDeleteGroupClick = {
                                        showTier2Dialog = true
                                        tier2Title = "Delete Task Group?"
                                        tier2SessionsToDelete = sessions
                                    }
                                )
                            }
                        }

                        // 2. Past Month Days (8-30 days)
                        if (pastMonthDays.isNotEmpty()) {
                            item(key = "prev_days_header", contentType = "section_header") {
                                Text(
                                    text = "PREVIOUS DAYS",
                                    fontSize = 12.scaledSp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.textSecondary,
                                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp, start = 4.dp, end = 4.dp),
                                    fontFamily = currentFont
                                )
                            }
                            itemsIndexed(
                                items = pastMonthDays,
                                key = { _, d -> "past_$d" },
                                contentType = { _, _ -> "past_day_pill" }
                            ) { pastIndex, dateMillis ->
                                val dateSessions = sessionsByDay[dateMillis] ?: emptyList()
                                MicroSummaryPill(
                                    dateMillis = dateMillis,
                                    sessions = dateSessions,
                                    viewModel = viewModel,
                                    allTasks = allTasks,
                                    staggerIndex = pastIndex.coerceAtMost(8),
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = PomoMotion.defaultEffects(),
                                        placementSpec = PomoMotion.intOffsetSpatial,
                                        fadeOutSpec = PomoMotion.fastEffects()
                                    ),
                                    onDeleteDayClick = {
                                        showTier2Dialog = true
                                        tier2Title = "Delete Day's Progress?"
                                        tier2SessionsToDelete = dateSessions
                                    },
                                    onDeleteGroupClick = { list ->
                                        showTier2Dialog = true
                                        tier2Title = "Delete Task Group?"
                                        tier2SessionsToDelete = list
                                    }
                                )
                            }
                        }

                        // 3. Archives (30+ days)
                        if (archiveEntries.isNotEmpty()) {
                            item(key = "archives_header", contentType = "section_header") {
                                Text(
                                    text = "ARCHIVES",
                                    fontSize = 12.scaledSp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.textSecondary,
                                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp, start = 4.dp, end = 4.dp),
                                    fontFamily = currentFont
                                )
                            }
                            itemsIndexed(
                                items = archiveEntries,
                                key = { _, e -> "arch_${e.first}" },
                                contentType = { _, _ -> "month_card" }
                            ) { archIndex, (monthStr, sessions) ->
                                MonthSummaryCard(
                                    monthStr = monthStr,
                                    sessions = sessions,
                                    allTasks = allTasks,
                                    staggerIndex = archIndex.coerceAtMost(6),
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = PomoMotion.defaultEffects(),
                                        placementSpec = PomoMotion.intOffsetSpatial,
                                        fadeOutSpec = PomoMotion.fastEffects()
                                    ),
                                    onDeleteMonthClick = {
                                        showTier3Dialog = true
                                        tier3MonthStr = monthStr
                                        tier3SessionsToDelete = sessions
                                        tier3InputText = ""
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Subtle 32.dp Bottom Scrim so scrolled session cards dissolve cleanly at the viewport edge
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

        // Editable Daily Focus Goal Modal
        if (showGoalDialog) {
            DailyFocusGoalDialog(
                currentGoalMinutes = dailyGoalMinutes,
                focusLengthMinutes = SettingsManager.getFocusTimeMins(),
                onSave = { newGoal ->
                    SettingsManager.setDailyFocusGoalMins(newGoal)
                    showGoalDialog = false
                },
                onDismiss = { showGoalDialog = false }
            )
        }

        // --- Tier 2 Clean AlertDialog ---
        if (showTier2Dialog) {
            var tier2AnimateIn by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { tier2AnimateIn = true }
            val tier2Scale by animateFloatAsState(
                targetValue = if (tier2AnimateIn) 1f else 0.88f,
                animationSpec = PomoMotion.expressiveSpatial(),
                label = "tier2Scale"
            )
            val tier2Alpha by animateFloatAsState(
                targetValue = if (tier2AnimateIn) 1f else 0f,
                animationSpec = PomoMotion.defaultEffects(),
                label = "tier2Alpha"
            )

            AlertDialog(
                onDismissRequest = { showTier2Dialog = false },
                modifier = Modifier.graphicsLayer {
                    scaleX = tier2Scale
                    scaleY = tier2Scale
                    alpha = tier2Alpha
                    compositingStrategy = if (tier2Alpha < 0.999f) CompositingStrategy.Offscreen else CompositingStrategy.Auto
                },
                title = {
                    Text(
                        text = tier2Title,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont
                    )
                },
                text = {
                    Text(
                        text = "You are about to clear an entire block of history. This will remove all associated study minutes from your daily summary. Do you want to proceed?",
                        color = currentTheme.textSecondary,
                        fontSize = 14.scaledSp,
                        fontFamily = currentFont
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            try {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (_: Exception) {}
                            viewModel.deleteSessions(tier2SessionsToDelete)
                            showTier2Dialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Delete",
                            color = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                            showTier2Dialog = false
                        }
                    ) {
                        Text("Cancel", color = currentTheme.textSecondary)
                    }
                },
                containerColor = currentTheme.surface,
                shape = RoundedCornerShape(24.dp)
            )
        }

        // --- Tier 3 Serious Overlay Dialog ---
        if (showTier3Dialog) {
            Dialog(
                onDismissRequest = {
                    showTier3Dialog = false
                    tier3InputText = ""
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                var tier3AnimateIn by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { tier3AnimateIn = true }
                val tier3Scale by animateFloatAsState(
                    targetValue = if (tier3AnimateIn) 1f else 0.88f,
                    animationSpec = PomoMotion.expressiveSpatial(),
                    label = "tier3Scale"
                )
                val tier3Alpha by animateFloatAsState(
                    targetValue = if (tier3AnimateIn) 1f else 0f,
                    animationSpec = PomoMotion.defaultEffects(),
                    label = "tier3Alpha"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.45f * tier3Alpha))
                        .imePadding()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2B1B1B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = tier3Scale
                                scaleY = tier3Scale
                                alpha = tier3Alpha
                                compositingStrategy = if (tier3Alpha < 0.999f) CompositingStrategy.Offscreen else CompositingStrategy.Auto
                            }
                            .border(2.dp, Color(0xFFFF5252), RoundedCornerShape(24.dp)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ CRITICAL ACTION REQUIRED",
                                fontSize = 18.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF5252),
                                fontFamily = currentFont
                            )

                            Text(
                                text = "You are about to permanently wipe out $tier3MonthStr of your hard-earned study archive. All telemetry logs, focus metrics, and streak data for this month will be erased.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.scaledSp,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.scaledSp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Type 'ERASE' in uppercase to authorize deletion:",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 11.scaledSp,
                                fontWeight = FontWeight.SemiBold
                            )

                            OutlinedTextField(
                                value = tier3InputText,
                                onValueChange = { tier3InputText = it },
                                placeholder = { Text("Type ERASE here", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    cursorColor = Color(0xFFFF5252),
                                    focusedBorderColor = Color(0xFFFF5252),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                                ),
                                singleLine = true
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                    showTier3Dialog = false
                                    tier3InputText = ""
                                }) {
                                    Text("Cancel", color = Color.White.copy(alpha = 0.6f))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (tier3InputText == "ERASE") {
                                            try {
                                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                            } catch (_: Exception) {}
                                            viewModel.deleteSessions(tier3SessionsToDelete)
                                            showTier3Dialog = false
                                            tier3InputText = ""
                                        }
                                    },
                                    enabled = tier3InputText == "ERASE",
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFF5252),
                                        disabledContainerColor = Color(0xFFFF5252).copy(alpha = 0.3f)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Erase Month",
                                        color = if (tier3InputText == "ERASE") Color.White else Color.White.copy(alpha = 0.5f),
                                        fontWeight = FontWeight.Bold
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
private fun HistoryModeFilterRow(
    selectedMode: HistoryModeFilter,
    allCount: Int,
    focusCount: Int,
    restCount: Int,
    onSelectMode: (HistoryModeFilter) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val items = listOf(
        Triple(HistoryModeFilter.ALL, "All", allCount),
        Triple(HistoryModeFilter.FOCUS, "Focus", focusCount),
        Triple(HistoryModeFilter.REST, "Rest", restCount)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (mode, label, count) ->
            val isSelected = selectedMode == mode
            val activeAccent = when (mode) {
                HistoryModeFilter.REST -> currentTheme.secondary
                else -> currentTheme.primary
            }
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) activeAccent else currentTheme.surface,
                animationSpec = PomoMotion.colorSpec,
                label = "filterBg_${mode.name}"
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) activeAccent else currentTheme.cardBorder,
                animationSpec = PomoMotion.colorSpec,
                label = "filterBorder_${mode.name}"
            )
            val contentColor = if (isSelected) {
                if (activeAccent.luminance() > 0.52f) Color(0xFF1E1E1E) else Color.White
            } else {
                currentTheme.textPrimary
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = bgColor,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 44.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onSelectMode(mode) }
                    .testTag("history_filter_${mode.name.lowercase()}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        fontSize = 13.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        fontFamily = currentFont
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) contentColor.copy(alpha = 0.16f)
                                else currentTheme.backgroundSecondary
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$count",
                            fontSize = 11.scaledSp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) contentColor else currentTheme.textSecondary,
                            fontFamily = MonospaceFontFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryEmptyState(
    isFilterEmpty: Boolean,
    activeFilterLabel: String,
    onResetFilter: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Empty",
            tint = currentTheme.textSecondary.copy(alpha = 0.35f),
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = if (isFilterEmpty) "No $activeFilterLabel sessions logged" else "No focus sessions yet",
            color = currentTheme.textPrimary,
            fontSize = 18.scaledSp,
            fontWeight = FontWeight.Bold,
            fontFamily = currentFont,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (isFilterEmpty) {
                "Try switching back to All to browse your full timeline."
            } else {
                "Complete your first study session to light up your dashboard."
            },
            color = currentTheme.textSecondary,
            fontSize = 14.scaledSp,
            fontFamily = currentFont,
            textAlign = TextAlign.Center
        )
        if (isFilterEmpty) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onResetFilter,
                colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Show All Sessions",
                    color = if (currentTheme.primary.luminance() > 0.55f) Color(0xFF1E1E1E) else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont
                )
            }
        }
    }
}

@Composable
fun MicroSummaryPill(
    dateMillis: Long,
    sessions: List<TimerSession>,
    viewModel: TimerViewModel,
    allTasks: List<TaskItem>,
    staggerIndex: Int = 0,
    modifier: Modifier = Modifier,
    onDeleteDayClick: () -> Unit,
    onDeleteGroupClick: (List<TimerSession>) -> Unit
) {
    val view = LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        if (expanded) 180f else 0f,
        animationSpec = PomoMotion.expressiveSpatial(),
        label = "microArrow"
    )
    val revealProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (staggerIndex > 0) delay(staggerIndex * 28L)
        revealProgress.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = PomoMotion.pressScaleSpec,
        label = "microPressScale"
    )
    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(dateMillis))
    val focusSessions = sessions.filter { !it.isBreak }
    val totalFocusMins = focusSessions.sumOf { it.durationMinutes }
    val sessionsCount = focusSessions.size
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Card(
        onClick = {
            try {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            } catch (_: Exception) {}
            expanded = !expanded
        },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        border = BorderStroke(1.dp, currentTheme.cardBorder),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val p = revealProgress.value
                alpha = p.coerceIn(0f, 1f)
                val s = (0.95f + 0.05f * p) * pressScale
                scaleX = s
                scaleY = s
                translationY = (1f - p) * 14.dp.toPx()
                compositingStrategy = if (p < 0.999f) CompositingStrategy.Offscreen else CompositingStrategy.Auto
            }
            .historyCardShadow(
                cornerRadius = 20.dp,
                shadowColor = currentTheme.shadowColor
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(currentTheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = currentTheme.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dateStr,
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.scaledSp,
                        color = currentTheme.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${formatCompactMinutes(totalFocusMins)} focus • $sessionsCount sessions",
                        fontFamily = currentFont,
                        fontSize = 13.scaledSp,
                        color = currentTheme.textSecondary
                    )
                }
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (_: Exception) {}
                        onDeleteDayClick()
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Day",
                        tint = currentTheme.textSecondary.copy(alpha = 0.45f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = currentTheme.textSecondary,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = PomoMotion.expandSection,
                exit = PomoMotion.collapseSection
            ) {
                Column(modifier = Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val groupedByTask = sessions.groupBy { it.taskName }
                    groupedByTask.forEach { (task, list) ->
                        HistorySessionPill(
                            taskName = task,
                            sessions = list,
                            viewModel = viewModel,
                            allTasks = allTasks,
                            onDeleteGroupClick = { onDeleteGroupClick(list) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonthSummaryCard(
    monthStr: String,
    sessions: List<TimerSession>,
    allTasks: List<TaskItem>,
    staggerIndex: Int = 0,
    modifier: Modifier = Modifier,
    onDeleteMonthClick: () -> Unit
) {
    val view = LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        if (expanded) 180f else 0f,
        animationSpec = PomoMotion.expressiveSpatial(),
        label = "monthArrow"
    )
    val revealProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (staggerIndex > 0) delay(staggerIndex * 28L)
        revealProgress.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = PomoMotion.pressScaleSpec,
        label = "monthPressScale"
    )
    val focusSessions = sessions.filter { !it.isBreak }
    val totalFocusMins = focusSessions.sumOf { it.durationMinutes }
    val totalDays = sessions.groupBy { getStartOfDay(it.startTime) }.size
    val dailyAvg = if (totalDays > 0) totalFocusMins / totalDays else 0
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Card(
        onClick = {
            try {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            } catch (_: Exception) {}
            expanded = !expanded
        },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        border = BorderStroke(1.dp, currentTheme.cardBorder),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val p = revealProgress.value
                alpha = p.coerceIn(0f, 1f)
                val s = (0.95f + 0.05f * p) * pressScale
                scaleX = s
                scaleY = s
                translationY = (1f - p) * 14.dp.toPx()
                compositingStrategy = if (p < 0.999f) CompositingStrategy.Offscreen else CompositingStrategy.Auto
            }
            .historyCardShadow(
                cornerRadius = 24.dp,
                shadowColor = currentTheme.shadowColor
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(currentTheme.secondary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = currentTheme.secondary, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = monthStr,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${formatCompactMinutes(totalFocusMins)} • ${formatCompactMinutes(dailyAvg)}/day avg",
                        fontSize = 13.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textSecondary
                    )
                }
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (_: Exception) {}
                        onDeleteMonthClick()
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Month",
                        tint = currentTheme.textSecondary.copy(alpha = 0.45f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = currentTheme.textSecondary,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = PomoMotion.expandSection,
                exit = PomoMotion.collapseSection
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = currentTheme.cardBorder, modifier = Modifier.padding(bottom = 12.dp))
                    Text("Breakdown Details", fontWeight = FontWeight.SemiBold, fontSize = 14.scaledSp, color = currentTheme.textPrimary)
                    Spacer(modifier = Modifier.height(8.dp))

                    val groupedTasks = sessions.groupBy { it.taskName }
                    groupedTasks.entries.sortedByDescending { it.value.sumOf { s -> s.durationMinutes } }.forEach { (task, list) ->
                        val taskMins = list.sumOf { it.durationMinutes }
                        val hasFocus = list.any { !it.isBreak }
                        val baseColor = resolveTaskColor(task, list, allTasks, currentTheme)
                        val taskColor = if (hasFocus) baseColor else baseColor.copy(alpha = 0.65f)

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = task,
                                color = taskColor,
                                fontSize = 13.scaledSp,
                                fontFamily = currentFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatCompactMinutes(taskMins),
                                color = taskColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.scaledSp,
                                fontFamily = MonospaceFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistorySessionPill(
    taskName: String,
    sessions: List<TimerSession>,
    viewModel: TimerViewModel,
    allTasks: List<TaskItem>,
    staggerIndex: Int = 0,
    modifier: Modifier = Modifier,
    onDeleteGroupClick: () -> Unit
) {
    val view = LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        if (expanded) 180f else 0f,
        animationSpec = PomoMotion.expressiveSpatial(),
        label = "arrow"
    )
    val revealProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (staggerIndex > 0) delay(staggerIndex * 26L)
        revealProgress.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = PomoMotion.pressScaleSpec,
        label = "sessionPillPressScale"
    )

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                val p = revealProgress.value
                alpha = p.coerceIn(0f, 1f)
                val s = (0.95f + 0.05f * p) * pressScale
                scaleX = s
                scaleY = s
                translationY = (1f - p) * 14.dp.toPx()
                compositingStrategy = if (p < 0.999f) CompositingStrategy.Offscreen else CompositingStrategy.Auto
            }
            .historyCardShadow(
                cornerRadius = 24.dp,
                shadowColor = currentTheme.shadowColor
            )
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true)
            ) {
                try {
                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (_: Exception) {}
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            val totalMins = sessions.sumOf { it.durationMinutes }
            val hasFocus = sessions.any { !it.isBreak }
            val baseColor = resolveTaskColor(taskName, sessions, allTasks, currentTheme)
            val tintColor = if (hasFocus) baseColor else baseColor.copy(alpha = 0.65f)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (hasFocus) tintColor.copy(alpha = 0.15f) else Color.Transparent)
                        .border(if (!hasFocus) 2.dp else 0.dp, tintColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (hasFocus) Icons.Default.CheckCircle else Icons.Default.Pause,
                        contentDescription = null,
                        tint = tintColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = taskName,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont,
                        fontSize = 16.scaledSp,
                        color = currentTheme.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${sessions.size} ${if (sessions.size == 1) "session" else "sessions"} • ${formatCompactMinutes(totalMins)}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textSecondary
                    )
                }

                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (_: Exception) {}
                        onDeleteGroupClick()
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task Sessions",
                        tint = currentTheme.textSecondary.copy(alpha = 0.45f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = currentTheme.textSecondary,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = PomoMotion.expandSection,
                exit = PomoMotion.collapseSection
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
                    sessions.sortedBy { it.startTime }.forEach { session ->
                        key(session.id) {
                            val type = if (session.isBreak) "Rest" else "Focus"
                            val start = timeFormat.format(Date(session.startTime))
                            val end = timeFormat.format(Date(session.endTime))
                            val sessionColor = resolveTaskColor(taskName, listOf(session), allTasks, currentTheme)

                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { value ->
                                    if (value == SwipeToDismissBoxValue.EndToStart) {
                                        try {
                                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                        } catch (_: Exception) {}
                                        viewModel.deleteSession(session)
                                        true
                                    } else {
                                        false
                                    }
                                }
                            )
                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(currentTheme.primary)
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                content = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(currentTheme.surface)
                                            .padding(horizontal = 14.dp, vertical = 11.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(if (session.isBreak) Color.Transparent else sessionColor)
                                                    .border(if (session.isBreak) 2.dp else 0.dp, sessionColor, CircleShape)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = "$type • ${session.durationMinutes}m",
                                                color = currentTheme.textPrimary,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.scaledSp,
                                                fontFamily = currentFont
                                            )
                                        }
                                        Text(
                                            text = "$start - $end",
                                            color = currentTheme.textSecondary,
                                            fontSize = 12.scaledSp,
                                            fontFamily = MonospaceFontFamily
                                        )
                                    }
                                },
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
