package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for interacting with the recent files local cache.
 */
@Dao
interface RecentFileDao {

    @Query("SELECT * FROM recent_files ORDER BY lastModified DESC LIMIT 10")
    fun getRecentFiles(): Flow<List<RecentFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentFile(file: RecentFileEntity)

    @Query("DELETE FROM recent_files WHERE id = :id")
    suspend fun deleteRecentFileById(id: Int)

    @Query("DELETE FROM recent_files")
    suspend fun clearAllRecentFiles()
}
