import sys

with open('./app/src/main/java/com/example/ui/SystemProtectionScreen.kt', 'w') as f:
    f.write("""package com.example.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.ui.components.scaledSp
import com.example.ui.theme.LocalAppTheme

enum class PermissionState {
    READY,
    ACTION_REQUIRED,
    UNKNOWN
}

@Composable
fun SystemProtectionScreen() {
    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val currentTheme = LocalAppTheme.current
    
    var notificationsState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    var batteryState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    var dndState by remember { mutableStateOf(PermissionState.UNKNOWN) }
    
    val checkPermissions = {
        // Check Notifications
        notificationsState = try {
            val notificationManagerCompat = androidx.core.app.NotificationManagerCompat.from(context)
            if (notificationManagerCompat.areNotificationsEnabled()) PermissionState.READY else PermissionState.ACTION_REQUIRED
        } catch (e: Exception) {
            PermissionState.UNKNOWN
        }

        // Check Battery
        batteryState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
                if (powerManager.isIgnoringBatteryOptimizations(context.packageName)) PermissionState.READY else PermissionState.ACTION_REQUIRED
            } catch (e: Exception) {
                PermissionState.UNKNOWN
            }
        } else {
            PermissionState.UNKNOWN // Unknown on older devices
        }

        // Check DND
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
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Refresh on composition
    LaunchedEffect(Unit) {
        checkPermissions()
    }
    
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    // Prepare data for UI
    val states = listOf(notificationsState, batteryState, dndState)
    val readyCount = states.count { it == PermissionState.READY }
    val requiredCount = states.count { it == PermissionState.ACTION_REQUIRED }
    val applicableCount = states.count { it != PermissionState.UNKNOWN }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + slideInVertically(tween(500, easing = FastOutSlowInEasing), initialOffsetY = { it / 4 })
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overall Status Header
            OverallStatusCard(readyCount = readyCount, applicableCount = applicableCount, requiredCount = requiredCount)

            // Intro text
            Text(
                text = "These settings ensure PomoPal can wake up your screen, bypass battery killers, and block distracting notifications when your timer finishes.",
                fontSize = 12.scaledSp,
                color = currentTheme.textSecondary,
                lineHeight = 16.scaledSp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // Cards
            ProtectionCard(
                title = "Notification Delivery",
                description = "Allows PomoPal to show full-screen alerts when your session completes.",
                icon = Icons.Rounded.Notifications,
                state = notificationsState,
                onClick = {
                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                    val intent = Intent().apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        } else {
                            action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            putExtra("app_package", context.packageName)
                            putExtra("app_uid", context.applicationInfo.uid)
                        }
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "Notification settings unavailable", Toast.LENGTH_SHORT).show()
                    } catch (e: SecurityException) {
                        Toast.makeText(context, "Permission denied to open settings", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Unable to open settings", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            ProtectionCard(
                title = "Background Reliability",
                description = "Allow unrestricted usage for better performance. Prevents the system from silently killing your timer.",
                icon = Icons.Rounded.BatteryStd,
                state = batteryState,
                onClick = {
                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val intent = Intent().apply {
                            val pm = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
                            if (pm.isIgnoringBatteryOptimizations(context.packageName)) {
                                action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
                            } else {
                                action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                                data = android.net.Uri.parse("package:${context.packageName}")
                            }
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "Battery settings unavailable on this device", Toast.LENGTH_SHORT).show()
                        } catch (e: SecurityException) {
                            Toast.makeText(context, "Permission denied to open battery settings", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Unable to open battery settings", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Not required on this Android version", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            ProtectionCard(
                title = "Deep Focus (DND)",
                description = "Requires Notification Policy Access to silence incoming calls and app notifications during a session.",
                icon = Icons.Rounded.DoNotDisturbOn,
                state = dndState,
                onClick = {
                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "DND settings unavailable on this device", Toast.LENGTH_SHORT).show()
                        } catch (e: SecurityException) {
                            Toast.makeText(context, "Permission denied to open DND settings", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Unable to open DND settings", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Not required on this Android version", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            if (dndState == PermissionState.ACTION_REQUIRED) {
                SideloadedAppTip()
            }
        }
    }
}

@Composable
fun OverallStatusCard(readyCount: Int, applicableCount: Int, requiredCount: Int) {
    val currentTheme = LocalAppTheme.current
    val allGranted = requiredCount == 0 && applicableCount > 0
    val bgColor = if (allGranted) Color(0xFF4CAF50).copy(alpha = 0.15f) else currentTheme.primary.copy(alpha = 0.15f)
    val contentColor = if (allGranted) Color(0xFF388E3C) else currentTheme.primary

    val statusTitle = if (allGranted) "You're all set" else "Almost there"
    val statusText = if (allGranted) {
        if (applicableCount == 0) "Status unknown" else "$readyCount of $applicableCount protections ready"
    } else {
        "$requiredCount of $applicableCount protections need attention"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(contentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (allGranted) Icons.Rounded.VerifiedUser else Icons.Rounded.Shield,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = statusTitle,
                fontSize = 16.scaledSp,
                fontWeight = FontWeight.Bold,
                color = currentTheme.textPrimary
            )
            Text(
                text = statusText,
                fontSize = 12.scaledSp,
                color = contentColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProtectionCard(
    title: String,
    description: String,
    icon: ImageVector,
    state: PermissionState,
    onClick: () -> Unit
) {
    val currentTheme = LocalAppTheme.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val isGranted = state == PermissionState.READY
    val isUnknown = state == PermissionState.UNKNOWN

    val containerColor by animateColorAsState(
        targetValue = if (isGranted) currentTheme.surface else currentTheme.background,
        animationSpec = tween(300)
    )
    val borderColor by animateColorAsState(
        targetValue = if (isGranted) Color(0xFF4CAF50).copy(alpha = 0.5f) else currentTheme.cardBorder,
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(color = currentTheme.primary),
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isGranted) Color(0xFF4CAF50).copy(alpha = 0.15f) else currentTheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isGranted) Color(0xFF4CAF50) else currentTheme.textSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 11.scaledSp,
                    color = currentTheme.textSecondary,
                    lineHeight = 15.scaledSp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            isGranted -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                            isUnknown -> currentTheme.surface
                            else -> currentTheme.primary
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        isGranted -> "Ready"
                        isUnknown -> "Unknown"
                        else -> "Open"
                    },
                    fontSize = 12.scaledSp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isGranted -> Color(0xFF388E3C)
                        isUnknown -> currentTheme.textSecondary
                        else -> Color.White
                    }
                )
            }
        }
    }
}

@Composable
fun SideloadedAppTip() {
    val currentTheme = LocalAppTheme.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (currentTheme.isDark) Color(0xFF3E2723) else Color(0xFFFFF3E0))
            .border(1.dp, if (currentTheme.isDark) Color(0xFF5D4037) else Color(0xFFFFCC80), RoundedCornerShape(12.dp))
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
        Column {
            Text(
                text = "Missing PomoPal in DND list?",
                fontSize = 13.scaledSp,
                fontWeight = FontWeight.Bold,
                color = if (currentTheme.isDark) Color(0xFFFFE0B2) else Color(0xFFE65100)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Android 13+ restricts sideloaded apps. Go to phone Settings > Apps > PomoPal. Tap the (⋮) menu in the top-right and select 'Allow restricted settings'.",
                fontSize = 11.scaledSp,
                color = if (currentTheme.isDark) Color(0xFFFFCC80) else Color(0xFFEF6C00),
                lineHeight = 16.scaledSp
            )
        }
    }
}
""")
