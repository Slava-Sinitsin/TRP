package com.example.trp.domain.navigation.graphs.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.graphs.common.Graph
import com.example.trp.ui.screens.admin.AdminGroupsTasksScreen

private const val ADMIN_DISCIPLINES_ID = "admin_discipline_id"

fun NavGraphBuilder.curriculumNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.ADMIN_DISCIPLINES}/{$ADMIN_DISCIPLINES_ID}",
        arguments = listOf(navArgument(ADMIN_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${AdminGroupsScreen.Groups.route}/{$ADMIN_DISCIPLINES_ID}"
    ) {
        composable(route = "${AdminGroupsScreen.Groups.route}/{$ADMIN_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(ADMIN_DISCIPLINES_ID)
            disciplineId?.let { id ->
                AdminGroupsTasksScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                        // navController.navigate("${AdminGroupsScreen.GroupInfo.route}/$groupId") TODO
                    },
                    onTaskClick = { taskId ->
                        // navController.navigate("${AdminGroupsScreen.TaskInfo.route}/$taskId") TODO
                    },
                    onAddTaskClick = { disciplineId ->
                        // navController.navigate("${AdminGroupsScreen.AddNewTask.route}/$disciplineId") TODO
                    }
                )
            }
        }
    }
}

sealed class AdminGroupsScreen(val route: String) {
    object Groups : AdminGroupsScreen(route = "curriculum_GROUPS")
    object GroupInfo : AdminGroupsScreen(route = "curriculum_GROUP_INFO")
    object TaskInfo : AdminGroupsScreen(route = "curriculum_TASK_INFO")
    object AddNewTask : AdminGroupsScreen(route = "curriculum_ADD_NEW_TASK")
}