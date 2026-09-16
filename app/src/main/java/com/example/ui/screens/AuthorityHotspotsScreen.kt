package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Hotspot
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen

@Composable
fun AuthorityHotspotsScreen(
    hotspots: List<Hotspot>,
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        DemoNoticeBanner(currentLanguage)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateNavy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF38BDF8))
                            Text("🔮 " + StringsDictionary.get("predictive_intelligence", currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                        Text(
                            text = "Transitioning municipal waste management from Reactive to Predictive through historical spatial-temporal learning.",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Intelligent Deduplication Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Default.People, contentDescription = null, tint = Color(0xFF0284C7))
                            Text("🔁 " + StringsDictionary.get("intelligent_dedup", currentLanguage), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E40AF))
                        }
                        Text(
                            text = "50 Citizen Reports → AI Similarity & GPS Clustering → 1 Unified Master Incident (WG-1042)",
                            fontSize = 13.sp,
                            color = Color(0xFF1E3A8A)
                        )
                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFDBEAFE)) {
                            Text("23 duplicate citizen reports linked to WG-1042", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF))
                        }
                    }
                }
            }

            items(hotspots) { spot ->
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(spot.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateNavy)
                            RiskBadge(riskLevel = if (spot.currentRiskScore >= 75) "CRITICAL" else "HIGH", score = spot.currentRiskScore)
                        }

                        Divider(color = Color(0xFFF1F5F9))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ward / District:", fontSize = 13.sp, color = Color(0xFF64748B))
                            Text("${spot.ward}, ${spot.district}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Cumulative Citizen Reports:", fontSize = 13.sp, color = Color(0xFF64748B))
                            Text("${spot.totalReports} reports", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Recurring Pattern:", fontSize = 13.sp, color = Color(0xFF64748B))
                            Text(spot.recurringPattern, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Stormwater Drain Proximity:", fontSize = 13.sp, color = Color(0xFF64748B))
                            Text("${spot.drainProximityMeters} meters", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskCritical)
                        }

                        // AI Prediction Box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("🔮 AI Prediction: ${spot.weekendRiskPredictionPercent}% Weekend Overflow Risk", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Color(0xFF92400E))
                                Text("Recommendation: " + spot.recommendedAction, fontSize = 12.sp, color = Color(0xFF78350F))
                            }
                        }
                    }
                }
            }
        }
    }
}
