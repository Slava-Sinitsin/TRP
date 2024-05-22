package com.example.trp.ui.screens.teacher

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TeacherBottomBarScreen(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object Checklist : TeacherBottomBarScreen(
        title = "Checklist",
        icon = Icons.Filled.Checklist,
        route = "checklist",
    )

    object Home : TeacherBottomBarScreen(
        title = "Home",
        icon = Icons.Filled.Home,
        route = "home"
    )

    object Me : TeacherBottomBarScreen(
        title = "Me",
        icon = Icons.Filled.Person,
        route = "me",
    )
}