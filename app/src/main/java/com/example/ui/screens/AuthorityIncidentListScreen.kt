package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Incident
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.components.IncidentCard
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SlateNavy

@Composable
fun AuthorityIncidentListScreen(
    incidents: List<Incident>,
    currentLanguage: AppLanguage,
    onSelectIncident: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: All, 1: Critical, 2: Active, 3: Resolved

    val filteredIncidents = incidents.filter { inc ->
        when (selectedTab) {
            1 -> inc.riskLevel == "CRITICAL"
            2 -> !inc.status.contains("Resolved")
            3 -> inc.status.contains("Resolved")
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        DemoNoticeBanner(currentLanguage)

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = androidx.compose.ui.graphics.Color.White,
            contentColor = GreenPrimary
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("All", fontWeight = FontWeight.Bold) })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Critical", fontWeight = FontWeight.Bold) })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Active", fontWeight = FontWeight.Bold) })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Resolved", fontWeight = FontWeight.Bold) })
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredIncidents) { incident ->
                IncidentCard(
                    incident = incident,
                    onClick = { onSelectIncident(incident.id) }
                )
            }
        }
    }
}
