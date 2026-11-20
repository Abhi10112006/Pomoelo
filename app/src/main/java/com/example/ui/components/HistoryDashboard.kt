package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingFlat
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.service.safeParseColor
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import com.example.ui.theme.PomoMotion
import com.example.ui.theme.ThemeOption
import com.example.ui.theme.blend
import com.example.ui.theme.ensureContrast
import com.example.ui.theme.lighten
import com.example.ui.theme.luminance
import kotlin.math.roundToInt

@Immutable
data class DailyPulseBar(
    val dayStartMillis: Long,
    val shortDayLabel: String,
    val fullDateLabel: String,
    val focusMinutes: Int,
    val restMinutes: Int,
    val sessionCount: Int,
    val isToday: Boolean
)

@Immutable
data class CategoryShareSlice(
    val label: String,
    val minutes: Int,
    val colorLong: Long?,
    val isBreak: Boolean
)

@Immutable
data class HistoryDashboardTelemetry(
    val todayPomodoros: Int,
    val todayFocusMinutes: Int,
    val todayBreakMinutes: Int,
    val yesterdayFocusMinutes: Int,
    val deltaVsYesterdayMinutes: Int,
    val weeklyFocusMinutes: Int,
    val weeklyDailyAvgMinutes: Int,
    val currentStreakDays: Int,
    val weeklyDays: List<DailyPulseBar>,
    val categoryShares: List<CategoryShareSlice>
)

fun formatCompactMinutes(totalMinutes: Int): String {
    val safe = totalMinutes.coerceAtLeast(0)
    val hrs = safe / 60
    val mins = safe % 60
    return when {
        hrs > 0 && mins > 0 -> "${hrs}h ${mins}m"
        hrs > 0 -> "${hrs}h"
        else -> "${mins}m"
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryDailyDashboardCard(
    telemetry: HistoryDashboardTelemetry,
    dailyGoalMinutes: Int,
    onEditGoalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val entrance = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        entrance.animateTo(1f, animationSpec = PomoMotion.defaultSpatial())
    }

    val safeGoal = dailyGoalMinutes.coerceAtLeast(30)
    val targetFocusRatio = (telemetry.todayFocusMinutes.toFloat() / safeGoal.toFloat()).coerceIn(0f, 1.5f)
    val totalTodayMinutes = telemetry.todayFocusMinutes + telemetry.todayBreakMinutes
    val targetRestRatio = if (totalTodayMinutes > 0) {
        (telemetry.todayBreakMinutes.toFloat() / totalTodayMinutes.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    val animatedFocusRatio by animateFloatAsState(
        targetValue = targetFocusRatio,
        animationSpec = PomoMotion.slowSpatial(),
        label = "ringFocusRatio"
    )
    val animatedRestRatio by animateFloatAsState(
        targetValue = targetRestRatio,
        animationSpec = PomoMotion.slowSpatial(),
        label = "ringRestRatio"
    )
    val animatedPomodoros by animateIntAsState(
        targetValue = telemetry.todayPomodoros,
        animationSpec = PomoMotion.slowEffects(),
        label = "dashPomodoros"
    )
    val animatedFocusMins by animateIntAsState(
        targetValue = telemetry.todayFocusMinutes,
        animationSpec = PomoMotion.slowEffects(),
        label = "dashFocusMins"
    )
    val animatedBreakMins by animateIntAsState(
        targetValue = telemetry.todayBreakMinutes,
        animationSpec = PomoMotion.slowEffects(),
        label = "dashBreakMins"
    )

    var selectedBarDayMillis by remember { mutableStateOf<Long?>(null) }
    val selectedBar = remember(telemetry.weeklyDays, selectedBarDayMillis) {
        telemetry.weeklyDays.find { it.dayStartMillis == selectedBarDayMillis }
    }

    // Dynamic Make-It-Yours derived tokens (adapt cleanly across Light/Dark & any custom Primary/Secondary/Accent)
    val primaryInk = remember(currentTheme.primary, currentTheme.primaryLight, currentTheme.primaryDark, currentTheme.surface, currentTheme.isDark) {
        ensureContrast(
            if (currentTheme.isDark) currentTheme.primaryLight else currentTheme.primaryDark,
            currentTheme.surface,
            minContrast = 4.5
        )
    }
    val secondaryInk = remember(currentTheme.secondary, currentTheme.surface) {
        ensureContrast(currentTheme.secondary, currentTheme.surface, minContrast = 3.6)
    }
    val accentInk = remember(currentTheme.accent, currentTheme.surface) {
        ensureContrast(currentTheme.accent, currentTheme.surface, minContrast = 3.6)
    }
    val goalPillBg = remember(currentTheme.surface, currentTheme.primary, currentTheme.isDark) {
        currentTheme.surface.blend(currentTheme.primary, if (currentTheme.isDark) 0.24f else 0.13f)
    }
    val streakPillBg = remember(currentTheme.surface, currentTheme.accent, currentTheme.isDark) {
        currentTheme.surface.blend(currentTheme.accent, if (currentTheme.isDark) 0.24f else 0.14f)
    }
    val subTileBg = remember(currentTheme.surface, currentTheme.backgroundSecondary, currentTheme.isDark) {
        if (currentTheme.isDark) currentTheme.surface.lighten(0.05f) else currentTheme.backgroundSecondary.copy(alpha = 0.78f)
    }
    val cardAtmosphericBrush = remember(currentTheme.surface, currentTheme.primary, currentTheme.secondary, currentTheme.isDark) {
        val topWash = currentTheme.surface.blend(currentTheme.primary, if (currentTheme.isDark) 0.12f else 0.06f)
        val bottomWash = currentTheme.surface.blend(currentTheme.secondary, if (currentTheme.isDark) 0.10f else 0.05f)
        Brush.linearGradient(colors = listOf(topWash, currentTheme.surface, bottomWash))
    }
    val cardBorderBrush = remember(currentTheme.primary, currentTheme.secondary, currentTheme.cardBorder, currentTheme.isDark) {
        val edgeAlpha = if (currentTheme.isDark) 0.36f else 0.28f
        Brush.linearGradient(
            colors = listOf(
                currentTheme.primary.copy(alpha = edgeAlpha),
                currentTheme.cardBorder,
                currentTheme.secondary.copy(alpha = edgeAlpha)
            )
        )
    }

    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(fontFamily = currentFont)
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
            border = BorderStroke(1.2.dp, cardBorderBrush),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = modifier
                .fillMaxWidth()
                .graphicsLayer {
                    val p = entrance.value
                    alpha = p.coerceIn(0f, 1f)
                    val s = 0.94f + (0.06f * p)
                    scaleX = s
                    scaleY = s
                    translationY = (1f - p) * 18.dp.toPx()
                    compositingStrategy = if (p < 0.999f) {
                        CompositingStrategy.Offscreen
                    } else {
                        CompositingStrategy.Auto
                    }
                }
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = currentTheme.shadowColor,
                    spotColor = currentTheme.shadowColor,
                    clip = false
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardAtmosphericBrush)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Header Row: Daily Goal Trigger + Streak Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = goalPillBg,
                        border = BorderStroke(1.dp, currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.42f else 0.28f)),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                onEditGoalClick()
                            }
                            .testTag("daily_goal_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.TrackChanges,
                                contentDescription = "Edit daily goal",
                                tint = primaryInk,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Goal: ${formatCompactMinutes(safeGoal)}",
                                color = primaryInk,
                                fontSize = 12.scaledSp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont
                            )
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null,
                                tint = primaryInk.copy(alpha = 0.8f),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    val streakActive = telemetry.currentStreakDays > 0
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (streakActive) streakPillBg else subTileBg,
                        border = BorderStroke(
                            1.dp,
                            if (streakActive) currentTheme.accent.copy(alpha = if (currentTheme.isDark) 0.45f else 0.35f) else currentTheme.cardBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = if (streakActive) accentInk else currentTheme.textSecondary.copy(alpha = 0.65f),
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (streakActive) {
                                    "${telemetry.currentStreakDays}d streak"
                                } else {
                                    "Start streak"
                                },
                                color = if (streakActive) currentTheme.textPrimary else currentTheme.textSecondary,
                                fontSize = 12.scaledSp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont
                            )
                        }
                    }
                }

                // Centerpiece Row: Dual-Arc Focus vs. Rest Ring + Today's Telemetry Tiles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DualArcTelemetryRing(
                        todayFocusMinutes = telemetry.todayFocusMinutes,
                        todayBreakMinutes = telemetry.todayBreakMinutes,
                        dailyGoalMinutes = safeGoal,
                        focusRatioProvider = { animatedFocusRatio },
                        restRatioProvider = { animatedRestRatio },
                        theme = currentTheme,
                        accentInk = accentInk,
                        secondaryInk = secondaryInk,
                        modifier = Modifier.size(144.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TelemetryStatRow(
                            label = "SESSIONS",
                            value = "$animatedPomodoros",
                            accentColor = accentInk,
                            subtitle = if (animatedPomodoros == 1) "pomodoro" else "pomodoros"
                        )
                        HorizontalDivider(color = currentTheme.cardBorder.copy(alpha = 0.65f))
                        TelemetryStatRow(
                            label = "FOCUS",
                            value = formatCompactMinutes(animatedFocusMins),
                            accentColor = primaryInk,
                            subtitle = "${((telemetry.todayFocusMinutes.toFloat() / safeGoal) * 100).roundToInt()}% of goal"
                        )
                        HorizontalDivider(color = currentTheme.cardBorder.copy(alpha = 0.65f))
                        TelemetryStatRow(
                            label = "REST",
                            value = formatCompactMinutes(animatedBreakMins),
                            accentColor = secondaryInk,
                            subtitle = if (totalTodayMinutes > 0) {
                                "${(targetRestRatio * 100).roundToInt()}% recovery"
                            } else {
                                "balanced rest"
                            }
                        )
                    }
                }

                // Comparison Strip: vs. Yesterday + 7-Day Weekly Summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val delta = telemetry.deltaVsYesterdayMinutes
                    val deltaText = when {
                        telemetry.todayFocusMinutes == 0 && telemetry.yesterdayFocusMinutes == 0 -> "New day ahead"
                        delta > 0 -> "+${formatCompactMinutes(delta)} vs yest."
                        delta < 0 -> "-${formatCompactMinutes(-delta)} vs yest."
                        else -> "Tied with yest."
                    }
                    val deltaIcon = when {
                        delta > 0 -> Icons.AutoMirrored.Rounded.TrendingUp
                        delta < 0 -> Icons.AutoMirrored.Rounded.TrendingDown
                        else -> Icons.AutoMirrored.Rounded.TrendingFlat
                    }
                    val deltaColor = when {
                        delta > 0 -> accentInk
                        delta < 0 -> primaryInk
                        else -> currentTheme.textSecondary
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = subTileBg,
                        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.7f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = deltaIcon,
                                contentDescription = null,
                                tint = deltaColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = deltaText,
                                fontSize = 11.scaledSp,
                                fontWeight = FontWeight.SemiBold,
                                color = currentTheme.textPrimary,
                                fontFamily = currentFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = subTileBg,
                        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.7f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "7d: ${formatCompactMinutes(telemetry.weeklyFocusMinutes)} (${formatCompactMinutes(telemetry.weeklyDailyAvgMinutes)}/d)",
                                fontSize = 11.scaledSp,
                                fontWeight = FontWeight.SemiBold,
                                color = currentTheme.textPrimary,
                                fontFamily = currentFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Interactive 7-Day Weekly Pulse Strip
                if (telemetry.weeklyDays.isNotEmpty()) {
                    WeeklyPulseStrip(
                        days = telemetry.weeklyDays,
                        dailyGoalMinutes = safeGoal,
                        selectedDayMillis = selectedBarDayMillis,
                        primaryInk = primaryInk,
                        accentInk = accentInk,
                        onDayBarTap = { clickedDay ->
                            try {
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                            } catch (_: Exception) {}
                            selectedBarDayMillis = if (selectedBarDayMillis == clickedDay.dayStartMillis) {
                                null
                            } else {
                                clickedDay.dayStartMillis
                            }
                        }
                    )
                }

                AnimatedVisibility(
                    visible = selectedBar != null,
                    enter = PomoMotion.expandSection,
                    exit = PomoMotion.collapseSection
                ) {
                    selectedBar?.let { bar ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = goalPillBg,
                            border = BorderStroke(1.dp, currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.4f else 0.24f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (bar.isToday) "Today (${bar.fullDateLabel})" else bar.fullDateLabel,
                                    fontSize = 12.scaledSp,
                                    fontWeight = FontWeight.Bold,
                                    color = currentTheme.textPrimary,
                                    fontFamily = currentFont
                                )
                                Text(
                                    text = "${formatCompactMinutes(bar.focusMinutes)} focus • ${formatCompactMinutes(bar.restMinutes)} rest • ${bar.sessionCount}x",
                                    fontSize = 11.scaledSp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = primaryInk,
                                    fontFamily = MonospaceFontFamily
                                )
                            }
                        }
                    }
                }

                // Today's Task / Category Split Bar
                if (telemetry.categoryShares.isNotEmpty()) {
                    CategoryShareBreakdown(
                        slices = telemetry.categoryShares,
                        theme = currentTheme,
                        chipBg = subTileBg
                    )
                }
            }
        }
    }
}

@Composable
private fun TelemetryStatRow(
    label: String,
    value: String,
    accentColor: Color,
    subtitle: String
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 10.scaledSp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.9.sp,
                color = currentTheme.textSecondary,
                fontFamily = currentFont
            )
            Text(
                text = subtitle,
                fontSize = 11.scaledSp,
                color = currentTheme.textSecondary.copy(alpha = 0.78f),
                fontFamily = currentFont,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = value,
            fontSize = 19.scaledSp,
            fontWeight = FontWeight.Black,
            color = accentColor,
            fontFamily = MonospaceFontFamily
        )
    }
}

@Composable
private fun DualArcTelemetryRing(
    todayFocusMinutes: Int,
    todayBreakMinutes: Int,
    dailyGoalMinutes: Int,
    focusRatioProvider: () -> Float,
    restRatioProvider: () -> Float,
    theme: ThemeOption,
    accentInk: Color,
    secondaryInk: Color,
    modifier: Modifier = Modifier
) {
    val currentFont = LocalAppFont.current
    val density = LocalDensity.current
    val isZeroState = todayFocusMinutes == 0

    val outerPx = with(density) { 13.dp.toPx() }
    val innerPx = with(density) { 8.dp.toPx() }
    val bonusPx = outerPx * 0.55f
    val outerStroke = remember(outerPx) { Stroke(width = outerPx, cap = StrokeCap.Round) }
    val innerStroke = remember(innerPx) { Stroke(width = innerPx, cap = StrokeCap.Round) }
    val bonusStroke = remember(bonusPx) { Stroke(width = bonusPx, cap = StrokeCap.Round) }
    val focusBrush = remember(theme.primary, theme.primaryLight, theme.accent) {
        Brush.sweepGradient(
            colors = listOf(
                theme.primary,
                theme.primaryLight,
                theme.accent,
                theme.primary
            )
        )
    }
    val haloBrush = remember(theme.primary, theme.isDark) {
        Brush.radialGradient(
            colors = listOf(
                theme.primary.copy(alpha = if (theme.isDark) 0.16f else 0.08f),
                Color.Transparent
            )
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(brush = haloBrush, radius = size.minDimension * 0.5f)

            val outerPad = outerPx / 2f + 2.dp.toPx()
            val outerDiameter = (size.minDimension - outerPad * 2f).coerceAtLeast(10f)
            val outerTopLeft = Offset(
                (size.width - outerDiameter) / 2f,
                (size.height - outerDiameter) / 2f
            )
            val outerSize = Size(outerDiameter, outerDiameter)

            val innerGap = outerPx / 2f + 7.dp.toPx() + innerPx / 2f
            val innerDiameter = (outerDiameter - innerGap * 2f).coerceAtLeast(10f)
            val innerTopLeft = Offset(
                (size.width - innerDiameter) / 2f,
                (size.height - innerDiameter) / 2f
            )
            val innerSize = Size(innerDiameter, innerDiameter)

            val trackAlphaOuter = if (isZeroState) {
                if (theme.isDark) 0.22f else 0.14f
            } else {
                if (theme.isDark) 0.26f else 0.18f
            }
            val trackAlphaInner = if (isZeroState) {
                if (theme.isDark) 0.18f else 0.11f
            } else {
                if (theme.isDark) 0.22f else 0.15f
            }

            // Outer Focus track
            drawArc(
                color = theme.primary.copy(alpha = trackAlphaOuter),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = outerTopLeft,
                size = outerSize,
                style = outerStroke
            )

            // Inner Rest balance track
            drawArc(
                color = theme.secondary.copy(alpha = trackAlphaInner),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = innerTopLeft,
                size = innerSize,
                style = innerStroke
            )

            val focusRatio = focusRatioProvider().coerceAtLeast(0f)
            if (focusRatio > 0.005f) {
                val primarySweep = (focusRatio.coerceAtMost(1f)) * 360f
                drawArc(
                    brush = focusBrush,
                    startAngle = -90f,
                    sweepAngle = primarySweep,
                    useCenter = false,
                    topLeft = outerTopLeft,
                    size = outerSize,
                    style = outerStroke
                )
                if (focusRatio > 1.01f) {
                    val bonusSweep = ((focusRatio - 1f).coerceIn(0f, 0.5f)) * 360f
                    drawArc(
                        color = theme.accent,
                        startAngle = -90f,
                        sweepAngle = bonusSweep,
                        useCenter = false,
                        topLeft = outerTopLeft,
                        size = outerSize,
                        style = bonusStroke
                    )
                }
            }

            val restRatio = restRatioProvider().coerceIn(0f, 1f)
            if (restRatio > 0.005f) {
                drawArc(
                    color = theme.secondary,
                    startAngle = -90f,
                    sweepAngle = restRatio * 360f,
                    useCenter = false,
                    topLeft = innerTopLeft,
                    size = innerSize,
                    style = innerStroke
                )
            }
        }

        // Graceful Zero-State vs Active Center Telemetry
        AnimatedContent(
            targetState = isZeroState,
            transitionSpec = { PomoMotion.fadeThroughTransition() },
            label = "ringCenterState"
        ) { empty ->
            if (empty) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SelfImprovement,
                        contentDescription = null,
                        tint = theme.primary.copy(alpha = if (theme.isDark) 0.90f else 0.78f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Ready to\nfocus",
                        fontSize = 12.scaledSp,
                        lineHeight = 14.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = theme.textPrimary,
                        fontFamily = currentFont,
                        textAlign = TextAlign.Center
                    )
                    if (todayBreakMinutes > 0) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${formatCompactMinutes(todayBreakMinutes)} rest",
                            fontSize = 10.scaledSp,
                            color = secondaryInk,
                            fontFamily = MonospaceFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                val pct = ((todayFocusMinutes.toFloat() / dailyGoalMinutes.coerceAtLeast(1)) * 100).roundToInt()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 14.dp)
                ) {
                    Text(
                        text = "$pct%",
                        fontSize = 23.scaledSp,
                        fontWeight = FontWeight.Black,
                        color = if (pct >= 100) accentInk else theme.textPrimary,
                        fontFamily = MonospaceFontFamily
                    )
                    Text(
                        text = "of daily goal",
                        fontSize = 10.scaledSp,
                        color = theme.textSecondary,
                        fontFamily = currentFont,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyPulseStrip(
    days: List<DailyPulseBar>,
    dailyGoalMinutes: Int,
    selectedDayMillis: Long?,
    primaryInk: Color,
    accentInk: Color,
    onDayBarTap: (DailyPulseBar) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    val maxScaleMins = remember(days, dailyGoalMinutes) {
        val peak = days.maxOfOrNull { it.focusMinutes } ?: 0
        maxOf(dailyGoalMinutes, peak, 60)
    }
    val goalFraction = (dailyGoalMinutes.toFloat() / maxScaleMins.toFloat()).coerceIn(0.15f, 0.95f)
    val dashEffect = remember { PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "7-DAY PULSE",
                fontSize = 11.scaledSp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.9.sp,
                color = currentTheme.textSecondary,
                fontFamily = currentFont
            )
            Text(
                text = "Tap bar for details",
                fontSize = 10.scaledSp,
                color = currentTheme.textSecondary.copy(alpha = 0.72f),
                fontFamily = currentFont
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(82.dp)
        ) {
            // Goal threshold dashed reference line behind bars
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.TopCenter)
            ) {
                val y = size.height * (1f - goalFraction)
                drawLine(
                    color = currentTheme.accent.copy(alpha = if (currentTheme.isDark) 0.55f else 0.42f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = dashEffect
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                days.forEach { bar ->
                    val isSelected = selectedDayMillis == bar.dayStartMillis
                    val rawFrac = if (bar.focusMinutes <= 0) 0.06f else {
                        (bar.focusMinutes.toFloat() / maxScaleMins.toFloat()).coerceIn(0.10f, 1f)
                    }
                    val animFrac by animateFloatAsState(
                        targetValue = rawFrac,
                        animationSpec = PomoMotion.defaultSpatial(),
                        label = "barFrac_${bar.dayStartMillis}"
                    )
                    val goalMet = bar.focusMinutes >= dailyGoalMinutes
                    val barColor by animateColorAsState(
                        targetValue = when {
                            isSelected -> primaryInk
                            goalMet -> accentInk
                            bar.isToday -> currentTheme.primary
                            bar.focusMinutes > 0 -> currentTheme.secondary.copy(alpha = if (currentTheme.isDark) 0.82f else 0.72f)
                            else -> currentTheme.cardBorder.copy(alpha = if (currentTheme.isDark) 0.55f else 0.65f)
                        },
                        animationSpec = PomoMotion.colorSpec,
                        label = "barCol_${bar.dayStartMillis}"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onDayBarTap(bar) }
                            .padding(vertical = 2.dp)
                            .testTag("weekly_bar_${bar.shortDayLabel}"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(if (isSelected) 16.dp else 12.dp)
                                    .fillMaxHeight(animFrac.coerceIn(0f, 1f))
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(barColor)
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = bar.shortDayLabel,
                            fontSize = 10.scaledSp,
                            fontWeight = if (bar.isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (bar.isToday || isSelected) primaryInk else currentTheme.textSecondary,
                            fontFamily = currentFont,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryShareBreakdown(
    slices: List<CategoryShareSlice>,
    theme: ThemeOption,
    chipBg: Color
) {
    val currentFont = LocalAppFont.current
    val totalMins = slices.sumOf { it.minutes }.coerceAtLeast(1)
    val sweepProgress = remember { Animatable(0f) }
    LaunchedEffect(slices) {
        sweepProgress.snapTo(0f)
        sweepProgress.animateTo(1f, animationSpec = PomoMotion.slowSpatial())
    }

    val resolvedColors = remember(slices, theme.primary, theme.secondary, theme.accent, theme.surface, theme.isDark) {
        slices.map { slice ->
            val raw = when {
                slice.colorLong != null -> safeParseColor(slice.colorLong, theme.primary)
                slice.isBreak -> theme.secondary
                else -> theme.primary
            }
            ensureContrast(raw, theme.surface, minContrast = 2.8)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "TODAY'S DISTRIBUTION",
            fontSize = 11.scaledSp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.9.sp,
            color = theme.textSecondary,
            fontFamily = currentFont
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(chipBg)
        ) {
            val reveal = sweepProgress.value.coerceIn(0f, 1f)
            val totalWidth = size.width * reveal
            var cursorX = 0f
            val gapPx = if (slices.size > 1) 2.dp.toPx() else 0f
            val usableWidth = (totalWidth - gapPx * (slices.size - 1)).coerceAtLeast(0f)

            slices.forEachIndexed { idx, slice ->
                val fraction = slice.minutes.toFloat() / totalMins.toFloat()
                val segWidth = usableWidth * fraction
                if (segWidth > 0.5f) {
                    drawRoundRect(
                        color = resolvedColors[idx],
                        topLeft = Offset(cursorX, 0f),
                        size = Size(segWidth, size.height),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    )
                    cursorX += segWidth + gapPx
                }
            }
        }

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            slices.take(4).forEachIndexed { idx, slice ->
                val dotColor = resolvedColors[idx]
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(chipBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    Text(
                        text = "${slice.label} ${formatCompactMinutes(slice.minutes)}",
                        fontSize = 11.scaledSp,
                        color = theme.textPrimary,
                        fontFamily = currentFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyFocusGoalDialog(
    currentGoalMinutes: Int,
    focusLengthMinutes: Int,
    onSave: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    var draftMinutes by remember(currentGoalMinutes) {
        mutableIntStateOf(((currentGoalMinutes + 7) / 15 * 15).coerceIn(30, 480))
    }

    var animateIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { animateIn = true }

    val modalScale by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0.90f,
        animationSpec = PomoMotion.expressiveSpatial(),
        label = "goalModalScale"
    )
    val modalAlpha by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = PomoMotion.defaultEffects(),
        label = "goalModalAlpha"
    )

    val presets = listOf(60, 90, 120, 180, 240)
    val approxPomos = (draftMinutes / focusLengthMinutes.coerceAtLeast(15)).coerceAtLeast(1)
    val modalPrimaryInk = remember(currentTheme.primary, currentTheme.primaryLight, currentTheme.primaryDark, currentTheme.surface, currentTheme.isDark) {
        ensureContrast(
            if (currentTheme.isDark) currentTheme.primaryLight else currentTheme.primaryDark,
            currentTheme.surface,
            minContrast = 4.5
        )
    }
    val modalBannerBg = remember(currentTheme.surface, currentTheme.primary, currentTheme.isDark) {
        currentTheme.surface.blend(currentTheme.primary, if (currentTheme.isDark) 0.24f else 0.14f)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = (0.42f * modalAlpha).coerceIn(0f, 1f)))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                border = BorderStroke(1.dp, currentTheme.cardBorder),
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = modalScale
                        scaleY = modalScale
                        alpha = modalAlpha.coerceIn(0f, 1f)
                        compositingStrategy = if (modalAlpha < 0.999f) {
                            CompositingStrategy.Offscreen
                        } else {
                            CompositingStrategy.Auto
                        }
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Daily Focus Goal",
                            fontSize = 22.scaledSp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textPrimary,
                            fontFamily = currentFont
                        )
                        Text(
                            text = "Adjust your daily target to tune your dashboard ring & 7-day line.",
                            fontSize = 13.scaledSp,
                            color = currentTheme.textSecondary,
                            fontFamily = currentFont
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = modalBannerBg,
                        border = BorderStroke(1.dp, currentTheme.primary.copy(alpha = if (currentTheme.isDark) 0.42f else 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = formatCompactMinutes(draftMinutes),
                                fontSize = 30.scaledSp,
                                fontWeight = FontWeight.Black,
                                color = modalPrimaryInk,
                                fontFamily = MonospaceFontFamily
                            )
                            Text(
                                text = "~$approxPomos focus sessions per day",
                                fontSize = 12.scaledSp,
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont
                            )
                        }
                    }

                    Slider(
                        value = draftMinutes.toFloat(),
                        onValueChange = { raw ->
                            val snapped = ((raw / 15f).roundToInt() * 15).coerceIn(30, 480)
                            if (snapped != draftMinutes) {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                draftMinutes = snapped
                            }
                        },
                        valueRange = 30f..480f,
                        steps = 29,
                        colors = SliderDefaults.colors(
                            thumbColor = currentTheme.primary,
                            activeTrackColor = currentTheme.primary,
                            inactiveTrackColor = currentTheme.primary.copy(alpha = 0.22f)
                        ),
                        modifier = Modifier.testTag("daily_goal_slider")
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presets.forEach { preset ->
                            val isSelected = draftMinutes == preset
                            val interaction = remember { MutableInteractionSource() }
                            val isPressed by interaction.collectIsPressedAsState()
                            val chipScale by animateFloatAsState(
                                targetValue = if (isPressed) 0.94f else 1f,
                                animationSpec = PomoMotion.pressScaleSpec,
                                label = "presetScale_$preset"
                            )

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) currentTheme.primary else currentTheme.backgroundSecondary,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) currentTheme.primary else currentTheme.cardBorder
                                ),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 40.dp)
                                    .graphicsLayer {
                                        scaleX = chipScale
                                        scaleY = chipScale
                                    }
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable(
                                        interactionSource = interaction,
                                        indication = ripple(bounded = true)
                                    ) {
                                        try {
                                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                        } catch (_: Exception) {}
                                        draftMinutes = preset
                                    }
                                    .testTag("goal_preset_$preset")
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val activeText = if (currentTheme.primary.luminance() > 0.55f) {
                                        Color(0xFF1E1E1E)
                                    } else {
                                        Color.White
                                    }
                                    Text(
                                        text = formatCompactMinutes(preset),
                                        fontSize = 13.scaledSp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) activeText else currentTheme.textPrimary,
                                        fontFamily = MonospaceFontFamily
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                onDismiss()
                            }
                        ) {
                            Text("Cancel", color = currentTheme.textSecondary, fontFamily = currentFont)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        val btnContent = if (currentTheme.primary.luminance() > 0.55f) {
                            Color(0xFF1E1E1E)
                        } else {
                            Color.White
                        }
                        Button(
                            onClick = {
                                try {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                } catch (_: Exception) {}
                                onSave(draftMinutes)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = currentTheme.primary,
                                contentColor = btnContent
                            ),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                            modifier = Modifier.testTag("save_daily_goal_button")
                        ) {
                            Text(
                                text = "Save Goal",
                                fontWeight = FontWeight.Bold,
                                fontFamily = currentFont
                            )
                        }
                    }
                }
            }
        }
    }
}
