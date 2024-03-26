package com.example.trp.ui.components.tabs

sealed class UsersTabs(
    val title: String,
    val route: String,
) {
    object GroupsScreen : UsersTabs(
        title = "Groups",
        route = "groups_screen",
    )
    object TeachersScreen : UsersTabs(
        title = "Teachers",
        route = "teachers_screen",
    )
}