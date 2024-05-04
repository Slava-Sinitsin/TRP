package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.domain.navigation.common.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var destination by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            val user = repository.getActiveUser()
            if (user.isLogged == true) {
                user.login?.let { login ->
                    user.password?.let { password ->
                        repository.login(
                            login = login,
                            password = password
                        )
                    }
                }
                when (user.role) {
                    "ROLE_STUDENT" -> destination = Graph.STUDENT_WELCOME
                    "ROLE_LECTURE_TEACHER" -> destination = Graph.TEACHER_WELCOME
                    "ROLE_ADMIN" -> destination = Graph.ADMIN_WELCOME
                }
            } else {
                destination = Graph.LOGIN
            }
        }
    }
}