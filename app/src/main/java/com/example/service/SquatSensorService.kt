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
    
    private val DOWN_THRESHOLD = 7.5f // m/s^2 (lower than typical 9.8 gravity)
    private val UP_THRESHOLD = 11.5f // m/s^2 (higher than typical gravity when coming up)

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
        onSquatsCompleted = onComplete
        onSquatDetected = onUpdate

        accelSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopTracking() {
        sensorManager?.unregisterListener(this)
        onSquatsCompleted = null
        onSquatDetected = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        // Assuming the phone is held vertically. Y axis is event.values[1]
        val yAccel = event.values[1]

        if (!inSquatDown && yAccel < DOWN_THRESHOLD) {
            inSquatDown = true
        } else if (inSquatDown && yAccel > UP_THRESHOLD) {
            inSquatDown = false
            squatCount++
            onSquatDetected?.invoke(squatCount)
            
            if (squatCount >= requiredSquats) {
                stopTracking()
                onSquatsCompleted?.invoke()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for simple squat detection
    }
}
