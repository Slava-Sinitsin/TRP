package com.example.trp.domain.navigation.admin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.screens.admin.GroupInfoScreen

private const val ADMIN_GROUP_ID = "admin_group_id"

fun NavGraphBuilder.groupsNavGraph(navController: NavHostController) {
    navigation(
        route = "${Graph.ADMIN_GROUPS}/{$ADMIN_GROUP_ID}",
        arguments = listOf(navArgument(ADMIN_GROUP_ID) { type = NavType.IntType }),
        startDestination = "${AdminGroupsTeachersScreen.GroupInfo.route}/{$ADMIN_GROUP_ID}"
    ) {
        composable(route = "${AdminGroupsTeachersScreen.GroupInfo.route}/{$ADMIN_GROUP_ID}") {
            val groupId = it.arguments?.getInt(ADMIN_GROUP_ID)
            groupId?.let { id ->
                GroupInfoScreen(groupId = id,
                    onCreateStudentClick = { id -> })
            }
        }
    }
}

sealed class AdminGroupsTeachersScreen(val route: String) {
    object GroupInfo : AdminGroupsTeachersScreen(route = "users_GROUP_INFO")
}