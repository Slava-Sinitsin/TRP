package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var task by mutableStateOf(Task())
        private set
    var disciplineName by mutableStateOf("")
        private set
    var solutionTextFieldValue by mutableStateOf(TextFieldValue())
        private set
    var outputText by mutableStateOf("")
        private set

    private val language = CodeLang.C
    private val parser by mutableStateOf(PrettifyParser())
    private var themeState by mutableStateOf(CodeThemeType.Monokai)
    private val theme by mutableStateOf(themeState.theme())
    var linesCount by mutableStateOf(emptyArray<Float>())
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
            task = repository.getTask(taskId)
            disciplineName = repository.taskDisciplineData.name ?: ""
            solutionTextFieldValue = TextFieldValue(
                annotatedString = parseCodeAsAnnotatedString(
                    parser = parser,
                    theme = theme,
                    lang = language,
                    code = repository.taskSolution.code ?: ""
                )
            )
        }
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
    }

    fun updateLinesCount(textLayoutResult: TextLayoutResult) {
        linesCount = Array(textLayoutResult.lineCount) { textLayoutResult.getLineTop(it) }
    }

    fun onSaveCodeButtonClick() {
        viewModelScope.launch {
            solutionTextFieldValue.text.let { repository.postTaskSolution(it) }
        }
    }

    fun onRunCodeButtonClick() {
        viewModelScope.launch {
            solutionTextFieldValue.text.let { repository.postTaskSolution(it) }
            val output = repository.runCode()
            outputText = output.data ?: output.error ?: "Error"
        }
    }
}