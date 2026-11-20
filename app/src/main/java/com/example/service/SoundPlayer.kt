package com.example.service

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import kotlinx.coroutines.*

object SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var ringtone: android.media.Ringtone? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var stopJob: Job? = null

    fun playCompletion(context: Context, type: Int) {
        stop()
        val appContext = context.applicationContext
        try {
            when (type) {
                3 -> { // Custom
                    val uriStr = SettingsManager.getCustomCompletionUri()
                    if (uriStr != null) {
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(appContext, Uri.parse(uriStr))
                            isLooping = true
                            prepare()
                            start()
                        }
                    }
                }
                1, 2 -> { // Alarm, Ring
                    val uri = when (type) {
                        1 -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        else -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    }
                    ringtone = RingtoneManager.getRingtone(appContext, uri)
                    ringtone?.play()
                }
                else -> { // 0: Get Up (default)
                    val player = MediaPlayer.create(appContext, com.example.R.raw.get_up)
                    if (player != null) {
                        player.isLooping = true
                        player.start()
                        mediaPlayer = player
                    } else {
                        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        ringtone = RingtoneManager.getRingtone(appContext, uri)
                        ringtone?.play()
                    }
                }
            }

            val durationMs = SettingsManager.getCompletionDurationSec().coerceAtLeast(1) * 1000L
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
}
