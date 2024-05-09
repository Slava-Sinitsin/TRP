package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class CodeReview(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("code") val code: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("taskMessages") val taskMessages: List<TaskMessage>? = null,
    @SerializedName("codeThreads") val codeThreads: List<CodeThread>? = null
)