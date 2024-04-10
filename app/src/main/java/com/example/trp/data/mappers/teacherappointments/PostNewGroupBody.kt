package com.example.trp.data.mappers.teacherappointments

import com.example.trp.data.mappers.tasks.PostNewStudentBody
import com.google.gson.annotations.SerializedName

data class PostNewGroupBody(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("students") val students: List<PostNewStudentBody>? = null
)