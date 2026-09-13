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
    private const val KEY_BLOCKED_APPS = "blocked_apps_set"

    private const val KEY_USER_NAME = "user_name"
    private const val KEY_LAST_BACKUP_TIME = "last_backup_time"
    private const val KEY_AUTO_BACKUP_FREQ = "auto_backup_freq"

    private const val KEY_SQUAT_VALLEY_THRESHOLD = "user_squat_valley_threshold"
    private const val KEY_SQUAT_PEAK_THRESHOLD = "user_squat_peak_threshold"

    private const val KEY_THEME_ID = "selected_app_theme"
    private const val KEY_FONT_ID = "selected_app_font"

    private const val KEY_CUSTOM_BG_COLOR = "custom_bg_color"
    private const val KEY_CUSTOM_PRIMARY_COLOR = "custom_primary_color"
    private const val KEY_CUSTOM_SECONDARY_COLOR = "custom_secondary_color"
    private const val KEY_CUSTOM_ACCENT_COLOR = "custom_accent_color"
    private const val KEY_APPEARANCE_MODE = "appearance_mode"

    enum class AppearanceMode(val id: String) {
        LIGHT("light"),
        DARK("dark"),
        SYSTEM("system");

        companion object {
            fun fromId(id: String?): AppearanceMode = entries.firstOrNull { it.id == id } ?: SYSTEM
        }
    }

    private val _appearanceState = kotlinx.coroutines.flow.MutableStateFlow(AppearanceMode.SYSTEM)
    val appearanceState: kotlinx.coroutines.flow.StateFlow<AppearanceMode> = _appearanceState

    private val _themeState = kotlinx.coroutines.flow.MutableStateFlow(com.example.ui.theme.ThemeOption.PREMIUM)
    val themeState: kotlinx.coroutines.flow.StateFlow<com.example.ui.theme.ThemeOption> = _themeState

    private val _fontState = kotlinx.coroutines.flow.MutableStateFlow(com.example.ui.theme.FontOption.COMIC_NEUE)
    val fontState: kotlinx.coroutines.flow.StateFlow<com.example.ui.theme.FontOption> = _fontState

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

        _appearanceState.value = getAppearanceMode()
        updateThemeState()
        _fontState.value = com.example.ui.theme.FontOption.fromId(getFontId())
    }

    fun getSquatValleyThreshold(): Float? = if (getPrefs().contains(KEY_SQUAT_VALLEY_THRESHOLD)) getPrefs().getFloat(KEY_SQUAT_VALLEY_THRESHOLD, -0.4f) else null
    fun setSquatValleyThreshold(value: Float) = getPrefs().edit().putFloat(KEY_SQUAT_VALLEY_THRESHOLD, value).apply()

    fun getSquatPeakThreshold(): Float? = if (getPrefs().contains(KEY_SQUAT_PEAK_THRESHOLD)) getPrefs().getFloat(KEY_SQUAT_PEAK_THRESHOLD, 0.4f) else null
    fun setSquatPeakThreshold(value: Float) = getPrefs().edit().putFloat(KEY_SQUAT_PEAK_THRESHOLD, value).apply()

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

    fun getBlockedApps(): Set<String> {
        return getPrefs().getStringSet(KEY_BLOCKED_APPS, emptySet()) ?: emptySet()
    }

    fun setBlockedApps(apps: Set<String>) {
        getPrefs().edit().putStringSet(KEY_BLOCKED_APPS, apps).apply()
    }
    fun getUserName(): String? = getPrefs().getString(KEY_USER_NAME, null)
    fun setUserName(name: String?) = getPrefs().edit().putString(KEY_USER_NAME, name).apply()
    fun getLastBackupTime(): Long = getPrefs().getLong(KEY_LAST_BACKUP_TIME, 0L)
    fun setLastBackupTime(timeMs: Long) = getPrefs().edit().putLong(KEY_LAST_BACKUP_TIME, timeMs).apply()
    fun getAutoBackupFreq(): Int = getPrefs().getInt(KEY_AUTO_BACKUP_FREQ, 0)
    fun setAutoBackupFreq(freq: Int) = getPrefs().edit().putInt(KEY_AUTO_BACKUP_FREQ, freq).apply()

    fun getThemeId(): String = getPrefs().getString(KEY_THEME_ID, "premium") ?: "premium"
    fun setThemeId(themeId: String) {
        getPrefs().edit().putString(KEY_THEME_ID, themeId).apply()
        updateThemeState()
    }

    fun getCustomBgColor(): Long? = if (getPrefs().contains(KEY_CUSTOM_BG_COLOR)) getPrefs().getLong(KEY_CUSTOM_BG_COLOR, 0L) else null
    fun setCustomBgColor(color: Long?) {
        if (color == null) getPrefs().edit().remove(KEY_CUSTOM_BG_COLOR).apply()
        else getPrefs().edit().putLong(KEY_CUSTOM_BG_COLOR, color).apply()
        updateThemeState()
    }

    fun getCustomPrimaryColor(): Long? = if (getPrefs().contains(KEY_CUSTOM_PRIMARY_COLOR)) getPrefs().getLong(KEY_CUSTOM_PRIMARY_COLOR, 0L) else null
    fun setCustomPrimaryColor(color: Long?) {
        if (color == null) getPrefs().edit().remove(KEY_CUSTOM_PRIMARY_COLOR).apply()
        else getPrefs().edit().putLong(KEY_CUSTOM_PRIMARY_COLOR, color).apply()
        updateThemeState()
    }

    fun getCustomSecondaryColor(): Long? = if (getPrefs().contains(KEY_CUSTOM_SECONDARY_COLOR)) getPrefs().getLong(KEY_CUSTOM_SECONDARY_COLOR, 0L) else null
    fun setCustomSecondaryColor(color: Long?) {
        if (color == null) getPrefs().edit().remove(KEY_CUSTOM_SECONDARY_COLOR).apply()
        else getPrefs().edit().putLong(KEY_CUSTOM_SECONDARY_COLOR, color).apply()
        updateThemeState()
    }

    fun getCustomAccentColor(): Long? = if (getPrefs().contains(KEY_CUSTOM_ACCENT_COLOR)) getPrefs().getLong(KEY_CUSTOM_ACCENT_COLOR, 0L) else null
    fun setCustomAccentColor(color: Long?) {
        if (color == null) getPrefs().edit().remove(KEY_CUSTOM_ACCENT_COLOR).apply()
        else getPrefs().edit().putLong(KEY_CUSTOM_ACCENT_COLOR, color).apply()
        updateThemeState()
    }

    fun getAppearanceMode(): AppearanceMode = AppearanceMode.fromId(getPrefs().getString(KEY_APPEARANCE_MODE, AppearanceMode.SYSTEM.id))
    fun setAppearanceMode(mode: AppearanceMode) {
        getPrefs().edit().putString(KEY_APPEARANCE_MODE, mode.id).apply()
        _appearanceState.value = mode
        updateThemeState()
    }

    fun updateThemeState() {
        val baseTheme = com.example.ui.theme.ThemeOption.PREMIUM
        val bg = getCustomBgColor()?.toColor()
        val pri = getCustomPrimaryColor()?.toColor()
        val sec = getCustomSecondaryColor()?.toColor()
        val acc = getCustomAccentColor()?.toColor()

        val activeBg = bg ?: baseTheme.background
        val activePri = pri ?: baseTheme.primary
        val activeSec = sec ?: baseTheme.secondary
        val activeAcc = acc ?: baseTheme.accent

        _themeState.value = baseTheme.copy(
            background = activeBg,
            runningBg = activeBg,
            primary = activePri,
            secondary = activeSec,
            accent = activeAcc,
            timerArcColors = listOf(activePri, activeSec, activeAcc)
        )
    }

    fun getFontId(): String = getPrefs().getString(KEY_FONT_ID, "comic_neue") ?: "comic_neue"
    fun setFontId(fontId: String) {
        getPrefs().edit().putString(KEY_FONT_ID, fontId).apply()
        _fontState.value = com.example.ui.theme.FontOption.fromId(fontId)
    }
}
