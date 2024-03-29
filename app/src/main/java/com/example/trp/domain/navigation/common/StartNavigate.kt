package com.example.trp.domain.navigation.common

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.theme.TRPThemeDefaultSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartNavigate :
    ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode = isSystemInDarkTheme()
            val trpThemeSettings by remember {
                mutableStateOf(
                    TRPThemeDefaultSettings.copy(
                        isDarkMode = isDarkMode
                    )
                )
            }
            TRPTheme(
                TRPThemeSettings = trpThemeSettings
            ) {
                RootNavGraph(navController = rememberNavController())
            }
        }
    }
}