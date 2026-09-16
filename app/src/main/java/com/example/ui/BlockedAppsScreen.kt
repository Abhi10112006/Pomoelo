package com.example.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.service.AppBlockerManager
import com.example.service.SettingsManager
import com.example.service.TimerManager
import com.example.ui.components.PomoButton
import com.example.ui.components.pomoShadow
import com.example.ui.theme.LocalAppFont
import com.example.ui.theme.LocalAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AppItem(val packageName: String, val label: String, val isBlocked: Boolean)

private val POPULAR_DISTRACTION_PACKAGES = setOf(
    "com.instagram.android",
    "com.google.android.youtube",
    "com.zhiliaoapp.musically",
    "com.twitter.android",
    "com.snapchat.android",
    "com.reddit.frontpage",
    "com.netflix.mediaclient",
    "tv.twitch.android.app",
    "com.discord",
    "com.facebook.katana",
    "com.pinterest",
    "com.whatsapp",
    "org.telegram.messenger",
    "com.valvesoftware.android.steam.community",
    "com.riotgames.league.wildrift",
    "com.supercell.clashofclans",
    "com.supercell.clashroyale",
    "com.pubg.imobile",
    "com.dts.freefireth"
)

@Composable
fun AppIcon(packageName: String) {
    val context = LocalContext.current
    var bitmap by remember(packageName) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(packageName) {
        withContext(Dispatchers.IO) {
            try {
                val drawable = context.packageManager.getApplicationIcon(packageName)
                val bmp = drawable.toBitmap(width = 120, height = 120)
                val imageBitmap = bmp.asImageBitmap()
                withContext(Dispatchers.Main) {
                    bitmap = imageBitmap
                }
            } catch (e: Exception) {}
        }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = null,
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    } else {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(Color.Gray.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Apps, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAppsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current
    var appList by remember { mutableStateOf<List<AppItem>>(emptyList()) }
    var blockedSet by remember { mutableStateOf(SettingsManager.getBlockedApps()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyBlocked by remember { mutableStateOf(false) }

    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    val timerState by TimerManager.timerState.collectAsState()
    val isBreakMode by TimerManager.isBreakMode.collectAsState()

    var isStrictMode by remember { mutableStateOf(AppBlockerManager.isStrictMode(context)) }
    var isVibrateEnabled by remember { mutableStateOf(AppBlockerManager.isVibrateEnabled(context)) }
    var blockedAttemptsToday by remember { mutableIntStateOf(AppBlockerManager.getBlockAttemptsToday(context)) }

    androidx.activity.compose.BackHandler {
        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
        if (searchQuery.isNotEmpty()) {
            searchQuery = ""
        } else {
            onBack()
        }
    }

    // Check accessibility permission
    var hasAccessibilityPermission by remember { mutableStateOf(isAccessibilityServiceEnabled(context)) }

    LaunchedEffect(Unit) {
        hasAccessibilityPermission = isAccessibilityServiceEnabled(context)
        blockedAttemptsToday = AppBlockerManager.getBlockAttemptsToday(context)
        withContext(Dispatchers.IO) {
            val pm = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null).apply { addCategory(Intent.CATEGORY_LAUNCHER) }
            val resolveInfos = pm.queryIntentActivities(intent, 0)

            val buildList = mutableListOf<AppItem>()
            val addedPackages = mutableSetOf<String>()

            for (resolveInfo in resolveInfos) {
                val packageName = resolveInfo.activityInfo.packageName
                if (packageName == context.packageName) continue
                if (addedPackages.contains(packageName)) continue

                val label = resolveInfo.loadLabel(pm).toString()
                buildList.add(AppItem(packageName, label, blockedSet.contains(packageName)))
                addedPackages.add(packageName)
            }
            buildList.sortBy { it.label.lowercase() }
            withContext(Dispatchers.Main) {
                appList = buildList
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Distraction Shield",
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFont,
                            color = currentTheme.textPrimary,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "${blockedSet.size} apps protected",
                            fontSize = 12.sp,
                            fontFamily = currentFont,
                            color = currentTheme.textSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        if (searchQuery.isNotEmpty()) {
                            searchQuery = ""
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = currentTheme.textPrimary)
                    }
                },
                actions = {
                    // Preview Button in top bar
                    IconButton(onClick = {
                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                        val previewIntent = Intent(context, BlockedAppActivity::class.java).apply {
                            putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, "com.instagram.android")
                            putExtra(BlockedAppActivity.EXTRA_PREVIEW_MODE, true)
                        }
                        context.startActivity(previewIntent)
                    }) {
                        Icon(
                            Icons.Filled.Visibility,
                            contentDescription = "Preview Blocker",
                            tint = currentTheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = currentTheme.background)
            )
        },
        containerColor = currentTheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // 1. Accessibility Service Warning Banner (if disabled)
                if (!hasAccessibilityPermission) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .pomoShadow(
                                    shape = RoundedCornerShape(20.dp),
                                    elevation = 3.dp,
                                    shadowColor = currentTheme.shadowColor
                                ),
                            colors = CardDefaults.cardColors(containerColor = currentTheme.accent.copy(alpha = 0.9f)),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, currentTheme.cardBorder)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Filled.Warning,
                                        contentDescription = null,
                                        tint = currentTheme.textPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        "Accessibility Permission Required",
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = currentFont,
                                        color = currentTheme.textPrimary,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "To intercept blocked apps and display the mindful 5-second pause while your focus timer is running, enable 'PomoPal' under Accessibility Settings.",
                                    fontFamily = currentFont,
                                    color = currentTheme.textPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                PomoButton(
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                                    },
                                    containerColor = currentTheme.surface,
                                    contentColor = currentTheme.textPrimary,
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) {
                                    Icon(Icons.Filled.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Enable in Settings", fontWeight = FontWeight.Bold, fontFamily = currentFont)
                                }
                            }
                        }
                    }
                }

                // 2. Real-Time Status & Daily Stats Dashboard
                item {
                    val statusText: String
                    val statusColor: Color
                    val statusIcon = when {
                        !hasAccessibilityPermission -> {
                            statusText = "Permission Needed"
                            statusColor = Color(0xFFE53935)
                            Icons.Filled.ErrorOutline
                        }
                        isBreakMode -> {
                            statusText = "Break Time • Unlocked"
                            statusColor = Color(0xFF1E88E5)
                            Icons.Filled.Coffee
                        }
                        timerState == TimerManager.TimerState.RUNNING -> {
                            statusText = "Active & Guarding Focus"
                            statusColor = Color(0xFF43A047)
                            Icons.Filled.Shield
                        }
                        timerState == TimerManager.TimerState.PAUSED -> {
                            if (isStrictMode) {
                                statusText = "Strict Mode Active (Paused)"
                                statusColor = Color(0xFF43A047)
                                Icons.Filled.Shield
                            } else {
                                statusText = "Timer Paused"
                                statusColor = Color(0xFFFB8C00)
                                Icons.Filled.PauseCircle
                            }
                        }
                        else -> {
                            statusText = "Standby (Ready to Focus)"
                            statusColor = currentTheme.textSecondary
                            Icons.Filled.CheckCircleOutline
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .pomoShadow(
                                shape = RoundedCornerShape(20.dp),
                                elevation = 3.dp,
                                shadowColor = currentTheme.shadowColor
                            ),
                        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(statusColor, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = statusText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = currentTheme.textPrimary,
                                    fontFamily = currentFont
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                // Test / Preview Pill
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = currentTheme.primary.copy(alpha = 0.12f),
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        val previewIntent = Intent(context, BlockedAppActivity::class.java).apply {
                                            putExtra(BlockedAppActivity.EXTRA_PACKAGE_NAME, "com.instagram.android")
                                            putExtra(BlockedAppActivity.EXTRA_PREVIEW_MODE, true)
                                        }
                                        context.startActivity(previewIntent)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Filled.PlayArrow,
                                            contentDescription = null,
                                            tint = currentTheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "Preview 5s",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = currentTheme.primary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Stats row: Blocked count & Distractions resisted
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = currentTheme.background.copy(alpha = 0.7f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${blockedSet.size}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = currentTheme.primary,
                                            fontFamily = currentFont
                                        )
                                        Text(
                                            text = "Apps Blocked",
                                            fontSize = 11.sp,
                                            color = currentTheme.textSecondary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = currentTheme.background.copy(alpha = 0.7f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "$blockedAttemptsToday",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = currentTheme.secondary,
                                            fontFamily = currentFont
                                        )
                                        Text(
                                            text = "Resisted Today",
                                            fontSize = 11.sp,
                                            color = currentTheme.textSecondary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }

                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = currentTheme.background.copy(alpha = 0.7f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "5s",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = currentTheme.textPrimary,
                                            fontFamily = currentFont
                                        )
                                        Text(
                                            text = "Mindful Pause",
                                            fontSize = 11.sp,
                                            color = currentTheme.textSecondary,
                                            fontFamily = currentFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Strict Mode & Haptic Settings Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .pomoShadow(
                                shape = RoundedCornerShape(20.dp),
                                elevation = 2.dp,
                                shadowColor = currentTheme.shadowColor
                            ),
                        colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            // Strict Mode Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Strict Mode",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = currentTheme.textPrimary,
                                        fontFamily = currentFont
                                    )
                                    Text(
                                        "Keep blocking active even when timer is paused",
                                        fontSize = 12.sp,
                                        color = currentTheme.textSecondary,
                                        fontFamily = currentFont
                                    )
                                }
                                Switch(
                                    checked = isStrictMode,
                                    onCheckedChange = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        isStrictMode = it
                                        AppBlockerManager.setStrictMode(context, it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = currentTheme.surface,
                                        checkedTrackColor = currentTheme.primary
                                    )
                                )
                            }

                            HorizontalDivider(
                                color = currentTheme.cardBorder.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Haptic Feedback Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Tactile Awareness",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = currentTheme.textPrimary,
                                        fontFamily = currentFont
                                    )
                                    Text(
                                        "Gentle haptic vibration when an app is intercepted",
                                        fontSize = 12.sp,
                                        color = currentTheme.textSecondary,
                                        fontFamily = currentFont
                                    )
                                }
                                Switch(
                                    checked = isVibrateEnabled,
                                    onCheckedChange = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        isVibrateEnabled = it
                                        AppBlockerManager.setVibrateEnabled(context, it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = currentTheme.surface,
                                        checkedTrackColor = currentTheme.primary
                                    )
                                )
                            }
                        }
                    }
                }

                // 4. Quick Selection Chips & Search
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Smart Distractions Preset
                            FilterChip(
                                selected = false,
                                onClick = {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                    val newSet = blockedSet.toMutableSet()
                                    // Add installed popular distractions
                                    appList.forEach { app ->
                                        if (POPULAR_DISTRACTION_PACKAGES.contains(app.packageName)) {
                                            newSet.add(app.packageName)
                                        }
                                    }
                                    blockedSet = newSet
                                    SettingsManager.setBlockedApps(newSet)
                                    appList = appList.map { it.copy(isBlocked = newSet.contains(it.packageName)) }
                                },
                                label = { Text("⚡ Block Social & Games", fontFamily = currentFont, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = currentTheme.surface,
                                    labelColor = currentTheme.textPrimary
                                )
                            )

                            // Show Only Blocked toggle
                            FilterChip(
                                selected = showOnlyBlocked,
                                onClick = {
                                    try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                    showOnlyBlocked = !showOnlyBlocked
                                },
                                label = { Text("Blocked Only (${blockedSet.size})", fontFamily = currentFont, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = currentTheme.primary.copy(alpha = 0.15f),
                                    selectedLabelColor = currentTheme.primary,
                                    containerColor = currentTheme.surface,
                                    labelColor = currentTheme.textPrimary
                                )
                            )

                            // Clear All if any are selected
                            if (blockedSet.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        blockedSet = emptySet()
                                        SettingsManager.setBlockedApps(emptySet())
                                        appList = appList.map { it.copy(isBlocked = false) }
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.ClearAll,
                                        contentDescription = "Clear All Blocked",
                                        tint = currentTheme.textSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Search Field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search apps...", color = currentTheme.textSecondary, fontFamily = currentFont) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Filled.Search, contentDescription = null, tint = currentTheme.textSecondary)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Filled.Close, contentDescription = "Clear search", tint = currentTheme.textSecondary)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = currentTheme.primary,
                                unfocusedBorderColor = currentTheme.cardBorder,
                                focusedContainerColor = currentTheme.surface,
                                unfocusedContainerColor = currentTheme.surface,
                                focusedTextColor = currentTheme.textPrimary,
                                unfocusedTextColor = currentTheme.textPrimary
                            )
                        )
                    }
                }

                // 5. Apps List
                if (isLoading) {
                    item {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(200.dp)) {
                            CircularProgressIndicator(color = currentTheme.primary)
                        }
                    }
                } else {
                    val filteredAppList = appList.filter { app ->
                        val matchesSearch = searchQuery.isEmpty() || app.label.contains(searchQuery, ignoreCase = true)
                        val matchesBlockedFilter = !showOnlyBlocked || app.isBlocked
                        matchesSearch && matchesBlockedFilter
                    }

                    if (filteredAppList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Filled.SearchOff,
                                        contentDescription = null,
                                        tint = currentTheme.textSecondary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (showOnlyBlocked) "No apps currently blocked." else "No matching apps found.",
                                        color = currentTheme.textSecondary,
                                        fontFamily = currentFont,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    } else {
                        items(filteredAppList, key = { it.packageName }) { app ->
                            val isSelected = app.isBlocked
                            val containerColor by animateColorAsState(
                                targetValue = if (isSelected) currentTheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                                animationSpec = tween(300),
                                label = "bgColor"
                            )
                            val checkScale by animateFloatAsState(
                                targetValue = if (isSelected) 1f else 0f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                                label = "checkScale"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(containerColor)
                                    .clickable {
                                        try { view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                                        val currentSet = blockedSet.toMutableSet()
                                        if (currentSet.contains(app.packageName)) {
                                            currentSet.remove(app.packageName)
                                        } else {
                                            currentSet.add(app.packageName)
                                        }
                                        blockedSet = currentSet
                                        SettingsManager.setBlockedApps(currentSet)

                                        appList = appList.map {
                                            if (it.packageName == app.packageName) it.copy(isBlocked = currentSet.contains(app.packageName)) else it
                                        }
                                    }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppIcon(packageName = app.packageName)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = app.label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 15.sp,
                                        fontFamily = currentFont,
                                        color = currentTheme.textPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = if (isSelected) "Distraction Shield Active" else "Allowed during study",
                                        fontSize = 12.sp,
                                        fontFamily = currentFont,
                                        color = if (isSelected) currentTheme.primary else currentTheme.textSecondary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            color = if (isSelected) currentTheme.primary else currentTheme.cardBorder.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected || checkScale > 0.05f) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Blocked",
                                            tint = Color.White,
                                            modifier = Modifier.scale(checkScale).size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 6. Mindful Philosophy Explainer Card
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .pomoShadow(
                                shape = RoundedCornerShape(18.dp),
                                elevation = 1.dp,
                                shadowColor = currentTheme.shadowColor
                            ),
                        colors = CardDefaults.cardColors(containerColor = currentTheme.surface.copy(alpha = 0.7f)),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, currentTheme.cardBorder.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Lightbulb,
                                    contentDescription = null,
                                    tint = currentTheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Mindful Focus Philosophy",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = currentTheme.textPrimary,
                                    fontFamily = currentFont
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "When you open a blocked app, PomoPal shows a serene 5-second pause with student-focused motivations. This brief moment interrupts the compulsive dopamine loop, grounds your intention, and gracefully returns you to your study session.",
                                fontSize = 12.sp,
                                color = currentTheme.textSecondary,
                                fontFamily = currentFont,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

fun isAccessibilityServiceEnabled(context: Context): Boolean {
    var accessibilityEnabled = 0
    val service = context.packageName + "/com.example.service.AppBlockerService"
    try {
        accessibilityEnabled = Settings.Secure.getInt(
            context.applicationContext.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        )
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }
    val mStringColonSplitter = android.text.TextUtils.SimpleStringSplitter(':')
    if (accessibilityEnabled == 1) {
        val settingValue = Settings.Secure.getString(
            context.applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (settingValue != null) {
            mStringColonSplitter.setString(settingValue)
            while (mStringColonSplitter.hasNext()) {
                val accessibilityService = mStringColonSplitter.next()
                if (accessibilityService.equals(service, ignoreCase = true)) {
                    return true
                }
            }
        }
    }
    return false
}
