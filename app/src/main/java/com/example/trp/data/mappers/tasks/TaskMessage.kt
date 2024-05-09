package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class TaskMessage(
    @SerializedName("message") val message: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("author") val author: Author? = null
)