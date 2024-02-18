package com.example.trp.ui.components.tabs

sealed class GroupsTasksTabs(
    val title: String,
    val route: String,
) {
    object Groups : GroupsTasksTabs(
        title = "Groups",
        route = "groups",
    )

    object Tasks : GroupsTasksTabs(
        title = "Tasks",
        route = "tasks"
    )
}