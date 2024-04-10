package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TeacherHomeScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var isRefreshing by mutableStateOf(false)
        private set
    var events by mutableStateOf(listOf(1, 2))
        private set

    @AssistedFactory
    interface Factory {
        fun create(): TeacherHomeScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherHomeScreenViewModel(
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

        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true

            isRefreshing = false
        }
    }

    fun getEvent(index:Int): Int {
        return events[index]
    }
}