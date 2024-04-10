package com.example.trp.data.mappers.teacherappointments

import com.google.gson.annotations.SerializedName

data class DeleteGroupResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: String? = null
)