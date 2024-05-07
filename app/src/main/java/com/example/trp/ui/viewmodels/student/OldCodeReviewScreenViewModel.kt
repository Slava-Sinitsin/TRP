package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.CodeReview
import com.example.trp.data.mappers.user.User
import com.example.trp.data.repository.UserAPIRepositoryImpl
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

class OldCodeReviewScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val codeReviewId: Int
) : ViewModel() {
    var codeReview by mutableStateOf(CodeReview())
        private set
    var errorMessage by mutableStateOf("")
        private set
    var codeList by mutableStateOf(emptyList<Pair<AnnotatedString, Boolean>>())
        private set
    private val language = CodeLang.C
    private val parser by mutableStateOf(PrettifyParser())
    private var themeState by mutableStateOf(CodeThemeType.Monokai)
    private val theme by mutableStateOf(themeState.theme())
    var user by mutableStateOf(User())
        private set

    @AssistedFactory
    interface Factory {
        fun create(codeReviewId: Int): OldCodeReviewScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideOldCodeReviewScreenViewModel(
            factory: Factory,
            codeReviewId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(codeReviewId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                codeReview = repository.getCodeReview(codeReviewId).let { codeReview ->
                    codeReview.copy(notes = codeReview.notes?.sortedBy { it.id })
                }
                codeList = padCodeList(splitCode(codeReview.code ?: ""))
                user = repository.user
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
}