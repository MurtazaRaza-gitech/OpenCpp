package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.models.RecentFile

/**
 * Room Entity mapping the recent_files table.
 */
@Entity(tableName = "recent_files")
data class RecentFileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val path: String,
    val lastModified: Long,
    val sizeBytes: Long
) {
    /**
     * Converts Room database entity into clean domain model.
     */
    fun toDomain(): RecentFile = RecentFile(
        id = id,
        name = name,
        path = path,
        lastModified = lastModified,
        sizeBytes = sizeBytes
    )

    companion object {
        /**
         * Creates a Room entity from a clean domain model.
         */
        fun fromDomain(domain: RecentFile): RecentFileEntity = RecentFileEntity(
            id = domain.id,
            name = domain.name,
            path = domain.path,
            lastModified = domain.lastModified,
            sizeBytes = domain.sizeBytes
        )
    }
}
