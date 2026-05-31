package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquatCalibrationScreen(
    onNavigateBack: () -> Unit,
    viewModel: SquatCalibrationViewModel = viewModel()
) {
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isComplete by viewModel.isCalibrationComplete.collectAsState()
    val powerScore by viewModel.powerScore.collectAsState()
    val view = androidx.compose.ui.platform.LocalView.current

    DisposableEffect(Unit) {
        viewModel.startCalibration()
        onDispose {
            viewModel.cleanUp()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Auto-Calibration", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAF6F0)
                )
            )
        },
        containerColor = Color(0xFFFAF6F0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isComplete) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Calibration",
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Let's calibrate your hardware. Hold your phone and squat continuously for 10 seconds.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D4037),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { timeLeft / 10f },
                        modifier = Modifier.size(160.dp),
                        color = Color(0xFFE65100),
                        strokeWidth = 12.dp,
                        trackColor = Color(0xFFE5D5D0)
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$timeLeft",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = "sec",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF00E676),
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Calibration Complete!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF5D4037)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    ) {
                        Text(
                            text = "SQUAT POWER SCORE",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = String.format("%.2f", powerScore),
                            fontSize = 64.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = "Motion Energy",
                            fontSize = 14.sp,
                            color = Color(0xFF5D4037),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Awesome! We've captured your unique motion signature. To wake up tomorrow, make sure you drop down and stand back up with this same energy to silence the alarm.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Button(
                    onClick = {
                        try {
                            view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP)
                        } catch (e: Exception) {}
                        onNavigateBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D4037)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Finish Setup", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
