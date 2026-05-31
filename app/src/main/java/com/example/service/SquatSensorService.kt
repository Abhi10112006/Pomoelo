package com.example.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.util.ArrayList
import kotlin.math.sqrt

class SquatSensorService(context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelSensor: Sensor? = null
    
    // Squat detection state
    private var squatCount = 0
    private var requiredSquats = 0
    
    // Low-pass filter for 3D vector magnitude
    private var filteredValue = 9.81f
    private val alpha = 0.15f 

    // Chaotic high-frequency vibration/shake safety shield
    private val rawMagnitudeHistory = ArrayList<Float>()
    private val HISTORY_SIZE = 25
    private var isShakingChaotically = false
    private var lastShakeTime = 0L

    // State machine delay to ensure authentic deliberate vertical movement
    private var isDescending = false
    private var descentStartTime = 0L
    private var inSquatDown = false
    private var isAscending = false
    private var ascentStartTime = 0L
    private var lastCompletedSquatTime = 0L

    private val DOWN_THRESHOLD = 8.2f // m/s^2 (descending gravity release)
    private val UP_THRESHOLD = 11.4f  // m/s^2 (ascending heavy thrust/deceleration)

    private var onSquatsCompleted: (() -> Unit)? = null
    private var onSquatDetected: ((Int) -> Unit)? = null
    private var onShakeStatusChanged: ((Boolean) -> Unit)? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
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
        filteredValue = 9.81f
        rawMagnitudeHistory.clear()
        isShakingChaotically = false
        lastShakeTime = 0L
        isDescending = false
        descentStartTime = 0L
        inSquatDown = false
        isAscending = false
        ascentStartTime = 0L
        lastCompletedSquatTime = 0L
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
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        
        // 1. Calculate 3D acceleration magnitude (orientation-independent)
        val rawMagnitude = sqrt(x * x + y * y + z * z)

        // 2. Accumulate raw magnitude history to detect high-frequency shaking
        rawMagnitudeHistory.add(rawMagnitude)
        if (rawMagnitudeHistory.size > HISTORY_SIZE) {
            rawMagnitudeHistory.removeAt(0)
        }

        // 3. Identify noise level via peak-to-peak amplitude over the sliding window
        val prevShakeState = isShakingChaotically
        if (rawMagnitudeHistory.size >= 10) {
            val minHistory = rawMagnitudeHistory.minOrNull() ?: 9.81f
            val maxHistory = rawMagnitudeHistory.maxOrNull() ?: 9.81f
            val amplitude = maxHistory - minHistory
            // Shaking rapidly spikes acceleration magnitude up and down (heavy variance)
            isShakingChaotically = amplitude > 7.0f
        } else {
            isShakingChaotically = false
        }

        if (isShakingChaotically) {
            lastShakeTime = currentTime
        }

        if (prevShakeState != isShakingChaotically) {
            onShakeStatusChanged?.invoke(isShakingChaotically)
        }

        // 4. State validation (Fully lockout / reset progress if chaotic shaking is ongoing or recently occurred)
        if (isShakingChaotically || currentTime - lastShakeTime < 1000L) {
            // Hard reset of squat state machine to prevent false positives from settling data
            isDescending = false
            inSquatDown = false
            isAscending = false
            return
        }

        // 5. Low-Pass Filter: suppress small vibrations and extract slow, smooth intentional human motion
        filteredValue = alpha * rawMagnitude + (1.0f - alpha) * filteredValue

        // 6. Deliberate Human Squat State Machine
        if (!inSquatDown) {
            // Looking for a sustained, intentional descent (gravity release)
            if (filteredValue < DOWN_THRESHOLD) {
                if (!isDescending) {
                    isDescending = true
                    descentStartTime = currentTime
                } else {
                    // Check if descent is sustained for at least 250 milliseconds
                    if (currentTime - descentStartTime >= 250L) {
                        inSquatDown = true
                        isDescending = false
                    }
                }
            } else {
                isDescending = false
            }
        } else {
            // In the descent phase, looking for a sustained, intentional push-up ascent
            // Reset if they hold the down position or are inactive for more than 4.5 seconds (prevents drift)
            if (currentTime - descentStartTime > 4500L) {
                inSquatDown = false
                isAscending = false
            }
            
            if (filteredValue > UP_THRESHOLD) {
                if (!isAscending) {
                    isAscending = true
                    ascentStartTime = currentTime
                } else {
                    // Check if ascent is sustained for at least 250 milliseconds
                    if (currentTime - ascentStartTime >= 250L) {
                        val timeSinceLastSquat = currentTime - lastCompletedSquatTime
                        // Require at least 1500ms between standard physical squats
                        if (timeSinceLastSquat >= 1500L) {
                            inSquatDown = false
                            isAscending = false
                            lastCompletedSquatTime = currentTime
                            
                            squatCount++
                            onSquatDetected?.invoke(squatCount)
                            
                            if (squatCount >= requiredSquats) {
                                stopTracking()
                                onSquatsCompleted?.invoke()
                            }
                        } else {
                            isAscending = false
                        }
                    }
                }
            } else {
                isAscending = false
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
