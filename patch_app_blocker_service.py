import re

with open('./app/src/main/java/com/example/service/AppBlockerService.kt', 'r') as f:
    content = f.read()

companion = """    companion object {
        private val SYSTEM_EXCLUSIONS = setOf(
            "com.android.systemui",
            "android",
            "com.android.settings",
            "com.google.android.dialer",
            "com.android.phone",
            "com.android.server.telecom"
        )
        
        @Volatile
        var isServiceConnected: Boolean = false
            private set
            
        @Volatile
        var isServiceInterrupted: Boolean = false
            private set
    }"""

content = re.sub(r'\s*companion object \{.*?\n    }', '\n' + companion, content, flags=re.DOTALL)

interrupt = """    override fun onInterrupt() {
        isServiceInterrupted = true
    }"""

content = re.sub(r'    override fun onInterrupt\(\) \{.*?\n    \}', interrupt, content, flags=re.DOTALL)

service_connected = """    override fun onServiceConnected() {
        isServiceConnected = true
        isServiceInterrupted = false
        val info = AccessibilityServiceInfo().apply {"""

content = re.sub(r'    override fun onServiceConnected\(\) \{.*?val info = AccessibilityServiceInfo\(\)\.apply \{', service_connected, content, flags=re.DOTALL)

unbind = """    override fun onUnbind(intent: Intent?): Boolean {
        isServiceConnected = false
        isServiceInterrupted = false
        return super.onUnbind(intent)
    }
}"""

content = re.sub(r'\}\s*$', '\n' + unbind, content)

with open('./app/src/main/java/com/example/service/AppBlockerService.kt', 'w') as f:
    f.write(content)

