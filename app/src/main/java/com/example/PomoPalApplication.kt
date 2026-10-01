package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.service.SettingsManager

class PomoPalApplication : Application() {
    companion object {
        private var instance: PomoPalApplication? = null

        fun getContext(): Context {
            return instance?.applicationContext 
                ?: throw IllegalStateException("PomoPalApplication not initialized yet.")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Initialize settings manager as early as possible
        SettingsManager.init(this)
        

        // Initialize "Somatic Alarms" Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "somatic_alarm_channel",
                "Somatic Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Somatic physical squat alarms"
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                enableVibration(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)

            // Initialize "Study Ritual Reminders" Notification Channel
            val studyChannel = NotificationChannel(
                com.example.service.StudyBlockReminderScheduler.CHANNEL_ID,
                "Study Ritual Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Mindful notifications when your scheduled study blocks are ready to begin"
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                enableVibration(true)
                setShowBadge(true)
            }
            manager?.createNotificationChannel(studyChannel)
        }
    }
}
