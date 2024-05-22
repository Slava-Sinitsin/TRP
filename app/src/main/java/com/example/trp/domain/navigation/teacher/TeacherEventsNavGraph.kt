package com.example.trp.domain.navigation.teacher

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.common.OldCodeReviewScreen
import com.example.trp.ui.screens.teacher.TeacherTaskScreen

private const val TEAM_APPOINTMENT_ID = "team_appointment_id"
private const val CODE_REVIEW_ID = "code_review_id"

fun NavGraphBuilder.eventNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.TEACHER_EVENT}/{$TEAM_APPOINTMENT_ID}",
        arguments = listOf(navArgument(TEAM_APPOINTMENT_ID) { type = NavType.IntType }),
        startDestination = "${TeacherEventScreen.TeamTaskInfo.route}/{$TEAM_APPOINTMENT_ID}"
    ) {
        composable(
            route = "${TeacherEventScreen.TeamTaskInfo.route}/{$TEAM_APPOINTMENT_ID}",
            arguments = listOf(navArgument(TEAM_APPOINTMENT_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TEAM_APPOINTMENT_ID)
            taskId?.let { id ->
                TeacherTaskScreen(
                    teamAppointmentId = id,
                    onOldCodeReviewClick = { codeReviewId -> navController.navigate("${TeacherEventScreen.CodeReview.route}/$codeReviewId") },
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherEventScreen.CodeReview.route}/{$CODE_REVIEW_ID}",
            arguments = listOf(navArgument(CODE_REVIEW_ID) { type = NavType.IntType })
        ) {
            it.arguments?.run {
                val codeReviewId = getInt(CODE_REVIEW_ID)
                OldCodeReviewScreen(codeReviewId = codeReviewId)
            }
        }
    }
}

sealed class TeacherEventScreen(val route: String) {
    object TeamTaskInfo : TeacherEventScreen(route = "home_STUDENT_TASK_INFO")
    object CodeReview : TeacherEventScreen(route = "home_CODE_REVIEW")
}