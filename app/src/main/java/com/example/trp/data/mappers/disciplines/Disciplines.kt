package com.example.trp.data.mappers.disciplines

import com.google.gson.annotations.SerializedName

data class Disciplines(
    @SerializedName("data") val list: List<DisciplineData>? = null
)