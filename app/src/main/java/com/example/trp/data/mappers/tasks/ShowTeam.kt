package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class ShowTeam(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("students") val students: List<Student>? = null
)