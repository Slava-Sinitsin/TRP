package com.example.trp.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Discipline : BottomBarScreen(
        route = "disciplines",
        title = "Disciplines",
        icon = Icons.Filled.Task
    )

    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    )

    object Me : BottomBarScreen(
        route = "me",
        title = "Me",
        icon = Icons.Filled.Person
    )
}