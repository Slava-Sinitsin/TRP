package com.example.trp.domain.navigation.teacher

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.common.AddNewTaskScreen
import com.example.trp.ui.screens.common.TaskTestsInfoScreen
import com.example.trp.ui.screens.teacher.AddTaskToStudentScreen
import com.example.trp.ui.screens.teacher.StudentInfoScreen
import com.example.trp.ui.screens.teacher.StudentsScreen
import com.example.trp.ui.screens.teacher.TeacherGroupsTasksScreen

private const val TEACHER_DISCIPLINES_ID = "teacher_discipline_id"
private const val GROUP_ID = "group_id"
private const val STUDENT_ID = "student_id"
private const val TASK_ID = "task_id"

fun NavGraphBuilder.groupsNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.TEACHER_DISCIPLINES}/{$TEACHER_DISCIPLINES_ID}",
        arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${TeacherGroupsTasksScreen.GroupsTasks.route}/{$TEACHER_DISCIPLINES_ID}"
    ) {
        composable(route = "${TeacherGroupsTasksScreen.GroupsTasks.route}/{$TEACHER_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                TeacherGroupsTasksScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                        navController.navigate("${TeacherGroupsTasksScreen.GroupInfo.route}/$groupId")
                    },
                    onTaskClick = { taskId ->
                        navController.navigate("${TeacherGroupsTasksScreen.TaskInfo.route}/$taskId")
                    },
                    onAddTaskClick = { disciplineId ->
                        navController.navigate("${TeacherGroupsTasksScreen.AddNewTask.route}/$disciplineId")
                    }
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.GroupInfo.route}/{$GROUP_ID}",
            arguments = listOf(navArgument(GROUP_ID) { type = NavType.IntType })
        ) {
            val groupId = it.arguments?.getInt(GROUP_ID)
            groupId?.let { id ->
                StudentsScreen(
                    groupId = id,
                    onStudentClick = { studentId ->
                        navController.navigate("${TeacherGroupsTasksScreen.StudentInfo.route}/$studentId")
                    },
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.StudentInfo.route}/{$STUDENT_ID}",
            arguments = listOf(navArgument(STUDENT_ID) { type = NavType.IntType })
        ) {
            val studentId = it.arguments?.getInt(STUDENT_ID)
            studentId?.let { id ->
                StudentInfoScreen(
                    studentId = id,
                    onAddTaskToStudentClick = { studentId ->
                        navController.navigate("${TeacherGroupsTasksScreen.AddTaskToStudent.route}/$studentId")
                    },
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.AddTaskToStudent.route}/{$STUDENT_ID}",
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
            route = "${TeacherGroupsTasksScreen.TaskInfo.route}/{$TASK_ID}",
            arguments = listOf(navArgument(TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TASK_ID)
            taskId?.let { id ->
                TaskTestsInfoScreen(
                    taskId = id,
                    navController = navController,
                    onAddTestClick = { },
                    onTestClick = { }
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.AddNewTask.route}/{$TEACHER_DISCIPLINES_ID}",
            arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType })
        ) {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                AddNewTaskScreen(disciplineId = id, navController = navController)
            }
        }
    }
}

sealed class TeacherGroupsTasksScreen(val route: String) {
    object GroupsTasks : TeacherGroupsTasksScreen(route = "checklist_GROUPS_TASKS")
    object GroupInfo : TeacherGroupsTasksScreen(route = "checklist_GROUP_INFO")
    object TaskInfo : TeacherGroupsTasksScreen(route = "checklist_TASK_INFO")
    object AddNewTask : TeacherGroupsTasksScreen(route = "checklist_ADD_NEW_TASK")
    object StudentInfo : TeacherGroupsTasksScreen(route = "checklist_STUDENT_INFO")
    object AddTaskToStudent : TeacherGroupsTasksScreen(route = "checklist_ADD_TASK_TO_STUDENT")
}