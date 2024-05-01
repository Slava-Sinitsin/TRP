package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.TeamAppointments
import com.example.trp.data.repository.UserAPIRepositoryImpl
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
    var teamAppointment by mutableStateOf(TeamAppointments())
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
    val taskScreens = listOf(
        TaskTabs.Description,
        TaskTabs.Solution,
        TaskTabs.Review
    )
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
                    repository.teamAppointments.find { it.task?.id == taskId } ?: TeamAppointments()
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
                updateLinesCount()
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
}