package com.example.trp.ui.screens.bottombar

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
    object Tasks : BottomBarScreen(
        route = "Tasks",
        title = "Tasks",
        icon = Icons.Filled.Task
    )

    object Home : BottomBarScreen(
        route = "Home",
        title = "Home",
        icon = Icons.Filled.Home
    )

    object Me : BottomBarScreen(
        route = "Me",
        title = "Me",
        icon = Icons.Filled.Person
    )
}