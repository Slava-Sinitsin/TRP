package com.example.trp.domain.navigation.graphs.teacher

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.graphs.common.Graph
import com.example.trp.ui.screens.teacher.GroupsScreen
import com.example.trp.ui.screens.teacher.StudentsScreen

private const val TEACHER_DISCIPLINES_ID = "student_discipline_id"
private const val GROUP_ID = "group_id"

fun NavGraphBuilder.groupsNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.TEACHER_DISCIPLINES}/{$TEACHER_DISCIPLINES_ID}",
        arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${GroupsScreen.Groups.route}/{$TEACHER_DISCIPLINES_ID}"
    ) {
        composable(route = "${GroupsScreen.Groups.route}/{$TEACHER_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                GroupsScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                        navController.navigate("${GroupsScreen.GroupInfo.route}/$groupId")
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
                    groupId = groupId,
                    onStudentClick = { studentId ->
                        // navController.navigate("${GroupsScreen.StudentInfo.route}/$studentId") TODO
                    }
                )
            }
        }
    }
}

sealed class GroupsScreen(val route: String) {
    object Groups : GroupsScreen(route = "checklist_GROUPS")
    object GroupInfo : GroupsScreen(route = "checklist_GROUP_INFO")
    object StudentInfo : GroupsScreen(route = "checklist_STUDENT_INFO")
}