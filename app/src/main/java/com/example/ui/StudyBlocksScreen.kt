package com.example.ui

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.data.StudyBlock
import com.example.ui.components.scaledSp
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import java.text.SimpleDateFormat
import java.util.*

// Safe color helper avoiding Compose 64-bit color-space packing issues
fun parseBlockColor(colorLong: Long): Color {
    val argb = (colorLong and 0xFFFFFFFFL).toInt()
    return Color(argb)
}

// Reliable RGB perceptual luminance check
fun isColorLight(color: Color): Boolean {
    return (color.red * 0.299f + color.green * 0.587f + color.blue * 0.114f) > 0.55f
}

private val STUDY_BLOCK_PALETTE = listOf(
    Pair("Coral", 0xFFF28F75L),
    Pair("Sky Blue", 0xFF8BB5CAL),
    Pair("Sage Green", 0xFF9EAC95L),
    Pair("Lilac", 0xFFB39DDBL),
    Pair("Honey Amber", 0xFFFFCA28L),
    Pair("Warm Peach", 0xFFFFAB91L),
    Pair("Mint Fresh", 0xFF80CBC4L),
    Pair("Wild Rose", 0xFFF48FB1L)
)

private val DURATION_PRESETS = listOf(15, 25, 45, 60, 90)

private val REPEAT_OPTIONS = listOf(
    Pair("Once", "None"),
    Pair("Daily", "Daily"),
    Pair("Weekdays", "Weekdays"),
    Pair("Weekly", "Weekly")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyBlocksScreen(
    viewModel: TimerViewModel,
    navController: NavController,
    bottomPadding: Dp = 0.dp
) {
    val studyBlocks by viewModel.allStudyBlocks.collectAsState()
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val view = LocalView.current

    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingBlock by remember { mutableStateOf<StudyBlock?>(null) }
    var blockToDelete by remember { mutableStateOf<StudyBlock?>(null) }

    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val startOfToday = cal.timeInMillis
    cal.add(Calendar.DAY_OF_YEAR, 1)
    val startOfTomorrow = cal.timeInMillis

    val (todayBlocks, upcomingBlocks) = remember(studyBlocks, startOfToday, startOfTomorrow) {
        val upcoming = studyBlocks.filter { block ->
            (block.repeatRule == null || block.repeatRule == "None") && block.startTime >= startOfTomorrow
        }
        val today = studyBlocks.filter { it !in upcoming }
        Pair(today, upcoming)
    }

    val totalPlannedMinutesToday = remember(todayBlocks) {
        todayBlocks.sumOf { it.duration }
    }

    Scaffold(
        containerColor = currentTheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.padding(start = 4.dp, top = 2.dp)) {
                        Text(
                            text = "POMOPAL SANCTUARY",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.scaledSp,
                            letterSpacing = 1.8.sp,
                            color = currentTheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Study Blocks",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.scaledSp,
                                color = currentTheme.textPrimary
                            )
                            if (studyBlocks.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(currentTheme.pillActiveBg)
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${studyBlocks.size}",
                                        fontFamily = currentFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.scaledSp,
                                        color = currentTheme.primary
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    editingBlock = null
                    showAddEditDialog = true
                },
                containerColor = currentTheme.primary,
                contentColor = if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(bottom = bottomPadding + 14.dp, end = 12.dp)
                    .size(56.dp)
                    .testTag("add_study_block_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Study Block",
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    ) { paddingValues ->
        if (studyBlocks.isEmpty()) {
            EmptyStudyBlocksView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = bottomPadding),
                onCreateFirst = {
                    editingBlock = null
                    showAddEditDialog = true
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 4.dp,
                    bottom = bottomPadding + 88.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Sleek Overview Summary Card
                item {
                    OverviewSummaryCard(
                        todayCount = todayBlocks.size,
                        totalMinutes = totalPlannedMinutesToday,
                        onQuickFocus = {
                            viewModel.createQuickFocusSession(25, "Quick Focus", (currentTheme.primary.toArgb().toLong() and 0xFFFFFFFFL))
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }

                if (todayBlocks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "TODAY'S SCHEDULE",
                            count = todayBlocks.size,
                            icon = Icons.Outlined.CalendarToday
                        )
                    }
                    items(todayBlocks, key = { it.id }) { block ->
                        StudyBlockCard(
                            block = block,
                            onStart = {
                                viewModel.startSessionFromStudyBlock(block)
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onEdit = {
                                editingBlock = block
                                showAddEditDialog = true
                            },
                            onDelete = {
                                blockToDelete = block
                            }
                        )
                    }
                }

                if (upcomingBlocks.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        SectionHeader(
                            title = "UPCOMING SCHEDULE",
                            count = upcomingBlocks.size,
                            icon = Icons.Outlined.Upcoming
                        )
                    }
                    items(upcomingBlocks, key = { it.id }) { block ->
                        StudyBlockCard(
                            block = block,
                            onStart = {
                                viewModel.startSessionFromStudyBlock(block)
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onEdit = {
                                editingBlock = block
                                showAddEditDialog = true
                            },
                            onDelete = {
                                blockToDelete = block
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddEditDialog) {
        AddEditStudyBlockDialog(
            initialBlock = editingBlock,
            onDismiss = { showAddEditDialog = false },
            onSave = { name, description, color, startTime, duration, repeatRule, reminderEnabled ->
                val current = editingBlock
                if (current == null) {
                    viewModel.createStudyBlock(
                        name = name,
                        description = description,
                        color = color,
                        startTime = startTime,
                        duration = duration,
                        repeatRule = repeatRule,
                        reminderEnabled = reminderEnabled
                    )
                } else {
                    viewModel.updateStudyBlock(
                        current.copy(
                            name = name,
                            description = description,
                            color = color,
                            startTime = startTime,
                            duration = duration,
                            repeatRule = repeatRule,
                            reminderEnabled = reminderEnabled
                        )
                    )
                }
                showAddEditDialog = false
            }
        )
    }

    blockToDelete?.let { block ->
        AlertDialog(
            onDismissRequest = { blockToDelete = null },
            title = {
                Text(
                    text = "Delete Study Block?",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove \"${block.name}\"? Past completed sessions in your History remain preserved.",
                    fontFamily = currentFont,
                    color = currentTheme.textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteStudyBlock(block)
                        blockToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = currentFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { blockToDelete = null }) {
                    Text("Cancel", color = currentTheme.textSecondary, fontFamily = currentFont)
                }
            },
            containerColor = currentTheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun OverviewSummaryCard(
    todayCount: Int,
    totalMinutes: Int,
    onQuickFocus: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val view = LocalView.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, currentTheme.cardBorder.copy(alpha = 0.6f), RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TODAY'S COMMITMENT",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.scaledSp,
                    letterSpacing = 1.sp,
                    color = currentTheme.textSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${totalMinutes}m",
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.scaledSp,
                        color = currentTheme.textPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "planned across $todayCount ${if (todayCount == 1) "block" else "blocks"}",
                        fontFamily = currentFont,
                        fontSize = 12.scaledSp,
                        color = currentTheme.textSecondary
                    )
                }
            }

            FilledTonalButton(
                onClick = {
                    try {
                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                    } catch (e: Exception) {}
                    onQuickFocus()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = currentTheme.primary.copy(alpha = 0.15f),
                    contentColor = currentTheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Quick Focus",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Quick Focus",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.scaledSp
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Int,
    icon: ImageVector
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = currentTheme.primary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontFamily = currentFont,
            fontWeight = FontWeight.Bold,
            fontSize = 11.scaledSp,
            letterSpacing = 1.2.sp,
            color = currentTheme.textSecondary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(currentTheme.pillActiveBg)
                .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
            Text(
                text = count.toString(),
                fontSize = 10.scaledSp,
                fontWeight = FontWeight.Bold,
                color = currentTheme.primary
            )
        }
    }
}

@Composable
private fun StudyBlockCard(
    block: StudyBlock,
    onStart: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val blockColor = parseBlockColor(block.color)
    val view = LocalView.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, currentTheme.cardBorder.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable { onEdit() }
            .testTag("study_block_card_${block.id}"),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Color strip + Name + Reminder Status + Quick Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Colored dot accent
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(blockColor)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = block.name,
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.scaledSp,
                    color = currentTheme.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (block.reminderEnabled) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = currentTheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(22.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Reminder active",
                                tint = currentTheme.primary,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Alert",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.scaledSp,
                                color = currentTheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Block",
                        tint = currentTheme.textSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(15.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete Block",
                        tint = Color(0xFFEF9A9A),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Optional Note / Description
            if (block.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = block.description,
                    fontFamily = currentFont,
                    fontSize = 12.scaledSp,
                    color = currentTheme.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.scaledSp,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Footer Row: Metadata Chips + Prominent Focus Action Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Info badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    InfoPill(
                        icon = Icons.Outlined.Timer,
                        text = "${block.duration}m"
                    )

                    val timeLabel = formatStartTime(block.startTime)
                    InfoPill(
                        icon = Icons.Outlined.Schedule,
                        text = timeLabel
                    )

                    if (!block.repeatRule.isNullOrBlank() && block.repeatRule != "None") {
                        val displayRepeat = when (block.repeatRule) {
                            "Daily" -> "Daily"
                            "Weekdays" -> "Mon-Fri"
                            else -> block.repeatRule
                        }
                        InfoPill(
                            icon = Icons.Outlined.Repeat,
                            text = displayRepeat
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Prominent primary "Focus" button
                Button(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        onStart()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blockColor,
                        contentColor = if (isColorLight(blockColor)) Color(0xFF1E1E1E) else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 7.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("start_block_button_${block.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Focus",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Focus",
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.scaledSp
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoPill(
    icon: ImageVector,
    text: String
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Surface(
        color = currentTheme.pillActiveBg.copy(alpha = 0.7f),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.height(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 7.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = currentTheme.textSecondary,
                modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontFamily = currentFont,
                fontWeight = FontWeight.Medium,
                fontSize = 11.scaledSp,
                color = currentTheme.textPrimary
            )
        }
    }
}

@Composable
private fun EmptyStudyBlocksView(
    modifier: Modifier = Modifier,
    onCreateFirst: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Box(
        modifier = modifier.padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, currentTheme.cardBorder.copy(alpha = 0.6f), RoundedCornerShape(22.dp)),
            colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(currentTheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                        contentDescription = null,
                        tint = currentTheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "No Study Blocks Yet",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.scaledSp,
                    color = currentTheme.textPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Plan your deep work sessions, assign custom durations, and schedule mindful alerts for distraction-free study.",
                    fontFamily = currentFont,
                    fontSize = 12.scaledSp,
                    color = currentTheme.textSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.scaledSp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onCreateFirst,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentTheme.primary,
                        contentColor = if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                    modifier = Modifier.testTag("create_first_study_block_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Create First Block",
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.scaledSp
                    )
                }
            }
        }
    }
}

@Composable
fun AddEditStudyBlockDialog(
    initialBlock: StudyBlock?,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        description: String,
        color: Long,
        startTime: Long,
        duration: Int,
        repeatRule: String?,
        reminderEnabled: Boolean
    ) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current
    val context = androidx.compose.ui.platform.LocalContext.current

    var name by remember(initialBlock) { mutableStateOf(initialBlock?.name ?: "") }
    var description by remember(initialBlock) { mutableStateOf(initialBlock?.description ?: "") }
    var selectedColor by remember(initialBlock) { mutableStateOf(initialBlock?.color ?: STUDY_BLOCK_PALETTE[0].second) }
    var selectedDuration by remember(initialBlock) { mutableIntStateOf(initialBlock?.duration ?: 25) }
    var isCustomDuration by remember(initialBlock) {
        mutableStateOf(initialBlock != null && initialBlock.duration !in DURATION_PRESETS)
    }
    var customDurationInput by remember(initialBlock) {
        mutableStateOf(if (initialBlock != null && initialBlock.duration !in DURATION_PRESETS) initialBlock.duration.toString() else "50")
    }

    var selectedRepeatRule by remember(initialBlock) { mutableStateOf(initialBlock?.repeatRule ?: "None") }
    var reminderEnabled by remember(initialBlock) { mutableStateOf(initialBlock?.reminderEnabled ?: false) }

    val initialHour = remember(initialBlock) {
        val cal = Calendar.getInstance()
        if (initialBlock != null && initialBlock.startTime > 0L) {
            cal.timeInMillis = initialBlock.startTime
            cal.get(Calendar.HOUR_OF_DAY)
        } else {
            9
        }
    }
    val initialMinute = remember(initialBlock) {
        val cal = Calendar.getInstance()
        if (initialBlock != null && initialBlock.startTime > 0L) {
            cal.timeInMillis = initialBlock.startTime
            cal.get(Calendar.MINUTE)
        } else {
            0
        }
    }

    var scheduledHour by remember(initialBlock) { mutableIntStateOf(initialHour) }
    var scheduledMinute by remember(initialBlock) { mutableIntStateOf(initialMinute) }
    var isFlexibleTime by remember(initialBlock) { mutableStateOf(initialBlock?.startTime == null || initialBlock.startTime == 0L) }
    var validationError by remember { mutableStateOf<String?>(null) }

    var showPermissionRationale by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            reminderEnabled = true
            if (isFlexibleTime) {
                isFlexibleTime = false
            }
        } else {
            reminderEnabled = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    })
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.88f)
                    .imePadding()
                    .border(1.dp, currentTheme.cardBorder.copy(alpha = 0.8f), RoundedCornerShape(24.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Header Bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = if (initialBlock == null) "New Study Block" else "Edit Study Block",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.scaledSp,
                                color = currentTheme.textPrimary
                            )
                            Text(
                                text = "Configure your focus session parameters",
                                fontFamily = currentFont,
                                fontSize = 11.scaledSp,
                                color = currentTheme.textSecondary
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = currentTheme.textSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Section 1: Inputs
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                if (validationError != null && it.isNotBlank()) {
                                    validationError = null
                                }
                            },
                            label = { Text("Block Name *", fontFamily = currentFont) },
                            placeholder = { Text("e.g. Linear Algebra, Thesis Chapter", fontFamily = currentFont) },
                            isError = validationError != null,
                            supportingText = {
                                if (validationError != null) {
                                    Text(text = validationError ?: "", color = MaterialTheme.colorScheme.error, fontFamily = currentFont)
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("study_block_name_input"),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            )
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { newText ->
                                if (newText.contains('\n')) {
                                    description = newText.replace("\n", "").trim()
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                } else {
                                    description = newText
                                }
                            },
                            label = { Text("Optional Notes or Intentions", fontFamily = currentFont) },
                            placeholder = { Text("e.g. Solve problem set 3, review key terms", fontFamily = currentFont) },
                            maxLines = 2,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("study_block_desc_input"),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            )
                        )

                        // Section 2: Color Palette
                        Column {
                            Text(
                                text = "COLOR ACCENT",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.scaledSp,
                                letterSpacing = 1.sp,
                                color = currentTheme.textSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                STUDY_BLOCK_PALETTE.forEach { (colorName, colorValue) ->
                                    val isSelected = selectedColor == colorValue
                                    val composeColor = parseBlockColor(colorValue)
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(composeColor)
                                            .border(
                                                width = if (isSelected) 2.5.dp else 0.dp,
                                                color = if (isSelected) currentTheme.textPrimary else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable { selectedColor = colorValue },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = colorName,
                                                tint = if (isColorLight(composeColor)) Color.Black else Color.White,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Section 3: Focus Duration (Presets + Custom)
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "FOCUS DURATION",
                                    fontFamily = currentFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.scaledSp,
                                    letterSpacing = 1.sp,
                                    color = currentTheme.textSecondary
                                )
                                Text(
                                    text = "${selectedDuration} min total",
                                    fontFamily = currentFont,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.scaledSp,
                                    color = currentTheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                DURATION_PRESETS.forEach { mins ->
                                    val isSelected = !isCustomDuration && selectedDuration == mins
                                    Surface(
                                        selected = isSelected,
                                        onClick = {
                                            try {
                                                view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                            } catch (e: Exception) {}
                                            isCustomDuration = false
                                            selectedDuration = mins
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) currentTheme.primary else currentTheme.pillActiveBg,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (isSelected) currentTheme.primary else currentTheme.cardBorder.copy(alpha = 0.5f)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${mins}m",
                                                fontFamily = currentFont,
                                                fontSize = 12.scaledSp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) (if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White) else currentTheme.textPrimary
                                            )
                                        }
                                    }
                                }

                                // Custom option pill
                                Surface(
                                    selected = isCustomDuration,
                                    onClick = {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        isCustomDuration = true
                                        val parsed = customDurationInput.toIntOrNull() ?: 50
                                        selectedDuration = parsed.coerceIn(5, 360)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isCustomDuration) currentTheme.primary else currentTheme.pillActiveBg,
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isCustomDuration) currentTheme.primary else currentTheme.cardBorder.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.weight(1.3f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (isCustomDuration) "${selectedDuration}m ⚙" else "Custom",
                                            fontFamily = currentFont,
                                            fontSize = 11.scaledSp,
                                            fontWeight = if (isCustomDuration) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isCustomDuration) (if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White) else currentTheme.textPrimary
                                        )
                                    }
                                }
                            }

                            // If custom duration is selected, show smooth inline stepper
                            if (isCustomDuration) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(currentTheme.pillActiveBg.copy(alpha = 0.6f))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Custom minutes:",
                                        fontFamily = currentFont,
                                        fontSize = 11.scaledSp,
                                        color = currentTheme.textSecondary
                                    )

                                    OutlinedButton(
                                        onClick = {
                                            val newVal = (selectedDuration - 5).coerceAtLeast(5)
                                            selectedDuration = newVal
                                            customDurationInput = newVal.toString()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("-5", fontSize = 11.scaledSp, fontFamily = currentFont)
                                    }

                                    OutlinedTextField(
                                        value = customDurationInput,
                                        onValueChange = { str ->
                                            val filtered = str.filter { it.isDigit() }.take(3)
                                            customDurationInput = filtered
                                            val parsed = filtered.toIntOrNull()
                                            if (parsed != null && parsed > 0) {
                                                selectedDuration = parsed.coerceIn(5, 360)
                                            }
                                        },
                                        singleLine = true,
                                        shape = RoundedCornerShape(8.dp),
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                keyboardController?.hide()
                                                focusManager.clearFocus()
                                            }
                                        ),
                                        modifier = Modifier
                                            .width(72.dp)
                                            .height(48.dp)
                                    )

                                    OutlinedButton(
                                        onClick = {
                                            val newVal = (selectedDuration + 5).coerceAtMost(360)
                                            selectedDuration = newVal
                                            customDurationInput = newVal.toString()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("+5", fontSize = 11.scaledSp, fontFamily = currentFont)
                                    }
                                }
                            }
                        }

                        // Section 4: Schedule Timing
                        Column {
                            Text(
                                text = "SCHEDULE TIMING",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.scaledSp,
                                letterSpacing = 1.sp,
                                color = currentTheme.textSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    selected = isFlexibleTime,
                                    onClick = { isFlexibleTime = true },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isFlexibleTime) currentTheme.primary else currentTheme.pillActiveBg,
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isFlexibleTime) currentTheme.primary else currentTheme.cardBorder.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.HourglassEmpty,
                                            contentDescription = null,
                                            tint = if (isFlexibleTime) (if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White) else currentTheme.textSecondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Flexible Time",
                                            fontFamily = currentFont,
                                            fontSize = 12.scaledSp,
                                            fontWeight = if (isFlexibleTime) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isFlexibleTime) (if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White) else currentTheme.textPrimary
                                        )
                                    }
                                }

                                Surface(
                                    selected = !isFlexibleTime,
                                    onClick = { isFlexibleTime = false },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (!isFlexibleTime) currentTheme.primary else currentTheme.pillActiveBg,
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (!isFlexibleTime) currentTheme.primary else currentTheme.cardBorder.copy(alpha = 0.5f)
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    val ampm = if (scheduledHour < 12) "AM" else "PM"
                                    val displayHour = when (val h = scheduledHour % 12) {
                                        0 -> 12
                                        else -> h
                                    }
                                    val displayMinute = String.format("%02d", scheduledMinute)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Schedule,
                                            contentDescription = null,
                                            tint = if (!isFlexibleTime) (if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White) else currentTheme.textSecondary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "$displayHour:$displayMinute $ampm",
                                            fontFamily = currentFont,
                                            fontSize = 12.scaledSp,
                                            fontWeight = if (!isFlexibleTime) FontWeight.Bold else FontWeight.Medium,
                                            color = if (!isFlexibleTime) (if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White) else currentTheme.textPrimary
                                        )
                                    }
                                }
                            }

                            if (!isFlexibleTime) {
                                Spacer(modifier = Modifier.height(10.dp))
                                PremiumTimeWheelPicker(
                                    hour24 = scheduledHour,
                                    minute = scheduledMinute,
                                    onTimeChanged = { h, m ->
                                        scheduledHour = h
                                        scheduledMinute = m
                                    }
                                )
                            }
                        }

                        // Section 5: Recurrence
                        Column {
                            Text(
                                text = "RECURRENCE",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.scaledSp,
                                letterSpacing = 1.sp,
                                color = currentTheme.textSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                REPEAT_OPTIONS.forEach { (label, value) ->
                                    val isSelected = selectedRepeatRule == value
                                    Surface(
                                        selected = isSelected,
                                        onClick = { selectedRepeatRule = value },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isSelected) currentTheme.primary else currentTheme.pillActiveBg,
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = if (isSelected) currentTheme.primary else currentTheme.cardBorder.copy(alpha = 0.5f)
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 7.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontFamily = currentFont,
                                                fontSize = 11.scaledSp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) (if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White) else currentTheme.textPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Section 6: Mindful Alert Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(currentTheme.pillActiveBg.copy(alpha = 0.7f))
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = null,
                                    tint = currentTheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Mindful Alert",
                                        fontFamily = currentFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.scaledSp,
                                        color = currentTheme.textPrimary
                                    )
                                    Text(
                                        text = "Gentle notification at scheduled time",
                                        fontFamily = currentFont,
                                        fontSize = 10.scaledSp,
                                        color = currentTheme.textSecondary
                                    )
                                }
                            }
                            Switch(
                                checked = reminderEnabled,
                                onCheckedChange = { enable ->
                                    if (enable) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(
                                                context,
                                                android.Manifest.permission.POST_NOTIFICATIONS
                                            ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                                            if (!hasPermission) {
                                                showPermissionRationale = true
                                            } else {
                                                reminderEnabled = true
                                                if (isFlexibleTime) {
                                                    isFlexibleTime = false
                                                }
                                            }
                                        } else {
                                            reminderEnabled = true
                                            if (isFlexibleTime) {
                                                isFlexibleTime = false
                                            }
                                        }
                                    } else {
                                        reminderEnabled = false
                                    }
                                }
                            )
                        }
                    }

                    if (showPermissionRationale) {
                        AlertDialog(
                            onDismissRequest = { showPermissionRationale = false },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = currentTheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            },
                            title = {
                                Text(
                                    text = "Enable Focus Alerts",
                                    fontFamily = currentFont,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.textPrimary
                                )
                            },
                            text = {
                                Text(
                                    text = "PomoPal needs notification permission to alert you when your scheduled study blocks begin.",
                                    fontFamily = currentFont,
                                    color = currentTheme.textSecondary,
                                    fontSize = 12.scaledSp
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showPermissionRationale = false
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = currentTheme.primary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Allow Alerts",
                                        color = if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = currentFont
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showPermissionRationale = false }) {
                                    Text("Not Now", color = currentTheme.textSecondary, fontFamily = currentFont)
                                }
                            },
                            containerColor = currentTheme.surface,
                            shape = RoundedCornerShape(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dialog Actions
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", fontFamily = currentFont, color = currentTheme.textSecondary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (name.trim().isEmpty()) {
                                    validationError = "Please enter a block name"
                                    return@Button
                                }
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}

                                val targetStartTime = if (isFlexibleTime && !reminderEnabled) {
                                    0L
                                } else {
                                    val c = Calendar.getInstance()
                                    c.set(Calendar.HOUR_OF_DAY, scheduledHour)
                                    c.set(Calendar.MINUTE, scheduledMinute)
                                    c.set(Calendar.SECOND, 0)
                                    c.set(Calendar.MILLISECOND, 0)
                                    c.timeInMillis
                                }

                                onSave(
                                    name.trim(),
                                    description.trim(),
                                    selectedColor,
                                    targetStartTime,
                                    selectedDuration,
                                    if (selectedRepeatRule == "None") null else selectedRepeatRule,
                                    reminderEnabled
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentTheme.primary,
                                contentColor = if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White
                            ),
                            modifier = Modifier.testTag("save_study_block_button")
                        ) {
                            Text(
                                text = if (initialBlock == null) "Create Block" else "Save Block",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatStartTime(timeMillis: Long): String {
    if (timeMillis == 0L) return "Flexible"
    val cal = Calendar.getInstance().apply { this.timeInMillis = timeMillis }
    val format = SimpleDateFormat("h:mm a", Locale.getDefault())
    return format.format(cal.time)
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun TimeWheelColumn(
    value: Int,
    range: List<Int>,
    label: String,
    format: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemsCount = range.size
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val coroutineScope = rememberCoroutineScope()
    val itemHeight = 36.dp

    val initialIndex = remember {
        val middleBase = (Int.MAX_VALUE / 2 / itemsCount) * itemsCount
        val offset = range.indexOf(value).coerceAtLeast(0)
        middleBase + offset - 1
    }

    val listState = androidx.compose.foundation.lazy.rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior(lazyListState = listState)

    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) {
                listState.firstVisibleItemIndex + 1
            } else {
                val center = layoutInfo.viewportEndOffset / 2
                val centerItem = layoutInfo.visibleItemsInfo.minByOrNull {
                    kotlin.math.abs((it.offset + it.size / 2) - center)
                }
                centerItem?.index ?: (listState.firstVisibleItemIndex + 1)
            }
        }
    }

    LaunchedEffect(centerIndex) {
        val itemVal = range[centerIndex % itemsCount]
        if (itemVal != value) {
            onValueChange(itemVal)
            try {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
            } catch (e: Exception) {}
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 11.scaledSp,
            color = currentTheme.textSecondary,
            fontWeight = FontWeight.Bold,
            fontFamily = currentFont,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(currentTheme.pillActiveBg.copy(alpha = 0.5f))
                .border(1.dp, currentTheme.cardBorder.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .padding(vertical = 4.dp, horizontal = 4.dp)
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(centerIndex)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Increase $label",
                    tint = currentTheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .height(108.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Center Selection Lens Highlight
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(itemHeight)
                        .clip(RoundedCornerShape(8.dp))
                        .background(currentTheme.primary.copy(alpha = 0.12f))
                        .border(1.dp, currentTheme.primary.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                )

                androidx.compose.foundation.lazy.LazyColumn(
                    state = listState,
                    flingBehavior = flingBehavior,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(Int.MAX_VALUE) { index ->
                        val itemVal = range[index % itemsCount]
                        val isCenter = index == centerIndex
                        val alpha = if (isCenter) 1f else 0.3f
                        val textSize = if (isCenter) 22 else 16
                        val fontWeight = if (isCenter) FontWeight.ExtraBold else FontWeight.Medium

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(itemHeight)
                                .clickable {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem((index - 1).coerceAtLeast(0))
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = String.format(format, itemVal),
                                fontSize = textSize.scaledSp,
                                fontWeight = fontWeight,
                                color = if (isCenter) currentTheme.primary else currentTheme.textSecondary.copy(alpha = alpha),
                                fontFamily = MonospaceFontFamily,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Top & Bottom gradient fade masks
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    currentTheme.surface,
                                    currentTheme.surface.copy(alpha = 0f)
                                )
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    currentTheme.surface.copy(alpha = 0f),
                                    currentTheme.surface
                                )
                            )
                        )
                )
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        val target = if (centerIndex - 2 < 0) 0 else centerIndex - 2
                        listState.animateScrollToItem(target)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Decrease $label",
                    tint = currentTheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PremiumTimeWheelPicker(
    hour24: Int,
    minute: Int,
    onTimeChanged: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val view = androidx.compose.ui.platform.LocalView.current

    val isPm = hour24 >= 12
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    val hoursList = remember { (1..12).toList() }
    val minutesList = remember { (0..59).toList() }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = currentTheme.backgroundSecondary.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.7f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live formatted time showcase pill
            val previewAmPm = if (isPm) "PM" else "AM"
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = currentTheme.primary.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, currentTheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = currentTheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = String.format("Selected Time: %02d:%02d %s", hour12, minute, previewAmPm),
                        fontFamily = MonospaceFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.scaledSp,
                        color = currentTheme.primary
                    )
                }
            }

            // Wheel Row: Hour Wheel, Colon, Minute Wheel, AM/PM Segmented Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Hours Wheel
                TimeWheelColumn(
                    value = hour12,
                    range = hoursList,
                    label = "HOUR",
                    format = "%02d",
                    onValueChange = { newHour12 ->
                        val newHour24 = if (isPm) {
                            if (newHour12 == 12) 12 else newHour12 + 12
                        } else {
                            if (newHour12 == 12) 0 else newHour12
                        }
                        onTimeChanged(newHour24, minute)
                    },
                    modifier = Modifier.weight(1.2f)
                )

                // Colon Separator
                Text(
                    text = ":",
                    fontSize = 24.scaledSp,
                    fontWeight = FontWeight.Black,
                    color = currentTheme.primary,
                    fontFamily = MonospaceFontFamily,
                    modifier = Modifier.padding(top = 18.dp)
                )

                // Minutes Wheel
                TimeWheelColumn(
                    value = minute,
                    range = minutesList,
                    label = "MINUTE",
                    format = "%02d",
                    onValueChange = { newMinute ->
                        onTimeChanged(hour24, newMinute)
                    },
                    modifier = Modifier.weight(1.2f)
                )

                // Period (AM/PM) Segmented Box
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "PERIOD",
                        fontSize = 11.scaledSp,
                        color = currentTheme.textSecondary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Column(
                        modifier = Modifier
                            .height(176.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(currentTheme.pillActiveBg.copy(alpha = 0.5f))
                            .border(1.dp, currentTheme.cardBorder.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                            .padding(4.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // AM Pill
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!isPm) currentTheme.primary else Color.Transparent)
                                .clickable {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    if (isPm) {
                                        val newHour24 = if (hour12 == 12) 0 else hour12
                                        onTimeChanged(newHour24, minute)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AM",
                                fontSize = 13.scaledSp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont,
                                color = if (!isPm) {
                                    if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White
                                } else currentTheme.textSecondary
                            )
                        }

                        // PM Pill
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isPm) currentTheme.primary else Color.Transparent)
                                .clickable {
                                    try {
                                        view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (e: Exception) {}
                                    if (!isPm) {
                                        val newHour24 = if (hour12 == 12) 12 else hour12 + 12
                                        onTimeChanged(newHour24, minute)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PM",
                                fontSize = 13.scaledSp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont,
                                color = if (isPm) {
                                    if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White
                                } else currentTheme.textSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}
