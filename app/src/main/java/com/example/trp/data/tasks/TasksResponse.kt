package com.example.trp.data.tasks

import com.google.gson.annotations.SerializedName

data class TasksResponse(
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("data") val data: Task? = null
)
