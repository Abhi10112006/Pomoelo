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
import android.os.Build
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.graphics.ColorUtils
import com.example.MainActivity
import com.example.R

/**
 * Posts clean, Material 3-styled proactive notifications for daily app usage limits:
 * - Halfway (50%) mindful check-in
 * - 5-minute final warning ("After 5 minutes this app will block — go study!")
 * - 1-minute wrap-up nudge
 * - "Just 5 mins more" live 5-minute countdown notification
 */
object AppLimitNotificationHelper {

    const val LIMIT_ALERTS_CHANNEL_ID = "PomoPalAppLimitAlertsChannel"
    private const val NOTIFICATION_BASE_ID = 4100

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            LIMIT_ALERTS_CHANNEL_ID,
            "App Limit Warnings & Nudges",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alerts you at 50% of your daily app limit, 5 minutes before an app locks, and during 5-minute grace passes."
            enableVibration(true)
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        manager.createNotificationChannel(channel)
    }

    fun notifyMilestone(
        context: Context,
        evaluation: AppUsageLimitManager.LimitEvaluation
    ) {
        if (evaluation.milestoneToNotify == AppUsageLimitManager.UsageMilestone.NONE) return
        createChannel(context)

        val appName = AppBlockerManager.getAppLabel(context, evaluation.packageName)
        val usedText = AppUsageLimitManager.formatDurationCompact(evaluation.usedMillis)
        val limitText = AppUsageLimitManager.formatMinutesLabel(evaluation.limitMinutes)
        val percent = ((evaluation.usedMillis.toFloat() / evaluation.limitMillis.coerceAtLeast(1L).toFloat()) * 100f)
            .toInt()
            .coerceIn(0, 100)

        val theme = SettingsManager.themeState.value
        val accentArgb: Int
        val subText: String
        val title: String
        val body: String
        val badgeLabel: String

        when (evaluation.milestoneToNotify) {
            AppUsageLimitManager.UsageMilestone.HALFWAY -> {
                accentArgb = theme.primary.toArgb()
                subText = "Daily App Limit • $percent% Used"
                title = "Halfway through your $appName limit"
                body = "You've used $appName for $usedText out of your $limitText daily limit (halfway there). Save some energy for studying!"
                badgeLabel = "50%"
            }
            AppUsageLimitManager.UsageMilestone.HALFWAY_AND_FIVE_MIN -> {
                accentArgb = 0xFFF57C00.toInt()
                subText = "5 Minutes Left • Halfway Reached"
                title = "$appName blocks in 5 minutes — go study!"
                body = "You've used half of your $limitText daily limit ($usedText). After 5 minutes $appName will lock so you can get back to studying."
                badgeLabel = "5m"
            }
            AppUsageLimitManager.UsageMilestone.FIVE_MIN_WARNING -> {
                accentArgb = 0xFFF57C00.toInt()
                subText = "5 Minutes Remaining • $usedText / $limitText"
                title = "After 5 minutes $appName will block — go study"
                body = "Only 5 minutes left on your $limitText daily limit for $appName. Start wrapping up and head back to your focus goal!"
                badgeLabel = "5m"
            }
            AppUsageLimitManager.UsageMilestone.ONE_MIN_WARNING -> {
                accentArgb = 0xFFE53935.toInt()
                subText = "1 Minute Left • Almost Locked"
                title = "1 minute left on $appName"
                body = "$appName will lock in about 1 minute. Finish up now and jump back into PomoPal!"
                badgeLabel = "1m"
            }
            AppUsageLimitManager.UsageMilestone.GRACE_ONE_MIN_WARNING -> {
                accentArgb = 0xFFE53935.toInt()
                subText = "Extra 5m Pass Ending"
                title = "1 minute left on your extra pass for $appName"
                body = "Your 5-minute grace pass is almost up — $appName will block shortly. Time to go study!"
                badgeLabel = "1m"
            }
            AppUsageLimitManager.UsageMilestone.NONE -> return
        }

        val badge = createTonalLimitBadge(
            accentArgb = accentArgb,
            percent = percent.coerceAtLeast(50),
            centerText = badgeLabel,
            isDark = theme.isDark
        )

        postNotification(
            context = context,
            notificationId = notificationIdFor(evaluation.packageName),
            subText = subText,
            title = title,
            body = body,
            accentArgb = accentArgb,
            largeIcon = badge,
            chronometerEndMs = null
        )
    }

    /**
     * Fires immediately when the user taps "Just 5 mins more" on the Daily Limit Blocking Screen,
     * showing a live 5-minute countdown reminder that the app will lock again after 5 minutes.
     */
    fun notifyFiveMinuteGraceStarted(
        context: Context,
        packageName: String,
        graceExpiryMs: Long,
        extensionsUsedToday: Int
    ) {
        createChannel(context)
        val appName = AppBlockerManager.getAppLabel(context, packageName)
        val theme = SettingsManager.themeState.value
        val accentArgb = 0xFFF57C00.toInt()
        val passNote = if (extensionsUsedToday > 1) " (Pass #$extensionsUsedToday today)" else ""
        val subText = "5-Min Grace Pass$passNote"
        val title = "After 5 minutes $appName will block — go study"
        val body = "You unlocked 5 extra minutes on $appName. Wrap up quickly — PomoPal will lock it automatically when the countdown hits zero."

        val badge = createTonalLimitBadge(
            accentArgb = accentArgb,
            percent = 100,
            centerText = "+5m",
            isDark = theme.isDark
        )

        postNotification(
            context = context,
            notificationId = notificationIdFor(packageName),
            subText = subText,
            title = title,
            body = body,
            accentArgb = accentArgb,
            largeIcon = badge,
            chronometerEndMs = graceExpiryMs
        )
    }

    private fun postNotification(
        context: Context,
        notificationId: Int,
        subText: String,
        title: String,
        body: String,
        accentArgb: Int,
        largeIcon: Bitmap,
        chronometerEndMs: Long?
    ) {
        try {
            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val openPendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                openAppIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, LIMIT_ALERTS_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_timer)
                .setLargeIcon(largeIcon)
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
                .setColor(accentArgb)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_notif_play, "Go Study", openPendingIntent)

            if (chronometerEndMs != null && chronometerEndMs > System.currentTimeMillis()) {
                builder
                    .setWhen(chronometerEndMs)
                    .setShowWhen(true)
                    .setUsesChronometer(true)
                    .setChronometerCountDown(true)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationId, builder.build())
        } catch (_: Exception) {}
    }

    private fun notificationIdFor(packageName: String): Int {
        return NOTIFICATION_BASE_ID + (packageName.hashCode() and 0x0FFF)
    }

    private fun createTonalLimitBadge(
        accentArgb: Int,
        percent: Int,
        centerText: String,
        isDark: Boolean
    ): Bitmap {
        val size = 128
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = size / 2f
        val cy = size / 2f

        val containerColor = if (isDark) {
            ColorUtils.blendARGB(0xFF1E1E24.toInt(), accentArgb, 0.24f)
        } else {
            ColorUtils.blendARGB(Color.WHITE, accentArgb, 0.16f)
        }
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = containerColor
            style = Paint.Style.FILL
        }
        canvas.drawCircle(cx, cy, cx - 2f, fillPaint)

        val inset = 13f
        val rect = RectF(inset, inset, size - inset, size - inset)
        val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ColorUtils.setAlphaComponent(accentArgb, 56)
            style = Paint.Style.STROKE
            strokeWidth = 10f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawArc(rect, 0f, 360f, false, trackPaint)

        val sweep = ((percent.coerceIn(0, 100) / 100f) * 360f).coerceIn(12f, 360f)
        val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accentArgb
            style = Paint.Style.STROKE
            strokeWidth = 10f
            strokeCap = Paint.Cap.ROUND
        }
        canvas.drawArc(rect, -90f, sweep, false, arcPaint)

        val inkColor = if (isDark) {
            ColorUtils.blendARGB(accentArgb, Color.WHITE, 0.65f)
        } else {
            ColorUtils.blendARGB(accentArgb, 0xFF1A1A1F.toInt(), 0.42f)
        }
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = inkColor
            textSize = 32f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
        canvas.drawText(centerText, cx, cy + 11f, textPaint)
        return bitmap
    }
}
