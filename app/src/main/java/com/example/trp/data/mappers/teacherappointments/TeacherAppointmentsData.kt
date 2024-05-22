package com.example.trp.data.mappers.teacherappointments

import com.example.trp.data.mappers.disciplines.DisciplineData
import com.google.gson.annotations.SerializedName

data class TeacherAppointmentsData(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("teacherId") val teacherId: Int? = null,
    @SerializedName("groupId") val groupId: Int? = null,
    @SerializedName("disciplineId") val disciplineId: Int? = null,
    @SerializedName("teacher") val teacher: Teacher? = null,
    @SerializedName("group") val group: Group? = null,
    @SerializedName("discipline") val discipline: DisciplineData? = null
)