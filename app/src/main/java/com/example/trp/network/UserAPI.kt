package com.example.trp.network

import com.example.trp.data.tasks.TasksResponse
import com.example.trp.data.disciplines.Disciplines
import com.example.trp.data.user.AuthRequest
import com.example.trp.data.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {
    @POST("auth/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<User>

    @GET("student/disciplines")
    suspend fun disciplines(@Header("Authorization") token: String): Response<Disciplines>

    @GET("student/disciplines/{disciplineId}")
    suspend fun tasks(
        @Header("Authorization") token: String,
        @Path("disciplineId") id: Int
    ): Response<TasksResponse>
}