package com.example.trp.network

import com.example.trp.data.AuthRequest
import com.example.trp.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {
    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>
}