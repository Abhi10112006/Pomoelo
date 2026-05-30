package com.example.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.data.AlarmItem
import com.example.data.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

object AlarmScheduler {

    fun scheduleAlarm(context: Context, alarm: AlarmItem) {
        if (!alarm.isEnabled) {
            cancelAlarm(context, alarm.id)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val triggerTime = getNextTriggerTime(alarm.hour, alarm.minute, alarm.daysOfWeek)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.example.action.ALARM_TRIGGERED"
            putExtra("ALARM_ID", alarm.id)
            putExtra("SQUAT_TARGET", alarm.squatTarget)
            putExtra("LABEL", alarm.label)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelAlarm(context: Context, alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.example.action.ALARM_TRIGGERED"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun rescheduleAll(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = DatabaseProvider.getDatabase(context)
                val allAlarms = db.alarmDao().getAllAlarms().first()
                for (alarm in allAlarms) {
                    if (alarm.isEnabled) {
                        scheduleAlarm(context, alarm)
                    } else {
                        cancelAlarm(context, alarm.id)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getNextTriggerTime(hour: Int, minute: Int, daysOfWeek: String): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (daysOfWeek == "Daily" || daysOfWeek.isBlank()) {
            if (target.before(now)) {
                target.add(Calendar.DAY_OF_YEAR, 1)
            }
            return target.timeInMillis
        }

        // Specific days of week represented as comma-separated integers, e.g. "2,3,4,5,6" (Monday to Friday)
        // Values are Calendar day of week integers: Sunday=1, Monday=2, ..., Saturday=7
        val daysList = daysOfWeek.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .sorted()

        if (daysList.isEmpty()) {
            if (target.before(now)) {
                target.add(Calendar.DAY_OF_YEAR, 1)
            }
            return target.timeInMillis
        }

        val currentDayOfWeek = now.get(Calendar.DAY_OF_WEEK)

        // Find the next day in the list starting from today inclusive
        var targetDayOfWeek = -1
        var daysToAdd = -1

        for (day in daysList) {
            if (day == currentDayOfWeek) {
                // If the time is later today, select today!
                if (target.after(now)) {
                    targetDayOfWeek = day
                    daysToAdd = 0
                    break
                }
            } else if (day > currentDayOfWeek) {
                targetDayOfWeek = day
                daysToAdd = day - currentDayOfWeek
                break
            }
        }

        // If not found in the rest of the week, take the first day of next week
        if (targetDayOfWeek == -1) {
            val firstDay = daysList.first()
            targetDayOfWeek = firstDay
            daysToAdd = (7 - currentDayOfWeek) + firstDay
        }

        target.add(Calendar.DAY_OF_YEAR, daysToAdd)
        return target.timeInMillis
    }
}
