package com.example.trp.ui.components

import androidx.compose.ui.graphics.Color

sealed class TaskStatus(
    val status: String,
    val float: Float,
    val color: Color
) {
    object New : TaskStatus(
        status = "NEW",
        float = 0.25f,
        color = Color(0xFFCA4754)
    )

    object InProgress : TaskStatus(
        status = "IN_PROGRESS",
        float = 0.5f,
        color = Color(0xFFE2B714)
    )

    object Complete : TaskStatus(
        status = "COMPLETE",
        float = 1f,
        color = Color(0xFF00CE7C)
    )
}