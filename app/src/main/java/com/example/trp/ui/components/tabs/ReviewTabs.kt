package com.example.trp.ui.components.tabs

sealed class ReviewTabs(
    val title: String,
    val route: String,
) {
    object Description : ReviewTabs(
        title = "Description",
        route = "description",
    )

    object Review : ReviewTabs(
        title = "Review",
        route = "review",
    )

    object History : ReviewTabs(
        title = "History",
        route = "history"
    )
}