package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.PostRateBody
import com.example.trp.data.mappers.Rating
import com.example.trp.data.mappers.TeamAppointment
import com.example.trp.data.mappers.tasks.CodeReview
import com.example.trp.data.mappers.tasks.solution.CommentLine
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

class TeacherTaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var teamAppointment by mutableStateOf(TeamAppointment())
        private set
    private var codeReviews by mutableStateOf(emptyList<CodeReview>())
    private var currentCodeReview by mutableStateOf(CodeReview())
    var codeList by mutableStateOf(emptyList<Pair<AnnotatedString, Boolean>>())
        private set
    var commentList by mutableStateOf(emptyList<CommentLine>())
        private set
    private val language = CodeLang.C
    private val parser by mutableStateOf(PrettifyParser())
    private var themeState by mutableStateOf(CodeThemeType.Monokai)
    private val theme by mutableStateOf(themeState.theme())
    var showAcceptDialog by mutableStateOf(false)
        private set
    var showRejectDialog by mutableStateOf(false)
        private set
    var showSubmitDialog by mutableStateOf(false)
        private set
    var reviewMessage by mutableStateOf("")
    var maxRate by mutableStateOf(0f)
    var rateList by mutableStateOf(emptyList<Rating>())
    var errorMessage by mutableStateOf("")
        private set
    var finishButtonsEnabled by mutableStateOf(true)
        private set
    var responseSuccess by mutableStateOf(false)
        private set

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
            try {
                teamAppointment =
                    repository.teamAppointments.find { it.task?.id == taskId } ?: TeamAppointment()
                codeReviews = teamAppointment.codeReviewIds?.mapNotNull {
                    repository.getCodeReview(it)?.body()?.data
                } ?: emptyList()
                currentCodeReview =
                    codeReviews.maxByOrNull { it.mergeRequestId ?: -1 } ?: CodeReview()
                codeList = padCodeList(splitCode(currentCodeReview.code ?: ""))
                maxRate = 10.toFloat() / 100f
                rateList = teamAppointment.team?.students?.mapIndexed { _, student ->
                    Rating(studentId = student.id, 8)
                } ?: emptyList()
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

    private fun isValidLines(input: String): Boolean {
        val regex = Regex("""^\d+(-\d+)?$""")
        if (!regex.matches(input)) {
            return false
        }
        val numbers = input.split("-").map { it.toInt() }
        if (numbers.size == 2 && numbers[0] >= numbers[1]) {
            return false
        }
        return !numbers.any { it < 1 || it > codeList.size }
    }

    fun addComment(lines: String = "") {
        commentList += CommentLine(lines = lines, isMatch = false)
        finishButtonsEnabled = false
    }

    fun getComment(index: Int): CommentLine {
        return commentList[index]
    }

    fun onCodeLineClick(lineNumber: Int) {
        if (!codeList[lineNumber - 1].second) {
            addComment(lines = lineNumber.toString())
            updateCodeListSelectedLines()
        }
    }

    fun updateCommentLines(index: Int, newCommentLines: String) {
        commentList = commentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                item.copy(lines = newCommentLines)
            } else {
                item
            }
        }
        updateCodeListSelectedLines()
    }

    private fun updateCodeListSelectedLines() {
        commentList = commentList.map { comment ->
            comment.copy(isMatch = comment.lines?.let { isValidLines(it) })
        }
        val intersectingList = findIntersectingLines()
        if (intersectingList.isNotEmpty()) {
            intersectingList.forEach { intersectingIndex ->
                commentList = commentList.mapIndexed { currentIndex, comment ->
                    if (currentIndex == intersectingIndex) {
                        comment.copy(isMatch = false)
                    } else {
                        comment
                    }
                }
            }
            commentList.forEach { comment ->
                if (comment.isMatch == false && comment.lines?.let { isValidLines(it) } == true) {
                    parseLineIndexes(comment.lines).forEach { commentLine ->
                        codeList = codeList.mapIndexed { currentIndex, item ->
                            if (currentIndex == commentLine) {
                                item.copy(second = false)
                            } else {
                                item
                            }
                        }
                    }
                }
            }
        }
        codeList = codeList.map { it.copy(second = false) }
        commentList.forEach { comment ->
            if (comment.isMatch == true) {
                val commentLines = comment.lines?.let { parseLineIndexes(it) }
                commentLines?.forEach { commentLine ->
                    codeList = codeList.mapIndexed { currentIndex, item ->
                        if (currentIndex == commentLine) {
                            item.copy(second = true)
                        } else {
                            item
                        }
                    }
                }
            }
        }
        updateFinishButtonsEnabled()
    }

    private fun updateFinishButtonsEnabled() {
        if (commentList.isNotEmpty()) {
            commentList.forEach {
                if (it.isMatch == false || it.comment.isNullOrEmpty()) {
                    finishButtonsEnabled = false
                    return@forEach
                }
                finishButtonsEnabled = true
            }
        } else {
            finishButtonsEnabled = true
        }
    }

    private fun findIntersectingLines(): List<Int> {
        val indexes = mutableListOf<Int>()
        val filteredCommentList = commentList.filter { it.isMatch == true }
        for ((index1, comment1) in filteredCommentList.withIndex()) {
            if (comment1.lines.isNullOrBlank()) continue
            for ((index2, comment2) in filteredCommentList.withIndex()) {
                if (index1 != index2 && !comment2.lines.isNullOrBlank()) {
                    val range1 = parseLineIndexes(comment1.lines)
                    val range2 = parseLineIndexes(comment2.lines)
                    if (range1.first in range2 || range1.last in range2 ||
                        range2.first in range1 || range2.last in range1
                    ) {
                        indexes.add(index1)
                        indexes.add(index2)
                    }
                }
            }
        }
        return indexes.distinct()
    }

    private fun parseLineIndexes(input: String): IntRange {
        val parts = input.split("-")
        return if (parts.size == 2) {
            val start = parts[0].toInt() - 1
            val end = parts[1].toInt() - 1
            if (start <= end) start..end else end..start
        } else {
            val line = input.toInt() - 1
            line..line
        }
    }

    fun updateComment(index: Int, newComment: String) {
        commentList = commentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                item.copy(comment = newComment)
            } else {
                item
            }
        }
        updateFinishButtonsEnabled()
    }

    fun onDeleteCommentLineClick(index: Int) {
        commentList = commentList.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }
        updateCodeListSelectedLines()
    }

    fun onRejectButtonClick() {
        showRejectDialog = true
    }

    fun rejectConfirmButtonClick() { // TODO
        viewModelScope.launch {
            try {
                currentCodeReview.mergeRequestId?.let { repository.closeCodeReview(it) }
                showRejectDialog = false
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

    fun rejectDismissButtonClick() {
        reviewMessage = ""
        showRejectDialog = false
    }

    fun onSubmitButtonClick() {
        showSubmitDialog = true
    }

    fun submitConfirmButtonClick() { // TODO
        viewModelScope.launch {
            try {
                currentCodeReview.mergeRequestId?.let { codeReviewId ->
                    repository.addNoteToCodeReview(
                        codeReviewId = codeReviewId,
                        note = reviewMessage
                    )
                }
                showSubmitDialog = false
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

    fun submitDismissButtonClick() {
        reviewMessage = ""
        showSubmitDialog = false
    }

    fun onAcceptButtonClick() {
        showAcceptDialog = true
    }

    fun acceptConfirmButtonClick() { // TODO
        viewModelScope.launch {
            try {
                currentCodeReview.mergeRequestId?.let { repository.approveCodeReview(it) }
                teamAppointment.id?.let {
                    repository.postRate(
                        teamAppointmentId = it,
                        postRateBody = PostRateBody(
                            listOf()
                        )
                    )
                }
                teamAppointment.id?.let {
                    repository.postRate(
                        teamAppointmentId = it,
                        PostRateBody(rateList)
                    )
                }
                showAcceptDialog = false
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

    fun acceptDismissButtonClick() {
        reviewMessage = ""
        rateList = rateList.map { it.copy(grade = 8) }
        showAcceptDialog = false
    }

    fun updateReviewMessage(newReviewMessage: String) {
        reviewMessage = newReviewMessage
    }

    fun updateMark(newRate: Float, index: Int) {
        rateList = rateList.mapIndexed { currentIndex, mark ->
            if (currentIndex == index) {
                mark.copy(grade = (newRate * 100).toInt())
            } else
                mark
        }
    }
}