package com.example.trp.domain.navigation.graphs.teacher

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.graphs.common.Graph
import com.example.trp.ui.screens.teacher.GroupsTasksScreen
import com.example.trp.ui.screens.teacher.StudentsScreen
import com.example.trp.ui.screens.teacher.TaskInfoScreen

private const val TEACHER_DISCIPLINES_ID = "student_discipline_id"
private const val GROUP_ID = "group_id"
private const val TASK_ID = "task_id"

fun NavGraphBuilder.groupsNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.TEACHER_DISCIPLINES}/{$TEACHER_DISCIPLINES_ID}",
        arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${GroupsScreen.Groups.route}/{$TEACHER_DISCIPLINES_ID}"
    ) {
        composable(route = "${GroupsScreen.Groups.route}/{$TEACHER_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                GroupsTasksScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                        navController.navigate("${GroupsScreen.GroupInfo.route}/$groupId")
                    },
                    onTaskClick = { taskId ->
                        navController.navigate("${GroupsScreen.TaskInfo.route}/$taskId")
                    }
                )
            }
        }
        composable(
            route = "${GroupsScreen.GroupInfo.route}/{$GROUP_ID}",
            arguments = listOf(navArgument(GROUP_ID) { type = NavType.IntType })
        ) {
            val groupId = it.arguments?.getInt(GROUP_ID)
            groupId?.let { id ->
                StudentsScreen(
                    groupId = id,
                    onStudentClick = { studentId ->
                        // navController.navigate("${GroupsScreen.StudentInfo.route}/$studentId") TODO
                    }
                )
            }
        }
        composable(
            route = "${GroupsScreen.TaskInfo.route}/{$TASK_ID}",
            arguments = listOf(navArgument(TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TASK_ID)
            taskId?.let {
                TaskInfoScreen(taskId = taskId)
            }
        }
    }
}

sealed class GroupsScreen(val route: String) {
    object Groups : GroupsScreen(route = "checklist_GROUPS")
    object GroupInfo : GroupsScreen(route = "checklist_GROUP_INFO")
    object TaskInfo : GroupsScreen(route = "checklist_TASK_INFO")
    object StudentInfo : GroupsScreen(route = "checklist_STUDENT_INFO")
}