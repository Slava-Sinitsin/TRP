package com.example.trp.ui.viewmodels.common

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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class OldCodeReviewScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val codeReviewId: Int
) : ViewModel() {
    var codeReview by mutableStateOf(CodeReview())
        private set
    var errorMessage by mutableStateOf("")
        private set
    private var codeList by mutableStateOf(emptyList<AnnotatedString>())
    var padCodeList by mutableStateOf(emptyList<Pair<AnnotatedString, Boolean>>())
        private set
    val language = CodeLang.C
    val parser by mutableStateOf(PrettifyParser())
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
                }
                codeList = splitCode(codeReview.code ?: "")
                padCodeList = padCodeList(splitCode(codeReview.code ?: ""))
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
            Pair(AnnotatedString(code.text + padding), false)
        }
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
}