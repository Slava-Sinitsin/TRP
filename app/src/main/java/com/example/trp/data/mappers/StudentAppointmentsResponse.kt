package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class StudentAppointmentsResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: List<StudentAppointments>? = null
)