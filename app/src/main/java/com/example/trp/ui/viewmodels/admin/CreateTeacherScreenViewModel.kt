package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.PostNewTeacherBody
import com.example.trp.data.mappers.tasks.TeacherResponse
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.Locale

class CreateTeacherScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var teacherFullName by mutableStateOf("")
        private set
    var teacherUsername by mutableStateOf("")
        private set
    var usernameCorrect by mutableStateOf(false)
        private set
    var teacherPassword by mutableStateOf("")
        private set

    var applyButtonEnabled by mutableStateOf(false)
        private set

    var positions by mutableStateOf(listOf("ROLE_LECTURER", "ROLE_ASSISTANT"))
        private set
    var selectedPosition by mutableStateOf(positions[0])
        private set
    var errorMessage by mutableStateOf("")
        private set
    var responseSuccess by mutableStateOf(false)
        private set
    private var conflictUsernameList by mutableStateOf(emptyList<String>())
    var createError by mutableStateOf(true)
        private set

    @AssistedFactory
    interface Factory {
        fun create(): CreateTeacherScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateTeacherScreenViewModel(
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
            teacherPassword = generatePassword()
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun updateTeacherFullNameValue(newTeacherFullName: String) {
        teacherFullName = newTeacherFullName
        teacherUsername = transliterate(newTeacherFullName)
        usernameCorrect = isValidUsername(teacherUsername)
        checkFields()
    }

    private fun isValidUsername(input: String): Boolean {
        val regex = Regex("[a-zA-Z0-9_]+")
        return !input.contains(" ") && regex.matches(input) && input !in conflictUsernameList
    }

    private fun generatePassword(): String { // TODO
        // val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return "rebustubus"
        /*(1..10)
        .map { chars.random() }
        .joinToString("")*/
    }

    fun updateTeacherUsernameValue(newTeacherUsername: String) {
        usernameCorrect = isValidUsername(newTeacherUsername)
        teacherUsername = newTeacherUsername
        checkFields()
    }

    fun updateTeacherPasswordValue(newTeacherPassword: String) {
        teacherPassword = newTeacherPassword
        checkFields()
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

    private fun checkFields() {
        applyButtonEnabled =
            (teacherFullName.isNotEmpty() && teacherUsername.isNotEmpty()
                    && teacherPassword.isNotEmpty() && usernameCorrect)
    }

    fun updatePositionValue(newPosition: String) {
        selectedPosition = newPosition
    }

    fun onApplyButtonClick() {
        responseSuccess = false
        viewModelScope.launch {
            try {
                when (selectedPosition) {
                    "ROLE_LECTURER" -> {
                        val response = repository.postNewLectureTeacher(
                            PostNewTeacherBody(
                                username = teacherUsername,
                                fullName = teacherFullName,
                                password = teacherPassword
                            )
                        )
                        response?.errorBody()?.let {
                            val errorBody = it.string()
                            val postGroupResponse = TeacherResponse(
                                status = JSONObject(errorBody).getInt("status"),
                                message = JSONObject(errorBody).getString("message"),
                                error = JSONObject(errorBody).getString("error")
                            )
                            if (postGroupResponse.error?.startsWith("Username is already taken") == true) {
                                usernameCorrect = false
                                applyButtonEnabled = false
                                conflictUsernameList += teacherUsername
                            }
                        } ?: run { createError = false }
                    }

                    "ROLE_ASSISTANT" -> {
                        val response = repository.postNewLabWorkTeacher(
                            PostNewTeacherBody(
                                username = teacherUsername,
                                fullName = teacherFullName,
                                password = teacherPassword
                            )
                        )
                        response?.errorBody()?.let {
                            val errorBody = it.string()
                            val postGroupResponse = TeacherResponse(
                                status = JSONObject(errorBody).getInt("status"),
                                message = JSONObject(errorBody).getString("message"),
                                error = JSONObject(errorBody).getString("error")
                            )
                            if (postGroupResponse.error?.startsWith("Username is already taken") == true) {
                                usernameCorrect = false
                                applyButtonEnabled = false
                                conflictUsernameList += teacherUsername
                            }
                        } ?: run { createError = false }
                    }
                }
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
}