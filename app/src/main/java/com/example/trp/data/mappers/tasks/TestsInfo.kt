package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class TestsInfo(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("stdout") val stdout: String? = null,
    @SerializedName("stderr") val stderr: String? = null,
    @SerializedName("open") val open: Boolean? = null,
    @SerializedName("input") val input: String? = null,
    @SerializedName("output") val output: String? = null,
    @SerializedName("expected") val expected: String? = null
)