package com.example.trp.domain.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.AdminUsersGroupInfoScreen
import com.example.trp.ui.screens.admin.CreateStudentScreen

private const val ADMIN_GROUP_ID = "admin_group_id"

fun NavGraphBuilder.adminGroupsNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.ADMIN_GROUPS}/{$ADMIN_GROUP_ID}",
        arguments = listOf(navArgument(ADMIN_GROUP_ID) { type = NavType.IntType }),
        startDestination = "${AdminGroupsTeachersScreen.GroupInfo.route}/{$ADMIN_GROUP_ID}"
    ) {
        composable(route = "${AdminGroupsTeachersScreen.GroupInfo.route}/{$ADMIN_GROUP_ID}") {
            val groupId = it.arguments?.getInt(ADMIN_GROUP_ID)
            groupId?.let { id ->
                AdminUsersGroupInfoScreen(
                    groupId = id,
                    onCreateStudentClick = { groupId -> navController.navigate("${AdminGroupsTeachersScreen.CreateStudent.route}/$groupId") }
                )
            }
        }
        composable(
            route = "${AdminGroupsTeachersScreen.CreateStudent.route}/{$ADMIN_GROUP_ID}",
            arguments = listOf(navArgument(ADMIN_GROUP_ID) { type = NavType.IntType })
        ) {
            val groupId = it.arguments?.getInt(ADMIN_GROUP_ID)
            groupId?.let { id ->
                CreateStudentScreen(
                    groupId = id,
                    navController = navController
                )
            }
        }
    }
}

sealed class AdminGroupsTeachersScreen(val route: String) {
    object GroupInfo : AdminGroupsTeachersScreen(route = "users_GROUP_INFO")
    object CreateStudent : AdminGroupsTeachersScreen(route = "users_CREATE_STUDENT")
}