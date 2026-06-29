package com.example.ui.components

// ─── Standard library ────────────────────────────────────────────────────────
import kotlin.math.roundToInt

// ─── Compose foundation ───────────────────────────────────────────────────────
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll

// ─── Animation ───────────────────────────────────────────────────────────────
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween

// ─── Material icons ───────────────────────────────────────────────────────────
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ErrorOutline

// ─── Material 3 ──────────────────────────────────────────────────────────────
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

// ─── Runtime ─────────────────────────────────────────────────────────────────
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

// ─── UI ──────────────────────────────────────────────────────────────────────
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// =============================================================================
// Syntax colour tokens
// =============================================================================

/**
 * Theme-aware colour set used by [highlightCpp].
 * Obtain via [rememberSyntaxColors].
 */
data class SyntaxColors(
    val keyword: Color,
    val type: Color,
    val comment: Color,
    val string: Color,
    val number: Color,
    val preprocessor: Color,
    val boolean: Color,
    val operator: Color,
    val function: Color,
    val default: Color
)

/**
 * Returns dark- or light-mode [SyntaxColors] matching a VS Code–inspired palette.
 * Stable across recompositions for the same [isDark] value.
 */
@Composable
fun rememberSyntaxColors(isDark: Boolean): SyntaxColors =
    if (isDark) SyntaxColors(
        keyword      = Color(0xFFCC99CD),
        type         = Color(0xFF4EC9B0),
        comment      = Color(0xFF6A9955),
        string       = Color(0xFFCE9178),
        number       = Color(0xFFB5CEA8),
        preprocessor = Color(0xFF9CDCFE),
        boolean      = Color(0xFF569CD6),
        operator     = Color(0xFFD4D4D4),
        function     = Color(0xFFDCDCAA),
        default      = Color(0xFFD4D4D4)
    )
    else SyntaxColors(
        keyword      = Color(0xFF0000FF),
        type         = Color(0xFF267F99),
        comment      = Color(0xFF008000),
        string       = Color(0xFFA31515),
        number       = Color(0xFF098658),
        preprocessor = Color(0xFF0070C1),
        boolean      = Color(0xFF0000FF),
        operator     = Color(0xFF000000),
        function     = Color(0xFF795E26),
        default      = Color(0xFF1E1E1E)
    )

// =============================================================================
// Syntax highlighting engine
// =============================================================================

private val CPP_KEYWORDS = setOf(
    "alignas", "alignof", "and", "and_eq", "asm", "auto", "bitand", "bitor",
    "break", "case", "catch", "class", "compl", "concept", "const",
    "consteval", "constexpr", "constinit", "const_cast", "continue",
    "co_await", "co_return", "co_yield", "decltype", "default", "delete",
    "do", "dynamic_cast", "else", "enum", "explicit", "export", "extern",
    "false", "for", "friend", "goto", "if", "inline", "mutable", "namespace",
    "new", "noexcept", "not", "not_eq", "nullptr", "operator", "or", "or_eq",
    "private", "protected", "public", "reinterpret_cast", "requires",
    "return", "sizeof", "static", "static_assert", "static_cast", "struct",
    "switch", "template", "this", "thread_local", "throw", "true", "try",
    "typedef", "typeid", "typename", "union", "using", "virtual", "volatile",
    "while", "xor", "xor_eq"
)

private val CPP_TYPES = setOf(
    "bool", "char", "char8_t", "char16_t", "char32_t", "double", "float",
    "int", "long", "short", "signed", "unsigned", "void", "wchar_t",
    "size_t", "ptrdiff_t", "int8_t", "int16_t", "int32_t", "int64_t",
    "uint8_t", "uint16_t", "uint32_t", "uint64_t", "string", "vector",
    "map", "set", "pair", "tuple", "array", "deque", "list", "stack",
    "queue", "priority_queue", "unordered_map", "unordered_set", "optional",
    "variant", "any", "span", "string_view", "unique_ptr", "shared_ptr",
    "weak_ptr"
)

private val CPP_BOOLEANS = setOf("true", "false", "nullptr", "NULL")

/**
 * Converts raw C++ [source] into a syntax-highlighted [AnnotatedString].
 *
 * Pure function — no side effects, safe inside `remember {}`.
 * Supports: keywords, types, booleans, preprocessor directives,
 * single-line comments, string/char literals, numeric literals,
 * function calls, and operators.
 */
fun highlightCpp(source: String, colors: SyntaxColors): AnnotatedString =
    buildAnnotatedString {
        val lines = source.lines()
        lines.forEachIndexed { lineIndex, line ->
            var i = 0
            while (i < line.length) {
                // Single-line comment  //…
                if (i + 1 < line.length && line[i] == '/' && line[i + 1] == '/') {
                    withStyle(SpanStyle(color = colors.comment, fontStyle = FontStyle.Italic)) {
                        append(line.substring(i))
                    }
                    i = line.length
                    continue
                }
                // Preprocessor  #…
                if (line[i] == '#') {
                    withStyle(SpanStyle(color = colors.preprocessor)) {
                        append(line.substring(i))
                    }
                    i = line.length
                    continue
                }
                // String literal  "…"
                if (line[i] == '"') {
                    val end = line.indexOf('"', i + 1)
                        .let { if (it == -1) line.length else it + 1 }
                    withStyle(SpanStyle(color = colors.string)) {
                        append(line.substring(i, end))
                    }
                    i = end
                    continue
                }
                // Char literal  '…'
                if (line[i] == '\'') {
                    val end = line.indexOf('\'', i + 1)
                        .let { if (it == -1) line.length else it + 1 }
                    withStyle(SpanStyle(color = colors.string)) {
                        append(line.substring(i, end))
                    }
                    i = end
                    continue
                }
                // Numeric literal
                if (line[i].isDigit()) {
                    val start = i
                    while (i < line.length &&
                        (line[i].isDigit() || line[i] == '.' || line[i] == 'f' ||
                                line[i] == 'x' || line[i] == 'u' || line[i] == 'L' ||
                                line[i] in 'a'..'f' || line[i] in 'A'..'F')
                    ) i++
                    withStyle(SpanStyle(color = colors.number)) {
                        append(line.substring(start, i))
                    }
                    continue
                }
                // Identifier → classify as keyword / type / boolean / function / default
                if (line[i].isLetter() || line[i] == '_') {
                    val start = i
                    while (i < line.length && (line[i].isLetterOrDigit() || line[i] == '_')) i++
                    val word = line.substring(start, i)
                    val isCall = i < line.length && line[i] == '('
                    val style = when {
                        word in CPP_KEYWORDS -> SpanStyle(
                            color      = colors.keyword,
                            fontWeight = FontWeight.Bold
                        )
                        word in CPP_TYPES    -> SpanStyle(color = colors.type)
                        word in CPP_BOOLEANS -> SpanStyle(
                            color      = colors.boolean,
                            fontWeight = FontWeight.Bold
                        )
                        isCall               -> SpanStyle(color = colors.function)
                        else                 -> SpanStyle(color = colors.default)
                    }
                    withStyle(style) { append(word) }
                    continue
                }
                // Operator characters
                if (line[i] in "+-*/%=<>!&|^~?:.") {
                    withStyle(SpanStyle(color = colors.operator)) { append(line[i]) }
                    i++
                    continue
                }
                // Whitespace / punctuation — no style
                append(line[i])
                i++
            }
            if (lineIndex < lines.lastIndex) append('\n')
        }
    }

// =============================================================================
// Search helpers
// =============================================================================

/**
 * Returns [base] with every case-insensitive occurrence of [query] overlaid
 * with a [highlightColor] background. Returns [base] unchanged when [query]
 * is blank.
 */
fun highlightSearchMatches(
    base: AnnotatedString,
    query: String,
    highlightColor: Color
): AnnotatedString {
    if (query.isBlank()) return base
    return buildAnnotatedString {
        append(base)
        val text = base.text
        var start = text.indexOf(query, ignoreCase = true)
        while (start != -1) {
            addStyle(
                SpanStyle(background = highlightColor, color = Color.Black),
                start,
                start + query.length
            )
            start = text.indexOf(query, start + query.length, ignoreCase = true)
        }
    }
}

/**
 * Returns the number of case-insensitive occurrences of [query] in [text].
 * Returns 0 when [query] is blank.
 */
fun countMatches(text: String, query: String): Int {
    if (query.isBlank()) return 0
    var count = 0
    var start = text.indexOf(query, ignoreCase = true)
    while (start != -1) {
        count++
        start = text.indexOf(query, start + query.length, ignoreCase = true)
    }
    return count
}

// =============================================================================
// FileInfoCard
// =============================================================================

/**
 * Card showing the file name, language, byte size, and status chips.
 */
@Composable
fun FileInfoCard(
    fileName: String,
    language: String,
    fileSizeBytes: Int,
    modifier: Modifier = Modifier
) {
    val sizeLabel = when {
        fileSizeBytes < 1_024 -> "$fileSizeBytes B"
        else -> "${"%.1f".format(fileSizeBytes / 1_024f)} KB"
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Code,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = fileName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FileInfoPill(label = language)
                FileInfoPill(label = sizeLabel)
                FileStatusChip(
                    label = "Read Only",
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                )
                FileStatusChip(
                    label = "Original Src",
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun FileInfoPill(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun FileStatusChip(
    label: String,
    leadingIcon: @Composable () -> Unit
) {
    AssistChip(
        onClick = {},
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        },
        leadingIcon = leadingIcon,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            labelColor = MaterialTheme.colorScheme.onTertiaryContainer,
            leadingIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        border = null
    )
}

// =============================================================================
// ReadOnlyBanner
// =============================================================================

/**
 * Informational banner explaining that the file is opened in read-only mode.
 */
@Composable
fun ReadOnlyBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "Original Source",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "This file is opened in read-only mode. " +
                            "Use \u201cOpen in Editor\u201d to modify it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

// =============================================================================
// CodeSearchBar
// =============================================================================

/**
 * Inline search field for filtering code.
 *
 * @param query         Current search text.
 * @param onQueryChange Called whenever the user changes the text.
 * @param matchCount    Number of matches to display in the trailing label.
 */
@Composable
fun CodeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    matchCount: Int,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Search in code" },
        placeholder = {
            Text(
                text = "Search in code\u2026",
                style = MaterialTheme.typography.bodySmall
            )
        },
        singleLine = true,
        trailingIcon = {
            if (query.isNotEmpty()) {
                Text(
                    text = "$matchCount match${if (matchCount != 1) "es" else ""}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        textStyle = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace
        )
    )
}

// =============================================================================
// ZoomControls
// =============================================================================

/**
 * Compact zoom strip: Zoom out / current size label / Zoom in / Reset.
 *
 * @param fontSize  Current font size (display only, clamping done by caller).
 * @param onZoomIn  Called when "+" is tapped.
 * @param onZoomOut Called when "−" is tapped.
 * @param onReset   Called when "↺" is tapped.
 */
@Composable
fun ZoomControls(
    fontSize: TextUnit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ZoomIconButton(label = "\u2212", description = "Zoom out", onClick = onZoomOut)
            Text(
                text = "${fontSize.value.roundToInt()}sp",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 36.dp)
            )
            ZoomIconButton(label = "+", description = "Zoom in", onClick = onZoomIn)
            ZoomIconButton(label = "\u21BA", description = "Reset zoom", onClick = onReset)
        }
    }
}

@Composable
private fun ZoomIconButton(label: String, description: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(36.dp)
            .semantics { contentDescription = description }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// =============================================================================
// CodePanel
// =============================================================================

/**
 * Renders [annotatedCode] with a line-number gutter on the left and
 * selectable, horizontally-scrollable code on the right.
 *
 * Vertical scrolling is managed by the caller's `Column + verticalScroll`.
 *
 * @param annotatedCode Syntax/search-highlighted [AnnotatedString].
 * @param fontSize      Current monospace font size.
 */
@Composable
fun CodePanel(
    annotatedCode: AnnotatedString,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val lines = annotatedCode.text.lines()
    val lineCount = lines.size

    val gutterWidth: Dp = when {
        lineCount < 100  -> 32.dp
        lineCount < 1000 -> 42.dp
        else             -> 54.dp
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // ── Gutter ───────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .width(gutterWidth)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(top = 16.dp, bottom = 16.dp, end = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                repeat(lineCount) { index ->
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = (fontSize.value * 0.85f).sp
                        ),
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.semantics {
                            contentDescription = "Line ${index + 1}"
                        }
                    )
                }
            }
            // ── Code body ────────────────────────────────────────────────────
            SelectionContainer {
                Text(
                    text = annotatedCode,
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(
                            start = 12.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = fontSize,
                        lineHeight = (fontSize.value * 1.6f).sp
                    ),
                    softWrap = false
                )
            }
        }
    }
}

// =============================================================================
// FABs
// =============================================================================

/**
 * Floating action button that triggers a copy-to-clipboard action.
 * The caller is responsible for writing to the clipboard and showing a Snackbar.
 */
@Composable
fun CopyCodeFab(
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onCopy,
        modifier = modifier.semantics { contentDescription = "Copy code" },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Icon(
            imageVector = Icons.Outlined.ContentCopy,
            contentDescription = null
        )
    }
}

/**
 * Extended FAB that exposes [onOpenEditor] so the caller can navigate to
 * the editor screen without this composable knowing about destinations.
 */
@Composable
fun OpenInEditorFab(
    onOpenEditor: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onOpenEditor,
        modifier = modifier.semantics { contentDescription = "Open in editor" },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null
            )
        },
        text = { Text("Open in Editor") },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

// =============================================================================
// CodeInfoBar
// =============================================================================

/**
 * Slim status bar displayed at the bottom of the screen.
 * Shows line count, character count, encoding, and language.
 */
@Composable
fun CodeInfoBar(
    lineCount: Int,
    charCount: Int,
    encoding: String = "UTF-8",
    language: String = "C++",
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CodeInfoBarItem(label = "Lines",    value = lineCount.toString())
            CodeInfoBarItem(label = "Chars",    value = charCount.toString())
            CodeInfoBarItem(label = "Encoding", value = encoding)
            CodeInfoBarItem(label = "Language", value = language)
        }
    }
}

@Composable
private fun CodeInfoBarItem(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// =============================================================================
// Empty state
// =============================================================================

/**
 * Full-screen empty state shown when the code string is blank.
 */
@Composable
fun EmptyCodeState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Code,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Text(
                text = "No code available.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Open a file from the home screen to get started.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

// =============================================================================
// Error state
// =============================================================================

/**
 * Reusable error state composable for future file-loading failures.
 *
 * @param message Human-readable description of what went wrong.
 */
@Composable
fun CodeErrorState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Error",
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Unable to load file",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
    }
}

// =============================================================================
// Loading shimmer
// =============================================================================

/**
 * Full-screen animated shimmer skeleton shown while code is loading.
 * Safe to reuse anywhere in the app.
 */
@Composable
fun CodeLoadingShimmer(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    @Composable
    fun ShimmerLine(widthFraction: Float, height: Dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .height(height)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            MaterialTheme.colorScheme.surfaceContainerHighest,
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        start = Offset(offset, 0f),
                        end = Offset(offset + 400f, 0f)
                    )
                )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ShimmerLine(widthFraction = 0.40f, height = 20.dp)
        Spacer(Modifier.height(4.dp))
        ShimmerLine(widthFraction = 0.90f, height = 14.dp)
        ShimmerLine(widthFraction = 0.70f, height = 14.dp)
        ShimmerLine(widthFraction = 0.85f, height = 14.dp)
        ShimmerLine(widthFraction = 0.50f, height = 14.dp)
        ShimmerLine(widthFraction = 0.95f, height = 14.dp)
        ShimmerLine(widthFraction = 0.60f, height = 14.dp)
        ShimmerLine(widthFraction = 0.80f, height = 14.dp)
        ShimmerLine(widthFraction = 0.75f, height = 14.dp)
        ShimmerLine(widthFraction = 0.55f, height = 14.dp)
        ShimmerLine(widthFraction = 0.88f, height = 14.dp)
        ShimmerLine(widthFraction = 0.65f, height = 14.dp)
    }
}