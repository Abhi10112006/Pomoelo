package com.example

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object UpdateChecker {
    private const val CURRENT_VERSION = "2.0"
    private const val GITHUB_VERSION_URL = "https://raw.githubusercontent.com/Abhi10112006/Pomoelo/main/version.json"

    suspend fun checkForUpdates(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val jsonStr = URL(GITHUB_VERSION_URL).readText()
                val json = JSONObject(jsonStr)
                val latestVersion = json.getString("version")
                val downloadUrl = json.getString("download_url")
                
                if (latestVersion != CURRENT_VERSION) {
                    return@withContext downloadUrl
                }
            } catch (e: Throwable) {
                // Return null if request fails or there's no update
            }
            null
        }
    }
}
