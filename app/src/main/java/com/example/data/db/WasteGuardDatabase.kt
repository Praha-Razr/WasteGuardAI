package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CleanupTeam
import com.example.data.model.Hotspot
import com.example.data.model.Incident

@Database(
    entities = [Incident::class, Hotspot::class, CleanupTeam::class],
    version = 1,
    exportSchema = false
)
abstract class WasteGuardDatabase : RoomDatabase() {
    abstract fun incidentDao(): IncidentDao
    abstract fun hotspotDao(): HotspotDao
    abstract fun cleanupTeamDao(): CleanupTeamDao

    companion object {
        @Volatile
        private var INSTANCE: WasteGuardDatabase? = null

        fun getDatabase(context: Context): WasteGuardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WasteGuardDatabase::class.java,
                    "wasteguard_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
