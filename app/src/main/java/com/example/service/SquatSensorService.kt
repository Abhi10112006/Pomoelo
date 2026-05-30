package com.example.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.util.ArrayList

class SquatSensorService(context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelSensor: Sensor? = null
    
    // Squat detection state
    private var squatCount = 0
    private var inSquatDown = false
    private var requiredSquats = 0
    
    // Low-Pass Filter: smaller alpha = heavier smoothing. 0.12f isolates smooth human motion beautifully.
    private var filteredY = 9.81f
    private val alpha = 0.12f 

    // Chaotic high-frequency vibration/shake safety shield
    private val rawYHistory = ArrayList<Float>()
    private val HISTORY_SIZE = 20
    private var isShakingChaotically = false

    // State machine delay to ensure authentic deliberate vertical movement
    private var lastStateTransitionTime = 0L
    private var lastCompletedSquatTime = 0L

    private val MIN_STATE_DURATION_MS = 600L   // Deliberate down/up phases take time
    private val MIN_SQUAT_DURATION_MS = 1800L   // Cooldown between repetitions prevents rapid vibration double-counts

    private val DOWN_THRESHOLD = 7.6f // m/s^2 (descending gravity release)
    private val UP_THRESHOLD = 11.6f  // m/s^2 (ascending heavy thrust/deceleration)

    private var onSquatsCompleted: (() -> Unit)? = null
    private var onSquatDetected: ((Int) -> Unit)? = null
    private var onShakeStatusChanged: ((Boolean) -> Unit)? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun startTracking(
        targetSquats: Int, 
        onComplete: () -> Unit, 
        onUpdate: (Int) -> Unit,
        onShakeWarning: ((Boolean) -> Unit)? = null
    ) {
        if (targetSquats <= 0) {
            onComplete()
            return
        }
        requiredSquats = targetSquats
        squatCount = 0
        inSquatDown = false
        filteredY = 9.81f
        rawYHistory.clear()
        isShakingChaotically = false
        val now = System.currentTimeMillis()
        lastStateTransitionTime = now
        lastCompletedSquatTime = now
        onSquatsCompleted = onComplete
        onSquatDetected = onUpdate
        onShakeStatusChanged = onShakeWarning

        accelSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopTracking() {
        sensorManager?.unregisterListener(this)
        onSquatsCompleted = null
        onSquatDetected = null
        onShakeStatusChanged = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val currentTime = System.currentTimeMillis()
        val rawY = event.values[1]

        // 1. Accumulate raw Y history to detect high-frequency shaking
        rawYHistory.add(rawY)
        if (rawYHistory.size > HISTORY_SIZE) {
            rawYHistory.removeAt(0)
        }

        // 2. Identify noise level via peak-to-peak amplitude
        val prevShakeState = isShakingChaotically
        if (rawYHistory.size >= 10) {
            val minHistory = rawYHistory.minOrNull() ?: 9.81f
            val maxHistory = rawYHistory.maxOrNull() ?: 9.81f
            val amplitude = maxHistory - minHistory
            // If the phone is being shaken, values spike up and down rapidly (peak-to-peak amplitude > 8.5 m/s^2)
            isShakingChaotically = amplitude > 8.5f
        } else {
            isShakingChaotically = false
        }

        if (prevShakeState != isShakingChaotically) {
            onShakeStatusChanged?.invoke(isShakingChaotically)
        }

        // 3. Low-Pass Filter: suppress chaotic raw inputs
        // Formula: filteredY = alpha * rawY + (1.0f - alpha) * filteredY
        filteredY = alpha * rawY + (1.0f - alpha) * filteredY

        // 4. State validation (Fully block progress if chaotic shaking is ongoing)
        if (isShakingChaotically) {
            // Keep resetting state timers so they must stabilize before starting a real squat
            lastStateTransitionTime = currentTime
            return
        }

        // 5. Normal human squat state machine checks
        if (!inSquatDown) {
            if (filteredY < DOWN_THRESHOLD) {
                // Ensure the transition remains stable
                if (currentTime - lastStateTransitionTime > MIN_STATE_DURATION_MS) {
                    inSquatDown = true
                    lastStateTransitionTime = currentTime
                }
            }
        } else {
            if (filteredY > UP_THRESHOLD) {
                val stateDuration = currentTime - lastStateTransitionTime
                val squatDuration = currentTime - lastCompletedSquatTime

                // Require deliberate descent phase duration AND realistic repetition period
                if (stateDuration > MIN_STATE_DURATION_MS && squatDuration > MIN_SQUAT_DURATION_MS) {
                    inSquatDown = false
                    lastStateTransitionTime = currentTime
                    lastCompletedSquatTime = currentTime
                    
                    squatCount++
                    onSquatDetected?.invoke(squatCount)
                    
                    if (squatCount >= requiredSquats) {
                        stopTracking()
                        onSquatsCompleted?.invoke()
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
