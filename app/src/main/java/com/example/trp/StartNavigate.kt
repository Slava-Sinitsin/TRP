package com.example.trp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trp.screens.LoginScreen
import com.example.trp.screens.SplashScreen
import com.example.trp.ui.theme.TrpTheme

class StartNavigate : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrpTheme {
                SplashScreenNavigation()
            }
        }
    }
}

@Composable
fun SplashScreenNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "SplashScreen"
    ) {
        composable("SplashScreen") {
            SplashScreen(navController = navController)
        }
        composable("LoginScreen") {
            LoginScreen()
        }
    }
}