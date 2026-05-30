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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
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

    val todaySessions = allSessions.filter { it.startTime >= todayStart }

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
        containerColor = Color(0xFFFCEBEE)
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
                    Text("$displayPomodoros", fontSize = 32.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFFFF8A80))
                    Text("Sessions", fontSize = 14.scaledSp, color = Color.Gray)
                }
                Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0xFFE0E0E0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${displayFocus / 60}h ${displayFocus % 60}m", fontSize = 24.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFF81D4FA))
                    Text("Focus", fontSize = 14.scaledSp, color = Color.Gray)
                }
                Box(modifier = Modifier.width(1.dp).height(50.dp).background(Color(0xFFE0E0E0)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${displayBreak / 60}h ${displayBreak % 60}m", fontSize = 24.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFFA5D6A7))
                    Text("Rest", fontSize = 14.scaledSp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Text("Task History", fontSize = 20.scaledSp, fontWeight = FontWeight.SemiBold, color = Color(0xFF8D6E63))
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = bottomPadding + 88.dp)
            ) {
                
                val sessionsByDay = allSessions.sortedByDescending { it.startTime }.groupBy { getStartOfDay(it.startTime) }

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
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        viewModel.deleteSessionsInRange(dateMillis, dateMillis + dayInMillis)
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
                            HistorySessionPill(taskName = taskName, sessions = sessions, viewModel = viewModel)
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
                            MicroSummaryPill(dateMillis, dateSessions, viewModel, dayInMillis)
                        }
                    }

                    // 3. Archive (30+ days or past years) - Monthly Collapsed Summaries
                    if (archiveMonths.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Archives", fontSize = 16.scaledSp, fontWeight = FontWeight.Bold, color = Color(0xFFBCAAA4), modifier = Modifier.padding(vertical = 8.dp))
                        }
                        items(archiveMonths.entries.toList(), key = { it.key }) { (monthStr, sessions) ->
                            MonthSummaryCard(monthStr, sessions, viewModel)
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun MicroSummaryPill(dateMillis: Long, sessions: List<TimerSession>, viewModel: TimerViewModel, dayInMillis: Long) {
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
                Text(
                    text = "$dateStr — Total: ${totalFocusMins / 60}h ${totalFocusMins % 60}m Focus | $sessionsCount Sessions",
                    fontSize = 13.scaledSp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        viewModel.deleteSessionsInRange(dateMillis, dateMillis + dayInMillis)
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
                        HistorySessionPill(task, list, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MonthSummaryCard(monthStr: String, sessions: List<TimerSession>, viewModel: TimerViewModel) {
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
                        if (sessions.isNotEmpty()) {
                            val start = sessions.minOf { it.startTime }
                            val end = sessions.maxOf { it.endTime } + 1000L
                            viewModel.deleteSessionsInRange(start, end)
                        }
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
            Text("Total Focus: ${totalFocusMins / 60}h ${totalFocusMins % 60}m | Daily Avg: ${dailyAvg / 60}h ${dailyAvg % 60}m", fontSize = 13.scaledSp, color = Color(0xFF607D8B))
            
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
                            Text(task, color = taskColor, fontSize = 13.scaledSp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${taskMins / 60}h ${taskMins % 60}m", color = taskColor, fontWeight = FontWeight.Bold, fontSize = 13.scaledSp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistorySessionPill(taskName: String, sessions: List<TimerSession>, viewModel: TimerViewModel) {
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
                Text(
                    text = "$taskName • ${totalMins}m",
                    fontWeight = FontWeight.Bold,
                    fontFamily = AppFontFamily,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF5D4037)
                )
                
                IconButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        if (sessions.isNotEmpty()) {
                            val start = sessions.minOf { it.startTime }
                            val end = sessions.maxOf { it.endTime } + 1000L
                            viewModel.deleteSessionsInRange(start, end)
                        }
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
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 28.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "$type ($start - $end)", color = Color.Gray, fontSize = 12.scaledSp, modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    viewModel.deleteSession(session)
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Single Session",
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
