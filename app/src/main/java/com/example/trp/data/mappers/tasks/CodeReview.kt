package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class CodeReview(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("mergeRequestId") var mergeRequestId: Int? = null,
    @SerializedName("projectId") var projectId: Int? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("notesList") var notes: List<Note>? = null
)