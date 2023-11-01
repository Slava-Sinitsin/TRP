package com.example.trp.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trp.data.datamanagers.DisciplinesDataManager
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.data.disciplines.DisciplineData
import com.example.trp.data.disciplines.DisciplineResponse
import com.example.trp.data.disciplines.Disciplines
import com.example.trp.data.network.ApiService
import com.example.trp.data.network.UserAPI
import com.example.trp.data.tasks.Task
import com.example.trp.data.tasks.TaskResponse
import com.example.trp.data.tasks.Tasks
import com.example.trp.data.user.AuthRequest
import com.example.trp.data.user.JWTDecoder
import com.example.trp.data.user.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response

class UserAPIRepositoryImpl : UserAPI {
    var user by mutableStateOf(User())
    private var userChanged by mutableStateOf(true)

    var disciplines by mutableStateOf(emptyList<DisciplineData>())
    var disciplinesChanged by mutableStateOf(true)

    var tasks by mutableStateOf(emptyList<Task>())
    private var tasksChanged by mutableStateOf(true)

    var task by mutableStateOf(Task())
    private var taskChanged by mutableStateOf(true)

    var taskDisciplineData by mutableStateOf(DisciplineData())

    override suspend fun getUserResponse(authRequest: AuthRequest): Response<User> {
        return ApiService.userAPI.getUserResponse(authRequest)
    }

    override suspend fun getDisciplinesResponse(token: String): Response<Disciplines> {
        return ApiService.userAPI.getDisciplinesResponse("Bearer $token")
    }

    override suspend fun getTasksResponse(token: String, id: Int): Response<Tasks> {
        return ApiService.userAPI.getTasksResponse("Bearer $token", id)
    }

    override suspend fun getTaskDescriptionResponse(
        token: String,
        id: Int
    ): Response<TaskResponse> {
        return ApiService.userAPI.getTaskDescriptionResponse("Bearer $token", id)
    }

    override suspend fun getDisciplineByID(token: String, id: Int): Response<DisciplineResponse> {
        return ApiService.userAPI.getDisciplineByID("Bearer $token", id)
    }

    suspend fun login(login: String, password: String): User {
        if (userChanged) {
            val response = getUserResponse(AuthRequest(login, password))
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
        val decodedToken = JWTDecoder().decodeToken(token)
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

    suspend fun getDisciplines(): List<DisciplineData> {
        if (disciplinesChanged) {
            val response = UserDataManager.getUser().token?.let { getDisciplinesResponse(it) }
            response?.body()?.let {
                DisciplinesDataManager.saveDisciplines(it)
                disciplinesChanged = false
            } ?: response?.errorBody()?.let {
                DisciplinesDataManager.saveDisciplines(Disciplines())
            }
        }
        disciplines = DisciplinesDataManager.getDisciplines() ?: emptyList()
        return disciplines
    }

    suspend fun getTasks(disciplineId: Int): List<Task> {
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

    suspend fun getTask(taskId: Int): Task {
        if (taskChanged) {
            val taskResponse =
                UserDataManager.getUser().token?.let { getTaskDescriptionResponse(it, taskId) }
            taskResponse?.body()?.let {
                task = it.task ?: Task()
            } ?: taskResponse?.errorBody()?.let {
                task = Task()
            }
            val disciplineDataResponse = UserDataManager.getUser().token?.let {
                getDisciplineByID(it, task.disciplineId ?: -1)
            }
            disciplineDataResponse?.body()?.let {
                taskDisciplineData = it.disciplineData ?: DisciplineData()
            } ?: taskResponse?.errorBody()?.let {
                taskDisciplineData = DisciplineData()
            }
        }
        return task
    }
}