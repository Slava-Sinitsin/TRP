package com.example.trp.data.tasks

import com.google.gson.annotations.SerializedName

data class Tasks(
    @SerializedName("data") val data: List<Task?>? = null
)