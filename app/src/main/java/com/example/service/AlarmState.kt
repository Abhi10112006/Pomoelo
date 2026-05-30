package com.example.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AlarmState {
    var isAlarmRinging by mutableStateOf(false)
    var activeAlarmId by mutableStateOf(-1)
    var activeAlarmSquatTarget by mutableStateOf(10)
    var activeAlarmLabel by mutableStateOf("Workout Alarm")
    var activeAlarmSquatCount by mutableStateOf(0)
}
