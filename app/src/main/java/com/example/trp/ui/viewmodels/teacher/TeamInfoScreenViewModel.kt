package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class TeamInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val teamId: Int
) : ViewModel() {
    var team by mutableStateOf(Team())
        private set
    var labs by mutableStateOf(emptyList<Lab>())
        private set
    var tasks by mutableStateOf(emptyList<Task>())
        private set
    var isRefreshing by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    @AssistedFactory
    interface Factory {
        fun create(studentId: Int): TeamInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeamInfoScreenViewModel(
            factory: Factory,
            studentId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(studentId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch { init() }
    } // TODO

    private suspend fun init() {
        try {
            team = repository.teams.find { it.id == teamId } ?: Team()
            labs = repository.getLabs(disciplineId = repository.currentDiscipline) // TODO
            tasks = repository.getTeamTasks(teamId).sortedBy { it.labWorkId }
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

    fun getTask(index: Int): Task {
        return tasks.find { it.id == tasks[index].id } ?: Task()
    }

    /*    fun getStatus(index: Int): Pair<Float, Color> {
            return when (getStudentAppointment(index).status) {
                TaskStatus.New.status -> Pair(
                    TaskStatus.New.float,
                    TaskStatus.New.color
                )

                TaskStatus.InProgress.status -> Pair(
                    TaskStatus.InProgress.float,
                    TaskStatus.InProgress.color
                )

                TaskStatus.Complete.status -> Pair(
                    TaskStatus.Complete.float,
                    TaskStatus.Complete.color
                )

                else -> Pair(0f, Color.Transparent)
            }
        }*/
}