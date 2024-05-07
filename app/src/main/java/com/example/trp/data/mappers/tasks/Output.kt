package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Output(
    @SerializedName("testPassed") var testPassed: Int? = null,
    @SerializedName("totalTests") var totalTests: Int? = null,
    @SerializedName("errorStatus") var errorStatus: Int? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null,
    @SerializedName("failedTestIds") var failedTestIds: List<Int>? = null,
    @SerializedName("testsInfo") var testsInfo: List<TestsInfo>? = null
)