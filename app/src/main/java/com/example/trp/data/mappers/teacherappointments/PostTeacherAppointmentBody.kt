package com.example.trp.data.mappers.teacherappointments

import com.google.gson.annotations.SerializedName

data class PostTeacherAppointmentBody(
    @SerializedName("groupId") val groupId: Int? = null,
    @SerializedName("teacherId") val teacherId: Int? = null,
    @SerializedName("disciplineId") val disciplineId: Int? = null
)