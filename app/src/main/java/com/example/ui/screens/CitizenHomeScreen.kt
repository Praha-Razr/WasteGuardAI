package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Hotspot
import com.example.data.model.Incident
import com.example.data.model.User
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.components.IncidentCard
import com.example.ui.components.WasteGuardMapView
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen

@Composable
fun CitizenHomeScreen(
    user: User,
    incidents: List<Incident>,
    hotspots: List<Hotspot>,
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onSelectIncident: (String) -> Unit
) {
    val userIncidents = incidents.filter { it.citizenId == user.id || it.citizenId == "user_1" }
    val totalCount = userIncidents.size.coerceAtLeast(12)
    val resolvedCount = userIncidents.count { it.status.contains("Resolved") }.coerceAtLeast(8)
    val activeCount = totalCount - resolvedCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        DemoNoticeBanner(currentLanguage)

        // Greeting Banner
        Surface(
            color = GreenDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "${StringsDictionary.get("good_morning", currentLanguage)}, ${user.name} 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color(0xFF6EE7B7),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${user.ward}, ${user.district}, Tamil Nadu",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // HUGE MAIN BUTTON: 📸 REPORT WASTE
            Button(
                onClick = { onNavigate(Screen.CITIZEN_REPORT) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Report Waste Camera",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "📸 " + StringsDictionary.get("report_waste", currentLanguage),
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Snap photo → AI Risk Score → Municipal Action",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Summary Stats Cards
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$totalCount", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                        Text(text = "Reports", fontSize = 12.sp, color = SlateNavyLight)
                    }
                    Divider(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp),
                        color = GreenBorder
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$resolvedCount", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
                        Text(text = "Resolved", fontSize = 12.sp, color = SlateNavyLight)
                    }
                    Divider(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp),
                        color = GreenBorder
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$activeCount", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = RiskHigh)
                        Text(text = "Active", fontSize = 12.sp, color = SlateNavyLight)
                    }
                }
            }

            // Weather & Drain Alert Banner
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CyanLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDamage,
                        contentDescription = "Rainfall Alert",
                        tint = CyanAccent,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = "🌧️ Weather & Stormwater Alert",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF0369A1)
                        )
                        Text(
                            text = "Heavy rainfall expected near Ward 18. Keep stormwater drain grates clear of plastic waste.",
                            fontSize = 12.sp,
                            color = SlateNavyLight
                        )
                    }
                }
            }

            // Nearby Waste Hotspots Map Preview
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringsDictionary.get("nearby_hotspots", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateNavy
                    )
                    TextButton(onClick = { onNavigate(Screen.CITIZEN_MAP) }) {
                        Text("View Full Map", color = GreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                WasteGuardMapView(
                    incidents = incidents,
                    hotspots = hotspots,
                    showHeatmap = true,
                    onMarkerClick = { id -> onSelectIncident(id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            // My Impact
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = StringsDictionary.get("my_impact", currentLanguage),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateNavy
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFECFDF5)) {
                            Text("🌱 $totalCount Reports", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, color = GreenDark, fontSize = 13.sp)
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFEFF6FF)) {
                            Text("♻️ $resolvedCount Resolved", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF), fontSize = 13.sp)
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFEF3C7)) {
                            Text("🌍 3 Hotspots", modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Bold, color = Color(0xFF92400E), fontSize = 13.sp)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = CircleShape, color = GreenPrimary.copy(alpha = 0.2f)) {
                            Text("🏅 Waste Watcher", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, color = GreenDark, fontWeight = FontWeight.Bold)
                        }
                        Surface(shape = CircleShape, color = Color(0xFF0284C7).copy(alpha = 0.2f)) {
                            Text("🛡️ Drain Protector", modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 11.sp, color = Color(0xFF0284C7), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Recent Incident Cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Recent Community Incidents",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateNavy
                )
                incidents.take(3).forEach { incident ->
                    IncidentCard(
                        incident = incident,
                        onClick = { onSelectIncident(incident.id) }
                    )
                }
            }
        }
    }
}
