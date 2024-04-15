package com.example.trp.domain.navigation.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.AddNewDisciplineScreen
import com.example.trp.ui.screens.admin.AdminBottomBarScreen
import com.example.trp.ui.screens.admin.AdminDisciplinesScreen
import com.example.trp.ui.screens.admin.CreateGroupScreen
import com.example.trp.ui.screens.admin.CreateTeacherScreen
import com.example.trp.ui.screens.admin.GroupsTeachersScreen
import com.example.trp.ui.screens.common.MeScreen

private const val ADMIN_ADD_DISCIPLINE = "admin_add_discipline"
private const val ADMIN_CREATE_GROUP = "admin_create_group"
private const val ADMIN_CREATE_TEACHER = "admin_create_teacher"

@Composable
fun AdminWelcomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ADMIN_WELCOME,
        startDestination = AdminBottomBarScreen.Users.route
    ) {
        composable(route = AdminBottomBarScreen.Curriculum.route) {
            AdminDisciplinesScreen(
                onAddDisciplineClick = {
                    navController.navigate("curriculum_${Graph.ADMIN_DISCIPLINES}/${ADMIN_ADD_DISCIPLINE}")
                },
                onDisciplineClick = { disciplineId ->
                    navController.navigate("${Graph.ADMIN_DISCIPLINES}/${disciplineId}")
                }
            )
        }
        composable(route = AdminBottomBarScreen.Users.route) {
            GroupsTeachersScreen(
                onCreateGroupClick = { navController.navigate("users_${Graph.ADMIN_GROUPS}") },
                onGroupClick = { groupId -> navController.navigate("${Graph.ADMIN_GROUPS}/${groupId}") },
                onCreateTeacherClick = { navController.navigate("users_${Graph.ADMIN_TEACHERS}") },
                onTeacherClick = { teacherId -> navController.navigate("${Graph.ADMIN_TEACHERS}/${teacherId}") }
            )
        }
        composable(route = "users_${Graph.ADMIN_GROUPS}/${ADMIN_CREATE_GROUP}") {
            CreateGroupScreen(navController = navController)
        }
        composable(route = "users_${Graph.ADMIN_GROUPS}/${ADMIN_CREATE_TEACHER}") {
            CreateTeacherScreen(navController = navController)
        }
        composable(route = AdminBottomBarScreen.Me.route) {
            MeScreen()
        }
        composable(route = "curriculum_${Graph.ADMIN_DISCIPLINES}/${ADMIN_ADD_DISCIPLINE}") {
            AddNewDisciplineScreen(navController = navController)
        }
        curriculumNavGraph(navController = navController)
        adminGroupsNavGraph(navController = navController)
        teachersNavGraph(navController= navController)
    }
}