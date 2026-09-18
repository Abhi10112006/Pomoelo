package com.example.ui

import com.example.ui.components.pomoShadow

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.draw.drawBehind
import com.example.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.scaledSp
import androidx.navigation.NavController
import com.example.data.TimerSession
import com.example.data.TaskItem
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
    cornerRadius: androidx.compose.ui.unit.Dp = 24.dp,
    shadowColor: Color
): Modifier = this.shadow(
    elevation = 12.dp,
    shape = RoundedCornerShape(cornerRadius),
    ambientColor = shadowColor,
    spotColor = shadowColor,
    clip = false
)

fun resolveTaskColor(taskName: String, sessions: List<TimerSession>, allTasks: List<TaskItem>, currentTheme: ThemeOption): Color {
    val isDefaultTask = taskName.isBlank() || taskName == "Focus Time!" || taskName == "Deep Focus Session"
    if (isDefaultTask) return currentTheme.primary
    
    val isDefaultBreak = taskName == "Break Time!" || taskName == "Break" || taskName == "Long Break"
    if (isDefaultBreak) return currentTheme.secondary
    
    val targetSession = sessions
        .filter { it.taskName == taskName && it.taskColor != null }
        .maxByOrNull { it.startTime }
        
    if (targetSession?.taskColor != null) {
        return Color(targetSession.taskColor.toULong())
    }
    
    val task = allTasks.find { it.name == taskName }
    return if (task != null) {
        Color(task.categoryColor.toULong())
    } else {
        currentTheme.textSecondary // Fallback for unknown/deleted historical task
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(viewModel: TimerViewModel, navController: NavController, bottomPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    val view = androidx.compose.ui.platform.LocalView.current
    val allSessions by viewModel.allSessions.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState()
    
    val todayStart = getStartOfDay(System.currentTimeMillis())
    val dayInMillis = 24 * 60 * 60 * 1000L

    // State managers for Undo / Temporary Deletion Cache
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var temporarilyDeletedGroupKeys by remember { mutableStateOf(setOf<String>()) }
    var recentlyDeletedSessions by remember { mutableStateOf<List<TimerSession>?>(null) }

    // Tier 2 Deletion States
    var showTier2Dialog by remember { mutableStateOf(false) }
    var tier2Title by remember { mutableStateOf("") }
    var tier2SessionsToDelete by remember { mutableStateOf<List<TimerSession>>(emptyList()) }

    // Tier 3 Deletion States
    var showTier3Dialog by remember { mutableStateOf(false) }
    var tier3MonthStr by remember { mutableStateOf("") }
    var tier3SessionsToDelete by remember { mutableStateOf<List<TimerSession>>(emptyList()) }
    var tier3InputText by remember { mutableStateOf("") }

    // Dynamically filter sessions based on visible items
    val visibleSessions = allSessions.filter { session ->
        val dateMillis = getStartOfDay(session.startTime)
        val groupKey = "${session.taskName}|$dateMillis"
        !temporarilyDeletedGroupKeys.contains(groupKey)
    }

    val todaySessions = visibleSessions.filter { it.startTime >= todayStart }

    val todayPomodoros = todaySessions.count { !it.isBreak }
    val todayFocusMinutes = todaySessions.filter { !it.isBreak }.sumOf { it.durationMinutes }
    val todayBreakMinutes = todaySessions.filter { it.isBreak }.sumOf { it.durationMinutes }

    var animatedPomodoros by remember { mutableIntStateOf(0) }
    var animatedFocusTime by remember { mutableIntStateOf(0) }
    var animatedBreakTime by remember { mutableIntStateOf(0) }

    LaunchedEffect(todayPomodoros, todayFocusMinutes, todayBreakMinutes) {
        animatedPomodoros = todayPomodoros
        animatedFocusTime = todayFocusMinutes
        animatedBreakTime = todayBreakMinutes
    }

    val displayPomodoros by animateIntAsState(targetValue = animatedPomodoros, animationSpec = tween(1000), label = "pomo")
    val displayFocus by animateIntAsState(targetValue = animatedFocusTime, animationSpec = tween(1000), label = "focus")
    val displayBreak by animateIntAsState(targetValue = animatedBreakTime, animationSpec = tween(1000), label = "break")

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = bottomPadding + 16.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = currentTheme.primaryDark,
                    contentColor = currentTheme.surface,
                    actionColor = currentTheme.accent
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    "Your Focus Dashboard",
                    fontSize = 28.scaledSp,
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Your progress for today",
                    fontSize = 15.scaledSp,
                    fontFamily = currentFont,
                    color = currentTheme.textSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Dashboard widget
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .historyCardShadow(
                        cornerRadius = 24.dp,
                        shadowColor = currentTheme.shadowColor
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text("$displayPomodoros", fontSize = 32.scaledSp, fontWeight = FontWeight.Black, color = currentTheme.primary, fontFamily = MonospaceFontFamily)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("SESSIONS", fontSize = 11.scaledSp, color = currentTheme.textSecondary, fontFamily = currentFont, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(currentTheme.cardBorder))
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text("${displayFocus / 60}h ${displayFocus % 60}m", fontSize = 22.scaledSp, fontWeight = FontWeight.Black, color = currentTheme.secondary, fontFamily = MonospaceFontFamily)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("FOCUS", fontSize = 11.scaledSp, color = currentTheme.textSecondary, fontFamily = currentFont, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                    Box(modifier = Modifier.width(1.dp).height(40.dp).background(currentTheme.cardBorder))
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text("${displayBreak / 60}h ${displayBreak % 60}m", fontSize = 22.scaledSp, fontWeight = FontWeight.Black, color = currentTheme.accent, fontFamily = MonospaceFontFamily)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("REST", fontSize = 11.scaledSp, color = currentTheme.textSecondary, fontFamily = currentFont, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = bottomPadding + 88.dp)
            ) {
                
                val sessionsByDay = visibleSessions.sortedByDescending { it.startTime }.groupBy { getStartOfDay(it.startTime) }

                val recentDays = mutableListOf<Long>() // 0..7
                val pastMonthDays = mutableListOf<Long>() // 7..30 
                val archiveMonths = mutableMapOf<String, List<TimerSession>>()

                sessionsByDay.forEach { (startOfDay, sessions) ->
                    val diffDays = ((todayStart - startOfDay) / dayInMillis).toInt()
                    if (diffDays <= 7) {
                        recentDays.add(startOfDay)
                    } else if (diffDays <= 30) {
                        pastMonthDays.add(startOfDay)
                    } else {
                        val monthStr = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date(startOfDay))
                        val currentMonthSessions = archiveMonths.getOrDefault(monthStr, emptyList()).toMutableList()
                        currentMonthSessions.addAll(sessions)
                        archiveMonths[monthStr] = currentMonthSessions
                    }
                }

                if (sessionsByDay.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Empty",
                                tint = currentTheme.textSecondary.copy(alpha = 0.3f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No focus sessions yet",
                                color = currentTheme.textPrimary,
                                fontSize = 18.scaledSp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Your first study session will appear here.",
                                color = currentTheme.textSecondary,
                                fontSize = 14.scaledSp,
                                fontFamily = currentFont
                            )
                        }
                    }
                } else {
                    // 1. Recent Days (0-7 days ago) - Fully expanded Daily Headers
                    recentDays.forEach { dateMillis ->
                        stickyHeader {
                            val dateSessions = sessionsByDay[dateMillis]!!
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(currentTheme.background)
                                    .padding(vertical = 16.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    getDayLabel(dateMillis, todayStart, dayInMillis).uppercase(),
                                    color = currentTheme.textSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.scaledSp,
                                    letterSpacing = 1.sp,
                                    fontFamily = currentFont
                                )
                                IconButton(
                                    onClick = {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        showTier2Dialog = true
                                        tier2Title = "Delete Today's Progress?"
                                        tier2SessionsToDelete = dateSessions
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Day",
                                        tint = currentTheme.textSecondary.copy(alpha = 0.4f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        
                        val dateSessions = sessionsByDay[dateMillis]!!
                        val groupedByTask = dateSessions.groupBy { it.taskName }
                        
                        items(groupedByTask.entries.toList(), key = { it.key + dateMillis.toString() }) { (taskName, sessions) ->
                            HistorySessionPill(
                                taskName = taskName,
                                sessions = sessions,
                                viewModel = viewModel,
                                allTasks = allTasks,
                                onDeleteGroupClick = {
                                    showTier2Dialog = true
                                    tier2Title = "Delete Task Group?"
                                    tier2SessionsToDelete = sessions
                                }
                            )
                        }
                    }

                    // 2. Past Month Days (7-30 days) - Collapsed Micro Summaries
                    if (pastMonthDays.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("PREVIOUS DAYS", fontSize = 12.scaledSp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = currentTheme.textSecondary, modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), fontFamily = currentFont)
                        }
                        items(pastMonthDays, key = { it }) { dateMillis ->
                            val dateSessions = sessionsByDay[dateMillis]!!
                            MicroSummaryPill(
                                dateMillis = dateMillis,
                                sessions = dateSessions,
                                viewModel = viewModel,
                                allTasks = allTasks,
                                dayInMillis = dayInMillis,
                                onDeleteDayClick = {
                                    showTier2Dialog = true
                                    tier2Title = "Delete Today's Progress?"
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

                    // 3. Archive (30+ days or past years) - Monthly Collapsed Summaries
                    if (archiveMonths.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("ARCHIVES", fontSize = 12.scaledSp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = currentTheme.textSecondary, modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp), fontFamily = currentFont)
                        }
                        items(archiveMonths.entries.toList(), key = { it.key }) { (monthStr, sessions) ->
                            MonthSummaryCard(
                                monthStr = monthStr,
                                sessions = sessions,
                                viewModel = viewModel,
                                allTasks = allTasks,
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
                
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            // --- Tier 2 Clean AlertDialog ---
            if (showTier2Dialog) {
                AlertDialog(
                    onDismissRequest = { showTier2Dialog = false },
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
                            fontSize = 14.scaledSp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}
                                viewModel.deleteSessions(tier2SessionsToDelete)
                                showTier2Dialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("[ Delete anyway ]", color = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                showTier2Dialog = false
                            }
                        ) {
                            Text("[ Cancel ]", color = currentTheme.textSecondary)
                        }
                    },
                    containerColor = currentTheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
            }

            // --- Tier 3 Serious Overlay Dialog ---
            if (showTier3Dialog) {
                androidx.compose.ui.window.Dialog(
                    onDismissRequest = { 
                        showTier3Dialog = false
                        tier3InputText = ""
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF2B1B1B)), // High-friction dark serious mode custom color
                            modifier = Modifier
                                .fillMaxWidth()
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
                                    fontFamily = AppFontFamily
                                )

                                Text(
                                    text = "You are about to permanently wipe out an entire MONTH of your hard-earned study empire. All telemetry logs, focus metrics, and streak data will be completely obliterated. Are you sure you want to erase?",
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
                                        try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
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
                                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                                } catch (e: Exception) {}
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
                                            text = "[ PERMANENTLY DELETE ]",
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
fun MicroSummaryPill(
    dateMillis: Long,
    sessions: List<TimerSession>,
    viewModel: TimerViewModel,
    allTasks: List<TaskItem>,
    dayInMillis: Long,
    onDeleteDayClick: () -> Unit,
    onDeleteGroupClick: (List<TimerSession>) -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(dateMillis))
    val focusSessions = sessions.filter { !it.isBreak }
    val totalFocusMins = focusSessions.sumOf { it.durationMinutes }
    val sessionsCount = focusSessions.size
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    
    Card(
        onClick = {
            try {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
            } catch (e: Exception) {}
            expanded = !expanded
        },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        border = BorderStroke(1.dp, currentTheme.cardBorder),
        modifier = Modifier
            .fillMaxWidth()
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
                Spacer(modifier = Modifier.width(16.dp))
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
                        text = "${totalFocusMins / 60}h ${totalFocusMins % 60}m focus • $sessionsCount sessions",
                        fontFamily = currentFont,
                        fontSize = 13.scaledSp,
                        color = currentTheme.textSecondary
                    )
                }
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        onDeleteDayClick()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Day",
                        tint = currentTheme.textSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            
            AnimatedVisibility(visible = expanded) {
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
    viewModel: TimerViewModel,
    allTasks: List<TaskItem>,
    onDeleteMonthClick: () -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val focusSessions = sessions.filter { !it.isBreak }
    val totalFocusMins = focusSessions.sumOf { it.durationMinutes }
    val totalDays = sessions.groupBy { getStartOfDay(it.startTime) }.size
    val dailyAvg = if (totalDays > 0) totalFocusMins / totalDays else 0
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    
    Card(
        onClick = {
            try {
                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
            } catch (e: Exception) {}
            expanded = !expanded
        },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        border = BorderStroke(1.dp, currentTheme.cardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .historyCardShadow(
                cornerRadius = 20.dp,
                shadowColor = currentTheme.shadowColor
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(currentTheme.secondary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.List, contentDescription = null, tint = currentTheme.secondary, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = monthStr,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${totalFocusMins / 60}h ${totalFocusMins % 60}m • ${dailyAvg / 60}h ${dailyAvg % 60}m / day avg",
                        fontSize = 13.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textSecondary
                    )
                }
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        onDeleteMonthClick()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Month",
                        tint = currentTheme.textSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = currentTheme.cardBorder, modifier = Modifier.padding(bottom = 12.dp))
                    Text("Breakdown Details", fontWeight = FontWeight.SemiBold, fontSize = 14.scaledSp, color = currentTheme.textPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val groupedTasks = sessions.groupBy { it.taskName }
                    groupedTasks.entries.sortedByDescending { it.value.sumOf { s -> s.durationMinutes } }.forEach { (task, list) ->
                        val taskMins = list.sumOf { it.durationMinutes }
                        val hasFocus = list.any { !it.isBreak }
                        val baseColor = resolveTaskColor(task, list, allTasks, currentTheme)
                        val taskColor = if (hasFocus) baseColor else baseColor.copy(alpha = 0.6f)
                        
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(task, color = taskColor, fontSize = 13.scaledSp, fontFamily = currentFont, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${taskMins / 60}h ${taskMins % 60}m", color = taskColor, fontWeight = FontWeight.Bold, fontSize = 13.scaledSp, fontFamily = MonospaceFontFamily)
                            }
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
    onDeleteGroupClick: () -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "arrow")

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .historyCardShadow(
                cornerRadius = 24.dp,
                shadowColor = currentTheme.shadowColor
            )
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(24.dp))
            .clickable {
                try {
                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {}
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            val totalMins = sessions.sumOf { it.durationMinutes }
            val hasFocus = sessions.any { !it.isBreak }
            val baseColor = resolveTaskColor(taskName, sessions, allTasks, currentTheme)
            val tintColor = if (hasFocus) baseColor else baseColor.copy(alpha = 0.6f)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (hasFocus) tintColor.copy(alpha = 0.15f) else Color.Transparent)
                        .border(if (!hasFocus) 2.dp else 0.dp, tintColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (hasFocus) Icons.Default.CheckCircle else Icons.Default.Pause,
                        contentDescription = null,
                        tint = tintColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = taskName,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont,
                        fontSize = 16.scaledSp,
                        color = currentTheme.textPrimary,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${sessions.size} sessions • ${totalMins}m total",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textSecondary
                    )
                }
                
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        onDeleteGroupClick()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task Sessions",
                        tint = currentTheme.textSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = currentTheme.textSecondary,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }
            
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    sessions.sortedBy { it.startTime }.forEach { session ->
                        val type = if (session.isBreak) "Rest" else "Focus"
                        val start = timeFormat.format(Date(session.startTime))
                        val end = timeFormat.format(Date(session.endTime))
                        val sessionColor = resolveTaskColor(taskName, listOf(session), allTasks, currentTheme)
                        
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                if (value == SwipeToDismissBoxValue.EndToStart) {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS)
                                    } catch (e: Exception) {}
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
                                        .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier.size(8.dp).clip(CircleShape)
                                                .background(if (session.isBreak) Color.Transparent else sessionColor)
                                                .border(if (session.isBreak) 2.dp else 0.dp, sessionColor, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = type,
                                            color = currentTheme.textPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.scaledSp,
                                            fontFamily = currentFont
                                        )
                                    }
                                    Text(
                                        text = "$start - $end",
                                        color = currentTheme.textSecondary,
                                        fontSize = 13.scaledSp,
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
