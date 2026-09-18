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

    fun injectMockData() {
        viewModelScope.launch {
            val baseTime = System.currentTimeMillis()
            val tasks = listOf(
                TaskItem(name = "Design Mockups", categoryName = "Work", categoryColor = 0xFF3F51B5, timestamp = baseTime),
                TaskItem(name = "Fix Bug #142", categoryName = "Coding", categoryColor = 0xFFF44336, timestamp = baseTime),
                TaskItem(name = "Design Mockups", categoryName = "Freelance", categoryColor = 0xFF4CAF50, timestamp = baseTime), // same name diff color
                TaskItem(name = "Read Docs", categoryName = "Learning", categoryColor = 0xFFFFC107, timestamp = baseTime)
            )
            tasks.forEach { database.taskDao().insertTask(it) }
            
            val sessions = listOf(
                TimerSession(taskName = "Legacy Task", isBreak = false, durationMinutes = 25, startTime = baseTime - 86400000 * 4, endTime = baseTime - 86400000 * 4 + 1500000, taskColor = null),
                TimerSession(taskName = "Break", isBreak = true, durationMinutes = 5, startTime = baseTime - 86400000 * 4 + 1500000, endTime = baseTime - 86400000 * 4 + 1800000, taskColor = null),
                TimerSession(taskName = "Design Mockups", isBreak = false, durationMinutes = 25, startTime = baseTime - 86400000 * 2, endTime = baseTime - 86400000 * 2 + 1500000, taskColor = 0xFF3F51B5),
                TimerSession(taskName = "Fix Bug #142", isBreak = false, durationMinutes = 45, startTime = baseTime - 86400000 * 1, endTime = baseTime - 86400000 * 1 + 2700000, taskColor = 0xFFF44336),
                TimerSession(taskName = "Design Mockups", isBreak = false, durationMinutes = 30, startTime = baseTime - 3600000, endTime = baseTime - 3600000 + 1800000, taskColor = 0xFF4CAF50),
                TimerSession(taskName = "Break", isBreak = true, durationMinutes = 10, startTime = baseTime - 1800000, endTime = baseTime - 1800000 + 600000, taskColor = null)
            )
            sessions.forEach { database.sessionDao().insertSession(it) }
        }
    }

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
            val id = database.taskDao().insertTask(
                TaskItem(
                    name = taskName,
                    categoryName = categoryName,
                    categoryColor = categoryColor
                )
            )
            onSaved?.invoke(id.toInt())
        }
    }
    
    fun deleteTask(timerItem: TaskItem) {
        viewModelScope.launch {
            database.taskDao().deleteTaskById(timerItem.id)
            if (TimerManager.currentTaskId.value == timerItem.id && TimerManager.timerState.value == TimerManager.TimerState.STOPPED) {
                TimerManager.setTask(-1, "Focus Time!")
            }
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
