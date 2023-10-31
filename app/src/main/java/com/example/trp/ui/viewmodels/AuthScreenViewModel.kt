package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.datamanagers.DisciplinesDataManager
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.data.disciplines.Disciplines
import com.example.trp.data.network.ApiService
import com.example.trp.data.user.AuthRequest
import com.example.trp.data.user.User
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
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
    var message by mutableStateOf("")
        private set
    var messageVisibility by mutableStateOf(false)
        private set
    var passwordVisibility by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            UserDataManager.getUser().collect {
                logValue = it.login ?: "android_student"
                passValue = it.password ?: "rebustubus"
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
                val response: Response<User> = repository.login(AuthRequest(logValue, passValue))
                handleLoginResponse(response)
            } catch (e: SocketTimeoutException) {
                messageChange("Timeout")
            } catch (e: ConnectException) {
                messageChange("Check internet connection")
            }
        }
    }

    private suspend fun handleLoginResponse(response: Response<User>) {
        response.body()?.let { user ->
            if (user.message == "OK") {
                val updatedUser = user.copy(login = logValue, password = passValue)
                UserDataManager.updateUser(updatedUser)
                getDisciplines()
                loggedChange(true)
            }
        } ?: run {
            response.errorBody()?.let { errorBody ->
                val message = JSONObject(errorBody.string()).getString("error")
                if (message.isNotEmpty()) {
                    messageChange(message)
                }
            } ?: run {
                messageChange("Bad response")
            }
        }
    }

    private fun getDisciplines() {
        viewModelScope.launch {
            val user = UserDataManager.getUser().first()
            val response: Response<Disciplines> =
                ApiService.userAPI.getDisciplines("Bearer " + user.token)
            response.body()?.let { Disciplines(it.list) }?.let {
                DisciplinesDataManager.saveDisciplines(it)
            }
        }
    }
}