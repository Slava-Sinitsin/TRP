package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.TeamAppointment
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.mappers.user.User
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.TaskStatus
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
    var user by mutableStateOf(User())
        private set
    var teamAppointments by mutableStateOf(emptyList<TeamAppointment>())
        private set
    var errorMessage by mutableStateOf("")
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var labs by mutableStateOf(emptyList<Lab>())
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
        viewModelScope.launch { init() }
    }

    private suspend fun init() {
        try {
            user = repository.user
            labs = repository.getLabs(disciplineId = disciplineId).sortedBy { it.id }
            teamAppointments = repository.getTeamAppointments(disciplineId = disciplineId)
                .sortedBy { it.task?.labWorkId }
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
            init()
            isRefreshing = false
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun getTask(index: Int): TeamAppointment {
        return teamAppointments[index]
    }

    fun getStatus(index: Int): TaskStatus {
        return when (teamAppointments[index].status) {
            TaskStatus.New.status -> TaskStatus.New
            TaskStatus.InProgress.status -> TaskStatus.InProgress
            TaskStatus.OnTesting.status -> TaskStatus.OnTesting
            TaskStatus.Tested.status -> TaskStatus.Tested
            TaskStatus.SentToCodeReview.status -> TaskStatus.SentToCodeReview
            TaskStatus.CodeReview.status -> TaskStatus.CodeReview
            TaskStatus.SentToRework.status -> TaskStatus.SentToRework
            TaskStatus.WaitingForGrade.status -> TaskStatus.WaitingForGrade
            TaskStatus.Rated.status -> TaskStatus.Rated
            else -> TaskStatus.New
        }
    }
}