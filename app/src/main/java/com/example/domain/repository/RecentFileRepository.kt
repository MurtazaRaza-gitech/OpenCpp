package com.example.domain.repository

import com.example.models.RecentFile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining access to recent C++ source files.
 * Aligns with Clean Architecture and SOLID principles.
 */
interface RecentFileRepository {
    /**
     * Observable stream of recent files ordered by usage timestamp.
     */
    fun getRecentFiles(): Flow<List<RecentFile>>

    /**
     * Adds or updates a recent file log.
     */
    suspend fun addRecentFile(file: RecentFile)

    /**
     * Removes a recent file log by ID.
     */
    suspend fun removeRecentFile(id: Int)

    /**
     * Clears all saved recent files.
     */
    suspend fun clearAll()
}
