package com.example.trp.data.mappers

import com.example.trp.data.mappers.tasks.CodeReview
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Team
import com.google.gson.annotations.SerializedName

data class TeamAppointment(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("team") val team: Team? = null,
    @SerializedName("labWorkVariant") val task: Task? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("codeReviewIds") val codeReviewIds: List<Int>? = null,
    @SerializedName("codeReviews") val codeReviews: List<CodeReview>? = null,
    @SerializedName("ratings") val ratings: List<Rating>? = null
)