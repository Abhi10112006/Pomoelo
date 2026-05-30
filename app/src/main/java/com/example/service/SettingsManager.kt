package com.example.service

import android.content.Context
import android.content.SharedPreferences

object SettingsManager {
    private const val PREFS_NAME = "PomoPalSettings"
    private const val KEY_FOCUS_TIME = "focus_time_mins"
    private const val KEY_BREAK_TIME = "break_time_mins"
    private const val KEY_COMPLETION_SOUND = "completion_sound"
    private const val KEY_COMPLETION_DURATION = "completion_duration_secs"
    private const val KEY_SQUAT_TARGET = "squat_target"
    private const val KEY_SQUAT_ALARM_ENABLED = "squat_alarm_enabled"
    private const val KEY_BACKGROUND_SOUND = "bg_sound"

    private const val KEY_CUSTOM_COMPLETION = "custom_completion_uri"
    private const val KEY_CUSTOM_BACKGROUND = "custom_background_uri"

    @Volatile
    private var prefs: SharedPreferences? = null

    private fun getPrefs(): SharedPreferences {
        return prefs ?: synchronized(this) {
            prefs ?: try {
                val context = com.example.PomoPalApplication.getContext()
                val shared = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs = shared
                shared
            } catch (e: Exception) {
                throw IllegalStateException("SettingsManager could not be initialized on-demand because application context is not ready.", e)
            }
        }
    }

    fun init(context: Context) {
        synchronized(this) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
        // Load into TimerManager
        TimerManager.setFocusTimeMins(getFocusTimeMins())
        TimerManager.setBreakTimeMins(getBreakTimeMins())
    }

    fun getSquatTarget(): Int = getPrefs().getInt(KEY_SQUAT_TARGET, 0)
    fun setSquatTarget(count: Int) = getPrefs().edit().putInt(KEY_SQUAT_TARGET, count).apply()

    fun isSquatAlarmEnabled(): Boolean = getPrefs().getBoolean(KEY_SQUAT_ALARM_ENABLED, false)
    fun setSquatAlarmEnabled(enabled: Boolean) = getPrefs().edit().putBoolean(KEY_SQUAT_ALARM_ENABLED, enabled).apply()

    fun getCompletionSound(): Int = getPrefs().getInt(KEY_COMPLETION_SOUND, 0)
    fun setCompletionSound(type: Int) = getPrefs().edit().putInt(KEY_COMPLETION_SOUND, type).apply()

    fun getCompletionDurationSec(): Int = getPrefs().getInt(KEY_COMPLETION_DURATION, 5)
    fun setCompletionDurationSec(sec: Int) = getPrefs().edit().putInt(KEY_COMPLETION_DURATION, sec).apply()

    fun getCustomCompletionUri(): String? = getPrefs().getString(KEY_CUSTOM_COMPLETION, null)
    fun setCustomCompletionUri(uri: String?) = getPrefs().edit().putString(KEY_CUSTOM_COMPLETION, uri).apply()

    fun getBackgroundSound(): Int = getPrefs().getInt(KEY_BACKGROUND_SOUND, 0)
    fun setBackgroundSound(type: Int) = getPrefs().edit().putInt(KEY_BACKGROUND_SOUND, type).apply()

    fun getCustomBackgroundUri(): String? = getPrefs().getString(KEY_CUSTOM_BACKGROUND, null)
    fun setCustomBackgroundUri(uri: String?) = getPrefs().edit().putString(KEY_CUSTOM_BACKGROUND, uri).apply()

    fun getFocusTimeMins(): Int = getPrefs().getInt(KEY_FOCUS_TIME, 25)
    fun getBreakTimeMins(): Int = getPrefs().getInt(KEY_BREAK_TIME, 5)

    fun setFocusTimeMins(mins: Int) {
        getPrefs().edit().putInt(KEY_FOCUS_TIME, mins).apply()
        TimerManager.setFocusTimeMins(mins)
    }

    fun setBreakTimeMins(mins: Int) {
        getPrefs().edit().putInt(KEY_BREAK_TIME, mins).apply()
        TimerManager.setBreakTimeMins(mins)
    }
}
