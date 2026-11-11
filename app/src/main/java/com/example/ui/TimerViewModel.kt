package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.TaskItem
import com.example.data.TimerSession
import com.example.service.TimerManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
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

    fun autoCleanupIfNeeded() {
        viewModelScope.launch {
            try {
                val startOfToday = getStartOfToday()
                database.taskDao().deleteTasksBefore(startOfToday)
            } catch (_: Throwable) {}
        }
    }
    
    val allTasks = database.taskDao().getAllTasks()
        .catch { emit(emptyList()) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val allSessions = database.sessionDao().getAllSessions()
        .catch { emit(emptyList()) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun clearAllData() {
        viewModelScope.launch {
            try {
                database.sessionDao().deleteAllSessions()
                database.taskDao().deleteAllTasks()
            } catch (_: Throwable) {}
        }
    }

    fun deleteSessionsInRange(startMillis: Long, endMillis: Long) {
        viewModelScope.launch {
            try {
                database.sessionDao().deleteSessionsInRange(startMillis, endMillis)
            } catch (_: Throwable) {}
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
    
    private val _settingsTab = kotlinx.coroutines.flow.MutableStateFlow(0)
    val settingsTab: StateFlow<Int> = _settingsTab.asStateFlow()

    fun setSettingsOpen(isOpen: Boolean, tab: Int = 0) {
        if (isOpen) {
            _settingsTab.value = tab
        }
        _isSettingsOpen.value = isOpen
    }

    fun startTimer() {
        // Handled via Intent to Service in Activity, but update name first
    }

    fun setTask(id: Int, name: String, color: Long? = null) {
        TimerManager.setTask(id, name, color)
    }
    
    fun saveTask(taskName: String, categoryName: String, categoryColor: Long, onSaved: ((Int) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val id = database.taskDao().insertTask(
                    TaskItem(
                        name = taskName,
                        categoryName = categoryName,
                        categoryColor = categoryColor
                    )
                )
                onSaved?.invoke(id.toInt())
            } catch (_: Throwable) {}
        }
    }
    
    fun deleteTask(timerItem: TaskItem) {
        viewModelScope.launch {
            try {
                database.taskDao().deleteTaskById(timerItem.id)
                if (TimerManager.currentTaskId.value == timerItem.id && TimerManager.timerState.value == TimerManager.TimerState.STOPPED) {
                    TimerManager.setTask(-1, "Focus Time!")
                }
            } catch (_: Throwable) {}
        }
    }

    fun deleteSession(session: TimerSession) {
        viewModelScope.launch {
            try {
                database.sessionDao().deleteSession(session)
            } catch (_: Throwable) {}
        }
    }

    fun deleteSessions(sessions: List<TimerSession>) {
        viewModelScope.launch {
            try {
                sessions.forEach {
                    database.sessionDao().deleteSession(it)
                }
            } catch (_: Throwable) {}
        }
    }
}

