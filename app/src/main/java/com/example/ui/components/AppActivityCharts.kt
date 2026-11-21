package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.AppUsageLimitManager
import com.example.ui.AppIcon
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.MonospaceFontFamily
import com.example.ui.theme.PomoMotion
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt

enum class ActivityTimeScope(val label: String) {
    TODAY("Today"),
    WEEK("7 Days")
}

/**
 * Choreographed GPU-accelerated entrance reveal for dashboard sections (zero layout thrash,
 * critically damped so values never overshoot).
 */
@Composable
fun StaggeredSectionReveal(
    stageIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (stageIndex > 0) delay(stageIndex * 50L)
        progress.animateTo(1f, animationSpec = PomoMotion.smoothSpring(stiffness = 380f))
    }
    Box(
        modifier = modifier.graphicsLayer {
            val p = progress.value.coerceIn(0f, 1f)
            alpha = p
            val s = 0.965f + (0.035f * p)
            scaleX = s
            scaleY = s
            translationY = (1f - p) * 18.dp.toPx()
            compositingStrategy = if (p < 0.999f) CompositingStrategy.Offscreen else CompositingStrategy.Auto
        }
    ) {
        content()
    }
}

@Composable
fun AppActivityHeroBentoCard(
    summary: AppUsageLimitManager.AppActivitySummary,
    scope: ActivityTimeScope,
    selectedDayIndex: Int,
    onScopeChange: (ActivityTimeScope) -> Unit
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val selectedDay = summary.weeklyDays.getOrNull(selectedDayIndex) ?: summary.todayPoint
    val displayMillis = if (scope == ActivityTimeScope.WEEK) {
        summary.weeklyTotalMillis
    } else {
        selectedDay?.totalMillis ?: summary.totalScreenTimeMillis
    }

    val avgMillis = summary.weeklyDailyAverageMillis
    val deltaPercent: Int? = if (scope == ActivityTimeScope.TODAY && avgMillis > 60_000L && displayMillis > 0L) {
        (((displayMillis - avgMillis).toFloat() / avgMillis.toFloat()) * 100f).roundToInt()
    } else {
        null
    }

    val launchesValue = if (scope == ActivityTimeScope.WEEK) {
        summary.weeklyDays.sumOf { it.launchCount }
    } else {
        selectedDay?.launchCount ?: 0
    }

    val peakHourText = if (scope == ActivityTimeScope.WEEK) {
        val combined = LongArray(24)
        summary.weeklyDays.forEach { d ->
            d.hourlyBucketsMillis.forEachIndexed { idx, v ->
                if (idx in 0..23) combined[idx] += v
            }
        }
        val peakIdx = combined.indices.maxByOrNull { combined[it] }
        if (peakIdx != null && combined[peakIdx] > 0L) {
            AppUsageLimitManager.formatHourCompact((AppUsageLimitManager.LOGICAL_DAY_START_HOUR + peakIdx) % 24)
        } else "—"
    } else {
        selectedDay?.peakHourLabel ?: "—"
    }

    val longestSessionMs = if (scope == ActivityTimeScope.WEEK) {
        summary.weeklyDays.maxOfOrNull { it.longestSessionMillis } ?: 0L
    } else {
        selectedDay?.longestSessionMillis ?: 0L
    }

    val calmGreen = Color(0xFF2E7D32)
    val warmCoral = Color(0xFFE65100)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .pomoShadow(
                shape = RoundedCornerShape(28.dp),
                elevation = 3.dp,
                shadowColor = currentTheme.shadowColor
            ),
        shape = RoundedCornerShape(28.dp),
        color = currentTheme.surface,
        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.85f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    if (size.width <= 1f || size.height <= 1f) return@drawBehind
                    val glowRadius = (size.width * 0.65f).coerceAtLeast(1f)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                currentTheme.primary.copy(alpha = 0.11f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.86f, size.height * 0.14f),
                            radius = glowRadius
                        )
                    )
                }
                .padding(20.dp)
        ) {
            // Top bar: Subtle label + Minimal Segmented Pill (Today | 7 Days)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when {
                            scope == ActivityTimeScope.WEEK -> "7-DAY SCREEN TIME"
                            selectedDay?.isToday == true -> "TODAY · SINCE 4:00 AM"
                            selectedDay != null -> "${selectedDay.shortDayLabel.uppercase()}, ${selectedDay.fullDateLabel.uppercase()}"
                            else -> "TODAY · SINCE 4:00 AM"
                        },
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Segmented M3 Pill Switch
                Surface(
                    shape = CircleShape,
                    color = currentTheme.background.copy(alpha = 0.9f),
                    border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.7f))
                ) {
                    Row(modifier = Modifier.padding(3.dp)) {
                        ActivityTimeScope.entries.forEach { option ->
                            val isSelected = scope == option
                            val bgColor by animateColorAsState(
                                targetValue = if (isSelected) currentTheme.primary else Color.Transparent,
                                animationSpec = PomoMotion.colorSpec,
                                label = "scopePillBg"
                            )
                            val fgColor by animateColorAsState(
                                targetValue = if (isSelected) Color.White else currentTheme.textSecondary,
                                animationSpec = PomoMotion.colorSpec,
                                label = "scopePillFg"
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(bgColor)
                                    .clickable {
                                        if (!isSelected) {
                                            try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                            onScopeChange(option)
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 5.dp)
                                    .testTag("activity_scope_${option.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option.label,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = fgColor,
                                    fontFamily = currentFont
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Giant Tabular Display Metric + Delta/Average Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedContent(
                    targetState = AppUsageLimitManager.formatDurationCompact(displayMillis),
                    transitionSpec = { fadeIn(PomoMotion.defaultEffects()).togetherWith(fadeOut(PomoMotion.fastEffects())) },
                    label = "heroTimeAnimated"
                ) { formatted ->
                    Text(
                        text = formatted,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = currentTheme.textPrimary,
                        fontFamily = MonospaceFontFamily,
                        letterSpacing = (-1).sp
                    )
                }

                if (scope == ActivityTimeScope.WEEK) {
                    Surface(
                        shape = CircleShape,
                        color = currentTheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "Avg ${AppUsageLimitManager.formatDurationCompact(avgMillis)}/day",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.primary,
                            fontFamily = currentFont,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                        )
                    }
                } else if (deltaPercent != null) {
                    val isLower = deltaPercent <= 0
                    val pillColor = if (isLower) calmGreen else warmCoral
                    Surface(
                        shape = CircleShape,
                        color = pillColor.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isLower) Icons.Rounded.TrendingDown else Icons.Rounded.TrendingUp,
                                contentDescription = null,
                                tint = pillColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${abs(deltaPercent)}% vs avg",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = pillColor,
                                fontFamily = currentFont
                            )
                        }
                    }
                } else if (summary.activeLimitsCount > 0) {
                    Surface(
                        shape = CircleShape,
                        color = currentTheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${summary.activeLimitsCount} limits on",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.primary,
                            fontFamily = currentFont,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3 Minimal Micro-KPI Pills (Launches · Peak Hour · Longest Streak)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MicroStatTile(
                    icon = Icons.Rounded.TouchApp,
                    label = "Launches",
                    value = if (launchesValue > 0) "$launchesValue" else "—",
                    modifier = Modifier.weight(1f)
                )
                MicroStatTile(
                    icon = Icons.Rounded.Bolt,
                    label = "Peak Hour",
                    value = peakHourText,
                    modifier = Modifier.weight(1f)
                )
                MicroStatTile(
                    icon = Icons.Rounded.Schedule,
                    label = "Longest",
                    value = if (longestSessionMs > 0L) AppUsageLimitManager.formatDurationCompact(longestSessionMs) else "—",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MicroStatTile(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = currentTheme.background.copy(alpha = 0.72f),
        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = currentTheme.primary.copy(alpha = 0.85f),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = currentTheme.textSecondary,
                    fontFamily = currentFont,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = currentTheme.textPrimary,
                fontFamily = MonospaceFontFamily,
                maxLines = 1
            )
        }
    }
}

/**
 * Interactive 7-Day Activity Capsule Bar Chart + 24-Hour (4 AM -> 4 AM) Smooth Rhythm Area Graph.
 * Uses critically-damped springs + strict [0f, 1f] fraction clamping so bars never overshoot/crash.
 */
@Composable
fun WeeklyActivityGraphCard(
    days: List<AppUsageLimitManager.DailyUsagePoint>,
    weeklyAvgMillis: Long,
    selectedDayIndex: Int,
    scope: ActivityTimeScope,
    onSelectDay: (Int) -> Unit
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    if (days.isEmpty()) return

    val maxDayMs = remember(days, weeklyAvgMillis) {
        maxOf(days.maxOfOrNull { it.totalMillis } ?: 0L, weeklyAvgMillis, 60_000L)
    }
    val selectedDay = days.getOrNull(selectedDayIndex) ?: days.last()

    // Entry bar growth multiplier (0f -> 1f smoothly on screen entry)
    val chartEntryProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(80L)
        chartEntryProgress.animateTo(1f, animationSpec = PomoMotion.smoothSpring(stiffness = 300f))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .pomoShadow(
                shape = RoundedCornerShape(26.dp),
                elevation = 2.dp,
                shadowColor = currentTheme.shadowColor
            ),
        shape = RoundedCornerShape(26.dp),
        color = currentTheme.surface,
        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.85f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Weekly Rhythm",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont
                    )
                    Text(
                        text = "Tap any day to inspect · 4 AM – 4 AM",
                        fontSize = 11.5.sp,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont
                    )
                }

                if (weeklyAvgMillis > 0L) {
                    Surface(
                        shape = CircleShape,
                        color = currentTheme.background,
                        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = "avg ${AppUsageLimitManager.formatDurationCompact(weeklyAvgMillis)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.textSecondary,
                            fontFamily = MonospaceFontFamily,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val avgRatio = (weeklyAvgMillis.toFloat() / maxDayMs.toFloat()).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
            ) {
                // Background subtle gridlines + dashed average line
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 20.dp)
                ) {
                    if (size.width <= 1f || size.height <= 1f) return@Canvas
                    val guideColor = currentTheme.cardBorder.copy(alpha = 0.35f)
                    drawLine(
                        color = guideColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                    drawLine(
                        color = guideColor.copy(alpha = 0.22f),
                        start = Offset(0f, size.height * 0.5f),
                        end = Offset(size.width, size.height * 0.5f),
                        strokeWidth = 1.dp.toPx()
                    )
                    if (weeklyAvgMillis > 0L) {
                        val avgY = (size.height * (1f - avgRatio)).coerceIn(0f, size.height)
                        drawLine(
                            color = currentTheme.primary.copy(alpha = 0.45f),
                            start = Offset(0f, avgY),
                            end = Offset(size.width, avgY),
                            strokeWidth = 1.25.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                        )
                    }
                }

                // 7 Interactive Capsule Columns
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    days.forEachIndexed { idx, day ->
                        val isSelected = scope == ActivityTimeScope.TODAY && idx == selectedDayIndex
                        val rawRatio = if (day.totalMillis > 0L) {
                            (day.totalMillis.toFloat() / maxDayMs.toFloat()).coerceIn(0.08f, 1f)
                        } else {
                            0.04f
                        }
                        val animatedRatio by animateFloatAsState(
                            targetValue = (rawRatio * chartEntryProgress.value).coerceIn(0.02f, 1f),
                            animationSpec = PomoMotion.smoothSpring(stiffness = 420f),
                            label = "dayBarHeight_$idx"
                        )
                        val barColor by animateColorAsState(
                            targetValue = when {
                                isSelected -> currentTheme.primary
                                scope == ActivityTimeScope.WEEK && day.totalMillis > 0L -> currentTheme.primary.copy(alpha = 0.72f)
                                day.isToday -> currentTheme.primary.copy(alpha = 0.45f)
                                day.totalMillis > 0L -> currentTheme.primary.copy(alpha = 0.24f)
                                else -> currentTheme.cardBorder.copy(alpha = 0.35f)
                            },
                            animationSpec = PomoMotion.colorSpec,
                            label = "dayBarColor_$idx"
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                    onSelectDay(idx)
                                }
                                .testTag("weekly_bar_$idx"),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                modifier = Modifier.height(18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected || (scope == ActivityTimeScope.WEEK && day.totalMillis == maxDayMs && day.totalMillis > 0L)) {
                                    Text(
                                        text = AppUsageLimitManager.formatDurationCompact(day.totalMillis),
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = currentTheme.primary,
                                        fontFamily = MonospaceFontFamily,
                                        maxLines = 1
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(92.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                val safeHeightFraction = animatedRatio.coerceIn(0.02f, 1f)
                                Box(
                                    modifier = Modifier
                                        .width(if (isSelected) 22.dp else 18.dp)
                                        .fillMaxHeight(safeHeightFraction)
                                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                                        .background(
                                            if (isSelected) {
                                                Brush.verticalGradient(
                                                    listOf(currentTheme.primary, currentTheme.secondary)
                                                )
                                            } else {
                                                Brush.verticalGradient(listOf(barColor, barColor))
                                            }
                                        )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (day.isToday) "Now" else day.shortDayLabel.take(3),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected || day.isToday) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) currentTheme.primary else currentTheme.textSecondary,
                                fontFamily = currentFont,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = currentTheme.cardBorder.copy(alpha = 0.45f))
            Spacer(modifier = Modifier.height(14.dp))

            // 24-Hour Smooth Bezier Rhythm Wave (4 AM -> 4 AM)
            val hourlyBuckets = remember(scope, selectedDay, days) {
                if (scope == ActivityTimeScope.WEEK) {
                    val agg = LongArray(24)
                    days.forEach { d ->
                        d.hourlyBucketsMillis.forEachIndexed { idx, v ->
                            if (idx in 0..23) agg[idx] += v
                        }
                    }
                    agg.toList()
                } else {
                    selectedDay.hourlyBucketsMillis
                }
            }
            val peakIdx = remember(hourlyBuckets) {
                val maxVal = hourlyBuckets.maxOrNull() ?: 0L
                if (maxVal > 0L) hourlyBuckets.indexOfFirst { it == maxVal } else -1
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (scope == ActivityTimeScope.WEEK) "Hourly Rhythm (7-Day)" else "Hourly Rhythm (${selectedDay.shortDayLabel})",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont
                )
                if (peakIdx >= 0) {
                    val peakClock = AppUsageLimitManager.formatHourCompact((AppUsageLimitManager.LOGICAL_DAY_START_HOUR + peakIdx) % 24)
                    Text(
                        text = "Peak · $peakClock",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.primary,
                        fontFamily = currentFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HourlyRhythmWaveCanvas(
                buckets = hourlyBuckets,
                peakIndex = peakIdx,
                revealProgress = chartEntryProgress.value.coerceIn(0f, 1f),
                primaryColor = currentTheme.primary,
                secondaryColor = currentTheme.secondary,
                trackColor = currentTheme.cardBorder.copy(alpha = 0.35f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("4 AM", "10 AM", "4 PM", "10 PM", "4 AM").forEach { label ->
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = currentTheme.textSecondary.copy(alpha = 0.85f),
                        fontFamily = MonospaceFontFamily
                    )
                }
            }
        }
    }
}

@Composable
private fun HourlyRhythmWaveCanvas(
    buckets: List<Long>,
    peakIndex: Int,
    revealProgress: Float,
    primaryColor: Color,
    secondaryColor: Color,
    trackColor: Color
) {
    val maxBucket = remember(buckets) { (buckets.maxOrNull() ?: 0L).coerceAtLeast(1L) }
    val hasData = remember(buckets) { buckets.any { it > 0L } }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
    ) {
        val w = size.width
        val h = size.height
        if (w <= 2f || h <= 2f) return@Canvas

        val topPad = 10.dp.toPx()
        val bottomPad = 6.dp.toPx()
        val usableH = (h - topPad - bottomPad).coerceAtLeast(1f)
        val baselineY = h - bottomPad

        drawLine(
            color = trackColor,
            start = Offset(0f, baselineY),
            end = Offset(w, baselineY),
            strokeWidth = 1.dp.toPx()
        )

        if (!hasData || buckets.size < 2) {
            drawLine(
                color = primaryColor.copy(alpha = 0.25f),
                start = Offset(0f, (baselineY - 4.dp.toPx()).coerceAtLeast(topPad)),
                end = Offset(w * revealProgress.coerceIn(0.05f, 1f), (baselineY - 4.dp.toPx()).coerceAtLeast(topPad)),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            return@Canvas
        }

        val stepX = w / (buckets.size - 1).toFloat()
        val points = buckets.mapIndexed { idx, value ->
            val ratio = (value.toFloat() / maxBucket.toFloat()).coerceIn(0f, 1f)
            Offset(
                x = idx * stepX,
                y = baselineY - (ratio * usableH)
            )
        }

        val strokePath = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (i in 0 until points.lastIndex) {
                val p0 = points[i]
                val p1 = points[i + 1]
                val midX = (p0.x + p1.x) * 0.5f
                cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
            }
        }

        val fillPath = Path().apply {
            addPath(strokePath)
            lineTo(points.last().x, baselineY)
            lineTo(points.first().x, baselineY)
            close()
        }

        val clipRight = (w * revealProgress.coerceIn(0.01f, 1f)).coerceIn(1f, w)
        clipRect(left = 0f, top = 0f, right = clipRight, bottom = h) {
            if (baselineY > topPad) {
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.26f), Color.Transparent),
                        startY = topPad,
                        endY = baselineY
                    )
                )
            }

            drawPath(
                path = strokePath,
                brush = Brush.horizontalGradient(listOf(primaryColor, secondaryColor)),
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )

            if (peakIndex in points.indices && revealProgress > 0.7f) {
                val peak = points[peakIndex]
                drawCircle(
                    color = primaryColor.copy(alpha = 0.22f),
                    radius = 9.dp.toPx(),
                    center = peak
                )
                drawCircle(
                    color = Color.White,
                    radius = 4.5.dp.toPx(),
                    center = peak
                )
                drawCircle(
                    color = primaryColor,
                    radius = 3.dp.toPx(),
                    center = peak
                )
            }
        }
    }
}

/**
 * Top 5 Most Used Apps — Minimal Editorial Podium with Proportional 5-Color Ribbon & 1-Tap Limit Pills.
 */
@Composable
fun TopFiveAppsCard(
    entries: List<Pair<AppUsageLimitManager.AppActivityEntry, Long>>,
    totalScopeMillis: Long,
    scopeSubtitle: String,
    onSelectAppToLimit: (AppUsageLimitManager.AppActivityEntry) -> Unit
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val palette = remember(currentTheme) {
        listOf(
            currentTheme.primary,
            currentTheme.secondary,
            currentTheme.accent,
            Color(0xFF7E57C2),
            Color(0xFF26A69A)
        )
    }

    val barSweepReveal = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(130L)
        barSweepReveal.animateTo(1f, animationSpec = PomoMotion.smoothSpring(stiffness = 320f))
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .pomoShadow(
                shape = RoundedCornerShape(26.dp),
                elevation = 2.dp,
                shadowColor = currentTheme.shadowColor
            ),
        shape = RoundedCornerShape(26.dp),
        color = currentTheme.surface,
        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.85f))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Top 5 Most Used",
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.textPrimary,
                        fontFamily = currentFont
                    )
                    Text(
                        text = scopeSubtitle,
                        fontSize = 11.5.sp,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = currentTheme.primary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "Tap to limit",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = currentTheme.primary,
                        fontFamily = currentFont,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (entries.isEmpty() || totalScopeMillis <= 0L) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = currentTheme.background.copy(alpha = 0.72f),
                    border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            tint = currentTheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "No app usage recorded in this window yet.",
                            fontSize = 12.5.sp,
                            color = currentTheme.textSecondary,
                            fontFamily = currentFont
                        )
                    }
                }
            } else {
                val safeTotal = totalScopeMillis.coerceAtLeast(1L).toFloat()
                val topSum = entries.sumOf { it.second }
                val otherMs = (totalScopeMillis - topSum).coerceAtLeast(0L)

                // Proportional 5-segment ribbon + "Other"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape)
                        .background(currentTheme.cardBorder.copy(alpha = 0.25f)),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    entries.forEachIndexed { idx, (_, ms) ->
                        val w = (ms.toFloat() / safeTotal).coerceIn(0.03f, 1f)
                        Box(
                            modifier = Modifier
                                .weight(w)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(palette[idx % palette.size])
                        )
                    }
                    if (otherMs > 0L) {
                        val ow = (otherMs.toFloat() / safeTotal).coerceIn(0.03f, 1f)
                        Box(
                            modifier = Modifier
                                .weight(ow)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(currentTheme.cardBorder.copy(alpha = 0.65f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val maxEntryMs = (entries.firstOrNull()?.second ?: 1L).coerceAtLeast(1L)

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    entries.forEachIndexed { idx, (app, usageMs) ->
                        val accent = palette[idx % palette.size]
                        val targetBar = ((usageMs.toFloat() / maxEntryMs.toFloat()) * barSweepReveal.value)
                            .coerceIn(0.03f, 1f)
                        val relativeRatio by animateFloatAsState(
                            targetValue = targetBar,
                            animationSpec = PomoMotion.smoothSpring(stiffness = 420f),
                            label = "top5Bar_$idx"
                        )
                        val sharePct = ((usageMs.toFloat() / safeTotal) * 100f).roundToInt().coerceIn(1, 100)
                        val rowInteraction = remember { MutableInteractionSource() }
                        val isPressed by rowInteraction.collectIsPressedAsState()
                        val rowScale by animateFloatAsState(
                            targetValue = if (isPressed) 0.98f else 1f,
                            animationSpec = PomoMotion.pressScaleSpec,
                            label = "top5Scale_$idx"
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    scaleX = rowScale
                                    scaleY = rowScale
                                }
                                .clip(RoundedCornerShape(16.dp))
                                .background(currentTheme.background.copy(alpha = 0.55f))
                                .clickable(
                                    interactionSource = rowInteraction,
                                    indication = ripple(bounded = true)
                                ) {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                                    onSelectAppToLimit(app)
                                }
                                .padding(horizontal = 10.dp, vertical = 9.dp)
                                .testTag("top_five_app_${idx + 1}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "0${idx + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = accent,
                                fontFamily = MonospaceFontFamily,
                                modifier = Modifier.width(22.dp)
                            )

                            AppIcon(packageName = app.packageName)

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = app.label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        color = currentTheme.textPrimary,
                                        fontFamily = currentFont,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${AppUsageLimitManager.formatDurationCompact(usageMs)} · $sharePct%",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = currentTheme.textPrimary,
                                        fontFamily = MonospaceFontFamily
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(accent.copy(alpha = 0.14f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(relativeRatio.coerceIn(0.02f, 1f))
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(accent)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            val isLimited = app.dailyLimitMinutes != null
                            val pillTint = when {
                                app.isLimitReached -> Color(0xFFE53935)
                                isLimited -> accent
                                else -> currentTheme.textSecondary
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isLimited) pillTint.copy(alpha = 0.14f) else currentTheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (isLimited) pillTint.copy(alpha = 0.35f) else currentTheme.cardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Timer,
                                        contentDescription = null,
                                        tint = pillTint,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = app.dailyLimitMinutes?.let {
                                            AppUsageLimitManager.formatMinutesLabel(it)
                                        } ?: "Cap",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLimited) pillTint else currentTheme.textPrimary,
                                        fontFamily = currentFont
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

/**
 * Genuine data-driven 1-tap limit suggestion for the user's top unlimited distraction app.
 */
@Composable
fun SmartLimitSuggestionCard(
    topUnlimitedApp: AppUsageLimitManager.AppActivityEntry,
    suggestedMinutes: Int,
    onQuickApply: () -> Unit
) {
    val view = LocalView.current
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        color = currentTheme.primary.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, currentTheme.primary.copy(alpha = 0.24f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(currentTheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    tint = currentTheme.primary,
                    modifier = Modifier.size(19.dp)
                )
            }
            Spacer(modifier = Modifier.width(11.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Smart Focus Tip",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.primary,
                    fontFamily = currentFont
                )
                Text(
                    text = "${topUnlimitedApp.label} averages ${AppUsageLimitManager.formatDurationCompact(topUnlimitedApp.weeklyAverageMillis)}/day. Try a ${AppUsageLimitManager.formatMinutesLabel(suggestedMinutes)} cap?",
                    fontSize = 11.5.sp,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont,
                    lineHeight = 15.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            FilledTonalButton(
                onClick = {
                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (_: Exception) {}
                    onQuickApply()
                },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = currentTheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier.testTag("smart_tip_apply_button")
            ) {
                Text(
                    text = "Set ${AppUsageLimitManager.formatMinutesLabel(suggestedMinutes)}",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = currentFont
                )
            }
        }
    }
}

/**
 * Interactive 7-day per-app mini bar chart inside the Daily Limit Bottom Sheet.
 * Dragging the limit slider glides a glowing dashed threshold line across the app's real 7-day bars.
 */
@Composable
fun AppSevenDayLimitPreviewChart(
    weeklyDailyMillis: List<Long>,
    dayLabels: List<String>,
    selectedLimitMinutes: Int
) {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    if (weeklyDailyMillis.isEmpty()) return

    val limitMs = selectedLimitMinutes.coerceAtLeast(5) * 60_000L
    val maxScaleMs = maxOf(
        weeklyDailyMillis.maxOrNull() ?: 0L,
        limitMs,
        15 * 60_000L
    )
    val overCount = weeklyDailyMillis.count { it > limitMs }
    val warnCoral = Color(0xFFE65100)

    val animatedLimitRatio by animateFloatAsState(
        targetValue = (limitMs.toFloat() / maxScaleMs.toFloat()).coerceIn(0.05f, 1f),
        animationSpec = PomoMotion.smoothSpring(stiffness = 650f),
        label = "limitLineRatio"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = currentTheme.background.copy(alpha = 0.78f),
        border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.55f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "7-Day History vs Limit",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary,
                    fontFamily = currentFont
                )
                Text(
                    text = when {
                        weeklyDailyMillis.all { it == 0L } -> "No usage this week"
                        overCount == 0 -> "Under limit all week"
                        overCount == 1 -> "Would cap 1 day"
                        else -> "Would cap $overCount days"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (overCount > 0) warnCoral else currentTheme.primary,
                    fontFamily = currentFont
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                if (size.width <= 2f || size.height <= 2f) return@Canvas
                val count = weeklyDailyMillis.size
                if (count <= 0) return@Canvas
                val slotW = size.width / count.toFloat()
                val barW = (slotW * 0.42f).coerceIn(8.dp.toPx(), 20.dp.toPx())
                val h = size.height

                weeklyDailyMillis.forEachIndexed { idx, ms ->
                    val ratio = if (ms > 0L) (ms.toFloat() / maxScaleMs.toFloat()).coerceIn(0.06f, 1f) else 0.04f
                    val barH = (h * ratio).coerceIn(2f, h)
                    val left = idx * slotW + (slotW - barW) / 2f
                    val barColor = when {
                        ms > limitMs -> warnCoral.copy(alpha = 0.85f)
                        ms > 0L -> currentTheme.primary.copy(alpha = 0.55f)
                        else -> currentTheme.cardBorder.copy(alpha = 0.35f)
                    }
                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(left, h - barH),
                        size = Size(barW, barH),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                }

                val safeRatio = animatedLimitRatio.coerceIn(0.04f, 1f)
                val lineY = (h * (1f - safeRatio)).coerceIn(2f, h - 2f)
                drawLine(
                    color = currentTheme.primary,
                    start = Offset(0f, lineY),
                    end = Offset(size.width, lineY),
                    strokeWidth = 1.75.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weeklyDailyMillis.indices.forEach { idx ->
                    val lbl = dayLabels.getOrNull(idx) ?: ""
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (idx == weeklyDailyMillis.lastIndex) "Now" else lbl.take(3),
                            fontSize = 10.sp,
                            fontWeight = if (idx == weeklyDailyMillis.lastIndex) FontWeight.Bold else FontWeight.Medium,
                            color = currentTheme.textSecondary,
                            fontFamily = currentFont
                        )
                    }
                }
            }
        }
    }
}

/**
 * Minimal animated loading placeholder matching the top Bento Hero + Weekly Graph cards
 * so the screen starts calmly at the top with zero scroll-anchor jump.
 */
@Composable
fun AppActivityLoadingSkeleton() {
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(178.dp),
            shape = RoundedCornerShape(28.dp),
            color = currentTheme.surface,
            border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.65f))
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = currentTheme.primary,
                        strokeWidth = 2.5.dp,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Loading 7-day activity...",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = currentTheme.textSecondary,
                        fontFamily = currentFont
                    )
                }
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(26.dp),
            color = currentTheme.surface.copy(alpha = 0.72f),
            border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.45f))
        ) {}
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(26.dp),
            color = currentTheme.surface.copy(alpha = 0.55f),
            border = BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.35f))
        ) {}
    }
}
