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

    private val _calibratedSquats = MutableStateFlow(0)
    val calibratedSquats: StateFlow<Int> = _calibratedSquats.asStateFlow()

    private val _isCalibrationComplete = MutableStateFlow(false)
    val isCalibrationComplete: StateFlow<Boolean> = _isCalibrationComplete.asStateFlow()

    // Store peaks and valleys
    private val valleyList = mutableListOf<Float>()
    private val peakList = mutableListOf<Float>()

    fun startCalibration() {
        _calibratedSquats.value = 0
        _isCalibrationComplete.value = false
        valleyList.clear()
        peakList.clear()

        // Temporarily set loose thresholds just for calibration to ensure we catch reps easily
        val originalValley = SettingsManager.getSquatValleyThreshold()
        val originalPeak = SettingsManager.getSquatPeakThreshold()
        
        SettingsManager.setSquatValleyThreshold(-0.2f)
        SettingsManager.setSquatPeakThreshold(0.2f)

        sensorService.startTracking(
            targetSquats = 0, // 0 means don't auto-stop
            onComplete = {},
            onUpdate = { count ->
                if (_calibratedSquats.value < 3) {
                    _calibratedSquats.value = count
                }
            },
            onShakeWarning = {},
            onMetrics = { minVel, maxVel ->
                if (valleyList.size < 3) {
                    valleyList.add(minVel)
                    peakList.add(maxVel)
                    
                    if (valleyList.size == 3) {
                        finishCalibration(originalValley, originalPeak)
                    }
                }
            }
        )
    }

    private fun finishCalibration(originalValley: Float, originalPeak: Float) {
        sensorService.stopTracking()
        
        val avgValley = valleyList.average().toFloat()
        val avgPeak = peakList.average().toFloat()
        
        // Apply 70% tolerance multiplier
        val finalValley = avgValley * 0.7f
        val finalPeak = avgPeak * 0.7f
        
        // To be safe, ensure it doesn't get too close to 0 due to noise
        val safeValley = if (finalValley > -0.2f) -0.2f else finalValley
        val safePeak = if (finalPeak < 0.2f) 0.2f else finalPeak

        // Save to settings
        SettingsManager.setSquatValleyThreshold(safeValley)
        SettingsManager.setSquatPeakThreshold(safePeak)
        
        _calibratedSquats.value = 3
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
