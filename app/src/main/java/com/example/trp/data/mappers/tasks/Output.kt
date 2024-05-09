package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Output(
    @SerializedName("testPassed") val testPassed: Int? = null,
    @SerializedName("totalTests") val totalTests: Int? = null,
    @SerializedName("errorStatus") val errorStatus: Int? = null,
    @SerializedName("errorMessage") val errorMessage: String? = null,
    @SerializedName("failedTestIds") val failedTestIds: List<Int>? = null,
    @SerializedName("testsInfo") val testsInfo: List<TestsInfo>? = null
)