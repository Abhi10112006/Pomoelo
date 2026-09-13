package com.example.ui

import com.example.ui.components.pomoShadow

import com.example.service.SquatSensorService
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.LocalAppFont

@Composable
fun WorkoutScreen(bottomPadding: Dp) {
    val context = LocalContext.current
    var isTracking by remember { mutableStateOf(false) }
    var secondsElapsed by remember { mutableStateOf(0) }
    
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current

    
    // Tracking Variables
    var distanceMeters by remember { mutableStateOf(0f) }
    var sessionSteps by remember { mutableStateOf(0) }
    var initialSteps by remember { mutableStateOf(-1) }
    var squatsCount by remember { mutableStateOf(0) }
    var isShaking by remember { mutableStateOf(false) }

    // Permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // We will start tracking regardless, but we use whichever sensors are granted
        isTracking = true
    }

    // Hardware Managers (Local only)
    val locationManager = remember { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val squatSensorService = remember { SquatSensorService(context) }

    var lastLocation by remember { mutableStateOf<Location?>(null) }
    
    // GPS Callback for Distance
    val locationListener = remember {
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (isTracking) {
                    lastLocation?.let {
                        distanceMeters += it.distanceTo(location)
                    }
                    lastLocation = location
                }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
    }

    // Sensor Callback for Steps
    val stepListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (isTracking && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                    val currentSteps = event.values[0].toInt()
                    if (initialSteps == -1) {
                        initialSteps = currentSteps
                    }
                    sessionSteps = currentSteps - initialSteps
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    LaunchedEffect(isTracking) {
        if (isTracking) {
            // Register Sensors
            try {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 2f, locationListener)
                }
            } catch (e: Exception) { e.printStackTrace() }

            sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)?.also { stepSensor ->
                sensorManager.registerListener(stepListener, stepSensor, SensorManager.SENSOR_DELAY_UI)
            }
            
            // Start advanced squat tracking
            squatSensorService.startTracking(
                targetSquats = Int.MAX_VALUE,
                onComplete = { /* Never completes with MAX_VALUE */ },
                onUpdate = { count -> squatsCount = count },
                onShakeWarning = { shaking -> isShaking = shaking }
            )

            while (true) {
                delay(1000)
                secondsElapsed++
            }
        } else {
            // Unregister
            try { locationManager.removeUpdates(locationListener) } catch (e: Exception) { }
            sensorManager.unregisterListener(stepListener)
            squatSensorService.stopTracking()
            lastLocation = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = bottomPadding)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
            contentDescription = "Workout",
            modifier = Modifier.size(80.dp),
            tint = currentTheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Offline Workout",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = currentFont,
            color = currentTheme.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Using local GPS & hardware sensors",
            fontSize = 14.sp,
            fontFamily = currentFont,
            color = currentTheme.textSecondary
        )
                
        Spacer(modifier = Modifier.height(32.dp))
                
        Card(
            modifier = Modifier.fillMaxWidth().pomoShadow(
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp,
                shadowColor = currentTheme.shadowColor
            ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Duration", fontSize = 16.sp, fontFamily = currentFont, color = currentTheme.textSecondary)
                Text(
                    text = String.format("%02d:%02d", secondsElapsed / 60, secondsElapsed % 60),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentTheme.primary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Distance", fontSize = 14.sp, fontFamily = currentFont, color = currentTheme.textSecondary)
                        Text(
                            text = String.format("%.2f km", distanceMeters / 1000f),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.secondary
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Steps", fontSize = 14.sp, fontFamily = currentFont, color = currentTheme.textSecondary)
                        Text(
                            text = "$sessionSteps",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.accent
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Squats section with a manual fallback button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Squats", fontSize = 14.sp, fontFamily = currentFont, color = currentTheme.textSecondary)
                        Text(
                            text = "$squatsCount",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.primaryDark
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(
                        onClick = { squatsCount++ },
                        modifier = Modifier.size(48.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = currentTheme.pillActiveBg)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Squat", tint = currentTheme.primary)
                    }
                }
            }
        }
                
        Spacer(modifier = Modifier.height(32.dp))
                
        com.example.ui.components.PomoButton(
            onClick = {
                if (!isTracking) {
                    // Check permissions before starting
                    val fineLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    val actRec = ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)
                    
                    if (fineLoc != PackageManager.PERMISSION_GRANTED || actRec != PackageManager.PERMISSION_GRANTED) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACTIVITY_RECOGNITION
                            )
                        )
                    } else {
                        isTracking = true
                    }
                } else {
                    isTracking = false
                }
            },
            containerColor = if (isTracking) currentTheme.primaryDark else currentTheme.primary,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Icon(
                imageVector = if (isTracking) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = if (isTracking) "Stop" else "Start",
                tint = currentTheme.surface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isTracking) "Stop Workout" else "Start Workout",
                fontFamily = currentFont,
                color = currentTheme.surface,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (!isTracking && secondsElapsed > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = { 
                    secondsElapsed = 0 
                    distanceMeters = 0f
                    sessionSteps = 0
                    initialSteps = -1
                    squatsCount = 0
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Reset", color = currentTheme.textSecondary, fontFamily = currentFont, fontWeight = FontWeight.Bold)
            }
        }
    }
}
