package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.screens.admin.AdminBottomBarScreen
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AdminWelcomeScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    private var user by mutableStateOf(repository.user)

    @AssistedFactory
    interface Factory {
        fun create(): AdminWelcomeScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAdminWelcomeScreenViewModel(
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
            user = repository.user
        }
    }

    val screens = listOf(
        AdminBottomBarScreen.Curriculum,
        AdminBottomBarScreen.Home,
        AdminBottomBarScreen.Me
    )
}