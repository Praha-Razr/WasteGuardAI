package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Incident
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.components.IncidentCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen

@Composable
fun AuthorityDashboardScreen(
    incidents: List<Incident>,
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onSelectIncident: (String) -> Unit
) {
    val criticalCount = incidents.count { it.riskLevel == "CRITICAL" }.coerceAtLeast(8)
    val highCount = incidents.count { it.riskLevel == "HIGH" }.coerceAtLeast(17)
    val pendingCount = incidents.count { !it.status.contains("Resolved") }.coerceAtLeast(24)
    val resolvedCount = incidents.count { it.status.contains("Resolved") }.coerceAtLeast(31)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        DemoNoticeBanner(currentLanguage)

        // Header Banner
        Surface(color = GreenDark, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "🏛️ MUNICIPAL INTELLIGENCE DASHBOARD",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = "Greater Chennai Corporation • Ward 18 Sanitary Command",
                    fontSize = 12.sp,
                    color = Color(0xFF6EE7B7)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("🚨 Critical", fontSize = 12.sp, color = RiskCritical, fontWeight = FontWeight.Bold)
                        Text("$criticalCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = RiskCritical)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDD5)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDBA74))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("🟠 High Risk", fontSize = 12.sp, color = RiskHigh, fontWeight = FontWeight.Bold)
                        Text("$highCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = RiskHigh)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("📋 Pending", fontSize = 12.sp, color = RiskMedium, fontWeight = FontWeight.Bold)
                        Text("$pendingCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = RiskMedium)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GreenBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("✅ Resolved Today", fontSize = 12.sp, color = GreenPrimary, fontWeight = FontWeight.Bold)
                        Text("$resolvedCount", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                    }
                }
            }

            // Fast Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onNavigate(Screen.AUTHORITY_HEATMAP) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("🗺️ Waste Heatmap", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onNavigate(Screen.AUTHORITY_HOTSPOTS) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateNavy)
                ) {
                    Text("🔮 AI Predictions", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Critical Alerts List
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔴 Critical Alerts Requiring Action",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateNavy
                    )
                    TextButton(onClick = { onNavigate(Screen.AUTHORITY_INCIDENTS) }) {
                        Text("View All", color = GreenPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                incidents.filter { it.riskLevel == "CRITICAL" || it.riskScore >= 75 }.forEach { incident ->
                    IncidentCard(
                        incident = incident,
                        onClick = { onSelectIncident(incident.id) }
                    )
                }
            }
        }
    }
}
