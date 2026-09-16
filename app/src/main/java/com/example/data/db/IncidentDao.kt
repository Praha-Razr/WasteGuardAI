package com.example.data.db

import androidx.room.*
import com.example.data.model.Incident
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {
    @Query("SELECT * FROM incidents ORDER BY createdAt DESC")
    fun getAllIncidents(): Flow<List<Incident>>

    @Query("SELECT * FROM incidents WHERE id = :id")
    suspend fun getIncidentById(id: String): Incident?

    @Query("SELECT * FROM incidents WHERE citizenId = :citizenId ORDER BY createdAt DESC")
    fun getIncidentsByCitizen(citizenId: String): Flow<List<Incident>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: Incident)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllIncidents(incidents: List<Incident>)

    @Update
    suspend fun updateIncident(incident: Incident)

    @Query("DELETE FROM incidents")
    suspend fun deleteAll()
}
