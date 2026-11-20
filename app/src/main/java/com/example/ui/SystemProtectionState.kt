package com.example.ui

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.scaledSp
import com.example.ui.theme.LocalAppTheme
import com.example.service.SettingsManager
import androidx.compose.material.icons.rounded.Security



enum class PermissionState {
    READY,
    ACTION_REQUIRED,
    UNKNOWN
}

data class SystemProtectionStates(
    val notificationsState: PermissionState,
    val batteryState: PermissionState,
    val dndState: PermissionState,
    val liveUpdatesState: PermissionState = PermissionState.UNKNOWN
) {
    val needsAttention: Boolean
        get() = notificationsState == PermissionState.ACTION_REQUIRED ||
                batteryState == PermissionState.ACTION_REQUIRED ||
                dndState == PermissionState.ACTION_REQUIRED
}

@Composable
fun rememberSystemProtectionStates(): SystemProtectionStates {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var notificationsState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    var batteryState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    var dndState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    var liveUpdatesState by remember { mutableStateOf(PermissionState.UNKNOWN) }

    val checkPermissions = {
        notificationsState = try {
            val notificationManagerCompat = androidx.core.app.NotificationManagerCompat.from(context)
            if (notificationManagerCompat.areNotificationsEnabled()) PermissionState.READY else PermissionState.ACTION_REQUIRED
        } catch (e: Exception) {
            PermissionState.UNKNOWN
        }

        batteryState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
                if (powerManager.isIgnoringBatteryOptimizations(context.packageName)) PermissionState.READY else PermissionState.ACTION_REQUIRED
            } catch (e: Exception) {
                PermissionState.UNKNOWN
            }
        } else {
            PermissionState.UNKNOWN
        }

        dndState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                if (systemNotificationManager.isNotificationPolicyAccessGranted) PermissionState.READY else PermissionState.ACTION_REQUIRED
            } catch (e: Exception) {
                PermissionState.UNKNOWN
            }
        } else {
            PermissionState.UNKNOWN
        }

        liveUpdatesState = if (Build.VERSION.SDK_INT >= 36) {
            try {
                val systemNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                if (systemNotificationManager.canPostPromotedNotifications()) PermissionState.READY else PermissionState.ACTION_REQUIRED
            } catch (e: Exception) {
                PermissionState.UNKNOWN
            }
        } else {
            PermissionState.UNKNOWN
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME || event == Lifecycle.Event.ON_START) {
                checkPermissions()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        checkPermissions() // Initial check
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val view = androidx.compose.ui.platform.LocalView.current
    
    // Premium feedback when state changes to READY
    var prevNotif by remember { mutableStateOf(notificationsState) }
    var prevBattery by remember { mutableStateOf(batteryState) }
    var prevDnd by remember { mutableStateOf(dndState) }
    var prevLiveUpdates by remember { mutableStateOf(liveUpdatesState) }
    
    androidx.compose.runtime.LaunchedEffect(notificationsState, batteryState, dndState, liveUpdatesState) {
        val becameReady = 
            (prevNotif != PermissionState.READY && notificationsState == PermissionState.READY) ||
            (prevBattery != PermissionState.READY && batteryState == PermissionState.READY) ||
            (prevDnd != PermissionState.READY && dndState == PermissionState.READY) ||
            (prevLiveUpdates != PermissionState.READY && liveUpdatesState == PermissionState.READY)
            
        if (becameReady) {
            try { view.performHapticFeedback(16) } catch (e: Exception) {}
        }
        
        prevNotif = notificationsState
        prevBattery = batteryState
        prevDnd = dndState
        prevLiveUpdates = liveUpdatesState
    }

    return SystemProtectionStates(notificationsState, batteryState, dndState, liveUpdatesState)
}


@Composable
fun SystemProtectionBanner(
    state: SystemProtectionStates,
    onFixClicked: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val context = LocalContext.current
    var hasAchievedFullProtection by remember {
        mutableStateOf(SettingsManager.hasAchievedFullProtection(context))
    }

    LaunchedEffect(state.needsAttention) {
        if (!state.needsAttention && !hasAchievedFullProtection) {
            hasAchievedFullProtection = true
            SettingsManager.setHasAchievedFullProtection(context, true)
        }
    }

    var showBanner by remember { mutableStateOf(false) }

    LaunchedEffect(hasAchievedFullProtection, state.needsAttention) {
        if (state.needsAttention) {
            if (!hasAchievedFullProtection) {
                kotlinx.coroutines.delay(2500)
                showBanner = true
            } else {
                showBanner = true
            }
        } else {
            showBanner = false
        }
    }

    androidx.compose.animation.AnimatedVisibility(
        visible = showBanner && state.needsAttention,
        enter = androidx.compose.animation.expandVertically() + androidx.compose.animation.fadeIn(),
        exit = androidx.compose.animation.shrinkVertically() + androidx.compose.animation.fadeOut()
    ) {
        val isFirstTime = !hasAchievedFullProtection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isFirstTime) currentTheme.primary.copy(alpha = 0.1f) else currentTheme.surface)
                .border(1.dp, if (isFirstTime) currentTheme.primary.copy(alpha = 0.3f) else Color(0xFFE53935).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable { onFixClicked() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isFirstTime) Icons.Rounded.Security else Icons.Rounded.Warning,
                contentDescription = null,
                tint = if (isFirstTime) currentTheme.primary else Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isFirstTime) "Supercharge your focus" else "System protection needs attention",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
                if (isFirstTime) {
                    Text(
                        text = "Set up protection to block distractions",
                        fontSize = 11.scaledSp,
                        color = currentTheme.textSecondary
                    )
                }
            }
            Text(
                text = if (isFirstTime) "Set Up" else "Fix",
                fontSize = 13.scaledSp,
                fontWeight = FontWeight.Bold,
                color = if (isFirstTime) currentTheme.primary else Color(0xFFE53935)
            )
        }
    }
}

@Composable
fun ContextualSetupMessage(
    state: SystemProtectionStates,
    onSetup: (String) -> Unit
) {
    val currentTheme = LocalAppTheme.current
    
    val items = mutableListOf<Triple<String, String, String>>()
    if (state.notificationsState == PermissionState.ACTION_REQUIRED) {
        items.add(Triple("PomoPal needs notification access", "Allow notifications so PomoPal can let you know when your focus session is complete.", "NOTIFICATIONS"))
    }
    if (state.dndState == PermissionState.ACTION_REQUIRED) {
        items.add(Triple("PomoPal needs Do Not Disturb access", "Allow Do Not Disturb access so PomoPal can silence distractions during your focus session.", "DND"))
    }
    if (state.batteryState == PermissionState.ACTION_REQUIRED) {
        items.add(Triple("PomoPal needs background reliability", "Disable battery optimization so your timer doesn't stop unexpectedly when the screen is off.", "BATTERY"))
    }

    Column {
        items.forEach { (title, desc, actionId) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (currentTheme.isDark) Color(0xFF3E2723) else Color(0xFFFFF3E0))
                    .border(1.dp, if (currentTheme.isDark) Color(0xFF5D4037) else Color(0xFFFFCC80), RoundedCornerShape(12.dp))
                    .clickable { onSetup(actionId) }
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = if (currentTheme.isDark) Color(0xFFFFB74D) else Color(0xFFF57C00),
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 13.scaledSp,
                        fontWeight = FontWeight.Bold,
                        color = if (currentTheme.isDark) Color(0xFFFFE0B2) else Color(0xFFE65100)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        fontSize = 11.scaledSp,
                        color = if (currentTheme.isDark) Color(0xFFFFCC80) else Color(0xFFEF6C00),
                        lineHeight = 16.scaledSp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Set Up",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentTheme.isDark) Color(0xFFFFB74D) else Color(0xFFF57C00),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}