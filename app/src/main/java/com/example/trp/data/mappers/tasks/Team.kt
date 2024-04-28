package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("disciplineId") val disciplineId: Int? = null,
    @SerializedName("students") val students: List<Student>? = null,
    @SerializedName("leaderStudentId") val leaderStudentId: Int? = null
)