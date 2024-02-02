package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class PostStudentAppointmentsBody(
    @SerializedName("studentId") val studentId: Int? = null,
    @SerializedName("taskId") val taskId: Int? = null
)