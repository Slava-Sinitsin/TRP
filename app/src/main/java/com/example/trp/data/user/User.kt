package com.example.trp.data.user

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("login") val login: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("data") val token: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("sub") val sub: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("fullName") val fullName: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("iat") val iat: Long? = null,
    @SerializedName("iss") val iss: String? = null,
    @SerializedName("exp") val exp: Long? = null
)
