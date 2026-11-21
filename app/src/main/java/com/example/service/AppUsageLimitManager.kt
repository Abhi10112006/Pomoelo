package com.example.service

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Manages real Android app activity telemetry strictly on the 4:00 AM – 4:00 AM logical day cycle,
 * including 7-day weekly trends, 24-hour distraction rhythm, per-app 7-day history,
 * per-app daily usage limits, "Just 5 mins more" grace passes, and milestone notification state.
 */
object AppUsageLimitManager {

    private const val PREFS_NAME = "PomoPalAppLimitsPrefs"
    private const val KEY_LIMITS_JSON = "app_daily_limits_json"
    private const val KEY_GRACE_EXPIRY_PREFIX = "grace_expiry_"
    private const val KEY_GRACE_COUNT_PREFIX = "grace_count_"
    private const val KEY_NOTIFIED_HALF_PREFIX = "notified_half_"
    private const val KEY_NOTIFIED_FIVE_PREFIX = "notified_five_"
    private const val KEY_NOTIFIED_ONE_PREFIX = "notified_one_"
    private const val KEY_NOTIFIED_GRACE_ONE_PREFIX = "notified_grace_one_"

    const val LOGICAL_DAY_START_HOUR = 4
    const val FIVE_MINUTES_MS = 5L * 60_000L
    const val ONE_MINUTE_MS = 60_000L

    private val EXCLUDED_ACTIVITY_PACKAGES = setOf(
        "com.android.systemui",
        "android",
        "com.android.settings",
        "com.google.android.dialer",
        "com.android.phone",
        "com.android.server.telecom",
        "com.google.android.permissioncontroller",
        "com.android.permissioncontroller"
    )

    @Immutable
    data class DailyUsagePoint(
        val dayStartMillis: Long,
        val shortDayLabel: String,
        val fullDateLabel: String,
        val isToday: Boolean,
        val totalMillis: Long,
        val appUsageMap: Map<String, Long>,
        val hourlyBucketsMillis: List<Long>, // 24 slots: slot 0 = 4:00 AM, slot 23 = 3:00 AM
        val launchCount: Int,
        val longestSessionMillis: Long,
        val longestSessionPackage: String?
    ) {
        val peakHourOffset: Int?
            get() {
                var maxIdx = -1
                var maxVal = 0L
                hourlyBucketsMillis.forEachIndexed { idx, value ->
                    if (value > maxVal) {
                        maxVal = value
                        maxIdx = idx
                    }
                }
                return if (maxIdx >= 0 && maxVal > 0L) maxIdx else null
            }

        val peakHourLabel: String
            get() {
                val offset = peakHourOffset ?: return "—"
                val clockHour = (LOGICAL_DAY_START_HOUR + offset) % 24
                return formatHourCompact(clockHour)
            }
    }

    @Immutable
    data class AppActivityEntry(
        val packageName: String,
        val label: String,
        val usageMillisToday: Long,
        val weeklyTotalMillis: Long = usageMillisToday,
        val weeklyDailyMillis: List<Long> = emptyList(), // 7 values matching DailyUsagePoint order
        val dailyLimitMinutes: Int?,
        val activeGraceExpiryMs: Long,
        val extensionsUsedToday: Int
    ) {
        val usageMinutes: Int get() = (usageMillisToday / 60_000L).toInt()
        val limitMillis: Long? get() = dailyLimitMinutes?.let { it * 60_000L }
        val isLimitReached: Boolean
            get() = limitMillis != null && usageMillisToday >= limitMillis!!
        val isInGraceWindow: Boolean
            get() = activeGraceExpiryMs > System.currentTimeMillis()
        val remainingMillis: Long?
            get() = limitMillis?.let { (it - usageMillisToday).coerceAtLeast(0L) }
        val progressFraction: Float
            get() {
                val limit = limitMillis ?: return 0f
                if (limit <= 0L) return 0f
                return (usageMillisToday.toFloat() / limit.toFloat()).coerceIn(0f, 1.25f)
            }
        val weeklyAverageMillis: Long
            get() = if (weeklyDailyMillis.isEmpty()) usageMillisToday else weeklyTotalMillis / weeklyDailyMillis.size.coerceAtLeast(1)
    }

    @Immutable
    data class AppActivitySummary(
        val hasUsagePermission: Boolean,
        val dayStartMillis: Long,
        val nextResetMillis: Long,
        val totalScreenTimeMillis: Long,
        val weeklyTotalMillis: Long = totalScreenTimeMillis,
        val weeklyDailyAverageMillis: Long = totalScreenTimeMillis,
        val weeklyDays: List<DailyUsagePoint> = emptyList(),
        val apps: List<AppActivityEntry>
    ) {
        val mostUsedApp: AppActivityEntry? get() = apps.firstOrNull { it.usageMillisToday > 0L }
        val topFiveToday: List<AppActivityEntry> get() = apps.filter { it.usageMillisToday > 0L }.take(5)
        val topFiveWeek: List<AppActivityEntry>
            get() = apps
                .filter { it.weeklyTotalMillis > 0L }
                .sortedByDescending { it.weeklyTotalMillis }
                .take(5)
        val activeLimitsCount: Int get() = apps.count { it.dailyLimitMinutes != null }
        val todayPoint: DailyUsagePoint? get() = weeklyDays.lastOrNull()
    }

    enum class UsageMilestone {
        NONE,
        HALFWAY_AND_FIVE_MIN,
        HALFWAY,
        FIVE_MIN_WARNING,
        ONE_MIN_WARNING,
        GRACE_ONE_MIN_WARNING
    }

    data class LimitEvaluation(
        val packageName: String,
        val limitMinutes: Int,
        val usedMillis: Long,
        val limitMillis: Long,
        val remainingMillis: Long,
        val isBlockedNow: Boolean,
        val isInGracePass: Boolean,
        val graceRemainingMillis: Long,
        val extensionsUsedToday: Int,
        val milestoneToNotify: UsageMilestone
    )

    private val _limitsVersion = MutableStateFlow(0L)
    val limitsVersion: StateFlow<Long> = _limitsVersion.asStateFlow()

    @Volatile
    private var cachedLimits: Map<String, Int>? = null

    private fun getPrefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // -------------------------------------------------------------------------
    // 4:00 AM – 4:00 AM Logical Day Helpers (DST-safe via Calendar)
    // -------------------------------------------------------------------------

    fun getLogicalDayStartMillis(nowMillis: Long = System.currentTimeMillis()): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            if (get(Calendar.HOUR_OF_DAY) < LOGICAL_DAY_START_HOUR) {
                add(Calendar.DAY_OF_YEAR, -1)
            }
            set(Calendar.HOUR_OF_DAY, LOGICAL_DAY_START_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun getLogicalDayStartOffsetMillis(dayOffsetFromToday: Int, nowMillis: Long = System.currentTimeMillis()): Long {
        val todayStart = getLogicalDayStartMillis(nowMillis)
        val cal = Calendar.getInstance().apply {
            timeInMillis = todayStart
            add(Calendar.DAY_OF_YEAR, dayOffsetFromToday)
            set(Calendar.HOUR_OF_DAY, LOGICAL_DAY_START_HOUR)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun getNextResetMillis(nowMillis: Long = System.currentTimeMillis()): Long {
        return getLogicalDayStartOffsetMillis(1, nowMillis)
    }

    fun getLogicalDayKey(nowMillis: Long = System.currentTimeMillis()): String {
        val dayStart = getLogicalDayStartMillis(nowMillis)
        return SimpleDateFormat("yyyyMMdd", Locale.US).format(Date(dayStart))
    }

    fun formatHourCompact(hour24: Int): String {
        val h = ((hour24 % 24) + 24) % 24
        return when (h) {
            0 -> "12 AM"
            12 -> "12 PM"
            in 1..11 -> "$h AM"
            else -> "${h - 12} PM"
        }
    }

    fun formatDurationCompact(millis: Long): String {
        val totalSeconds = (millis / 1000L).coerceAtLeast(0L)
        if (totalSeconds == 0L) return "0m"
        if (totalSeconds < 60L) return "< 1m"
        val totalMinutes = totalSeconds / 60L
        val hours = totalMinutes / 60L
        val minutes = totalMinutes % 60L
        return when {
            hours > 0L && minutes > 0L -> "${hours}h ${minutes}m"
            hours > 0L -> "${hours}h"
            else -> "${minutes}m"
        }
    }

    fun formatMinutesLabel(minutes: Int): String {
        val safe = minutes.coerceAtLeast(1)
        val hours = safe / 60
        val mins = safe % 60
        return when {
            hours > 0 && mins > 0 -> "${hours}h ${mins}m"
            hours > 0 -> "${hours}h"
            else -> "${mins}m"
        }
    }

    fun formatTimeUntilReset(nowMillis: Long = System.currentTimeMillis()): String {
        val diff = (getNextResetMillis(nowMillis) - nowMillis).coerceAtLeast(0L)
        val totalMins = (diff / 60_000L).coerceAtLeast(1L)
        val hours = totalMins / 60L
        val mins = totalMins % 60L
        return when {
            hours > 0L && mins > 0L -> "${hours}h ${mins}m"
            hours > 0L -> "${hours}h"
            else -> "${mins}m"
        }
    }

    // -------------------------------------------------------------------------
    // Usage Access Permission Check
    // -------------------------------------------------------------------------

    fun hasUsageStatsPermission(context: Context): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            }
            if (mode == AppOpsManager.MODE_DEFAULT) {
                context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) ==
                    PackageManager.PERMISSION_GRANTED
            } else {
                mode == AppOpsManager.MODE_ALLOWED
            }
        } catch (_: Exception) {
            false
        }
    }

    // -------------------------------------------------------------------------
    // Daily App Limit Persistence
    // -------------------------------------------------------------------------

    fun getAllLimits(context: Context): Map<String, Int> {
        cachedLimits?.let { return it }
        val raw = getPrefs(context).getString(KEY_LIMITS_JSON, null)
        val map = mutableMapOf<String, Int>()
        if (!raw.isNullOrBlank()) {
            try {
                val json = JSONObject(raw)
                val keys = json.keys()
                while (keys.hasNext()) {
                    val pkg = keys.next()
                    val mins = json.optInt(pkg, 0)
                    if (mins > 0) {
                        map[pkg] = mins.coerceIn(5, 360)
                    }
                }
            } catch (_: Exception) {}
        }
        cachedLimits = map
        return map
    }

    fun getAppLimitMinutes(context: Context, packageName: String): Int? {
        return getAllLimits(context)[packageName]
    }

    fun setAppLimitMinutes(context: Context, packageName: String, limitMinutes: Int?) {
        val mutable = getAllLimits(context).toMutableMap()
        if (limitMinutes == null || limitMinutes <= 0) {
            mutable.remove(packageName)
        } else {
            mutable[packageName] = limitMinutes.coerceIn(5, 360)
        }
        val json = JSONObject()
        mutable.forEach { (pkg, mins) -> json.put(pkg, mins) }

        val dayKey = getLogicalDayKey()
        getPrefs(context).edit()
            .putString(KEY_LIMITS_JSON, json.toString())
            .remove(KEY_NOTIFIED_HALF_PREFIX + dayKey + "_" + packageName)
            .remove(KEY_NOTIFIED_FIVE_PREFIX + dayKey + "_" + packageName)
            .remove(KEY_NOTIFIED_ONE_PREFIX + dayKey + "_" + packageName)
            .remove(KEY_GRACE_EXPIRY_PREFIX + dayKey + "_" + packageName)
            .remove(KEY_NOTIFIED_GRACE_ONE_PREFIX + dayKey + "_" + packageName)
            .apply()

        cachedLimits = mutable.toMap()
        _limitsVersion.value = System.currentTimeMillis()
    }

    fun hasAnyDailyLimits(context: Context): Boolean = getAllLimits(context).isNotEmpty()

    // -------------------------------------------------------------------------
    // "Just 5 mins more" Grace Pass (scoped to 4:00 AM – 4:00 AM logical day)
    // -------------------------------------------------------------------------

    fun getActiveGraceExpiryMs(context: Context, packageName: String, nowMs: Long = System.currentTimeMillis()): Long {
        val dayKey = getLogicalDayKey(nowMs)
        val expiry = getPrefs(context).getLong(KEY_GRACE_EXPIRY_PREFIX + dayKey + "_" + packageName, 0L)
        return if (expiry > nowMs) expiry else 0L
    }

    fun getExtensionsUsedToday(context: Context, packageName: String, nowMs: Long = System.currentTimeMillis()): Int {
        val dayKey = getLogicalDayKey(nowMs)
        return getPrefs(context).getInt(KEY_GRACE_COUNT_PREFIX + dayKey + "_" + packageName, 0)
    }

    fun grantFiveMinuteExtension(context: Context, packageName: String): Long {
        val now = System.currentTimeMillis()
        val dayKey = getLogicalDayKey(now)
        val expiryMs = now + FIVE_MINUTES_MS
        val prefs = getPrefs(context)
        val newCount = prefs.getInt(KEY_GRACE_COUNT_PREFIX + dayKey + "_" + packageName, 0) + 1
        prefs.edit()
            .putLong(KEY_GRACE_EXPIRY_PREFIX + dayKey + "_" + packageName, expiryMs)
            .putInt(KEY_GRACE_COUNT_PREFIX + dayKey + "_" + packageName, newCount)
            .remove(KEY_NOTIFIED_GRACE_ONE_PREFIX + dayKey + "_" + packageName)
            .apply()
        _limitsVersion.value = now
        return expiryMs
    }

    fun clearExpiredGrace(context: Context, packageName: String) {
        val dayKey = getLogicalDayKey()
        getPrefs(context).edit()
            .remove(KEY_GRACE_EXPIRY_PREFIX + dayKey + "_" + packageName)
            .remove(KEY_NOTIFIED_GRACE_ONE_PREFIX + dayKey + "_" + packageName)
            .apply()
    }

    // -------------------------------------------------------------------------
    // Real UsageStatsManager 4:00 AM – 4:00 AM Query Engine (Today + Full Week)
    // -------------------------------------------------------------------------

    fun queryUsageMapForCurrentLogicalDay(
        context: Context,
        nowMillis: Long = System.currentTimeMillis()
    ): Map<String, Long> {
        if (!hasUsageStatsPermission(context)) return emptyMap()

        val dayStartMillis = getLogicalDayStartMillis(nowMillis)
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager ?: return emptyMap()

        val usageByPackage = mutableMapOf<String, Long>()
        val activeStartByPackage = mutableMapOf<String, Long>()
        val seenAnyEventForPackage = mutableSetOf<String>()

        try {
            val events = usm.queryEvents(dayStartMillis, nowMillis)
            val event = UsageEvents.Event()
            while (events.hasNextEvent()) {
                events.getNextEvent(event)
                val pkg = event.packageName ?: continue
                val ts = event.timeStamp.coerceIn(dayStartMillis, nowMillis)

                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        seenAnyEventForPackage.add(pkg)
                        if (!activeStartByPackage.containsKey(pkg)) {
                            activeStartByPackage[pkg] = ts
                        }
                    }
                    UsageEvents.Event.ACTIVITY_PAUSED,
                    UsageEvents.Event.ACTIVITY_STOPPED -> {
                        val started = activeStartByPackage.remove(pkg)
                        if (started != null) {
                            val delta = (ts - started).coerceAtLeast(0L)
                            usageByPackage[pkg] = (usageByPackage[pkg] ?: 0L) + delta
                        } else if (!seenAnyEventForPackage.contains(pkg)) {
                            val delta = (ts - dayStartMillis).coerceAtLeast(0L)
                            if (delta in 1L..FIVE_MINUTES_MS * 24) {
                                usageByPackage[pkg] = (usageByPackage[pkg] ?: 0L) + delta
                            }
                        }
                        seenAnyEventForPackage.add(pkg)
                    }
                    UsageEvents.Event.SCREEN_NON_INTERACTIVE,
                    UsageEvents.Event.KEYGUARD_SHOWN -> {
                        activeStartByPackage.forEach { (openPkg, started) ->
                            val delta = (ts - started).coerceAtLeast(0L)
                            usageByPackage[openPkg] = (usageByPackage[openPkg] ?: 0L) + delta
                        }
                        activeStartByPackage.clear()
                    }
                }
            }

            activeStartByPackage.forEach { (openPkg, started) ->
                val delta = (nowMillis - started).coerceAtLeast(0L)
                if (delta in 1L..(4L * 3600_000L)) {
                    usageByPackage[openPkg] = (usageByPackage[openPkg] ?: 0L) + delta
                }
            }
        } catch (_: Exception) {}

        if (usageByPackage.isEmpty()) {
            try {
                val aggregated = usm.queryAndAggregateUsageStats(dayStartMillis, nowMillis)
                aggregated?.forEach { (pkg, stats) ->
                    val fg = stats.totalTimeInForeground
                    if (fg > 0L) {
                        usageByPackage[pkg] = fg
                    }
                }
            } catch (_: Exception) {}
        }

        return usageByPackage
    }

    fun getAppUsageMillisToday(
        context: Context,
        packageName: String,
        nowMillis: Long = System.currentTimeMillis()
    ): Long {
        if (!hasUsageStatsPermission(context)) return 0L
        val map = queryUsageMapForCurrentLogicalDay(context, nowMillis)
        return map[packageName] ?: 0L
    }

    private class MutableDayAccumulator(
        val startMs: Long,
        val endMs: Long
    ) {
        val appUsage = mutableMapOf<String, Long>()
        val hourlyBuckets = LongArray(24)
        var launchCount: Int = 0
        var longestSessionMs: Long = 0L
        var longestSessionPkg: String? = null
    }

    /**
     * Loads the complete 7-day (4:00 AM – 4:00 AM) app activity analytics + today's breakdown
     * in a single fast pass over UsageEvents (with INTERVAL_DAILY fallback for older pruned days).
     */
    fun loadAppActivitySummary(context: Context): AppActivitySummary {
        val now = System.currentTimeMillis()
        val todayStart = getLogicalDayStartMillis(now)
        val nextReset = getNextResetMillis(now)
        val hasPerm = hasUsageStatsPermission(context)
        val limits = getAllLimits(context)

        val pm = context.packageManager
        val defaultLauncherPkg = try {
            val homeIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
            pm.resolveActivity(homeIntent, PackageManager.MATCH_DEFAULT_ONLY)?.activityInfo?.packageName
        } catch (_: Exception) {
            null
        }

        val launcherIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val resolveInfos = try {
            pm.queryIntentActivities(launcherIntent, 0)
        } catch (_: Exception) {
            emptyList()
        }

        val launchableLabels = linkedMapOf<String, String>()
        for (info in resolveInfos) {
            val pkg = info.activityInfo?.packageName ?: continue
            if (pkg == context.packageName) continue
            if (pkg == defaultLauncherPkg) continue
            if (EXCLUDED_ACTIVITY_PACKAGES.contains(pkg)) continue
            if (launchableLabels.containsKey(pkg)) continue
            val label = try {
                info.loadLabel(pm).toString()
            } catch (_: Exception) {
                AppBlockerManager.getAppLabel(context, pkg)
            }
            launchableLabels[pkg] = label
        }

        // Build 7 logical day buckets: index 0 = 6 days ago ... index 6 = Today
        val dayBoundaries = LongArray(8) { idx ->
            getLogicalDayStartOffsetMillis(idx - 6, now)
        }
        val accumulators = Array(7) { idx ->
            MutableDayAccumulator(
                startMs = dayBoundaries[idx],
                endMs = minOf(dayBoundaries[idx + 1], now)
            )
        }

        if (hasPerm) {
            populateWeeklyAccumulators(
                context = context,
                weekStartMs = dayBoundaries[0],
                nowMillis = now,
                dayBoundaries = dayBoundaries,
                accumulators = accumulators,
                allowedPackages = launchableLabels.keys
            )
        }

        val shortDayFmt = SimpleDateFormat("EEE", Locale.getDefault())
        val fullDateFmt = SimpleDateFormat("MMM d", Locale.getDefault())

        val dailyPoints = accumulators.mapIndexed { idx, acc ->
            val isToday = idx == 6
            val totalDayMs = acc.appUsage.values.sum()
            DailyUsagePoint(
                dayStartMillis = acc.startMs,
                shortDayLabel = if (isToday) "Today" else shortDayFmt.format(Date(acc.startMs)),
                fullDateLabel = fullDateFmt.format(Date(acc.startMs)),
                isToday = isToday,
                totalMillis = totalDayMs,
                appUsageMap = acc.appUsage.toMap(),
                hourlyBucketsMillis = acc.hourlyBuckets.toList(),
                launchCount = acc.launchCount,
                longestSessionMillis = acc.longestSessionMs,
                longestSessionPackage = acc.longestSessionPkg
            )
        }

        val todayUsageMap = dailyPoints.lastOrNull()?.appUsageMap ?: emptyMap()
        val totalTodayMs = dailyPoints.lastOrNull()?.totalMillis ?: 0L
        val weeklyTotalMs = dailyPoints.sumOf { it.totalMillis }
        val activeDaysCount = dailyPoints.count { it.totalMillis > 0L }.coerceAtLeast(1)
        val weeklyAvgMs = weeklyTotalMs / activeDaysCount

        val entries = launchableLabels.map { (pkg, label) ->
            val perDay = dailyPoints.map { it.appUsageMap[pkg] ?: 0L }
            val todayMs = todayUsageMap[pkg] ?: 0L
            val weekMs = perDay.sum()
            AppActivityEntry(
                packageName = pkg,
                label = label,
                usageMillisToday = todayMs,
                weeklyTotalMillis = weekMs,
                weeklyDailyMillis = perDay,
                dailyLimitMinutes = limits[pkg],
                activeGraceExpiryMs = getActiveGraceExpiryMs(context, pkg, now),
                extensionsUsedToday = getExtensionsUsedToday(context, pkg, now)
            )
        }.sortedWith(
            compareByDescending<AppActivityEntry> { it.usageMillisToday }
                .thenByDescending { it.weeklyTotalMillis }
                .thenByDescending { it.dailyLimitMinutes != null }
                .thenBy { it.label.lowercase(Locale.getDefault()) }
        )

        return AppActivitySummary(
            hasUsagePermission = hasPerm,
            dayStartMillis = todayStart,
            nextResetMillis = nextReset,
            totalScreenTimeMillis = totalTodayMs,
            weeklyTotalMillis = weeklyTotalMs,
            weeklyDailyAverageMillis = weeklyAvgMs,
            weeklyDays = dailyPoints,
            apps = entries
        )
    }

    private fun populateWeeklyAccumulators(
        context: Context,
        weekStartMs: Long,
        nowMillis: Long,
        dayBoundaries: LongArray,
        accumulators: Array<MutableDayAccumulator>,
        allowedPackages: Set<String>
    ) {
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager ?: return
        val activeStartByPackage = mutableMapOf<String, Long>()
        var lastLaunchedPkg: String? = null

        fun recordInterval(pkg: String, rawStart: Long, rawEnd: Long) {
            if (!allowedPackages.contains(pkg)) return
            val start = rawStart.coerceAtLeast(weekStartMs)
            val end = rawEnd.coerceAtMost(nowMillis)
            if (end <= start) return
            // Guard against bogus unclosed multi-hour ghost events
            if (end - start > 5L * 3600_000L) return

            // Find starting day index
            var dayIdx = 0
            while (dayIdx < 6 && start >= dayBoundaries[dayIdx + 1]) {
                dayIdx++
            }
            var cursor = start
            while (cursor < end && dayIdx < 7) {
                val dayEnd = minOf(dayBoundaries[dayIdx + 1], end)
                if (dayEnd > cursor) {
                    val acc = accumulators[dayIdx]
                    val sliceMs = dayEnd - cursor
                    acc.appUsage[pkg] = (acc.appUsage[pkg] ?: 0L) + sliceMs
                    if (sliceMs > acc.longestSessionMs) {
                        acc.longestSessionMs = sliceMs
                        acc.longestSessionPkg = pkg
                    }
                    // Attribute into 24 logical-day hourly buckets (slot 0 = 4 AM..5 AM)
                    val dayStart = dayBoundaries[dayIdx]
                    var hCursor = cursor
                    while (hCursor < dayEnd) {
                        val hourSlot = (((hCursor - dayStart) / 3600_000L).toInt()).coerceIn(0, 23)
                        val nextHourEdge = minOf(dayStart + ((hourSlot + 1) * 3600_000L), dayEnd)
                        val hourSlice = (nextHourEdge - hCursor).coerceAtLeast(0L)
                        acc.hourlyBuckets[hourSlot] += hourSlice
                        hCursor = if (nextHourEdge > hCursor) nextHourEdge else dayEnd
                    }
                }
                cursor = dayBoundaries[dayIdx + 1]
                dayIdx++
            }
        }

        fun recordLaunch(pkg: String, ts: Long) {
            if (!allowedPackages.contains(pkg)) return
            var dayIdx = 0
            while (dayIdx < 6 && ts >= dayBoundaries[dayIdx + 1]) {
                dayIdx++
            }
            if (dayIdx in 0..6) {
                accumulators[dayIdx].launchCount++
            }
        }

        try {
            val events = usm.queryEvents(weekStartMs, nowMillis)
            val event = UsageEvents.Event()
            while (events.hasNextEvent()) {
                events.getNextEvent(event)
                val pkg = event.packageName ?: continue
                val ts = event.timeStamp.coerceIn(weekStartMs, nowMillis)

                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        if (!activeStartByPackage.containsKey(pkg)) {
                            activeStartByPackage[pkg] = ts
                        }
                        if (pkg != lastLaunchedPkg) {
                            recordLaunch(pkg, ts)
                            lastLaunchedPkg = pkg
                        }
                    }
                    UsageEvents.Event.ACTIVITY_PAUSED,
                    UsageEvents.Event.ACTIVITY_STOPPED -> {
                        val started = activeStartByPackage.remove(pkg)
                        if (started != null) {
                            recordInterval(pkg, started, ts)
                        }
                    }
                    UsageEvents.Event.SCREEN_NON_INTERACTIVE,
                    UsageEvents.Event.KEYGUARD_SHOWN -> {
                        activeStartByPackage.forEach { (openPkg, started) ->
                            recordInterval(openPkg, started, ts)
                        }
                        activeStartByPackage.clear()
                        lastLaunchedPkg = null
                    }
                }
            }

            activeStartByPackage.forEach { (openPkg, started) ->
                if (nowMillis - started in 1L..(4L * 3600_000L)) {
                    recordInterval(openPkg, started, nowMillis)
                }
            }
        } catch (_: Exception) {}

        // Fallback for older days if the OEM pruned raw UsageEvents earlier than 7 days
        for (idx in 0..6) {
            val acc = accumulators[idx]
            if (acc.appUsage.isEmpty() && acc.endMs > acc.startMs) {
                try {
                    val dailyStats = usm.queryUsageStats(
                        UsageStatsManager.INTERVAL_DAILY,
                        acc.startMs,
                        acc.endMs
                    )
                    dailyStats?.forEach { stat ->
                        val pkg = stat.packageName ?: return@forEach
                        if (!allowedPackages.contains(pkg)) return@forEach
                        val fg = stat.totalTimeInForeground
                        if (fg > 0L) {
                            acc.appUsage[pkg] = (acc.appUsage[pkg] ?: 0L) + fg
                        }
                    }
                } catch (_: Exception) {}
            }
        }
    }

    // -------------------------------------------------------------------------
    // Real-Time Limit & Milestone Evaluation (called by AppBlockerService)
    // -------------------------------------------------------------------------

    fun evaluateAppLimit(
        context: Context,
        packageName: String,
        extraForegroundFallbackMs: Long = 0L
    ): LimitEvaluation? {
        val limitMinutes = getAppLimitMinutes(context, packageName) ?: return null
        val now = System.currentTimeMillis()
        val dayKey = getLogicalDayKey(now)
        val prefs = getPrefs(context)

        val statsUsedMs = getAppUsageMillisToday(context, packageName, now)
        val usedMs = maxOf(statsUsedMs, extraForegroundFallbackMs)
        val limitMs = limitMinutes * 60_000L
        val remainingMs = (limitMs - usedMs).coerceAtLeast(0L)

        val graceExpiryMs = getActiveGraceExpiryMs(context, packageName, now)
        val isInGrace = graceExpiryMs > now
        val graceRemainingMs = if (isInGrace) (graceExpiryMs - now).coerceAtLeast(0L) else 0L
        val extensionsUsed = getExtensionsUsedToday(context, packageName, now)

        if (usedMs >= limitMs) {
            if (isInGrace) {
                val graceOneKey = KEY_NOTIFIED_GRACE_ONE_PREFIX + dayKey + "_" + packageName
                val milestone = if (graceRemainingMs in 1L..ONE_MINUTE_MS && !prefs.getBoolean(graceOneKey, false)) {
                    prefs.edit().putBoolean(graceOneKey, true).apply()
                    UsageMilestone.GRACE_ONE_MIN_WARNING
                } else {
                    UsageMilestone.NONE
                }
                return LimitEvaluation(
                    packageName = packageName,
                    limitMinutes = limitMinutes,
                    usedMillis = usedMs,
                    limitMillis = limitMs,
                    remainingMillis = 0L,
                    isBlockedNow = false,
                    isInGracePass = true,
                    graceRemainingMillis = graceRemainingMs,
                    extensionsUsedToday = extensionsUsed,
                    milestoneToNotify = milestone
                )
            } else {
                return LimitEvaluation(
                    packageName = packageName,
                    limitMinutes = limitMinutes,
                    usedMillis = usedMs,
                    limitMillis = limitMs,
                    remainingMillis = 0L,
                    isBlockedNow = true,
                    isInGracePass = false,
                    graceRemainingMillis = 0L,
                    extensionsUsedToday = extensionsUsed,
                    milestoneToNotify = UsageMilestone.NONE
                )
            }
        }

        val halfKey = KEY_NOTIFIED_HALF_PREFIX + dayKey + "_" + packageName
        val fiveKey = KEY_NOTIFIED_FIVE_PREFIX + dayKey + "_" + packageName
        val oneKey = KEY_NOTIFIED_ONE_PREFIX + dayKey + "_" + packageName

        val hasNotifiedHalf = prefs.getBoolean(halfKey, false)
        val hasNotifiedFive = prefs.getBoolean(fiveKey, false)
        val hasNotifiedOne = prefs.getBoolean(oneKey, false)

        val crossedHalf = usedMs >= (limitMs / 2L)
        val withinFiveMin = limitMinutes > 5 && remainingMs in (ONE_MINUTE_MS + 1L)..FIVE_MINUTES_MS
        val withinOneMin = remainingMs in 1L..ONE_MINUTE_MS

        val milestone = when {
            withinOneMin && !hasNotifiedOne -> {
                prefs.edit()
                    .putBoolean(oneKey, true)
                    .putBoolean(fiveKey, true)
                    .putBoolean(halfKey, true)
                    .apply()
                UsageMilestone.ONE_MIN_WARNING
            }
            withinFiveMin && crossedHalf && !hasNotifiedHalf && !hasNotifiedFive -> {
                prefs.edit()
                    .putBoolean(halfKey, true)
                    .putBoolean(fiveKey, true)
                    .apply()
                UsageMilestone.HALFWAY_AND_FIVE_MIN
            }
            withinFiveMin && !hasNotifiedFive -> {
                prefs.edit()
                    .putBoolean(fiveKey, true)
                    .putBoolean(halfKey, true)
                    .apply()
                UsageMilestone.FIVE_MIN_WARNING
            }
            crossedHalf && !hasNotifiedHalf -> {
                prefs.edit().putBoolean(halfKey, true).apply()
                UsageMilestone.HALFWAY
            }
            else -> UsageMilestone.NONE
        }

        return LimitEvaluation(
            packageName = packageName,
            limitMinutes = limitMinutes,
            usedMillis = usedMs,
            limitMillis = limitMs,
            remainingMillis = remainingMs,
            isBlockedNow = false,
            isInGracePass = false,
            graceRemainingMillis = 0L,
            extensionsUsedToday = extensionsUsed,
            milestoneToNotify = milestone
        )
    }
}
