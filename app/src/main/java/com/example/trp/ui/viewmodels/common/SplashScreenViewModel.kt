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
class SplashScreenViewModel @Inject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var destination by mutableStateOf("")
        private set
    private var user by mutableStateOf(User())

    init {
        viewModelScope.launch {
            try {
                user = repository.getActiveUser().copy(role = null)
                if (user.isLogged == true) {
                    user = user.login?.let { login ->
                        user.password?.let { password ->
                            repository.login(
                                login = login,
                                password = password
                            )
                        }
                    } ?: User()
                    repository.getDisciplines(update = true)
                }
            } catch (_: SocketTimeoutException) {
            } catch (_: ConnectException) {
            } catch (_: Exception) {
            }
            destination = when (user.role) {
                "ROLE_STUDENT" -> Graph.STUDENT_WELCOME
                "ROLE_LECTURE_TEACHER" -> Graph.TEACHER_WELCOME
                "ROLE_LAB_WORK_TEACHER" -> Graph.TEACHER_WELCOME
                "ROLE_ADMIN" -> Graph.ADMIN_WELCOME
                else -> Graph.LOGIN
            }
        }
    }
}