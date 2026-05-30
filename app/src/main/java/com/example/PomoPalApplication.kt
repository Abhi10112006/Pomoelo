package com.example

import android.app.Application
import android.content.Context
import com.example.service.SettingsManager

class PomoPalApplication : Application() {
    companion object {
        private var instance: PomoPalApplication? = null

        fun getContext(): Context {
            return instance?.applicationContext 
                ?: throw IllegalStateException("PomoPalApplication not initialized yet.")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Initialize settings manager as early as possible
        SettingsManager.init(this)
    }
}
