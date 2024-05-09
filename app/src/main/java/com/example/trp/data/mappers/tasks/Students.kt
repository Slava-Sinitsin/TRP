package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Students(
    @SerializedName("data") val data: List<Student>? = null
)