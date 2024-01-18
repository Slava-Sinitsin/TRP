package com.example.trp.domain.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("Splash_screen")
    object LoginScreen : Screen("Login_screen")
    object WelcomeScreen : Screen("Welcome_screen")
}
