package com.example.trp.domain.navigation.graphs.student

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.graphs.common.Graph
import com.example.trp.ui.screens.student.TaskScreen
import com.example.trp.ui.screens.student.TasksScreen

private const val STUDENT_DISCIPLINE_ID = "student_discipline_id"
private const val TASK_ID = "task_id"

fun NavGraphBuilder.tasksNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.STUDENT_DISCIPLINES}/{$STUDENT_DISCIPLINE_ID}",
        arguments = listOf(navArgument(STUDENT_DISCIPLINE_ID) { type = NavType.IntType }),
        startDestination = "${TasksScreen.Tasks.route}/{$STUDENT_DISCIPLINE_ID}"
    ) {
        composable(route = "${TasksScreen.Tasks.route}/{$STUDENT_DISCIPLINE_ID}") {
            val disciplineId = it.arguments?.getInt(STUDENT_DISCIPLINE_ID)
            disciplineId?.let { id ->
                TasksScreen(
                    disciplineId = id,
                    onTaskClick = { taskId ->
                        navController.navigate("${TasksScreen.TaskInfo.route}/$taskId")
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