package com.example.trp.data.user

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("username") var mail: String? = null,
    @SerializedName("password") var password: String? = null
)