package com.example.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import java.util.Calendar

@Composable
fun WorkoutScreen(bottomPadding: androidx.compose.ui.unit.Dp) {
    val context = LocalContext.current
    var isTracking by remember { mutableStateOf(false) }
    var distanceMeters by remember { mutableStateOf(0f) }
    var stepCount by remember { mutableStateOf(0) }
    var hasPermissions by remember { mutableStateOf(
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    ) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermissions = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var lastLocation by remember { mutableStateOf<Location?>(null) }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (isTracking) {
                        lastLocation?.let {
                            distanceMeters += it.distanceTo(location)
                        }
                        lastLocation = location
                    }
                }
            }
        }
    }

    LaunchedEffect(isTracking, hasPermissions) {
        if (isTracking && hasPermissions) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build()
            
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
        } else {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            lastLocation = null
        }
    }

    val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "Connected to Google Fit", Toast.LENGTH_SHORT).show()
            readStepCount(context, fitnessOptions) { steps ->
                stepCount = steps
            }
        } else {
            Toast.makeText(context, "Google Fit Connection Failed", Toast.LENGTH_SHORT).show()
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
            imageVector = Icons.Default.DirectionsRun,
            contentDescription = "Workout",
            modifier = Modifier.size(80.dp),
            tint = Color(0xFF4CAF50)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Workout Mode",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Distance", fontSize = 16.sp, color = Color.Gray)
                Text(
                    text = String.format("%.2f km", distanceMeters / 1000f),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Google Fit Steps Today", fontSize = 16.sp, color = Color.Gray)
                Text(
                    text = "$stepCount",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (!hasPermissions) {
                        permissionLauncher.launch(arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ))
                    } else {
                        isTracking = !isTracking
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTracking) Color.Red else Color(0xFF4CAF50)
                ),
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    imageVector = if (isTracking) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isTracking) "Stop" else "Start"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isTracking) "Stop Tracking" else "Start GPS Tracking")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
                if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
                    
                    // This is slightly complex since Fitness API uses its own intent
                    GoogleSignIn.requestPermissions(
                        context as Activity,
                        1,
                        account,
                        fitnessOptions
                    )
                } else {
                    readStepCount(context, fitnessOptions) { steps ->
                        stepCount = steps
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier = Modifier.height(56.dp)
        ) {
            Icon(imageVector = Icons.Default.HealthAndSafety, contentDescription = "Fit")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sync with Google Fit")
        }
    }
}

private fun readStepCount(context: Context, fitnessOptions: FitnessOptions, onResult: (Int) -> Unit) {
    val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
    
    val cal = Calendar.getInstance()
    cal.time = java.util.Date()
    val endTime = cal.timeInMillis
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    val startTime = cal.timeInMillis

    val readRequest = DataReadRequest.Builder()
        .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
        .bucketByTime(1, TimeUnit.DAYS)
        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
        .build()

    Fitness.getHistoryClient(context, account)
        .readData(readRequest)
        .addOnSuccessListener { response ->
            var totalSteps = 0
            for (bucket in response.buckets) {
                for (dataSet in bucket.dataSets) {
                    for (dp in dataSet.dataPoints) {
                        for (field in dp.dataType.fields) {
                            totalSteps += dp.getValue(field).asInt()
                        }
                    }
                }
            }
            onResult(totalSteps)
        }
        .addOnFailureListener {
            onResult(0)
        }
}
