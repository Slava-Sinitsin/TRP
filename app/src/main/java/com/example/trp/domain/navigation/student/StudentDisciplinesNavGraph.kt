package com.example.trp.domain.navigation.student

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.common.OldCodeReviewScreen
import com.example.trp.ui.screens.student.TaskScreen
import com.example.trp.ui.screens.student.TasksScreen

private const val STUDENT_DISCIPLINE_ID = "student_discipline_id"
private const val TEAM_APPOINTMENT_ID = "team_appointment_id"
private const val CODE_REVIEW_ID = "code_review_id"

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
            route = "${TasksScreen.TaskInfo.route}/{$TEAM_APPOINTMENT_ID}",
            arguments = listOf(navArgument(TEAM_APPOINTMENT_ID) { type = NavType.IntType })
        ) {
            val teamAppointmentId = it.arguments?.getInt(TEAM_APPOINTMENT_ID)
            teamAppointmentId?.let { id ->
                TaskScreen(
                    teamAppointmentId = id,
                    navController = navController,
                    onOldCodeReviewClick = { codeReviewId -> navController.navigate("${TasksScreen.CodeReview.route}/$codeReviewId") }
                )
            }
        }
        composable(
            route = "${TasksScreen.CodeReview.route}/{$CODE_REVIEW_ID}",
            arguments = listOf(navArgument(CODE_REVIEW_ID) { type = NavType.IntType })
        ) {
            val codeReviewId = it.arguments?.getInt(CODE_REVIEW_ID)
            codeReviewId?.let { id ->
                OldCodeReviewScreen(codeReviewId = id)
            }
        }
    }
}

sealed class TasksScreen(val route: String) {
    object Tasks : TasksScreen(route = "disciplines_TASKS")
    object TaskInfo : TasksScreen(route = "disciplines_TASK_INFO")
    object CodeReview : TasksScreen(route = "disciplines_CODE_REVIEW")
}