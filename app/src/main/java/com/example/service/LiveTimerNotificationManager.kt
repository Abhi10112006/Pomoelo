package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.graphics.ColorUtils
import com.example.MainActivity
import com.example.R
import java.util.Locale

/**
 * Builds Android 16+ (API 36+) native Live Update / Dynamic Island / Status Chip notifications
 * using [Notification.ProgressStyle] with Material 3 multi-phase journey segments, milestone
 * points, live chronometer countdown, and tonal M3 ring badges — automatically falling back to a
 * clean, flicker-free ongoing Chronometer notification on Android 15 and below.
 */
object LiveTimerNotificationManager {

    const val LIVE_TIMER_CHANNEL_ID = "PomoPalLiveTimerChannel"
    const val COMPLETION_CHANNEL_ID = "TimerCompletionChannel"

    private const val PROGRESS_MAX = 1000

    data class LiveTimerSnapshot(
        val taskName: String,
        val isBreak: Boolean,
        val state: TimerManager.TimerState,
        val timeRemainingSeconds: Int,
        val totalSeconds: Int,
        val taskColor: Long?,
        val isDndActive: Boolean,
        val focusCompletedToday: Int
    )

    private data class BadgeCacheKey(
        val accentArgb: Int,
        val secondaryArgb: Int,
        val percent: Int,
        val isPaused: Boolean,
        val isBreak: Boolean,
        val isDarkTheme: Boolean
    )

    @Volatile
    private var lastBadgeKey: BadgeCacheKey? = null

    @Volatile
    private var lastBadgeBitmap: Bitmap? = null

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return

        // Android 16 Live Updates require a non-IMPORTANCE_MIN channel (IMPORTANCE_DEFAULT with
        // sound & vibration disabled promotes cleanly across Pixel, Samsung One UI 8, OriginOS 6,
        // ColorOS, HyperOS, etc. while staying completely silent during session ticks).
        val liveChannel = NotificationChannel(
            LIVE_TIMER_CHANNEL_ID,
            "Live Focus Timer",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Shows your live focus countdown, progress journey, and quick controls in the status chip, island, and lock screen."
            setSound(null, null)
            enableVibration(false)
            enableLights(false)
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(liveChannel)

        val completionChannel = NotificationChannel(
            COMPLETION_CHANNEL_ID,
            "Session Completion Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies you when a focus or break session finishes."
            enableVibration(true)
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(completionChannel)
    }

    fun resolveAccentArgb(taskColor: Long?, isBreak: Boolean): Int {
        val theme = SettingsManager.themeState.value
        val defaultArgb = if (isBreak) theme.secondary.toArgb() else theme.primary.toArgb()
        return if (taskColor != null) {
            safeParseColorArgb(taskColor, defaultArgb)
        } else {
            defaultArgb
        }
    }

    fun buildLiveTimerNotification(
        context: Context,
        snapshot: LiveTimerSnapshot
    ): Notification {
        val safeTotal = snapshot.totalSeconds.coerceAtLeast(1)
        val safeRemaining = snapshot.timeRemainingSeconds.coerceIn(0, safeTotal)
        val elapsed = (safeTotal - safeRemaining).coerceIn(0, safeTotal)
        val progressFraction = (elapsed.toFloat() / safeTotal.toFloat()).coerceIn(0f, 1f)
        val progressThousandths = (progressFraction * PROGRESS_MAX).toInt().coerceIn(0, PROGRESS_MAX)
        val percent = (progressFraction * 100f).toInt().coerceIn(0, 100)

        val isRunning = snapshot.state == TimerManager.TimerState.RUNNING
        val isPaused = snapshot.state == TimerManager.TimerState.PAUSED
        val endTimeMillis = System.currentTimeMillis() + (safeRemaining * 1000L)

        val cleanTaskName = when {
            snapshot.isBreak && (snapshot.taskName.isBlank() || snapshot.taskName == "Break Time!") -> "Mindful Break"
            !snapshot.isBreak && (snapshot.taskName.isBlank() || snapshot.taskName == "Focus Time!") -> "Deep Focus"
            else -> snapshot.taskName.trim()
        }

        val phase = resolveSessionPhase(
            isBreak = snapshot.isBreak,
            isPaused = isPaused,
            progressFraction = progressFraction
        )

        val timeText = formatClock(safeRemaining)
        val subText = buildSubText(snapshot, phase, percent, isPaused)
        val titleText = if (isPaused) {
            "$cleanTaskName • Paused ($timeText)"
        } else {
            cleanTaskName
        }
        val bodyText = buildBodyText(
            phase = phase,
            isBreak = snapshot.isBreak,
            isPaused = isPaused,
            timeText = timeText,
            isDndActive = snapshot.isDndActive,
            focusCompletedToday = snapshot.focusCompletedToday
        )

        val theme = SettingsManager.themeState.value
        val accentArgb = resolveAccentArgb(snapshot.taskColor, snapshot.isBreak)
        val secondaryArgb = theme.accent.toArgb()
        val largeIconBitmap = getOrCreateM3ProgressBadge(
            accentArgb = accentArgb,
            secondaryArgb = secondaryArgb,
            percent = percent,
            isPaused = isPaused,
            isBreak = snapshot.isBreak,
            isDarkTheme = theme.isDark
        )

        val openAppIntent = createOpenAppPendingIntent(context, requestCode = 0)
        val toggleActionIntent = createServicePendingIntent(
            context = context,
            requestCode = 1,
            action = if (isRunning) TimerService.ACTION_PAUSE else TimerService.ACTION_START
        )
        val addFiveIntent = createServicePendingIntent(
            context = context,
            requestCode = 2,
            action = TimerService.ACTION_ADD_FIVE_MIN
        )
        val endOrSkipIntent = createServicePendingIntent(
            context = context,
            requestCode = 3,
            action = if (snapshot.isBreak) TimerService.ACTION_SKIP_BREAK else TimerService.ACTION_STOP
        )
        val dismissIntent = createServicePendingIntent(
            context = context,
            requestCode = 4,
            action = TimerService.ACTION_NOTIFICATION_DISMISSED
        )

        val toggleIconRes = if (isRunning) R.drawable.ic_notif_pause else R.drawable.ic_notif_play
        val toggleLabel = if (isRunning) "Pause" else "Resume"
        val thirdIconRes = if (snapshot.isBreak) R.drawable.ic_notif_skip else R.drawable.ic_notif_stop
        val thirdLabel = if (snapshot.isBreak) "Skip" else "End"

        return if (Build.VERSION.SDK_INT >= 36) {
            buildAndroid16LiveUpdate(
                context = context,
                titleText = titleText,
                bodyText = bodyText,
                subText = subText,
                isRunning = isRunning,
                isPaused = isPaused,
                isBreak = snapshot.isBreak,
                endTimeMillis = endTimeMillis,
                progressThousandths = progressThousandths,
                accentArgb = accentArgb,
                warmArgb = blendColor(accentArgb, Color.WHITE, 0.34f),
                sprintArgb = blendColor(accentArgb, secondaryArgb, 0.45f),
                largeIconBitmap = largeIconBitmap,
                openAppIntent = openAppIntent,
                dismissIntent = dismissIntent,
                toggleIconRes = toggleIconRes,
                toggleLabel = toggleLabel,
                toggleIntent = toggleActionIntent,
                addFiveIntent = addFiveIntent,
                thirdIconRes = thirdIconRes,
                thirdLabel = thirdLabel,
                thirdIntent = endOrSkipIntent
            )
        } else {
            buildLegacyChronometerFallback(
                context = context,
                titleText = titleText,
                bodyText = bodyText,
                subText = subText,
                isRunning = isRunning,
                endTimeMillis = endTimeMillis,
                progressThousandths = progressThousandths,
                accentArgb = accentArgb,
                largeIconBitmap = largeIconBitmap,
                openAppIntent = openAppIntent,
                toggleIconRes = toggleIconRes,
                toggleLabel = toggleLabel,
                toggleIntent = toggleActionIntent,
                addFiveIntent = addFiveIntent,
                thirdIconRes = thirdIconRes,
                thirdLabel = thirdLabel,
                thirdIntent = endOrSkipIntent
            )
        }
    }

    @RequiresApi(36)
    private fun buildAndroid16LiveUpdate(
        context: Context,
        titleText: String,
        bodyText: String,
        subText: String,
        isRunning: Boolean,
        isPaused: Boolean,
        isBreak: Boolean,
        endTimeMillis: Long,
        progressThousandths: Int,
        accentArgb: Int,
        warmArgb: Int,
        sprintArgb: Int,
        largeIconBitmap: Bitmap,
        openAppIntent: PendingIntent,
        dismissIntent: PendingIntent,
        toggleIconRes: Int,
        toggleLabel: String,
        toggleIntent: PendingIntent,
        addFiveIntent: PendingIntent,
        thirdIconRes: Int,
        thirdLabel: String,
        thirdIntent: PendingIntent
    ): Notification {
        val trackerIconRes = when {
            isPaused -> R.drawable.ic_notif_paused_tracker
            isBreak -> R.drawable.ic_notif_break_tracker
            else -> R.drawable.ic_notif_focus_tracker
        }

        val segments = if (isBreak) {
            listOf(
                Notification.ProgressStyle.Segment(650).setColor(accentArgb),
                Notification.ProgressStyle.Segment(350).setColor(sprintArgb)
            )
        } else {
            listOf(
                Notification.ProgressStyle.Segment(200).setColor(warmArgb),
                Notification.ProgressStyle.Segment(600).setColor(accentArgb),
                Notification.ProgressStyle.Segment(200).setColor(sprintArgb)
            )
        }

        val milestonePoints = if (isBreak) {
            listOf(
                Notification.ProgressStyle.Point(650).setColor(sprintArgb)
            )
        } else {
            listOf(
                Notification.ProgressStyle.Point(200).setColor(warmArgb),
                Notification.ProgressStyle.Point(500).setColor(accentArgb),
                Notification.ProgressStyle.Point(800).setColor(sprintArgb)
            )
        }

        val progressStyle = Notification.ProgressStyle()
            .setStyledByProgress(false)
            .setProgress(progressThousandths)
            .setProgressTrackerIcon(Icon.createWithResource(context, trackerIconRes))
            .setProgressSegments(segments)
            .setProgressPoints(milestonePoints)

        val extras = Bundle().apply {
            putBoolean(Notification.EXTRA_REQUEST_PROMOTED_ONGOING, true)
        }

        val builder = Notification.Builder(context, LIVE_TIMER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setLargeIcon(largeIconBitmap)
            .setSubText(subText)
            .setContentTitle(titleText)
            .setContentText(bodyText)
            .setStyle(progressStyle)
            .setContentIntent(openAppIntent)
            .setDeleteIntent(dismissIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setColor(accentArgb)
            .setColorized(false) // Required by Android 16 Live Update promotion rules
            .setCategory(Notification.CATEGORY_PROGRESS)
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .addExtras(extras)
            .addAction(
                Notification.Action.Builder(
                    Icon.createWithResource(context, toggleIconRes),
                    toggleLabel,
                    toggleIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    Icon.createWithResource(context, R.drawable.ic_notif_add_time),
                    "+5 min",
                    addFiveIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    Icon.createWithResource(context, thirdIconRes),
                    thirdLabel,
                    thirdIntent
                ).build()
            )

        if (isRunning) {
            // Native SystemUI countdown inside Pixel Status Chip, Samsung Now Bar, and OriginOS 6 Atomic Island
            builder
                .setWhen(endTimeMillis)
                .setShowWhen(true)
                .setUsesChronometer(true)
                .setChronometerCountDown(true)
        } else {
            // Crisp <= 7 char status chip label when paused
            builder
                .setShowWhen(false)
                .setUsesChronometer(false)
                .setShortCriticalText("Paused")
        }

        return builder.build()
    }

    private fun buildLegacyChronometerFallback(
        context: Context,
        titleText: String,
        bodyText: String,
        subText: String,
        isRunning: Boolean,
        endTimeMillis: Long,
        progressThousandths: Int,
        accentArgb: Int,
        largeIconBitmap: Bitmap,
        openAppIntent: PendingIntent,
        toggleIconRes: Int,
        toggleLabel: String,
        toggleIntent: PendingIntent,
        addFiveIntent: PendingIntent,
        thirdIconRes: Int,
        thirdLabel: String,
        thirdIntent: PendingIntent
    ): Notification {
        val builder = NotificationCompat.Builder(context, LIVE_TIMER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setLargeIcon(largeIconBitmap)
            .setSubText(subText)
            .setContentTitle(titleText)
            .setContentText(bodyText)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(titleText)
                    .bigText(bodyText)
            )
            .setContentIntent(openAppIntent)
            .setOngoing(isRunning)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setColor(accentArgb)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setProgress(PROGRESS_MAX, progressThousandths, false)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(toggleIconRes, toggleLabel, toggleIntent)
            .addAction(R.drawable.ic_notif_add_time, "+5 min", addFiveIntent)
            .addAction(thirdIconRes, thirdLabel, thirdIntent)

        if (isRunning) {
            builder
                .setWhen(endTimeMillis)
                .setShowWhen(true)
                .setUsesChronometer(true)
                .setChronometerCountDown(true)
        } else {
            builder
                .setShowWhen(false)
                .setUsesChronometer(false)
        }

        return builder.build()
    }

    fun buildCompletionNotification(
        context: Context,
        completedWasBreak: Boolean,
        completedTaskName: String,
        completedDurationMins: Int,
        taskColor: Long?
    ): Notification {
        val openPendingIntent = createOpenAppPendingIntent(context, requestCode = 10)
        val nextSessionPendingIntent = createServicePendingIntent(
            context = context,
            requestCode = 11,
            action = TimerService.ACTION_START
        )

        val subText = if (completedWasBreak) {
            "Break Complete • Recharged"
        } else {
            "Focus Complete • ${completedDurationMins}m Logged"
        }

        val cleanTask = if (completedTaskName == "Focus Time!" || completedTaskName.isBlank()) {
            "your focus session"
        } else {
            completedTaskName
        }

        val title = if (completedWasBreak) {
            "Break finished — ready to focus?"
        } else {
            "Session complete • Well done"
        }

        val nextMins = if (completedWasBreak) {
            SettingsManager.getFocusTimeMins()
        } else {
            SettingsManager.getBreakTimeMins()
        }

        val body = if (completedWasBreak) {
            "Your mindful break is complete. Start your next ${nextMins}-minute focus block whenever you're ready."
        } else {
            "You logged ${completedDurationMins}m on $cleanTask. Take a ${nextMins}-minute break to rest and reset."
        }

        val actionLabel = if (completedWasBreak) "Start Focus (${nextMins}m)" else "Start Break (${nextMins}m)"
        val theme = SettingsManager.themeState.value
        val accentArgb = resolveAccentArgb(taskColor, completedWasBreak)
        val completionBadge = createM3CompletionBadge(accentArgb, theme.isDark)

        return NotificationCompat.Builder(context, COMPLETION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setLargeIcon(completionBadge)
            .setSubText(subText)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(body)
            )
            .setContentIntent(openPendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)
            .setColor(accentArgb)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_notif_play, actionLabel, nextSessionPendingIntent)
            .build()
    }

    private enum class SessionPhase(val shortLabel: String) {
        WARM_UP("Warm-Up"),
        DEEP_FLOW("Deep Flow"),
        HALFWAY("Strong Momentum"),
        FINAL_SPRINT("Final Sprint"),
        BREAK_UNWIND("Unwind"),
        BREAK_WRAP("Returning Soon"),
        PAUSED("Paused")
    }

    private fun resolveSessionPhase(
        isBreak: Boolean,
        isPaused: Boolean,
        progressFraction: Float
    ): SessionPhase {
        if (isPaused) return SessionPhase.PAUSED
        if (isBreak) {
            return if (progressFraction < 0.65f) SessionPhase.BREAK_UNWIND else SessionPhase.BREAK_WRAP
        }
        return when {
            progressFraction < 0.20f -> SessionPhase.WARM_UP
            progressFraction < 0.50f -> SessionPhase.DEEP_FLOW
            progressFraction < 0.80f -> SessionPhase.HALFWAY
            else -> SessionPhase.FINAL_SPRINT
        }
    }

    private fun buildSubText(
        snapshot: LiveTimerSnapshot,
        phase: SessionPhase,
        percent: Int,
        isPaused: Boolean
    ): String {
        val mode = if (snapshot.isBreak) "Break" else "Focus"
        return if (isPaused) {
            "$mode Paused • $percent%"
        } else {
            "$mode • ${phase.shortLabel} • $percent%"
        }
    }

    private fun buildBodyText(
        phase: SessionPhase,
        isBreak: Boolean,
        isPaused: Boolean,
        timeText: String,
        isDndActive: Boolean,
        focusCompletedToday: Int
    ): String {
        if (isPaused) {
            return "Paused with $timeText left. Tap Resume to pick up where you left off."
        }
        val dndNote = if (!isBreak && isDndActive) " • DND active" else ""
        val countNote = if (!isBreak && focusCompletedToday > 0) " • #$focusCompletedToday today" else ""
        return when (phase) {
            SessionPhase.WARM_UP -> "Settling into focus$dndNote$countNote"
            SessionPhase.DEEP_FLOW -> "In the zone — stay with one goal$dndNote$countNote"
            SessionPhase.HALFWAY -> "Past halfway — keep your rhythm$dndNote$countNote"
            SessionPhase.FINAL_SPRINT -> "Final stretch — finish strong$dndNote$countNote"
            SessionPhase.BREAK_UNWIND -> "Step back, relax your eyes, and breathe"
            SessionPhase.BREAK_WRAP -> "Break wrapping up — next focus block ahead"
            SessionPhase.PAUSED -> "Paused with $timeText remaining."
        }
    }

    private fun formatClock(totalSeconds: Int): String {
        val mins = totalSeconds / 60
        val secs = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
    }

    private fun blendColor(from: Int, to: Int, ratio: Float): Int {
        return ColorUtils.blendARGB(from, to, ratio.coerceIn(0f, 1f))
    }

    private fun getOrCreateM3ProgressBadge(
        accentArgb: Int,
        secondaryArgb: Int,
        percent: Int,
        isPaused: Boolean,
        isBreak: Boolean,
        isDarkTheme: Boolean
    ): Bitmap {
        val key = BadgeCacheKey(accentArgb, secondaryArgb, percent, isPaused, isBreak, isDarkTheme)
        val cached = lastBadgeBitmap
        if (key == lastBadgeKey && cached != null && !cached.isRecycled) {
            return cached
        }
        val size = 128
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = size / 2f
        val cy = size / 2f

        // M3 tonal container circle
        val containerColor = if (isDarkTheme) {
            ColorUtils.blendARGB(0xFF1E1E24.toInt(), accentArgb, 0.24f)
        } else {
            ColorUtils.blendARGB(Color.WHITE, accentArgb, 0.16f)
        }
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = containerColor
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy, cx - 2f, fillPaint)

        // M3 progress track
        val ringInset = 13f
        val ringRect = RectF(ringInset, ringInset, size - ringInset, size - ringInset)
        val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ColorUtils.setAlphaComponent(accentArgb, 56)
            style = Paint.Style.STROKE
            strokeWidth = 10f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawArc(ringRect, 0f, 360f, false, trackPaint)

        // Active rounded M3 progress arc
        val sweep = ((percent / 100f) * 360f).coerceIn(8f, 360f)
        val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accentArgb
            style = Paint.Style.STROKE
            strokeWidth = 10f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawArc(ringRect, -90f, sweep, false, arcPaint)

        // Center content: crisp M3 pause bars or tabular percentage
        val inkColor = if (isDarkTheme) {
            ColorUtils.blendARGB(accentArgb, Color.WHITE, 0.65f)
        } else {
            ColorUtils.blendARGB(accentArgb, 0xFF1A1A1F.toInt(), 0.42f)
        }

        if (isPaused) {
            val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = inkColor
                style = Paint.Style.FILL
            }
            val barWidth = 11f
            val barHeight = 34f
            val gap = 10f
            val top = cy - barHeight / 2f
            val left1 = cx - gap / 2f - barWidth
            val left2 = cx + gap / 2f
            canvas.drawRoundRect(RectF(left1, top, left1 + barWidth, top + barHeight), 5.5f, 5.5f, barPaint)
            canvas.drawRoundRect(RectF(left2, top, left2 + barWidth, top + barHeight), 5.5f, 5.5f, barPaint)
        } else {
            val percentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = inkColor
                textSize = 31f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            }
            val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = ColorUtils.setAlphaComponent(inkColor, 185)
                textSize = 13f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                letterSpacing = 0.08f
            }
            canvas.drawText("$percent%", cx, cy + 5f, percentPaint)
            canvas.drawText(if (isBreak) "REST" else "FLOW", cx, cy + 23f, labelPaint)
        }

        lastBadgeKey = key
        lastBadgeBitmap = bitmap
        return bitmap
    }

    private fun createM3CompletionBadge(accentArgb: Int, isDarkTheme: Boolean): Bitmap {
        val size = 128
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = size / 2f
        val cy = size / 2f

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accentArgb
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy, cx - 4f, bgPaint)

        val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ColorUtils.setAlphaComponent(Color.WHITE, 90)
            style = Paint.Style.STROKE
            strokeWidth = 6f
        }
        canvas.drawCircle(cx, cy, cx - 14f, ringPaint)

        val checkColor = if (ColorUtils.calculateLuminance(accentArgb) > 0.55) 0xFF1A1A1F.toInt() else Color.WHITE
        val checkPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = checkColor
            style = Paint.Style.STROKE
            strokeWidth = 10f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
        val path = android.graphics.Path().apply {
            moveTo(cx - 22f, cy + 2f)
            lineTo(cx - 6f, cy + 18f)
            lineTo(cx + 24f, cy - 16f)
        }
        canvas.drawPath(path, checkPaint)
        return bitmap
    }

    private fun createOpenAppPendingIntent(context: Context, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createServicePendingIntent(
        context: Context,
        requestCode: Int,
        action: String
    ): PendingIntent {
        val intent = Intent(context, TimerService::class.java).apply {
            this.action = action
        }
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        return if (action == TimerService.ACTION_START && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(context, requestCode, intent, flags)
        } else {
            PendingIntent.getService(context, requestCode, intent, flags)
        }
    }
}
