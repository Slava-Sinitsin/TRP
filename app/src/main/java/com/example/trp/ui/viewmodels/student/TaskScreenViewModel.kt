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
    val taskId: Int
) : ViewModel() {
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

    @AssistedFactory
    interface Factory {
        fun create(taskId: Int): TaskScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTaskScreenViewModel(
            factory: Factory,
            taskId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(taskId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                teamAppointment =
                    repository.teamAppointments.find { it.task?.id == taskId } ?: TeamAppointment()
                solutionTextFieldValue = TextFieldValue(
                    annotatedString = parseCodeAsAnnotatedString(
                        parser = parser,
                        theme = theme,
                        lang = language,
                        code = teamAppointment.task?.id?.let {
                            repository.getTaskSolution(it).code
                        } ?: ""
                    )
                )
                linesCount = solutionTextFieldValue.text.lineSequence().count()
                codeBck = solutionTextFieldValue.text
                isRunButtonEnabled = teamAppointment.status == TaskStatus.New.status
                        || teamAppointment.status == TaskStatus.InProgress.status
                        || teamAppointment.status == TaskStatus.OnTesting.status
                        || teamAppointment.status == TaskStatus.Tested.status
                updateLinesCount()
                codeReviews = teamAppointment.codeReviewIds?.mapNotNull {
                    repository.getCodeReview(it)?.body()?.data
                } ?: emptyList()
                taskScreens = if (codeReviews.isNotEmpty()) {
                    listOf(TaskTabs.Description, TaskTabs.Review)
                } else {
                    listOf(TaskTabs.Description, TaskTabs.Solution)
                }
                currentCodeReview =
                    codeReviews.maxByOrNull { it.mergeRequestId ?: -1 } ?: CodeReview()
                codeList = padCodeList(splitCode(currentCodeReview.code ?: ""))
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun updateTaskText(newTaskText: TextFieldValue) {
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
            try {
                onSaveCodeButtonClick()
                solutionTextFieldValue.text.let {
                    repository.postTaskSolution(
                        taskId = taskId,
                        solutionText = it
                    )
                }
                val output = repository.runCode(taskId)
                outputText =
                    if (output.data?.testPassed != null && output.data.totalTests != null) {
                        "Test passed: ${output.data.testPassed} / ${output.data.totalTests}"
                    } else {
                        "Error"
                    }
                reviewButtonEnabled = output.data?.testPassed == output.data?.totalTests
                        && repository.user.id == teamAppointment.team?.leaderStudentId
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
                        taskId = taskId,
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
        viewModelScope.launch {
            try {
                teamAppointment.id?.let { repository.postCodeReview(it) }
                reviewButtonEnabled = false
                isReviewDialogShow = false
                isRunButtonEnabled = false
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
            val padding = " ".repeat(paddingLength)
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
}