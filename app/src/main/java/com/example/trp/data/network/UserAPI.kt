package com.example.trp.data.network

import com.example.trp.data.mappers.PostStudentAppointmentsBody
import com.example.trp.data.mappers.PostStudentAppointmentsResponse
import com.example.trp.data.mappers.StudentAppointmentsResponse
import com.example.trp.data.mappers.disciplines.DisciplineResponse
import com.example.trp.data.mappers.disciplines.Disciplines
import com.example.trp.data.mappers.disciplines.PostNewDisciplineBody
import com.example.trp.data.mappers.disciplines.PostNewDisciplineResponse
import com.example.trp.data.mappers.tasks.Output
import com.example.trp.data.mappers.tasks.Students
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.TaskResponse
import com.example.trp.data.mappers.tasks.Tasks
import com.example.trp.data.mappers.tasks.solution.SolutionResponse
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse
import com.example.trp.data.mappers.user.AuthRequest
import com.example.trp.data.mappers.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserAPI {
    @POST("auth/login")
    suspend fun getUserResponse(@Body authRequest: AuthRequest): Response<User>

    @GET("api/v2/disciplines")
    suspend fun getDisciplinesResponse(@Header("Authorization") token: String): Response<Disciplines>

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
    ): Response<SolutionResponse>

    @POST("api/v2/tasks/{id}/solution")
    suspend fun postTaskSolution(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int,
        @Body code: String
    ): Response<SolutionResponse>

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

    @PUT("api/v2/tasks/{id}")
    suspend fun putTask(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int,
        @Body task: Task
    ): Response<Task>

    @DELETE("api/v2/tasks/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int,
    ): Response<Task>

    @POST("api/v2/tasks")
    suspend fun postTask(
        @Header("Authorization") token: String,
        @Body task: Task
    ): Response<Task>

    @GET("api/v2/student-appointments")
    suspend fun getStudentAppointments(
        @Header("Authorization") token: String
    ): Response<StudentAppointmentsResponse>

    @POST("api/v2/student-appointments")
    suspend fun postStudentAppointments(
        @Header("Authorization") token: String,
        @Body postStudentAppointmentsBody: PostStudentAppointmentsBody
    ): Response<PostStudentAppointmentsResponse>

    @POST("/api/v2/disciplines")
    suspend fun postNewDiscipline(
        @Header("Authorization") token: String,
        @Body postNewDisciplineBody: PostNewDisciplineBody
    ): Response<PostNewDisciplineResponse>
}