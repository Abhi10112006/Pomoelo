package com.example

import android.util.Log

object UpdateChecker {
    private const val CURRENT_VERSION = "2.0"

    suspend fun checkForUpdates(): String? {
        try {
            val updateInfo = CloudBackendManager.getAppUpdateInfo()
            if (updateInfo != null) {
                val latestVersion = updateInfo["version"] as? String
                val downloadUrl = updateInfo["download_url"] as? String
                
                if (latestVersion != null && latestVersion != CURRENT_VERSION) {
                    return downloadUrl
                }
            }
        } catch (e: Throwable) {
            Log.e("UpdateChecker", "Failed to check for updates: ${e.message}")
        }
        return null
    }
}
