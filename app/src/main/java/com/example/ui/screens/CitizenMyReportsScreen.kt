package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*

@Composable
fun CitizenMyReportsScreen(
    incidents: List<Incident>,
    selectedIncidentId: String?,
    currentLanguage: AppLanguage,
    onSelectIncident: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: All, 1: Active, 2: Resolved

    val filteredIncidents = incidents.filter { inc ->
        when (selectedTab) {
            1 -> !inc.status.contains("Resolved")
            2 -> inc.status.contains("Resolved")
            else -> true
        }
    }

    val selectedIncident = incidents.firstOrNull { it.id == selectedIncidentId } ?: incidents.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        DemoNoticeBanner(currentLanguage)

        // Top Filter Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = GreenPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("All Reports (${incidents.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Active (${incidents.count { !it.status.contains("Resolved") }})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Resolved (${incidents.count { it.status.contains("Resolved") }})", fontWeight = FontWeight.Bold) }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedIncident != null) {
                item {
                    // Selected Detailed Status Timeline Banner
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = GreenDark),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Incident Status Timeline",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = selectedIncident.id,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF6EE7B7)
                                )
                            }

                            Divider(color = Color.White.copy(alpha = 0.2f))

                            val steps = listOf(
                                "Report Submitted" to true,
                                "AI Analysis" to true,
                                "Authority Notified" to true,
                                "Cleanup Assigned" to (selectedIncident.status.contains("Assigned") || selectedIncident.status.contains("Progress") || selectedIncident.status.contains("Resolved")),
                                "Cleanup Completed" to selectedIncident.status.contains("Resolved"),
                                "Verification" to selectedIncident.isVerified
                            )

                            steps.forEach { (step, isDone) ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (isDone) Color(0xFF6EE7B7) else Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = step,
                                        color = if (isDone) Color.White else Color.White.copy(alpha = 0.5f),
                                        fontSize = 13.sp,
                                        fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            items(filteredIncidents) { incident ->
                IncidentCard(
                    incident = incident,
                    onClick = { onSelectIncident(incident.id) }
                )
            }
        }
    }
}
