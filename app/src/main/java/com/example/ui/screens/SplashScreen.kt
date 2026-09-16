package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen

@Composable
fun SplashScreen(
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    onNavigate: (Screen) -> Unit,
    onDemoCitizenLogin: () -> Unit,
    onDemoAuthorityLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Language Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tamil Nadu AI Initiative",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenDark.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GreenLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                    modifier = Modifier.clickable { onLanguageToggle() }
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.ENGLISH) "English | தமிழ்" else "தமிழ் | English",
                        color = GreenDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            // Center Branding Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                // Hero Shield Icon Box
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = GreenPrimary.copy(alpha = 0.3f))
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(GreenPrimary, Color(0xFF065F46))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🛡️", fontSize = 44.sp)
                }

                Text(
                    text = "WasteGuard AI",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenDark,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.5).sp
                )

                Text(
                    text = "SEE IT. PREDICT IT. PREVENT IT.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                // Glassmorphic Info Card
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Citizen-powered Predictive Intelligence",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenDark,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Leveraging local expertise and AI to build a cleaner, flood-resilient Tamil Nadu.",
                            fontSize = 12.sp,
                            color = SlateNavyLight,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Bottom Section with Stats & Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Quick Impact Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        color = GreenLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "ACTIVE ISSUES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenDark.copy(alpha = 0.7f),
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "1,240+",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenDark,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        color = CyanLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyanBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "HOTSPOTS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent.copy(alpha = 0.8f),
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "42 Active",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                // Primary Citizen Login Button
                Button(
                    onClick = { onDemoCitizenLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = GreenPrimary.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Citizen Login",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "குடிமகன் உள்நுழைவு",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👤", fontSize = 18.sp)
                        }
                    }
                }

                // Municipal Authority Button
                OutlinedButton(
                    onClick = { onDemoAuthorityLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(2.dp, GreenPrimary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Municipal Authority",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenDark
                            )
                            Text(
                                text = "நகராட்சி நிர்வாகம்",
                                fontSize = 11.sp,
                                color = GreenDark.copy(alpha = 0.6f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(GreenPrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏛️", fontSize = 18.sp)
                        }
                    }
                }

                // Registration / Account Link
                TextButton(
                    onClick = { onNavigate(Screen.REGISTRATION) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "New Citizen? Register Account →",
                        color = GreenPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Disclaimer
                Text(
                    text = "PROTOTYPE DATA • HACKATHON DEMO ONLY • MADE FOR TAMIL NADU",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateNavyLight.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
