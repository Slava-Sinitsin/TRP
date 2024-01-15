package com.example.trp.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trp.data.datamanagers.DisciplinesDataManager
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.data.mappers.user.User
import com.example.trp.data.network.ApiService
import com.example.trp.data.network.UserAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response

class UserAPIRepositoryImpl : UserAPI {
    var user by mutableStateOf(User())
    private var userChanged by mutableStateOf(true)

    var disciplines by mutableStateOf(emptyList<com.example.trp.data.mappers.disciplines.DisciplineData>())
    var disciplinesChanged by mutableStateOf(true)

    var tasks by mutableStateOf(emptyList<com.example.trp.data.mappers.tasks.Task>())
    private var tasksChanged by mutableStateOf(true)

    var task by mutableStateOf(com.example.trp.data.mappers.tasks.Task())
    private var taskChanged by mutableStateOf(true)

    var taskDisciplineData by mutableStateOf(com.example.trp.data.mappers.disciplines.DisciplineData())

    var taskSolution by mutableStateOf(com.example.trp.data.mappers.tasks.solution.Solution())

    var teacherAppointments by mutableStateOf(emptyList<com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsData>())

    var students by mutableStateOf(emptyList<com.example.trp.data.mappers.tasks.Student>())

    override suspend fun getUserResponse(authRequest: com.example.trp.data.mappers.user.AuthRequest): Response<User> {
        return ApiService.userAPI.getUserResponse(authRequest)
    }

    override suspend fun getDisciplinesResponse(token: String): Response<com.example.trp.data.mappers.disciplines.Disciplines> {
        return ApiService.userAPI.getDisciplinesResponse("Bearer $token")
    }

    override suspend fun getTasksResponse(token: String, id: Int): Response<com.example.trp.data.mappers.tasks.Tasks> {
        return ApiService.userAPI.getTasksResponse("Bearer $token", id)
    }

    override suspend fun getTaskDescriptionResponse(
        token: String,
        id: Int
    ): Response<com.example.trp.data.mappers.tasks.TaskResponse> {
        return ApiService.userAPI.getTaskDescriptionResponse("Bearer $token", id)
    }

    override suspend fun getDisciplineByID(token: String, id: Int): Response<com.example.trp.data.mappers.disciplines.DisciplineResponse> {
        return ApiService.userAPI.getDisciplineByID("Bearer $token", id)
    }

    override suspend fun getTaskSolution(
        token: String,
        taskId: Int
    ): Response<com.example.trp.data.mappers.tasks.solution.GetSolutionResponse> {
        return ApiService.userAPI.getTaskSolution("Bearer $token", taskId)
    }

    override suspend fun postTaskSolution(
        token: String,
        taskId: Int,
        code: String
    ): Response<com.example.trp.data.mappers.tasks.solution.PostSolutionResponse> {
        return ApiService.userAPI.postTaskSolution("Bearer $token", taskId, code)
    }

    override suspend fun teacherAppointments(token: String): Response<com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse> {
        return ApiService.userAPI.teacherAppointments("Bearer $token")
    }

    override suspend fun getStudents(
        token: String,
        id: Int
    ): Response<com.example.trp.data.mappers.tasks.Students> {
        return ApiService.userAPI.getStudents("Bearer $token", id)
    }

    suspend fun login(login: String, password: String): User {
        if (userChanged) {
            val response = getUserResponse(
                com.example.trp.data.mappers.user.AuthRequest(
                    login,
                    password
                )
            )
            response.body()?.let {
                UserDataManager.updateUser(
                    it.copy(
                        login = login,
                        password = password
                    )
                )
                userChanged = false
            } ?: run {
                response.errorBody()?.let { errorBody ->
                    UserDataManager.updateUser(
                        user.copy(message = JSONObject(errorBody.string()).getString("error"))
                    )
                } ?: run {
                    UserDataManager.updateUser(user.copy(message = "Bad response"))
                }
            }
        }
        user = UserDataManager.getUser()
        return user
    }

    suspend fun getUser(): User {
        user = UserDataManager.getUser()
        return user
    }

    suspend fun addUserInformation() {
        val updatedUser = parseToken(user.token.toString())
        UserDataManager.updateUser(updatedUser)
        user = UserDataManager.getUser()
    }

    private fun parseToken(token: String): User {
        val decodedToken = com.example.trp.data.mappers.user.JWTDecoder().decodeToken(token)
        val tempUser = Json.decodeFromString<User>(decodedToken.toString())
        return user.copy(
            sub = tempUser.sub,
            id = tempUser.id,
            username = tempUser.username,
            fullName = tempUser.fullName,
            role = tempUser.role,
            iat = tempUser.iat,
            iss = tempUser.iss,
            exp = tempUser.exp
        )
    }

    suspend fun getDisciplines(): List<com.example.trp.data.mappers.disciplines.DisciplineData> {
        if (disciplinesChanged) {
            val response = UserDataManager.getUser().token?.let { getDisciplinesResponse(it) }
            response?.body()?.let {
                DisciplinesDataManager.saveDisciplines(it)
                disciplinesChanged = false
            } ?: response?.errorBody()?.let {
                DisciplinesDataManager.saveDisciplines(com.example.trp.data.mappers.disciplines.Disciplines())
            }
        }
        disciplines = DisciplinesDataManager.getDisciplines() ?: emptyList()
        return disciplines
    }

    suspend fun getTasks(disciplineId: Int): List<com.example.trp.data.mappers.tasks.Task> {
        if (tasksChanged) {
            val response =
                UserDataManager.getUser().token?.let { getTasksResponse(it, disciplineId) }
            response?.body()?.let {
                tasks = it.data ?: emptyList()

            } ?: response?.errorBody()?.let {
                tasks = emptyList()
            }
        }
        return tasks
    }

    suspend fun getTask(taskId: Int): com.example.trp.data.mappers.tasks.Task {
        if (taskChanged) {
            val taskResponse =
                UserDataManager.getUser().token?.let { getTaskDescriptionResponse(it, taskId) }
            taskResponse?.body()?.let {
                task = it.task ?: com.example.trp.data.mappers.tasks.Task()
                if (task != com.example.trp.data.mappers.tasks.Task()) {
                    taskDisciplineData = getTaskDisciplineData()
                    taskSolution = getTaskSolution()
                }
            } ?: taskResponse?.errorBody()?.let {
                task = com.example.trp.data.mappers.tasks.Task()
            }
        }
        return task
    }

    private suspend fun getTaskDisciplineData(): com.example.trp.data.mappers.disciplines.DisciplineData {
        val disciplineDataResponse = UserDataManager.getUser().token?.let { token ->
            task.disciplineId?.let { disciplineId -> getDisciplineByID(token, disciplineId) }
        }
        return disciplineDataResponse?.body()?.disciplineData ?: com.example.trp.data.mappers.disciplines.DisciplineData()
    }

    private suspend fun getTaskSolution(): com.example.trp.data.mappers.tasks.solution.Solution {
        val solutionResponse = UserDataManager.getUser().token?.let { token ->
            task.id?.let { taskId -> getTaskSolution(token, taskId) }
        }
        return solutionResponse?.body()?.data ?: com.example.trp.data.mappers.tasks.solution.Solution()
    }

    suspend fun postTaskSolution(solutionText: String) {
        UserDataManager.getUser().token?.let { token ->
            task.id?.let { taskId -> postTaskSolution(token, taskId, solutionText) }
        }
    }

    suspend fun getTeacherAppointments(): List<com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsData> {
        val teacherAppointmentsResponse = UserDataManager.getUser().token?.let { token ->
            teacherAppointments(token)
        }
        return teacherAppointmentsResponse?.body()?.data ?: emptyList()
    }

    suspend fun getStudents(groupId: Int): List<com.example.trp.data.mappers.tasks.Student> {
        val studentsResponse = UserDataManager.getUser().token?.let { token ->
            getStudents(token, groupId)
        }
        return studentsResponse?.body()?.data ?: emptyList()
    }
}