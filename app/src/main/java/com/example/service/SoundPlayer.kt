package com.example.service

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import kotlinx.coroutines.*

object SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var ringtone: android.media.Ringtone? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var stopJob: Job? = null

    fun playCompletion(context: Context, type: Int) {
        stop()
        try {
            if (type == 3) { // Custom
                val uriStr = SettingsManager.getCustomCompletionUri()
                if (uriStr != null) {
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(context, Uri.parse(uriStr))
                        prepare()
                        start()
                    }
                }
            } else {
                val uri = when (type) {
                    1 -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    2 -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    else -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                }
                ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone?.play()
            }
            
            val durationMs = SettingsManager.getCompletionDurationSec() * 1000L
            stopJob = scope.launch {
                delay(durationMs)
                stop()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        stopJob?.cancel()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {}
        mediaPlayer = null

        try {
            ringtone?.stop()
        } catch (e: Exception) {}
        ringtone = null
    }

    private var alarmMediaPlayer: MediaPlayer? = null
    private var volumeJob: Job? = null

    fun startContinuousAlarm(context: Context) {
        stopContinuousAlarm()
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as android.media.AudioManager
            val maxAlarmVol = audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_ALARM)
            val maxMusicVol = audioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC)
            
            audioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM, maxAlarmVol, 0)
            audioManager.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, maxMusicVol, 0)

            // Continuously force max volume to prevent user from dismissing/silencing through standard keys
            volumeJob = scope.launch(Dispatchers.Main) {
                while (isActive) {
                    try {
                        audioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM, maxAlarmVol, 0)
                        audioManager.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, maxMusicVol, 0)
                    } catch (e: Exception) {}
                    delay(1000)
                }
            }

            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            alarmMediaPlayer = MediaPlayer().apply {
                setDataSource(context, uri)
                // Use STREAM_ALARM or fallback to STREAM_MUSIC
                setAudioStreamType(android.media.AudioManager.STREAM_ALARM)
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback play completion or Ringtone
            try {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone?.play()
            } catch (ex: Exception) {}
        }
    }

    fun stopContinuousAlarm() {
        volumeJob?.cancel()
        volumeJob = null
        try {
            alarmMediaPlayer?.stop()
            alarmMediaPlayer?.release()
        } catch (e: Exception) {}
        alarmMediaPlayer = null
    }
}
