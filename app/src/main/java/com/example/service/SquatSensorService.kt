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
    
    // Squat detection counter state
    private var squatCount = 0
    private var requiredSquats = 0
    
    // Dynamic Gravity vector calculation (Slow LPF)
    private var gravityX = 0f
    private var gravityY = 9.81f
    private var gravityZ = 0f
    private val gravityAlpha = 0.08f 

    // Smooth linear vertical acceleration along gravity vector (Highly restrictive LPF)
    private var filteredVerticalAccel = 0f
    private val alphaVertical = 0.07f // Restricted to perfectly isolate massive slow squat waves and suppress micro-vibrations

    // Sliding window of raw magnitude values to detect high-frequency chaotic rattling/shaking
    private val rawMagnitudeHistory = ArrayList<Float>()
    private val HISTORY_SIZE = 25
    private var isShakingChaotically = false
    private var lastShakeTime = 0L

    // Distinct Peak-Valley dynamic threshold states
    private var valleyDetected = false
    private var valleyTime = 0L
    private var minVerticalValue = 0f

    // Standard gravity-normalized thresholds for human mechanics (in m/s^2)
    private val VALLEY_THRESHOLD = -1.8f
    private val PEAK_THRESHOLD = 1.8f  

    private var lastCompletedSquatTime = 0L

    // Live update listeners
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
        
        // Reset dynamic gravity estimates to avoid holding stale orientations
        gravityX = 0f
        gravityY = 9.81f
        gravityZ = 0f
        filteredVerticalAccel = 0f
        
        rawMagnitudeHistory.clear()
        isShakingChaotically = false
        lastShakeTime = 0L
        
        valleyDetected = false
        valleyTime = 0L
        minVerticalValue = 0f
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

        // 1. Estimate current coordinate system's gravity components dynamically.
        // This makes the sensor perfectly pocket/directional agnostic.
        if (gravityX == 0f && gravityY == 9.81f && gravityZ == 0f) {
            gravityX = x
            gravityY = y
            gravityZ = z
        } else {
            gravityX = gravityAlpha * x + (1.0f - gravityAlpha) * gravityX
            gravityY = gravityAlpha * y + (1.0f - gravityAlpha) * gravityY
            gravityZ = gravityAlpha * z + (1.0f - gravityAlpha) * gravityZ
        }

        val gMagnitude = sqrt(gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ)
        if (gMagnitude < 1.0f) return // Out of range baseline check

        // 2. Compute normal vectors along dynamic vertical axis
        val nx = gravityX / gMagnitude
        val ny = gravityY / gMagnitude
        val nz = gravityZ / gMagnitude

        // 3. Project current raw 3D acceleration along the dynamic gravity vector
        val rawAccelAlongGravity = x * nx + y * ny + z * nz

        // 4. Subtract gravity magnitude to get the vertical component of linear acceleration
        // In free space: ~0m/s^2 when stationary.
        // Negative component = moving listlessly downwards, Positive component = accelerating upwards.
        val rawLinearVerticalAccel = rawAccelAlongGravity - gMagnitude

        // 5. Aggressive Low-Pass Filter (Rule 1: alpha Vertical = 0.07f)
        // Eliminates jitter/shaking spikes while conserving slow body momentum waves.
        filteredVerticalAccel = alphaVertical * rawLinearVerticalAccel + (1.0f - alphaVertical) * filteredVerticalAccel

        // 6. Compute Raw Acceleration Magnitude for Jitter/Chaotic Shake Monitoring
        val rawAccelMagnitude = sqrt(x * x + y * y + z * z)
        rawMagnitudeHistory.add(rawAccelMagnitude)
        if (rawMagnitudeHistory.size > HISTORY_SIZE) {
            rawMagnitudeHistory.removeAt(0)
        }

        val prevShakeState = isShakingChaotically
        if (rawMagnitudeHistory.size >= 12) {
            val minHistory = rawMagnitudeHistory.minOrNull() ?: 9.81f
            val maxHistory = rawMagnitudeHistory.maxOrNull() ?: 9.81f
            val amplitude = maxHistory - minHistory
            // High magnitude variance clearly marks rapid erratic phone rattling (humanly impossible squats)
            isShakingChaotically = amplitude > 7.5f
        } else {
            isShakingChaotically = false
        }

        if (isShakingChaotically) {
            lastShakeTime = currentTime
        }

        if (prevShakeState != isShakingChaotically) {
            onShakeStatusChanged?.invoke(isShakingChaotically)
        }

        // Lockout Guard: Fully block progress and reset peak states if active high-frequency vibrations are detected
        if (isShakingChaotically || (currentTime - lastShakeTime) < 1200L) {
            valleyDetected = false
            minVerticalValue = 0f
            return
        }

        // 7. Peak-Valley State Machine for intentional squat registration
        if (!valleyDetected) {
            // Find a massive downwards movement trigger (valley)
            if (filteredVerticalAccel < VALLEY_THRESHOLD) {
                valleyDetected = true
                valleyTime = currentTime
                minVerticalValue = filteredVerticalAccel
            }
        } else {
            // Looking for the rebound upward acceleration spike (peak)
            // Safety release: Reset descent if they hold the down position or drift for more than 3.5 seconds
            if (currentTime - valleyTime > 3500L) {
                valleyDetected = false
                return
            }

            if (filteredVerticalAccel < minVerticalValue) {
                minVerticalValue = filteredVerticalAccel
            }

            if (filteredVerticalAccel > PEAK_THRESHOLD) {
                val transitionTime = currentTime - valleyTime

                // CRITICAL TIME LOCKOUT (Rule 4):
                // If transition from downwards to upward peak happens faster than 300ms, it is a high-frequency shake.
                if (transitionTime < 300L) {
                    valleyDetected = false
                    return
                }

                // Strict deliberate human squat check: cooldown guard (1500m/s)
                val timeSinceLastSquat = currentTime - lastCompletedSquatTime
                if (timeSinceLastSquat >= 1500L) {
                    valleyDetected = false
                    lastCompletedSquatTime = currentTime
                    
                    squatCount++
                    onSquatDetected?.invoke(squatCount)

                    if (squatCount >= requiredSquats) {
                        stopTracking()
                        onSquatsCompleted?.invoke()
                    }
                } else {
                    valleyDetected = false
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
