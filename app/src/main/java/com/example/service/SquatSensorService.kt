package com.example.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import java.util.ArrayList
import kotlin.math.sqrt

class SquatSensorService(context: Context) : SensorEventListener {

    enum class SquatState {
        IDLE,
        DESCENDING,
        ASCENDING
    }

    private var sensorManager: SensorManager? = null
    private var gravitySensor: Sensor? = null
    private var linearAccelSensor: Sensor? = null
    private var accelSensor: Sensor? = null
    private var usingFallbackAccel = false
    
    // Squat detection counter state
    private var squatCount = 0
    private var requiredSquats = 0
    
    // Sensor fusion & orientation independence states
    private var gravityX = 0f
    private var gravityY = 9.81f
    private var gravityZ = 0f
    private var gravityReceived = false

    // Smooth linear vertical acceleration along gravity vector (LPF alpha ~ 0.1f)
    private var filteredVerticalAccel = 0f
    private val LPF_ALPHA = 0.1f

    // Leaky velocity integration state
    private var verticalVelocity = 0f
    private var lastTimestamp = 0L

    // Sliding window of raw magnitude values to detect high-frequency chaotic shaking
    private val rawMagnitudeHistory = ArrayList<Float>()
    private val HISTORY_SIZE = 25
    private var isShakingChaotically = false
    private var lastShakeTime = 0L

    // State machine for deliberate squat tracking
    private var currentState = SquatState.IDLE
    private var descentStartTime = 0L
    private var lastCompletedSquatTime = 0L
    private var currentRepMinVelocity = 0f
    private var currentRepMaxVelocity = 0f

    // Live update listeners
    private var onSquatsCompleted: (() -> Unit)? = null
    private var onSquatDetected: ((Int) -> Unit)? = null
    private var onShakeStatusChanged: ((Boolean) -> Unit)? = null
    private var onSquatMetrics: ((Float, Float) -> Unit)? = null


    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        linearAccelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        
        if (linearAccelSensor == null) {
            accelSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            usingFallbackAccel = true
        }
    }

    fun startTracking(
        targetSquats: Int, 
        onComplete: () -> Unit, 
        onUpdate: (Int) -> Unit,
        onShakeWarning: ((Boolean) -> Unit)? = null,
        onMetrics: ((Float, Float) -> Unit)? = null
    ) {
        if (targetSquats <= 0 && onMetrics == null) {
            onComplete()
            return
        }
        requiredSquats = targetSquats
        squatCount = 0
        
        // Reset dynamic states
        gravityX = 0f
        gravityY = 9.81f
        gravityZ = 0f
        gravityReceived = false
        filteredVerticalAccel = 0f
        verticalVelocity = 0f
        lastTimestamp = 0L
        
        currentState = SquatState.IDLE
        descentStartTime = 0L
        lastCompletedSquatTime = 0L
        currentRepMinVelocity = 0f
        currentRepMaxVelocity = 0f
        
        rawMagnitudeHistory.clear()
        isShakingChaotically = false
        lastShakeTime = 0L
        
        onSquatsCompleted = onComplete
        onSquatDetected = onUpdate
        onShakeStatusChanged = onShakeWarning
        onSquatMetrics = onMetrics


        if (!usingFallbackAccel) {
            gravitySensor?.let {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
            linearAccelSensor?.let {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        } else {
            accelSensor?.let {
                sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    fun stopTracking() {
        sensorManager?.unregisterListener(this)
        onSquatsCompleted = null
        onSquatDetected = null
        onShakeStatusChanged = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        val currentTime = System.currentTimeMillis()

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            gravityX = event.values[0]
            gravityY = event.values[1]
            gravityZ = event.values[2]
            gravityReceived = true
            return
        }

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // Apply low pass filter to raw accelerometer to isolate gravity
            val alpha = 0.8f
            gravityX = alpha * gravityX + (1 - alpha) * event.values[0]
            gravityY = alpha * gravityY + (1 - alpha) * event.values[1]
            gravityZ = alpha * gravityZ + (1 - alpha) * event.values[2]
            gravityReceived = true

            // Subtract gravity to estimate linear acceleration
            val linX = event.values[0] - gravityX
            val linY = event.values[1] - gravityY
            val linZ = event.values[2] - gravityZ

            processLinearAcceleration(linX, linY, linZ, event.timestamp, currentTime)
            return
        }

        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            if (!gravityReceived) return
            processLinearAcceleration(event.values[0], event.values[1], event.values[2], event.timestamp, currentTime)
        }
    }

    private fun processLinearAcceleration(linX: Float, linY: Float, linZ: Float, timestamp: Long, currentTime: Long) {
            // 1. Sensor Fusion & Orientation Independence
            val gMagnitude = sqrt(gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ)
            if (gMagnitude < 1.0f) return

            val normGx = gravityX / gMagnitude
            val normGy = gravityY / gMagnitude
            val normGz = gravityZ / gMagnitude

            // Dot product to project linear acceleration on normal gravity vector
            val trueVerticalAccel = (linX * normGx) + (linY * normGy) + (linZ * normGz)

            // Low-Pass Filter (alpha ~ 0.1f)
            filteredVerticalAccel = (LPF_ALPHA * trueVerticalAccel) + ((1.0f - LPF_ALPHA) * filteredVerticalAccel)

            // 2. Leaky Velocity Integration
            val dt = if (lastTimestamp == 0L) {
                0f
            } else {
                (timestamp - lastTimestamp) / 1_000_000_000f
            }
            lastTimestamp = timestamp

            // Handle frame/event hiccups gracefully
            val cappedDt = if (dt > 0.1f) 0.1f else dt

            // Integrate with leaky decay of 0.95f
            verticalVelocity = (verticalVelocity + (filteredVerticalAccel * cappedDt)) * 0.95f

            val currentValleyThreshold = SettingsManager.getSquatValleyThreshold() ?: -0.4f
            val currentPeakThreshold = SettingsManager.getSquatPeakThreshold() ?: 0.4f

            // Verbose logging of the telemetry
            Log.d(
                "SquatDebug",
                "Current Velocity: ${verticalVelocity.format()} | Required Valley Threshold: ${currentValleyThreshold.format()} (Personalized)\n" +
                "TrueVert: ${trueVerticalAccel.format()}, " +
                "FilteredVert: ${filteredVerticalAccel.format()}, " +
                "State: $currentState, " +
                "Elapsed: ${if (descentStartTime > 0) "${currentTime - descentStartTime}ms" else "N/A"}"
            )

            // 3. Shake Detection & Guard
            val rawLinearMagnitude = sqrt(linX * linX + linY * linY + linZ * linZ)
            rawMagnitudeHistory.add(rawLinearMagnitude)
            if (rawMagnitudeHistory.size > HISTORY_SIZE) {
                rawMagnitudeHistory.removeAt(0)
            }

            val prevShakeState = isShakingChaotically
            if (rawMagnitudeHistory.size >= 12) {
                val minHistory = rawMagnitudeHistory.minOrNull() ?: 0f
                val maxHistory = rawMagnitudeHistory.maxOrNull() ?: 0f
                val amplitude = maxHistory - minHistory
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

            // Lockout logic
            if (isShakingChaotically || (currentTime - lastShakeTime) < 1200L) {
                if (currentState != SquatState.IDLE) {
                    currentState = SquatState.IDLE
                    Log.d("SquatDebug", "Lockout due to high-frequency shaking. Reset to IDLE.")
                }
                verticalVelocity = 0f
                return
            }

            // Track extremes during movement
            if (currentState != SquatState.IDLE || requiredSquats == 0) { // always track extremes in calibration mode
                if (verticalVelocity < currentRepMinVelocity) currentRepMinVelocity = verticalVelocity
                if (verticalVelocity > currentRepMaxVelocity) currentRepMaxVelocity = verticalVelocity
                onSquatMetrics?.invoke(verticalVelocity, verticalVelocity)
            }

            // 4. Velocity-Based State Machine
            // State 1 (Descending Phase): verticalVelocity drops below valleyThreshold
            if (verticalVelocity < currentValleyThreshold) {
                if (currentState != SquatState.DESCENDING) {
                    currentState = SquatState.DESCENDING
                    descentStartTime = currentTime
                    currentRepMinVelocity = 0f // Reset relative to rep
                    currentRepMaxVelocity = 0f
                    Log.d("SquatDebug", "State change: IDLE -> DESCENDING, velocity: ${verticalVelocity.format()}")
                }
            } 
            // State 2 (Ascending Phase): verticalVelocity rises above peakThreshold
            else if (verticalVelocity > currentPeakThreshold) {
                if (currentState == SquatState.DESCENDING) {
                    val elapsed = currentTime - descentStartTime
                    Log.d("SquatDebug", "State evaluation: DESCENDING -> ASCENDING check. elapsed: ${elapsed}ms, velocity: ${verticalVelocity.format()}")

                    // Verify transition window: 500ms to 2500ms
                    if (elapsed in 500..2500) {
                        val timeSinceLast = currentTime - lastCompletedSquatTime
                        if (timeSinceLast >= 200L) {
                            currentState = SquatState.ASCENDING
                            lastCompletedSquatTime = currentTime
                            
                            squatCount++
                            onSquatDetected?.invoke(squatCount)
                            Log.d("SquatDebug", "SQUAT VALIDATED! repCount: $squatCount, completionTime: ${elapsed}ms")

                            if (requiredSquats > 0 && squatCount >= requiredSquats) {
                                onSquatsCompleted?.invoke()
                                stopTracking()
                            }
                        } else {
                            Log.d("SquatDebug", "Squat ignored: cooldown of 200ms active. ${timeSinceLast}ms elapsed")
                            currentState = SquatState.IDLE
                        }
                    } else {
                        Log.d("SquatDebug", "Squat ignored: invalid duration bounds of ${elapsed}ms")
                        if (elapsed > 2500L) {
                            currentState = SquatState.IDLE
                        }
                    }
                }
            } else {
                // Return to IDLE when velocity calms down or descent timer is exceeded
                if (currentState == SquatState.ASCENDING && verticalVelocity < currentPeakThreshold / 2) {
                    currentState = SquatState.IDLE
                    Log.d("SquatDebug", "State change: ASCENDING -> IDLE, velocity normalized: ${verticalVelocity.format()}")
                }

                if (currentState == SquatState.DESCENDING && (currentTime - descentStartTime) > 2500L) {
                    currentState = SquatState.IDLE
                    Log.d("SquatDebug", "State change: DESCENDING -> IDLE due to timeout expiration")
                }
            }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Unused
    }

    private fun Float.format() = String.format("%.3f", this)
}
