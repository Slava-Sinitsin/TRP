package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class OutputData(
    @SerializedName("testPassed") val testPassed: Int? = null,
    @SerializedName("totalTests") val totalTests: Int? = null,
    @SerializedName("passed") val passed: Boolean? = null
)