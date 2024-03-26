package com.example.trp.ui.components.tabs

sealed class CreateGroupTabs(
    val title: String,
    val route: String,
) {
    object MainScreen : CreateGroupTabs(
        title = "MainScreen",
        route = "main_screen",
    )

    object CreateStudentScreen : CreateGroupTabs(
        title = "CreateStudentScreen",
        route = "create_student_screen",
    )
}