package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("authorUsername") var authorUsername: String? = null,
    @SerializedName("author") var author: Author? = null,
    @SerializedName("message") var message: String? = null
)