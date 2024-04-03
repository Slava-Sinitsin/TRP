package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Test(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("labWorkVariantId") val taskId: Int? = null,
    @SerializedName("input") val input: String? = null,
    @SerializedName("output") val output: String? = null
)
