package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.user.User
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.domain.navigation.common.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(val repository: UserAPIRepositoryImpl) : ViewModel() {
    var user by mutableStateOf(User())
        private set
    var logValue by mutableStateOf("")
        private set
    var passValue by mutableStateOf("")
        private set
    var message by mutableStateOf("")
        private set
    var messageVisibility by mutableStateOf(false)
        private set
    var passwordVisibility by mutableStateOf(false)
        private set
    var destination by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            try {
                user = repository.getActiveUser()
                logValue = user.login ?: ""
                passValue = ""
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
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

    private fun loggedChange() {
        when (user.role) {
            "ROLE_STUDENT" -> destination = Graph.STUDENT_WELCOME
            "ROLE_LECTURE_TEACHER" -> destination = Graph.TEACHER_WELCOME
            "ROLE_LAB_WORK_TEACHER" -> destination = Graph.TEACHER_WELCOME
            "ROLE_ADMIN" -> destination = Graph.ADMIN_WELCOME
        }
    }

    private fun updateErrorMessage(newMessage: String) {
        messageVisibility = true
        message = newMessage
    }

    fun onLoginButtonClick() {
        messageVisibility = false
        viewModelScope.launch {
            try {
                user = repository.login(logValue, passValue)
                user.let { user ->
                    if (user.message == "OK") {
                        repository.getDisciplines(update = true)
                        loggedChange()
                    } else {
                        user.message?.let { updateErrorMessage(it) }
                    }
                }
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