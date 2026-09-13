package com.example.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.accessibility.AccessibilityEvent
import com.example.ui.BlockedAppActivity

class AppBlockerService : AccessibilityService() {

    private var lastInterceptTimeMs = 0L
    private var lastInterceptedPackage: String? = null
    private var lastWatchdogCheckMs = 0L

    companion object {
        private val SYSTEM_EXCLUSIONS = setOf(
            "com.android.systemui",
            "android",
            "com.android.settings",
            "com.google.android.dialer",
            "com.android.phone",
            "com.android.server.telecom"
        )
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val eventType = event.eventType
        val isWindowState = eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        val isContentChanged = eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED

        if (!isWindowState && !isContentChanged) return

        val packageName = event.packageName?.toString() ?: return

        // Never intercept PomoPal itself or essential Android system UI / phone emergency packages
        if (packageName == applicationContext.packageName || SYSTEM_EXCLUSIONS.contains(packageName)) {
            return
        }

        val now = System.currentTimeMillis()

        // Content changed events occur extremely frequently during animations/typing;
        // throttle them as a secondary watchdog so CPU usage remains near zero.
        if (isContentChanged && !isWindowState) {
            if (now - lastWatchdogCheckMs < 750) {
                return
            }
            lastWatchdogCheckMs = now
        }

        // Verify that focus blocking is currently active (respects Focus Running and Strict Mode)
        if (!AppBlockerManager.isBlockingActive()) {
            return
        }

        // Check if the current foreground package is in the student's blocked apps list
        val blockedApps = SettingsManager.getBlockedApps()
        if (!blockedApps.contains(packageName)) {
            return
        }

        // Edge Case 1: If the blocking screen is already currently on top displaying this package, avoid restarting
        if (AppBlockerManager.isBlockingScreenShowing && packageName == AppBlockerManager.currentBlockedPackage) {
            return
        }

        // Edge Case 2: Debounce rapid-fire lifecycle transitions within the same app
        if (packageName == lastInterceptedPackage && (now - lastInterceptTimeMs < 600)) {
            return
        }

        lastInterceptedPackage = packageName
        lastInterceptTimeMs = now

        // Record attempt to empower student stats (e.g. "Resisted 5 distractions today")
        AppBlockerManager.recordBlockAttempt(applicationContext, packageName)

        // Subtle tactile alert if enabled
        if (AppBlockerManager.isVibrateEnabled(applicationContext)) {
            triggerGentleHaptic()
        }

        // Intercept: Display the premium, calming 5-second mindful pause screen
        try {
            val launchIntent = Intent(this, BlockedAppActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
                )
                putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, packageName)
            }
            startActivity(launchIntent)
        } catch (e: Exception) {
            // Safety net: In the rare case activity launch fails, push Home to protect focus
            performGlobalAction(GLOBAL_ACTION_HOME)
        }
    }

    private fun triggerGentleHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(60L)
            }
        } catch (e: Exception) {
            // Ignore if vibration fails or unsupported
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

