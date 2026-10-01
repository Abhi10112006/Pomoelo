package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.StudyBlock
import com.example.data.StudyBlockRepository
import com.example.data.TaskItem
import com.example.data.TimerSession
import com.example.service.TimerManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel(private val database: AppDatabase) : ViewModel() {
    
    val studyBlockRepository = StudyBlockRepository(
        studyBlockDao = database.studyBlockDao(),
        sessionDao = database.sessionDao()
    )

    val allStudyBlocks: StateFlow<List<StudyBlock>> = studyBlockRepository.allStudyBlocks.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

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

    fun createStudyBlock(
        name: String,
        description: String = "",
        color: Long,
        startTime: Long = 0L,
        duration: Int = 25,
        repeatRule: String? = null,
        reminderEnabled: Boolean = false,
        onCreated: ((Long) -> Unit)? = null
    ) {
        viewModelScope.launch {
            val block = StudyBlock(
                name = name,
                description = description,
                color = color,
                startTime = startTime,
                duration = duration,
                repeatRule = repeatRule,
                reminderEnabled = reminderEnabled
            )
            val id = studyBlockRepository.create(block)
            val created = block.copy(id = id)
            try {
                com.example.service.StudyBlockReminderScheduler.schedule(
                    com.example.PomoPalApplication.getContext(),
                    created
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            onCreated?.invoke(id)
        }
    }

    fun updateStudyBlock(studyBlock: StudyBlock) {
        viewModelScope.launch {
            studyBlockRepository.update(studyBlock)
            try {
                com.example.service.StudyBlockReminderScheduler.schedule(
                    com.example.PomoPalApplication.getContext(),
                    studyBlock
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteStudyBlock(studyBlock: StudyBlock) {
        viewModelScope.launch {
            try {
                com.example.service.StudyBlockReminderScheduler.cancel(
                    com.example.PomoPalApplication.getContext(),
                    studyBlock.id
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            studyBlockRepository.delete(studyBlock)
        }
    }

    fun deleteStudyBlockById(id: Long) {
        viewModelScope.launch {
            try {
                com.example.service.StudyBlockReminderScheduler.cancel(
                    com.example.PomoPalApplication.getContext(),
                    id
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            studyBlockRepository.deleteById(id)
        }
    }

    fun getStudyBlockById(id: Long, onResult: (StudyBlock?) -> Unit) {
        viewModelScope.launch {
            val block = studyBlockRepository.getById(id)
            onResult(block)
        }
    }

    fun startSessionFromStudyBlock(studyBlock: StudyBlock, sessionType: String = "Focus") {
        viewModelScope.launch {
            TimerManager.setFocusTimeMins(studyBlock.duration)
            TimerManager.setTask(
                id = studyBlock.id.toInt(),
                name = studyBlock.name,
                color = studyBlock.color
            )
            studyBlockRepository.startSessionFromStudyBlock(studyBlock, sessionType)
        }
    }

    fun createQuickFocusSession(
        durationMinutes: Int = 25,
        name: String = "Quick Focus",
        color: Long? = null
    ) {
        viewModelScope.launch {
            TimerManager.setFocusTimeMins(durationMinutes)
            TimerManager.setTask(
                id = -1,
                name = name,
                color = color
            )
            studyBlockRepository.createQuickFocusSession(
                name = name,
                durationMinutes = durationMinutes,
                color = color
            )
        }
    }
}
