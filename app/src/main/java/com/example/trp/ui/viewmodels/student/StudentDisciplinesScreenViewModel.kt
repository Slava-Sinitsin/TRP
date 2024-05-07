package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class StudentDisciplinesScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var disciplines by mutableStateOf(repository.disciplines)
        private set
    var errorMessage by mutableStateOf("")
        private set
    var isRefreshing by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(): StudentDisciplinesScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideStudentDisciplinesScreenViewModel(
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
        viewModelScope.launch { init() }
    }

    private suspend fun init(update: Boolean = false) {
        try {
            disciplines = repository.getDisciplines(update = update).sortedBy { it.name }
        } catch (e: SocketTimeoutException) {
            updateErrorMessage("Timeout")
        } catch (e: ConnectException) {
            updateErrorMessage("Check internet connection")
        } catch (e: Exception) {
            updateErrorMessage("Error")
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            init(update = true)
            isRefreshing = false
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun getDiscipline(index: Int): DisciplineData {
        return disciplines[index]
    }
}