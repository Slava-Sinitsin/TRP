package com.example.trp.domain.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.AdminGroupsTasksScreen
import com.example.trp.ui.screens.common.CreateTaskScreen
import com.example.trp.ui.screens.common.CreateNewTestScreen
import com.example.trp.ui.screens.common.TaskInfoTestsScreen

private const val ADMIN_DISCIPLINES_ID = "admin_discipline_id"
private const val ADMIN_TASK_ID = "admin_task_id"
private const val ADMIN_LAB_ID = "admin_lab_id"

fun NavGraphBuilder.curriculumNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.ADMIN_DISCIPLINES}/{$ADMIN_DISCIPLINES_ID}",
        arguments = listOf(navArgument(ADMIN_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${AdminGroupsTasksScreen.GroupsTasks.route}/{$ADMIN_DISCIPLINES_ID}"
    ) {
        composable(route = "${AdminGroupsTasksScreen.GroupsTasks.route}/{$ADMIN_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(ADMIN_DISCIPLINES_ID)
            disciplineId?.let { id ->
                AdminGroupsTasksScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                        // navController.navigate("${AdminGroupsScreen.GroupInfo.route}/$groupId") TODO
                    },
                    onAddTaskClick = { labId ->
                        navController.navigate("${AdminGroupsTasksScreen.CreateTask.route}/$labId")
                    },
                    onTaskClick = { taskId ->
                        navController.navigate("${AdminGroupsTasksScreen.TaskInfo.route}/$taskId")
                    }
                )
            }
        }
        composable(
            route = "${AdminGroupsTasksScreen.CreateTask.route}/{$ADMIN_LAB_ID}",
            arguments = listOf(navArgument(ADMIN_LAB_ID) { type = NavType.IntType })
        ) {
            val labId = it.arguments?.getInt(ADMIN_LAB_ID)
            labId?.let { id ->
                CreateTaskScreen(labId = id, navController = navController)
            }
        }
        composable(
            route = "${AdminGroupsTasksScreen.TaskInfo.route}/{$ADMIN_TASK_ID}",
            arguments = listOf(navArgument(ADMIN_TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(ADMIN_TASK_ID)
            taskId?.let { id ->
                TaskInfoTestsScreen(
                    taskId = id,
                    navController = navController,
                    onAddTestClick = { navController.navigate("${AdminGroupsTasksScreen.AddNewTest.route}/$id") },
                    onTestClick = { }
                )
            }
        }

        composable(
            route = "${AdminGroupsTasksScreen.AddNewTest.route}/{$ADMIN_TASK_ID}",
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

sealed class AdminGroupsTasksScreen(val route: String) {
    object GroupsTasks : AdminGroupsTasksScreen(route = "curriculum_GROUPS_TASKS")
    object GroupInfo : AdminGroupsTasksScreen(route = "curriculum_GROUP_INFO")
    object TaskInfo : AdminGroupsTasksScreen(route = "curriculum_TASK_INFO")
    object CreateTask : AdminGroupsTasksScreen(route = "curriculum_CREATE_TASK")
    object AddNewTest : AdminGroupsTasksScreen(route = "curriculum_ADD_NEW_TEST")
}