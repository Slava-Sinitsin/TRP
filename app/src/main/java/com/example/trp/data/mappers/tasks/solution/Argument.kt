package com.example.trp.data.mappers.tasks.solution

import com.google.gson.annotations.SerializedName

data class Argument(
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: String? = null,
)