package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Hotspot
import com.example.data.model.Incident
import com.example.localization.AppLanguage
import com.example.localization.StringsDictionary
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.components.IncidentCard
import com.example.ui.components.WasteGuardMapView
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.SlateNavy

@Composable
fun CitizenMapScreen(
    incidents: List<Incident>,
    hotspots: List<Hotspot>,
    currentLanguage: AppLanguage,
    onSelectIncident: (String) -> Unit
) {
    var selectedIncidentId by remember { mutableStateOf<String?>("WG-1042") }
    val selectedIncident = incidents.firstOrNull { it.id == selectedIncidentId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        DemoNoticeBanner(currentLanguage)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🗺️ " + StringsDictionary.get("map", currentLanguage) + " — Tamil Nadu Waste Incidents",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SlateNavy
            )

            WasteGuardMapView(
                incidents = incidents,
                hotspots = hotspots,
                showHeatmap = true,
                onMarkerClick = { id ->
                    selectedIncidentId = id
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if (selectedIncident != null) {
                Text(
                    text = "Selected Incident Details:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateNavy
                )
                IncidentCard(
                    incident = selectedIncident,
                    onClick = { onSelectIncident(selectedIncident.id) }
                )
            }
        }
    }
}
