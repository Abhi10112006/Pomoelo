package com.example.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.service.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AppItem(val packageName: String, val label: String, val isBlocked: Boolean)

@Composable
fun AppIcon(packageName: String) {
    val context = LocalContext.current
    var bitmap by remember(packageName) { mutableStateOf<ImageBitmap?>(null) }
    
    LaunchedEffect(packageName) {
        withContext(Dispatchers.IO) {
            try {
                val drawable = context.packageManager.getApplicationIcon(packageName)
                val bmp = drawable.toBitmap(width = 120, height = 120) // crisp 120x120 icon
                val imageBitmap = bmp.asImageBitmap()
                withContext(Dispatchers.Main) {
                    bitmap = imageBitmap
                }
            } catch (e: Exception) {}
        }
    }
    if (bitmap != null) {
        Image(bitmap = bitmap!!, contentDescription = null, modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)))
    } else {
        Box(modifier = Modifier.size(44.dp).background(Color.Gray.copy(alpha = 0.2f), RoundedCornerShape(8.dp)))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAppsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var appList by remember { mutableStateOf<List<AppItem>>(emptyList()) }
    var blockedSet by remember { mutableStateOf(SettingsManager.getBlockedApps()) }
    var isLoading by remember { mutableStateOf(true) }

    // Check accessibility permission
    var hasAccessibilityPermission by remember { mutableStateOf(isAccessibilityServiceEnabled(context)) }

    LaunchedEffect(Unit) {
        hasAccessibilityPermission = isAccessibilityServiceEnabled(context)
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
                title = { Text("Distraction Manager", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF5D4037))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFF0EC))
            )
        },
        containerColor = Color(0xFFFFF0EC)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            
            if (!hasAccessibilityPermission) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD54F)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Warning, contentDescription = null, tint = Color(0xFF5D4037))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Service Required", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("To block these apps when the timer is running, please enable 'PomoPal App Blocker' in your device's Accessibility Settings.", color = Color(0xFF5D4037))
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Open Settings", color = Color(0xFF5D4037))
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text("Select the target apps you want to block while the focus timer is actively running.", modifier = Modifier.padding(16.dp), color = Color.Gray, fontSize = 14.sp)
                }
            }

            if (isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = Color(0xFFFF8A80))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(appList, key = { it.packageName }) { app ->
                        val isSelected = app.isBlocked
                        val containerColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFFFF8A80).copy(alpha = 0.12f) else Color.Transparent,
                            animationSpec = tween(400),
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
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppIcon(packageName = app.packageName)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = app.label, 
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, 
                                fontSize = 16.sp, 
                                color = Color(0xFF5D4037),
                                modifier = Modifier.weight(1f)
                            )
                            
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(
                                        color = if (isSelected) Color(0xFFFF8A80) else Color.Gray.copy(alpha = 0.1f), 
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
