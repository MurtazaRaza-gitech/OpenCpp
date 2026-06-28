package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Reusable Custom Shapes according to Material Design 3 and technical requirements.
 */
object AppShapes {
    val Button = RoundedCornerShape(16.dp)
    val Card = RoundedCornerShape(20.dp)
    val Dialog = RoundedCornerShape(24.dp)
    val TextField = RoundedCornerShape(14.dp)
    val BottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
}
