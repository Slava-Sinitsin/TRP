package com.example.trp.domain.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.AdminGroupInfoScreen
import com.example.trp.ui.screens.admin.AdminGroupsScreen
import com.example.trp.ui.screens.common.CreateNewTestScreen
import com.example.trp.ui.screens.common.CreateTaskScreen
import com.example.trp.ui.screens.common.TaskInfoTestsScreen

private const val ADMIN_DISCIPLINES_ID = "admin_discipline_id"
private const val ADMIN_TASK_ID = "admin_task_id"
private const val ADMIN_LAB_ID = "admin_lab_id"
private const val ADMIN_GROUP_ID = "admin_group_id"

fun NavGraphBuilder.curriculumNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.ADMIN_DISCIPLINES}/{$ADMIN_DISCIPLINES_ID}",
        arguments = listOf(navArgument(ADMIN_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${AdminGroupsLabsScreen.GroupsLabs.route}/{$ADMIN_DISCIPLINES_ID}"
    ) {
        composable(route = "${AdminGroupsLabsScreen.GroupsLabs.route}/{$ADMIN_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(ADMIN_DISCIPLINES_ID)
            disciplineId?.let { id ->
                AdminGroupsScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                         navController.navigate("${AdminGroupsLabsScreen.GroupInfo.route}/$groupId")
                    }
                )
            }
        }
        composable(
            route = "${AdminGroupsLabsScreen.GroupInfo.route}/{$ADMIN_GROUP_ID}",
            arguments = listOf(navArgument(ADMIN_GROUP_ID) { type = NavType.IntType })
        ) {
            val groupId = it.arguments?.getInt(ADMIN_GROUP_ID)
            groupId?.let { id ->
                AdminGroupInfoScreen(
                    groupId = id
                )
            }
        }
        composable(
            route = "${AdminGroupsLabsScreen.CreateTask.route}/{$ADMIN_LAB_ID}",
            arguments = listOf(navArgument(ADMIN_LAB_ID) { type = NavType.IntType })
        ) {
            val labId = it.arguments?.getInt(ADMIN_LAB_ID)
            labId?.let { id ->
                CreateTaskScreen(labId = id, navController = navController)
            }
        }
        composable(
            route = "${AdminGroupsLabsScreen.TaskInfo.route}/{$ADMIN_TASK_ID}",
            arguments = listOf(navArgument(ADMIN_TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(ADMIN_TASK_ID)
            taskId?.let { id ->
                TaskInfoTestsScreen(
                    taskId = id,
                    navController = navController,
                    onAddTestClick = { navController.navigate("${AdminGroupsLabsScreen.AddNewTest.route}/$id") },
                    onTestClick = { }
                )
            }
        }

        composable(
            route = "${AdminGroupsLabsScreen.AddNewTest.route}/{$ADMIN_TASK_ID}",
            arguments = listOf(navArgument(ADMIN_TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(ADMIN_TASK_ID)
            taskId?.let { id ->
                CreateNewTestScreen(
                    taskId = id,
                    navController = navController
                )
            }
        }
    }
}

sealed class AdminGroupsLabsScreen(val route: String) {
    object GroupsLabs : AdminGroupsLabsScreen(route = "curriculum_GROUPS_TASKS")
    object GroupInfo : AdminGroupsLabsScreen(route = "curriculum_GROUP_INFO")
    object TaskInfo : AdminGroupsLabsScreen(route = "curriculum_TASK_INFO")
    object CreateTask : AdminGroupsLabsScreen(route = "curriculum_CREATE_TASK")
    object AddNewTest : AdminGroupsLabsScreen(route = "curriculum_ADD_NEW_TEST")
}