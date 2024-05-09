package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class PostMultilineNoteBody(
    @SerializedName("note") val note: String? = null,
    @SerializedName("beginLineNumber") val beginLineNumber: Int? = null,
    @SerializedName("endLineNumber") val endLineNumber: Int? = null
)