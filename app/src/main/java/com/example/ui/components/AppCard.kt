package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.AppShapes
import com.example.ui.theme.AppSpacing

/**
 * Reusable Card component designed according to Material Design 3 spacing and shape requirements (20dp).
 */
@Composable
fun OpenCppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    testTag: String = "custom_card",
    content: @Composable ColumnScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier
            .clip(AppShapes.Card)
            .clickable(onClick = onClick)
    } else {
        Modifier
    }

    Card(
        modifier = modifier
            .testTag(testTag)
            .then(clickableModifier),
        shape = AppShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = AppSpacing.ExtraSmall
        )
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.CardPadding)
        ) {
            content()
        }
    }
}
