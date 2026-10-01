package com.example

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.example.data.DatabaseProvider
import com.example.data.StudyBlock
import com.example.service.StudyBlockReminderReceiver
import com.example.service.StudyBlockReminderScheduler
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class StudyBlockReminderTest {

    @Test
    fun testStableUniqueNotificationId() {
        val id1 = StudyBlockReminderScheduler.getNotificationId(1L)
        val id2 = StudyBlockReminderScheduler.getNotificationId(1L)
        val id3 = StudyBlockReminderScheduler.getNotificationId(2L)

        assertEquals("Same block ID must yield identical notification ID", id1, id2)
        assertNotEquals("Different block IDs must yield different notification IDs", id1, id3)
        assertTrue("Notification ID must be positive", id1 > 0)
    }

    @Test
    fun testCalculateNextTriggerTimeAlwaysInFuture() {
        val now = System.currentTimeMillis()
        val pastCal = Calendar.getInstance().apply {
            timeInMillis = now
            add(Calendar.HOUR_OF_DAY, -2) // 2 hours ago today
        }
        val pastTime = pastCal.timeInMillis

        // 1. "Once" / "None" in the past
        val nextOnce = StudyBlockReminderScheduler.calculateNextTriggerTime(pastTime, "None")
        assertTrue("Past 'Once' reminder must be safely projected into the future", nextOnce > now)

        // 2. "Daily"
        val nextDaily = StudyBlockReminderScheduler.calculateNextTriggerTime(pastTime, "Daily")
        assertTrue("Daily reminder must be in the future", nextDaily > now)
        assertTrue("Daily reminder must be within 25 hours from now", nextDaily - now <= 25 * 3600 * 1000L)

        // 3. "Weekdays"
        val nextWeekdays = StudyBlockReminderScheduler.calculateNextTriggerTime(pastTime, "Weekdays")
        assertTrue("Weekdays reminder must be in the future", nextWeekdays > now)
        val weekdayCal = Calendar.getInstance().apply { timeInMillis = nextWeekdays }
        val dayOfWeek = weekdayCal.get(Calendar.DAY_OF_WEEK)
        assertNotEquals("Weekdays reminder must never trigger on Saturday", Calendar.SATURDAY, dayOfWeek)
        assertNotEquals("Weekdays reminder must never trigger on Sunday", Calendar.SUNDAY, dayOfWeek)

        // 4. "Weekly"
        val nextWeekly = StudyBlockReminderScheduler.calculateNextTriggerTime(pastTime, "Weekly")
        assertTrue("Weekly reminder must be in the future", nextWeekly > now)
        val weeklyCal = Calendar.getInstance().apply { timeInMillis = nextWeekly }
        val expectedDay = pastCal.get(Calendar.DAY_OF_WEEK)
        assertEquals("Weekly reminder must match the day of week", expectedDay, weeklyCal.get(Calendar.DAY_OF_WEEK))
    }

    @Test
    fun testScheduleAndCancelReminder() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val block = StudyBlock(
            id = 42L,
            name = "Organic Chemistry",
            description = "Reaction mechanisms",
            color = 0xFFF28F75L,
            startTime = System.currentTimeMillis() + 3600000L,
            duration = 45,
            repeatRule = "Daily",
            reminderEnabled = true
        )

        // Schedule
        StudyBlockReminderScheduler.schedule(context, block)

        // Cancel
        StudyBlockReminderScheduler.cancel(context, block.id)
    }

    @Test
    fun testReminderReceiverHandlesIntentAndPostsNotification() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val receiver = StudyBlockReminderReceiver()

        val intent = Intent(StudyBlockReminderScheduler.ACTION_REMINDER).apply {
            putExtra("STUDY_BLOCK_ID", 101L)
            putExtra("STUDY_BLOCK_NAME", "Linear Algebra")
            putExtra("STUDY_BLOCK_DESCRIPTION", "Eigenvalues & Eigenvectors")
            putExtra("STUDY_BLOCK_COLOR", 0xFF8BB5CAL)
            putExtra("STUDY_BLOCK_DURATION", 60)
            putExtra("STUDY_BLOCK_REPEAT", "Daily")
        }

        receiver.onReceive(context, intent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = StudyBlockReminderScheduler.getNotificationId(101L)
        val shadowNotifications = notificationManager.activeNotifications
        val found = shadowNotifications.any { it.id == notificationId }
        assertTrue("Notification must be posted when reminder triggers", found)
    }
}
