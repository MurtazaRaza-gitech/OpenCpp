package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.navigation.OpenCppNavigation
import com.example.ui.theme.OpenCppTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ViewModelFactory

/**
 * Main Activity of the OpenCpp application.
 * Manages view model initialization, active window bindings, and theme selection state changes.
 */
class MainActivity : ComponentActivity() {

    // Initialize the ViewModel with dependencies resolved from the application container
    private val viewModel: MainViewModel by viewModels {
        val appContainer = (application as OpenCppApplication).container
        ViewModelFactory(
            themePreferences = appContainer.themePreferences,
            recentFileRepository = appContainer.recentFileRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup edge-to-edge system insets support
        enableEdgeToEdge()
        
        setContent {
            // Collect theme changes reactively from preferences DataStore flow
            val selectedTheme by viewModel.selectedThemeState.collectAsState()

            OpenCppTheme(appTheme = selectedTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // Setup main type-safe navigation graph
                    OpenCppNavigation(
                        currentTheme = selectedTheme,
                        onThemeSelected = { newTheme ->
                            viewModel.updateTheme(newTheme)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
