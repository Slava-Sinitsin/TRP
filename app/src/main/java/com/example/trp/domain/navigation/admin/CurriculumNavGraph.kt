package com.example.trp.domain.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.AdminGroupsTasksScreen
import com.example.trp.ui.screens.common.AddNewTaskScreen
import com.example.trp.ui.screens.common.AddNewTestScreen
import com.example.trp.ui.screens.common.TaskTestsInfoScreen

private const val ADMIN_DISCIPLINES_ID = "admin_discipline_id"
private const val ADMIN_TASK_ID = "admin_task_id"

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
                    onTaskClick = { taskId ->
                        navController.navigate("${AdminGroupsTasksScreen.TaskInfo.route}/$taskId")
                    },
                    onAddTaskClick = { disciplineId ->
                        navController.navigate("${AdminGroupsTasksScreen.AddNewTask.route}/$disciplineId")
                    }
                )
            }
        }
        composable(
            route = "${AdminGroupsTasksScreen.AddNewTask.route}/{$ADMIN_DISCIPLINES_ID}",
            arguments = listOf(navArgument(ADMIN_DISCIPLINES_ID) { type = NavType.IntType })
        ) {
            val disciplineId = it.arguments?.getInt(ADMIN_DISCIPLINES_ID)
            disciplineId?.let { id ->
                AddNewTaskScreen(disciplineId = id, navController = navController)
            }
        }
        composable(
            route = "${AdminGroupsTasksScreen.TaskInfo.route}/{$ADMIN_TASK_ID}",
            arguments = listOf(navArgument(ADMIN_TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(ADMIN_TASK_ID)
            taskId?.let { id ->
                TaskTestsInfoScreen(
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
                AddNewTestScreen(
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
    object AddNewTask : AdminGroupsTasksScreen(route = "curriculum_ADD_NEW_TASK")
    object AddNewTest : AdminGroupsTasksScreen(route = "curriculum_ADD_NEW_TEST")
}