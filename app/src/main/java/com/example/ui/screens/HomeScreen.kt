package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OpenCppCard
import com.example.ui.components.OpenCppTopBar
import com.example.ui.theme.AppCodeTypography
import com.example.ui.theme.AppIcons
import com.example.ui.theme.AppSpacing

/**
 * Main Home screen presenting temporary cards and recent logs styled with "Professional Polish" visual language.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToViewer: () -> Unit,
    onNavigateToEditor: () -> Unit,
    onNavigateToThemePicker: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.testTag("home_screen"),
        topBar = {
            OpenCppTopBar(
                title = "OpenCpp Dashboard",
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = AppSpacing.Medium)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .clickable { onNavigateToPremium() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PRO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(start = AppSpacing.ScreenPadding, top = AppSpacing.Medium, end = AppSpacing.ScreenPadding, bottom = AppSpacing.DoubleExtraLarge),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
        ) {
            // Header Tagline / Hero Banner
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "// Open. Edit. Compile.",
                        style = AppCodeTypography.Medium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Production-ready C++ environment",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Quick Actions Title Block
            item {
                Text(
                    text = "WORKSPACE ACTIONS",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = AppSpacing.ExtraSmall)
                )
            }

            // Quick Actions 2x2 Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                    ) {
                        HomeGridActionCard(
                            title = "Open File",
                            icon = AppIcons.OpenFile,
                            iconColor = Color(0xFF4FC1FF),
                            onClick = onNavigateToViewer,
                            modifier = Modifier.weight(1f)
                        )
                        HomeGridActionCard(
                            title = "New Script",
                            icon = Icons.Default.AddBox,
                            iconColor = Color(0xFF4FC1FF),
                            onClick = onNavigateToEditor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
                    ) {
                        HomeGridActionCard(
                            title = "Themes",
                            icon = AppIcons.Theme,
                            iconColor = Color(0xFFCE9178),
                            onClick = onNavigateToThemePicker,
                            modifier = Modifier.weight(1f)
                        )
                        HomeGridActionCard(
                            title = "Settings",
                            icon = AppIcons.Settings,
                            iconColor = Color(0xFF9CDCFE),
                            onClick = onNavigateToSettings,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Recent Projects Title Block with action on right
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppSpacing.Medium, bottom = AppSpacing.ExtraSmall),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT PROJECTS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigateToViewer() }
                    )
                }
            }

            // Recent Projects List (Left-accented cards matching the design template)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)) {
                    RecentFileCardItem(
                        name = "main_engine.cpp",
                        tag = "C++",
                        tagColor = Color(0xFF4FC1FF),
                        info = "Modified 2 hours ago",
                        accentColor = MaterialTheme.colorScheme.primary,
                        onClick = onNavigateToViewer
                    )

                    RecentFileCardItem(
                        name = "utils_header.h",
                        tag = "H",
                        tagColor = Color(0xFFCE9178),
                        info = "Modified Yesterday",
                        accentColor = null,
                        onClick = onNavigateToViewer
                    )
                }
            }

            // Options & Info Quick Row Section
            item {
                Text(
                    text = "RESOURCES",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = AppSpacing.Medium, bottom = AppSpacing.ExtraSmall)
                )
            }

            // Quick Info row card
            item {
                OpenCppCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavigateToAbout,
                    testTag = "about_info_card"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
                    ) {
                        Icon(
                            imageVector = AppIcons.About,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "About OpenCpp environment",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Learn about local sandboxing, libraries & specifications.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Professional Premium Upgrade Banner (Gradient with Pill Button)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppSpacing.Medium)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                                )
                            )
                        )
                        .clickable { onNavigateToPremium() }
                        .padding(AppSpacing.Medium)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Get Premium",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Unlimited compilation & Cloud sync",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        Button(
                            onClick = onNavigateToPremium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = AppSpacing.Medium, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = "UPGRADE",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Reusable layout block for major action cards in Home Dashboard.
 * Formatted with 20dp rounded corners, outlines, and custom icon colors.
 */
@Composable
fun HomeGridActionCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OpenCppCard(
        modifier = modifier.height(110.dp),
        onClick = onClick,
        testTag = "action_card_${title.lowercase().replace(" ", "_")}"
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Left-accented card presenting individual file logs with file tags and more option actions.
 */
@Composable
fun RecentFileCardItem(
    name: String,
    tag: String,
    tagColor: Color,
    info: String,
    accentColor: Color?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable(onClick = onClick)
            .padding(AppSpacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Medium)
    ) {
        // High-contrast C++ or Header Tag Box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tag,
                style = AppCodeTypography.Small,
                fontWeight = FontWeight.Bold,
                color = tagColor
            )
        }

        // Project Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = info,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Options Action Menu Icon
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Options",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(18.dp)
        )
    }
}
