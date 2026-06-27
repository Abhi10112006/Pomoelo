package com.example

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object GoogleDriveManager {
    private const val TAG = "GoogleDriveManager"

    suspend fun backupData(context: Context, account: GoogleSignInAccount, data: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Mocking the backup process for now to avoid SDK issues
                delay(1500)
                Log.d(TAG, "Mock Backup successful")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Backup failed: \${e.message}")
                false
            }
        }
    }
}
