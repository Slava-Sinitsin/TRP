package com.example.trp.data.mappers.tasks.solution

import com.google.gson.annotations.SerializedName

data class CommentLine(
    @SerializedName("lines") val lines: String? = null,
    @SerializedName("comment") val comment: String? = null,
    @SerializedName("match") val isMatch: Boolean? = null
)