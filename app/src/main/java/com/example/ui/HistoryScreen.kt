package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.example.ui.theme.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.scaledSp
import androidx.navigation.NavController
import com.example.data.TimerSession
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(viewModel: TimerViewModel, navController: NavController, bottomPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    val view = androidx.compose.ui.platform.LocalView.current
    val allSessions by viewModel.allSessions.collectAsState()
    
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

    Scaffold(
        containerColor = Color(0xFFFCEBEE),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = bottomPadding + 16.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF3E2723), // Athletic Accent deep brown slate
                    contentColor = Color.White,
                    actionColor = Color(0xFFFFCC80) // Enhanced gold-peach with super high contrast (>7.4:1) against deep brown
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
            Text(
                "Your Focus Dashboard",
                fontSize = 28.scaledSp,
                fontFamily = CursiveFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5D4037)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Dashboard widget
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$displayPomodoros", fontSize = 32.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8A80), fontFamily = MonospaceFontFamily)
                    Text("Sessions", fontSize = 14.scaledSp, color = Color.Gray, fontFamily = AppFontFamily)
                }
                Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0xFFE0E0E0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${displayFocus / 60}h ${displayFocus % 60}m", fontSize = 24.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFF81D4FA), fontFamily = MonospaceFontFamily)
                    Text("Focus", fontSize = 14.scaledSp, color = Color.Gray, fontFamily = AppFontFamily)
                }
                Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0xFFE0E0E0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${displayBreak / 60}h ${displayBreak % 60}m", fontSize = 24.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFFA5D6A7), fontFamily = MonospaceFontFamily)
                    Text("Rest", fontSize = 14.scaledSp, color = Color.Gray, fontFamily = AppFontFamily)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text("Task History", fontSize = 20.scaledSp, fontWeight = FontWeight.SemiBold, color = Color(0xFF8D6E63), fontFamily = CursiveFontFamily)
            Spacer(modifier = Modifier.height(16.dp))

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
                        Text("No completed focus sessions yet. Time to start working!", color = Color.Gray, modifier = Modifier.padding(16.dp))
                    }
                } else {
                    // 1. Recent Days (0-7 days ago) - Fully expanded Daily Headers
                    recentDays.forEach { dateMillis ->
                        stickyHeader {
                            val dateSessions = sessionsByDay[dateMillis]!!
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFCEBEE))
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    getDayLabel(dateMillis, todayStart, dayInMillis),
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = MonospaceFontFamily
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
                                        tint = Color.Gray,
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
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Previous Days", fontSize = 16.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFFBCAAA4), modifier = Modifier.padding(vertical = 8.dp))
                        }
                        items(pastMonthDays, key = { it }) { dateMillis ->
                            val dateSessions = sessionsByDay[dateMillis]!!
                            MicroSummaryPill(
                                dateMillis = dateMillis,
                                sessions = dateSessions,
                                viewModel = viewModel,
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
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Archives", fontSize = 16.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFFBCAAA4), modifier = Modifier.padding(vertical = 8.dp))
                        }
                        items(archiveMonths.entries.toList(), key = { it.key }) { (monthStr, sessions) ->
                            MonthSummaryCard(
                                monthStr = monthStr,
                                sessions = sessions,
                                viewModel = viewModel,
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
                            color = Color(0xFF5D4037),
                            fontFamily = AppFontFamily
                        )
                    },
                    text = {
                        Text(
                            text = "You are about to clear an entire block of history. This will remove all associated study minutes from your daily summary. Do you want to proceed?",
                            color = Color.DarkGray,
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A80)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("[ Delete anyway ]", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showTier2Dialog = false }
                        ) {
                            Text("[ Cancel ]", color = Color(0xFF8D6E63))
                        }
                    },
                    containerColor = Color.White,
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
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                try {
                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {}
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$dateStr • ",
                        fontFamily = MonospaceFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.scaledSp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "${totalFocusMins / 60}h ${totalFocusMins % 60}m Focus | $sessionsCount Sessions",
                        fontFamily = AppFontFamily,
                        fontSize = 12.scaledSp,
                        color = Color.Gray
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
                        tint = Color.Gray,
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
    onDeleteMonthClick: () -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val focusSessions = sessions.filter { !it.isBreak }
    val totalFocusMins = focusSessions.sumOf { it.durationMinutes }
    val totalDays = sessions.groupBy { getStartOfDay(it.startTime) }.size
    val dailyAvg = if (totalDays > 0) totalFocusMins / totalDays else 0
    
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                try {
                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                } catch (e: Exception) {}
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "📁 $monthStr Summary",
                    fontSize = 16.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF455A64),
                    modifier = Modifier.weight(1f)
                )
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
                        tint = Color(0xFF455A64),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total Focus: ${totalFocusMins / 60}h ${totalFocusMins % 60}m | Daily Avg: ${dailyAvg / 60}h ${dailyAvg % 60}m", fontSize = 13.scaledSp, color = Color(0xFF607D8B), fontFamily = MonospaceFontFamily)
            
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = Color(0xFFCFD8DC), modifier = Modifier.padding(bottom = 12.dp))
                    Text("Breakdown Details", fontWeight = FontWeight.SemiBold, fontSize = 14.scaledSp, color = Color(0xFF455A64))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val groupedTasks = sessions.groupBy { it.taskName }
                    groupedTasks.entries.sortedByDescending { it.value.sumOf { s -> s.durationMinutes } }.forEach { (task, list) ->
                        val taskMins = list.sumOf { it.durationMinutes }
                        val taskColor = if (task.contains("Break")) Color(0xFF546E7A) else Color(0xFF455A64)
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(task, color = taskColor, fontSize = 13.scaledSp, fontFamily = AppFontFamily)
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
    onDeleteGroupClick: () -> Unit
) {
    val view = androidx.compose.ui.platform.LocalView.current
    var expanded by remember { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "arrow")

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
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
            val tintColor = if (!hasFocus) Color(0xFF81D4FA) else Color(0xFFFF8A80)
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(tintColor)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$taskName • ",
                        fontWeight = FontWeight.Bold,
                        fontFamily = AppFontFamily,
                        color = Color(0xFF5D4037)
                    )
                    Text(
                        text = "${totalMins}m",
                        fontWeight = FontWeight.Bold,
                        fontFamily = MonospaceFontFamily,
                        color = tintColor
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
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color.Gray,
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
                                        .background(Color(0xFFFF8A80))
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            },
                            content = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(start = 28.dp, top = 8.dp, bottom = 8.dp, end = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "⚡ $type ($start - $end)", 
                                        color = Color.Gray, 
                                        fontSize = 12.scaledSp, 
                                        fontFamily = MonospaceFontFamily,
                                        modifier = Modifier.weight(1f)
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
