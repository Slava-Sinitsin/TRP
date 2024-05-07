package com.example.trp.domain.navigation.teacher

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.common.CreateNewTestScreen
import com.example.trp.ui.screens.common.CreateTaskScreen
import com.example.trp.ui.screens.common.TaskInfoTestsScreen
import com.example.trp.ui.screens.student.OldCodeReviewScreen
import com.example.trp.ui.screens.teacher.AddTaskToTeamScreen
import com.example.trp.ui.screens.teacher.CreateLabScreen
import com.example.trp.ui.screens.teacher.CreateTeamScreen
import com.example.trp.ui.screens.teacher.TeacherGroupsLabsScreen
import com.example.trp.ui.screens.teacher.TeacherTaskScreen
import com.example.trp.ui.screens.teacher.TeacherTasksScreen
import com.example.trp.ui.screens.teacher.TeamInfoScreen
import com.example.trp.ui.screens.teacher.TeamsScreen

private const val TEACHER_DISCIPLINES_ID = "teacher_discipline_id"
private const val GROUP_ID = "group_id"
private const val STUDENT_ID = "student_id"
private const val TEAM_ID = "team_id"
private const val LAB_ID = "lab_id"
private const val TEACHER_TASK_ID = "teacher_task_id"
private const val TEAM_APPOINTMENT_ID = "team_appointment_id"
private const val CODE_REVIEW_ID = "code_review_id"

fun NavGraphBuilder.teacherGroupsNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.TEACHER_DISCIPLINES}/{$TEACHER_DISCIPLINES_ID}",
        arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType }),
        startDestination = "${TeacherGroupsTasksScreen.GroupsLabs.route}/{$TEACHER_DISCIPLINES_ID}"
    ) {
        composable(route = "${TeacherGroupsTasksScreen.GroupsLabs.route}/{$TEACHER_DISCIPLINES_ID}") {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                TeacherGroupsLabsScreen(
                    disciplineId = id,
                    onGroupClick = { groupId ->
                        navController.navigate("${TeacherGroupsTasksScreen.GroupInfo.route}/$groupId")
                    },
                    onLabClick = { labId ->
                        navController.navigate("${TeacherGroupsTasksScreen.Tasks.route}/$labId")
                    },
                    onCreateLabClick = { disciplineId ->
                        navController.navigate("${TeacherGroupsTasksScreen.CreateLab.route}/$disciplineId")
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
                TeamsScreen(
                    groupId = id,
                    onTeamClick = { teamId ->
                        navController.navigate("${TeacherGroupsTasksScreen.TeamInfo.route}/$teamId/$id")
                    },
                    onCreateTeamClick = { groupId ->
                        navController.navigate("${TeacherGroupsTasksScreen.CreateTeam.route}/$groupId")
                    },
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.TeamInfo.route}/{$TEAM_ID}/{$GROUP_ID}",
            arguments = listOf(
                navArgument(TEAM_ID) { type = NavType.IntType },
                navArgument(GROUP_ID) { type = NavType.IntType })
        ) {
            val teamId = it.arguments?.getInt(TEAM_ID)
            val groupId = it.arguments?.getInt(GROUP_ID)
            teamId?.let { nonNullTeamId ->
                groupId?.let { nonNullGroupId ->
                    TeamInfoScreen(
                        teamId = nonNullTeamId,
                        groupId = nonNullGroupId,
                        onAddTaskToTeamClick = { teamId, labId ->
                            navController.navigate("${TeacherGroupsTasksScreen.AddTaskToTeam.route}/$teamId/$labId")
                        },
                        onTaskClick = { taskId ->
                            navController.navigate("${TeacherGroupsTasksScreen.StudentTaskInfo.route}/$taskId")
                        },
                        navController = navController
                    )
                }
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.CreateTeam.route}/{$GROUP_ID}",
            arguments = listOf(navArgument(GROUP_ID) { type = NavType.IntType })
        ) {
            val groupId = it.arguments?.getInt(GROUP_ID)
            groupId?.let { id ->
                CreateTeamScreen(
                    groupId = id,
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.StudentTaskInfo.route}/{$TEAM_APPOINTMENT_ID}",
            arguments = listOf(navArgument(TEAM_APPOINTMENT_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TEAM_APPOINTMENT_ID)
            taskId?.let { id ->
                TeacherTaskScreen(
                    teamAppointmentId = id,
                    onOldCodeReviewClick = { codeReviewId -> navController.navigate("${TeacherGroupsTasksScreen.CodeReview.route}/$codeReviewId") },
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.CodeReview.route}/{$CODE_REVIEW_ID}",
            arguments = listOf(navArgument(CODE_REVIEW_ID) { type = NavType.IntType })
        ) {
            it.arguments?.run {
                val codeReviewId = getInt(CODE_REVIEW_ID)
                OldCodeReviewScreen(codeReviewId = codeReviewId)
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.AddTaskToTeam.route}/{$TEAM_ID}/{$LAB_ID}",
            arguments = listOf(
                navArgument(TEAM_ID) { type = NavType.IntType },
                navArgument(LAB_ID) { type = NavType.IntType }
            )
        ) {
            it.arguments?.run {
                val teamId = getInt(TEAM_ID)
                val labId = getInt(LAB_ID)
                AddTaskToTeamScreen(
                    teamId = teamId,
                    labId = labId,
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.Tasks.route}/{$LAB_ID}",
            arguments = listOf(navArgument(LAB_ID) { type = NavType.IntType })
        ) {
            val labId = it.arguments?.getInt(LAB_ID)
            labId?.let { id ->
                TeacherTasksScreen(
                    labId = id,
                    onCreateTaskClick = { labId ->
                        navController.navigate("${TeacherGroupsTasksScreen.CreateTask.route}/$labId")
                    },
                    onTaskClick = { taskId ->
                        navController.navigate("${TeacherGroupsTasksScreen.TaskInfo.route}/$taskId")
                    }
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.CreateTask.route}/{$LAB_ID}",
            arguments = listOf(navArgument(LAB_ID) { type = NavType.IntType })
        ) {
            val labId = it.arguments?.getInt(LAB_ID)
            labId?.let { id ->
                CreateTaskScreen(
                    labId = id,
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.TaskInfo.route}/{$TEACHER_TASK_ID}",
            arguments = listOf(navArgument(TEACHER_TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TEACHER_TASK_ID)
            taskId?.let { id ->
                TaskInfoTestsScreen(
                    taskId = id,
                    navController = navController,
                    onAddTestClick = { navController.navigate("${TeacherGroupsTasksScreen.AddNewTest.route}/$id") },
                    onTestClick = { }
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.AddNewTest.route}/{$TEACHER_TASK_ID}",
            arguments = listOf(navArgument(TEACHER_TASK_ID) { type = NavType.IntType })
        ) {
            val taskId = it.arguments?.getInt(TEACHER_TASK_ID)
            taskId?.let { id ->
                CreateNewTestScreen(
                    taskId = id,
                    navController = navController
                )
            }
        }
        composable(
            route = "${TeacherGroupsTasksScreen.CreateLab.route}/{$TEACHER_DISCIPLINES_ID}",
            arguments = listOf(navArgument(TEACHER_DISCIPLINES_ID) { type = NavType.IntType })
        ) {
            val disciplineId = it.arguments?.getInt(TEACHER_DISCIPLINES_ID)
            disciplineId?.let { id ->
                CreateLabScreen(disciplineId = id, navController = navController)
            }
        }
    }
}

sealed class TeacherGroupsTasksScreen(val route: String) {
    object GroupsLabs : TeacherGroupsTasksScreen(route = "checklist_GROUPS_LABS")
    object CreateLab : TeacherGroupsTasksScreen(route = "checklist_CREATE_LAB")
    object GroupInfo : TeacherGroupsTasksScreen(route = "checklist_GROUP_INFO")
    object CreateTeam : TeacherGroupsTasksScreen(route = "checklist_CREATE_TEAM")
    object TaskInfo : TeacherGroupsTasksScreen(route = "checklist_TASK_INFO")
    object Tasks : TeacherGroupsTasksScreen(route = "checklist_TASKS")
    object CreateTask : TeacherGroupsTasksScreen(route = "checklist_CREATE_TASK")
    object TeamInfo : TeacherGroupsTasksScreen(route = "checklist_STUDENT_INFO")
    object StudentTaskInfo : TeacherGroupsTasksScreen(route = "checklist_STUDENT_TASK_INFO")
    object AddTaskToTeam : TeacherGroupsTasksScreen(route = "checklist_ADD_TASK_TO_TEAM")
    object AddNewTest : TeacherGroupsTasksScreen(route = "checklist_ADD_NEW_TEST")
    object CodeReview : TeacherGroupsTasksScreen(route = "checklist_CODE_REVIEW")
}