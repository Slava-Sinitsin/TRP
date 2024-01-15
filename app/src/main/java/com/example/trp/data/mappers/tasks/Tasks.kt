package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Tasks(
    @SerializedName("data") var data: List<com.example.trp.data.mappers.tasks.Task>? = null
)