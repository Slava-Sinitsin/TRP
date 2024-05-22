package com.example.trp.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trp.data.maindb.MainDB
import com.example.trp.data.mappers.PostRateBody
import com.example.trp.data.mappers.PostStudentAppointmentsResponse
import com.example.trp.data.mappers.PostTeamAppointmentsBody
import com.example.trp.data.mappers.TeamAppointment
import com.example.trp.data.mappers.TeamAppointmentResponse
import com.example.trp.data.mappers.TeamAppointmentsResponse
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.data.mappers.disciplines.DisciplineResponse
import com.example.trp.data.mappers.disciplines.Disciplines
import com.example.trp.data.mappers.disciplines.PostNewDisciplineBody
import com.example.trp.data.mappers.disciplines.PostNewDisciplineResponse
import com.example.trp.data.mappers.tasks.CloseCodeReviewResponse
import com.example.trp.data.mappers.tasks.CodeReview
import com.example.trp.data.mappers.tasks.CodeReviewResponse
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.mappers.tasks.LabsResponse
import com.example.trp.data.mappers.tasks.OutputResponse
import com.example.trp.data.mappers.tasks.PostCodeReviewResponse
import com.example.trp.data.mappers.tasks.PostLabResponse
import com.example.trp.data.mappers.tasks.PostMultilineNoteBody
import com.example.trp.data.mappers.tasks.PostNewStudentBody
import com.example.trp.data.mappers.tasks.PostNewTeacherBody
import com.example.trp.data.mappers.tasks.PostRateResponse
import com.example.trp.data.mappers.tasks.PostTeamBody
import com.example.trp.data.mappers.tasks.PostTeamResponse
import com.example.trp.data.mappers.tasks.PostTestResponse
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.tasks.StudentResponse
import com.example.trp.data.mappers.tasks.Students
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.TaskResponse
import com.example.trp.data.mappers.tasks.Tasks
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.mappers.tasks.TeamResponse
import com.example.trp.data.mappers.tasks.Test
import com.example.trp.data.mappers.tasks.TestsResponse
import com.example.trp.data.mappers.tasks.solution.PostSolutionResponse
import com.example.trp.data.mappers.tasks.solution.Solution
import com.example.trp.data.mappers.tasks.solution.SolutionResponse
import com.example.trp.data.mappers.teacherappointments.DeleteGroupResponse
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.mappers.teacherappointments.GroupsResponse
import com.example.trp.data.mappers.teacherappointments.PostGroupResponse
import com.example.trp.data.mappers.teacherappointments.PostNewDisciplineWithInfoBody
import com.example.trp.data.mappers.teacherappointments.PostNewDisciplineWithInfoResponse
import com.example.trp.data.mappers.teacherappointments.PostNewGroupBody
import com.example.trp.data.mappers.teacherappointments.PostTeacherAppointmentBody
import com.example.trp.data.mappers.teacherappointments.PostTeacherResponse
import com.example.trp.data.mappers.teacherappointments.SimpleTeacherAppointmentsResponse
import com.example.trp.data.mappers.teacherappointments.Teacher
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsData
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsResponse
import com.example.trp.data.mappers.teacherappointments.TeachersResponse
import com.example.trp.data.mappers.user.AuthRequest
import com.example.trp.data.mappers.user.JWTDecoder
import com.example.trp.data.mappers.user.User
import com.example.trp.data.network.UserAPI
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response

class UserAPIRepositoryImpl(
    private val mainDB: MainDB,
    private val apiService: UserAPI
) : UserAPI {
    var user by mutableStateOf(User())

    var disciplines by mutableStateOf(emptyList<DisciplineData>())
    var currentDiscipline by mutableStateOf(0) // TODO

    var tasks by mutableStateOf(emptyList<Task>())
        private set
    private var tasksChanged by mutableStateOf(true)

    var task by mutableStateOf(Task()) // TODO

    var taskSolution by mutableStateOf(String())

    var teacherAppointments by mutableStateOf(emptyList<TeacherAppointmentsData>())

    var students by mutableStateOf(emptyList<Student>())

    var teamAppointments by mutableStateOf(emptyList<TeamAppointment>())

    var teams by mutableStateOf(emptyList<Team>())

    var labs by mutableStateOf(emptyList<Lab>())

    override suspend fun getUserResponse(authRequest: AuthRequest): Response<User> {
        return apiService.getUserResponse(authRequest)
    }

    override suspend fun getDisciplinesResponse(token: String): Response<Disciplines> {
        return apiService.getDisciplinesResponse("Bearer $token")
    }

    override suspend fun getTasks(token: String, labId: Int): Response<Tasks> {
        return apiService.getTasks("Bearer $token", labId)
    }

    override suspend fun getTask(token: String, id: Int): Response<TaskResponse> {
        return apiService.getTask("Bearer $token", id)
    }

    override suspend fun getTaskWithOpenTests(
        token: String,
        id: Int
    ): Response<TaskResponse> {
        return apiService.getTaskWithOpenTests("Bearer $token", id)
    }

    override suspend fun getDisciplineByID(token: String, id: Int): Response<DisciplineResponse> {
        return apiService.getDisciplineByID("Bearer $token", id)
    }

    override suspend fun getTaskSolution(
        token: String,
        taskId: Int
    ): Response<SolutionResponse> {
        return apiService.getTaskSolution("Bearer $token", taskId)
    }

    override suspend fun postTaskSolution(
        token: String,
        taskId: Int,
        code: String
    ): Response<PostSolutionResponse> {
        return apiService.postTaskSolution("Bearer $token", taskId, code)
    }

    override suspend fun teacherAppointments(token: String): Response<TeacherAppointmentsResponse> {
        return apiService.teacherAppointments("Bearer $token")
    }

    override suspend fun getStudents(
        token: String,
        id: Int
    ): Response<Students> {
        return apiService.getStudents("Bearer $token", id)
    }

    override suspend fun runCode(
        token: String,
        taskId: Int,
    ): Response<OutputResponse> {
        return apiService.runCode("Bearer $token", taskId)
    }

    override suspend fun putTask(token: String, taskId: Int, task: Task): Response<Task> {
        return apiService.putTask("Bearer $token", taskId, task)
    }

    override suspend fun deleteTask(token: String, taskId: Int): Response<Task> {
        return apiService.deleteTask("Bearer $token", taskId)
    }

    override suspend fun postTestableTask(token: String, task: Task): Response<Task> {
        return apiService.postTestableTask("Bearer $token", task)
    }

    override suspend fun postNonTestableTask(token: String, task: Task): Response<Task> {
        return apiService.postNonTestableTask("Bearer $token", task)
    }

    override suspend fun getAllTeamAppointments(
        token: String,
        disciplineId: Int,
        groupId: Int
    ): Response<TeamAppointmentsResponse> {
        return apiService.getAllTeamAppointments("Bearer $token", disciplineId, groupId)
    }

    override suspend fun postTeamAppointments(
        token: String,
        postTeamAppointmentsBody: PostTeamAppointmentsBody
    ): Response<PostStudentAppointmentsResponse> {
        return apiService.postTeamAppointments(
            "Bearer $token",
            postTeamAppointmentsBody
        )
    }

    override suspend fun postNewDiscipline(
        token: String,
        postNewDisciplineBody: PostNewDisciplineBody
    ): Response<PostNewDisciplineResponse> {
        return apiService.postNewDiscipline(
            "Bearer $token",
            postNewDisciplineBody
        )
    }

    override suspend fun getTeachers(token: String): Response<TeachersResponse> {
        return apiService.getTeachers("Bearer $token")
    }

    override suspend fun getGroups(token: String): Response<GroupsResponse> {
        return apiService.getGroups("Bearer $token")
    }

    override suspend fun getTests(token: String, taskId: Int): Response<TestsResponse> {
        return apiService.getTests("Bearer $token", taskId)
    }

    override suspend fun postNewTest(
        token: String,
        test: Test
    ): Response<PostTestResponse> {
        return apiService.postNewTest("Bearer $token", test)
    }

    override suspend fun getTeamAppointments(
        token: String,
        disciplineId: Int
    ): Response<TeamAppointmentsResponse> {
        return apiService.getTeamAppointments("Bearer $token", disciplineId)
    }

    override suspend fun getTeams(token: String, disciplineId: Int): Response<TeamResponse> {
        return apiService.getTeams("Bearer $token", disciplineId)
    }

    override suspend fun postNewTeam(
        token: String,
        postTeamBody: PostTeamBody
    ): Response<PostTeamResponse> {
        return apiService.postNewTeam("Bearer $token", postTeamBody)
    }

    override suspend fun getLabs(token: String, disciplineId: Int): Response<LabsResponse> {
        return apiService.getLabs("Bearer $token", disciplineId)
    }

    override suspend fun postNewLab(token: String, postLabBody: Lab): Response<PostLabResponse> {
        return apiService.postNewLab("Bearer $token", postLabBody)
    }

    override suspend fun getTeamTasks(token: String, teamId: Int): Response<Tasks> {
        return apiService.getTeamTasks("Bearer $token", teamId)
    }

    override suspend fun postNewGroup(
        token: String,
        group: PostNewGroupBody
    ): Response<PostGroupResponse> {
        return apiService.postNewGroup("Bearer $token", group)
    }

    override suspend fun postNewStudent(
        token: String,
        student: PostNewStudentBody
    ): Response<StudentResponse> {
        return apiService.postNewStudent("Bearer $token", student)
    }

    override suspend fun deleteGroup(token: String, id: Int): Response<DeleteGroupResponse> {
        return apiService.deleteGroup("Bearer $token", id)
    }

    override suspend fun postNewLectureTeacher(
        token: String,
        teacher: PostNewTeacherBody
    ): Response<PostTeacherResponse> {
        return apiService.postNewLectureTeacher("Bearer $token", teacher)
    }

    override suspend fun postNewLabWorkTeacher(
        token: String,
        teacher: PostNewTeacherBody
    ): Response<PostTeacherResponse> {
        return apiService.postNewLabWorkTeacher("Bearer $token", teacher)
    }

    override suspend fun postCodeReview(
        token: String,
        teamAppointmentId: Int
    ): Response<PostCodeReviewResponse> {
        return apiService.postCodeReview("Bearer $token", teamAppointmentId)
    }

    override suspend fun getCodeReview(
        token: String,
        codeReviewId: Int
    ): Response<CodeReviewResponse> {
        return apiService.getCodeReview("Bearer $token", codeReviewId)
    }

    override suspend fun closeCodeReview(
        token: String,
        codeReviewId: Int
    ): Response<CloseCodeReviewResponse> {
        return apiService.closeCodeReview("Bearer $token", codeReviewId)
    }

    override suspend fun approveCodeReview(
        token: String,
        codeReviewId: Int
    ): Response<CloseCodeReviewResponse> {
        return apiService.approveCodeReview("Bearer $token", codeReviewId)
    }

    override suspend fun addNoteToCodeReview(
        token: String,
        codeReviewId: Int,
        note: String
    ): Response<CodeReviewResponse> {
        return apiService.addNoteToCodeReview("Bearer $token", codeReviewId, note)
    }

    override suspend fun postRate(
        token: String,
        teamAppointmentId: Int,
        postRateBody: PostRateBody
    ): Response<PostRateResponse> {
        return apiService.postRate("Bearer $token", teamAppointmentId, postRateBody)
    }

    override suspend fun getTeamAppointment(
        token: String,
        teamAppointmentId: Int
    ): Response<TeamAppointmentResponse> {
        return apiService.getTeamAppointment("Bearer $token", teamAppointmentId)
    }

    override suspend fun postMultilineNote(
        token: String,
        codeReviewId: Int,
        postMultilineNoteBody: PostMultilineNoteBody
    ): Response<CodeReviewResponse> {
        return apiService.postMultilineNote(
            "Bearer $token",
            codeReviewId,
            postMultilineNoteBody
        )
    }

    override suspend fun postNewDisciplineWithInfo(
        token: String,
        postNewDisciplineWithInfoBody: PostNewDisciplineWithInfoBody
    ): Response<PostNewDisciplineWithInfoResponse> {
        return apiService.postNewDisciplineWithInfo("Bearer $token", postNewDisciplineWithInfoBody)
    }

    override suspend fun postNewTeacherAppointment(
        token: String,
        postNewTeacherAppointmentBody: PostTeacherAppointmentBody
    ): Response<SimpleTeacherAppointmentsResponse> {
        return apiService.postNewTeacherAppointment("Bearer $token", postNewTeacherAppointmentBody)
    }

    suspend fun getActiveUser(): User {
        user = mainDB.userDAO.getActiveUser() ?: User()
        return user
    }

    suspend fun login(login: String, password: String): User {
        val response = getUserResponse(
            AuthRequest(login, password)
        )
        response.body()?.let {
            it.token?.let { token ->
                it.message?.let { message ->
                    return addUserInformation(login, password, token, message)
                }
            }
        } ?: run {
            response.errorBody()?.let { errorBody ->
                return User().copy(message = JSONObject(errorBody.string()).getString("error"))
            } ?: run {
                return User().copy(message = "Bad response")
            }
        }
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
            isActive = true,
            isLogged = true
        )
        mainDB.userDAO.setAllIsActiveFalse()
        mainDB.userDAO.insertActiveUser(updatedUser)
        user = mainDB.userDAO.getActiveUser() ?: User()
        return user
    }

    suspend fun setUserIsLogged(newUserIsLogged: Boolean) {
        mainDB.userDAO.insertActiveUser(user.copy(isLogged = newUserIsLogged))
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

    suspend fun getDisciplines(update: Boolean = false): List<DisciplineData> {
        if (update) {
            mainDB.disciplinesDAO.deleteAllDisciplines()
            val response = user.token?.let { getDisciplinesResponse(it) }
            response?.body()?.let {
                it.list?.forEach { discipline ->
                    mainDB.disciplinesDAO.insertDiscipline(discipline)
                }
            } ?: response?.errorBody()?.let {
                return emptyList()
            }
        }
        disciplines = mainDB.disciplinesDAO.getDisciplines() ?: emptyList()
        return disciplines
    }

    suspend fun getTasks(labId: Int): List<Task> {
        if (tasksChanged) {
            val response =
                user.token?.let { getTasks(it, labId) }
            response?.body()?.let {
                tasks = it.data ?: emptyList()
            } ?: response?.errorBody()?.let {
                tasks = emptyList()
            }
        }
        return tasks
    }

    suspend fun getTask(
        taskId: Int,
        testable: Boolean = false,
        isTeacher: Boolean = false
    ): Task {
        return if (testable) {
            if (isTeacher) {
                getTask(taskId).copy(tests = getTests(taskId))
            } else {
                getTaskWithOpenTests(taskId)?.copy(solution = getTaskSolution(taskId)) ?: Task()
            }
        } else {
            if (isTeacher) {
                getTask(taskId)
            } else {
                getTask(taskId).copy(solution = getTaskSolution(taskId))
            }
        }
    }

    private suspend fun getTaskSolution(taskId: Int): Solution {
        return user.token?.let { token ->
            getTaskSolution(token, taskId)
        }?.body()?.solution ?: Solution()
    }

    suspend fun postTaskSolution(taskId: Int, solutionText: String) {
        user.token?.let { token ->
            postTaskSolution(token, taskId, solutionText)
        }
    }

    suspend fun runCode(taskId: Int): OutputResponse {
        val response =
            user.token?.let { token -> runCode(token, taskId) }
        return response?.body() ?: response?.errorBody()?.let {
            val errorBody = it.string()
            OutputResponse().copy(
                status = JSONObject(errorBody).getInt("status"),
                message = JSONObject(errorBody).getString("message"),
                error = JSONObject(errorBody).getString("error")
            )
        } ?: OutputResponse()
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

    suspend fun postTestableTask(task: Task) {
        user.token?.let { token ->
            postTestableTask(token, task)
        }
    }

    suspend fun postNonTestableTask(task: Task) {
        user.token?.let { token ->
            postNonTestableTask(token, task)
        }
    }

    suspend fun deleteTask(taskId: Int) {
        user.token?.let { token ->
            deleteTask(token, taskId)
        }
    }

    suspend fun getAllTeamAppointments(disciplineId: Int, groupId: Int): List<TeamAppointment> {
        teamAppointments = user.token?.let { token ->
            getAllTeamAppointments(token, disciplineId, groupId)
        }?.body()?.data ?: emptyList()
        return teamAppointments
    }

    suspend fun postTeamAppointments(postTeamAppointmentsBody: PostTeamAppointmentsBody) {
        user.token?.let { token ->
            postTeamAppointments(token, postTeamAppointmentsBody)
        }
    }

    suspend fun postNewDiscipline(postNewDisciplineBody: PostNewDisciplineBody) {
        user.token?.let { token ->
            postNewDiscipline(token, postNewDisciplineBody)
        }
        disciplines = getDisciplines(update = true)
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

    suspend fun getTeamAppointments(disciplineId: Int): List<TeamAppointment> {
        teamAppointments = user.token?.let { token ->
            getTeamAppointments(token, disciplineId).body()?.data
        } ?: emptyList()
        return teamAppointments
    }

    suspend fun getTeams(disciplineId: Int): List<Team> {
        teams = user.token?.let { token ->
            getTeams(token, disciplineId).body()?.data
        } ?: emptyList()
        return teams
    }

    suspend fun postNewTeam(team: PostTeamBody) {
        user.token?.let { token -> postNewTeam(token, team) }
    }

    fun setCurrentDisciplineId(id: Int) {
        currentDiscipline = id
    }

    suspend fun getLabs(disciplineId: Int): List<Lab> {
        labs = user.token?.let { token ->
            getLabs(token, disciplineId).body()?.data
        } ?: emptyList()
        return labs
    }

    suspend fun postNewLab(lab: Lab) {
        user.token?.let { token -> postNewLab(token, lab) }
    }

    suspend fun getTeamTasks(teamId: Int): List<Task> {
        if (tasksChanged) {
            val response =
                user.token?.let { getTeamTasks(it, teamId) }
            response?.body()?.let {
                tasks = it.data ?: emptyList()
            } ?: response?.errorBody()?.let {
                tasks = emptyList()
            }
        }
        return tasks
    }

    suspend fun postNewGroup(group: PostNewGroupBody): Response<PostGroupResponse>? {
        return user.token?.let { token -> postNewGroup(token, group) }
    }

    suspend fun postNewStudent(student: PostNewStudentBody): Response<StudentResponse>? {
        return user.token?.let { token -> postNewStudent(token, student) }
    }

    suspend fun deleteGroup(id: Int): DeleteGroupResponse {
        return user.token?.let { token -> deleteGroup(token, id).body() } ?: DeleteGroupResponse()
    }

    suspend fun postNewLectureTeacher(teacher: PostNewTeacherBody): Response<PostTeacherResponse>? {
        return user.token?.let { token -> postNewLectureTeacher(token, teacher) }
    }

    suspend fun postNewLabWorkTeacher(teacher: PostNewTeacherBody): Response<PostTeacherResponse>? {
        return user.token?.let { token -> postNewLabWorkTeacher(token, teacher) }
    }

    suspend fun postCodeReview(teamAppointmentId: Int): Response<PostCodeReviewResponse>? {
        return user.token?.let { token -> postCodeReview(token, teamAppointmentId) }
    }

    suspend fun getCodeReview(codeReviewId: Int): CodeReview {
        return user.token?.let { token -> getCodeReview(token, codeReviewId) }?.body()?.data
            ?: CodeReview()
    }

    suspend fun closeCodeReview(codeReviewId: Int): Response<CloseCodeReviewResponse>? {
        return user.token?.let { token -> closeCodeReview(token, codeReviewId) }
    }

    suspend fun approveCodeReview(codeReviewId: Int): Response<CloseCodeReviewResponse>? {
        return user.token?.let { token -> approveCodeReview(token, codeReviewId) }
    }

    suspend fun addNoteToCodeReview(
        codeReviewId: Int,
        note: String
    ): Response<CodeReviewResponse>? {
        return user.token?.let { token -> addNoteToCodeReview(token, codeReviewId, note) }
    }

    suspend fun postRate(
        teamAppointmentId: Int,
        postRateBody: PostRateBody
    ): Response<PostRateResponse>? {
        return user.token?.let { token -> postRate(token, teamAppointmentId, postRateBody) }
    }

    suspend fun getTeamAppointment(
        teamAppointmentId: Int,
    ): TeamAppointment? {
        return user.token?.let { token -> getTeamAppointment(token, teamAppointmentId) }
            ?.body()?.data
    }

    suspend fun postMultilineNote(
        codeReviewId: Int,
        postMultilineNoteBody: PostMultilineNoteBody
    ): CodeReviewResponse? {
        return user.token?.let { token ->
            postMultilineNote(
                token,
                codeReviewId,
                postMultilineNoteBody
            )
        }?.body()
    }

    private suspend fun getTask(
        taskId: Int,
    ): Task {
        return user.token?.let { token -> getTask(token, taskId) }?.body()?.task ?: Task()
    }

    private suspend fun getTaskWithOpenTests(
        taskId: Int,
    ): Task? {
        return user.token?.let { token -> getTaskWithOpenTests(token, taskId) }?.body()?.task
    }

    suspend fun postNewDisciplineWithInfo(
        postNewDisciplineWithInfo: PostNewDisciplineWithInfoBody,
    ): PostNewDisciplineWithInfoResponse? {
        return user.token?.let { token ->
            postNewDisciplineWithInfo(
                token,
                postNewDisciplineWithInfo
            )
        }?.body()
    }

    suspend fun postNewTeacherAppointment(
        postNewTeacherAppointmentBody: PostTeacherAppointmentBody
    ): SimpleTeacherAppointmentsResponse? {
        return user.token?.let { token ->
            postNewTeacherAppointment(
                token,
                postNewTeacherAppointmentBody
            )
        }?.body()
    }
}