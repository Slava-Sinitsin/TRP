package com.example.trp.domain.navigation.teacher

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.teacher.AddNewTaskScreen
import com.example.trp.ui.screens.teacher.AddTaskToStudentScreen
import com.example.trp.ui.screens.teacher.TeacherGroupsTasksScreen
import com.example.trp.ui.screens.teacher.StudentInfoScreen
import com.example.trp.ui.screens.teacher.StudentsScreen
import com.example.trp.ui.screens.teacher.TaskInfoScreen

private const val TEACHER_DISCIPLINES_ID = "teacher_discipline_id"
private const val GROUP_ID = "group_id"
private const val STUDENT_ID = "student_id"
private const val TASK_ID = "task_id"

fun NavGraphBuilder.groupsNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.TEACHER_DISCIPLINES}/{$TEACHER_DISCIPLINES_ID}",
        arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${TeacherGroupsScreen.Groups.route}/{$TEACHER_DISCIPLINES_ID}"
    ) {
        composable(route = "${TeacherGroupsScreen.Groups.route}/{$TEACHER_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                TeacherGroupsTasksScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                        navController.navigate("${TeacherGroupsScreen.GroupInfo.route}/$groupId")
                    },
                    onTaskClick = { taskId ->
                        navController.navigate("${TeacherGroupsScreen.TaskInfo.route}/$taskId")
                    },
                    onAddTaskClick = { disciplineId ->
                        navController.navigate("${TeacherGroupsScreen.AddNewTask.route}/$disciplineId")
                    }
                )
            }
        }
        composable(
            route = "${TeacherGroupsScreen.GroupInfo.route}/{$GROUP_ID}",
            arguments = listOf(navArgument(GROUP_ID) { type = NavType.IntType })
        ) {
            val groupId = it.arguments?.getInt(GROUP_ID)
            groupId?.let { id ->
                StudentsScreen(
                    groupId = id,
                    onStudentClick = { studentId ->
                        navController.navigate("${TeacherGroupsScreen.StudentInfo.route}/$studentId")
                    },
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsScreen.StudentInfo.route}/{$STUDENT_ID}",
            arguments = listOf(navArgument(STUDENT_ID) { type = NavType.IntType })
        ) {
            val studentId = it.arguments?.getInt(STUDENT_ID)
            studentId?.let { id ->
                StudentInfoScreen(
                    studentId = id,
                    onAddTaskToStudentClick = { studentId ->
                        navController.navigate("${TeacherGroupsScreen.AddTaskToStudent.route}/$studentId")
                    },
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsScreen.AddTaskToStudent.route}/{$STUDENT_ID}",
            arguments = listOf(navArgument(STUDENT_ID) { type = NavType.IntType })
        ) {
            val studentId = it.arguments?.getInt(STUDENT_ID)
            studentId?.let { id ->
                AddTaskToStudentScreen(
                    studentId = id,
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsScreen.TaskInfo.route}/{$TASK_ID}",
            arguments = listOf(navArgument(TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TASK_ID)
            taskId?.let { id ->
                TaskInfoScreen(taskId = id, navController = navController)
            }
        }
        composable(
            route = "${TeacherGroupsScreen.AddNewTask.route}/{$TEACHER_DISCIPLINES_ID}",
            arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType })
        ) {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                AddNewTaskScreen(disciplineId = id, navController = navController)
            }
        }
    }
}

sealed class TeacherGroupsScreen(val route: String) {
    object Groups : TeacherGroupsScreen(route = "checklist_GROUPS")
    object GroupInfo : TeacherGroupsScreen(route = "checklist_GROUP_INFO")
    object TaskInfo : TeacherGroupsScreen(route = "checklist_TASK_INFO")
    object AddNewTask : TeacherGroupsScreen(route = "checklist_ADD_NEW_TASK")
    object StudentInfo : TeacherGroupsScreen(route = "checklist_STUDENT_INFO")
    object AddTaskToStudent : TeacherGroupsScreen(route = "checklist_ADD_TASK_TO_STUDENT")
}