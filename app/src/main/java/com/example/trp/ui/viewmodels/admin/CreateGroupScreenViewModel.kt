package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.PostNewStudentBody
import com.example.trp.data.mappers.teacherappointments.PostNewGroupBody
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.CreateGroupTabs
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.util.Locale

class CreateGroupScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var groupName by mutableStateOf("")
        private set
    var selectedTabIndex by mutableStateOf(0)
        private set
    val createGroupScreens = mutableListOf(
        CreateGroupTabs.MainScreen,
        CreateGroupTabs.CreateStudentScreen
    )
    var topBarText by mutableStateOf("Create new group")
        private set

    var studentFullName by mutableStateOf("")
        private set
    var studentUsername by mutableStateOf("")
        private set
    var usernameCorrect by mutableStateOf(false)
        private set
    var studentPassword by mutableStateOf("")
        private set
    var groupApplyButtonEnabled by mutableStateOf(false)
        private set
    var studentApplyButtonEnabled by mutableStateOf(false)
        private set
    var students by mutableStateOf(emptyList<PostNewStudentBody>())
        private set
    var studentEditMode by mutableStateOf(false)
        private set
    private var studentEditIndex by mutableStateOf(0)

    @AssistedFactory
    interface Factory {
        fun create(): CreateGroupScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateGroupScreenViewModel(
            factory: Factory
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create() as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {

        }
    }

    fun updateGroupNameValue(newGroupName: String) {
        groupName = newGroupName
        checkGroupFields()
    }

    fun createNewStudent() {
        studentFullName = ""
        studentUsername = ""
        studentPassword = generatePassword()
        checkStudentFields()
        setPagerState(1)
    }

    fun setPagerState(index: Int) {
        selectedTabIndex = index
        when (selectedTabIndex) {
            0 -> {
                topBarText = "Create new group"
            }

            1 -> {
                topBarText = if (studentEditMode) {
                    "Edit student"
                } else {
                    "Create new student"
                }
            }
        }
    }

    fun updateStudentFullNameValue(newStudentFullName: String) {
        studentFullName = newStudentFullName
        val newStudentUsername = transliterate(newStudentFullName)
        usernameCorrect = isValidUsername(newStudentUsername)
        studentUsername = newStudentUsername
        checkDuplicatesUsername()
        checkStudentFields()
    }

    private fun transliterate(input: String): String {
        val cyrillic = arrayOf(
            "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н",
            "о", "п", "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь",
            "э", "ю", "я", "А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К",
            "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ",
            "Ъ", "Ы", "Ь", "Э", "Ю", "Я", " ", "-"
        )
        val latin = arrayOf(
            "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n",
            "o", "p", "r", "s", "t", "u", "f", "kh", "ts", "ch", "sh", "sch", "'", "y",
            "'", "e", "yu", "ya", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y",
            "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "kh", "ts", "ch", "sh",
            "sch", "'", "y", "'", "e", "yu", "ya", "_", "_"
        )
        val result = StringBuilder()
        input.lowercase(Locale.getDefault()).forEach { char ->
            val index = cyrillic.indexOf(char.toString())
            result.append(if (index != -1) latin[index] else char)
        }
        return result.toString()
    }

    fun updateStudentUsernameValue(newStudentUsername: String) {
        usernameCorrect = isValidUsername(newStudentUsername)
        studentUsername = newStudentUsername
        checkDuplicatesUsername()
        checkStudentFields()
    }

    private fun isValidUsername(input: String): Boolean {
        val regex = Regex("[a-zA-Z0-9_]+")
        return !input.contains(" ") && regex.matches(input)
    }

    fun updateStudentPasswordValue(newStudentPassword: String) {
        studentPassword = newStudentPassword
        checkStudentFields()
    }

    private fun generatePassword(): String { // TODO
        // val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return "rebustubus"
        /*(1..10)
        .map { chars.random() }
        .joinToString("")*/
    }

    private fun checkGroupFields() {
        groupApplyButtonEnabled = (groupName.isNotEmpty())
    }

    private fun checkStudentFields() {
        studentApplyButtonEnabled =
            (studentFullName.isNotEmpty()
                    && studentUsername.isNotEmpty()
                    && studentPassword.isNotEmpty()
                    && usernameCorrect)
    }

    private fun checkDuplicatesUsername() {
        val usernames = students.map { it.username }
        if (usernames.count { it == studentUsername } > 0) {
            usernameCorrect = false
        }
    }

    fun onApplyCreateStudentClick() {
        students += PostNewStudentBody(
            fullName = studentFullName,
            username = studentUsername,
            password = studentPassword
        )
        setPagerState(0)
    }

    fun onDeleteStudentClick(index: Int) {
        students = students.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }
    }

    fun onEditStudentButtonClick(index: Int) {
        studentEditMode = true
        studentEditIndex = index
        studentFullName = students[studentEditIndex].fullName ?: ""
        studentUsername = students[studentEditIndex].username ?: ""
        studentPassword = students[studentEditIndex].password ?: ""
        checkStudentFields()
        setPagerState(1)
    }

    fun onApplyEditStudentClick() {
        students = students.toMutableList().also {
            it[studentEditIndex] = PostNewStudentBody(
                fullName = studentFullName,
                username = studentUsername,
                password = studentPassword
            )
        }
        studentEditMode = false
        setPagerState(0)
    }

    fun onEditStudentBackButtonClick() {
        studentEditMode = false
        setPagerState(0)
    }

    fun onApplyCreateGroupClick() { // TODO
        viewModelScope.launch {
            repository.postNewGroup(
                PostNewGroupBody(
                    name = groupName,
                    students = students
                )
            )
        }
    }
}