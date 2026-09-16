package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.ui.components.DemoNoticeBanner
import com.example.ui.theme.*

@Composable
fun AuthorityInsightsScreen(
    currentLanguage: AppLanguage
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        DemoNoticeBanner(currentLanguage)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "📊 Waste Analytics & Risk Predictions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SlateNavy
            )

            // Waste by Type
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Waste Composition by Type", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    val wasteTypes = listOf(
                        "Plastic & Packaging" to 42,
                        "Organic & Market Waste" to 28,
                        "Mixed Urban Waste" to 15,
                        "Construction Debris" to 9,
                        "E-Waste & Batteries" to 6
                    )

                    wasteTypes.forEach { (type, percent) ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(type, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text("$percent%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                            }
                            LinearProgressIndicator(
                                progress = { percent / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = GreenPrimary,
                                trackColor = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
            }

            // Incidents by Risk Level
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Incidents by AI Risk Level", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFEF2F2)) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("8", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = RiskCritical)
                                Text("Critical", fontSize = 11.sp, color = RiskCritical, fontWeight = FontWeight.Bold)
                            }
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFFEDD5)) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("17", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = RiskHigh)
                                Text("High", fontSize = 11.sp, color = RiskHigh, fontWeight = FontWeight.Bold)
                            }
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFEF3C7)) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("24", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = RiskMedium)
                                Text("Medium", fontSize = 11.sp, color = RiskMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFECFDF5)) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("31", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = GreenDark)
                                Text("Resolved", fontSize = 11.sp, color = GreenDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Top Recurring Hotspots
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Top 5 Recurring Hotspots (Tamil Nadu)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SlateNavy)
                    Divider(color = Color(0xFFF1F5F9))

                    Text("1. Ward 18 GCC (Stormwater Drain) — 32 Reports (Friday Eve)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskCritical)
                    Text("2. Ward 45 GCC (T. Nagar Market Rear) — 24 Reports (Sunday AM)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RiskHigh)
                    Text("3. Ward 12 Coimbatore (Bus Stand North) — 19 Reports (Daily 6 PM)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text("4. Ward 5 Madurai (Meenakshi Temple East) — 15 Reports", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text("5. Ward 8 Tiruchirappalli (Thillai Nagar) — 12 Reports", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
