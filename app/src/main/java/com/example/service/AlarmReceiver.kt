package com.example.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.data.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 4004
        
        fun dismissNotification(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.action.ALARM_TRIGGERED") {
            val alarmId = intent.getIntExtra("ALARM_ID", -1)
            val squatTarget = intent.getIntExtra("SQUAT_TARGET", 10)
            val label = intent.getStringExtra("LABEL") ?: "Workout Alarm"

            // Set global ringing state
            AlarmState.isAlarmRinging = true
            AlarmState.activeAlarmId = alarmId
            AlarmState.activeAlarmSquatTarget = squatTarget
            AlarmState.activeAlarmLabel = label
            AlarmState.activeAlarmSquatCount = 0

            // Play blaring continuous max-volume sound
            SoundPlayer.startContinuousAlarm(context.applicationContext)

            // Build high-priority persistent notification
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Create full-screen PendingIntent pointing to MainActivity
            val fullScreenIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = "com.example.action.ALARM_TRIGGERED"
                putExtra("ALARM_ID", alarmId)
            }
            
            val pendingIntent = PendingIntent.getActivity(
                context,
                alarmId,
                fullScreenIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )

            val notificationBuilder = NotificationCompat.Builder(context, "somatic_alarm_channel")
                .setSmallIcon(com.example.R.drawable.ic_notification_timer)
                .setContentTitle("Wake Up & Workout!")
                .setContentText("Time for your morning squats.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setOngoing(true)
                .setAutoCancel(false)
                .setFullScreenIntent(pendingIntent, true)
                .setContentIntent(pendingIntent)

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

            // Launch MainActivity dynamically
            context.startActivity(fullScreenIntent)

            // Reschedule the repeating alarm for its next frequency checkpoint
            if (alarmId != -1) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = DatabaseProvider.getDatabase(context.applicationContext)
                        val alarm = db.alarmDao().getAlarmById(alarmId)
                        if (alarm != null && alarm.isEnabled) {
                            AlarmScheduler.scheduleAlarm(context.applicationContext, alarm)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
