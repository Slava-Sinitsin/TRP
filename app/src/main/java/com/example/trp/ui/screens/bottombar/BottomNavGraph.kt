package com.example.trp.ui.screens.bottombar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trp.ui.screens.HomeScreen
import com.example.trp.ui.screens.MeScreen
import com.example.trp.ui.screens.TasksScreen


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
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
