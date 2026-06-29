package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CodeErrorState
import com.example.ui.components.CodeInfoBar
import com.example.ui.components.CodeLoadingShimmer
import com.example.ui.components.CodePanel
import com.example.ui.components.CodeSearchBar
import com.example.ui.components.CopyCodeFab
import com.example.ui.components.EmptyCodeState
import com.example.ui.components.FileInfoCard
import com.example.ui.components.OpenInEditorFab
import com.example.ui.components.ReadOnlyBanner
import com.example.ui.components.ZoomControls
import com.example.ui.components.countMatches
import com.example.ui.components.highlightCpp
import com.example.ui.components.highlightSearchMatches
import com.example.ui.components.rememberSyntaxColors
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────
// Constants
// ─────────────────────────────────────────────

private const val FALLBACK_FILE_NAME = "Unknown.cpp"
private const val LANGUAGE_LABEL     = "C++"
private const val ENCODING_LABEL     = "UTF-8"

private val FONT_SIZE_DEFAULT = 14.sp
private val FONT_SIZE_MIN     = 10.sp
private val FONT_SIZE_MAX     = 32.sp
private val FONT_SIZE_STEP    = 2f

// ─────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────

/**
 * Professional read-only C++ source viewer.
 *
 * Features: syntax highlighting, line numbers, search, zoom, copy-to-clipboard,
 * shimmer loading state, error state, empty state, and a bottom info bar.
 *
 * @param code           Raw C++ source to display. Empty string shows the empty state.
 * @param fileName       File name shown in the top bar and info card.
 *                       Blank falls back to "Unknown.cpp".
 * @param onBackClick    Invoked when the user taps the back button.
 * @param onOpenEditor   Invoked when the user taps "Open in Editor". Receives the
 *                       current [code] and [fileName] so the caller can navigate
 *                       without this screen knowing about destinations.
 * @param isLoading      When true, a shimmer placeholder replaces the code panel.
 * @param errorMessage   When non-null, an error state replaces the code panel.
 * @param modifier       Optional modifier for the root [Scaffold].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(
    code: String,
    fileName: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenEditor: (code: String, fileName: String) -> Unit = { _, _ -> },
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    // ── Derived values ──────────────────────────────────────────────────────
    val displayedFileName  = fileName.ifBlank { FALLBACK_FILE_NAME }
    val isDark             = isSystemInDarkTheme()
    val syntaxColors       = rememberSyntaxColors(isDark)
    val searchHighlight    = MaterialTheme.colorScheme.tertiaryContainer

    // ── State ───────────────────────────────────────────────────────────────
    val snackbarHostState  = remember { SnackbarHostState() }
    val scope              = rememberCoroutineScope()
    val clipboard          = LocalClipboardManager.current

    var searchQuery    by remember { mutableStateOf("") }
    var fontSizeSp     by remember { mutableFloatStateOf(FONT_SIZE_DEFAULT.value) }

    // Syntax-highlight once; re-compute only when code or theme changes.
    val highlightedCode = remember(code, isDark) {
        if (code.isBlank()) AnnotatedString("") else highlightCpp(code, syntaxColors)
    }

    // Apply search highlights on top of syntax highlights.
    val displayedAnnotated = remember(highlightedCode, searchQuery, searchHighlight) {
        highlightSearchMatches(highlightedCode, searchQuery, searchHighlight)
    }

    val matchCount = remember(code, searchQuery) { countMatches(code, searchQuery) }
    val lineCount  = remember(code) { if (code.isBlank()) 0 else code.lines().size }
    val charCount  = remember(code) { code.length }

    // ── UI ──────────────────────────────────────────────────────────────────
    Scaffold(
        modifier = modifier.testTag("viewer_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = displayedFileName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.semantics {
                            contentDescription = "Navigate back"
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* Overflow menu — placeholder for future items */ },
                        modifier = Modifier.semantics { contentDescription = "More options" }
                    ) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor         = MaterialTheme.colorScheme.surface,
                    titleContentColor      = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (!isLoading && errorMessage == null && code.isNotBlank()) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding()
                ) {
                    CopyCodeFab(
                        onCopy = {
                            clipboard.setText(AnnotatedString(code))
                            scope.launch {
                                snackbarHostState.showSnackbar("Code copied successfully.")
                            }
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                    OpenInEditorFab(
                        onOpenEditor = { onOpenEditor(code, displayedFileName) }
                    )
                }
            }
        },
        bottomBar = {
            if (!isLoading && errorMessage == null && code.isNotBlank()) {
                CodeInfoBar(
                    lineCount = lineCount,
                    charCount = charCount,
                    encoding  = ENCODING_LABEL,
                    language  = LANGUAGE_LABEL
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isLoading       -> CodeLoadingShimmer()
                errorMessage != null -> CodeErrorState(message = errorMessage)
                code.isBlank()  -> EmptyCodeState()
                else            -> ViewerContent(
                    displayedFileName  = displayedFileName,
                    code               = code,
                    displayedAnnotated = displayedAnnotated,
                    searchQuery        = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    matchCount         = matchCount,
                    fontSizeSp         = fontSizeSp,
                    onZoomIn  = { fontSizeSp = (fontSizeSp + FONT_SIZE_STEP).coerceAtMost(FONT_SIZE_MAX.value) },
                    onZoomOut = { fontSizeSp = (fontSizeSp - FONT_SIZE_STEP).coerceAtLeast(FONT_SIZE_MIN.value) },
                    onReset   = { fontSizeSp = FONT_SIZE_DEFAULT.value }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// Content column (extracted to keep ViewerScreen readable)
// ─────────────────────────────────────────────

@Composable
private fun ViewerContent(
    displayedFileName: String,
    code: String,
    displayedAnnotated: AnnotatedString,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    matchCount: Int,
    fontSizeSp: Float,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // File metadata card
        FileInfoCard(
            fileName       = displayedFileName,
            language       = LANGUAGE_LABEL,
            fileSizeBytes  = code.toByteArray(Charsets.UTF_8).size
        )

        Spacer(Modifier.height(12.dp))

        // Read-only banner
        ReadOnlyBanner()

        Spacer(Modifier.height(12.dp))

        // Search
        CodeSearchBar(
            query          = searchQuery,
            onQueryChange  = onSearchQueryChange,
            matchCount     = matchCount
        )

        Spacer(Modifier.height(8.dp))

        // Zoom controls
        ZoomControls(
            fontSize  = fontSizeSp.sp,
            onZoomIn  = onZoomIn,
            onZoomOut = onZoomOut,
            onReset   = onReset
        )

        Spacer(Modifier.height(8.dp))

        // Code panel — does NOT scroll itself vertically; the outer Column does.
        // Horizontal scroll is handled internally by CodePanel.
        CodePanel(
            annotatedCode = displayedAnnotated,
            fontSize      = fontSizeSp.sp
        )

        // Extra bottom padding so FABs don't obscure last line
        Spacer(Modifier.height(88.dp))
    }
}