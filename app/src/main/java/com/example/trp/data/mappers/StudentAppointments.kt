package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class StudentAppointments(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("studentId") val studentId: Int? = null,
    @SerializedName("taskId") val taskId: Int? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("grade") val grade: Int? = null,
    @SerializedName("codeReviewUrl") val codeReviewUrl: String? = null
)