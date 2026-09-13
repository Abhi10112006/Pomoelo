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
import androidx.navigation.NavController
import com.example.service.SettingsManager
import com.example.ui.theme.LocalAppTheme
import com.example.ui.theme.LocalAppFont

@Composable
fun SignInScreen(navController: NavController, onSignInSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    val currentTheme = LocalAppTheme.current
    val currentFont = LocalAppFont.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(currentTheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Welcome to PomoPal", 
            fontSize = 28.sp, 
            fontWeight = FontWeight.Bold, 
            fontFamily = currentFont,
            color = currentTheme.textPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Enter your name to personalize your experience.", 
            fontSize = 14.sp, 
            fontFamily = currentFont,
            color = currentTheme.textSecondary
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Name", fontFamily = currentFont, color = currentTheme.textSecondary) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = currentTheme.primary,
                unfocusedBorderColor = currentTheme.cardBorder,
                focusedTextColor = currentTheme.textPrimary,
                unfocusedTextColor = currentTheme.textPrimary
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        com.example.ui.components.PomoButton(
            text = "Get Started",
            onClick = {
                if (name.isNotBlank()) {
                    SettingsManager.setUserName(name)
                    onSignInSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
