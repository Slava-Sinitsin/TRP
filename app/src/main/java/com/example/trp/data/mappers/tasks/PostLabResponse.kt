package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class PostLabResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("disciplineId") val disciplineId: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("maxRating") val maxRating: Int? = null
)