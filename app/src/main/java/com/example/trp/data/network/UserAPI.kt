package com.example.trp.data.network

import com.example.trp.data.mappers.PostStudentAppointmentsResponse
import com.example.trp.data.mappers.PostTeamAppointmentsBody
import com.example.trp.data.mappers.TeamAppointmentsResponse
import com.example.trp.data.mappers.disciplines.DisciplineResponse
import com.example.trp.data.mappers.disciplines.Disciplines
import com.example.trp.data.mappers.disciplines.PostNewDisciplineBody
import com.example.trp.data.mappers.disciplines.PostNewDisciplineResponse
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.mappers.tasks.LabsResponse
import com.example.trp.data.mappers.tasks.Output
import com.example.trp.data.mappers.tasks.PostLabResponse
import com.example.trp.data.mappers.tasks.PostNewStudentBody
import com.example.trp.data.mappers.tasks.PostTeamBody
import com.example.trp.data.mappers.tasks.PostTeamResponse
import com.example.trp.data.mappers.tasks.PostTestResponse
import com.example.trp.data.mappers.tasks.StudentResponse
import com.example.trp.data.mappers.tasks.Students
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.TaskResponse
import com.example.trp.data.mappers.tasks.Tasks
import com.example.trp.data.mappers.tasks.TeamResponse
import com.example.trp.data.mappers.tasks.Test
import com.example.trp.data.mappers.tasks.TestsResponse
import com.example.trp.data.mappers.tasks.solution.SolutionResponse
import com.example.trp.data.mappers.teacherappointments.DeleteGroupResponse
import com.example.trp.data.mappers.teacherappointments.GroupsResponse
import com.example.trp.data.mappers.teacherappointments.PostGroupResponse
import com.example.trp.data.mappers.teacherappointments.PostNewGroupBody
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse
import com.example.trp.data.mappers.teacherappointments.TeacherResponse
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

    @GET("api/v2/lab-works/{id}/lab-work-variants")
    suspend fun getTasks(
        @Header("Authorization") token: String,
        @Path("id") labId: Int
    ): Response<Tasks>

    @GET("api/v2/lab-work-variants/{id}")
    suspend fun getTaskDescriptionResponse(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<TaskResponse>

    @GET("api/v2/disciplines/{id}")
    suspend fun getDisciplineByID(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DisciplineResponse>

    @GET("api/v2/lab-work-variants/{id}/solution")
    suspend fun getTaskSolution(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int
    ): Response<SolutionResponse>

    @POST("api/v2/lab-work-variants/{id}/solution")
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

    @POST("api/v2/lab-work-variants/{id}/solution/execute")
    suspend fun runCode(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int
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

    @POST("api/v2/lab-work-variants")
    suspend fun postTask(
        @Header("Authorization") token: String,
        @Body task: Task
    ): Response<Task>

    @GET("api/v2/disciplines/{id}/team-appointments")
    suspend fun getAllTeamAppointments(
        @Header("Authorization") token: String,
        @Path("id") disciplineId: Int
    ): Response<TeamAppointmentsResponse>

    @POST("api/v2/team-appointments")
    suspend fun postTeamAppointments(
        @Header("Authorization") token: String,
        @Body postTeamAppointmentsBody: PostTeamAppointmentsBody
    ): Response<PostStudentAppointmentsResponse>

    @POST("/api/v2/disciplines")
    suspend fun postNewDiscipline(
        @Header("Authorization") token: String,
        @Body postNewDisciplineBody: PostNewDisciplineBody
    ): Response<PostNewDisciplineResponse>

    @GET("api/v2/teachers")
    suspend fun getTeachers(
        @Header("Authorization") token: String
    ): Response<TeacherResponse>

    @GET("api/v2/groups")
    suspend fun getGroups(
        @Header("Authorization") token: String
    ): Response<GroupsResponse>

    @GET("api/v2/lab-work-variants/{id}/tests")
    suspend fun getTests(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int
    ): Response<TestsResponse>

    @POST("api/v2/lab-work-variant-tests")
    suspend fun postNewTest(
        @Header("Authorization") token: String,
        @Body test: Test
    ): Response<PostTestResponse>

    @GET("api/v2/teams/{id}/team-appointments")
    suspend fun getTeamAppointments(
        @Header("Authorization") token: String,
        @Path("id") teamId: Int
    ): Response<TeamAppointmentsResponse>

    @GET("api/v2/disciplines/{id}/teams")
    suspend fun getTeams(
        @Header("Authorization") token: String,
        @Path("id") disciplineId: Int
    ): Response<TeamResponse>

    @POST("api/v2/teams")
    suspend fun postNewTeam(
        @Header("Authorization") token: String,
        @Body postTeamBody: PostTeamBody
    ): Response<PostTeamResponse>

    @GET("api/v2/disciplines/{id}/lab-works")
    suspend fun getLabs(
        @Header("Authorization") token: String,
        @Path("id") disciplineId: Int
    ): Response<LabsResponse>

    @POST("api/v2/lab-works")
    suspend fun postNewLab(
        @Header("Authorization") token: String,
        @Body postLabBody: Lab
    ): Response<PostLabResponse>

    @GET("api/v2/teams/{id}/lab-work-variants")
    suspend fun getTeamTasks(
        @Header("Authorization") token: String,
        @Path("id") teamId: Int
    ): Response<Tasks>

    @POST("api/v2/groups/create-with-students")
    suspend fun postNewGroup(
        @Header("Authorization") token: String,
        @Body group: PostNewGroupBody
    ): Response<PostGroupResponse>

    @POST("admin/registration/student")
    suspend fun postNewStudent(
        @Header("Authorization") token: String,
        @Body student: PostNewStudentBody
    ): Response<StudentResponse>

    @DELETE("api/v2/groups/{id}")
    suspend fun deleteGroup(
        @Header("Authorization") token: String,
        @Body id: Int
    ): Response<DeleteGroupResponse>
}