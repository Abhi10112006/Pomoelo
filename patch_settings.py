import re

with open('./app/src/main/java/com/example/service/SettingsManager.kt', 'r') as f:
    content = f.read()

new_methods = """
    fun setHasAchievedFullProtection(context: Context, achieved: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_PROTECTION_ONBOARDING_DONE, achieved).apply()
    }

    fun hasAchievedFullProtection(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_PROTECTION_ONBOARDING_DONE, false)
    }
}
"""

content = content.replace("}\n}", "}\n" + new_methods)

with open('./app/src/main/java/com/example/service/SettingsManager.kt', 'w') as f:
    f.write(content)
