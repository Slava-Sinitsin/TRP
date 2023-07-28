package com.example.trp.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.trp.ui.screens.TaskScreen
import com.example.trp.ui.screens.TasksScreen

fun NavGraphBuilder.disciplineNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.TASKS,
        startDestination = DisciplineScreen.Tasks.route
    ) {
        composable(route = DisciplineScreen.Tasks.route) {
            TasksScreen(onTaskClick = {
                navController.navigate(DisciplineScreen.TaskInfo.route)
            })
        }
        composable(route = DisciplineScreen.TaskInfo.route) {
            TaskScreen()
        }
    }
}

sealed class DisciplineScreen(val route: String) {
    object Tasks : DisciplineScreen(route = "TASKS")
    object TaskInfo : DisciplineScreen(route = "TASK_INFO")
}