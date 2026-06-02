package com.example.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class AppItem(val packageName: String, val label: String, val isBlocked: Boolean)

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
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val buildList = mutableListOf<AppItem>()
            for (appInfo in packages) {
                // only show launchable user apps
                if (pm.getLaunchIntentForPackage(appInfo.packageName) != null) {
                    // skip our own app
                    if (appInfo.packageName == context.packageName) continue
                    
                    val label = appInfo.loadLabel(pm).toString()
                    buildList.add(AppItem(appInfo.packageName, label, blockedSet.contains(appInfo.packageName)))
                }
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
                title = { Text("Manage Blocked Apps", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037)) },
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD54F))
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
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text("Select the apps you want to block while the focus timer is running.", modifier = Modifier.padding(16.dp), color = Color.Gray)
                }
            }

            if (isLoading) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = Color(0xFFFF8A80))
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(appList, key = { it.packageName }) { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
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
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(app.label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF5D4037))
                            if (app.isBlocked) {
                                Icon(Icons.Filled.Check, contentDescription = "Blocked", tint = Color(0xFFFF8A80))
                            }
                        }
                        HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
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
