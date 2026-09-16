package com.example.ui

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
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



enum class PermissionState {
    READY,
    ACTION_REQUIRED,
    UNKNOWN
}

data class SystemProtectionStates(
    val notificationsState: PermissionState,
    val batteryState: PermissionState,
    val dndState: PermissionState
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
    
    androidx.compose.runtime.LaunchedEffect(notificationsState, batteryState, dndState) {
        val becameReady = 
            (prevNotif != PermissionState.READY && notificationsState == PermissionState.READY) ||
            (prevBattery != PermissionState.READY && batteryState == PermissionState.READY) ||
            (prevDnd != PermissionState.READY && dndState == PermissionState.READY)
            
        if (becameReady) {
            try { view.performHapticFeedback(16) } catch (e: Exception) {}
        }
        
        prevNotif = notificationsState
        prevBattery = batteryState
        prevDnd = dndState
    }

    return SystemProtectionStates(notificationsState, batteryState, dndState)
}


@Composable
fun SystemProtectionBanner(
    state: SystemProtectionStates,
    onFixClicked: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    if (state.needsAttention) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(currentTheme.surface)
                .border(1.dp, Color(0xFFE53935).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .clickable { onFixClicked() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = null,
                tint = Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "System protection needs attention",
                    fontSize = 13.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
            }
            Text(
                text = "Fix",
                fontSize = 13.scaledSp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE53935)
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
    
    val title: String
    val desc: String
    val actionId: String
    
    when {
        state.notificationsState == PermissionState.ACTION_REQUIRED -> {
            title = "PomoPal needs notification access"
            desc = "Allow notifications so PomoPal can let you know when your focus session is complete."
            actionId = "NOTIFICATIONS"
        }
        state.dndState == PermissionState.ACTION_REQUIRED -> {
            title = "PomoPal needs Do Not Disturb access"
            desc = "Allow Do Not Disturb access so PomoPal can silence distractions during your focus session."
            actionId = "DND"
        }
        state.batteryState == PermissionState.ACTION_REQUIRED -> {
            title = "PomoPal needs background reliability"
            desc = "Disable battery optimization so your timer doesn't stop unexpectedly when the screen is off."
            actionId = "BATTERY"
        }
        else -> return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
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

