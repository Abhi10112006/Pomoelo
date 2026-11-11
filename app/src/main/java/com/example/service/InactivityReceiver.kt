package com.example.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class InactivityReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "InactivityReminderChannel"
        const val NOTIFICATION_ID = 404
        const val ACTION_INACTIVITY_ALERT = "com.example.action.INACTIVITY_ALERT"
    }

    override fun onReceive(context: Context, intent: Intent) {
        SettingsManager.init(context.applicationContext)

        if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
            return
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        ensureChannel(notificationManager)

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val tapPendingIntent = PendingIntent.getActivity(
            context,
            404,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val userName = SettingsManager.getUserName()?.takeIf { it.isNotBlank() }
        val currentTask = TimerManager.currentTaskName.value.takeIf {
            it.isNotBlank() && it != "Focus Time!" && it != "Break Time!"
        }
        val focusMins = SettingsManager.getFocusTimeMins()

        val title = when {
            currentTask != null -> "Ready to resume $currentTask?"
            userName != null -> "A quiet moment for focus, $userName"
            else -> "Ready for a mindful focus session?"
        }

        val body = when {
            currentTask != null -> "Step into your focus sanctuary for a ${focusMins}-minute session on $currentTask."
            else -> "Even one ${focusMins}-minute session builds calm, steady momentum for your day."
        }

        val accentColor = try {
            SettingsManager.themeState.value.primary.toArgb()
        } catch (_: Throwable) {
            0xFFF28F75.toInt()
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setSubText("Mindful Check-In")
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(body)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(accentColor)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .addAction(0, "Open PomoPal", tapPendingIntent)
            .build()

        try {
            notificationManager.notify(NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {
        } catch (_: Exception) {}
    }

    private fun ensureChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Mindful Focus Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Gentle check-ins to help you return to your focus sessions."
                enableVibration(true)
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}

object InactivityScheduler {
    private const val REQUEST_CODE = 404
    private const val INACTIVITY_DELAY_MS = 2 * 60 * 60 * 1000L // 2 hours
    private const val WINDOW_LENGTH_MS = 15 * 60 * 1000L // 15-minute delivery window

    fun schedule(context: Context) {
        try {
            val appContext = context.applicationContext
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(appContext, InactivityReceiver::class.java).apply {
                action = InactivityReceiver.ACTION_INACTIVITY_ALERT
            }
            val pendingIntent = PendingIntent.getBroadcast(
                appContext,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.cancel(pendingIntent)

            val triggerTime = System.currentTimeMillis() + INACTIVITY_DELAY_MS
            alarmManager.setWindow(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                WINDOW_LENGTH_MS,
                pendingIntent
            )
        } catch (_: Exception) {}
    }

    fun cancel(context: Context) {
        try {
            val appContext = context.applicationContext
            val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(appContext, InactivityReceiver::class.java).apply {
                action = InactivityReceiver.ACTION_INACTIVITY_ALERT
            }
            val pendingIntent = PendingIntent.getBroadcast(
                appContext,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        } catch (_: Exception) {}
    }
}
