package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.service.InactivityReceiver
import com.example.service.SettingsManager
import com.example.service.TimerService

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
        try {
            SettingsManager.init(this)
        } catch (_: Exception) {}
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val manager = getSystemService(NotificationManager::class.java) ?: return

                val progressChannel = NotificationChannel(
                    TimerService.CHANNEL_ID,
                    "Active Focus Timer",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Displays real-time countdown and controls during active focus and break sessions."
                    setShowBadge(false)
                }

                val completionChannel = NotificationChannel(
                    TimerService.COMPLETION_CHANNEL_ID,
                    "Session Completion Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifies you when a focus or break session finishes."
                    enableVibration(true)
                    setShowBadge(true)
                }

                val reminderChannel = NotificationChannel(
                    InactivityReceiver.CHANNEL_ID,
                    "Mindful Focus Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Gentle check-ins to help you return to your focus sessions."
                    enableVibration(true)
                    setShowBadge(true)
                }

                manager.createNotificationChannels(
                    listOf(progressChannel, completionChannel, reminderChannel)
                )
            } catch (_: Exception) {}
        }
    }
}

