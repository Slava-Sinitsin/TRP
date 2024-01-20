package com.example.trp.data.network

import com.example.trp.data.mappers.disciplines.DisciplineResponse
import com.example.trp.data.mappers.tasks.Output
import com.example.trp.data.mappers.tasks.Students
import com.example.trp.data.mappers.tasks.TaskResponse
import com.example.trp.data.mappers.tasks.Tasks
import com.example.trp.data.mappers.tasks.solution.GetSolutionResponse
import com.example.trp.data.mappers.tasks.solution.PostSolutionResponse
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse
import com.example.trp.data.mappers.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {
    @POST("auth/login")
    suspend fun getUserResponse(@Body authRequest: com.example.trp.data.mappers.user.AuthRequest): Response<User>

    @GET("api/v2/disciplines")
    suspend fun getDisciplinesResponse(@Header("Authorization") token: String): Response<com.example.trp.data.mappers.disciplines.Disciplines>

    @GET("/api/v2/disciplines/{id}/tasks")
    suspend fun getTasksResponse(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Tasks>

    @GET("api/v2/tasks/{id}")
    suspend fun getTaskDescriptionResponse(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<TaskResponse>

    @GET("api/v2/disciplines/{id}")
    suspend fun getDisciplineByID(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DisciplineResponse>

    @GET("api/v2/tasks/{id}/solution")
    suspend fun getTaskSolution(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int
    ): Response<GetSolutionResponse>

    @POST("api/v2/tasks/{id}/solution")
    suspend fun postTaskSolution(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int,
        @Body code: String
    ): Response<PostSolutionResponse>

    @GET("api/v2/teacher-appointments/all")
    suspend fun teacherAppointments(
        @Header("Authorization") token: String,
    ): Response<TeacherAppointmentsResponse>

    @GET("api/v2/groups/{id}/students")
    suspend fun getStudents(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Students>

    @POST("api/v2/tasks/{taskId}/solution/execute/student")
    suspend fun runCode(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: Int
    ): Response<Output>
}