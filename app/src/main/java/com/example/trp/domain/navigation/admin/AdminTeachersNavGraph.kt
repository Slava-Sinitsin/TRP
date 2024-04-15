package com.example.trp.domain.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.TeacherInfoScreen

private const val ADMIN_TEACHER_ID = "admin_teacher_id"

fun NavGraphBuilder.teachersNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.ADMIN_TEACHERS}/{$ADMIN_TEACHER_ID}",
        arguments = listOf(navArgument(ADMIN_TEACHER_ID) { type = NavType.IntType }),
        startDestination = "${AdminTeachersScreen.TeacherInfo.route}/{$ADMIN_TEACHER_ID}"
    ) {
        composable(route = "${AdminTeachersScreen.TeacherInfo.route}/{$ADMIN_TEACHER_ID}") {
            val teacherId = it.arguments?.getInt(ADMIN_TEACHER_ID)
            teacherId?.let { id -> TeacherInfoScreen(teacherId = id) }
        }
    }
}

sealed class AdminTeachersScreen(val route: String) {
    object TeacherInfo : AdminTeachersScreen(route = "users_TEACHER_INFO")
}