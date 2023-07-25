package com.example.trp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trp.ui.screens.SplashScreen
import com.example.trp.ui.screens.WelcomeScreen

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH
    ) {
        authNavGraph(navController = navController)
        composable(route = Graph.SPLASH) {
            SplashScreen(navigate = {
                navController.popBackStack()
                navController.navigate(Graph.AUTHENTICATION)
            })
        }
        composable(route = Graph.WELCOME) {
            WelcomeScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val SPLASH = "splash_graph"
    const val AUTHENTICATION = "auth_graph"
    const val WELCOME = "welcome_graph"
}