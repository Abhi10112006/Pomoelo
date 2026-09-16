import sys

with open('./app/src/main/java/com/example/ui/TimerViewModel.kt', 'r') as f:
    content = f.read()

target = """    private val _isSettingsOpen = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isSettingsOpen: StateFlow<Boolean> = _isSettingsOpen.asStateFlow()

    fun setSettingsOpen(isOpen: Boolean) {
        _isSettingsOpen.value = isOpen
    }"""

replacement = """    private val _isSettingsOpen = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isSettingsOpen: StateFlow<Boolean> = _isSettingsOpen.asStateFlow()
    
    private val _settingsTab = kotlinx.coroutines.flow.MutableStateFlow(0)
    val settingsTab: StateFlow<Int> = _settingsTab.asStateFlow()

    fun setSettingsOpen(isOpen: Boolean, tab: Int = 0) {
        if (isOpen) {
            _settingsTab.value = tab
        }
        _isSettingsOpen.value = isOpen
    }"""

content = content.replace(target, replacement)

with open('./app/src/main/java/com/example/ui/TimerViewModel.kt', 'w') as f:
    f.write(content)
