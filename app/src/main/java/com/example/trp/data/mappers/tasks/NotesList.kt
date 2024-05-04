package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class NotesList(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("authorUsername") var authorUsername: String? = null,
    @SerializedName("body") var body: String? = null
)