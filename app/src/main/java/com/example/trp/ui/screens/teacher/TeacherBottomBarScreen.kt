package com.example.trp.ui.screens.teacher

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TeacherBottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object TeacherDisciplines : TeacherBottomBarScreen(
        route = "checklist",
        title = "Checklist",
        icon = Icons.Filled.Checklist
    )

    object Home : TeacherBottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    )

    object Me : TeacherBottomBarScreen(
        route = "me",
        title = "Me",
        icon = Icons.Filled.Person
    )
}