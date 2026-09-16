package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cleanup_teams")
data class CleanupTeam(
    @PrimaryKey val id: String,
    val name: String,
    val status: String, // "Available", "On Route", "In Action"
    val distanceKm: Double,
    val assignedIncidentId: String? = null,
    val district: String = "Chennai",
    val ward: String = "Ward 18"
)
