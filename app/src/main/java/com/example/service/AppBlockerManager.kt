package com.example.service

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppBlockerManager {
    private const val PREFS_NAME = "PomoPalAppBlockerPrefs"
    private const val KEY_BLOCK_COUNT_PREFIX = "block_count_"
    private const val KEY_STRICT_MODE = "app_blocker_strict_mode"
    private const val KEY_VIBRATE = "app_blocker_vibrate"
    private const val KEY_MOTIVATION_INDEX = "app_blocker_quote_index"

    @Volatile
    var isBlockingScreenShowing: Boolean = false

    @Volatile
    var currentBlockedPackage: String? = null

    // 56 uplifting, empathetic, and empowering student-focused motivations
    val studentMotivations = listOf(
        "Take a gentle breath. The urge to scroll will pass in sixty seconds.",
        "Your future self is depending on the choice you make right now.",
        "Deep focus is a superpower. Protect it like your most valuable treasure.",
        "Every minute you spend studying today builds the confidence you will feel tomorrow.",
        "Distraction promises instant novelty, but deep focus delivers lifelong mastery.",
        "You didn't come this far to trade your dreams for cheap dopamine.",
        "One focused problem, one page, one concept at a time. You've got this.",
        "A calm, undivided mind accomplishes in one hour what a distracted mind cannot do all day.",
        "Remember why you started. That ambition is worth far more than a fleeting scroll.",
        "The temptation to stop is strongest right before you make a genuine breakthrough.",
        "Discipline is simply choosing between what you want right now and what you want most.",
        "True mastery is forged in the quiet hours when nobody else is watching.",
        "Step back, take a breath, and let's conquer this study session together.",
        "Your focus is your signature. Make it something you are deeply proud of.",
        "Ten minutes of true immersion will dissolve the urge to procrastinate.",
        "Mastery is not an accident; it is the daily decision to stay the course.",
        "The notifications will always be there later. Your study time is happening right now.",
        "Small, consistent efforts repeated daily produce monumental transformations.",
        "Quiet the noise. The most important conversation is the one with your potential.",
        "You are stronger than an algorithm designed to capture your attention.",
        "Honor your commitments to yourself. Self-trust is built in moments like this.",
        "Close your eyes for three seconds, reset your mindset, and let's dive back in.",
        "The effort of studying is temporary; the pride of achievement lasts a lifetime.",
        "Great minds reflect on ideas; disciplined minds execute them with unbroken focus.",
        "Every distraction resisted is willpower earned. Your mental stamina is growing.",
        "Put down the screen, pick up your pencil. Your brilliance is waiting to unfold.",
        "You possess everything you need to master this material. Believe in yourself.",
        "Don't trade your life's ambition for fifteen seconds of mindless scrolling.",
        "Progress isn't always loud. Sometimes it's just quietly returning to your desk.",
        "Your peers might be resting or scrolling. This is where you create your edge.",
        "Breathe in clarity, exhale the distraction. You are in complete control.",
        "The best feeling in the world is finishing your day knowing you gave it your all.",
        "Momentum is precious. Guard your current focus block with fierce intention.",
        "Nothing truly great comes from effortless comfort. Embrace the learning challenge.",
        "You are creating the person you will be five years from now. Make them proud.",
        "The hardest part is staying seated. Once you restart, flow will naturally follow.",
        "Turn away from the chatter. Your life's masterpiece requires your full presence.",
        "When you feel like quitting, ask yourself: what would the master version of me do?",
        "A single focused hour can change the entire trajectory of your week.",
        "Give this topic the respect it deserves, and it will reward you with deep clarity.",
        "You have handled harder challenges than this. Draw confidence from your past wins.",
        "Focus is a muscle. You just did a mental repetition of discipline by pausing here.",
        "Let the messages wait. Your education and your future are your highest priorities.",
        "Calm persistence overcomes the steepest obstacles. One step at a time.",
        "Celebrate this moment: you caught yourself before losing your valuable time.",
        "Direct your energy inward. The wisdom you seek is in the work, not the feed.",
        "The secret to extraordinary results is ordinary work done with unshakeable consistency.",
        "Your attention is your most scarce resource. Invest it where it generates real value.",
        "Return to your study goal. You will be so grateful that you stayed true to yourself.",
        "Every expert was once a student who refused to look away from the challenge.",
        "Protect your peace, protect your time, and let your results do the talking.",
        "Breathe, smile, and remember: you are fully capable of doing great work today.",
        "One finished session is worth a thousand planned intentions. Let's finish strong.",
        "Stay patient. Deep understanding takes time to bloom. Keep watering the roots.",
        "You are just a few minutes away from the profound satisfaction of a completed study block.",
        "Be proud of your ambition, and honor it by giving your studies your finest effort."
    )

    enum class AccessibilityServiceStatus {
        DISABLED,
        READY,
        STALE,
        UNKNOWN
    }

    fun getAccessibilityStatus(context: Context): AccessibilityServiceStatus {
        try {
            val isEnabled = com.example.ui.isAccessibilityServiceEnabled(context)
            if (!isEnabled) {
                return AccessibilityServiceStatus.DISABLED
            }
            if (AppBlockerService.isServiceInterrupted) {
                return AccessibilityServiceStatus.STALE
            }
            if (AppBlockerService.isServiceConnected) {
                return AccessibilityServiceStatus.READY
            }
            return AccessibilityServiceStatus.STALE
        } catch (e: Exception) {
            return AccessibilityServiceStatus.UNKNOWN
        }
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun getTodayDateKey(): String {
        return SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
    }

    fun getFreshMotivation(context: Context): String {
        val prefs = getPrefs(context)
        val lastIdx = prefs.getInt(KEY_MOTIVATION_INDEX, -1)
        var nextIdx = (0 until studentMotivations.size).random()
        if (nextIdx == lastIdx && studentMotivations.size > 1) {
            nextIdx = (nextIdx + 1) % studentMotivations.size
        }
        prefs.edit().putInt(KEY_MOTIVATION_INDEX, nextIdx).apply()
        return studentMotivations[nextIdx]
    }

    fun recordBlockAttempt(context: Context, packageName: String): Int {
        val prefs = getPrefs(context)
        val todayKey = KEY_BLOCK_COUNT_PREFIX + getTodayDateKey()
        val current = prefs.getInt(todayKey, 0) + 1
        prefs.edit().putInt(todayKey, current).apply()
        return current
    }

    fun getBlockAttemptsToday(context: Context): Int {
        val prefs = getPrefs(context)
        val todayKey = KEY_BLOCK_COUNT_PREFIX + getTodayDateKey()
        return prefs.getInt(todayKey, 0)
    }

    fun isStrictMode(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_STRICT_MODE, false)
    }

    fun setStrictMode(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_STRICT_MODE, enabled).apply()
    }

    fun isVibrateEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_VIBRATE, true)
    }

    fun setVibrateEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_VIBRATE, enabled).apply()
    }

    fun isBlockingActive(): Boolean {
        val blockedApps = SettingsManager.getBlockedApps()
        if (blockedApps.isEmpty()) return false

        val state = TimerManager.timerState.value
        val isBreak = TimerManager.isBreakMode.value
        if (isBreak) return false

        val context = com.example.PomoPalApplication.getContext()
        return if (isStrictMode(context)) {
            state == TimerManager.TimerState.RUNNING || state == TimerManager.TimerState.PAUSED
        } else {
            state == TimerManager.TimerState.RUNNING
        }
    }

    fun getAppLabel(context: Context, packageName: String): String {
        return try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName.substringAfterLast('.').replaceFirstChar { it.uppercase() }
        }
    }
}
