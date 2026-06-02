package com.example.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import java.util.Random

class InactivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_INACTIVITY) {
            // Check if timer is currently running - if so, do not bother the user!
            if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
                return
            }

            // Do not send notifications between 12 AM and 5 AM
            val hourOfDay = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
            if (hourOfDay in 0..4) {
                InactivityScheduler.schedule(context)
                return
            }

            // Post notification
            showInactivityNotification(context)
            
            // Auto schedule the next check for a fresh reminder (not repeated)
            InactivityScheduler.schedule(context)
        }
    }

    private fun showInactivityNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Ensure channel exists
        val channelId = "inactivity_reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Inactivity Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Friendly anti-scrolling and focus nudges"
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
            manager.createNotificationChannel(channel)
        }

        // Get a fresh quote
        val message = getFreshInactivityQuote(context)

        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            101,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("🎯 Focus Reminder")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setColor(android.graphics.Color.parseColor("#FF8A80"))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(NOTIFICATION_ID_INACTIVITY, notification)
    }

    companion object {
        const val ACTION_INACTIVITY = "com.example.action.INACTIVITY_ALERT"
        const val NOTIFICATION_ID_INACTIVITY = 4040

        fun getFreshInactivityQuote(context: Context): String {
            return QuoteManager.getFreshQuote(context, false, "inactivity_quote_index")
        }
    }
}

object InactivityScheduler {
    
    // Default threshold is 30 minutes
    private const val INACTIVITY_DELAY_MS = 30 * 60 * 1000L

    fun schedule(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, InactivityReceiver::class.java).apply {
                action = InactivityReceiver.ACTION_INACTIVITY
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                202,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerAt = System.currentTimeMillis() + INACTIVITY_DELAY_MS

            // Cancel previous if any
            try {
                alarmManager.cancel(pendingIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )
                }
            } catch (e: SecurityException) {
                // Fallback for missing exact alarm permission on Android 12+ / 14+
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancel(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, InactivityReceiver::class.java).apply {
                action = InactivityReceiver.ACTION_INACTIVITY
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                202,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
