package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("studentId") val studentId: Int? = null,
    @SerializedName("grade") val grade: Int? = null
)