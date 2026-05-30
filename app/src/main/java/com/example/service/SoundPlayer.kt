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
}
