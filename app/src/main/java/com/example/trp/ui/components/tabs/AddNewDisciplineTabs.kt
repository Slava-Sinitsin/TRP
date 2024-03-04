package com.example.trp.ui.components.tabs

sealed class AddNewDisciplineTabs(
    val title: String,
    val route: String,
) {
    object MainScreen : AddNewDisciplineTabs(
        title = "MainScreen",
        route = "main_screen",
    )

    object SelectScreen : AddNewDisciplineTabs(
        title = "SelectScreen",
        route = "select_screen",
    )
}