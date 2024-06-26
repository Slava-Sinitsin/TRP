package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.TeamAppointment
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.TaskStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class TeamInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted("teamId")
    val teamId: Int,
    @Assisted("groupId")
    val groupId: Int
) : ViewModel() {
    var team by mutableStateOf(Team())
        private set
    var teamAppointments by mutableStateOf(emptyList<TeamAppointment>())
        private set
    var labs by mutableStateOf(emptyList<Lab>())
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("teamId") teamId: Int,
            @Assisted("groupId") groupId: Int
        ): TeamInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeamInfoScreenViewModel(
            factory: Factory,
            teamId: Int,
            groupId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(teamId, groupId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch { init() }
    }

    private suspend fun init() {
        try {
            team = repository.teams.find { it.id == teamId } ?: Team()
            labs = repository.getLabs(disciplineId = repository.currentDiscipline) // TODO
                .sortedBy { it.id }
            teamAppointments = repository.getAllTeamAppointments(
                disciplineId = repository.currentDiscipline, // TODO
                groupId = groupId
            ).filter { it.team?.id == teamId }.sortedBy { it.task?.labWorkId }
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

    fun getStatus(id: Int): TaskStatus {
        return when (teamAppointments.find { it.id == id }?.status) {
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