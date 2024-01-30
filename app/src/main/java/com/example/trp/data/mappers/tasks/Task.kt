package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("disciplineId") val disciplineId: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("functionName") val functionName: String? = null,
    @SerializedName("language") val language: String? = null
)