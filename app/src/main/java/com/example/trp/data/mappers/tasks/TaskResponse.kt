package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("data") val task: com.example.trp.data.mappers.tasks.Task? = null
)