package com.example.trp.ui.components

import androidx.compose.ui.graphics.Color

sealed class TaskStatus(
    val status: String,
    val progress: Float,
    val color: Color
) {
    object New : TaskStatus(
        status = "NEW",
        progress = 0.2f,
        color = Color(0xFFCA4754)
    )

    object InProgress : TaskStatus(
        status = "IN_PROGRESS",
        progress = 0.4f,
        color = Color(0xFFCA4754)
    )

    object OnTesting : TaskStatus(
        status = "ON_TESTING",
        progress = 0.6f,
        color = Color(0xFFE2B714)
    )

    object Tested : TaskStatus(
        status = "TESTED",
        progress = 0.8f,
        color = Color(0xFFE2B714)
    )

    object SentToCodeReview : TaskStatus(
        status = "SENT_TO_CODE_REVIEW",
        progress = 0.8f,
        color = Color(0xFF0073CF)
    )

    object CodeReview : TaskStatus(
        status = "CODE_REVIEW",
        progress = 1f,
        color = Color(0xFF0073CF)
    )

    object SentToRework : TaskStatus(
        status = "SENT_TO_REWORK",
        progress = 1f,
        color = Color(0xFFCA4754)
    )

    object WaitingForGrade : TaskStatus(
        status = "WAITING_FOR_GRADE",
        progress = 1f,
        color = Color(0xFF00CE7C)
    )

    object Rated : TaskStatus(
        status = "RATED",
        progress = 1f,
        color = Color(0xFF00CE7C)
    )
}