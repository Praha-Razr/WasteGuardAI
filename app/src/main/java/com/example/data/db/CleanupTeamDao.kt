package com.example.data.db

import androidx.room.*
import com.example.data.model.CleanupTeam
import kotlinx.coroutines.flow.Flow

@Dao
interface CleanupTeamDao {
    @Query("SELECT * FROM cleanup_teams")
    fun getAllTeams(): Flow<List<CleanupTeam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<CleanupTeam>)

    @Update
    suspend fun updateTeam(team: CleanupTeam)
}
