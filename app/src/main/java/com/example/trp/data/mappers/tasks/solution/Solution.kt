package com.example.trp.data.mappers.tasks.solution

import com.google.gson.annotations.SerializedName

data class Solution(
    @SerializedName("arguments") val arguments: List<Argument>? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("functionName") val functionName: String? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("returnType") val returnType: String? = null
)