package com.example.trp.data.disciplines

import com.google.gson.annotations.SerializedName

data class DisciplineResponse(
    @SerializedName("data") val disciplineData: DisciplineData? = null
)
