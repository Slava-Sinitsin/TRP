package com.example.trp.ui.components.tabs

sealed class TaskTestsTabs(
    val title: String,
    val route: String,
) {
    object TaskInfo : TaskTestsTabs(
        title = "Info",
        route = "task_info",
    )

    object Tests : TaskTestsTabs(
        title = "Tests",
        route = "tests"
    )
}