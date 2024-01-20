package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Output(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: String? = null
)