package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.repository.UserAPIRepositoryImpl
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import kotlinx.coroutines.launch

class TaskScreenViewModel(
    taskId: Int
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    var task by mutableStateOf(repository.task)
    var disciplineName by mutableStateOf("")
    private var solutionText by mutableStateOf("")
    var solutionTextFieldValue by mutableStateOf(TextFieldValue())
    var outputText by mutableStateOf("")

    private val language = CodeLang.C
    private val parser by mutableStateOf(PrettifyParser())
    private var themeState by mutableStateOf(CodeThemeType.Monokai)
    private val theme by mutableStateOf(themeState.theme())

    init {
        viewModelScope.launch {
            task = repository.getTask(taskId)
            disciplineName = repository.taskDisciplineData.name ?: ""
            solutionText = repository.taskSolution.code ?: ""
            solutionTextFieldValue = TextFieldValue(
                annotatedString = parseCodeAsAnnotatedString(
                    parser = parser,
                    theme = theme,
                    lang = language,
                    code = solutionText
                )
            )
        }
    }

    fun updateTaskText(newTaskText: TextFieldValue) {
        solutionText = newTaskText.text
        solutionTextFieldValue = newTaskText.copy(
            annotatedString = parseCodeAsAnnotatedString(
                parser = parser,
                theme = theme,
                lang = language,
                code = solutionText
            )
        )
    }

    fun onSaveCodeButtonClick() {
        viewModelScope.launch {
            solutionText.let { repository.postTaskSolution(it) }
        }
    }

    fun onRunCodeButtonClick() {
        // TODO
    }

    fun updateOutputText(newOutputText: String) {
        outputText = newOutputText
    }
}