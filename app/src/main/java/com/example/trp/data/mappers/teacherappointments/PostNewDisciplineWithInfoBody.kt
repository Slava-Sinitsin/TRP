package com.example.trp.data.mappers.teacherappointments

import com.example.trp.data.mappers.disciplines.DisciplineData
import com.google.gson.annotations.SerializedName

data class PostNewDisciplineWithInfoBody(
    @SerializedName("groupIds") val groupIds: List<Int>? = null,
    @SerializedName("teacherId") val teacherId: Int? = null,
    @SerializedName("discipline") val discipline: DisciplineData? = null
)