package com.example.trp.data.tasks

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("disciplineId") var disciplineId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null
)