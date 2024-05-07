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
    var codeList by mutableStateOf(emptyList<Pair<AnnotatedString, Boolean>>())
        private set
    var isAddMessageDialogShow by mutableStateOf(false)
        private set
    var reviewMessage by mutableStateOf("")
    var isRefreshing by mutableStateOf(false)
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
                    appointment.copy(task = appointment.task?.id?.let { repository.getTask(it) })
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
                codeReview.copy(notes = codeReview.notes?.sortedBy { it.id })
            } ?: CodeReview()
            codeList = padCodeList(splitCode(currentCodeReview.code ?: ""))
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
                solutionTextFieldValue.text.let { text ->
                    teamAppointment.task?.id?.let { taskId ->
                        repository.postTaskSolution(
                            taskId = taskId,
                            solutionText = text
                        )
                    }
                }
                val output = teamAppointment.task?.id?.let { repository.runCode(it) }
                if (output?.data?.testPassed != null && output.data.totalTests != null) {
                    outputText =
                        "Test passed: ${output.data.testPassed} / ${output.data.totalTests}"
                    reviewButtonEnabled =
                        repository.user.id == teamAppointment.team?.leaderStudentId && output.data.testPassed == output.data.totalTests
                } else {
                    outputText = when (output?.error) {
                        "ERROR WHILE COMPILATION CODE" -> "Compilation error"
                        else -> "Error"
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
                solutionTextFieldValue.text.let {
                    repository.postTaskSolution(
                        taskId = teamAppointmentId,
                        solutionText = it
                    )
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
                teamAppointment.id?.let { repository.postCodeReview(it) }
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
                    repository.addNoteToCodeReview(
                        codeReviewId = it,
                        note = reviewMessage
                    )
                }
                teamAppointment =
                    repository.getTeamAppointment(teamAppointmentId)?.let { appointment ->
                        appointment.copy(task = appointment.task?.id?.let { repository.getTask(it) })
                    } ?: TeamAppointment()
                currentCodeReview =
                    currentCodeReview.id?.let { repository.getCodeReview(it) } ?: CodeReview()
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
}