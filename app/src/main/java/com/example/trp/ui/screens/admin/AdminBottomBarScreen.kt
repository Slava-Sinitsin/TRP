package com.example.trp.ui.screens.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminBottomBarScreen(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object Curriculum : AdminBottomBarScreen(
        title = "Curriculum",
        icon = Icons.Filled.School,
        route = "curriculum",
    )

    object Home : AdminBottomBarScreen(
        title = "Home",
        icon = Icons.Filled.Home,
        route = "home"
    )

    object Me : AdminBottomBarScreen(
        title = "Me",
        icon = Icons.Filled.Person,
        route = "me",
    )
}