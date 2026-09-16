package com.example.data.repository

import com.example.data.db.WasteGuardDatabase
import com.example.data.model.CleanupTeam
import com.example.data.model.Hotspot
import com.example.data.model.Incident
import kotlinx.coroutines.flow.Flow

class WasteGuardRepository(private val db: WasteGuardDatabase) {

    val allIncidents: Flow<List<Incident>> = db.incidentDao().getAllIncidents()
    val allHotspots: Flow<List<Hotspot>> = db.hotspotDao().getAllHotspots()
    val allTeams: Flow<List<CleanupTeam>> = db.cleanupTeamDao().getAllTeams()

    fun getCitizenIncidents(citizenId: String): Flow<List<Incident>> {
        return db.incidentDao().getIncidentsByCitizen(citizenId)
    }

    suspend fun getIncidentById(id: String): Incident? {
        return db.incidentDao().getIncidentById(id)
    }

    suspend fun insertIncident(incident: Incident) {
        db.incidentDao().insertIncident(incident)
    }

    suspend fun updateIncident(incident: Incident) {
        db.incidentDao().updateIncident(incident)
    }

    suspend fun assignTeamToIncident(incidentId: String, teamId: String, teamName: String) {
        val incident = db.incidentDao().getIncidentById(incidentId)
        if (incident != null) {
            val updated = incident.copy(
                status = "Cleanup Assigned",
                assignedTeamId = teamId,
                assignedTeamName = teamName
            )
            db.incidentDao().updateIncident(updated)
        }
    }

    suspend fun verifyAndResolveCleanup(incidentId: String, afterPhotoUri: String?, afterScore: Int = 12) {
        val incident = db.incidentDao().getIncidentById(incidentId)
        if (incident != null) {
            val updated = incident.copy(
                status = "Resolved",
                afterPhotoUri = afterPhotoUri ?: "sample_clean_street",
                afterRiskScore = afterScore,
                isVerified = true
            )
            db.incidentDao().updateIncident(updated)
        }
    }

    suspend fun seedDemoDataIfEmpty() {
        // Seed initial Tamil Nadu hackathon demonstration data
        val existing = db.incidentDao().getIncidentById("WG-1042")
        if (existing == null) {
            val demoIncidents = listOf(
                Incident(
                    id = "WG-1042",
                    citizenId = "user_1",
                    citizenName = "Anand Kumar",
                    photoUri = "sample_plastic_drain",
                    wasteType = "Plastic & Mixed Waste",
                    severity = "Critical",
                    riskScore = 91,
                    riskLevel = "CRITICAL",
                    latitude = 13.0827,
                    longitude = 80.2707,
                    address = "M.G. Road, Ward 18, Greater Chennai Corporation",
                    district = "Chennai",
                    municipality = "Greater Chennai Corporation",
                    ward = "Ward 18",
                    description = "Heavy plastic waste dumping near stormwater drain grate. Drainage is partially blocked.",
                    status = "AI Analyzed",
                    createdAt = System.currentTimeMillis() - (20 * 60 * 1000), // 20 mins ago
                    drainRisk = "High (20m from stormwater drain)",
                    fireRisk = "Medium",
                    illegalDumpingProb = 89,
                    estimatedQuantity = "~150 kg",
                    weatherNote = "Heavy rainfall predicted near Ward 18 in 4 hours. High risk of urban waterlogging.",
                    duplicateCount = 23
                ),
                Incident(
                    id = "WG-1037",
                    citizenId = "user_2",
                    citizenName = "Priya Sundaram",
                    photoUri = "sample_market_waste",
                    wasteType = "Organic & Market Waste",
                    severity = "High",
                    riskScore = 78,
                    riskLevel = "HIGH",
                    latitude = 13.0418,
                    longitude = 80.2341,
                    address = "T. Nagar Market Road, Ward 45, Chennai",
                    district = "Chennai",
                    municipality = "Greater Chennai Corporation",
                    ward = "Ward 45",
                    description = "Vegetable and packaging accumulation behind market stalls.",
                    status = "Cleanup Assigned",
                    createdAt = System.currentTimeMillis() - (120 * 60 * 1000),
                    drainRisk = "Medium",
                    fireRisk = "Low",
                    illegalDumpingProb = 75,
                    estimatedQuantity = "~200 kg",
                    weatherNote = "Normal weather context.",
                    duplicateCount = 14,
                    assignedTeamId = "team_1",
                    assignedTeamName = "Team 01 (GCC Rapid Response)"
                ),
                Incident(
                    id = "WG-1029",
                    citizenId = "user_1",
                    citizenName = "Anand Kumar",
                    photoUri = "sample_organic_pile",
                    wasteType = "Organic Waste",
                    severity = "Medium",
                    riskScore = 45,
                    riskLevel = "MEDIUM",
                    latitude = 11.0168,
                    longitude = 76.9558,
                    address = "Gandhipuram Cross Street, Ward 12, Coimbatore",
                    district = "Coimbatore",
                    municipality = "Coimbatore Corporation",
                    ward = "Ward 12",
                    description = "Garden trimmings and organic waste piled on street corner.",
                    status = "Resolved",
                    createdAt = System.currentTimeMillis() - (24 * 3600 * 1000),
                    drainRisk = "Low",
                    fireRisk = "Low",
                    illegalDumpingProb = 60,
                    estimatedQuantity = "~40 kg",
                    weatherNote = "Clear sky.",
                    duplicateCount = 5,
                    assignedTeamId = "team_3",
                    assignedTeamName = "Team 03 (Coimbatore Green Crew)",
                    beforePhotoUri = "sample_organic_pile",
                    afterPhotoUri = "sample_clean_street",
                    afterRiskScore = 12,
                    isVerified = true
                ),
                Incident(
                    id = "WG-1021",
                    citizenId = "user_3",
                    citizenName = "Karthik Raja",
                    photoUri = "sample_e_waste",
                    wasteType = "E-Waste & Scrap Metal",
                    severity = "Critical",
                    riskScore = 82,
                    riskLevel = "CRITICAL",
                    latitude = 9.9252,
                    longitude = 78.1198,
                    address = "Near Meenakshi Temple East Gate, Ward 5, Madurai",
                    district = "Madurai",
                    municipality = "Madurai Corporation",
                    ward = "Ward 5",
                    description = "Dumped electronic circuit boards, wires, and old battery casings.",
                    status = "Authority Notified",
                    createdAt = System.currentTimeMillis() - (4 * 3600 * 1000),
                    drainRisk = "Low",
                    fireRisk = "High (Battery hazard)",
                    illegalDumpingProb = 94,
                    estimatedQuantity = "~80 kg",
                    weatherNote = "High temperature weather. Chemical hazards present.",
                    duplicateCount = 8
                ),
                Incident(
                    id = "WG-1015",
                    citizenId = "user_4",
                    citizenName = "Saravanan V.",
                    photoUri = "sample_construction_debris",
                    wasteType = "Construction Debris",
                    severity = "High",
                    riskScore = 65,
                    riskLevel = "HIGH",
                    latitude = 10.7905,
                    longitude = 78.7047,
                    address = "Thillai Nagar Main Road, Ward 8, Tiruchirappalli",
                    district = "Tiruchirappalli",
                    municipality = "Tiruchirappalli Corporation",
                    ward = "Ward 8",
                    description = "Concrete blocks and plaster left blocking sidewalk.",
                    status = "Resolved",
                    createdAt = System.currentTimeMillis() - (2 * 86400 * 1000),
                    drainRisk = "Medium",
                    fireRisk = "Low",
                    illegalDumpingProb = 88,
                    estimatedQuantity = "~300 kg",
                    weatherNote = "Moderate wind.",
                    duplicateCount = 11,
                    afterPhotoUri = "sample_clean_street",
                    afterRiskScore = 10,
                    isVerified = true
                )
            )
            db.incidentDao().insertAllIncidents(demoIncidents)

            val demoHotspots = listOf(
                Hotspot(
                    id = "hotspot_7",
                    name = "HOTSPOT #07 - Ward 18 Stormwater Drain",
                    district = "Chennai",
                    municipality = "Greater Chennai Corporation",
                    ward = "Ward 18",
                    totalReports = 32,
                    primaryWasteType = "Plastic & Mixed Waste",
                    recurringPattern = "Friday evenings",
                    drainProximityMeters = 20,
                    currentRiskScore = 87,
                    weekendRiskPredictionPercent = 91,
                    latitude = 13.0827,
                    longitude = 80.2707,
                    recommendedAction = "Deploy collection truck before Friday 5:00 PM."
                ),
                Hotspot(
                    id = "hotspot_3",
                    name = "HOTSPOT #03 - T. Nagar Market Rear Alley",
                    district = "Chennai",
                    municipality = "Greater Chennai Corporation",
                    ward = "Ward 45",
                    totalReports = 24,
                    primaryWasteType = "Organic & Market Packaging",
                    recurringPattern = "Sunday mornings",
                    drainProximityMeters = 45,
                    currentRiskScore = 76,
                    weekendRiskPredictionPercent = 85,
                    latitude = 13.0418,
                    longitude = 80.2341,
                    recommendedAction = "Increase weekend market sweep frequency."
                ),
                Hotspot(
                    id = "hotspot_12",
                    name = "HOTSPOT #12 - Gandhipuram Bus Stand North",
                    district = "Coimbatore",
                    municipality = "Coimbatore Corporation",
                    ward = "Ward 12",
                    totalReports = 19,
                    primaryWasteType = "Single-use Plastics & Bottles",
                    recurringPattern = "Daily 6:00 PM",
                    drainProximityMeters = 80,
                    currentRiskScore = 62,
                    weekendRiskPredictionPercent = 70,
                    latitude = 11.0168,
                    longitude = 76.9558,
                    recommendedAction = "Install secondary smart waste bins at bus bay."
                )
            )
            db.hotspotDao().insertAll(demoHotspots)

            val demoTeams = listOf(
                CleanupTeam(
                    id = "team_1",
                    name = "Team 01 (GCC Rapid Response)",
                    status = "Available",
                    distanceKm = 2.4,
                    district = "Chennai",
                    ward = "Ward 18"
                ),
                CleanupTeam(
                    id = "team_2",
                    name = "Team 02 (GCC Heavy Vehicle)",
                    status = "On Route",
                    distanceKm = 1.1,
                    district = "Chennai",
                    ward = "Ward 45"
                ),
                CleanupTeam(
                    id = "team_3",
                    name = "Team 03 (Coimbatore Green Crew)",
                    status = "Available",
                    distanceKm = 3.7,
                    district = "Coimbatore",
                    ward = "Ward 12"
                ),
                CleanupTeam(
                    id = "team_4",
                    name = "Team 04 (Madurai Eco Squad)",
                    status = "Available",
                    distanceKm = 4.2,
                    district = "Madurai",
                    ward = "Ward 5"
                )
            )
            db.cleanupTeamDao().insertAll(demoTeams)
        }
    }
}
