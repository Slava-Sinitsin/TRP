package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.PostNewStudentBody
import com.example.trp.data.mappers.teacherappointments.PostGroupResponse
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale

class CreateStudentScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val groupId: Int
) : ViewModel() {
    var fullName by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set
    var usernameCorrect by mutableStateOf(false)
        private set
    var password by mutableStateOf("")
        private set
    var studentApplyButtonEnabled by mutableStateOf(false)
        private set
    private var conflictUsernameList by mutableStateOf(emptyList<String>())
    var createError by mutableStateOf(true)
        private set

    @AssistedFactory
    interface Factory {
        fun create(groupId: Int): CreateStudentScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateStudentScreenViewModel(
            factory: Factory,
            groupId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(groupId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            fullName = ""
            username = ""
            password = generatePassword()
            checkStudentFields()
        }
    }

    fun updateStudentFullNameValue(newStudentFullName: String) {
        fullName = newStudentFullName
        val newStudentUsername = transliterate(newStudentFullName)
        usernameCorrect = isValidUsername(newStudentUsername)
        username = newStudentUsername
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
        username = newStudentUsername
        checkStudentFields()
    }

    private fun isValidUsername(input: String): Boolean {
        val regex = Regex("[a-zA-Z0-9_]+")
        return !input.contains(" ")
                && regex.matches(input)
                && input !in conflictUsernameList
    }

    fun updateStudentPasswordValue(newStudentPassword: String) {
        password = newStudentPassword
        checkStudentFields()
    }

    private fun generatePassword(): String { // TODO
        // val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return "rebustubus"
        /*(1..10)
        .map { chars.random() }
        .joinToString("")*/
    }

    private fun checkStudentFields() {
        studentApplyButtonEnabled =
            (fullName.isNotEmpty()
                    && username.isNotEmpty()
                    && password.isNotEmpty()
                    && usernameCorrect)
    }

    fun onApplyCreateStudentClick() {
        viewModelScope.launch {
            val response = repository.postNewStudent(
                PostNewStudentBody(
                    username = username,
                    fullName = fullName,
                    password = password,
                    groupId = groupId
                )
            )
            response?.errorBody()?.let {
                val errorBody = it.string()
                val postGroupResponse = PostGroupResponse(
                    status = JSONObject(errorBody).getInt("status"),
                    message = JSONObject(errorBody).getString("message"),
                    error = JSONObject(errorBody).getString("error")
                )
                if (postGroupResponse.error?.startsWith("Username is already taken") == true) {
                    usernameCorrect = false
                    studentApplyButtonEnabled = false
                    conflictUsernameList += username
                }
            } ?: run { createError = false }
        }
    }
}