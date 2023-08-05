package com.example.trp.navigation.graphs

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.ui.screens.TaskScreen
import com.example.trp.ui.screens.TasksScreen

private const val DISCIPLINE_ID = "discipline_id"
private const val TASK_ID = "task_id"

fun NavGraphBuilder.tasksNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.TASKS}/{$DISCIPLINE_ID}",
        arguments = listOf(navArgument(DISCIPLINE_ID) { type = NavType.IntType }),
        startDestination = "${TasksScreen.Tasks.route}/{$DISCIPLINE_ID}"
    ) {
        composable(route = "${TasksScreen.Tasks.route}/{$DISCIPLINE_ID}") {
            val disciplineId = it.arguments?.getInt(DISCIPLINE_ID)
            disciplineId?.let { id ->
                TasksScreen(
                    disciplineId = id,
                    onTaskClick = { taskId ->
                        navController.navigate("${TasksScreen.TaskInfo.route}/$taskId")
                        Log.e("TAG", navController.currentDestination.toString())
                    }
                )
            }
        }
        composable(
            route = "${TasksScreen.TaskInfo.route}/{$TASK_ID}",
            arguments = listOf(navArgument(TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TASK_ID)
            taskId?.let { id ->
                TaskScreen(taskId = id)
            }
        }
    }
}

sealed class TasksScreen(val route: String) {
    object Tasks : TasksScreen(route = "disciplines_TASKS")
    object TaskInfo : TasksScreen(route = "disciplines_TASK_INFO")
}