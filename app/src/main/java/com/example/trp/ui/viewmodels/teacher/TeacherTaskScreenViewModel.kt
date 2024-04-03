package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.solution.Solution
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TeacherTaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var task by mutableStateOf(Task())
        private set
    var taskCode by mutableStateOf("int add (int a, int b) {\n  printf(\"\\n\");\r\n\treturn a + b\r\n}")
    var disciplineName by mutableStateOf("")
    private var solution by mutableStateOf(Solution())
    var solutionTextFieldValue by mutableStateOf(TextFieldValue())
    var codeList by mutableStateOf(emptyList<AnnotatedString>())

    private val language = CodeLang.C
    private val parser by mutableStateOf(PrettifyParser())
    private var themeState by mutableStateOf(CodeThemeType.Monokai)
    private val theme by mutableStateOf(themeState.theme())

    @AssistedFactory
    interface Factory {
        fun create(taskId: Int): TeacherTaskScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherTaskScreenViewModel(
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
            solution = repository.taskSolution.copy(code = taskCode)
            codeList = splitCode(solution.code ?: "")
        }
    }

    private fun splitCode(input: String): List<AnnotatedString> {
        val regex = Regex("(?<!['\"])\\n(?!['\"])")
        return regex.split(input).map {
            parseCodeAsAnnotatedString(
                parser = parser,
                theme = theme,
                lang = language,
                code = it
            )
        }
    }
}