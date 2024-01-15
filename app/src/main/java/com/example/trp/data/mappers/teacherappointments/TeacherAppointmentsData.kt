package com.example.trp.data.mappers.teacherappointments

import com.example.trp.data.mappers.disciplines.DisciplineData
import com.google.gson.annotations.SerializedName

data class TeacherAppointmentsData(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("teacher") val teacher: com.example.trp.data.mappers.teacherappointments.Teacher? = null,
    @SerializedName("group") val group: com.example.trp.data.mappers.teacherappointments.Group? = null,
    @SerializedName("discipline") val discipline: com.example.trp.data.mappers.disciplines.DisciplineData? = null
)