package com.example.trp.data.mappers.disciplines

import com.google.gson.annotations.SerializedName

data class DisciplineResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val disciplineData: DisciplineData? = null
)