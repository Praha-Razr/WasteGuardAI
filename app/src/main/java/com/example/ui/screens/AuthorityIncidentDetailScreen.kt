package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CleanupTeam
import com.example.data.model.Incident
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.components.RiskBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorityIncidentDetailScreen(
    incident: Incident?,
    teams: List<CleanupTeam>,
    currentLanguage: AppLanguage,
    onNavigate: (Screen) -> Unit,
    onAssignTeam: (incidentId: String, teamId: String, teamName: String) -> Unit,
    onVerifyCleanup: (incidentId: String) -> Unit
) {
    if (incident == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Incident Not Found")
        }
        return
    }

    var showTeamDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Incident ${incident.id}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onNavigate(Screen.AUTHORITY_DASHBOARD) }) {
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DemoNoticeBanner(currentLanguage)

            // Header Overview Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        Text(incident.id, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = SlateNavy)
                        RiskBadge(riskLevel = incident.riskLevel, score = incident.riskScore)
                    }

                    Text(incident.wasteType, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = GreenDark)

                    Text(
                        text = "📍 Location: ${incident.address}",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Text(
                            text = "🔁 ${incident.duplicateCount} citizen reports merged into this incident",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }
                }
            }

            // AI Risk & Weather Metrics Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("AI Assessment Metrics", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("WasteGuard Risk Score:", fontSize = 13.sp, color = Color(0xFF64748B))
                        Text("🔴 ${incident.riskScore} / 100", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskCritical)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Stormwater Drain Risk:", fontSize = 13.sp, color = Color(0xFF64748B))
                        Text(incident.drainRisk, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskCritical)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Fire Hazard Level:", fontSize = 13.sp, color = Color(0xFF64748B))
                        Text(incident.fireRisk, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskHigh)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Estimated Quantity:", fontSize = 13.sp, color = Color(0xFF64748B))
                        Text(incident.estimatedQuantity, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SlateNavy)
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEF2F2),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🌧️ Weather Note: " + incident.weatherNote,
                            modifier = Modifier.padding(10.dp),
                            fontSize = 12.sp,
                            color = Color(0xFF991B1B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Team Assignment Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("🚛 Cleanup Team Status", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    if (incident.assignedTeamName != null) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFECFDF5)) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.LocalShipping, contentDescription = null, tint = GreenPrimary)
                                Text("Assigned: ${incident.assignedTeamName}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GreenDark)
                            }
                        }
                    } else {
                        Text("No cleanup team assigned yet.", fontSize = 13.sp, color = Color(0xFF64748B))
                        Button(
                            onClick = { showTeamDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text("Assign Cleanup Team", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // BEFORE / AFTER VERIFICATION SECTION (Section 25)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("📷 Before / After AI Verification", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // BEFORE CARD
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFFEF2F2)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("BEFORE", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = RiskCritical)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Score: ${incident.riskScore}/100", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = RiskCritical)
                                Text("High Risk", fontSize = 11.sp, color = Color(0xFF991B1B))
                            }
                        }

                        // AFTER CARD
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            color = if (incident.isVerified) Color(0xFFECFDF5) else Color(0xFFF1F5F9)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("AFTER", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = if (incident.isVerified) GreenDark else Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Score: ${incident.afterRiskScore ?: "--"}/100", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (incident.isVerified) GreenPrimary else Color(0xFF64748B))
                                Text(if (incident.isVerified) "Cleaned" else "Pending", fontSize = 11.sp, color = if (incident.isVerified) GreenDark else Color(0xFF64748B))
                            }
                        }
                    }

                    if (incident.isVerified) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFECFDF5),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = GreenPrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("CLEANUP VERIFIED ✓ (Score 89 → 12)", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = GreenDark)
                            }
                        }
                    } else {
                        Button(
                            onClick = { onVerifyCleanup(incident.id) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SlateNavy)
                        ) {
                            Text("Upload After Photo & Verify Cleanup ✓", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Team Assignment Dialog
    if (showTeamDialog) {
        AlertDialog(
            onDismissRequest = { showTeamDialog = false },
            title = { Text("Select Cleanup Team") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    teams.forEach { team ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAssignTeam(incident.id, team.id, team.name)
                                    showTeamDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(team.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("${team.distanceKm} km away • ${team.status}", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GreenPrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTeamDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
