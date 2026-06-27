package com.example

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object CloudBackendManager {
    private const val TAG = "CloudBackendManager"
    
    // Lazy init for Firestore
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun initialize(context: Context) {
        try {
            // Subscribe to cloud notifications topic for app-wide updates
            FirebaseMessaging.getInstance().subscribeToTopic("global_updates")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Subscribed to live notifications.")
                    } else {
                        Log.e(TAG, "Live notifications subscription failed.")
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization error: ${e.message}")
        }
    }

    suspend fun saveUserData(userId: String, userData: Map<String, Any>): Boolean {
        return try {
            firestore.collection("users").document(userId).set(userData).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user data: ${e.message}")
            false
        }
    }

    suspend fun getAppUpdateInfo(): Map<String, Any>? {
        return try {
            val doc = firestore.collection("app_config").document("update_info").get().await()
            if (doc.exists()) {
                doc.data
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get app update info: ${e.message}")
            null
        }
    }
}
