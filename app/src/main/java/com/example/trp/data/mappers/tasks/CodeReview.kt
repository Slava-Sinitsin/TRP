package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class CodeReview(
    @SerializedName("mergeRequestId") var mergeRequestId: Int? = null,
    @SerializedName("projectId") var projectId: Int? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("notesList") var notesList: List<NotesList>? = null
)