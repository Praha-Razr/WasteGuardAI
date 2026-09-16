package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.WasteAnalysisResult
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.WasteGuardMapView
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAnalysisScreen(
    isAnalyzing: Boolean,
    analysisResult: WasteAnalysisResult?,
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onSubmitReport: (description: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Risk Score Analysis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Screen.CITIZEN_REPORT) }) {
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
        if (isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(BackgroundLight),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = GreenPrimary,
                        strokeWidth = 6.dp
                    )
                    Text(
                        text = "🤖 " + StringsDictionary.get("analyzing_image", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateNavy,
                        textAlign = TextAlign.Center
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("✓ Deep visual scan & polymer texture analysis", fontSize = 13.sp, color = GreenPrimary, fontWeight = FontWeight.Bold)
                        Text("✓ Measuring drain proximity (Ward 18 Stormwater Drain)", fontSize = 13.sp, color = GreenPrimary, fontWeight = FontWeight.Bold)
                        Text("✓ Querying Open-Meteo live Tamil Nadu weather forecast", fontSize = 13.sp, color = GreenPrimary, fontWeight = FontWeight.Bold)
                        Text("✓ Computing WasteGuard AI Risk Score (0-100)", fontSize = 13.sp, color = GreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (analysisResult != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header status
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFECFDF5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(28.dp))
                        Column {
                            Text(
                                text = "🤖 " + StringsDictionary.get("ai_complete", currentLanguage),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = GreenDark
                            )
                            Text(
                                text = "Gemini AI Computer Vision Analysis Complete",
                                fontSize = 12.sp,
                                color = Color(0xFF047857)
                            )
                        }
                    }
                }

                // WASTEGUARD AI RISK SCORE CARD
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = StringsDictionary.get("risk_score", currentLanguage),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateNavyLight,
                            letterSpacing = 1.sp
                        )

                        // Score Circle Display
                        val scoreColor = when {
                            analysisResult.riskScore >= 75 -> RiskCritical
                            analysisResult.riskScore >= 50 -> RiskHigh
                            else -> RiskMedium
                        }

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(scoreColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${analysisResult.riskScore}",
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = scoreColor
                                )
                                Text(
                                    text = "/ 100",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateNavyLight
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = scoreColor
                        ) {
                            Text(
                                text = "${if (analysisResult.riskScore >= 75) "🔴 CRITICAL RISK" else if (analysisResult.riskScore >= 50) "🟠 HIGH RISK" else "🟡 MEDIUM RISK"}",
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // AI Detection Metrics Breakdown
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Comprehensive Analysis Breakdown",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = SlateNavy
                        )

                        Divider(color = GreenBorder)

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Waste Type & Composition:", fontSize = 12.sp, color = SlateNavyLight)
                            Text(analysisResult.wasteType, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Severity & Risk Level:", fontSize = 12.sp, color = SlateNavyLight)
                            Text("${analysisResult.severity} (${analysisResult.riskLevel})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RiskCritical)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Illegal Dumping Probability:", fontSize = 12.sp, color = SlateNavyLight)
                            Text("${analysisResult.dumpingProbability}%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Stormwater Drain Threat:", fontSize = 12.sp, color = SlateNavyLight)
                            Text(analysisResult.drainRisk, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RiskCritical)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Fire & Environmental Hazard:", fontSize = 12.sp, color = SlateNavyLight)
                            Text(analysisResult.fireRisk, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = RiskHigh)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Estimated Waste Volume/Weight:", fontSize = 12.sp, color = SlateNavyLight)
                            Text(analysisResult.estimatedQuantity, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                        }
                    }
                }

                // Live Weather Data Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CyanLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Thunderstorm, contentDescription = null, tint = CyanAccent)
                            Text(
                                text = "🌤️ Live Tamil Nadu Meteorological Status",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF0369A1)
                            )
                        }
                        Text(
                            text = analysisResult.weatherNote,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = SlateNavy
                        )
                    }
                }

                // Recommended Protocol
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = RiskCritical)
                            Text(
                                text = "🚛 Recommended Municipal Protocol",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF991B1B)
                            )
                        }
                        Text(
                            text = analysisResult.recommendedAction,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7F1D1D)
                        )
                    }
                }

                // Interactive Tamil Nadu Map of Incident Location
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "📍 Live Incident Geolocation on Tamil Nadu Map",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SlateNavy
                        )
                        WasteGuardMapView(
                            incidents = emptyList(),
                            hotspots = emptyList(),
                            centerLat = 13.0827,
                            centerLng = 80.2707,
                            zoomLevel = 13,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }
                }

                // Submit Button
                Button(
                    onClick = { onSubmitReport("Plastic waste pile near Ward 18 stormwater drain") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "SUBMIT OFFICIAL MUNICIPAL REPORT →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
