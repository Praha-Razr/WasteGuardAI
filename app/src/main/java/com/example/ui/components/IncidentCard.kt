package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Incident
import com.example.ui.theme.GreenDark
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SlateNavy

@Composable
fun IncidentCard(
    incident: Incident,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.GreenBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = incident.id,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = SlateNavy
                    )
                    RiskBadge(riskLevel = incident.riskLevel, score = incident.riskScore)
                }

                // Status pill
                val isResolved = incident.status.lowercase().contains("resolved")
                Surface(
                    shape = CircleShape,
                    color = if (isResolved) Color(0xFFD1FAE5) else Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = incident.status,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = if (isResolved) GreenDark else Color(0xFF92400E),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = incident.wasteType,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = GreenDark
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${incident.ward}, ${incident.district}",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (incident.duplicateCount > 0) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Duplicates",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "🔁 ${incident.duplicateCount} similar reports linked",
                            fontSize = 11.sp,
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Divider(color = Color(0xFFF1F5F9))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Drain Risk: ${incident.drainRisk}",
                    fontSize = 12.sp,
                    color = if (incident.drainRisk.contains("High")) Color(0xFFDC2626) else Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Details",
                    tint = GreenPrimary
                )
            }
        }
    }
}
