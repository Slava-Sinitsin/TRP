package com.example.trp.network

import com.example.trp.data.AuthRequest
import com.example.trp.data.Hello
import com.example.trp.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserAPI {
    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>

    @GET("hello")
    suspend fun hello(@Header("Authorization") token: String): Response<Hello>
}