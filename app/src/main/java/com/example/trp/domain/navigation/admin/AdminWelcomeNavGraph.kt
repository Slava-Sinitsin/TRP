package com.example.trp.domain.navigation.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.AddNewDisciplineScreen
import com.example.trp.ui.screens.admin.AdminBottomBarScreen
import com.example.trp.ui.screens.admin.AdminDisciplinesScreen
import com.example.trp.ui.screens.admin.AdminHomeScreen
import com.example.trp.ui.screens.common.MeScreen

private const val ADMIN_ADD_DISCIPLINE = "admin_add_discipline"

@Composable
fun AdminWelcomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ADMIN_WELCOME,
        startDestination = AdminBottomBarScreen.Home.route
    ) {
        composable(route = AdminBottomBarScreen.Curriculum.route) {
            AdminDisciplinesScreen(onDisciplineClick = { disciplineId ->
                navController.navigate("${Graph.ADMIN_DISCIPLINES}/${disciplineId}")
            },
                onAddDisciplineClick = {
                    navController.navigate("curriculum_${Graph.ADMIN_DISCIPLINES}/${ADMIN_ADD_DISCIPLINE}")
                })
        }
        composable(route = AdminBottomBarScreen.Home.route) {
            AdminHomeScreen()
        }
        composable(route = AdminBottomBarScreen.Me.route) {
            MeScreen()
        }
        composable(route = "curriculum_${Graph.ADMIN_DISCIPLINES}/${ADMIN_ADD_DISCIPLINE}") {
            AddNewDisciplineScreen(navController = navController)
        }
        curriculumNavGraph(navController = navController)
    }
}