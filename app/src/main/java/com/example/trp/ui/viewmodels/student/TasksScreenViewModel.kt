package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.TeamAppointments
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class TasksScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int
) : ViewModel() {
    var teamAppointments by mutableStateOf(emptyList<TeamAppointments>())
        private set
    var errorMessage by mutableStateOf("")
        private set

    @AssistedFactory
    interface Factory {
        fun create(disciplineId: Int): TasksScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTasksScreenViewModel(
            factory: Factory,
            disciplineId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(disciplineId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                teamAppointments = repository.getTeamAppointments(disciplineId = disciplineId)
                    .sortedBy { it.task?.title }
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun getTask(index: Int): TeamAppointments {
        return teamAppointments[index]
    }
}