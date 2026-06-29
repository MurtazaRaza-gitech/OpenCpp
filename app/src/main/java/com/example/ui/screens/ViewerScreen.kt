package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.ui.components.OpenCppTopBar
import com.example.ui.theme.AppSpacing

private const val FALLBACK_FILE_NAME = "Unknown.cpp"
private const val FALLBACK_CODE = "No code loaded"

/**
 * Source code viewer screen.
 *
 * @param onBackClick Called when the user presses the back button.
 * @param code        Raw source code to display. Pass an empty string to show
 *                    the "No code loaded" placeholder.
 * @param fileName    Name of the file shown below the READ ONLY chip. Pass an
 *                    empty string to fall back to "Unknown.cpp".
 * @param modifier    Optional modifier for the root [Scaffold].
 */
@Composable
fun ViewerScreen(
    onBackClick: () -> Unit,
    code: String,
    fileName: String,
    modifier: Modifier = Modifier
) {
    val displayedFileName = fileName.ifBlank { FALLBACK_FILE_NAME }
    val displayedCode = code.ifBlank { FALLBACK_CODE }
    val isPlaceholder = code.isBlank()

    Scaffold(
        modifier = modifier.testTag("viewer_screen"),
        topBar = {
            OpenCppTopBar(
                title = "Original Source",
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(AppSpacing.ScreenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = "READ ONLY",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Text(
                text = displayedFileName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = displayedCode,
                    modifier = Modifier
                        .padding(AppSpacing.Medium)
                        .horizontalScroll(rememberScrollState()),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = if (isPlaceholder) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    softWrap = false
                )
            }
        }
    }
}