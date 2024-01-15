package com.example.trp.ui.screens.student

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.vector.ImageVector

sealed class StudentBottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object StudentDisciplines : StudentBottomBarScreen(
        route = "disciplines",
        title = "Disciplines",
        icon = Icons.Filled.Task
    )

    object Home : StudentBottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    )

    object Me : StudentBottomBarScreen(
        route = "me",
        title = "Me",
        icon = Icons.Filled.Person
    )
}