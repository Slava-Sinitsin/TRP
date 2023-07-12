package com.example.trp.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var login: String? = null,
    var password: String? = null,
    @SerializedName("data") var token: String? = null,
    @SerializedName("message") var message: String? = null
)