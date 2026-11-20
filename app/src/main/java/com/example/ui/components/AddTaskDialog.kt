package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.CursiveFontFamily
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.PomoMotion
import com.example.ui.theme.luminance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Immutable
private data class TaskColorMark(
    val id: String,
    val name: String,
    val color: Color
)

@Immutable
private data class TaskCategoryPreset(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val suggestedColorId: String,
    val defaultIsBreak: Boolean
)

private val TaskColorMarks = listOf(
    TaskColorMark("coral", "Coral", Color(0xFFF28F75)),
    TaskColorMark("sky", "Sky", Color(0xFF64B5F6)),
    TaskColorMark("sage", "Sage", Color(0xFF81C784)),
    TaskColorMark("lavender", "Lavender", Color(0xFFBA68C8)),
    TaskColorMark("amber", "Amber", Color(0xFFFFB74D)),
    TaskColorMark("rose", "Rose", Color(0xFFF06292)),
    TaskColorMark("teal", "Teal", Color(0xFF4DB6AC)),
    TaskColorMark("indigo", "Indigo", Color(0xFF7986CB))
)

private val TaskCategoryPresets = listOf(
    TaskCategoryPreset("study", "Study", Icons.Rounded.MenuBook, "sky", defaultIsBreak = false),
    TaskCategoryPreset("work", "Work", Icons.Rounded.Work, "sage", defaultIsBreak = false),
    TaskCategoryPreset("read", "Read", Icons.Rounded.AutoStories, "lavender", defaultIsBreak = false),
    TaskCategoryPreset("code", "Code", Icons.Rounded.Code, "indigo", defaultIsBreak = false),
    TaskCategoryPreset("creative", "Creative", Icons.Rounded.Palette, "rose", defaultIsBreak = false),
    TaskCategoryPreset("rest", "Rest", Icons.Rounded.Spa, "teal", defaultIsBreak = true),
    TaskCategoryPreset("fitness", "Fitness", Icons.Rounded.FitnessCenter, "amber", defaultIsBreak = true)
)

fun resolveCategoryIcon(categoryName: String): ImageVector {
    val preset = TaskCategoryPresets.find { it.label.equals(categoryName.trim(), ignoreCase = true) }
    return preset?.icon ?: Icons.Rounded.Label
}

@Composable
fun PremiumJumpingTextPreview(
    text: String,
    accentColor: Color = LocalAppTheme.current.primary
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val scrollState = rememberScrollState()

    LaunchedEffect(text.length) {
        if (scrollState.maxValue > 0) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(34.dp)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (text.isEmpty()) {
            Text(
                text = "Focus Task",
                fontSize = 18.scaledSp,
                color = currentTheme.textSecondary.copy(alpha = 0.55f),
                fontFamily = currentFont,
                fontWeight = FontWeight.Bold
            )
        } else {
            val previewChars = text.take(36)
            previewChars.forEachIndexed { index, char ->
                val animatedOffset = remember(index) { Animatable(-12f) }
                val animatedScale = remember(index) { Animatable(0.76f) }
                val animatedAlpha = remember(index) { Animatable(0f) }

                LaunchedEffect(index) {
                    launch {
                        animatedOffset.animateTo(0f, animationSpec = PomoMotion.fastSpatial())
                    }
                    launch {
                        animatedScale.animateTo(1f, animationSpec = PomoMotion.expressiveSpatial())
                    }
                    launch {
                        animatedAlpha.animateTo(1f, animationSpec = tween(durationMillis = 160))
                    }
                }

                Text(
                    text = if (char == ' ') " " else char.toString(),
                    fontSize = 18.scaledSp,
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary,
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = animatedOffset.value
                            scaleX = animatedScale.value
                            scaleY = animatedScale.value
                            alpha = animatedAlpha.value
                        }
                )
            }
            if (text.length > 36) {
                Text(
                    text = "…",
                    fontSize = 18.scaledSp,
                    fontFamily = currentFont,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTaskCard(
    onSave: (String, String, Long, Boolean) -> Unit,
    onCancel: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf<TaskCategoryPreset?>(TaskCategoryPresets[0]) }
    var isCustomCategory by remember { mutableStateOf(false) }
    var customCategoryName by remember { mutableStateOf("") }
    var isBreakCategory by remember { mutableStateOf(false) }
    var selectedColorMark by remember { mutableStateOf(TaskColorMarks[1]) } // Sky matches Study
    var hasManuallyPickedColor by remember { mutableStateOf(false) }

    val resolvedCategory = remember(isCustomCategory, customCategoryName, selectedPreset) {
        if (isCustomCategory) {
            customCategoryName.trim().ifEmpty { "Custom" }
        } else {
            selectedPreset?.label ?: "Study"
        }
    }

    var isVisible by remember { mutableStateOf(false) }
    var isClosing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val targetShown = isVisible && !isClosing
    val cardScale by animateFloatAsState(
        targetValue = if (targetShown) 1f else 0.90f,
        animationSpec = if (targetShown) PomoMotion.expressiveSpatial() else PomoMotion.fastSpatial(),
        label = "addTaskCardScale"
    )
    val cardAlpha by animateFloatAsState(
        targetValue = if (targetShown) 1f else 0f,
        animationSpec = if (targetShown) PomoMotion.defaultEffects() else PomoMotion.fastEffects(),
        label = "addTaskCardAlpha"
    )
    val cardOffsetY by animateFloatAsState(
        targetValue = if (targetShown) 0f else 28f,
        animationSpec = if (targetShown) PomoMotion.defaultSpatial() else PomoMotion.fastSpatial(),
        label = "addTaskCardOffset"
    )

    val animatedMarkColor by animateColorAsState(
        targetValue = selectedColorMark.color,
        animationSpec = PomoMotion.colorSpec,
        label = "selectedColorMarkAnim"
    )

    fun triggerClose(actionAfter: () -> Unit) {
        if (isClosing) return
        isClosing = true
        keyboardController?.hide()
        focusManager.clearFocus()
        coroutineScope.launch {
            delay(145L)
            actionAfter()
        }
    }

    fun submitTask() {
        try {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        } catch (_: Exception) {}
        val finalTitle = title.trim().ifEmpty { if (isBreakCategory) "Mindful Break" else "Focus Task" }
        val finalCategory = resolvedCategory
        val finalColorLong = selectedColorMark.color.toArgb().toLong() and 0xFFFFFFFFL
        val finalIsBreak = isBreakCategory
        triggerClose {
            onSave(finalTitle, finalCategory, finalColorLong, finalIsBreak)
        }
    }

    Dialog(
        onDismissRequest = { triggerClose(onCancel) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(Color.Black.copy(alpha = 0.45f * cardAlpha))
                }
                .imePadding()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        triggerClose(onCancel)
                    })
                }
                .padding(horizontal = 18.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                border = BorderStroke(1.dp, currentTheme.cardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .widthIn(max = 460.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = cardScale
                        scaleY = cardScale
                        alpha = cardAlpha
                        translationY = cardOffsetY.dp.toPx()
                        compositingStrategy = if (cardAlpha < 0.999f) {
                            CompositingStrategy.Offscreen
                        } else {
                            CompositingStrategy.Auto
                        }
                    }
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(30.dp),
                        spotColor = currentTheme.shadowColor
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
                    }
                    .testTag("add_task_dialog_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(22.dp)
                ) {
                    // Header row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "New Focus Task",
                                fontFamily = CursiveFontFamily,
                                fontSize = 28.scaledSp,
                                fontWeight = FontWeight.Bold,
                                color = currentTheme.textPrimary
                            )
                            Text(
                                text = "Personalize mark & category",
                                fontSize = 12.scaledSp,
                                fontFamily = currentFont,
                                color = currentTheme.textSecondary
                            )
                        }

                        IconButton(
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                triggerClose(onCancel)
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(currentTheme.backgroundSecondary.copy(alpha = 0.7f))
                                .testTag("add_task_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = currentTheme.textSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Interactive Task Card Preview
                    LiveTaskPreviewCard(
                        title = title,
                        categoryName = resolvedCategory,
                        categoryIcon = resolveCategoryIcon(resolvedCategory),
                        markColor = animatedMarkColor,
                        colorMarkName = selectedColorMark.name,
                        isBreakCategory = isBreakCategory
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Task Title Input
                    OutlinedTextField(
                        value = title,
                        onValueChange = { if (it.length <= 60) title = it },
                        label = { Text("Task Name", fontFamily = currentFont) },
                        placeholder = { Text("e.g., Chapter 4 Physics Notes", fontFamily = currentFont) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.TaskAlt,
                                contentDescription = null,
                                tint = animatedMarkColor,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (title.isNotEmpty()) {
                                IconButton(
                                    onClick = { title = "" },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Clear task name",
                                        tint = currentTheme.textSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = currentTheme.textPrimary,
                            unfocusedTextColor = currentTheme.textPrimary,
                            cursorColor = animatedMarkColor,
                            focusedBorderColor = animatedMarkColor,
                            unfocusedBorderColor = currentTheme.cardBorder,
                            focusedLabelColor = animatedMarkColor,
                            unfocusedLabelColor = currentTheme.textSecondary
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { submitTask() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("add_task_name_input")
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Color Mark Picker Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Color Mark",
                            fontSize = 13.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            color = currentTheme.textPrimary
                        )
                        AnimatedContent(
                            targetState = selectedColorMark.name,
                            transitionSpec = { PomoMotion.verticalCrossfadeTransition(upward = true) },
                            label = "colorMarkLabel"
                        ) { colorName ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = animatedMarkColor.copy(alpha = 0.16f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(animatedMarkColor)
                                    )
                                    Text(
                                        text = colorName,
                                        fontSize = 11.scaledSp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = currentFont,
                                        color = currentTheme.textPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TaskColorMarks.forEachIndexed { idx, mark ->
                            ColorMarkSwatch(
                                mark = mark,
                                isSelected = selectedColorMark.id == mark.id,
                                index = idx,
                                onClick = {
                                    try {
                                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (_: Exception) {}
                                    selectedColorMark = mark
                                    hasManuallyPickedColor = true
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Category Picker Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category",
                            fontSize = 13.scaledSp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            color = currentTheme.textPrimary
                        )
                        Text(
                            text = if (isBreakCategory) "Runs Break Timer" else "Runs Work Timer",
                            fontSize = 11.scaledSp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = currentFont,
                            color = if (isBreakCategory) currentTheme.secondary else currentTheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Work Time vs Break Time Classification Toggle
                    TimerModeClassificationToggle(
                        isBreakCategory = isBreakCategory,
                        onModeChange = { nextBreak ->
                            try {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (_: Exception) {}
                            isBreakCategory = nextBreak
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TaskCategoryPresets.forEach { preset ->
                            val isSelected = !isCustomCategory && selectedPreset?.id == preset.id
                            CategoryOptionChip(
                                label = preset.label,
                                icon = preset.icon,
                                accentColor = animatedMarkColor,
                                isSelected = isSelected,
                                isBreakPreset = preset.defaultIsBreak,
                                tag = "add_task_category_${preset.id}",
                                onClick = {
                                    try {
                                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                    } catch (_: Exception) {}
                                    isCustomCategory = false
                                    selectedPreset = preset
                                    isBreakCategory = preset.defaultIsBreak
                                    if (!hasManuallyPickedColor) {
                                        TaskColorMarks.find { it.id == preset.suggestedColorId }?.let {
                                            selectedColorMark = it
                                        }
                                    }
                                }
                            )
                        }

                        CategoryOptionChip(
                            label = "Custom",
                            icon = Icons.Rounded.Edit,
                            accentColor = animatedMarkColor,
                            isSelected = isCustomCategory,
                            isBreakPreset = null,
                            tag = "add_task_category_custom",
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                isCustomCategory = true
                            }
                        )
                    }

                    // Smoothly expandable custom category field
                    AnimatedVisibility(
                        visible = isCustomCategory,
                        enter = PomoMotion.expandSection,
                        exit = PomoMotion.collapseSection
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = customCategoryName,
                                onValueChange = { if (it.length <= 20) customCategoryName = it },
                                label = { Text("Custom Category", fontFamily = currentFont) },
                                placeholder = { Text("e.g., Guitar, Thesis, Design", fontFamily = currentFont) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Label,
                                        contentDescription = null,
                                        tint = animatedMarkColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = currentTheme.textPrimary,
                                    unfocusedTextColor = currentTheme.textPrimary,
                                    cursorColor = animatedMarkColor,
                                    focusedBorderColor = animatedMarkColor,
                                    unfocusedBorderColor = currentTheme.cardBorder,
                                    focusedLabelColor = animatedMarkColor,
                                    unfocusedLabelColor = currentTheme.textSecondary
                                ),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { submitTask() }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("add_task_custom_category_input")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Action Footer
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                triggerClose(onCancel)
                            },
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("add_task_cancel_button")
                        ) {
                            Text(
                                text = "Cancel",
                                color = currentTheme.textSecondary,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = currentFont
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))

                        val saveContentColor = if (animatedMarkColor.luminance() > 0.55f) {
                            Color(0xFF1E1E1E)
                        } else {
                            Color.White
                        }
                        PomoButton(
                            onClick = { submitTask() },
                            modifier = Modifier.testTag("add_task_save_button"),
                            containerColor = animatedMarkColor,
                            contentColor = saveContentColor,
                            shape = RoundedCornerShape(18.dp),
                            elevation = 5.dp,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = saveContentColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Save Task",
                                color = saveContentColor,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont,
                                fontSize = 14.scaledSp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveTaskPreviewCard(
    title: String,
    categoryName: String,
    categoryIcon: ImageVector,
    markColor: Color,
    colorMarkName: String,
    isBreakCategory: Boolean
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val previewBg by animateColorAsState(
        targetValue = markColor.copy(alpha = if (currentTheme.isDark) 0.16f else 0.09f)
            .compositeOver(currentTheme.backgroundSecondary),
        animationSpec = PomoMotion.colorSpec,
        label = "previewCardBg"
    )
    val modeColor by animateColorAsState(
        targetValue = if (isBreakCategory) currentTheme.secondary else currentTheme.primary,
        animationSpec = PomoMotion.colorSpec,
        label = "previewModeColor"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(previewBg, currentTheme.surface)
                )
            )
            .border(1.2.dp, markColor.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(markColor.copy(alpha = 0.20f))
                    .border(1.dp, markColor.copy(alpha = 0.45f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = categoryIcon,
                    transitionSpec = { PomoMotion.sharedAxisZTransition(forward = true) },
                    label = "previewCategoryIcon"
                ) { iconVec ->
                    Icon(
                        imageVector = iconVec,
                        contentDescription = null,
                        tint = markColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE PREVIEW",
                        fontSize = 10.scaledSp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = currentFont,
                        color = markColor
                    )
                    AnimatedContent(
                        targetState = isBreakCategory,
                        transitionSpec = { PomoMotion.verticalCrossfadeTransition(upward = targetState) },
                        label = "previewTimerModePill"
                    ) { breakMode ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = modeColor.copy(alpha = 0.16f)
                        ) {
                            Text(
                                text = if (breakMode) "☕ BREAK TIMER" else "🍅 WORK TIMER",
                                fontSize = 9.scaledSp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont,
                                color = modeColor,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                PremiumJumpingTextPreview(text = title, accentColor = markColor)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedContent(
                        targetState = categoryName,
                        transitionSpec = { PomoMotion.verticalCrossfadeTransition(upward = true) },
                        label = "previewCatBadge"
                    ) { catText ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = markColor.copy(alpha = 0.18f)
                        ) {
                            Text(
                                text = catText,
                                fontSize = 11.scaledSp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = currentFont,
                                color = currentTheme.textPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "• $colorMarkName mark",
                        fontSize = 11.scaledSp,
                        fontFamily = currentFont,
                        color = currentTheme.textSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun TimerModeClassificationToggle(
    isBreakCategory: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(currentTheme.backgroundSecondary.copy(alpha = 0.75f))
            .border(1.dp, currentTheme.cardBorder, RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val modes = listOf(
            Triple(false, "Work Time", Icons.Rounded.Timer),
            Triple(true, "Break Time", Icons.Rounded.Coffee)
        )
        modes.forEach { (modeIsBreak, label, icon) ->
            val selected = isBreakCategory == modeIsBreak
            val modeTint = if (modeIsBreak) currentTheme.secondary else currentTheme.primary
            val pillBg by animateColorAsState(
                targetValue = if (selected) currentTheme.surface else Color.Transparent,
                animationSpec = PomoMotion.colorSpec,
                label = "modeToggleBg_$label"
            )
            val borderCol by animateColorAsState(
                targetValue = if (selected) modeTint.copy(alpha = 0.5f) else Color.Transparent,
                animationSpec = PomoMotion.colorSpec,
                label = "modeToggleBorder_$label"
            )
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val pressScale by animateFloatAsState(
                targetValue = if (isPressed) 0.96f else 1f,
                animationSpec = PomoMotion.pressScaleSpec,
                label = "modeToggleScale_$label"
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .graphicsLayer {
                        scaleX = pressScale
                        scaleY = pressScale
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(pillBg)
                    .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = true)
                    ) {
                        onModeChange(modeIsBreak)
                    }
                    .testTag(if (modeIsBreak) "add_task_mode_break" else "add_task_mode_work"),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) modeTint else currentTheme.textSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    fontSize = 13.scaledSp,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    fontFamily = currentFont,
                    color = if (selected) currentTheme.textPrimary else currentTheme.textSecondary
                )
            }
        }
    }
}

@Composable
private fun ColorMarkSwatch(
    mark: TaskColorMark,
    isSelected: Boolean,
    index: Int,
    onClick: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val swatchScale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.86f
            isSelected -> 1.12f
            else -> 1f
        },
        animationSpec = PomoMotion.expressiveSpatial(),
        label = "swatchScale_$index"
    )
    val ringWidth by animateDpAsState(
        targetValue = if (isSelected) 2.5.dp else 1.dp,
        animationSpec = PomoMotion.dpFastSpatial,
        label = "swatchRing_$index"
    )
    val ringColor by animateColorAsState(
        targetValue = if (isSelected) currentTheme.textPrimary else currentTheme.cardBorder,
        animationSpec = PomoMotion.colorSpec,
        label = "swatchRingColor_$index"
    )
    val checkTint = if (mark.color.luminance() > 0.55f) Color(0xFF1E1E1E) else Color.White

    Box(
        modifier = Modifier
            .size(44.dp)
            .graphicsLayer {
                scaleX = swatchScale
                scaleY = swatchScale
            }
            .clip(CircleShape)
            .border(ringWidth, ringColor, CircleShape)
            .padding(if (isSelected) 4.dp else 2.dp)
            .clip(CircleShape)
            .background(mark.color)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .testTag("add_task_color_$index"),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(initialScale = 0.5f, animationSpec = PomoMotion.expressiveSpatial()) +
                fadeIn(PomoMotion.fastEffects()),
            exit = scaleOut(targetScale = 0.5f, animationSpec = PomoMotion.fastSpatial()) +
                fadeOut(PomoMotion.fastEffects())
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "${mark.name} selected",
                tint = checkTint,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun CategoryOptionChip(
    label: String,
    icon: ImageVector,
    accentColor: Color,
    isSelected: Boolean,
    isBreakPreset: Boolean?,
    tag: String,
    onClick: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val chipScale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = PomoMotion.pressScaleSpec,
        label = "catChipScale_$label"
    )
    val chipBg by animateColorAsState(
        targetValue = if (isSelected) {
            accentColor.copy(alpha = if (currentTheme.isDark) 0.24f else 0.16f)
                .compositeOver(currentTheme.surface)
        } else {
            currentTheme.backgroundSecondary.copy(alpha = 0.65f)
        },
        animationSpec = PomoMotion.colorSpec,
        label = "catChipBg_$label"
    )
    val chipBorder by animateColorAsState(
        targetValue = if (isSelected) accentColor else currentTheme.cardBorder,
        animationSpec = PomoMotion.colorSpec,
        label = "catChipBorder_$label"
    )

    Row(
        modifier = Modifier
            .height(38.dp)
            .graphicsLayer {
                scaleX = chipScale
                scaleY = chipScale
            }
            .clip(RoundedCornerShape(14.dp))
            .background(chipBg)
            .border(if (isSelected) 1.5.dp else 1.dp, chipBorder, RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .padding(horizontal = 12.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) accentColor else currentTheme.textSecondary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            fontSize = 13.scaledSp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontFamily = currentFont,
            color = if (isSelected) currentTheme.textPrimary else currentTheme.textSecondary
        )
        if (isBreakPreset != null) {
            Text(
                text = if (isBreakPreset) "☕" else "🍅",
                fontSize = 11.scaledSp
            )
        }
    }
}
