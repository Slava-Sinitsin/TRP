package com.example.trp.data.tasks.solution

import com.google.gson.annotations.SerializedName

data class PostSolutionResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: String? = null
)
