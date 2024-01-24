package com.example.trp.ui.screens.teacher.tabs

sealed class GroupsLabsTabs(
    val title: String,
    val route: String,
) {
    object Groups : GroupsLabsTabs(
        title = "Groups",
        route = "groups",
    )

    object Labs : GroupsLabsTabs(
        title = "Labs",
        route = "labs"
    )
}