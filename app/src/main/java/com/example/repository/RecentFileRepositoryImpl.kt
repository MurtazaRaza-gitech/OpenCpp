package com.example.repository

import com.example.data.database.RecentFileDao
import com.example.data.database.RecentFileEntity
import com.example.domain.repository.RecentFileRepository
import com.example.models.RecentFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Production implementation of RecentFileRepository utilizing Room database.
 */
class RecentFileRepositoryImpl(
    private val recentFileDao: RecentFileDao
) : RecentFileRepository {

    override fun getRecentFiles(): Flow<List<RecentFile>> {
        return recentFileDao.getRecentFiles().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addRecentFile(file: RecentFile) {
        recentFileDao.insertRecentFile(RecentFileEntity.fromDomain(file))
    }

    override suspend fun removeRecentFile(id: Int) {
        recentFileDao.deleteRecentFileById(id)
    }

    override suspend fun clearAll() {
        recentFileDao.clearAllRecentFiles()
    }
}
