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

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Load into TimerManager
        TimerManager.setFocusTimeMins(getFocusTimeMins())
        TimerManager.setBreakTimeMins(getBreakTimeMins())
    }

    fun getSquatTarget(): Int = prefs.getInt(KEY_SQUAT_TARGET, 0)
    fun setSquatTarget(count: Int) = prefs.edit().putInt(KEY_SQUAT_TARGET, count).apply()

    fun isSquatAlarmEnabled(): Boolean = prefs.getBoolean(KEY_SQUAT_ALARM_ENABLED, false)
    fun setSquatAlarmEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_SQUAT_ALARM_ENABLED, enabled).apply()

    fun getCompletionSound(): Int = prefs.getInt(KEY_COMPLETION_SOUND, 0)
    fun setCompletionSound(type: Int) = prefs.edit().putInt(KEY_COMPLETION_SOUND, type).apply()

    fun getCompletionDurationSec(): Int = prefs.getInt(KEY_COMPLETION_DURATION, 5)
    fun setCompletionDurationSec(sec: Int) = prefs.edit().putInt(KEY_COMPLETION_DURATION, sec).apply()

    fun getCustomCompletionUri(): String? = prefs.getString(KEY_CUSTOM_COMPLETION, null)
    fun setCustomCompletionUri(uri: String?) = prefs.edit().putString(KEY_CUSTOM_COMPLETION, uri).apply()

    fun getBackgroundSound(): Int = prefs.getInt(KEY_BACKGROUND_SOUND, 0)
    fun setBackgroundSound(type: Int) = prefs.edit().putInt(KEY_BACKGROUND_SOUND, type).apply()

    fun getCustomBackgroundUri(): String? = prefs.getString(KEY_CUSTOM_BACKGROUND, null)
    fun setCustomBackgroundUri(uri: String?) = prefs.edit().putString(KEY_CUSTOM_BACKGROUND, uri).apply()

    fun getFocusTimeMins(): Int = prefs.getInt(KEY_FOCUS_TIME, 25)
    fun getBreakTimeMins(): Int = prefs.getInt(KEY_BREAK_TIME, 5)

    fun setFocusTimeMins(mins: Int) {
        prefs.edit().putInt(KEY_FOCUS_TIME, mins).apply()
        TimerManager.setFocusTimeMins(mins)
    }

    fun setBreakTimeMins(mins: Int) {
        prefs.edit().putInt(KEY_BREAK_TIME, mins).apply()
        TimerManager.setBreakTimeMins(mins)
    }
}
