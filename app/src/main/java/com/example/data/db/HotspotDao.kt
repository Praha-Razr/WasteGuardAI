package com.example.data.db

import androidx.room.*
import com.example.data.model.Hotspot
import kotlinx.coroutines.flow.Flow

@Dao
interface HotspotDao {
    @Query("SELECT * FROM hotspots ORDER BY currentRiskScore DESC")
    fun getAllHotspots(): Flow<List<Hotspot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(hotspots: List<Hotspot>)
}
