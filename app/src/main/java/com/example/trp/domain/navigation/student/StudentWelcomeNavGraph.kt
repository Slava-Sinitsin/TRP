package com.example.trp.domain.navigation.student

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.domain.navigation.common.RootNavGraph
import com.example.trp.ui.screens.common.MeScreen
import com.example.trp.ui.screens.student.DisciplinesScreen
import com.example.trp.ui.screens.student.StudentBottomBarScreen

@Composable
fun StudentWelcomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.STUDENT_WELCOME,
        startDestination = StudentBottomBarScreen.StudentDisciplines.route
    ) {
        composable(route = StudentBottomBarScreen.StudentDisciplines.route) {
            DisciplinesScreen(onDisciplineClick = { disciplineId ->
                navController.navigate("${Graph.STUDENT_DISCIPLINES}/${disciplineId}")
            })
        }
/*        composable(route = StudentBottomBarScreen.Home.route) { // TODO
            StudentHomeScreen()
        }*/
        composable(route = StudentBottomBarScreen.Me.route) {
            MeScreen(navController = navController)
        }
        composable(route = Graph.LOGIN) {
            RootNavGraph(startDestination = Graph.LOGIN, navController = rememberNavController())
        }
        tasksNavGraph(navController = navController)
    }
}