package com.example.trp.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("jwtToken") var token: String? = null,
)