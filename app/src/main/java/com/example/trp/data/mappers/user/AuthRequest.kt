package com.example.trp.data.mappers.user

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("username") val username: String? = null,
    @SerializedName("password") val password: String? = null
)