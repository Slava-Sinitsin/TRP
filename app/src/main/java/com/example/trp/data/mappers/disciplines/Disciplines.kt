package com.example.trp.data.mappers.disciplines

import com.google.gson.annotations.SerializedName

data class Disciplines(
    @SerializedName("data") var list: List<DisciplineData>? = null
)