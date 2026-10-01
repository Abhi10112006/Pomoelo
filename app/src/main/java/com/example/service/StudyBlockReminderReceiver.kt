package com.example.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudyBlockReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "StudyBlockReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == StudyBlockReminderScheduler.ACTION_REMINDER) {
            val blockId = intent.getLongExtra("STUDY_BLOCK_ID", -1L)
            if (blockId == -1L) {
                Log.w(TAG, "Received study block reminder without valid blockId")
                return
            }

            val name = intent.getStringExtra("STUDY_BLOCK_NAME") ?: "Study Session"
            val description = intent.getStringExtra("STUDY_BLOCK_DESCRIPTION") ?: ""
            val colorLong = intent.getLongExtra("STUDY_BLOCK_COLOR", 0xFFF28F75L)
            val duration = intent.getIntExtra("STUDY_BLOCK_DURATION", 25)
            val repeatRule = intent.getStringExtra("STUDY_BLOCK_REPEAT") ?: "None"

            Log.d(TAG, "Posting notification for study block '$name' (id=$blockId, duration=$duration m)")

            StudyBlockReminderScheduler.ensureChannel(context)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationId = StudyBlockReminderScheduler.getNotificationId(blockId)

            // Primary tap intent -> Opens MainActivity and begins/focuses the study block
            val contentIntent = Intent(context, MainActivity::class.java).apply {
                action = StudyBlockReminderScheduler.ACTION_START_STUDY
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("STUDY_BLOCK_ID", blockId)
                putExtra("STUDY_BLOCK_NAME", name)
                putExtra("STUDY_BLOCK_COLOR", colorLong)
                putExtra("STUDY_BLOCK_DURATION", duration)
            }

            val pendingContentIntent = PendingIntent.getActivity(
                context,
                notificationId,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Secondary action intent -> View Study Sanctuary screen
            val viewIntent = Intent(context, MainActivity::class.java).apply {
                action = "com.example.action.VIEW_STUDY_BLOCKS"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("OPEN_TAB", "focus")
            }

            val pendingViewIntent = PendingIntent.getActivity(
                context,
                notificationId + 1,
                viewIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val colorArgb = (colorLong and 0xFFFFFFFFL).toInt()

            val shortDescription = if (description.isNotBlank()) {
                "$duration min • $description"
            } else {
                "$duration min focus ritual • Tap to start session"
            }

            val detailedBody = buildString {
                append("Your $duration-minute study ritual is ready to begin.")
                if (description.isNotBlank()) {
                    append("\nIntentions: ").append(description)
                }
                append("\nSettle in, eliminate distractions, and enter deep flow.")
            }

            val notificationBuilder = NotificationCompat.Builder(context, StudyBlockReminderScheduler.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_timer)
                .setColor(colorArgb)
                .setColorized(true)
                .setContentTitle("Time for $name ✨")
                .setContentText(shortDescription)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Time for $name ✨")
                        .setSummaryText("Study Sanctuary • ${duration}m")
                        .bigText(detailedBody)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setContentIntent(pendingContentIntent)
                .addAction(
                    R.drawable.ic_notification_timer,
                    "▶ Start Focus",
                    pendingContentIntent
                )
                .addAction(
                    R.drawable.ic_notification_timer,
                    "Open Sanctuary",
                    pendingViewIntent
                )

            notificationManager.notify(notificationId, notificationBuilder.build())

            // Reschedule or finalize depending on recurrence rule
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = DatabaseProvider.getDatabase(context.applicationContext)
                    val currentBlock = db.studyBlockDao().getStudyBlockById(blockId)
                    if (currentBlock != null) {
                        if (repeatRule != "None" && repeatRule != "Once" && currentBlock.reminderEnabled) {
                            // Reschedule next recurrence (Daily, Weekdays, Weekly)
                            StudyBlockReminderScheduler.schedule(context.applicationContext, currentBlock)
                        } else {
                            // One-shot reminder has delivered; turn toggle off in DB
                            db.studyBlockDao().updateStudyBlock(currentBlock.copy(reminderEnabled = false))
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error handling reminder post-action for block id=$blockId", e)
                }
            }
        }
    }
}
