package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.service.SettingsManager
import com.example.service.SquatSensorService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SquatCalibrationViewModel(application: Application) : AndroidViewModel(application) {

    private val sensorService = SquatSensorService(application)

    private var _timeLeft = MutableStateFlow(10)
    val timeLeft: StateFlow<Int> = _timeLeft.asStateFlow()

    private val _isCalibrationComplete = MutableStateFlow(false)
    val isCalibrationComplete: StateFlow<Boolean> = _isCalibrationComplete.asStateFlow()

    private var absoluteMaxPeak = 0f
    private var absoluteMinValley = 0f

    fun startCalibration() {
        _timeLeft.value = 10
        _isCalibrationComplete.value = false
        absoluteMaxPeak = 0f
        absoluteMinValley = 0f

        // Let the tracking run with wide thresholds, though we don't care about reps anymore
        SettingsManager.setSquatValleyThreshold(-0.01f)
        SettingsManager.setSquatPeakThreshold(0.01f)

        // Give the user 10 seconds of raw tracking
        viewModelScope.launch {
            for (i in 10 downTo 1) {
                kotlinx.coroutines.delay(1000)
                _timeLeft.value = i - 1
            }
            finishCalibration()
        }

        sensorService.startTracking(
            targetSquats = 0, // Continuous
            onComplete = {},
            onUpdate = {},
            onShakeWarning = {},
            onMetrics = { minVel, maxVel ->
                if (minVel < absoluteMinValley) absoluteMinValley = minVel
                if (maxVel > absoluteMaxPeak) absoluteMaxPeak = maxVel
            }
        )
    }

    private fun finishCalibration() {
        sensorService.stopTracking()
        
        // Apply 30% tolerance multiplier
        val finalValley = absoluteMinValley * 0.3f
        val finalPeak = absoluteMaxPeak * 0.3f
        
        // To be safe, ensure it doesn't get too close to 0 due to noise
        val safeValley = if (finalValley > -0.2f) -0.2f else finalValley
        val safePeak = if (finalPeak < 0.2f) 0.2f else finalPeak

        // Save to settings
        SettingsManager.setSquatValleyThreshold(safeValley)
        SettingsManager.setSquatPeakThreshold(safePeak)
        
        _isCalibrationComplete.value = true
    }
    
    fun cleanUp() {
        sensorService.stopTracking()
    }
    
    override fun onCleared() {
        super.onCleared()
        cleanUp()
    }
}
