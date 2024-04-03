package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class TeamAppointments(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("teamId") val teamId: Int? = null,
    @SerializedName("labWorkVariantId") val taskId: Int? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("grade") val grade: Int? = null,
    @SerializedName("codeReviewUrl") val codeReviewUrl: String? = null
)