package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class PostTeamAppointmentsBody(
    @SerializedName("teamId") val teamId: Int? = null,
    @SerializedName("labWorkVariantId") val taskId: Int? = null
)