package com.example.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Centralized Icons Provider for consistent icon usage across the OpenCpp application.
 */
object AppIcons {
    val OpenFile: ImageVector = Icons.Default.FolderOpen
    val Run: ImageVector = Icons.Default.PlayArrow
    val Compile: ImageVector = Icons.Default.Build
    val Settings: ImageVector = Icons.Default.Settings
    val Premium: ImageVector = Icons.Default.Star
    val Theme: ImageVector = Icons.Default.Palette
    val Save: ImageVector = Icons.Default.Save
    val Delete: ImageVector = Icons.Default.Delete
    val Copy: ImageVector = Icons.Default.ContentCopy
    val Search: ImageVector = Icons.Default.Search
    val Share: ImageVector = Icons.Default.Share
    val Back: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
    val Home: ImageVector = Icons.Default.Home
    val About: ImageVector = Icons.Default.Info
}
