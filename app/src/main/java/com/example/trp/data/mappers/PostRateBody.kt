package com.example.trp.data.mappers

import com.google.gson.annotations.SerializedName

data class PostRateBody (
    @SerializedName("ratings") val ratings: List<Rating>? = null
)