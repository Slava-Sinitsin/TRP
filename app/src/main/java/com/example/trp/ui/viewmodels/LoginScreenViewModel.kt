package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.AuthRequest
import com.example.trp.data.User
import com.example.trp.data.UserDataManager
import com.example.trp.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class LoginScreenViewModel : ViewModel() {
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
        logValue = newLogValue
        messageVisibility = false
    }

    fun updatePassValue(newPassValue: String) {
        passValue = newPassValue
        messageVisibility = false
    }

    fun loggedChange(newIsLogged: Boolean) {
        isLogged = newIsLogged
    }

    fun messageChange(newMessage: String) {
        message = newMessage
        messageVisibility = true
    }

    fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<User> = ApiService.userAPI.auth(
                AuthRequest(
                    logValue,
                    passValue
                )
            )
            response.body()?.message?.let { message ->
                if (message == "OK") {
                    val user = User(
                        login = logValue,
                        password = passValue,
                        token = response.body()?.token,
                        message = response.body()?.message
                    )
                    UserDataManager.saveUser(user)
                    loggedChange(true)
                }
            }
            response.errorBody()?.string()?.let { errorBody ->
                val message = JSONObject(errorBody).getString("error")
                if (message.isNotEmpty()) {
                    messageChange(message)
                }
            }
        }
    }
}