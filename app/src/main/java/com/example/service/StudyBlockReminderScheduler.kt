package com.example.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.DatabaseProvider
import com.example.data.StudyBlock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

object StudyBlockReminderScheduler {
    const val CHANNEL_ID = "study_block_reminder_channel"
    const val ACTION_REMINDER = "com.example.action.STUDY_BLOCK_REMINDER"
    const val ACTION_START_STUDY = "com.example.action.START_STUDY_BLOCK"

    private const val TAG = "StudyBlockScheduler"

    fun getNotificationId(studyBlockId: Long): Int {
        return ((studyBlockId.hashCode() and 0x7FFFFFFF) % 500000) + 100000
    }

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java) ?: return
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Study Ritual Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Mindful notifications when your scheduled study blocks are ready to begin"
                    enableVibration(true)
                    setShowBadge(true)
                    lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    fun schedule(context: Context, studyBlock: StudyBlock) {
        ensureChannel(context)

        // Always cancel any existing schedule first to prevent duplicate notifications
        cancel(context, studyBlock.id)

        if (!studyBlock.reminderEnabled) {
            Log.d(TAG, "Reminder is disabled for block id=${studyBlock.id}; skipped scheduling.")
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val nextTriggerTime = calculateNextTriggerTime(studyBlock.startTime, studyBlock.repeatRule)

        val intent = Intent(context, StudyBlockReminderReceiver::class.java).apply {
            action = ACTION_REMINDER
            putExtra("STUDY_BLOCK_ID", studyBlock.id)
            putExtra("STUDY_BLOCK_NAME", studyBlock.name)
            putExtra("STUDY_BLOCK_DESCRIPTION", studyBlock.description)
            putExtra("STUDY_BLOCK_COLOR", studyBlock.color)
            putExtra("STUDY_BLOCK_DURATION", studyBlock.duration)
            putExtra("STUDY_BLOCK_REPEAT", studyBlock.repeatRule ?: "None")
        }

        val requestCode = getNotificationId(studyBlock.id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerTime, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerTime, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTriggerTime, pendingIntent)
            }
            Log.d(TAG, "Successfully scheduled reminder for '${studyBlock.name}' at $nextTriggerTime (id=${studyBlock.id})")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to schedule reminder for block id=${studyBlock.id}", e)
        }
    }

    fun cancel(context: Context, studyBlockId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val requestCode = getNotificationId(studyBlockId)

        val intent = Intent(context, StudyBlockReminderReceiver::class.java).apply {
            action = ACTION_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Cancelled alarm for block id=$studyBlockId")
        }

        // Also dismiss any active notification for this block
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.cancel(requestCode)
    }

    fun rescheduleAll(context: Context) {
        ensureChannel(context)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = DatabaseProvider.getDatabase(context)
                val allBlocks = db.studyBlockDao().getAllStudyBlocksList()
                for (block in allBlocks) {
                    if (block.reminderEnabled) {
                        schedule(context, block)
                    } else {
                        cancel(context, block.id)
                    }
                }
                Log.d(TAG, "Rescheduled all study block reminders (count=${allBlocks.size})")
            } catch (e: Exception) {
                Log.e(TAG, "Error rescheduling study block reminders", e)
            }
        }
    }

    fun calculateNextTriggerTime(startTime: Long, repeatRule: String?): Long {
        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance()

        val scheduledHour: Int
        val scheduledMinute: Int
        val sourceDayOfWeek: Int

        if (startTime > 0L) {
            val sourceCal = Calendar.getInstance().apply { timeInMillis = startTime }
            scheduledHour = sourceCal.get(Calendar.HOUR_OF_DAY)
            scheduledMinute = sourceCal.get(Calendar.MINUTE)
            sourceDayOfWeek = sourceCal.get(Calendar.DAY_OF_WEEK)
        } else {
            scheduledHour = 9
            scheduledMinute = 0
            sourceDayOfWeek = Calendar.MONDAY
        }

        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, scheduledHour)
            set(Calendar.MINUTE, scheduledMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        when (repeatRule) {
            "Daily" -> {
                if (target.timeInMillis <= now) {
                    target.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            "Weekdays" -> {
                if (target.timeInMillis <= now) {
                    target.add(Calendar.DAY_OF_YEAR, 1)
                }
                while (target.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                    target.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                ) {
                    target.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            "Weekly" -> {
                while (target.get(Calendar.DAY_OF_WEEK) != sourceDayOfWeek || target.timeInMillis <= now) {
                    target.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            else -> {
                // "Once" or "None"
                if (startTime > now) {
                    return startTime
                }
                if (target.timeInMillis <= now) {
                    target.add(Calendar.DAY_OF_YEAR, 1)
                }
            }
        }

        return target.timeInMillis
    }
}
