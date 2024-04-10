package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class StudentResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: Student? = null
)