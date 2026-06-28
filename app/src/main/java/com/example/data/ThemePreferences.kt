package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Extension property to delegate DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "opencpp_settings")

/**
 * DataStore preferences layer to manage local app configuration, including theme persistence.
 */
class ThemePreferences(private val context: Context) {

    companion object {
        private val KEY_SELECTED_THEME = stringPreferencesKey("selected_theme")
    }

    /**
     * Emits the currently selected AppTheme, defaulting to VS_CODE_DARK on error or initial load.
     */
    val selectedThemeFlow: Flow<AppTheme> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeName = preferences[KEY_SELECTED_THEME] ?: AppTheme.VS_CODE_DARK.name
            try {
                AppTheme.valueOf(themeName)
            } catch (e: IllegalArgumentException) {
                AppTheme.VS_CODE_DARK
            }
        }

    /**
     * Persists the chosen theme enum safely in local preferences storage.
     */
    suspend fun saveTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_THEME] = theme.name
        }
    }
}
