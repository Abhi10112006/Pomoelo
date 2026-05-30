package com.example.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.MainActivity
import com.example.data.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
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

            // Launch MainActivity dynamically
            val launchIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = "com.example.action.ALARM_TRIGGERED"
                putExtra("ALARM_ID", alarmId)
            }
            context.startActivity(launchIntent)

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
