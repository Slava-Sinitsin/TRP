package com.example.trp.data.mappers.disciplines

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Disciplines(
    @SerializedName("data") var list: List<com.example.trp.data.mappers.disciplines.DisciplineData>? = null
)