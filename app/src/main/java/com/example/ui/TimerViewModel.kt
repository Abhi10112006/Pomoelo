package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.TaskItem
import com.example.data.TimerSession
import com.example.service.TimerManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel(private val database: AppDatabase) : ViewModel() {
    
    val timerState = TimerManager.timerState
    val timeRemainingSeconds = TimerManager.timeRemainingSeconds
    val currentTaskName = TimerManager.currentTaskName

    private fun getStartOfToday(): Long {
        val cal = java.util.Calendar.getInstance()
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    init {
        viewModelScope.launch {
            TimerManager.taskCompletedEvent.collect { taskId ->
                database.taskDao().incrementPomodoroCount(taskId)
            }
        }

        viewModelScope.launch {
            TimerManager.sessionCompletedEvent.collect { data ->
                database.sessionDao().insertSession(
                    TimerSession(
                        taskName = data.taskName,
                        isBreak = data.isBreak,
                        durationMinutes = data.durationMinutes,
                        startTime = data.startTime,
                        endTime = data.endTime
                    )
                )
            }
        }
    }

    fun autoCleanupIfNeeded() {
        viewModelScope.launch {
            val startOfToday = getStartOfToday()
            database.taskDao().deleteTasksBefore(startOfToday)
        }
    }
    
    val allTasks = database.taskDao().getAllTasks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allSessions = database.sessionDao().getAllSessions().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun clearAllData() {
        viewModelScope.launch {
            database.sessionDao().deleteAllSessions()
            database.taskDao().deleteAllTasks()
        }
    }

    fun deleteSessionsInRange(startMillis: Long, endMillis: Long) {
        viewModelScope.launch {
            database.sessionDao().deleteSessionsInRange(startMillis, endMillis)
        }
    }

    val currentQuote: StateFlow<String> = TimerManager.currentQuote

    private val _isAddingTask = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isAddingTask: StateFlow<Boolean> = _isAddingTask.asStateFlow()

    fun setAddingTask(isAdding: Boolean) {
        _isAddingTask.value = isAdding
    }

    private val _isSettingsOpen = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isSettingsOpen: StateFlow<Boolean> = _isSettingsOpen.asStateFlow()

    fun setSettingsOpen(isOpen: Boolean) {
        _isSettingsOpen.value = isOpen
    }

    fun startTimer() {
        // Handled via Intent to Service in Activity, but update name first
    }

    fun setTask(id: Int, name: String) {
        TimerManager.setTask(id, name)
    }
    
    fun saveTask(taskName: String, categoryName: String, categoryColor: Long) {
        viewModelScope.launch {
            database.taskDao().insertTask(
                TaskItem(
                    name = taskName,
                    categoryName = categoryName,
                    categoryColor = categoryColor
                )
            )
        }
    }
    
    fun deleteTask(timerItem: TaskItem) {
        viewModelScope.launch {
            database.taskDao().deleteTaskById(timerItem.id)
        }
    }

    fun deleteSession(session: TimerSession) {
        viewModelScope.launch {
            database.sessionDao().deleteSession(session)
        }
    }

    fun deleteSessions(sessions: List<TimerSession>) {
        viewModelScope.launch {
            sessions.forEach {
                database.sessionDao().deleteSession(it)
            }
        }
    }
}
