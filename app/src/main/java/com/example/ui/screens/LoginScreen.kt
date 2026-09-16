package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SlateNavy
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onDemoCitizenLogin: () -> Unit,
    onDemoAuthorityLogin: () -> Unit
) {
    var email by remember { mutableStateOf("citizen@wasteguard.tn.gov.in") }
    var password by remember { mutableStateOf("••••••••") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account Login", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Screen.SPLASH) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenDark,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Welcome back to WasteGuard AI",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SlateNavy
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email or Official ID") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = { onDemoCitizenLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Login as Citizen", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "⚡ Hackathon Instant Demo Access:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0284C7)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { onDemoCitizenLogin() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("👤 Citizen Demo", fontSize = 13.sp)
                }

                Button(
                    onClick = { onDemoAuthorityLogin() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🏛️ Authority Demo", fontSize = 13.sp)
                }
            }
        }
    }
}
