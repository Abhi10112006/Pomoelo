package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.service.AppBlockerManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AppBlockerTest {

    @Test
    fun `test student motivation quotes exist and are uplifting`() {
        assertTrue("Expected at least 50 motivation quotes", AppBlockerManager.studentMotivations.size >= 50)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val quote = AppBlockerManager.getFreshMotivation(context)
        assertNotNull(quote)
        assertTrue(quote.isNotEmpty())
    }

    @Test
    fun `test record block attempts increases count`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val initialCount = AppBlockerManager.getBlockAttemptsToday(context)
        val newCount = AppBlockerManager.recordBlockAttempt(context, "com.instagram.android")
        assertEquals(initialCount + 1, newCount)
        assertEquals(newCount, AppBlockerManager.getBlockAttemptsToday(context))
    }

    @Test
    fun `test strict mode and vibration settings persist`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        AppBlockerManager.setStrictMode(context, true)
        assertTrue(AppBlockerManager.isStrictMode(context))
        AppBlockerManager.setStrictMode(context, false)
        assertTrue(!AppBlockerManager.isStrictMode(context))

        AppBlockerManager.setVibrateEnabled(context, false)
        assertTrue(!AppBlockerManager.isVibrateEnabled(context))
        AppBlockerManager.setVibrateEnabled(context, true)
        assertTrue(AppBlockerManager.isVibrateEnabled(context))
    }
}
