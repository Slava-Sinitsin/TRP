package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.TeamAppointment
import com.example.trp.data.mappers.tasks.CodeReview
import com.example.trp.data.mappers.tasks.PostMultilineNoteBody
import com.example.trp.data.mappers.user.User
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.TaskStatus
import com.example.trp.ui.components.tabs.TaskTabs
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val teamAppointmentId: Int
) : ViewModel() {
    var user by mutableStateOf(User())
        private set
    var teamAppointment by mutableStateOf(TeamAppointment())
        private set
    var solutionTextFieldValue by mutableStateOf(TextFieldValue())
        private set
    private var linesCount by mutableStateOf(0)
    var linesText by mutableStateOf("")
        private set
    var outputText by mutableStateOf("")
        private set
    private val language = CodeLang.C
    private val parser by mutableStateOf(PrettifyParser())
    private var themeState by mutableStateOf(CodeThemeType.Monokai)
    private val theme by mutableStateOf(themeState.theme())
    var taskScreens by mutableStateOf(emptyList<TaskTabs>())
    var selectedTabIndex by mutableStateOf(1)
        private set
    var userScrollEnabled by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf("")
        private set
    var runCodeButtonEnabled by mutableStateOf(true)
        private set
    var codeBck by mutableStateOf("")
        private set
    var isSaveDialogShow by mutableStateOf(false)
        private set
    var responseSuccess by mutableStateOf(false)
        private set
    var reviewButtonEnabled by mutableStateOf(false)
        private set
    var isReviewDialogShow by mutableStateOf(false)
        private set
    var isRunButtonEnabled by mutableStateOf(false)
        private set
    var codeReviews by mutableStateOf(emptyList<CodeReview>())
        private set
    var currentCodeReview by mutableStateOf(CodeReview())
        private set
    private var codeList by mutableStateOf(emptyList<AnnotatedString>())
    var padCodeList by mutableStateOf(emptyList<Pair<AnnotatedString, Boolean>>())
        private set
    var isAddMessageDialogShow by mutableStateOf(false)
        private set
    var reviewMessage by mutableStateOf("")
    var isRefreshing by mutableStateOf(false)
        private set
    var codeThreadCommentList by mutableStateOf(emptyList<String>())
        private set

    @AssistedFactory
    interface Factory {
        fun create(taskId: Int): TaskScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTaskScreenViewModel(
            factory: Factory,
            teamAppointmentId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(teamAppointmentId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch { init() }
    }

    private suspend fun init() {
        try {
            user = repository.user
            teamAppointment =
                repository.getTeamAppointment(teamAppointmentId)?.let { appointment ->
                    if (appointment.task?.testable == true) {
                        appointment.copy(task = appointment.task.id?.let { taskId ->
                            repository.getTask(
                                taskId = taskId,
                                testable = true
                            )
                        })
                    } else {
                        appointment.copy(task = appointment.task?.id?.let { taskId ->
                            repository.getTask(
                                taskId = taskId,
                                testable = false
                            )
                        })
                    }
                } ?: TeamAppointment()
            solutionTextFieldValue = TextFieldValue(
                annotatedString = parseCodeAsAnnotatedString(
                    parser = parser,
                    theme = theme,
                    lang = language,
                    code = teamAppointment.task?.solution?.code ?: ""
                )
            )
            linesCount = solutionTextFieldValue.text.lineSequence().count()
            codeBck = solutionTextFieldValue.text
            updateLinesCount()
            codeReviews = teamAppointment.codeReviewIds?.map {
                repository.getCodeReview(it)
            }?.sortedByDescending { it.id } ?: emptyList()
            currentCodeReview = codeReviews.maxByOrNull { it.id ?: -1 }?.let { codeReview ->
                codeReview.copy(
                    taskMessages = codeReview.taskMessages?.sortedBy {
                        ZonedDateTime.parse(it.createdAt).toInstant()
                    },
                    codeThreads = codeReview.codeThreads?.map {
                        it.copy(
                            messages = it.messages?.sortedBy { message ->
                                ZonedDateTime.parse(message.createdAt).toInstant()
                            }
                        )
                    }
                )
            } ?: CodeReview()
            codeList = splitCode(currentCodeReview.code ?: "").map { code ->
                parseCodeAsAnnotatedString(
                    parser = parser,
                    theme = theme,
                    lang = language,
                    code = code.text
                )
            }
            padCodeList = padCodeList(splitCode(currentCodeReview.code ?: ""))
            reviewButtonEnabled = teamAppointment.status == TaskStatus.Tested.status
            taskScreens =
                if (teamAppointment.status == TaskStatus.New.status
                    || teamAppointment.status == TaskStatus.InProgress.status
                    || teamAppointment.status == TaskStatus.OnTesting.status
                    || teamAppointment.status == TaskStatus.Tested.status
                    || teamAppointment.status == TaskStatus.SentToRework.status
                ) {
                    isRunButtonEnabled = true
                    if (codeReviews.isEmpty()) {
                        listOf(TaskTabs.Description, TaskTabs.Solution)
                    } else {
                        listOf(TaskTabs.Description, TaskTabs.Solution, TaskTabs.History)
                    }
                } else {
                    codeReviews = codeReviews.filter { it.id != currentCodeReview.id }
                    if (codeReviews.isEmpty()) {
                        listOf(TaskTabs.Description, TaskTabs.Review)
                    } else {
                        listOf(TaskTabs.Description, TaskTabs.Review, TaskTabs.History)
                    }
                }
            codeThreadCommentList =
                currentCodeReview.codeThreads?.size?.let { List(it) { "" } } ?: emptyList()
        } catch (e: SocketTimeoutException) {
            updateErrorMessage("Timeout")
        } catch (e: ConnectException) {
            updateErrorMessage("Check internet connection")
        } catch (e: Exception) {
            updateErrorMessage("Error")
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            init()
            isRefreshing = false
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun updateTaskText(newTaskText: TextFieldValue) {
        reviewButtonEnabled = false
        solutionTextFieldValue = newTaskText.copy(
            annotatedString = parseCodeAsAnnotatedString(
                parser = parser,
                theme = theme,
                lang = language,
                code = newTaskText.text
            )
        )
        updateLinesCount()
    }

    private fun updateLinesCount() {
        linesText = ""
        linesCount = solutionTextFieldValue.text.lineSequence().count()
        for (i in 1..linesCount) {
            linesText += "${i}\n"
        }
    }

    fun onRunCodeButtonClick() {
        viewModelScope.launch {
            outputText = "Testing..."
            runCodeButtonEnabled = false
            reviewButtonEnabled = false
            try {
                solutionTextFieldValue.text.let { solutionText ->
                    teamAppointment.task?.id?.let { taskId ->
                        repository.postTaskSolution(
                            taskId = taskId,
                            solutionText = solutionText
                        )
                    }
                }
                val output = teamAppointment.task?.id?.let { repository.runCode(it) }
                if (teamAppointment.task?.testable == true) {
                    if (output?.data?.testPassed != null && output.data.totalTests != null) {
                        if (output.data.testPassed == output.data.totalTests) {
                            outputText =
                                "Test passed: ${output.data.testPassed} / ${output.data.totalTests}"
                            reviewButtonEnabled =
                                repository.user.id == teamAppointment.team?.leaderStudentId
                        } else {
                            outputText = if (output.data.testsInfo?.isNotEmpty() == true) {
                                "Test passed: ${output.data.testPassed} / ${output.data.totalTests}\n" +
                                        "--------------------------------------------\n" +
                                        output.data.testsInfo.joinToString(separator = "\n") { test ->
                                            "Input: " + test.input +
                                                    "\nOutput: " + test.output +
                                                    "\nExpected: " + test.expected +
                                                    "\n--------------------------------------------\n"
                                        }
                            } else {
                                "Test passed: ${output.data.testPassed} / ${output.data.totalTests}"
                            }
                            reviewButtonEnabled = false
                        }
                    } else {
                        outputText = when (output?.error) {
                            "ERROR WHILE COMPILATION CODE" -> "Compilation error"
                            "ERROR WHILE EXECUTE CODE" -> "Execute error"
                            "TIMEOUT ERROR" -> "Timeout error"
                            else -> "Error"
                        }
                    }
                } else {
                    if (output?.error != null) {
                        outputText = when (output.error) {
                            "ERROR WHILE COMPILATION CODE" -> "Compilation error"
                            "ERROR WHILE EXECUTE CODE" -> "Execute error"
                            "TIMEOUT ERROR" -> "Timeout error"
                            else -> "Error"
                        }
                    } else {
                        if (output?.data?.executeInfo?.stdout != null) {
                            outputText = "Stdout: " + output.data.executeInfo.stdout + "\n"
                        }
                        if (output?.data?.executeInfo?.stderr != null) {
                            outputText += "Stderr: " + output.data.executeInfo.stderr
                        }
                    }
                }
                codeBck = solutionTextFieldValue.text
            } catch (e: SocketTimeoutException) {
                outputText = "Timeout"
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                outputText = "Check internet connection"
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                outputText = "Error"
                updateErrorMessage("Error")
            } finally {
                runCodeButtonEnabled = true
            }
        }
    }

    fun updateSelectedTabIndex(index: Int) {
        selectedTabIndex = index
    }

    fun updateEnableUserScroll(isEnable: Boolean) {
        userScrollEnabled = isEnable
    }

    fun showSaveDialog() {
        responseSuccess = false
        isSaveDialogShow = true
    }

    fun onSaveCodeButtonClick() {
        responseSuccess = false
        viewModelScope.launch {
            try {
                solutionTextFieldValue.text.let { solutionText ->
                    teamAppointment.task?.id?.let { taskId ->
                        repository.postTaskSolution(
                            taskId = taskId,
                            solutionText = solutionText
                        )
                    }
                }
                responseSuccess = true
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }

    fun onDoNotSaveCodeButtonClick() {
        isSaveDialogShow = false
    }

    fun showReviewDialog() {
        isReviewDialogShow = true
    }

    fun onDismissReviewButtonClick() {
        isReviewDialogShow = false
    }

    fun onPostCodeReviewButtonClick() {
        responseSuccess = false
        viewModelScope.launch {
            try {
                repository.postCodeReview(teamAppointmentId)
                responseSuccess = true
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }

    private fun splitCode(input: String): List<AnnotatedString> {
        val regex = Regex("(?<!['\"])\\n(?!['\"])")
        return regex.split(input).map {
            AnnotatedString(it)
        }
    }

    private fun padCodeList(codeList: List<AnnotatedString>): List<Pair<AnnotatedString, Boolean>> {
        val maxLength = codeList.maxBy { it.text.length }.text.length
        return codeList.map { code ->
            val paddingLength = maxLength - code.text.length + 2
            val padding = " ".repeat(paddingLength + 100)
            Pair(
                parseCodeAsAnnotatedString(
                    parser = parser,
                    theme = theme,
                    lang = language,
                    code = code.text + padding
                ), false
            )
        }
    }

    fun onAddMessageButtonClick() {
        isAddMessageDialogShow = true
    }

    fun onConfirmAddMessageButtonClick() {
        viewModelScope.launch {
            try {
                currentCodeReview.id?.let {
                    if (reviewMessage.isNotBlank()) {
                        repository.addNoteToCodeReview(
                            codeReviewId = it,
                            note = reviewMessage
                        )
                    }
                }
                codeThreadCommentList.forEachIndexed { index, comment ->
                    currentCodeReview.id?.let { currentCodeReviewId ->
                        if (comment.isNotBlank()) {
                            repository.postMultilineNote(
                                codeReviewId = currentCodeReviewId,
                                PostMultilineNoteBody(
                                    note = comment,
                                    beginLineNumber = currentCodeReview.codeThreads?.get(index)?.beginLineNumber,
                                    endLineNumber = currentCodeReview.codeThreads?.get(index)?.endLineNumber
                                )
                            )
                        }
                    }
                }
                teamAppointment =
                    repository.getTeamAppointment(teamAppointmentId)?.let { appointment ->
                        if (appointment.task?.testable == true) {
                            appointment.copy(task = appointment.task.id?.let { taskId ->
                                repository.getTask(
                                    taskId = taskId,
                                    testable = true
                                )
                            })
                        } else {
                            appointment.copy(task = appointment.task?.id?.let { taskId ->
                                repository.getTask(
                                    taskId = taskId,
                                    testable = false
                                )
                            })
                        }
                    } ?: TeamAppointment()
                currentCodeReview =
                    currentCodeReview.id?.let { repository.getCodeReview(it) }?.let { codeReview ->
                        codeReview.copy(
                            taskMessages = codeReview.taskMessages?.sortedBy {
                                ZonedDateTime.parse(it.createdAt).toInstant()
                            },
                            codeThreads = codeReview.codeThreads?.map {
                                it.copy(
                                    messages = it.messages?.sortedBy { message ->
                                        ZonedDateTime.parse(message.createdAt).toInstant()
                                    }
                                )
                            }
                        )
                    } ?: CodeReview()
                codeThreadCommentList =
                    currentCodeReview.codeThreads?.size?.let { List(it) { "" } } ?: emptyList()
                isAddMessageDialogShow = false
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }

    fun onDismissAddMessageButtonClick() {
        reviewMessage = ""
        isAddMessageDialogShow = false
    }

    fun updateReviewMessage(newReviewMessage: String) {
        reviewMessage = newReviewMessage
    }

    fun getCodeInRange(range: IntRange): List<Pair<Int, AnnotatedString>> {
        return codeList
            .filterIndexed { index, _ -> index + 1 in range }
            .mapIndexed { index, annotatedString -> index + range.first to annotatedString }
    }

    fun formatDate(isoDate: String): String {
        val dateTime = ZonedDateTime.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm")
        return dateTime.format(formatter)
    }

    fun updateCodeThreadComment(index: Int, comment: String) {
        codeThreadCommentList = codeThreadCommentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                comment
            } else {
                item
            }
        }
    }
}