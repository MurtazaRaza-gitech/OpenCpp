package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.EditorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OutputScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.ThemePickerScreen
import com.example.ui.screens.ViewerScreen
import com.example.ui.theme.AppTheme

/**
 * Centered navigation graph manager compiling all routes, sub-graphs, and transitions.
 */
@Composable
fun OpenCppNavigation(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier
    ) {
        // --- Splash Route ---
        composable<SplashRoute> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                }
            )
        }

        // --- Home Dashboard Route ---
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToViewer = { navController.navigate(ViewerRoute) },
                onNavigateToEditor = { navController.navigate(EditorRoute) },
                onNavigateToThemePicker = { navController.navigate(ThemePickerRoute) },
                onNavigateToPremium = { navController.navigate(PremiumRoute) },
                onNavigateToSettings = { navController.navigate(SettingsRoute) },
                onNavigateToAbout = { navController.navigate(AboutRoute) }
            )
        }

        // --- Code Viewer Route ---
        composable<ViewerRoute> {
            ViewerScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Code Editor Route ---
        composable<EditorRoute> {
            EditorScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Compiler Output Route ---
        composable<OutputRoute> {
            OutputScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Settings Route ---
        composable<SettingsRoute> {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Theme Picker Route ---
        composable<ThemePickerRoute> {
            ThemePickerScreen(
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected,
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- Premium Upgrade Route ---
        composable<PremiumRoute> {
            PremiumScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- About Route ---
        composable<AboutRoute> {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
