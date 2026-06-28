package com.example.ui.theme

/**
 * Enumeration of all supported themes in the OpenCpp dynamic theme engine.
 */
enum class AppTheme(val displayName: String, val isDark: Boolean) {
    VS_CODE_DARK("VS Code Dark", true),
    GITHUB_DARK("GitHub Dark", true),
    DRACULA("Dracula", true),
    NORD("Nord", true),
    MIDNIGHT_BLUE("Midnight Blue", true),
    
    MATERIAL_LIGHT("Material Light", false),
    GITHUB_LIGHT("GitHub Light", false),
    BLUE_GRAY("Blue Gray", false),
    GREEN_PROFESSIONAL("Green Professional", false),
    PURPLE_MODERN("Purple Modern", false)
}
