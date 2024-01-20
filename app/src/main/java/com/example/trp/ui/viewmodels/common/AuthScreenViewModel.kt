package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.domain.navigation.graphs.common.Graph
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(val repository: UserAPIRepositoryImpl) : ViewModel() {
    var logValue by mutableStateOf("")
        private set
    var passValue by mutableStateOf("")
        private set
    var isLogged by mutableStateOf(false)
        private set
    var message by mutableStateOf(repository.user.message.toString())
        private set
    var messageVisibility by mutableStateOf(false)
        private set
    var passwordVisibility by mutableStateOf(false)
        private set
    var destination by mutableStateOf(Graph.STUDENT_WELCOME)
        private set

    init {
        viewModelScope.launch {
            logValue = repository.getActiveUser().login ?: "android_student"
            passValue = repository.getActiveUser().password ?: "rebustubus"
        }
    }

    fun showPassword(newPasswordVisibility: Boolean) {
        passwordVisibility = newPasswordVisibility
    }

    fun updateLogValue(newLogValue: String) {
        messageVisibility = false
        logValue = newLogValue
    }

    fun updatePassValue(newPassValue: String) {
        messageVisibility = false
        passValue = newPassValue
    }

    @Suppress("SameParameterValue")
    private fun loggedChange(newIsLogged: Boolean) {
        if (repository.user.role == "ROLE_STUDENT") {
            destination = Graph.STUDENT_WELCOME
            isLogged = newIsLogged
        } else if (repository.user.role == "ROLE_TEACHER") {
            destination = Graph.TEACHER_WELCOME
            isLogged = newIsLogged
        }
    }

    private fun messageChange(newMessage: String) {
        messageVisibility = true
        message = newMessage
    }

    fun login() {
        messageVisibility = false
        viewModelScope.launch {
            try {
                repository.login(logValue, passValue).let { user ->
                    if (user.message == "OK") {
                        repository.disciplinesChanged = true
                        repository.getDisciplines()
                        loggedChange(true)
                    } else {
                        user.message?.let { messageChange(it) }
                    }
                }
            } catch (e: SocketTimeoutException) {
                messageChange("Timeout")
            } catch (e: ConnectException) {
                messageChange("Check internet connection")
            }
        }
    }
}