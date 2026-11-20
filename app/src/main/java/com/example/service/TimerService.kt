package com.example.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService : Service() {

    private var timerJob: Job? = null
    private var quoteJob: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob() + exceptionHandler)

    private var wakeLock: PowerManager.WakeLock? = null
    private var previousInterruptionFilter = NotificationManager.INTERRUPTION_FILTER_ALL
    private var dndModifiedByUs = false
    private lateinit var audioManager: AudioManager
    private var focusRequest: AudioFocusRequest? = null
    private var startTimestamp: Long = 0L
    private var sessionTotalSeconds: Int = 1500
    private var lastNotificationPercent: Int = -1
    private var lastNotificationUpdateAtMs: Long = 0L
    private var liveUpdateDismissedByUser: Boolean = false
    private var completedFocusCountThisRun: Int = 0

    private val afChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS,
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
                    SoundGenerator.stop()
                }
            }
            AudioManager.AUDIOFOCUS_GAIN -> {
                if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING && !TimerManager.isBreakMode.value) {
                    SoundGenerator.play(this@TimerService, SettingsManager.getBackgroundSound())
                }
            }
        }
    }

    companion object {
        const val CHANNEL_ID = LiveTimerNotificationManager.LIVE_TIMER_CHANNEL_ID
        const val COMPLETION_CHANNEL_ID = LiveTimerNotificationManager.COMPLETION_CHANNEL_ID
        const val NOTIFICATION_ID = 1
        const val COMPLETION_NOTIFICATION_ID = 2

        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_TOGGLE_MODE = "ACTION_TOGGLE_MODE"
        const val ACTION_ADD_FIVE_MIN = "ACTION_ADD_FIVE_MIN"
        const val ACTION_SKIP_BREAK = "ACTION_SKIP_BREAK"
        const val ACTION_NOTIFICATION_DISMISSED = "ACTION_NOTIFICATION_DISMISSED"
    }

    private lateinit var database: com.example.data.AppDatabase

    override fun onCreate() {
        super.onCreate()
        SettingsManager.init(applicationContext)
        database = com.example.data.DatabaseProvider.getDatabase(applicationContext)
        createNotificationChannels()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PomoPal::TimerWakeLock")
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(afChangeListener)
                .build()
        }
    }

    private fun requestAudioFocus(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val res = audioManager.requestAudioFocus(focusRequest!!)
                res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            } else {
                @Suppress("DEPRECATION")
                val res = audioManager.requestAudioFocus(
                    afChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
                res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun abandonAudioFocus() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                focusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
            } else {
                @Suppress("DEPRECATION")
                audioManager.abandonAudioFocus(afChangeListener)
            }
        } catch (_: Exception) {}
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == null) {
            if (TimerManager.timerState.value != TimerManager.TimerState.RUNNING) {
                stopSelf()
                return START_NOT_STICKY
            }
            promoteToForeground()
            return START_STICKY
        }

        when (action) {
            ACTION_START -> {
                startTimer()
                return START_STICKY
            }
            ACTION_PAUSE -> {
                pauseTimer()
                return START_NOT_STICKY
            }
            ACTION_STOP -> {
                stopTimer()
                return START_NOT_STICKY
            }
            ACTION_TOGGLE_MODE -> {
                toggleMode()
                return START_NOT_STICKY
            }
            ACTION_ADD_FIVE_MIN -> {
                addFiveMinutes()
                return if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) START_STICKY else START_NOT_STICKY
            }
            ACTION_SKIP_BREAK -> {
                skipBreakToFocus()
                return START_NOT_STICKY
            }
            ACTION_NOTIFICATION_DISMISSED -> {
                liveUpdateDismissedByUser = true
                return if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) START_STICKY else START_NOT_STICKY
            }
        }
        return START_NOT_STICKY
    }

    private fun promoteToForeground() {
        try {
            val notification = createProgressNotification()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        } catch (e: Exception) {
            try {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, createProgressNotification())
            } catch (_: Exception) {}
        }
    }

    private fun toggleMode() {
        stopTimer()
        val newIsBreak = !TimerManager.isBreakMode.value
        TimerManager.setBreakMode(newIsBreak)
        TimerManager.updateTime(
            if (newIsBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value
        )
        TimerManager.setTask(-1, if (newIsBreak) "Break Time!" else "Focus Time!")
    }

    private fun addFiveMinutes() {
        val currentRemaining = TimerManager.timeRemainingSeconds.value.coerceAtLeast(0)
        val extraSeconds = 5 * 60
        val updatedRemaining = currentRemaining + extraSeconds
        val defaultTotal = if (TimerManager.isBreakMode.value) {
            TimerManager.breakTimeSeconds.value
        } else {
            TimerManager.focusTimeSeconds.value
        }
        sessionTotalSeconds = maxOf(sessionTotalSeconds, defaultTotal, updatedRemaining)
        TimerManager.updateTime(updatedRemaining)

        // Extend wake lock if currently running
        if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
            try {
                if (wakeLock?.isHeld == true) {
                    wakeLock?.release()
                }
                wakeLock?.acquire((updatedRemaining + 60) * 1000L)
            } catch (_: Exception) {}
        }

        // User interacted with the notification -> allow immediate refresh
        liveUpdateDismissedByUser = false
        updateProgressNotification(force = true)
    }

    private fun skipBreakToFocus() {
        timerJob?.cancel()
        quoteJob?.cancel()
        SoundGenerator.stop()
        abandonAudioFocus()
        restoreDndIfModified()
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (_: Exception) {}
        TimerManager.setBreakMode(false)
        TimerManager.updateState(TimerManager.TimerState.STOPPED)
        TimerManager.updateTime(TimerManager.focusTimeSeconds.value)
        TimerManager.setTask(-1, "Focus Time!")
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
        } catch (_: Exception) {}
        stopForeground(STOP_FOREGROUND_REMOVE)
        InactivityScheduler.schedule(applicationContext)
        stopSelf()
    }

    private fun startTimer() {
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(COMPLETION_NOTIFICATION_ID)
        } catch (_: Exception) {}

        InactivityScheduler.cancel(applicationContext)
        liveUpdateDismissedByUser = false

        val wasPaused = TimerManager.timerState.value == TimerManager.TimerState.PAUSED
        if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
            promoteToForeground()
            return
        }

        val defaultDuration = if (TimerManager.isBreakMode.value) {
            TimerManager.breakTimeSeconds.value
        } else {
            TimerManager.focusTimeSeconds.value
        }.coerceAtLeast(1)

        if (TimerManager.timeRemainingSeconds.value <= 0) {
            TimerManager.updateTime(defaultDuration)
            sessionTotalSeconds = defaultDuration
        } else if (!wasPaused) {
            sessionTotalSeconds = maxOf(defaultDuration, TimerManager.timeRemainingSeconds.value)
        } else {
            sessionTotalSeconds = maxOf(sessionTotalSeconds, defaultDuration, TimerManager.timeRemainingSeconds.value)
        }

        TimerManager.updateState(TimerManager.TimerState.RUNNING)
        if (!wasPaused || startTimestamp <= 0L) {
            startTimestamp = System.currentTimeMillis()
        }
        enableDndIfPermitted()

        try {
            if (wakeLock?.isHeld == false) {
                val maxTimeMs = (TimerManager.timeRemainingSeconds.value + 60) * 1000L
                wakeLock?.acquire(maxTimeMs)
            }
        } catch (_: Exception) {}

        lastNotificationPercent = -1
        lastNotificationUpdateAtMs = System.currentTimeMillis()
        promoteToForeground()

        if (!TimerManager.isBreakMode.value) {
            if (requestAudioFocus()) {
                SoundGenerator.play(this, SettingsManager.getBackgroundSound())
            }
        } else {
            SoundGenerator.stop()
            abandonAudioFocus()
        }

        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (isActive && TimerManager.timeRemainingSeconds.value > 0) {
                delay(1000L)
                val nextValue = TimerManager.timeRemainingSeconds.value - 1
                TimerManager.updateTime(nextValue)
                updateProgressNotification(force = false)

                if (nextValue <= 0) {
                    handleTimerCompletion()
                }
            }
        }

        quoteJob?.cancel()
        quoteJob = serviceScope.launch {
            while (isActive && TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
                val isBreak = TimerManager.isBreakMode.value
                val quote = QuoteManager.getFreshQuote(
                    this@TimerService,
                    isBreak,
                    if (isBreak) "last_break_idx" else "last_focus_idx"
                )
                TimerManager.setCurrentQuote(quote)
                delay(10000L)
            }
        }
    }

    private fun pauseTimer() {
        TimerManager.updateState(TimerManager.TimerState.PAUSED)
        timerJob?.cancel()
        quoteJob?.cancel()
        SoundGenerator.stop()
        abandonAudioFocus()
        restoreDndIfModified()
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (_: Exception) {}
        liveUpdateDismissedByUser = false
        updateProgressNotification(force = true)
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun stopTimer() {
        TimerManager.updateState(TimerManager.TimerState.STOPPED)
        timerJob?.cancel()
        quoteJob?.cancel()
        SoundGenerator.stop()
        abandonAudioFocus()
        restoreDndIfModified()
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (_: Exception) {}
        TimerManager.updateTime(
            if (TimerManager.isBreakMode.value) {
                TimerManager.breakTimeSeconds.value
            } else {
                TimerManager.focusTimeSeconds.value
            }
        )
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
        } catch (_: Exception) {}
        stopForeground(STOP_FOREGROUND_REMOVE)
        InactivityScheduler.schedule(applicationContext)
        stopSelf()
    }

    private fun handleTimerCompletion() {
        val completedWasBreak = TimerManager.isBreakMode.value
        val completedTask = TimerManager.currentTaskName.value.ifBlank {
            if (completedWasBreak) "Mindful Break" else "Focus Session"
        }
        val completedColor = TimerManager.currentTaskColor.value
        val durationMins = if (completedWasBreak) {
            TimerManager.breakTimeSeconds.value / 60
        } else {
            TimerManager.focusTimeSeconds.value / 60
        }
        val end = System.currentTimeMillis()
        val start = if (startTimestamp > 0L) startTimestamp else (end - (durationMins * 60 * 1000L))

        val taskId = TimerManager.currentTaskId.value
        if (!completedWasBreak) {
            completedFocusCountThisRun++
            TimerManager.notifyTaskCompleted()
            serviceScope.launch {
                try {
                    database.sessionDao().insertSession(
                        com.example.data.TimerSession(
                            taskName = completedTask,
                            isBreak = false,
                            durationMinutes = durationMins.coerceAtLeast(1),
                            startTime = start,
                            endTime = end,
                            taskColor = completedColor
                        )
                    )
                    if (taskId != -1) {
                        database.taskDao().incrementPomodoroCount(taskId)
                    }
                } catch (_: Throwable) {}
            }
        } else {
            serviceScope.launch {
                try {
                    database.sessionDao().insertSession(
                        com.example.data.TimerSession(
                            taskName = if (taskId != -1) completedTask else "Break Time!",
                            isBreak = true,
                            durationMinutes = durationMins.coerceAtLeast(1),
                            startTime = start,
                            endTime = end,
                            taskColor = if (taskId != -1) completedColor else null
                        )
                    )
                    if (taskId != -1) {
                        database.taskDao().incrementPomodoroCount(taskId)
                    }
                } catch (_: Throwable) {}
            }
        }

        val newIsBreak = !completedWasBreak
        TimerManager.setBreakMode(newIsBreak)
        TimerManager.updateState(TimerManager.TimerState.STOPPED)
        TimerManager.updateTime(
            if (newIsBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value
        )
        TimerManager.setTask(-1, if (newIsBreak) "Break Time!" else "Focus Time!")

        SoundPlayer.playCompletion(this, SettingsManager.getCompletionSound())

        timerJob?.cancel()
        quoteJob?.cancel()
        SoundGenerator.stop()
        abandonAudioFocus()
        restoreDndIfModified()
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (_: Exception) {}

        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
        } catch (_: Exception) {}
        stopForeground(STOP_FOREGROUND_REMOVE)

        postCompletionNotification(
            completedWasBreak = completedWasBreak,
            completedTaskName = completedTask,
            completedDurationMins = durationMins.coerceAtLeast(1),
            taskColor = completedColor
        )

        InactivityScheduler.schedule(applicationContext)
        stopSelf()
    }

    private fun enableDndIfPermitted() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && notificationManager.isNotificationPolicyAccessGranted) {
            try {
                if (!TimerManager.isBreakMode.value) {
                    previousInterruptionFilter = notificationManager.currentInterruptionFilter
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
                    dndModifiedByUs = true
                }
            } catch (_: SecurityException) {}
        }
    }

    private fun restoreDndIfModified() {
        if (!dndModifiedByUs) return
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && notificationManager.isNotificationPolicyAccessGranted) {
            try {
                notificationManager.setInterruptionFilter(previousInterruptionFilter)
                dndModifiedByUs = false
            } catch (_: SecurityException) {}
        }
    }

    private fun updateProgressNotification(force: Boolean = false) {
        if (!force && liveUpdateDismissedByUser) return

        val totalSecs = sessionTotalSeconds.coerceAtLeast(1)
        val remaining = TimerManager.timeRemainingSeconds.value.coerceIn(0, totalSecs)
        val elapsed = (totalSecs - remaining).coerceIn(0, totalSecs)
        val currentPercent = ((elapsed.toFloat() / totalSecs.toFloat()) * 100f).toInt().coerceIn(0, 100)
        val now = System.currentTimeMillis()

        // SystemUI's native Chronometer ticks every second smoothly in the Island / Pill / Shade.
        // Refresh the ProgressStyle bar / M3 ring badge when forced, on phase milestones (20/50/65/80%),
        // or smoothly when progress moves >= 1% with at least a 5s cooldown (or every 15s max) to avoid
        // Android 15/16 notification rate limiting.
        if (!force) {
            val elapsedSinceLast = now - lastNotificationUpdateAtMs
            val percentChanged = currentPercent != lastNotificationPercent
            val crossedMilestone = (lastNotificationPercent < 20 && currentPercent >= 20) ||
                (lastNotificationPercent < 50 && currentPercent >= 50) ||
                (lastNotificationPercent < 65 && currentPercent >= 65) ||
                (lastNotificationPercent < 80 && currentPercent >= 80)
            val shouldUpdate = crossedMilestone ||
                (percentChanged && elapsedSinceLast >= 5_000L) ||
                elapsedSinceLast >= 15_000L
            if (!shouldUpdate) return
        }

        lastNotificationPercent = currentPercent
        lastNotificationUpdateAtMs = now
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, createProgressNotification())
        } catch (_: Exception) {}
    }

    private fun createProgressNotification(): Notification {
        val isBreak = TimerManager.isBreakMode.value
        val defaultTotal = (if (isBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value)
            .coerceAtLeast(1)
        val effectiveTotal = maxOf(sessionTotalSeconds, defaultTotal, TimerManager.timeRemainingSeconds.value)
            .coerceAtLeast(1)

        return LiveTimerNotificationManager.buildLiveTimerNotification(
            context = this,
            snapshot = LiveTimerNotificationManager.LiveTimerSnapshot(
                taskName = TimerManager.currentTaskName.value,
                isBreak = isBreak,
                state = TimerManager.timerState.value,
                timeRemainingSeconds = TimerManager.timeRemainingSeconds.value.coerceAtLeast(0),
                totalSeconds = effectiveTotal,
                taskColor = TimerManager.currentTaskColor.value,
                isDndActive = dndModifiedByUs,
                focusCompletedToday = completedFocusCountThisRun
            )
        )
    }

    private fun postCompletionNotification(
        completedWasBreak: Boolean,
        completedTaskName: String,
        completedDurationMins: Int,
        taskColor: Long?
    ) {
        try {
            val notification = LiveTimerNotificationManager.buildCompletionNotification(
                context = this,
                completedWasBreak = completedWasBreak,
                completedTaskName = completedTaskName,
                completedDurationMins = completedDurationMins,
                taskColor = taskColor
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(COMPLETION_NOTIFICATION_ID, notification)
        } catch (_: Exception) {}
    }

    private fun createNotificationChannels() {
        LiveTimerNotificationManager.createChannels(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        timerJob?.cancel()
        quoteJob?.cancel()
        serviceScope.cancel()
        SoundGenerator.stop()
        abandonAudioFocus()
        restoreDndIfModified()
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (_: Exception) {}
    }
}
