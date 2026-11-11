package com.example

import android.app.NotificationManager
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.example.service.InactivityReceiver
import com.example.service.SettingsManager
import com.example.service.TimerService
import com.example.service.safeParseColor
import com.example.service.toColor
import com.example.service.toLongValue
import com.example.ui.theme.ensureContrast
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("PomoPal", appName)
    }

    @Test
    fun `color parsing and contrast calculation never crash on zero or legacy values`() {
        val fallback = Color(0xFFF28F75)
        val cZero = safeParseColor(0L, fallback)
        assertEquals(1f, cZero.alpha, 0.001f)

        val cLegacyCompose = (0L).toColor(fallback)
        assertEquals(1f, cLegacyCompose.alpha, 0.001f)

        val cPacked = Color(0xFF81D4FA).value.toLong().toColor(fallback)
        assertEquals(1f, cPacked.alpha, 0.001f)

        val cUnsigned = Color(0xFF66BB6A).toLongValue().toColor(fallback)
        assertEquals(1f, cUnsigned.alpha, 0.001f)

        // Ensure ensureContrast never throws even if given translucent colors
        val adjusted = ensureContrast(Color.Transparent, Color.Transparent, minContrast = 4.5)
        assertEquals(1f, adjusted.alpha, 0.001f)
    }

    @Test
    fun `MainActivity launches cleanly and registers notification channels`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        SettingsManager.init(context)
        SettingsManager.setUserName("Alex")

        val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
        val activity = controller.get()
        assertNotNull(activity)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        assertNotNull(nm.getNotificationChannel(TimerService.CHANNEL_ID))
        assertNotNull(nm.getNotificationChannel(TimerService.COMPLETION_CHANNEL_ID))
        assertNotNull(nm.getNotificationChannel(InactivityReceiver.CHANNEL_ID))
        assertTrue(true)
    }
}

