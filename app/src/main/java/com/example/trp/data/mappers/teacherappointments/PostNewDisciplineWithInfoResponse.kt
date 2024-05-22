package com.example.trp.data.mappers.teacherappointments

import com.google.gson.annotations.SerializedName

data class PostNewDisciplineWithInfoResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: String? = null
)