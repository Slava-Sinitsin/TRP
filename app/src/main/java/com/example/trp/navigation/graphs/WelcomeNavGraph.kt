package com.example.trp.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trp.ui.screens.HomeScreen
import com.example.trp.ui.screens.MeScreen
import com.example.trp.ui.screens.TasksScreen
import com.example.trp.ui.screens.bottombar.BottomBarScreen

@Composable
fun WelcomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.WELCOME,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Tasks.route) {
            TasksScreen()
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreen.Me.route) {
            MeScreen()
        }
    }
}