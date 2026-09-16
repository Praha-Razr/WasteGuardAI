package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.User
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.theme.*

@Composable
fun CitizenProfileScreen(
    user: User,
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        DemoNoticeBanner(currentLanguage)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // User Card Header
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = GreenDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(GreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Column {
                        Text(text = user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = user.email, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                        Text(text = "${user.ward}, ${user.district}", fontSize = 12.sp, color = Color(0xFF6EE7B7), fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Impact Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = "🌱 " + StringsDictionary.get("my_impact", currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Reports Submitted:", fontSize = 14.sp, color = Color(0xFF64748B))
                        Text("12", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = SlateNavy)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Issues Resolved:", fontSize = 14.sp, color = Color(0xFF64748B))
                        Text("8", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = GreenPrimary)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Hotspots Identified:", fontSize = 14.sp, color = Color(0xFF64748B))
                        Text("3", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0284C7))
                    }
                }
            }

            // Badges Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🎖️ Community Badges", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFECFDF5)) {
                            Text("🌱 First Report", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, color = GreenDark, fontSize = 12.sp)
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFEFF6FF)) {
                            Text("♻️ Waste Watcher", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF), fontSize = 12.sp)
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFEF3C7)) {
                            Text("🌍 Community Contributor", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, color = Color(0xFF92400E), fontSize = 12.sp)
                        }
                    }
                }
            }

            // Settings & Language Toggle
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    TextButton(
                        onClick = { onLanguageToggle() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = GreenPrimary)
                                Text("Language Preference", color = SlateNavy, fontWeight = FontWeight.Bold)
                            }
                            Text(if (currentLanguage == AppLanguage.ENGLISH) "English" else "தமிழ்", color = GreenPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Divider(color = Color(0xFFF1F5F9))

                    Button(
                        onClick = { onLogout() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RiskCritical)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                            Text("Logout Account", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
