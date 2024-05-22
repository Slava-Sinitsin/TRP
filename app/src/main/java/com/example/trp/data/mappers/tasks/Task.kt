package com.example.trp.data.mappers.tasks

import com.example.trp.data.mappers.tasks.solution.Argument
import com.example.trp.data.mappers.tasks.solution.Solution
import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("labWorkId") val labWorkId: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("functionName") val functionName: String? = null,
    @SerializedName("returnType") val returnType: String? = null,
    @SerializedName("arguments") val arguments: List<Argument>? = null,
    @SerializedName("testable") val testable: Boolean? = null,
    @SerializedName("inputRegex") val inputRegex: String? = null,
    @SerializedName("outputRegex") val outputRegex: String? = null,
    @SerializedName("labWorkVariantTests") val tests: List<Test>? = null,
    @SerializedName("solution") val solution: Solution? = null,
)