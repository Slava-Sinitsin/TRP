package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Students(
    @SerializedName("data") var data: List<com.example.trp.data.mappers.tasks.Student>? = null
)