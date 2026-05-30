package com.example.service

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.random.Random

object SoundGenerator {
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var playThread: Thread? = null

    private var mediaPlayer: android.media.MediaPlayer? = null

    fun play(context: android.content.Context, type: Int) { // 1 = White, 2 = Rain (Brown), 3 = Ocean (Pink), 4 = Flute, 5 = Custom
        stop()
        if (type == 0) return
        
        if (type == 5) { // Custom
            val uriStr = SettingsManager.getCustomBackgroundUri()
            if (uriStr != null) {
                try {
                    mediaPlayer = android.media.MediaPlayer().apply {
                        setDataSource(context, android.net.Uri.parse(uriStr))
                        isLooping = true
                        prepare()
                        start()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
            return
        }

        isPlaying = true
        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack?.play()
        audioTrack?.setVolume(0.1f) // Keep it low for focus background

        playThread = Thread {
            val buffer = ShortArray(bufferSize)
            var lastOut = 0.0
            
            var b0 = 0.0; var b1 = 0.0; var b2 = 0.0; var b3 = 0.0; var b4 = 0.0; var b5 = 0.0; var b6 = 0.0
            
            // Flute params
            var phase = 0.0
            val frequency = 432.0 // A4 slightly lower (calm)
            val phaseInc = 2.0 * Math.PI * frequency / sampleRate

            while (isPlaying) {
                for (i in buffer.indices) {
                    val white = Random.nextDouble(-1.0, 1.0)
                    when (type) {
                        1 -> { // White Noise
                            buffer[i] = (white * 8000).toInt().toShort()
                        }
                        2 -> { // Brown Noise
                            lastOut = (lastOut + (0.02 * white)) / 1.02
                            buffer[i] = (lastOut * 32767 * 2).toInt().coerceIn(-32768, 32767).toShort()
                        }
                        3 -> { // Pink Noise
                            b0 = 0.99886 * b0 + white * 0.0555179
                            b1 = 0.99332 * b1 + white * 0.0750759
                            b2 = 0.96900 * b2 + white * 0.1538520
                            b3 = 0.86650 * b3 + white * 0.3104856
                            b4 = 0.55000 * b4 + white * 0.5329522
                            b5 = -0.7616 * b5 - white * 0.0168980
                            val pOut = b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362
                            b6 = white * 0.115926
                            buffer[i] = (pOut * 4000).toInt().coerceIn(-32768, 32767).toShort()
                        }
                        4 -> { // Flute (Sine with smooth breath-like modulation)
                            phase += phaseInc
                            if (phase >= 2.0 * Math.PI) phase -= 2.0 * Math.PI
                            // Combine fundamental with breathy noise
                            val sineVal = kotlin.math.sin(phase)
                            val modulated = sineVal + (white * 0.15 * kotlin.math.sin(phase / 2.0))
                            buffer[i] = (modulated * 16000).toInt().coerceIn(-32768, 32767).toShort()
                        }
                    }
                }
                audioTrack?.write(buffer, 0, buffer.size)
            }
        }
        playThread?.start()
    }

    fun stop() {
        isPlaying = false
        try {
            playThread?.join(200)
        } catch (e: Exception) {}
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {}
        audioTrack = null
        
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {}
        mediaPlayer = null
    }
}
