package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun RegistrationScreen(
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onRegister: (name: String, email: String, phone: String, district: String, ward: String) -> Unit
) {
    var fullName by remember { mutableStateOf("Anand Kumar") }
    var mobile by remember { mutableStateOf("+91 98765 43210") }
    var email by remember { mutableStateOf("anand@example.com") }
    var password by remember { mutableStateOf("••••••••") }
    var district by remember { mutableStateOf("Chennai") }
    var municipality by remember { mutableStateOf("Greater Chennai Corporation") }
    var ward by remember { mutableStateOf("Ward 18") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Citizen Registration", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Join Tamil Nadu Waste Watchers",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SlateNavy
            )
            Text(
                text = "Register your mobile account to report waste issues in your ward and protect stormwater drains from plastic clogging.",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
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

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("District") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = ward,
                    onValueChange = { ward = it },
                    label = { Text("Ward") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = municipality,
                onValueChange = { municipality = it },
                label = { Text("Municipality / Corporation") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onRegister(fullName, email, mobile, district, ward) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text(
                    text = "Create Citizen Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            TextButton(
                onClick = { onNavigate(Screen.LOGIN) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Already have an account? Login", color = GreenDark, fontWeight = FontWeight.Bold)
            }
        }
    }
}
