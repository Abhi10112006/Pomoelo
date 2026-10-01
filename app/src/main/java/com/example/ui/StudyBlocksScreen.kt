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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import java.text.SimpleDateFormat
import java.util.*

// Safe color helper that avoids any Compose 64-bit color-space packing issues
fun parseBlockColor(colorLong: Long): Color {
    val argb = (colorLong and 0xFFFFFFFFL).toInt()
    return Color(argb)
}

// Reliable RGB perceptual luminance check
fun isColorLight(color: Color): Boolean {
    return (color.red * 0.299f + color.green * 0.587f + color.blue * 0.114f) > 0.55f
}

private val STUDY_BLOCK_PALETTE = listOf(
    Pair("Coral Sunrise", 0xFFF28F75L),
    Pair("Sky Oasis", 0xFF8BB5CAL),
    Pair("Sage Garden", 0xFF9EAC95L),
    Pair("Soft Lilac", 0xFFB39DDBL),
    Pair("Honey Amber", 0xFFFFCA28L),
    Pair("Warm Peach", 0xFFFFAB91L),
    Pair("Mint Fresh", 0xFF80CBC4L),
    Pair("Wild Rose", 0xFFF48FB1L)
)

private val DURATION_PRESETS = listOf(
    Pair("⚡ 15m", 15),
    Pair("🍅 25m", 25),
    Pair("🧠 45m", 45),
    Pair("📚 60m", 60),
    Pair("⏳ 90m", 90)
)

private val REPEAT_OPTIONS = listOf(
    Pair("Once", "None"),
    Pair("Daily ✨", "Daily"),
    Pair("Weekdays 💼", "Weekdays"),
    Pair("Weekly 📅", "Weekly")
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
                    Column(modifier = Modifier.padding(start = 4.dp, top = 6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Study Sanctuary",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Black,
                                fontSize = 26.scaledSp,
                                color = currentTheme.textPrimary,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "✨", fontSize = 20.sp)
                        }
                        Text(
                            text = "Curate your deep work rituals & mindful study hours",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.scaledSp,
                            color = currentTheme.textSecondary
                        )
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
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = bottomPadding + 16.dp, end = 16.dp)
                    .testTag("add_study_block_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Study Block",
                    modifier = Modifier.size(28.dp)
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
                    top = 10.dp,
                    bottom = bottomPadding + 88.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary Metric Pill Card
                item {
                    OverviewSummaryCard(
                        todayCount = todayBlocks.size,
                        totalMinutes = totalPlannedMinutesToday,
                        onQuickFocus = {
                            viewModel.createQuickFocusSession(25, "Quick Focus", currentTheme.primary.value.toLong())
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }

                if (todayBlocks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "TODAY'S RITUALS",
                            count = todayBlocks.size,
                            icon = Icons.Default.Today
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
                        Spacer(modifier = Modifier.height(6.dp))
                        SectionHeader(
                            title = "UPCOMING HORIZONS",
                            count = upcomingBlocks.size,
                            icon = Icons.Default.Upcoming
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
                    text = "Release this study ritual?",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to remove \"${block.name}\"? Past completed sessions in your History will remain safely preserved.",
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
                    Text("Delete Ritual", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = currentFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { blockToDelete = null }) {
                    Text("Keep It", color = currentTheme.textSecondary, fontFamily = currentFont)
                }
            },
            containerColor = currentTheme.surface,
            shape = RoundedCornerShape(22.dp)
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
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp), spotColor = currentTheme.shadowColor)
            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Today's Focus Flow",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.scaledSp,
                    color = currentTheme.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🎯 $todayCount ${if (todayCount == 1) "ritual" else "rituals"}",
                        fontFamily = currentFont,
                        fontSize = 12.scaledSp,
                        color = currentTheme.textSecondary
                    )
                    Text(
                        text = "•",
                        fontSize = 12.scaledSp,
                        color = currentTheme.textSecondary.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "🍅 $totalMinutes min",
                        fontFamily = currentFont,
                        fontSize = 12.scaledSp,
                        fontWeight = FontWeight.SemiBold,
                        color = currentTheme.primary
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
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = currentTheme.pillActiveBg,
                    contentColor = currentTheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
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
            .padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = currentTheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontFamily = currentFont,
            fontWeight = FontWeight.Bold,
            fontSize = 12.scaledSp,
            letterSpacing = 1.2.sp,
            color = currentTheme.textSecondary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(currentTheme.pillActiveBg)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = count.toString(),
                fontSize = 11.scaledSp,
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
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(22.dp), spotColor = currentTheme.shadowColor)
            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .clickable { onEdit() }
            .testTag("study_block_card_${block.id}"),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Elegant vertical accent pill
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(blockColor)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(blockColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = block.name,
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.scaledSp,
                        color = currentTheme.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (block.reminderEnabled) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Mindful reminder on",
                            tint = currentTheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (block.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = block.description,
                        fontFamily = currentFont,
                        fontSize = 12.scaledSp,
                        color = currentTheme.textSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoPill(
                        icon = Icons.Default.Timer,
                        text = "${block.duration}m"
                    )

                    val timeLabel = formatStartTime(block.startTime)
                    InfoPill(
                        icon = Icons.Default.Schedule,
                        text = timeLabel
                    )

                    if (!block.repeatRule.isNullOrBlank() && block.repeatRule != "None") {
                        val displayRepeat = when (block.repeatRule) {
                            "Daily" -> "Every Day"
                            "Weekdays" -> "Mon-Fri"
                            else -> block.repeatRule
                        }
                        InfoPill(
                            icon = Icons.Default.Repeat,
                            text = displayRepeat
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                FilledTonalButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        onStart()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = blockColor.copy(alpha = 0.16f),
                        contentColor = blockColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("start_block_button_${block.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Session",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Focus",
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.scaledSp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Block",
                            tint = currentTheme.textSecondary.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Block",
                            tint = Color(0xFFEF9A9A),
                            modifier = Modifier.size(16.dp)
                        )
                    }
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
        color = currentTheme.pillActiveBg,
        shape = RoundedCornerShape(8.dp),
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
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(26.dp), spotColor = currentTheme.shadowColor)
                .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(26.dp)),
            colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
            shape = RoundedCornerShape(26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(currentTheme.pillActiveBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📚",
                        fontSize = 36.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Your Study Sanctuary",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.scaledSp,
                    color = currentTheme.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "No study blocks scheduled yet. Plan your deep focus rituals and conquer your courses one peaceful session at a time.",
                    fontFamily = currentFont,
                    fontSize = 13.scaledSp,
                    color = currentTheme.textSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.scaledSp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onCreateFirst,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentTheme.primary,
                        contentColor = if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White
                    ),
                    modifier = Modifier.testTag("create_first_study_block_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Craft First Study Block",
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.scaledSp
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
                .background(Color.Black.copy(alpha = 0.42f))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.88f)
                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(26.dp), spotColor = currentTheme.shadowColor)
                    .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(26.dp)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (initialBlock == null) "New Study Ritual" else "Edit Ritual",
                                fontFamily = currentFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.scaledSp,
                                color = currentTheme.textPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "✍️", fontSize = 18.sp)
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = currentTheme.textSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                if (validationError != null && it.isNotBlank()) {
                                    validationError = null
                                }
                            },
                            label = { Text("Ritual / Subject Name *", fontFamily = currentFont) },
                            placeholder = { Text("e.g. Advanced Calculus, World History", fontFamily = currentFont) },
                            isError = validationError != null,
                            supportingText = {
                                if (validationError != null) {
                                    Text(text = validationError ?: "", color = MaterialTheme.colorScheme.error, fontFamily = currentFont)
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("study_block_name_input"),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

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
                            label = { Text("Intentions & Notes (Optional)", fontFamily = currentFont) },
                            placeholder = { Text("e.g. Practice questions 1-15, review summary", fontFamily = currentFont) },
                            maxLines = 2,
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
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

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Color Essence",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.scaledSp,
                            color = currentTheme.textPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            STUDY_BLOCK_PALETTE.forEach { (colorName, colorValue) ->
                                val isSelected = selectedColor == colorValue
                                val composeColor = parseBlockColor(colorValue)
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(composeColor)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) currentTheme.textPrimary else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            selectedColor = colorValue
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = colorName,
                                            tint = if (isColorLight(composeColor)) Color.Black else Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Focus Duration",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.scaledSp,
                            color = currentTheme.textPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DURATION_PRESETS.forEach { (label, mins) ->
                                val isSelected = selectedDuration == mins
                                Surface(
                                    selected = isSelected,
                                    onClick = {
                                        try {
                                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (e: Exception) {}
                                        selectedDuration = mins
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) currentTheme.primary else currentTheme.pillActiveBg,
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (isSelected) currentTheme.primary else currentTheme.cardBorder
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 9.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            fontFamily = currentFont,
                                            fontSize = 12.scaledSp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) {
                                                if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White
                                            } else currentTheme.textPrimary,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Timing & Schedule",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.scaledSp,
                            color = currentTheme.textPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                selected = isFlexibleTime,
                                onClick = { isFlexibleTime = true },
                                label = { Text("Flexible / Anytime 🌱", fontFamily = currentFont) },
                                leadingIcon = {
                                    Icon(Icons.Default.HourglassEmpty, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                            FilterChip(
                                selected = !isFlexibleTime,
                                onClick = { isFlexibleTime = false },
                                label = {
                                    val ampm = if (scheduledHour < 12) "AM" else "PM"
                                    val displayHour = when (val h = scheduledHour % 12) {
                                        0 -> 12
                                        else -> h
                                    }
                                    val displayMinute = String.format("%02d", scheduledMinute)
                                    Text("$displayHour:$displayMinute $ampm ⏰", fontFamily = currentFont)
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp))
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        if (!isFlexibleTime) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        scheduledHour = (scheduledHour + 1) % 24
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Hour: ${String.format("%02d", scheduledHour)}", fontFamily = currentFont, fontSize = 12.scaledSp)
                                }
                                OutlinedButton(
                                    onClick = {
                                        scheduledMinute = (scheduledMinute + 15) % 60
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Minute: ${String.format("%02d", scheduledMinute)}", fontFamily = currentFont, fontSize = 12.scaledSp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Recurrence Rhythm",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.scaledSp,
                            color = currentTheme.textPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            REPEAT_OPTIONS.forEach { (label, value) ->
                                val isSelected = selectedRepeatRule == value
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedRepeatRule = value },
                                    label = { Text(label, fontFamily = currentFont, fontSize = 12.scaledSp) },
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(currentTheme.pillActiveBg)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = currentTheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Mindful Alert",
                                        fontFamily = currentFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.scaledSp,
                                        color = currentTheme.textPrimary
                                    )
                                    Text(
                                        text = "Gentle nudge when your focus session begins",
                                        fontFamily = currentFont,
                                        fontSize = 11.scaledSp,
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
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            title = {
                                Text(
                                    text = "Enable Focus Alerts 🔔",
                                    fontFamily = currentFont,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.textPrimary
                                )
                            },
                            text = {
                                Text(
                                    text = "PomoPal needs notification permission to gently alert you when it's time to begin your scheduled study rituals.",
                                    fontFamily = currentFont,
                                    color = currentTheme.textSecondary,
                                    fontSize = 13.scaledSp
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
                                    shape = RoundedCornerShape(14.dp)
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
                            shape = RoundedCornerShape(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
                                    validationError = "Please enter a ritual name"
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
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentTheme.primary,
                                contentColor = if (isColorLight(currentTheme.primary)) Color(0xFF1E1E1E) else Color.White
                            ),
                            modifier = Modifier.testTag("save_study_block_button")
                        ) {
                            Text(
                                text = if (initialBlock == null) "Create Ritual ✨" else "Save Ritual ✨",
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
