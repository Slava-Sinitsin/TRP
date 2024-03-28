package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("disciplineId") val disciplineId: Int? = null,
    @SerializedName("studentIds") val studentIds: List<Int>? = null
)