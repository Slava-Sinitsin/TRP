package com.example.trp.network

import com.example.trp.data.user.AuthRequest
import com.example.trp.data.disciplines.Disciplines
import com.example.trp.data.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserAPI {
    @POST("auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<User>

    @GET("student/disciplines")
    suspend fun disciplines(@Header("Authorization") token: String): Response<Disciplines>
}