package com.example.trp.data.network

import com.example.trp.data.mappers.PostRateBody
import com.example.trp.data.mappers.PostStudentAppointmentsResponse
import com.example.trp.data.mappers.PostTeamAppointmentsBody
import com.example.trp.data.mappers.TeamAppointmentResponse
import com.example.trp.data.mappers.TeamAppointmentsResponse
import com.example.trp.data.mappers.disciplines.DisciplineResponse
import com.example.trp.data.mappers.disciplines.Disciplines
import com.example.trp.data.mappers.disciplines.PostNewDisciplineBody
import com.example.trp.data.mappers.disciplines.PostNewDisciplineResponse
import com.example.trp.data.mappers.tasks.CloseCodeReviewResponse
import com.example.trp.data.mappers.tasks.CodeReviewResponse
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.mappers.tasks.LabsResponse
import com.example.trp.data.mappers.tasks.OutputResponse
import com.example.trp.data.mappers.tasks.PostCodeReviewResponse
import com.example.trp.data.mappers.tasks.PostLabResponse
import com.example.trp.data.mappers.tasks.PostNewStudentBody
import com.example.trp.data.mappers.tasks.PostNewTeacherBody
import com.example.trp.data.mappers.tasks.PostRateResponse
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
import com.example.trp.data.mappers.tasks.solution.PostSolutionResponse
import com.example.trp.data.mappers.tasks.solution.SolutionResponse
import com.example.trp.data.mappers.teacherappointments.DeleteGroupResponse
import com.example.trp.data.mappers.teacherappointments.GroupsResponse
import com.example.trp.data.mappers.teacherappointments.PostGroupResponse
import com.example.trp.data.mappers.teacherappointments.PostNewGroupBody
import com.example.trp.data.mappers.teacherappointments.PostTeacherResponse
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse
import com.example.trp.data.mappers.teacherappointments.TeachersResponse
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
import retrofit2.http.Query

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

    @GET("api/v2/lab-work-variants/{id}/openTests")
    suspend fun getTaskDescriptionResponse(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<TaskResponse>

    @GET("api/v2/disciplines/{id}")
    suspend fun getDisciplineByID(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<DisciplineResponse>

    @GET("/api/v2/lab-work-variants/{id}/new-solution")
    suspend fun getTaskSolution(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int
    ): Response<SolutionResponse>

    @POST("api/v2/lab-work-variants/{id}/solution")
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

    @POST("api/v2/lab-work-variants/{id}/solution/new-execute")
    suspend fun runCode(
        @Header("Authorization") token: String,
        @Path("id") taskId: Int
    ): Response<OutputResponse>

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

    @POST("api/v2/lab-work-variants/create-testable")
    suspend fun postTestableTask(
        @Header("Authorization") token: String,
        @Body task: Task
    ): Response<Task>

    @POST("api/v2/lab-work-variants/create-non-testable")
    suspend fun postNonTestableTask(
        @Header("Authorization") token: String,
        @Body task: Task
    ): Response<Task>

    @GET("/api/v2/team-appointments")
    suspend fun getAllTeamAppointments(
        @Header("Authorization") token: String,
        @Query("disciplineId") disciplineId: Int,
        @Query("groupId") groupId: Int
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
    ): Response<TeachersResponse>

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

    @GET("api/v2/disciplines/{id}/team-appointments")
    suspend fun getTeamAppointments(
        @Header("Authorization") token: String,
        @Path("id") disciplineId: Int
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

    @POST("admin/registration/lecture-teacher")
    suspend fun postNewLectureTeacher(
        @Header("Authorization") token: String,
        @Body teacher: PostNewTeacherBody
    ): Response<PostTeacherResponse>

    @POST("admin/registration/lab-work-teacher")
    suspend fun postNewLabWorkTeacher(
        @Header("Authorization") token: String,
        @Body teacher: PostNewTeacherBody
    ): Response<PostTeacherResponse>

    @POST("api/v2/team-appointments/{id}/code-review")
    suspend fun postCodeReview(
        @Header("Authorization") token: String,
        @Path("id") teamAppointmentId: Int
    ): Response<PostCodeReviewResponse>

    @GET("api/v2/code-reviews/{codeReviewId}")
    suspend fun getCodeReview(
        @Header("Authorization") token: String,
        @Path("codeReviewId") codeReviewId: Int
    ): Response<CodeReviewResponse>

    @PUT("api/v2/code-reviews/{codeReviewId}/close")
    suspend fun closeCodeReview(
        @Header("Authorization") token: String,
        @Path("codeReviewId") codeReviewId: Int
    ): Response<CloseCodeReviewResponse>

    @PUT("/api/v2/code-reviews/{codeReviewId}/approve")
    suspend fun approveCodeReview(
        @Header("Authorization") token: String,
        @Path("codeReviewId") codeReviewId: Int
    ): Response<CloseCodeReviewResponse>

    @POST("api/v2/code-reviews/{codeReviewId}/note")
    suspend fun addNoteToCodeReview(
        @Header("Authorization") token: String,
        @Path("codeReviewId") codeReviewId: Int,
        @Query("note") note: String
    ): Response<CodeReviewResponse>

    @POST("api/v2/team-appointments/{teamAppointmentId}/rate")
    suspend fun postRate(
        @Header("Authorization") token: String,
        @Path("teamAppointmentId") teamAppointmentId: Int,
        @Body postRateBody: PostRateBody
    ): Response<PostRateResponse>

    @GET("api/v2/team-appointments/{id}")
    suspend fun getTeamAppointment(
        @Header("Authorization") token: String,
        @Path("id") teamAppointmentId: Int,
    ): Response<TeamAppointmentResponse>
}