package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TimerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var timerJob: Job? = null
    
    private var wakeLock: android.os.PowerManager.WakeLock? = null

    private fun acquireWakeLock() {
        try {
            if (wakeLock == null) {
                val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
                wakeLock = powerManager.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, "PomeloTimer::TimerWakeLock")
            }
            if (wakeLock?.isHeld == false) {
                wakeLock?.acquire()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releaseWakeLock() {
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private var isBreak: Boolean
        get() = TimerManager.isBreakMode.value
        set(value) {
            TimerManager.setBreakMode(value)
        }
    private var sessionStartTime = 0L
    private var currentQuote = ""
        set(value) {
            field = value
            TimerManager.setCurrentQuote(value)
        }

    private fun getRandomQuote(isBreakMode: Boolean): String {
        return QuoteManager.getFreshQuote(this, isBreakMode, "timer_quote_index")
    }
    
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_TOGGLE_MODE = "ACTION_TOGGLE_MODE"
        const val CHANNEL_ID = "pomelo_timer_channel_v2"
        const val NOTIFICATION_ID = 1
        const val ALARM_CHANNEL_ID = "pomelo_alarm_channel"
        const val ALARM_NOTIFICATION_ID = 99
    }

    private var lastNotifiedMinute = -1
    private var trackingFinishTimeMillis = 0L
    private var originalInterruptionFilter = -1

    private fun setDndMode(enable: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (notificationManager.isNotificationPolicyAccessGranted) {
                    if (enable) {
                        if (originalInterruptionFilter == -1) {
                            originalInterruptionFilter = notificationManager.currentInterruptionFilter
                        }
                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                    } else {
                        if (originalInterruptionFilter != -1) {
                            notificationManager.setInterruptionFilter(originalInterruptionFilter)
                            originalInterruptionFilter = -1
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        TimerManager.timerState.onEach { state ->
            updateNotification()
        }.launchIn(serviceScope)

        TimerManager.timeRemainingSeconds.onEach { time ->
            val currentMinute = time / 60
            if (currentMinute != lastNotifiedMinute) {
                lastNotifiedMinute = currentMinute
                updateNotification()
            }
        }.launchIn(serviceScope)

        TimerManager.isBreakMode.onEach { mode ->
            updateNotification()
        }.launchIn(serviceScope)

        TimerManager.currentTaskName.onEach { name ->
            updateNotification()
        }.launchIn(serviceScope)
        
        val db = com.example.data.DatabaseProvider.getDatabase(applicationContext)

        TimerManager.taskCompletedEvent.onEach { taskId ->
            withContext(Dispatchers.IO) {
                db.taskDao().incrementPomodoroCount(taskId)
            }
        }.launchIn(serviceScope)

        TimerManager.sessionCompletedEvent.onEach { data ->
            withContext(Dispatchers.IO) {
                db.sessionDao().insertSession(
                    com.example.data.TimerSession(
                        taskName = data.taskName,
                        isBreak = data.isBreak,
                        durationMinutes = data.durationMinutes,
                        startTime = data.startTime,
                        endTime = data.endTime
                    )
                )
            }
        }.launchIn(serviceScope)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startTimer()
            ACTION_PAUSE -> pauseTimer()
            ACTION_STOP -> stopTimer()
            ACTION_TOGGLE_MODE -> toggleMode()
        }
        return START_STICKY
    }

    private fun toggleMode() {
        // Only allow toggle if STOPPED? Or anytime?
        // Let's allow anytime, it stops timer, toggles, updates state.
        timerJob?.cancel()
        SoundGenerator.stop()
        releaseWakeLock()
        setDndMode(false)
        isBreak = !isBreak
        currentQuote = getRandomQuote(isBreak)
        TimerManager.setBreakMode(isBreak)
        TimerManager.updateState(TimerManager.TimerState.STOPPED)
        TimerManager.updateTime(if (isBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value)
        TimerManager.setTask(-1, if (isBreak) "Break Time!" else "Focus Time!")
        updateNotification()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun startTimer() {
        // Prevent concurrent identical timers, but if we are restarting from finished, it's fine.
        // We cancel the existing job first just in case, rather than returning early.
        timerJob?.cancel()
        InactivityScheduler.cancel(this)
        
        // Always pick a fresh quote corresponding to the current isBreak mode on start
        currentQuote = getRandomQuote(isBreak)
            
        TimerManager.updateState(TimerManager.TimerState.RUNNING)
        TimerManager.setBreakMode(isBreak)
        setDndMode(!isBreak)
        startForegroundService()
        acquireWakeLock()
        
        SoundGenerator.stop()
        
        val initialRemaining = TimerManager.timeRemainingSeconds.value
        val targetTimeStr = System.currentTimeMillis() + initialRemaining * 1000L
        
        val totalMins = if (isBreak) SettingsManager.getBreakTimeMins() else SettingsManager.getFocusTimeMins()
        val totalSeconds = totalMins * 60
        if (initialRemaining == totalSeconds || sessionStartTime == 0L) {
            sessionStartTime = System.currentTimeMillis()
        }
        
        trackingFinishTimeMillis = System.currentTimeMillis() + initialRemaining * 1000L
        
        timerJob = serviceScope.launch {
            var lastQuoteSeconds = initialRemaining
            while (isActive) {
                    val remaining = ((targetTimeStr - System.currentTimeMillis()) / 1000).toInt()
                    if (remaining <= 0) {
                        TimerManager.updateTime(0)
                        break
                    }
                    if (remaining != TimerManager.timeRemainingSeconds.value) {
                         TimerManager.updateTime(remaining)
                         if (!isBreak && (lastQuoteSeconds - remaining) >= 60) {
                             lastQuoteSeconds = remaining
                             currentQuote = getRandomQuote(isBreak)
                         }
                    }
                    // Poll at a high frequency to keep UI buttery smooth without drift
                    delay(50) 
                }
                if (isActive) {
                    timerFinished()
                }
            }
        }

    private fun pauseTimer() {
        if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
            TimerManager.updateState(TimerManager.TimerState.PAUSED)
            timerJob?.cancel()
            SoundGenerator.stop()
            releaseWakeLock()
            setDndMode(false)
            updateNotification()
            InactivityScheduler.schedule(this)
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        SoundGenerator.stop()
        releaseWakeLock()
        setDndMode(false)
        isBreak = false
        currentQuote = ""
        TimerManager.setBreakMode(isBreak)
        TimerManager.updateState(TimerManager.TimerState.STOPPED)
        TimerManager.updateTime(TimerManager.focusTimeSeconds.value)
        stopForeground(STOP_FOREGROUND_REMOVE)
        InactivityScheduler.schedule(this)
        stopSelf()
    }

    private fun timerFinished() {
        SoundGenerator.stop()
        releaseWakeLock()
        setDndMode(false)
        vibrate()
        playSound()
        
        val endTime = System.currentTimeMillis()
        val totalMins = if (isBreak) SettingsManager.getBreakTimeMins() else SettingsManager.getFocusTimeMins()
        TimerManager.notifySessionCompleted(
            taskName = TimerManager.currentTaskName.value,
            isBreak = isBreak,
            durationMinutes = totalMins,
            startTime = if (sessionStartTime > 0) sessionStartTime else (endTime - totalMins * 60 * 1000),
            endTime = endTime
        )
        sessionStartTime = 0L
        
        // Notify Activity for celebration and db update
        val intent = Intent("com.example.TIMER_FINISHED")
        intent.putExtra("isBreak", isBreak)
        sendBroadcast(intent)
 
        if (!isBreak) {
            TimerManager.notifyTaskCompleted()
        }

        // Show a dedicated high-priority alarm notification
        showAlarmNotification()

        isBreak = !isBreak
        currentQuote = getRandomQuote(isBreak)
        TimerManager.setBreakMode(isBreak)
        TimerManager.updateState(TimerManager.TimerState.STOPPED)
        TimerManager.updateTime(if (isBreak) TimerManager.breakTimeSeconds.value else TimerManager.focusTimeSeconds.value)
        TimerManager.setTask(-1, if (isBreak) "Break Time!" else "Focus Time!")
        updateNotification()
        stopForeground(STOP_FOREGROUND_REMOVE)
        InactivityScheduler.schedule(this)
    }

    private fun showAlarmNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_IMMUTABLE)

        val title = if (isBreak) "☕ Break finished!" else "🎯 Focus Session Completed!"
        val actionText = if (isBreak) "Ready to focus?" else "Take a well-deserved break!"
        val message = "Great job! Click here to start your next session.\n✨ \"$currentQuote\""

        val builder = NotificationCompat.Builder(this, ALARM_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(actionText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(pendingIntent, true)
            .setColor(if (isBreak) android.graphics.Color.parseColor("#81D4FA") else android.graphics.Color.parseColor("#FF8A80"))

        manager.notify(ALARM_NOTIFICATION_ID, builder.build())
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(500)
        }
    }

    private fun playSound() {
        SoundPlayer.playCompletion(applicationContext, SettingsManager.getCompletionSound())
    }

    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, buildNotification())
        }
    }

    private fun updateNotification() {
        if (TimerManager.timerState.value != TimerManager.TimerState.STOPPED) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, buildNotification())
        }
    }

    private fun buildNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val totalMins = if (isBreak) SettingsManager.getBreakTimeMins() else SettingsManager.getFocusTimeMins()
        val totalSeconds = totalMins * 60
        val remainingSeconds = TimerManager.timeRemainingSeconds.value
        val elapsedSeconds = (totalSeconds - remainingSeconds).coerceAtLeast(0)

        if (currentQuote.isEmpty()) {
            currentQuote = getRandomQuote(isBreak)
        }

        val stateName = TimerManager.currentTaskName.value
        val timeStr = formatTime(remainingSeconds)
        val isRunning = TimerManager.timerState.value == TimerManager.TimerState.RUNNING

        val headerText = if (isBreak) "☕ Break Time" else "🎯 Focus Session"
        val displayTitle = if (isRunning) "$headerText Active" else "$headerText (PAUSED)"
        
        val contentText = if (isRunning) "✨ \"$currentQuote\"" else "⏳ $timeStr • ✨ \"$currentQuote\""

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(displayTitle)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Session: $stateName\n$contentText"))
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setContentIntent(pendingIntent)
            .setOngoing(isRunning)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setColor(if (isBreak) android.graphics.Color.parseColor("#81D4FA") else android.graphics.Color.parseColor("#FF8A80"))
            .setProgress(totalSeconds, elapsedSeconds, false)

        if (isRunning) {
            builder.setUsesChronometer(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setChronometerCountDown(true)
            }
            builder.setWhen(System.currentTimeMillis() + (remainingSeconds * 1000L))
        } else {
            builder.setUsesChronometer(false)
            builder.setShowWhen(false)
        }

        if (isRunning) {
            val pauseIntent = Intent(this, TimerService::class.java).apply { action = ACTION_PAUSE }
            val pausePendingIntent = PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
            builder.addAction(0, "⏸ PAUSE", pausePendingIntent)
        } else {
            val startIntent = Intent(this, TimerService::class.java).apply { action = ACTION_START }
            val startPendingIntent = PendingIntent.getService(this, 2, startIntent, PendingIntent.FLAG_IMMUTABLE)
            builder.addAction(0, "▶ RESUME", startPendingIntent)
        }

        val stopIntent = Intent(this, TimerService::class.java).apply { action = ACTION_STOP }
        val stopPendingIntent = PendingIntent.getService(this, 3, stopIntent, PendingIntent.FLAG_IMMUTABLE)
        builder.addAction(0, "⏹ STOP", stopPendingIntent)

        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                setSound(null, null)
            }
            val alarmChannel = NotificationChannel(
                ALARM_CHANNEL_ID,
                "Session Completion Alarms",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                enableVibration(true)
                description = "High priority alarm-style notifications when a timer finishes"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            manager.createNotificationChannel(alarmChannel)
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
