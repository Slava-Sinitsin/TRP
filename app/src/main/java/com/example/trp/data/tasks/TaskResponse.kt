package com.example.trp.data.tasks

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("data") val task: Task? = null
)