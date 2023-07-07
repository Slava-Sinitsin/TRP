package com.example.trp.data

import com.google.gson.annotations.SerializedName

data class Hello(
    @SerializedName("message") var message: String? = null
)