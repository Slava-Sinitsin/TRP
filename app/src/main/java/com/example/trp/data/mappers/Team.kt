package com.example.trp.data.mappers

import com.example.trp.data.mappers.tasks.Student
import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("teamNumber") val teamNumber: Int? = null,
    @SerializedName("students") val students: List<Student>? = null
)