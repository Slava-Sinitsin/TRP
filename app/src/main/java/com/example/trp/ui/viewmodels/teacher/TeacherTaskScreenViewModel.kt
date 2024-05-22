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
import com.example.trp.data.mappers.tasks.PostMultilineNoteBody
import com.example.trp.data.mappers.tasks.solution.CommentLine
import com.example.trp.data.mappers.user.User
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.TaskStatus
import com.example.trp.ui.components.tabs.ReviewTabs
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

class TeacherTaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val teamAppointmentId: Int
) : ViewModel() {
    var user by mutableStateOf(User())
        private set
    var teamAppointment by mutableStateOf(TeamAppointment())
        private set
    var codeReviews by mutableStateOf(emptyList<CodeReview>())
        private set
    var currentCodeReview by mutableStateOf(CodeReview())
        private set
    private var codeList by mutableStateOf(emptyList<AnnotatedString>())
    var padCodeList by mutableStateOf(emptyList<Pair<AnnotatedString, Boolean>>())
        private set
    var commentList by mutableStateOf(emptyList<CommentLine>())
        private set
    val language = CodeLang.C
    val parser by mutableStateOf(PrettifyParser())
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
    var selectedTabIndex by mutableStateOf(1)
    var reviewScreens by mutableStateOf(emptyList<ReviewTabs>())
    var isRefreshing by mutableStateOf(false)
        private set
    var codeThreadCommentList by mutableStateOf(emptyList<String>())
        private set

    @AssistedFactory
    interface Factory {
        fun create(taskId: Int): TeacherTaskScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherTaskScreenViewModel(
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
                                testable = true,
                                isTeacher = true
                            )
                        })
                    } else {
                        appointment.copy(task = appointment.task?.id?.let { taskId ->
                            repository.getTask(
                                taskId = taskId,
                                testable = false,
                                isTeacher = true
                            )
                        })
                    }
                } ?: TeamAppointment()
            codeReviews = teamAppointment.codeReviewIds?.map { codeReviewId ->
                repository.getCodeReview(codeReviewId)
            }?.sortedByDescending { it.id } ?: emptyList()
            reviewScreens = if (teamAppointment.status == TaskStatus.SentToCodeReview.status
                || teamAppointment.status == TaskStatus.CodeReview.status
                || teamAppointment.status == TaskStatus.WaitingForGrade.status
                || teamAppointment.status == TaskStatus.Rated.status
            ) {
                currentCodeReview = codeReviews.maxByOrNull { it.id ?: -1 }?.let { codeReview ->
                    codeReview.copy(
                        taskMessages = codeReview.taskMessages?.sortedBy {
                            ZonedDateTime.parse(it.createdAt).toInstant() // TODO
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
                codeReviews = codeReviews.filter { it.id != currentCodeReview.id }
                if (codeReviews.isEmpty()) {
                    listOf(ReviewTabs.Description, ReviewTabs.Review)
                } else {
                    listOf(ReviewTabs.Description, ReviewTabs.Review, ReviewTabs.History)
                }
            } else {
                listOf(ReviewTabs.Description, ReviewTabs.History)
            }
            codeList = splitCode(currentCodeReview.code ?: "")
            padCodeList = padCodeList(splitCode(currentCodeReview.code ?: ""))
            maxRate = 10.toFloat() / 100f
            rateList = teamAppointment.team?.students?.mapIndexed { _, student ->
                Rating(studentId = student.id, 8)
            } ?: emptyList()
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

    fun getCodeInRange(range: IntRange): List<Pair<Int, AnnotatedString>> {
        return codeList
            .filterIndexed { index, _ -> index + 1 in range }
            .mapIndexed { index, annotatedString -> index + range.first to annotatedString }
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

    private fun isValidLines(input: String): Boolean {
        val regex = Regex("""^\d+(-\d+)?$""")
        if (!regex.matches(input)) {
            return false
        }
        val numbers = input.split("-").map { it.toInt() }
        if (numbers.size == 2 && numbers[0] >= numbers[1]) {
            return false
        }
        return !numbers.any { it < 1 || it > padCodeList.size }
    }

    fun addComment(lines: String = "") {
        commentList += CommentLine(lines = lines, isMatch = false)
        finishButtonsEnabled = false
    }

    fun getComment(index: Int): CommentLine {
        return commentList[index]
    }

    fun onCodeLineClick(lineNumber: Int) {
        if (!padCodeList[lineNumber - 1].second) {
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
                        padCodeList = padCodeList.mapIndexed { currentIndex, item ->
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
        padCodeList = padCodeList.map { it.copy(second = false) }
        commentList.forEach { comment ->
            if (comment.isMatch == true) {
                val commentLines = comment.lines?.let { parseLineIndexes(it) }
                commentLines?.forEach { commentLine ->
                    padCodeList = padCodeList.mapIndexed { currentIndex, item ->
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
        responseSuccess = false
        viewModelScope.launch {
            try {
                commentList.forEach { comment ->
                    currentCodeReview.id?.let { currentCodeReviewId ->
                        repository.postMultilineNote(
                            codeReviewId = currentCodeReviewId,
                            PostMultilineNoteBody(
                                note = comment.comment,
                                beginLineNumber = comment.lines?.let { parseLineIndexes(it).first + 1 },
                                endLineNumber = comment.lines?.let { parseLineIndexes(it).last + 1 }
                            )
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
                currentCodeReview.id?.let { codeReviewId ->
                    repository.addNoteToCodeReview(
                        codeReviewId = codeReviewId,
                        note = reviewMessage
                    )
                    repository.closeCodeReview(codeReviewId = codeReviewId)
                }
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
        responseSuccess = false
        viewModelScope.launch {
            try {
                currentCodeReview.id?.let { codeReviewId ->
                    if (reviewMessage.isNotBlank()) {
                        repository.addNoteToCodeReview(
                            codeReviewId = codeReviewId,
                            note = reviewMessage
                        )
                    }
                }
                commentList.forEach { comment ->
                    currentCodeReview.id?.let { currentCodeReviewId ->
                        repository.postMultilineNote(
                            codeReviewId = currentCodeReviewId,
                            PostMultilineNoteBody(
                                note = comment.comment,
                                beginLineNumber = comment.lines?.let { parseLineIndexes(it).first + 1 },
                                endLineNumber = comment.lines?.let { parseLineIndexes(it).last + 1 }
                            )
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
        responseSuccess = false
        viewModelScope.launch {
            try {
                currentCodeReview.id?.let { codeReviewId ->
                    if (reviewMessage.isNotBlank()) {
                        repository.addNoteToCodeReview(
                            codeReviewId = codeReviewId,
                            note = reviewMessage
                        )
                    }
                }
                commentList.forEach { comment ->
                    currentCodeReview.id?.let { currentCodeReviewId ->
                        repository.postMultilineNote(
                            codeReviewId = currentCodeReviewId,
                            PostMultilineNoteBody(
                                note = comment.comment,
                                beginLineNumber = comment.lines?.let { parseLineIndexes(it).first + 1 },
                                endLineNumber = comment.lines?.let { parseLineIndexes(it).last + 1 }
                            )
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
                if (teamAppointment.status != TaskStatus.WaitingForGrade.status) {
                    currentCodeReview.id?.let { repository.approveCodeReview(it) }
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

    fun updateSelectedTabIndex(index: Int) {
        selectedTabIndex = index
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

    fun formatDate(isoDate: String): String {
        val dateTime = ZonedDateTime.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm")
        return dateTime.format(formatter)
    }
}