package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotspots")
data class Hotspot(
    @PrimaryKey val id: String,
    val name: String,
    val district: String,
    val municipality: String,
    val ward: String,
    val totalReports: Int,
    val primaryWasteType: String,
    val recurringPattern: String, // e.g. "Friday evenings"
    val drainProximityMeters: Int,
    val currentRiskScore: Int,
    val weekendRiskPredictionPercent: Int, // e.g. 91
    val latitude: Double,
    val longitude: Double,
    val recommendedAction: String
)
