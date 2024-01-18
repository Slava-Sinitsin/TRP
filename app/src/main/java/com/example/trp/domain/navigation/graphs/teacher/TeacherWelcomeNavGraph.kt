package com.example.trp.domain.navigation.graphs.teacher

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trp.domain.navigation.graphs.common.Graph
import com.example.trp.ui.screens.MeScreen
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
            TeacherHomeScreen()
        }
        composable(route = TeacherBottomBarScreen.Me.route) {
            MeScreen()
        }
        groupsNavGraph(navController = navController)
    }
}