package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("studentId") val studentId: Int? = null,
    @SerializedName("grade") val grade: Int? = null,
    @SerializedName("maxRating") val maxRating: Int? = null
)