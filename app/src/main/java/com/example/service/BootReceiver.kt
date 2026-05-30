package com.example.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            // Check if timer is running (or supposed to be) - normally it's cleared on boot/kill.
            // If not running, reschedule the friendly study / inactivity reminder!
            if (TimerManager.timerState.value != TimerManager.TimerState.RUNNING) {
                InactivityScheduler.schedule(context.applicationContext)
            }
            // Reschedule all active alarms
            AlarmScheduler.rescheduleAll(context.applicationContext)
        }
    }
}
