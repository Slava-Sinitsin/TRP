package com.example.trp.data.mappers.teacherappointments

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null
)