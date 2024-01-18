package com.example.trp.domain.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trp.data.datamanagers.DisciplinesDataManager
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.data.mappers.disciplines.DisciplineResponse
import com.example.trp.data.mappers.disciplines.Disciplines
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.tasks.Students
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.TaskResponse
import com.example.trp.data.mappers.tasks.Tasks
import com.example.trp.data.mappers.tasks.solution.GetSolutionResponse
import com.example.trp.data.mappers.tasks.solution.PostSolutionResponse
import com.example.trp.data.mappers.tasks.solution.Solution
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsData
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse
import com.example.trp.data.mappers.user.AuthRequest
import com.example.trp.data.mappers.user.JWTDecoder
import com.example.trp.data.mappers.user.User
import com.example.trp.data.network.ApiService
import com.example.trp.data.network.UserAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response

class UserAPIRepositoryImpl(private val userDataManager: UserDataManager) : UserAPI {
    var user by mutableStateOf(User())
    private var userChanged by mutableStateOf(true)

    var disciplines by mutableStateOf(emptyList<DisciplineData>())
    var disciplinesChanged by mutableStateOf(true)

    var tasks by mutableStateOf(emptyList<Task>())
    private var tasksChanged by mutableStateOf(true)

    var task by mutableStateOf(Task())
    private var taskChanged by mutableStateOf(true)

    var taskDisciplineData by mutableStateOf(DisciplineData())

    var taskSolution by mutableStateOf(Solution())

    var teacherAppointments by mutableStateOf(emptyList<TeacherAppointmentsData>())

    var students by mutableStateOf(emptyList<Student>())

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

    override suspend fun getTaskSolution(
        token: String,
        taskId: Int
    ): Response<GetSolutionResponse> {
        return ApiService.userAPI.getTaskSolution("Bearer $token", taskId)
    }

    override suspend fun postTaskSolution(
        token: String,
        taskId: Int,
        code: String
    ): Response<PostSolutionResponse> {
        return ApiService.userAPI.postTaskSolution("Bearer $token", taskId, code)
    }

    override suspend fun teacherAppointments(token: String): Response<TeacherAppointmentsResponse> {
        return ApiService.userAPI.teacherAppointments("Bearer $token")
    }

    override suspend fun getStudents(
        token: String,
        id: Int
    ): Response<Students> {
        return ApiService.userAPI.getStudents("Bearer $token", id)
    }

    suspend fun login(login: String, password: String): User {
        if (userChanged) {
            val response = getUserResponse(
                AuthRequest(
                    login,
                    password
                )
            )
            response.body()?.let {
                userDataManager.updateUser(
                    it.copy(
                        login = login,
                        password = password
                    )
                )
                userChanged = false
            } ?: run {
                response.errorBody()?.let { errorBody ->
                    userDataManager.updateUser(
                        user.copy(message = JSONObject(errorBody.string()).getString("error"))
                    )
                } ?: run {
                    userDataManager.updateUser(user.copy(message = "Bad response"))
                }
            }
        }
        user = userDataManager.getUser()
        return user
    }

    suspend fun getUser(): User {
        user = userDataManager.getUser()
        return user
    }

    suspend fun addUserInformation() {
        val updatedUser = parseToken(user.token.toString())
        userDataManager.updateUser(updatedUser)
        user = userDataManager.getUser()
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
            val response = userDataManager.getUser().token?.let { getDisciplinesResponse(it) }
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
                userDataManager.getUser().token?.let { getTasksResponse(it, disciplineId) }
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
                userDataManager.getUser().token?.let { getTaskDescriptionResponse(it, taskId) }
            taskResponse?.body()?.let {
                task = it.task ?: Task()
                if (task != Task()) {
                    taskDisciplineData = getTaskDisciplineData()
                    taskSolution = getTaskSolution()
                }
            } ?: taskResponse?.errorBody()?.let {
                task = Task()
            }
        }
        return task
    }

    private suspend fun getTaskDisciplineData(): DisciplineData {
        val disciplineDataResponse = userDataManager.getUser().token?.let { token ->
            task.disciplineId?.let { disciplineId -> getDisciplineByID(token, disciplineId) }
        }
        return disciplineDataResponse?.body()?.disciplineData ?: DisciplineData()
    }

    private suspend fun getTaskSolution(): Solution {
        val solutionResponse = userDataManager.getUser().token?.let { token ->
            task.id?.let { taskId -> getTaskSolution(token, taskId) }
        }
        return solutionResponse?.body()?.data ?: Solution()
    }

    suspend fun postTaskSolution(solutionText: String) {
        userDataManager.getUser().token?.let { token ->
            task.id?.let { taskId -> postTaskSolution(token, taskId, solutionText) }
        }
    }

    suspend fun getTeacherAppointments(): List<TeacherAppointmentsData> {
        val teacherAppointmentsResponse = userDataManager.getUser().token?.let { token ->
            teacherAppointments(token)
        }
        return teacherAppointmentsResponse?.body()?.data ?: emptyList()
    }

    suspend fun getStudents(groupId: Int): List<Student> {
        val studentsResponse = userDataManager.getUser().token?.let { token ->
            getStudents(token, groupId)
        }
        return studentsResponse?.body()?.data ?: emptyList()
    }
}