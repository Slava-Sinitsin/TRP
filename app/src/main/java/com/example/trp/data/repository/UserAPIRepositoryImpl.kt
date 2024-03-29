package com.example.trp.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trp.data.maindb.MainDB
import com.example.trp.data.mappers.PostStudentAppointmentsBody
import com.example.trp.data.mappers.PostStudentAppointmentsResponse
import com.example.trp.data.mappers.StudentAppointments
import com.example.trp.data.mappers.StudentAppointmentsResponse
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.data.mappers.disciplines.DisciplineResponse
import com.example.trp.data.mappers.disciplines.Disciplines
import com.example.trp.data.mappers.disciplines.PostNewDisciplineBody
import com.example.trp.data.mappers.disciplines.PostNewDisciplineResponse
import com.example.trp.data.mappers.tasks.Output
import com.example.trp.data.mappers.tasks.PostTestResponse
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.tasks.Students
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.TaskResponse
import com.example.trp.data.mappers.tasks.Tasks
import com.example.trp.data.mappers.tasks.Test
import com.example.trp.data.mappers.tasks.TestsResponse
import com.example.trp.data.mappers.tasks.solution.Solution
import com.example.trp.data.mappers.tasks.solution.SolutionResponse
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.mappers.teacherappointments.GroupsResponse
import com.example.trp.data.mappers.teacherappointments.Teacher
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsData
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse
import com.example.trp.data.mappers.teacherappointments.TeacherResponse
import com.example.trp.data.mappers.user.AuthRequest
import com.example.trp.data.mappers.user.JWTDecoder
import com.example.trp.data.mappers.user.User
import com.example.trp.data.network.ApiService
import com.example.trp.data.network.UserAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response

class UserAPIRepositoryImpl(
    val mainDB: MainDB
) : UserAPI {
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

    var studentAppointments by mutableStateOf(emptyList<StudentAppointments>())

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
    ): Response<SolutionResponse> {
        return ApiService.userAPI.getTaskSolution("Bearer $token", taskId)
    }

    override suspend fun postTaskSolution(
        token: String,
        taskId: Int,
        code: String
    ): Response<SolutionResponse> {
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

    override suspend fun runCode(
        token: String,
        taskId: Int,
    ): Response<Output> {
        return ApiService.userAPI.runCode("Bearer $token", taskId)
    }

    override suspend fun putTask(token: String, taskId: Int, task: Task): Response<Task> {
        return ApiService.userAPI.putTask("Bearer $token", taskId, task)
    }

    override suspend fun deleteTask(token: String, taskId: Int): Response<Task> {
        return ApiService.userAPI.deleteTask("Bearer $token", taskId)
    }

    override suspend fun postTask(token: String, task: Task): Response<Task> {
        return ApiService.userAPI.postTask("Bearer $token", task)
    }

    override suspend fun getStudentAppointments(token: String): Response<StudentAppointmentsResponse> {
        return ApiService.userAPI.getStudentAppointments("Bearer $token")
    }

    override suspend fun postStudentAppointments(
        token: String,
        postStudentAppointmentsBody: PostStudentAppointmentsBody
    ): Response<PostStudentAppointmentsResponse> {
        return ApiService.userAPI.postStudentAppointments(
            "Bearer $token",
            postStudentAppointmentsBody
        )
    }

    override suspend fun postNewDiscipline(
        token: String,
        postNewDisciplineBody: PostNewDisciplineBody
    ): Response<PostNewDisciplineResponse> {
        return ApiService.userAPI.postNewDiscipline(
            "Bearer $token",
            postNewDisciplineBody
        )
    }

    override suspend fun getTeachers(token: String): Response<TeacherResponse> {
        return ApiService.userAPI.getTeachers("Bearer $token")
    }

    override suspend fun getGroups(token: String): Response<GroupsResponse> {
        return ApiService.userAPI.getGroups("Bearer $token")
    }

    override suspend fun getTests(token: String, taskId: Int): Response<TestsResponse> {
        return ApiService.userAPI.getTests("Bearer $token", taskId)
    }

    override suspend fun postNewTest(
        token: String,
        test: Test
    ): Response<PostTestResponse> {
        return ApiService.userAPI.postNewTest("Bearer $token", test)
    }

    override suspend fun getStudentTasks(
        token: String,
        studentId: Int,
        disciplineId: Int
    ): Response<Tasks> {
        return ApiService.userAPI.getStudentTasks("Bearer $token", studentId, disciplineId)
    }

    suspend fun getActiveUser(): User {
        user = mainDB.userDAO.getActiveUser() ?: User()
        return user
    }

    suspend fun login(login: String, password: String): User {
        if (userChanged) {
            val response = getUserResponse(
                AuthRequest(login, password)
            )
            response.body()?.let {
                it.token?.let { token ->
                    it.message?.let { message ->
                        return addUserInformation(login, password, token, message)
                    }
                }
                userChanged = false
            } ?: run {
                response.errorBody()?.let { errorBody ->
                    return User().copy(message = JSONObject(errorBody.string()).getString("error"))
                } ?: run {
                    return User().copy(message = "Bad response")
                }
            }
        }
        return User()
    }

    private suspend fun addUserInformation(
        login: String,
        password: String,
        token: String,
        message: String
    ): User {
        val updatedUser = parseToken(token).copy(
            login = login,
            password = password,
            token = token,
            message = message,
            isActive = true
        )
        mainDB.userDAO.setAllIsActiveFalse()
        mainDB.userDAO.insertActiveUser(updatedUser)
        user = mainDB.userDAO.getActiveUser() ?: User()
        return user
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
            mainDB.disciplinesDAO.deleteAllDisciplines()
            val response = user.token?.let { getDisciplinesResponse(it) }
            response?.body()?.let {
                it.list?.forEach { discipline ->
                    mainDB.disciplinesDAO.insertDiscipline(discipline)
                }
                disciplinesChanged = false
            } ?: response?.errorBody()?.let {
                return emptyList()
            }
        }
        disciplines = mainDB.disciplinesDAO.getDisciplines() ?: emptyList()
        return disciplines
    }

    suspend fun getTasks(disciplineId: Int): List<Task> {
        if (tasksChanged) {
            val response =
                user.token?.let { getTasksResponse(it, disciplineId) }
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
                user.token?.let { getTaskDescriptionResponse(it, taskId) }
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

    suspend fun getTaskProperties(taskId: Int): Task {
        if (taskChanged) {
            val taskResponse =
                user.token?.let { getTaskDescriptionResponse(it, taskId) }
            taskResponse?.body()?.let {
                task = it.task ?: Task()
                if (task != Task()) {
                    taskDisciplineData = getTaskDisciplineData()
                }
            } ?: taskResponse?.errorBody()?.let {
                task = Task()
            }
        }
        return task
    }

    private suspend fun getTaskDisciplineData(): DisciplineData {
        val disciplineDataResponse = user.token?.let { token ->
            task.disciplineId?.let { disciplineId -> getDisciplineByID(token, disciplineId) }
        }
        return disciplineDataResponse?.body()?.disciplineData ?: DisciplineData()
    }

    private suspend fun getTaskSolution(): Solution {
        return user.token?.let { token ->
            task.id?.let { taskId -> getTaskSolution(token, taskId) }
        }?.body()?.data ?: Solution()
    }

    suspend fun postTaskSolution(solutionText: String) {
        user.token?.let { token ->
            task.id?.let { taskId -> postTaskSolution(token, taskId, solutionText) }
        }
    }

    suspend fun runCode(): Output {
        val response =
            user.token?.let { token -> task.id?.let { taskId -> runCode(token, taskId) } }
        return response?.body() ?: response?.errorBody()?.let {
            val errorBody = it.string()
            Output().copy(
                status = JSONObject(errorBody).getInt("status"),
                message = JSONObject(errorBody).getString("message"),
                error = JSONObject(errorBody).getString("error")
            )
        } ?: Output()
    }

    suspend fun getTeacherAppointments(): List<TeacherAppointmentsData> {
        teacherAppointments = user.token?.let { token ->
            teacherAppointments(token)
        }?.body()?.data ?: emptyList()
        return teacherAppointments
    }

    suspend fun getStudents(groupId: Int): List<Student> {
        val studentsResponse = user.token?.let { token ->
            getStudents(token, groupId)
        }
        students = studentsResponse?.body()?.data ?: emptyList()
        return students
    }

    suspend fun putTask(task: Task) {
        user.token?.let { token ->
            task.id?.let { taskId -> putTask(token, taskId, task) }
        }
    }

    suspend fun postTask(task: Task) {
        user.token?.let { token ->
            postTask(token, task)
        }
    }

    suspend fun deleteTask(taskId: Int) {
        user.token?.let { token ->
            deleteTask(token, taskId)
        }
    }

    suspend fun getStudentAppointments(): List<StudentAppointments> {
        studentAppointments = user.token?.let { token ->
            getStudentAppointments(token)
        }?.body()?.data ?: emptyList()
        return studentAppointments
    }

    suspend fun postStudentAppointments(postStudentAppointmentsBody: PostStudentAppointmentsBody) {
        user.token?.let { token ->
            postStudentAppointments(token, postStudentAppointmentsBody)
        }
        studentAppointments = getStudentAppointments()
    }

    suspend fun postNewDiscipline(postNewDisciplineBody: PostNewDisciplineBody) {
        user.token?.let { token ->
            postNewDiscipline(token, postNewDisciplineBody)
        }
        disciplinesChanged = true
        disciplines = getDisciplines()
    }

    suspend fun getGroups(): List<Group> {
        return user.token?.let { token ->
            getGroups(token)
        }?.body()?.data ?: emptyList()
    }

    suspend fun getTeachers(): List<Teacher> {
        return user.token?.let { token ->
            getTeachers(token)
        }?.body()?.data ?: emptyList()
    }

    suspend fun getTests(taskId: Int): List<Test> {
        return user.token?.let { token ->
            getTests(token, taskId).body()?.data
        } ?: emptyList()
    }

    suspend fun postNewTest(test: Test) {
        user.token?.let { token -> postNewTest(token, test) }
    }

    suspend fun getStudentTasks(studentId: Int): List<Task> {
        return user.token?.let { token ->
            taskDisciplineData.id?.let { disciplineId ->
                getStudentTasks(
                    token,
                    studentId,
                    disciplineId
                ).body()?.data
            }
        } ?: emptyList()
    }
}