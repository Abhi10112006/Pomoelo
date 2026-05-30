package com.example.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import java.util.Random

class InactivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_INACTIVITY) {
            // Check if timer is currently running - if so, do not bother the user!
            if (TimerManager.timerState.value == TimerManager.TimerState.RUNNING) {
                return
            }

            // Post notification
            showInactivityNotification(context)
            
            // Auto schedule the next check for a fresh reminder (not repeated)
            InactivityScheduler.schedule(context)
        }
    }

    private fun showInactivityNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Ensure channel exists
        val channelId = "inactivity_reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Inactivity Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Friendly anti-scrolling and focus nudges"
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }
            manager.createNotificationChannel(channel)
        }

        // Get a fresh quote
        val message = getFreshInactivityQuote(context)

        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            101,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("🎯 Focus Reminder")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(R.drawable.ic_notification_timer)
            .setColor(android.graphics.Color.parseColor("#FF8A80"))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(NOTIFICATION_ID_INACTIVITY, notification)
    }

    companion object {
        const val ACTION_INACTIVITY = "com.example.action.INACTIVITY_ALERT"
        const val NOTIFICATION_ID_INACTIVITY = 4040

        private val inactivityQuotes = listOf(
            "Your future is getting fucked while you scroll like a dumbass.",
            "Competitors are grinding, tu yahan reels pe lund chus raha hai.",
            "Padhna toh padega, saale. Yeh dopamine ka chuttad mat ban.",
            "Every notification you open is another seat you’re kissing goodbye.",
            "Time waste kar raha hai? Result mein gaand phategi teri.",
            "Toppers are solving papers, you’re busy liking bullshit posts.",
            "Kal se padhunga attitude will fuck your entire life.",
            "Yeh phone tera baap nahi hai, iska gulam mat ban.",
            "You’re not resting, you’re just a weak addicted piece of shit.",
            "Exam hall mein yeh regret tere muh pe thappad marega.",
            "Har reel ek question banegi jo tujhe fail karegi.",
            "Aukaat nahi hai toh sapne dekhna band kar, chutiye.",
            "Your rank is dying every minute you waste on this garbage.",
            "Distractions ne teri life ki gaand maar li hai.",
            "Stop lying, motherfucker. You’re not studying at all.",
            "Yeh “thoda aur” wala scene tujhe road pe laayega.",
            "Future self already hates your guts for this bullshit.",
            "Clock nahi rukega, teri gaand pakad ke le jayega.",
            "Competitors ne 4 hours padh liye, tu abhi bhi chat pe hai. ",
            "Yeh addiction tera selection ka gandha murderer hai.",
            "You chose cheap dopamine over your fucking dream.",
            "Result aayega toh sirf rona aur “kash” hi bachega.",
            "Phone ne tujhe bewakoof bana diya hai, samajh le.",
            "Average log scroll karte hain. Winners unko maar dete hain.",
            "Tera time barbaad ho raha hai aur tu has raha hai.",
            "Jab paper khali rahega, yaad aayega yeh moment.",
            "Stop betraying yourself, you spineless fuck.",
            "Notifications tere life ka control le chuke hain.",
            "Yeh break 2 ghante ka ho gaya, ab ro mat.",
            "Sapne bade hain, lekin discipline zero. Pathetic.",
            "Har ghanta waste = ek attempt ki gaand.",
            "Tu nahi so raha, tu apni future ko maar raha hai.",
            "Reels dekh ke kya milega? Sirf regret aur zero rank.",
            "Your competitors don’t give a fuck about your excuses.",
            "Phone rakh nahi pa raha? Toh haar maan le saale.",
            "Yeh “chill” mode tujhe lifetime loser bana dega.",
            "Distraction free nahi hua toh selection bhi nahi hoga.",
            "Time is fucking running, and you’re standing still.",
            "Exam ke din yeh guilt tujhe andar se kha jayega.",
            "Stop wasting your parents’ money on this bullshit habit.",
            "Put the fucking phone down and lock in right now.",
            "Grind kar, warna zindagi bhar gaand marwa.",
            "Timer on. Distractions ko fuck off bol.",
            "Lock the fuck in or get destroyed in the exam.",
            "Padhle madarchod, baad mein rona mat.",
            "Competitors jaise padh, warna unke peeche hi reh.",
            "No more bullshit. Books khol aur shuru ho ja.",
            "Phone side kar, brain on kar, abhi.",
            "Fuck the notifications. Study like your life depends on it.",
            "Aukaat dikha saale. Beast mode ON.",
            "Every second you delay is another seat gone, motherfucker.",
            "Stop thinking. Start fucking grinding.",
            "Yeh session complete kar, baaki duniya baad mein.",
            "Tu topper banega ya chu**iya banega? Decide now.",
            "Lock in so hard your distractions start crying.",
            "Gaand mein dum hai toh timer laga aur padh.",
            "No more “bas ek video”. Pure focus, abhi.",
            "Competitors nahi soye, tu kyun apni gaand bacha raha hai?",
            "Padh le bhenchod, warna sapne chod de.",
            "Crush this session like you want to crush the competition.",
            "Distraction free. Zero bakchodi. Full savage mode.",
            "Abhi shuru kar, warna lifetime regret shuru hoga.",
            "Timer laga. World ko mute kar. Lock in.",
            "Be the ruthless student your future self will respect.",
            "Gaali khani hai ya selection jeetna hai? Choose fast.",
            "Stop scrolling, start destroying questions.",
            "Hard work kar, baaki sab bakchodi hai saale.",
            "Lock in so brutally that even apps get scared.",
            "Yeh moment tera hai. Waste mat kar, chutiye.",
            "Phone band kar. Brain ON. Let’s fucking destroy it.",
            "Competitors ko peeche chhod, ab tera time hai.",
            "No mercy for distractions. Study like a machine.",
            "Timer shuru. Mind locked. Future secured, motherfucker.",
            "Padh le warna zindagi bhar “kash” bolta rahega.",
            "Become fucking unstoppable. Start this session now.",
            "Distractions ko delete kar. Discipline ko fuck in.",
            "Abhi ka pain kal ka top rank banega.",
            "No half-assed shit. Full savage focus.",
            "Tu jeetega, lekin abhi action lena padega bhenchod.",
            "Lock in. Grind hard. Fuck the competition."
        )

        fun getFreshInactivityQuote(context: Context): String {
            val prefs = context.getSharedPreferences("inactivity_prefs", Context.MODE_PRIVATE)
            val lastQuoteIndex = prefs.getInt("last_quote_index", -1)
            
            // Find a different quote to ensure a fresh, non-repeated experience
            var attempts = 0
            var selectIndex = Random().nextInt(inactivityQuotes.size)
            while (selectIndex == lastQuoteIndex && attempts < 10) {
                selectIndex = Random().nextInt(inactivityQuotes.size)
                attempts++
            }
            
            prefs.edit().putInt("last_quote_index", selectIndex).apply()
            return inactivityQuotes[selectIndex]
        }
    }
}

object InactivityScheduler {
    
    // Default threshold is 30 minutes
    private const val INACTIVITY_DELAY_MS = 30 * 60 * 1000L

    fun schedule(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, InactivityReceiver::class.java).apply {
                action = InactivityReceiver.ACTION_INACTIVITY
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                202,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val triggerAt = System.currentTimeMillis() + INACTIVITY_DELAY_MS

            // Cancel previous if any
            try {
                alarmManager.cancel(pendingIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        triggerAt,
                        pendingIntent
                    )
                }
            } catch (e: SecurityException) {
                // Fallback for missing exact alarm permission on Android 12+ / 14+
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAt,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancel(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, InactivityReceiver::class.java).apply {
                action = InactivityReceiver.ACTION_INACTIVITY
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                202,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
