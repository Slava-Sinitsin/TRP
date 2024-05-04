package com.example.trp.domain.navigation.teacher

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.domain.navigation.common.RootNavGraph
import com.example.trp.ui.screens.common.MeScreen
import com.example.trp.ui.screens.teacher.TeacherBottomBarScreen
import com.example.trp.ui.screens.teacher.TeacherDisciplinesScreen
import com.example.trp.ui.screens.teacher.TeacherHomeScreen

@Composable
fun TeacherWelcomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.TEACHER_WELCOME,
        startDestination = TeacherBottomBarScreen.Home.route
    ) {
        composable(route = TeacherBottomBarScreen.TeacherDisciplines.route) {
            TeacherDisciplinesScreen(onDisciplineClick = { disciplineId ->
                navController.navigate("${Graph.TEACHER_DISCIPLINES}/${disciplineId}")
            })
        }
        composable(route = TeacherBottomBarScreen.Home.route) {
            TeacherHomeScreen(onEventClick = { taskId -> })
        }
        composable(route = TeacherBottomBarScreen.Me.route) {
            MeScreen(navController = navController)
        }
        composable(route = Graph.LOGIN) {
            RootNavGraph(startDestination = Graph.LOGIN, navController = rememberNavController())
        }
        teacherGroupsNavGraph(navController = navController)
        eventNavGraph(navController = navController)
    }
}