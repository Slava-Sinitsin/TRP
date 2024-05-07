package com.example.trp.data.mappers.tasks

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("fullName") var fullName: String? = null,
    @SerializedName("role") var role: String? = null
)