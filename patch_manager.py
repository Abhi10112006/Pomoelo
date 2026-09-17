import re

with open('./app/src/main/java/com/example/service/AppBlockerManager.kt', 'r') as f:
    content = f.read()

new_logic = """    enum class AccessibilityServiceStatus {
        DISABLED,
        READY,
        STALE,
        UNKNOWN
    }

    fun getAccessibilityStatus(context: Context): AccessibilityServiceStatus {
        try {
            val isEnabled = com.example.ui.isAccessibilityServiceEnabled(context)
            if (!isEnabled) {
                return AccessibilityServiceStatus.DISABLED
            }
            if (AppBlockerService.isServiceInterrupted) {
                return AccessibilityServiceStatus.STALE
            }
            if (AppBlockerService.isServiceConnected) {
                return AccessibilityServiceStatus.READY
            }
            return AccessibilityServiceStatus.STALE
        } catch (e: Exception) {
            return AccessibilityServiceStatus.UNKNOWN
        }
    }
"""

content = content.replace("    private fun getPrefs", new_logic + "\n    private fun getPrefs")

with open('./app/src/main/java/com/example/service/AppBlockerManager.kt', 'w') as f:
    f.write(content)
