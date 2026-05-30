package com.example.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SquatSensorService(context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelSensor: Sensor? = null
    
    // Squat detection state
    private var squatCount = 0
    private var inSquatDown = false
    private var requiredSquats = 0
    
    // Low-Pass Filter parameters to kill high frequency shaking noises
    private var filteredY = 9.81f
    private val alpha = 0.2f // Smoothing coefficient: smaller values suppress high-frequency noise better

    // Time and speed lock variables to validate authentic human movement
    private var lastStateTransitionTime = 0L
    private var lastCompletedSquatTime = 0L

    private val MIN_STATE_DURATION_MS = 450L   // Real squat down/up transitions require physical phase duration
    private val MIN_SQUAT_DURATION_MS = 1000L   // Safeguards repetitions to look physically deliberate

    private val DOWN_THRESHOLD = 7.3f // m/s^2 (descending threshold)
    private val UP_THRESHOLD = 11.9f  // m/s^2 (ascending declaration threshold)

    private var onSquatsCompleted: (() -> Unit)? = null
    private var onSquatDetected: ((Int) -> Unit)? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun startTracking(targetSquats: Int, onComplete: () -> Unit, onUpdate: (Int) -> Unit) {
        if (targetSquats <= 0) {
            onComplete()
            return
        }
        requiredSquats = targetSquats
        squatCount = 0
        inSquatDown = false
        filteredY = 9.81f
        val now = System.currentTimeMillis()
        lastStateTransitionTime = now
        lastCompletedSquatTime = now
        onSquatsCompleted = onComplete
        onSquatDetected = onUpdate

        accelSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopTracking() {
        sensorManager?.unregisterListener(this)
        onSquatsCompleted = null
        onSquatDetected = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val currentTime = System.currentTimeMillis()
        val rawY = event.values[1]

        // 1. Low-Pass Filter: suppress chaotic high-frequency phone shaking
        filteredY = alpha * filteredY + (1.0f - alpha) * rawY

        // 2. Validate using state machine & time-locks (rejecting rapid twitching)
        if (!inSquatDown) {
            if (filteredY < DOWN_THRESHOLD) {
                // Eliminate rapid noise transition
                if (currentTime - lastStateTransitionTime > MIN_STATE_DURATION_MS) {
                    inSquatDown = true
                    lastStateTransitionTime = currentTime
                }
            }
        } else {
            if (filteredY > UP_THRESHOLD) {
                val stateDuration = currentTime - lastStateTransitionTime
                val squatDuration = currentTime - lastCompletedSquatTime

                // Require minimum duration inside the descent phase AND realistic squat repetition period
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
