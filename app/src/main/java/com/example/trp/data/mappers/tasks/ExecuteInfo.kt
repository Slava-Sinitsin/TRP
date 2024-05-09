package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class ExecuteInfo(
    @SerializedName("stdout") val stdout: String? = null,
    @SerializedName("stderr") val stderr: String? = null
)