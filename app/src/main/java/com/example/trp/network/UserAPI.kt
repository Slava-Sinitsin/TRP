package com.example.trp.network

import com.example.trp.data.disciplines.DisciplineResponse
import com.example.trp.data.disciplines.Disciplines
import com.example.trp.data.tasks.TaskDesc
import com.example.trp.data.tasks.Tasks
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

    @GET("api/v2/disciplines")
    suspend fun getDisciplines(@Header("Authorization") token: String): Response<Disciplines>

    @GET("/api/v2/disciplines/{id}/tasks")
    suspend fun getTasks(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Tasks>

    @GET("api/v2/tasks/{id}")
    suspend fun getTaskDesc(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<TaskDesc>

    @GET("api/v2/disciplines/{id}")
    suspend fun getDisciplineByID(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DisciplineResponse>
}