package com.example.trp.data.tasks

import com.google.gson.annotations.SerializedName

data class TaskDesc(
    @SerializedName("data") val taskDesc: Task? = null,
)