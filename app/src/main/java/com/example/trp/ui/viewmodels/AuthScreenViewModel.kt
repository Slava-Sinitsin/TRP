package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class AuthScreenViewModel : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

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

    init {
        viewModelScope.launch {
            logValue = repository.user.login ?: "android_student"
            passValue = repository.user.password ?: "rebustubus"
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
        isLogged = newIsLogged
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
                        repository.addUserInformation()
                        repository.disciplinesChanged = true
                        repository.getDisciplines()
                        loggedChange(true)
                    } else {
                        messageChange(user.message!!)
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