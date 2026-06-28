package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Main database definition for the OpenCpp application.
 */
@Database(
    entities = [RecentFileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentFileDao(): RecentFileDao
}
