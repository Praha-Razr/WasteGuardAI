package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class Incident(
    @PrimaryKey val id: String,
    val citizenId: String,
    val citizenName: String,
    val photoUri: String? = null,
    val wasteType: String,
    val severity: String, // "Low", "Medium", "High", "Critical"
    val riskScore: Int, // 0 to 100
    val riskLevel: String, // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val district: String,
    val municipality: String,
    val ward: String,
    val description: String = "",
    val status: String, // "Report Submitted", "AI Analyzed", "Authority Notified", "Cleanup Assigned", "In Progress", "Resolved"
    val createdAt: Long = System.currentTimeMillis(),
    val drainRisk: String = "Low", // "Low", "Medium", "High", "Critical"
    val fireRisk: String = "Low",
    val illegalDumpingProb: Int = 85,
    val estimatedQuantity: String = "~100 kg",
    val weatherNote: String = "Heavy rainfall expected near stormwater drain.",
    val duplicateCount: Int = 0,
    val assignedTeamId: String? = null,
    val assignedTeamName: String? = null,
    val beforePhotoUri: String? = null,
    val afterPhotoUri: String? = null,
    val afterRiskScore: Int? = null,
    val isVerified: Boolean = false
)
