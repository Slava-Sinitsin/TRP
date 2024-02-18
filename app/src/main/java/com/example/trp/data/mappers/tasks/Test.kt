package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Test(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("title") val title: String? = null
)
