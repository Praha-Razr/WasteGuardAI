package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SlateNavy

@Composable
fun AuthorityHeatmapScreen(
    incidents: List<Incident>,
    hotspots: List<Hotspot>,
    currentLanguage: AppLanguage,
    onSelectIncident: (String) -> Unit
) {
    val districts = listOf("All Districts", "Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Tiruppur")
    var selectedDistrict by remember { mutableStateOf("All Districts") }

    val filteredIncidents = if (selectedDistrict == "All Districts") {
        incidents
    } else {
        incidents.filter { it.district.equals(selectedDistrict, ignoreCase = true) }
    }

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
                text = "🗺️ Waste Risk Heatmap — Tamil Nadu Municipalities",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SlateNavy
            )

            // District Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(districts) { dist ->
                    val isSelected = dist == selectedDistrict
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedDistrict = dist },
                        label = { Text(dist, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Full Map
            WasteGuardMapView(
                incidents = filteredIncidents,
                hotspots = hotspots,
                showHeatmap = true,
                selectedDistrict = selectedDistrict,
                onMarkerClick = { id -> selectedIncidentId = id },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            if (selectedIncident != null) {
                IncidentCard(
                    incident = selectedIncident,
                    onClick = { onSelectIncident(selectedIncident.id) }
                )
            }
        }
    }
}
