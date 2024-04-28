package com.example.trp.ui.components.tabs

sealed class TaskTabs(
    val title: String,
    val route: String,
) {
    object Description : TaskTabs(
        title = "Description",
        route = "description",
    )

    object Solution : TaskTabs(
        title = "Solution",
        route = "solution"
    )

    object Review : TaskTabs(
        title = "Review",
        route = "review"
    )
}