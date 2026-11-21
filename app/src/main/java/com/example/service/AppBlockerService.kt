package com.example.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.accessibility.AccessibilityEvent
import android.view.inputmethod.InputMethodManager
import com.example.ui.BlockedAppActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AppBlockerService : AccessibilityService() {

    private var lastInterceptTimeMs = 0L
    private var lastInterceptedPackage: String? = null
    private var lastWatchdogCheckMs = 0L

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob() + exceptionHandler)
    private var limitMonitorJob: Job? = null

    @Volatile
    private var monitoredPackage: String? = null

    @Volatile
    private var monitoredPackageEnteredMs: Long = 0L

    @Volatile
    private var monitoredPackageBaselineUsageMs: Long = 0L

    companion object {
        private val SYSTEM_EXCLUSIONS = setOf(
            "com.android.systemui",
            "android",
            "com.android.settings",
            "com.google.android.dialer",
            "com.android.phone",
            "com.android.server.telecom",
            "com.google.android.permissioncontroller",
            "com.android.permissioncontroller"
        )

        @Volatile
        var isServiceConnected: Boolean = false
            private set

        @Volatile
        var isServiceInterrupted: Boolean = false
            private set
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val eventType = event.eventType
        val isWindowState = eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        val isContentChanged = eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED

        if (!isWindowState && !isContentChanged) return

        val packageName = event.packageName?.toString() ?: return

        // Ignore essential Android system UI, phone emergency packages, and active keyboards (IME)
        if (SYSTEM_EXCLUSIONS.contains(packageName) || isInputMethodPackage(packageName)) {
            return
        }

        // If user switched back to PomoPal, stop monitoring any external limited app
        if (packageName == applicationContext.packageName) {
            if (isWindowState) {
                stopLimitMonitor()
            }
            return
        }

        val now = System.currentTimeMillis()

        // Content changed events occur frequently during animations/typing; throttle them.
        if (isContentChanged && !isWindowState) {
            if (now - lastWatchdogCheckMs < 750) {
                return
            }
            lastWatchdogCheckMs = now
        }

        // 1. Focus Session Distraction Shield takes highest priority while a focus session is active
        if (AppBlockerManager.isBlockingActive()) {
            val blockedApps = SettingsManager.getBlockedApps()
            if (blockedApps.contains(packageName)) {
                stopLimitMonitor()
                if (shouldDebounceIntercept(packageName, now)) return
                launchFocusShieldBlock(packageName, now)
                return
            }
        }

        // 2. Daily App Usage Limits (4:00 AM – 4:00 AM cycle, active all day)
        val hasDailyLimit = AppUsageLimitManager.getAppLimitMinutes(applicationContext, packageName) != null
        if (hasDailyLimit) {
            if (monitoredPackage != packageName || limitMonitorJob?.isActive != true) {
                startLimitMonitor(packageName)
            } else if (isWindowState) {
                // Immediate check on returning to this limited app (e.g., after a 5-min grace pass)
                serviceScope.launch {
                    checkLimitedPackageOnce(packageName)
                }
            }
        } else if (isWindowState) {
            // User switched to a different unlimited app or launcher
            stopLimitMonitor()
        }
    }

    private fun startLimitMonitor(packageName: String) {
        stopLimitMonitor()
        monitoredPackage = packageName
        monitoredPackageEnteredMs = System.currentTimeMillis()
        monitoredPackageBaselineUsageMs = AppUsageLimitManager.getAppUsageMillisToday(applicationContext, packageName)

        limitMonitorJob = serviceScope.launch {
            while (isActive && monitoredPackage == packageName) {
                if (isDeviceScreenInteractive()) {
                    val blocked = checkLimitedPackageOnce(packageName)
                    if (blocked) {
                        // Stop polling once blocked; next window event or grace pass restarts monitor
                        break
                    }
                }
                delay(5_000L)
            }
        }
    }

    private fun stopLimitMonitor() {
        monitoredPackage = null
        monitoredPackageEnteredMs = 0L
        monitoredPackageBaselineUsageMs = 0L
        limitMonitorJob?.cancel()
        limitMonitorJob = null
    }

    private fun checkLimitedPackageOnce(packageName: String): Boolean {
        val now = System.currentTimeMillis()
        val liveSessionDeltaMs = if (monitoredPackage == packageName && monitoredPackageEnteredMs > 0L) {
            (now - monitoredPackageEnteredMs).coerceAtLeast(0L)
        } else {
            0L
        }
        val estimatedUsedMs = monitoredPackageBaselineUsageMs + liveSessionDeltaMs

        val eval = AppUsageLimitManager.evaluateAppLimit(
            context = applicationContext,
            packageName = packageName,
            extraForegroundFallbackMs = estimatedUsedMs
        ) ?: return false

        // Fire proactive M3 milestone nudge if crossed (50%, 5m left, 1m left, or 1m left in grace)
        if (eval.milestoneToNotify != AppUsageLimitManager.UsageMilestone.NONE) {
            AppLimitNotificationHelper.notifyMilestone(applicationContext, eval)
        }

        if (eval.isBlockedNow) {
            if (shouldDebounceIntercept(packageName, now)) return false
            AppUsageLimitManager.clearExpiredGrace(applicationContext, packageName)
            launchDailyLimitBlock(eval, now)
            return true
        }
        return false
    }

    private fun shouldDebounceIntercept(packageName: String, now: Long): Boolean {
        if (AppBlockerManager.isBlockingScreenShowing && packageName == AppBlockerManager.currentBlockedPackage) {
            return true
        }
        if (packageName == lastInterceptedPackage && (now - lastInterceptTimeMs < 600)) {
            return true
        }
        return false
    }

    private fun launchFocusShieldBlock(packageName: String, now: Long) {
        lastInterceptedPackage = packageName
        lastInterceptTimeMs = now

        AppBlockerManager.recordBlockAttempt(applicationContext, packageName)

        if (AppBlockerManager.isVibrateEnabled(applicationContext)) {
            triggerGentleHaptic()
        }

        try {
            val launchIntent = Intent(this, BlockedAppActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
                )
                putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, packageName)
                putExtra(BlockedAppActivity.EXTRA_BLOCK_MODE, BlockedAppActivity.MODE_FOCUS_SHIELD)
            }
            startActivity(launchIntent)
        } catch (_: Exception) {
            performGlobalAction(GLOBAL_ACTION_HOME)
        }
    }

    private fun launchDailyLimitBlock(eval: AppUsageLimitManager.LimitEvaluation, now: Long) {
        lastInterceptedPackage = eval.packageName
        lastInterceptTimeMs = now

        AppBlockerManager.recordBlockAttempt(applicationContext, eval.packageName)

        if (AppBlockerManager.isVibrateEnabled(applicationContext)) {
            triggerGentleHaptic()
        }

        try {
            val launchIntent = Intent(this, BlockedAppActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
                )
                putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, eval.packageName)
                putExtra(BlockedAppActivity.EXTRA_BLOCK_MODE, BlockedAppActivity.MODE_DAILY_LIMIT)
                putExtra(BlockedAppActivity.EXTRA_USED_MILLIS, eval.usedMillis)
                putExtra(BlockedAppActivity.EXTRA_LIMIT_MINUTES, eval.limitMinutes)
                putExtra(BlockedAppActivity.EXTRA_EXTENSIONS_USED, eval.extensionsUsedToday)
            }
            startActivity(launchIntent)
        } catch (_: Exception) {
            performGlobalAction(GLOBAL_ACTION_HOME)
        }
    }

    private fun isDeviceScreenInteractive(): Boolean {
        return try {
            val pm = getSystemService(Context.POWER_SERVICE) as? PowerManager
            pm?.isInteractive ?: true
        } catch (_: Exception) {
            true
        }
    }

    private fun isInputMethodPackage(packageName: String): Boolean {
        return try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.enabledInputMethodList?.any { it.packageName == packageName } == true
        } catch (_: Exception) {
            false
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
        } catch (_: Exception) {}
    }

    override fun onInterrupt() {
        isServiceInterrupted = true
        stopLimitMonitor()
    }

    override fun onServiceConnected() {
        isServiceConnected = true
        isServiceInterrupted = false
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS
        }
        serviceInfo = info
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isServiceConnected = false
        isServiceInterrupted = false
        stopLimitMonitor()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLimitMonitor()
        serviceScope.cancel()
    }
}