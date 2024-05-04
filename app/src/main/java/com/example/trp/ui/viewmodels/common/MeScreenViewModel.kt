package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.user.User
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeScreenViewModel @Inject constructor(val repository: UserAPIRepositoryImpl) : ViewModel() {
    var user by mutableStateOf(User())
        private set
    var isLogged by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            user = repository.user
        }
    }

    fun onLogoutButtonClick() {
        viewModelScope.launch {
            repository.setUserIsLogged(false)
            isLogged = false
        }
    }
}