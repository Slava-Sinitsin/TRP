package com.example.trp.ui.screens.teacher.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector

sealed class GroupsLabsTabs(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object Groups : GroupsLabsTabs(
        title = "Groups",
        icon = Icons.Filled.Groups,
        route = "groups",
    )

    object Labs : GroupsLabsTabs(
        title = "Labs",
        icon = Icons.Filled.ListAlt,
        route = "labs"
    )
}