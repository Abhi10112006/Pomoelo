package com.example.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

data class SessionCompletedData(
    val taskName: String,
    val isBreak: Boolean,
    val durationMinutes: Int,
    val startTime: Long,
    val endTime: Long
)

object TimerManager {
    enum class TimerState {
        STOPPED, RUNNING, PAUSED, BREAK
    }

    private val _timerState = MutableStateFlow(TimerState.STOPPED)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _isBreakMode = MutableStateFlow(false)
    val isBreakMode: StateFlow<Boolean> = _isBreakMode.asStateFlow()

    private val _timeRemainingSeconds = MutableStateFlow(25 * 60)
    val timeRemainingSeconds: StateFlow<Int> = _timeRemainingSeconds.asStateFlow()
    
    private val _currentTaskName = MutableStateFlow("Focus Time!")
    val currentTaskName: StateFlow<String> = _currentTaskName.asStateFlow()

    private val _currentQuote = MutableStateFlow("")
    val currentQuote: StateFlow<String> = _currentQuote.asStateFlow()

    private val _currentTaskId = MutableStateFlow(-1)
    val currentTaskId: StateFlow<Int> = _currentTaskId.asStateFlow()

    fun setCurrentQuote(quote: String) {
        _currentQuote.value = quote
    }

    private val _focusTimeSeconds = MutableStateFlow(25 * 60)
    val focusTimeSeconds: StateFlow<Int> = _focusTimeSeconds.asStateFlow()

    private val _breakTimeSeconds = MutableStateFlow(5 * 60)
    val breakTimeSeconds: StateFlow<Int> = _breakTimeSeconds.asStateFlow()

    private val _taskCompletedEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val taskCompletedEvent: SharedFlow<Int> = _taskCompletedEvent.asSharedFlow()

    private val _sessionCompletedEvent = MutableSharedFlow<SessionCompletedData>(extraBufferCapacity = 1)
    val sessionCompletedEvent: SharedFlow<SessionCompletedData> = _sessionCompletedEvent.asSharedFlow()

    fun updateState(state: TimerState) {
        _timerState.value = state
    }

    fun setBreakMode(isBreak: Boolean) {
        _isBreakMode.value = isBreak
    }

    fun updateTime(seconds: Int) {
        _timeRemainingSeconds.value = seconds
    }
    
    fun setTask(id: Int, name: String) {
        _currentTaskId.value = id
        _currentTaskName.value = name
    }

    fun setFocusTimeMins(mins: Int) {
        _focusTimeSeconds.value = mins * 60
        if (_timerState.value == TimerState.STOPPED && !_isBreakMode.value) {
            _timeRemainingSeconds.value = mins * 60
        }
    }

    fun setBreakTimeMins(mins: Int) {
        _breakTimeSeconds.value = mins * 60
        if (_timerState.value == TimerState.STOPPED && _isBreakMode.value) {
            _timeRemainingSeconds.value = mins * 60
        }
    }

    fun notifyTaskCompleted() {
        val taskId = _currentTaskId.value
        if (taskId != -1) {
            _taskCompletedEvent.tryEmit(taskId)
        }
    }

    fun notifySessionCompleted(taskName: String, isBreak: Boolean, durationMinutes: Int, startTime: Long, endTime: Long) {
        _sessionCompletedEvent.tryEmit(SessionCompletedData(taskName, isBreak, durationMinutes, startTime, endTime))
    }
}
