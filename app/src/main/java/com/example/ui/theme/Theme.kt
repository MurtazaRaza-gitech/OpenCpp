package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// ==========================================
// COLOR SCHEME MAPPINGS
// ==========================================

private val VsCodeDarkScheme = darkColorScheme(
    primary = VsCodePrimary,
    secondary = VsCodeSecondary,
    tertiary = VsCodeTertiary,
    background = VsCodeBackground,
    surface = VsCodeSurface,
    error = VsCodeError,
    outline = VsCodeOutline,
    onPrimary = VsCodeOnPrimary,
    onSecondary = VsCodeOnSecondary,
    onBackground = VsCodeOnBackground,
    onSurface = VsCodeOnSurface,
    surfaceVariant = VsCodeSurface,
    onSurfaceVariant = VsCodeOnSurface
)

private val GitHubDarkScheme = darkColorScheme(
    primary = GitHubDarkPrimary,
    secondary = GitHubDarkSecondary,
    tertiary = GitHubDarkTertiary,
    background = GitHubDarkBackground,
    surface = GitHubDarkSurface,
    error = GitHubDarkError,
    outline = GitHubDarkOutline,
    onPrimary = GitHubDarkOnPrimary,
    onSecondary = GitHubDarkOnSecondary,
    onBackground = GitHubDarkOnBackground,
    onSurface = GitHubDarkOnSurface,
    surfaceVariant = GitHubDarkSurface,
    onSurfaceVariant = GitHubDarkOnSurface
)

private val DraculaScheme = darkColorScheme(
    primary = DraculaPrimary,
    secondary = DraculaSecondary,
    tertiary = DraculaTertiary,
    background = DraculaBackground,
    surface = DraculaSurface,
    error = DraculaError,
    outline = DraculaOutline,
    onPrimary = DraculaOnPrimary,
    onSecondary = DraculaOnSecondary,
    onBackground = DraculaOnBackground,
    onSurface = DraculaOnSurface,
    surfaceVariant = DraculaSurface,
    onSurfaceVariant = DraculaOnSurface
)

private val NordScheme = darkColorScheme(
    primary = NordPrimary,
    secondary = NordSecondary,
    tertiary = NordTertiary,
    background = NordBackground,
    surface = NordSurface,
    error = NordError,
    outline = NordOutline,
    onPrimary = NordOnPrimary,
    onSecondary = NordOnSecondary,
    onBackground = NordOnBackground,
    onSurface = NordOnSurface,
    surfaceVariant = NordSurface,
    onSurfaceVariant = NordOnSurface
)

private val MidnightBlueScheme = darkColorScheme(
    primary = MidnightPrimary,
    secondary = MidnightSecondary,
    tertiary = MidnightTertiary,
    background = MidnightBackground,
    surface = MidnightSurface,
    error = MidnightError,
    outline = MidnightOutline,
    onPrimary = MidnightOnPrimary,
    onSecondary = MidnightOnSecondary,
    onBackground = MidnightOnBackground,
    onSurface = MidnightOnSurface,
    surfaceVariant = MidnightSurface,
    onSurfaceVariant = MidnightOnSurface
)

private val MaterialLightScheme = lightColorScheme(
    primary = MaterialLightPrimary,
    secondary = MaterialLightSecondary,
    tertiary = MaterialLightTertiary,
    background = MaterialLightBackground,
    surface = MaterialLightSurface,
    error = MaterialLightError,
    outline = MaterialLightOutline,
    onPrimary = MaterialLightOnPrimary,
    onSecondary = MaterialLightOnSecondary,
    onBackground = MaterialLightOnBackground,
    onSurface = MaterialLightOnSurface,
    surfaceVariant = MaterialLightSurface,
    onSurfaceVariant = MaterialLightOnSurface
)

private val GitHubLightScheme = lightColorScheme(
    primary = GitHubLightPrimary,
    secondary = GitHubLightSecondary,
    tertiary = GitHubLightTertiary,
    background = GitHubLightBackground,
    surface = GitHubLightSurface,
    error = GitHubLightError,
    outline = GitHubLightOutline,
    onPrimary = GitHubLightOnPrimary,
    onSecondary = GitHubLightOnSecondary,
    onBackground = GitHubLightOnBackground,
    onSurface = GitHubLightOnSurface,
    surfaceVariant = GitHubLightSurface,
    onSurfaceVariant = GitHubLightOnSurface
)

private val BlueGrayScheme = lightColorScheme(
    primary = BlueGrayPrimary,
    secondary = BlueGraySecondary,
    tertiary = BlueGrayTertiary,
    background = BlueGrayBackground,
    surface = BlueGraySurface,
    error = BlueGrayError,
    outline = BlueGrayOutline,
    onPrimary = BlueGrayOnPrimary,
    onSecondary = BlueGrayOnSecondary,
    onBackground = BlueGrayOnBackground,
    onSurface = BlueGrayOnSurface,
    surfaceVariant = BlueGraySurface,
    onSurfaceVariant = BlueGrayOnSurface
)

private val GreenProfessionalScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    tertiary = GreenTertiary,
    background = GreenBackground,
    surface = GreenSurface,
    error = GreenError,
    outline = GreenOutline,
    onPrimary = GreenOnPrimary,
    onSecondary = GreenOnSecondary,
    onBackground = GreenOnBackground,
    onSurface = GreenOnSurface,
    surfaceVariant = GreenSurface,
    onSurfaceVariant = GreenOnSurface
)

private val PurpleModernScheme = lightColorScheme(
    primary = PurpleModernPrimary,
    secondary = PurpleModernSecondary,
    tertiary = PurpleModernTertiary,
    background = PurpleModernBackground,
    surface = PurpleModernSurface,
    error = PurpleModernError,
    outline = PurpleModernOutline,
    onPrimary = PurpleModernOnPrimary,
    onSecondary = PurpleModernOnSecondary,
    onBackground = PurpleModernOnBackground,
    onSurface = PurpleModernOnSurface,
    surfaceVariant = PurpleModernSurface,
    onSurfaceVariant = PurpleModernOnSurface
)

/**
 * OpenCpp Core Theme Engine.
 * Dynamically provides typographic pairings, customized shapes, and
 * precise developer theme colors across light/dark environments.
 */
@Composable
fun OpenCppTheme(
    appTheme: AppTheme = AppTheme.VS_CODE_DARK,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when (appTheme) {
        AppTheme.VS_CODE_DARK -> VsCodeDarkScheme
        AppTheme.GITHUB_DARK -> GitHubDarkScheme
        AppTheme.DRACULA -> DraculaScheme
        AppTheme.NORD -> NordScheme
        AppTheme.MIDNIGHT_BLUE -> MidnightBlueScheme
        AppTheme.MATERIAL_LIGHT -> MaterialLightScheme
        AppTheme.GITHUB_LIGHT -> GitHubLightScheme
        AppTheme.BLUE_GRAY -> BlueGrayScheme
        AppTheme.GREEN_PROFESSIONAL -> GreenProfessionalScheme
        AppTheme.PURPLE_MODERN -> PurpleModernScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
