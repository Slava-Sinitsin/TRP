package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class PostStudentAppointmentsResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: TeamAppointment? = null
)