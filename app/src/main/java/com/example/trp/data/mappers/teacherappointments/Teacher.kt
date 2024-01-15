package com.example.trp.data.mappers.teacherappointments

import com.google.gson.annotations.SerializedName

data class Teacher(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("role") val role: String? = null
)