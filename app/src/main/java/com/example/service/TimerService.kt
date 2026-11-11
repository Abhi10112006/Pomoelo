package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
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
        const val CHANNEL_ID = "TimerServiceChannel"
        const val COMPLETION_CHANNEL_ID = "TimerCompletionChannel"
        const val NOTIFICATION_ID = 1
        const val COMPLETION_NOTIFICATION_ID = 2

        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_TOGGLE_MODE = "ACTION_TOGGLE_MODE"
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

    private fun startTimer() {
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(COMPLETION_NOTIFICATION_ID)
        } catch (_: Exception) {}

        InactivityScheduler.cancel(applicationContext)

        if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
            promoteToForeground()
            return
        }

        if (TimerManager.timeRemainingSeconds.value <= 0) {
            val initialSeconds = if (TimerManager.isBreakMode.value) {
                TimerManager.breakTimeSeconds.value
            } else {
                TimerManager.focusTimeSeconds.value
            }
            TimerManager.updateTime(initialSeconds)
        }

        TimerManager.updateState(TimerManager.TimerState.RUNNING)
        startTimestamp = System.currentTimeMillis()
        enableDndIfPermitted()

        try {
            if (wakeLock?.isHeld == false) {
                val maxTimeMs = (TimerManager.timeRemainingSeconds.value + 60) * 1000L
                wakeLock?.acquire(maxTimeMs)
            }
        } catch (_: Exception) {}

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
                updateProgressNotification()

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
        updateProgressNotification()
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

        if (!completedWasBreak) {
            TimerManager.notifyTaskCompleted()
            val taskId = TimerManager.currentTaskId.value
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
                            taskName = "Break Time!",
                            isBreak = true,
                            durationMinutes = durationMins.coerceAtLeast(1),
                            startTime = start,
                            endTime = end,
                            taskColor = null
                        )
                    )
                } catch (_: Throwable) {}
            }
        }

        val newIsBreak = !completedWasBreak
        TimerManager.setBreakMode(newIsBreak)
        TimerManager.updateState(TimerManager.TimerState.STOPPED)
        TimerManager.updateTime(
            if (newIsBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value
        )
        if (newIsBreak) {
            TimerManager.setTask(-1, "Break Time!")
        } else if (TimerManager.currentTaskId.value == -1) {
            TimerManager.setTask(-1, "Focus Time!")
        }

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

    private fun updateProgressNotification() {
        try {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, createProgressNotification())
        } catch (_: Exception) {}
    }

    private fun resolveNotificationColor(taskColor: Long?, isBreak: Boolean): Int {
        val theme = SettingsManager.themeState.value
        val defaultArgb = if (isBreak) theme.secondary.toArgb() else theme.primary.toArgb()
        return if (!isBreak && taskColor != null) {
            safeParseColorArgb(taskColor, defaultArgb)
        } else {
            defaultArgb
        }
    }

    private fun createProgressNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val timeRemaining = TimerManager.timeRemainingSeconds.value.coerceAtLeast(0)
        val minutes = timeRemaining / 60
        val seconds = timeRemaining % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)
        val isBreak = TimerManager.isBreakMode.value
        val state = TimerManager.timerState.value

        val rawTaskName = TimerManager.currentTaskName.value
        val cleanTaskName = when {
            isBreak -> "Mindful Break"
            rawTaskName.isBlank() || rawTaskName == "Focus Time!" -> "Deep Focus Session"
            else -> rawTaskName
        }

        val totalSeconds = (if (isBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value)
            .coerceAtLeast(1)
        val elapsedSeconds = (totalSeconds - timeRemaining).coerceIn(0, totalSeconds)

        val subText = when {
            state == TimerManager.TimerState.PAUSED -> "Session Paused"
            isBreak -> "Rest • Mindful Break"
            else -> "Focus • Deep Work"
        }

        val titleText = "$cleanTaskName  •  $timeString"
        val bodyText = when {
            state == TimerManager.TimerState.PAUSED -> "Paused with $timeString remaining. Tap Resume to continue."
            isBreak -> "Step away from the screen, breathe deeply, and recharge."
            else -> "Stay present with your goal. Distraction Shield is standing by."
        }

        val toggleIntent = Intent(this, TimerService::class.java).apply {
            action = if (state == TimerManager.TimerState.RUNNING) ACTION_PAUSE else ACTION_START
        }
        val togglePendingIntent = PendingIntent.getService(
            this,
            1,
            toggleIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val toggleLabel = if (state == TimerManager.TimerState.RUNNING) "Pause" else "Resume"

        val stopIntent = Intent(this, TimerService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            2,
            stopIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val accentColor = resolveNotificationColor(TimerManager.currentTaskColor.value, isBreak)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setSubText(subText)
            .setContentTitle(titleText)
            .setContentText(bodyText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bodyText).setBigContentTitle(titleText))
            .setContentIntent(pendingIntent)
            .setOngoing(state == TimerManager.TimerState.RUNNING)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setColor(accentColor)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setProgress(totalSeconds, elapsedSeconds, false)
            .addAction(0, toggleLabel, togglePendingIntent)
            .addAction(0, "End Session", stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun postCompletionNotification(
        completedWasBreak: Boolean,
        completedTaskName: String,
        completedDurationMins: Int,
        taskColor: Long?
    ) {
        try {
            val openAppIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val openPendingIntent = PendingIntent.getActivity(
                this,
                10,
                openAppIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val nextSessionIntent = Intent(this, TimerService::class.java).apply {
                action = ACTION_START
            }
            val nextSessionPendingIntent = PendingIntent.getService(
                this,
                11,
                nextSessionIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val subText = if (completedWasBreak) {
                "Break Complete • Recharged"
            } else {
                "Focus Complete • ${completedDurationMins}m Logged"
            }

            val cleanTask = if (completedTaskName == "Focus Time!" || completedTaskName.isBlank()) {
                "your focus session"
            } else {
                completedTaskName
            }

            val title = if (completedWasBreak) {
                "Break finished — ready to focus?"
            } else {
                "Session complete • Well done"
            }

            val nextMins = if (completedWasBreak) {
                SettingsManager.getFocusTimeMins()
            } else {
                SettingsManager.getBreakTimeMins()
            }

            val body = if (completedWasBreak) {
                "Your mindful break is complete. Start your next ${nextMins}-minute focus block whenever you're ready."
            } else {
                "You completed ${completedDurationMins}m on $cleanTask. Take a ${nextMins}-minute break to rest and reset."
            }

            val actionLabel = if (completedWasBreak) "Start Focus (${nextMins}m)" else "Start Break (${nextMins}m)"
            val accentColor = resolveNotificationColor(taskColor, completedWasBreak)

            val notification = NotificationCompat.Builder(this, COMPLETION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setSubText(subText)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .setBigContentTitle(title)
                        .bigText(body)
                )
                .setContentIntent(openPendingIntent)
                .setAutoCancel(true)
                .setOngoing(false)
                .setColor(accentColor)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, actionLabel, nextSessionPendingIntent)
                .build()

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(COMPLETION_NOTIFICATION_ID, notification)
        } catch (_: Exception) {}
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java) ?: return

            val progressChannel = NotificationChannel(
                CHANNEL_ID,
                "Active Focus Timer",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Displays real-time countdown and controls during active focus and break sessions."
                setShowBadge(false)
            }
            manager.createNotificationChannel(progressChannel)

            val completionChannel = NotificationChannel(
                COMPLETION_CHANNEL_ID,
                "Session Completion Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies you when a focus or break session finishes."
                enableVibration(true)
                setShowBadge(true)
            }
            manager.createNotificationChannel(completionChannel)
        }
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
