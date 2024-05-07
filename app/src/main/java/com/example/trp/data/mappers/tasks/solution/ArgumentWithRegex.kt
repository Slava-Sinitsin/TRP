package com.example.trp.data.mappers.tasks.solution

import com.google.gson.annotations.SerializedName
import kotlin.String

data class ArgumentWithRegex(
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("value") val value: String? = null,
    @SerializedName("regex") val regex: String? = null,
    @SerializedName("match") val isMatch: Boolean? = null
)