package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.ThemePreferences
import com.example.domain.repository.RecentFileRepository

/**
 * ViewModel Factory providing clean Constructor Injection of services and repositories into ViewModels.
 */
class ViewModelFactory(
    private val themePreferences: ThemePreferences,
    private val recentFileRepository: RecentFileRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(themePreferences, recentFileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
