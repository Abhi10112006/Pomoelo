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

    private val studyQuotes = listOf(
        "Your future is getting fucked while you scroll like a dumbass.",
        "Competitors are grinding, tu yahan reels pe lund chus raha hai.",
        "Padhna toh padega, saale. Yeh dopamine ka chuttad mat ban.",
        "Every notification you open is another seat you’re kissing goodbye.",
        "Time waste kar raha hai? Result mein gaand phategi teri.",
        "Toppers are solving papers, you’re busy liking bullshit posts.",
        "Kal se padhunga attitude will fuck your entire life.",
        "Yeh phone tera baap nahi hai, iska gulam mat ban.",
        "You’re not resting, you’re just a weak addicted piece of shit.",
        "Exam hall mein yeh regret tere muh pe thappad marega.",
        "Har reel ek question banegi jo tujhe fail karegi.",
        "Aukaat nahi hai toh sapne dekhna band kar, chutiye.",
        "Your rank is dying every minute you waste on this garbage.",
        "Distractions ne teri life ki gaand maar li hai.",
        "Stop lying, motherfucker. You’re not studying at all.",
        "Yeh “thoda aur” wala scene tujhe road pe laayega.",
        "Future self already hates your guts for this bullshit.",
        "Clock nahi rukega, teri gaand pakad ke le jayega.",
        "Competitors ne 4 hours padh liye, tu abhi bhi chat pe hai. ",
        "Yeh addiction tera selection ka gandha murderer hai.",
        "You chose cheap dopamine over your fucking dream.",
        "Result aayega toh sirf rona aur “kash” hi bachega.",
        "Phone ne tujhe bewakoof bana diya hai, samajh le.",
        "Average log scroll karte hain. Winners unko maar dete hain.",
        "Tera time barbaad ho raha hai aur tu has raha hai.",
        "Jab paper khali rahega, yaad aayega yeh moment.",
        "Stop betraying yourself, you spineless fuck.",
        "Notifications tere life ka control le chuke hain.",
        "Yeh break 2 ghante ka ho gaya, ab ro mat.",
        "Sapne bade hain, lekin discipline zero. Pathetic.",
        "Har ghanta waste = ek attempt ki gaand.",
        "Tu nahi so raha, tu apni future ko maar raha hai.",
        "Reels dekh ke kya milega? Sirf regret aur zero rank.",
        "Your competitors don’t give a fuck about your excuses.",
        "Phone rakh nahi pa raha? Toh haar maan le saale.",
        "Yeh “chill” mode tujhe lifetime loser bana dega.",
        "Distraction free nahi hua toh selection bhi nahi hoga.",
        "Time is fucking running, and you’re standing still.",
        "Exam ke din yeh guilt tujhe andar se kha jayega.",
        "Stop wasting your parents’ money on this bullshit habit.",
        "Put the fucking phone down and lock in right now.",
        "Grind kar, warna zindagi bhar gaand marwa.",
        "Timer on. Distractions ko fuck off bol.",
        "Lock the fuck in or get destroyed in the exam.",
        "Padhle madarchod, baad mein rona mat.",
        "Competitors jaise padh, warna unke peeche hi reh.",
        "No more bullshit. Books khol aur shuru ho ja.",
        "Phone side kar, brain on kar, abhi.",
        "Fuck the notifications. Study like your life depends on it.",
        "Aukaat dikha saale. Beast mode ON.",
        "Every second you delay is another seat gone, motherfucker.",
        "Stop thinking. Start fucking grinding.",
        "Yeh session complete kar, baaki duniya baad mein.",
        "Tu topper banega ya chu**iya banega? Decide now.",
        "Lock in so hard your distractions start crying.",
        "Gaand mein dum hai toh timer laga aur padh.",
        "No more “bas ek video”. Pure focus, abhi.",
        "Competitors nahi soye, tu kyun apni gaand bacha raha hai?",
        "Padh le bhenchod, warna sapne chod de.",
        "Crush this session like you want to crush the competition.",
        "Distraction free. Zero bakchodi. Full savage mode.",
        "Abhi shuru kar, warna lifetime regret shuru hoga.",
        "Timer laga. World ko mute kar. Lock in.",
        "Be the ruthless student your future self will respect.",
        "Gaali khani hai ya selection jeetna hai? Choose fast.",
        "Stop scrolling, start destroying questions.",
        "Hard work kar, baaki sab bakchodi hai saale.",
        "Lock in so brutally that even apps get scared.",
        "Yeh moment tera hai. Waste mat kar, chutiye.",
        "Phone band kar. Brain ON. Let’s fucking destroy it.",
        "Competitors ko peeche chhod, ab tera time hai.",
        "No mercy for distractions. Study like a machine.",
        "Timer shuru. Mind locked. Future secured, motherfucker.",
        "Padh le warna zindagi bhar “kash” bolta rahega.",
        "Become fucking unstoppable. Start this session now.",
        "Distractions ko delete kar. Discipline ko fuck in.",
        "Abhi ka pain kal ka top rank banega.",
        "No half-assed shit. Full savage focus.",
        "Tu jeetega, lekin abhi action lena padega bhenchod.",
        "Lock in. Grind hard. Fuck the competition."
    )

    private val breakQuotes = listOf(
        "Rest is not laziness; it is a vital part of growth. Breathe.",
        "Recharge your energy. Your mind deserves a peaceful pause.",
        "Disconnect for a few moments to reconnect even stronger.",
        "Relax, release tension, and let your mind wander.",
        "A mindful break clears the path for ultimate creativity.",
        "Inhale peace, exhale stress. Good job on your session!",
        "Rest when you're weary. Refresh and renew yourself.",
        "Taking time to rest is a gesture of self-respect.",
        "Give yourself permission to pause and appreciate progress.",
        "A rested brain is an inspired brain. Enjoy this moment.",
        "Quiet the mind and the soul will speak.",
        "Stretch your body, drink some water, and clear your gaze.",
        "Step back from the canvas of work to see the whole picture.",
        "Pause. Breathe. Smile. You are doing exceptionally well.",
        "Let your thoughts settle like petals on a quiet pond.",
        "True productivity includes knowing when to heal and rest.",
        "The rhythm of life requires both the stride and the pause.",
        "A brief rest restores the spirit and sharpens the intellect.",
        "Look out the window, find the sky, and remember your roots.",
        "Close your eyes for ten deep breaths. Let go of the hurry.",
        "Your mind is a sky; let the clouds of thoughts float away.",
        "Peace is not the absence of work; it is the presence of rest within.",
        "A glass of water, a deep stretch, a free mind.",
        "Gentle pauses create space for brilliant epiphanies.",
        "Allow your focus muscles to fully relax. You've earned it.",
        "Breathe in fresh inspiration, and blow out lingering fatigue.",
        "Pace yourself; the journey of knowledge is a beautiful marathon.",
        "Rest is the sacred soil in which focus takes root.",
        "Let your shoulders drop. Let your jaw soften. Just be.",
        "Stepping away is sometimes the fastest way to leap forward.",
        "Unload the mental stack. Empty your mind for a few minutes.",
        "The pauses between notes are what make the music beautiful.",
        "You have built momentum; now let it rest with grace.",
        "Take a minute to wander without a map.",
        "A break is the breath that supports your next great work.",
        "Savor this pause—it is the bridge between effort and reward.",
        "Nurture your clarity. Let your thoughts wander free.",
        "A moment of stillness can realign a whole day of focus.",
        "Give your focus a soft place to land before the next flight.",
        "You are precious, and your peace is paramount. Rest well."
    )

    private fun getRandomQuote(isBreakMode: Boolean): String {
        return if (isBreakMode) {
            breakQuotes.random()
        } else {
            studyQuotes.random()
        }
    }

    private fun getProgressBarString(percent: Int): String {
        val barLength = 10
        val filledCount = ((percent * barLength) / 100).coerceIn(0, barLength)
        val sb = StringBuilder()
        for (i in 0 until barLength) {
            if (i < filledCount) {
                sb.append("▰")
            } else {
                sb.append("▱")
            }
        }
        return "$sb $percent%"
    }
    
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_TOGGLE_MODE = "ACTION_TOGGLE_MODE"
        const val CHANNEL_ID = "pomelo_timer_channel"
        const val NOTIFICATION_ID = 1
        const val ALARM_CHANNEL_ID = "pomelo_alarm_channel"
        const val ALARM_NOTIFICATION_ID = 99
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        TimerManager.timerState.onEach { state ->
            updateNotification()
        }.launchIn(serviceScope)

        TimerManager.timeRemainingSeconds.onEach { time ->
            updateNotification()
        }.launchIn(serviceScope)

        TimerManager.isBreakMode.onEach { mode ->
            updateNotification()
        }.launchIn(serviceScope)

        TimerManager.currentTaskName.onEach { name ->
            updateNotification()
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
            updateNotification()
            InactivityScheduler.schedule(this)
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        SoundGenerator.stop()
        releaseWakeLock()
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
        val progressPercent = if (totalSeconds > 0) (elapsedSeconds * 100) / totalSeconds else 0

        if (currentQuote.isEmpty()) {
            currentQuote = getRandomQuote(isBreak)
        }

        val stateName = TimerManager.currentTaskName.value
        val timeStr = formatTime(remainingSeconds)
        val progressVisual = getProgressBarString(progressPercent.coerceIn(0, 100))

        val headerText = if (isBreak) "☕ Break Time" else "🎯 Focus Session"
        val isRunning = TimerManager.timerState.value == TimerManager.TimerState.RUNNING
        val displayTitle = "$headerText — $timeStr"
        
        val bigText = "Session: $stateName\n" +
                      "Progress: $progressVisual\n\n" +
                      "✨ \"$currentQuote\""

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(displayTitle)
            .setContentText("$progressVisual • $stateName")
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setColor(if (isBreak) android.graphics.Color.parseColor("#81D4FA") else android.graphics.Color.parseColor("#FF8A80"))
            .setProgress(totalSeconds, elapsedSeconds, false)
            .setUsesChronometer(false)
            .setShowWhen(false)

        if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
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
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
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
