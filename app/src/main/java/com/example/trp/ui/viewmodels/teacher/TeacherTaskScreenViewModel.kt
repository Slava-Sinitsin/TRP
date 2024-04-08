package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.solution.CommentLine
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
    private var taskCode by mutableStateOf(
        "int add (int a, int b) {\n  printf(\"\\n\");\r\n\treturn a + b\r\n}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "int add (int a, int b)int add (int a, int b)int add (int a, int b)intint add (int a, int b)int add (int a, int b)int add (int a, int b)int" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\nint add (int a, int b)int add (int a, int b)int add (int a, int b)intint add (int a, int b)int add (int a, int b)int add (int a, int b)int" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\nint add (int a, int b)int add (int a, int b)int add (int a, int b)intint add (int a, int b)int add (int a, int b)int add (int a, int b)int" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}int add (int a, int b) {\n" +
                "  printf(\"\\n\");\n" +
                "\treturn a + b\n" +
                "}"
    )
    private var solution by mutableStateOf(Solution())
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
    var reviewMessage by mutableStateOf("")
    var maxMark by mutableStateOf(0f)
    var mark by mutableStateOf(0f)

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
            solution = repository.taskSolution.copy(code = taskCode)
            codeList = padCodeList(splitCode(solution.code ?: ""))
            maxMark = 10.toFloat() / 100f
            mark = 8.toFloat() / 100f
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

    fun rejectConfirmButtonClick() {
        showRejectDialog = false
    }

    fun rejectDismissButtonClick() {
        reviewMessage = ""
        showRejectDialog = false
    }

    fun onAcceptButtonClick() {
        showAcceptDialog = true
    }

    fun acceptConfirmButtonClick() {
        showAcceptDialog = false
    }

    fun acceptDismissButtonClick() {
        reviewMessage = ""
        mark = 80.toFloat() / 100f
        showAcceptDialog = false
    }

    fun updateReviewMessage(newReviewMessage: String) {
        reviewMessage = newReviewMessage
    }

    fun updateMark(newMark: Float) {
        mark = newMark
    }
}