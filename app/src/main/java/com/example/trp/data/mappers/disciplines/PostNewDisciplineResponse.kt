package com.example.trp.data.mappers.disciplines

import com.google.gson.annotations.SerializedName

data class PostNewDisciplineResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: PostNewDisciplineBody? = null
)