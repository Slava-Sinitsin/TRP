package com.example.trp.ui.screens.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
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

    object Users : AdminBottomBarScreen(
        title = "Users",
        icon = Icons.Filled.People,
        route = "users"
    )

    object Me : AdminBottomBarScreen(
        title = "Me",
        icon = Icons.Filled.Person,
        route = "me",
    )
}