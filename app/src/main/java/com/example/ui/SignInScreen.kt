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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.GoogleAuthManager
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.example.service.SettingsManager

@Composable
fun SignInScreen(navController: NavController, onSignInSuccess: () -> Unit) {
    val context = LocalContext.current
    var isSigningIn by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isSigningIn = false
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                SettingsManager.setUserName(account?.givenName ?: account?.displayName ?: "User")
                onSignInSuccess()
            } catch (e: Exception) {
                errorMessage = "Sign in failed (${e.message}). You can continue as Guest."
                SettingsManager.setUserName(null)
            }
        } else {
            errorMessage = "Sign-in cancelled. You can continue as Guest."
            SettingsManager.setUserName(null)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0EC))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            tint = Color(0xFFFF8A80)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Welcome to PomoPal",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D4037)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Focus, track your health, and backup your progress seamlessly.",
            fontSize = 16.sp,
            color = Color(0xFF5D4037).copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                isSigningIn = true
                val signInIntent = GoogleAuthManager.getSignInClient(context).signInIntent
                googleSignInLauncher.launch(signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
            shape = RoundedCornerShape(28.dp),
            enabled = !isSigningIn
        ) {
            if (isSigningIn) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign in with Google", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = {
                SettingsManager.setUserName("Guest")
                onSignInSuccess()
            }
        ) {
            Text("Continue as Guest", color = Color(0xFFFF8A80), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
