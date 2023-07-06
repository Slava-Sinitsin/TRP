package com.example.trp.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("jwt-token") var token: String? = null,
    @SerializedName("message") var message: String? = null
)