package com.example.trp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class TRPColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tintColor: Color,
    val controlColor: Color,
    val errorColor: Color,
    val MyYellow: Color,
)

object TRPTheme {
    val colors: TRPColors
        @Composable
        get() = LocalTRPColors.current
}

data class TRPThemeSettings(
    val isDarkMode: Boolean,
)

val LocalTRPColors = staticCompositionLocalOf<TRPColors> {
    error("No colors provided")
}