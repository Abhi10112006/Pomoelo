package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

private val STUDY_BLOCK_PALETTE = listOf(
    Pair("Coral", 0xFFF28F75L),
    Pair("Sky Blue", 0xFF8BB5CAL),
    Pair("Sage", 0xFF9EAC95L),
    Pair("Lavender", 0xFFB39DDBL),
    Pair("Amber", 0xFFFFCA28L),
    Pair("Peach", 0xFFFFAB91L),
    Pair("Teal", 0xFF80CBC4L),
    Pair("Rose", 0xFFF48FB1L)
)

private val DURATION_PRESETS = listOf(15, 25, 45, 60, 90)
private val REPEAT_RULES = listOf("None", "Daily", "Weekdays", "Weekly")

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

    Scaffold(
        containerColor = currentTheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(modifier = Modifier.padding(start = 4.dp, top = 6.dp)) {
                        Text(
                            text = "Study Blocks 📚",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Black,
                            fontSize = 26.scaledSp,
                            color = currentTheme.textPrimary,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Structured focus blocks for deep work & learning",
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
                contentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = bottomPadding + 16.dp, end = 16.dp)
                    .testTag("add_study_block_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Study Block",
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
                    top = 12.dp,
                    bottom = bottomPadding + 88.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (todayBlocks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "TODAY",
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
                        Spacer(modifier = Modifier.height(8.dp))
                        SectionHeader(
                            title = "UPCOMING",
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
                    text = "Delete Study Block?",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${block.name}\"? Your existing session history will remain intact.",
                    fontFamily = currentFont,
                    color = currentTheme.textSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteStudyBlock(block)
                        blockToDelete = null
                    }
                ) {
                    Text("Delete", color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { blockToDelete = null }) {
                    Text("Cancel", color = currentTheme.textSecondary)
                }
            },
            containerColor = currentTheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector
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
    val blockColor = Color(block.color.toULong())
    val view = LocalView.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(20.dp), spotColor = currentTheme.shadowColor)
            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onEdit() }
            .testTag("study_block_card_${block.id}"),
        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(64.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(blockColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
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
                            contentDescription = "Reminder enabled",
                            tint = currentTheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                if (block.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoChip(
                        icon = Icons.Default.Timer,
                        text = "${block.duration} min"
                    )

                    val timeLabel = formatStartTime(block.startTime)
                    InfoChip(
                        icon = Icons.Default.AccessTime,
                        text = timeLabel
                    )

                    if (!block.repeatRule.isNullOrBlank() && block.repeatRule != "None") {
                        InfoChip(
                            icon = Icons.Default.Repeat,
                            text = block.repeatRule
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                FilledTonalButton(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        onStart()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = blockColor.copy(alpha = 0.18f),
                        contentColor = blockColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("start_block_button_${block.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Start",
                        fontFamily = currentFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.scaledSp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Block",
                            tint = currentTheme.textSecondary.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
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
private fun InfoChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = currentTheme.textSecondary,
                modifier = Modifier.size(12.dp)
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
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp), spotColor = currentTheme.shadowColor)
                .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(currentTheme.pillActiveBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📖",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No Study Blocks Yet",
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.scaledSp,
                    color = currentTheme.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Organize subjects, deep-work sessions, or reading routines with scheduled study blocks.",
                    fontFamily = currentFont,
                    fontSize = 13.scaledSp,
                    color = currentTheme.textSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onCreateFirst,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = currentTheme.primary,
                        contentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
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
                        text = "Create Study Block",
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
    val view = LocalView.current

    var name by remember { mutableStateOf(initialBlock?.name ?: "") }
    var description by remember { mutableStateOf(initialBlock?.description ?: "") }
    var selectedColor by remember { mutableStateOf(initialBlock?.color ?: STUDY_BLOCK_PALETTE[0].second) }
    var selectedDuration by remember { mutableStateOf(initialBlock?.duration ?: 25) }
    var customDurationText by remember { mutableStateOf("") }
    var selectedRepeatRule by remember { mutableStateOf(initialBlock?.repeatRule ?: "None") }
    var reminderEnabled by remember { mutableStateOf(initialBlock?.reminderEnabled ?: false) }
    var scheduledHour by remember {
        val cal = Calendar.getInstance()
        if (initialBlock != null && initialBlock.startTime > 0L) {
            cal.timeInMillis = initialBlock.startTime
            mutableIntStateOf(cal.get(Calendar.HOUR_OF_DAY))
        } else {
            mutableIntStateOf(9)
        }
    }
    var scheduledMinute by remember {
        val cal = Calendar.getInstance()
        if (initialBlock != null && initialBlock.startTime > 0L) {
            cal.timeInMillis = initialBlock.startTime
            mutableIntStateOf(cal.get(Calendar.MINUTE))
        } else {
            mutableIntStateOf(0)
        }
    }
    var isFlexibleTime by remember { mutableStateOf(initialBlock?.startTime == null || initialBlock.startTime == 0L) }
    var validationError by remember { mutableStateOf<String?>(null) }

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
                .background(Color.Black.copy(alpha = 0.45f))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.88f)
                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(28.dp), spotColor = currentTheme.shadowColor)
                    .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(28.dp)),
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
                        Text(
                            text = if (initialBlock == null) "New Study Block" else "Edit Study Block",
                            fontFamily = currentFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.scaledSp,
                            color = currentTheme.textPrimary
                        )
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = currentTheme.textSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

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
                            label = { Text("Block Name *", fontFamily = currentFont) },
                            placeholder = { Text("e.g. Linear Algebra, History Essay", fontFamily = currentFont) },
                            isError = validationError != null,
                            supportingText = {
                                if (validationError != null) {
                                    Text(text = validationError ?: "", color = MaterialTheme.colorScheme.error)
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
                            onValueChange = { description = it },
                            label = { Text("Description (Optional)", fontFamily = currentFont) },
                            placeholder = { Text("e.g. Chapter 3 exercises & revision", fontFamily = currentFont) },
                            maxLines = 3,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("study_block_desc_input"),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Color Theme",
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
                                val composeColor = Color(colorValue.toULong())
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
                                            tint = if (composeColor.luminance() > 0.5f) Color.Black else Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Duration (Minutes)",
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
                            DURATION_PRESETS.forEach { mins ->
                                val isSelected = selectedDuration == mins
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedDuration = mins
                                        customDurationText = ""
                                    },
                                    label = { Text("$mins m", fontFamily = currentFont, fontSize = 12.scaledSp) },
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Scheduled Time",
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
                                label = { Text("Flexible / Anytime", fontFamily = currentFont) },
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
                                    Text("$displayHour:$displayMinute $ampm", fontFamily = currentFont)
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
                            text = "Repeat Rule",
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
                            REPEAT_RULES.forEach { rule ->
                                val isSelected = selectedRepeatRule == rule
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedRepeatRule = rule },
                                    label = { Text(rule, fontFamily = currentFont, fontSize = 12.scaledSp) },
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
                                        text = "Reminder",
                                        fontFamily = currentFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.scaledSp,
                                        color = currentTheme.textPrimary
                                    )
                                    Text(
                                        text = "Alert when it's time to study",
                                        fontFamily = currentFont,
                                        fontSize = 11.scaledSp,
                                        color = currentTheme.textSecondary
                                    )
                                }
                            }
                            Switch(
                                checked = reminderEnabled,
                                onCheckedChange = { reminderEnabled = it }
                            )
                        }
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
                                    validationError = "Please enter a block name"
                                    return@Button
                                }
                                try {
                                    view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (e: Exception) {}

                                val targetStartTime = if (isFlexibleTime) {
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
                                contentColor = if (currentTheme.primary.luminance() > 0.5f) Color(0xFF1E1E1E) else Color.White
                            ),
                            modifier = Modifier.testTag("save_study_block_button")
                        ) {
                            Text(
                                text = if (initialBlock == null) "Create Block" else "Save Changes",
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
