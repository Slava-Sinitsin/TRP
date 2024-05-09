package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class CodeThread(
    @SerializedName("messages") val messages: List<CodeThreadMessage>? = null,
    @SerializedName("beginLineNumber") val beginLineNumber: Int? = null,
    @SerializedName("endLineNumber") val endLineNumber: Int? = null
)