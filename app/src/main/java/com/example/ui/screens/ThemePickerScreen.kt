package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.OpenCppCard
import com.example.ui.components.OpenCppSectionHeader
import com.example.ui.components.OpenCppTopBar
import com.example.ui.theme.AppIcons
import com.example.ui.theme.AppShapes
import com.example.ui.theme.AppSpacing
import com.example.ui.theme.AppTheme

// Import colors to display previews in swatches
import com.example.ui.theme.*

/**
 * Highly interactive Theme Picker screen presenting real-time preview swatches for all 10 presets.
 */
@Composable
fun ThemePickerScreen(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val darkThemes = AppTheme.entries.filter { it.isDark }
    val lightThemes = AppTheme.entries.filter { !it.isDark }

    Scaffold(
        modifier = modifier.testTag("theme_picker_screen"),
        topBar = {
            OpenCppTopBar(
                title = "Choose Theme Preset",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(vertical = AppSpacing.Medium),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
        ) {
            // Dark Themes Section Header
            item {
                OpenCppSectionHeader(title = "Dark Environments")
            }

            items(darkThemes) { theme ->
                ThemePresetRow(
                    theme = theme,
                    isSelected = theme == currentTheme,
                    onClick = { onThemeSelected(theme) }
                )
            }

            // Light Themes Section Header
            item {
                Spacer(modifier = Modifier.height(AppSpacing.Medium))
                OpenCppSectionHeader(title = "Light Environments")
            }

            items(lightThemes) { theme ->
                ThemePresetRow(
                    theme = theme,
                    isSelected = theme == currentTheme,
                    onClick = { onThemeSelected(theme) }
                )
            }
        }
    }
}

/**
 * Row card presenting individual theme information and visual color preview swatches.
 */
@Composable
fun ThemePresetRow(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Resolve core preview color values for the swatches
    val (primary, secondary, tertiary, background) = when (theme) {
        AppTheme.VS_CODE_DARK -> Quadruple(VsCodePrimary, VsCodeSecondary, VsCodeTertiary, VsCodeBackground)
        AppTheme.GITHUB_DARK -> Quadruple(GitHubDarkPrimary, GitHubDarkSecondary, GitHubDarkTertiary, GitHubDarkBackground)
        AppTheme.DRACULA -> Quadruple(DraculaPrimary, DraculaSecondary, DraculaTertiary, DraculaBackground)
        AppTheme.NORD -> Quadruple(NordPrimary, NordSecondary, NordTertiary, NordBackground)
        AppTheme.MIDNIGHT_BLUE -> Quadruple(MidnightPrimary, MidnightSecondary, MidnightTertiary, MidnightBackground)
        
        AppTheme.MATERIAL_LIGHT -> Quadruple(MaterialLightPrimary, MaterialLightSecondary, MaterialLightTertiary, MaterialLightBackground)
        AppTheme.GITHUB_LIGHT -> Quadruple(GitHubLightPrimary, GitHubLightSecondary, GitHubLightTertiary, GitHubLightBackground)
        AppTheme.BLUE_GRAY -> Quadruple(BlueGrayPrimary, BlueGraySecondary, BlueGrayTertiary, BlueGrayBackground)
        AppTheme.GREEN_PROFESSIONAL -> Quadruple(GreenPrimary, GreenSecondary, GreenTertiary, GreenBackground)
        AppTheme.PURPLE_MODERN -> Quadruple(PurpleModernPrimary, PurpleModernSecondary, PurpleModernTertiary, PurpleModernBackground)
    }

    OpenCppCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.ScreenPadding),
        onClick = onClick,
        testTag = "theme_row_${theme.name.lowercase()}"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio Indicator
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.width(AppSpacing.Medium))

            // Text Label
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = theme.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (theme.isDark) "Developer Dark" else "Professional Light",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Visual Color Swatch Preview
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.ExtraSmall)
            ) {
                Swatch(color = primary, tooltip = "Primary")
                Swatch(color = secondary, tooltip = "Secondary")
                Swatch(color = tertiary, tooltip = "Tertiary")
                Swatch(color = background, tooltip = "Background")
            }
        }
    }
}

/**
 * Small circular swatch previewing specific color tokens.
 */
@Composable
fun Swatch(
    color: Color,
    tooltip: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(18.dp)
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
            .background(color, CircleShape)
    )
}

// Simple Quad data class to pack theme swatches
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
