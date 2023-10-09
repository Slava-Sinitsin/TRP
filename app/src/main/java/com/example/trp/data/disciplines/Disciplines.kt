package com.example.trp.data.disciplines

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Disciplines(
    @SerializedName("data") var list: List<DisciplineData>? = null
)
