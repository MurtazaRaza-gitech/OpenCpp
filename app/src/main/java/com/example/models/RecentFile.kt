package com.example.models

import kotlinx.serialization.Serializable

/**
 * Domain model representing a C++ source code file recently opened by the user.
 */
@Serializable
data class RecentFile(
    val id: Int = 0,
    val name: String,
    val path: String,
    val lastModified: Long = System.currentTimeMillis(),
    val sizeBytes: Long = 0L
)
