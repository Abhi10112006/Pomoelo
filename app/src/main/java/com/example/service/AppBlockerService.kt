package com.example.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.MainActivity

class AppBlockerService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || 
            event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            // Check if timer is running and if it's focus mode
            val isRunning = TimerManager.timerState.value == TimerManager.TimerState.RUNNING
            val isFocusMode = !TimerManager.isBreakMode.value

            if (isRunning && isFocusMode) {
                val blockedApps = SettingsManager.getBlockedApps()
                if (blockedApps.contains(packageName) && packageName != applicationContext.packageName) {
                    // Block the app by launching our MainActivity overlay
                    val launchIntent = Intent(this, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        putExtra("BLOCKED_APP", packageName)
                    }
                    startActivity(launchIntent)
                }
            }
        }
    }

    override fun onInterrupt() {
        // No action needed
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
        }
        serviceInfo = info
    }
}
