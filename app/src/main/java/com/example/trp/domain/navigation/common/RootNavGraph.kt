package com.example.trp.domain.navigation.common

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trp.ui.screens.admin.AdminWelcomeScreen
import com.example.trp.ui.screens.common.LoginScreen
import com.example.trp.ui.screens.common.SplashScreen
import com.example.trp.ui.screens.student.StudentWelcomeScreen
import com.example.trp.ui.screens.teacher.TeacherWelcomeScreen

@Composable
fun RootNavGraph(startDestination: String = Graph.SPLASH, navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        composable(route = Graph.SPLASH) {
            SplashScreen(navigate = { destination ->
                navController.popBackStack()
                navController.navigate(destination)
            })
        }
        composable(route = Graph.LOGIN) {
            LoginScreen(onLogin = { destination ->
                navController.popBackStack()
                navController.navigate(destination)
            })
        }
        composable(route = Graph.STUDENT_WELCOME) {
            StudentWelcomeScreen()
        }
        composable(route = Graph.TEACHER_WELCOME) {
            TeacherWelcomeScreen()
        }
        composable(route = Graph.ADMIN_WELCOME) {
            AdminWelcomeScreen()
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val SPLASH = "splash_graph"
    const val LOGIN = "login_graph"
    const val STUDENT_WELCOME = "student_welcome_graph"
    const val STUDENT_DISCIPLINES = "student_disciplines_graph"
    const val TEACHER_WELCOME = "teacher_welcome_graph"
    const val TEACHER_DISCIPLINES = "teacher_disciplines_graph"
    const val TEACHER_EVENT = "teacher_event_graph"
    const val ADMIN_WELCOME = "admin_welcome_graph"
    const val ADMIN_DISCIPLINES = "admin_disciplines_graph"
    const val ADMIN_GROUPS = "admin_groups_graph"
    const val ADMIN_TEACHERS = "admin_teachers_graph"
}