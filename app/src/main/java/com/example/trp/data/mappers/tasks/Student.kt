package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("fullName") val fullName: String? = null
)