package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ThemePreferences
import com.example.domain.repository.RecentFileRepository
import com.example.models.RecentFile
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Main application ViewModel coordinating application state, theme switching, and repository streams.
 * Complies with the MVVM design pattern.
 */
class MainViewModel(
    private val themePreferences: ThemePreferences,
    private val recentFileRepository: RecentFileRepository
) : ViewModel() {

    /**
     * Exposes the currently selected application theme.
     */
    val selectedThemeState: StateFlow<AppTheme> = themePreferences.selectedThemeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.VS_CODE_DARK
        )

    /**
     * Exposes the recent C++ files list from the local cache.
     */
    val recentFilesState: StateFlow<List<RecentFile>> = recentFileRepository.getRecentFiles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Updates and persists the chosen theme.
     */
    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreferences.saveTheme(theme)
        }
    }

    /**
     * Registers a new opened C++ file into local persistence.
     */
    fun addRecentFile(name: String, path: String, sizeBytes: Long) {
        viewModelScope.launch {
            recentFileRepository.addRecentFile(
                RecentFile(
                    name = name,
                    path = path,
                    sizeBytes = sizeBytes,
                    lastModified = System.currentTimeMillis()
                )
            )
        }
    }
}
