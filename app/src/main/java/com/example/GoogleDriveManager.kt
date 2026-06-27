package com.example

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GoogleDriveManager {
    private const val TAG = "GoogleDriveManager"

    fun getDriveService(context: Context, account: GoogleSignInAccount): Drive {
        val credential = GoogleAccountCredential.usingOAuth2(
            context, listOf(DriveScopes.DRIVE_FILE)
        )
        credential.selectedAccount = account.account

        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("PomoPal")
            .build()
    }

    suspend fun backupData(context: Context, account: GoogleSignInAccount, data: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val driveService = getDriveService(context, account)
                
                // For simplicity, we just create a file
                val fileMetadata = com.google.api.services.drive.model.File()
                fileMetadata.name = "pomopal_backup.json"
                
                val mediaContent = com.google.api.client.http.ByteArrayContent.fromString(
                    "application/json", data
                )
                
                driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute()
                    
                Log.d(TAG, "Backup successful")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Backup failed: \${e.message}")
                false
            }
        }
    }
}
