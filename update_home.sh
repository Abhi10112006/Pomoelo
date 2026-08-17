sed -i '/LaunchedEffect(Unit) {/!b;n;c\
        viewModel.autoCleanupIfNeeded()\
        val lastBackup = com.example.service.SettingsManager.getLastBackupTime()\
        val backupFreq = com.example.service.SettingsManager.getAutoBackupFreq()\
        val freqMs = when (backupFreq) { 1 -> 24L * 60 * 60 * 1000L; 2 -> 7L * 24 * 60 * 60 * 1000L; else -> 0L }\
        if (freqMs > 0 && System.currentTimeMillis() - lastBackup > freqMs) {\
            val account = com.example.GoogleAuthManager.getLastSignedInAccount(context)\
            if (account != null) {\
                kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {\
                    val success = com.example.GoogleDriveManager.backupData(context, account, "auto-backup")\
                    if (success) com.example.service.SettingsManager.setLastBackupTime(System.currentTimeMillis())\
                }\
            }\
        }
' app/src/main/java/com/example/MainActivity.kt
