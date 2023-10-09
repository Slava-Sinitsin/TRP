package com.example.trp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun TRPTheme(
    TRPThemeSettings: TRPThemeSettings = TRPThemeDefaultSettings,
    content: @Composable () -> Unit
) = with(TRPThemeSettings) {
    val colors = when (isDarkMode) {
        true -> darkScheme
        false -> lightScheme
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = colors.myYellow
        )
    }

    CompositionLocalProvider(
        LocalTRPColors provides colors,
        content = content
    )
}

val TRPThemeDefaultSettings = TRPThemeSettings(
    isDarkMode = true
)