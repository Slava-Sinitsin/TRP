package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.CheckBoxState
import com.example.trp.data.mappers.PostTeamAppointmentsBody
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class AddTaskToTeamScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted("teamId")
    val teamId: Int,
    @Assisted("labId")
    val labId: Int
) : ViewModel() {
    var tasks by mutableStateOf(emptyList<Task>())
        private set
    var tasksCheckBoxStates by mutableStateOf(emptyList<CheckBoxState>())
        private set
    private var tasksCheckBoxStatesBck by mutableStateOf(emptyList<CheckBoxState>())
    var errorMessage by mutableStateOf("")
        private set
    var responseSuccess by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("teamId")
            teamId: Int,
            @Assisted("labId")
            labId: Int
        ): AddTaskToTeamScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAddTaskToTeamScreenViewModel(
            factory: Factory,
            teamId: Int,
            labId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(teamId, labId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            try {
                tasks = repository.getTasks(labId = labId).sortedBy { it.title }
                tasksCheckBoxStates = List(tasks.size) { CheckBoxState() }
                tasksCheckBoxStatesBck = tasksCheckBoxStates
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

    fun getTask(index: Int): Task {
        return tasks[index]
    }

    fun onCheckBoxClick(index: Int) {
        tasksCheckBoxStates = tasksCheckBoxStates.mapIndexed { currentIndex, item ->
            if (index == currentIndex) {
                item.copy(isSelected = !item.isSelected, isEnable = true)
            } else {
                item.copy(isSelected = false, isEnable = !item.isEnable)
            }
        }
    }

    fun beforeApplyButtonClick() {
        responseSuccess = false
        viewModelScope.launch {
            try {
                tasksCheckBoxStates.forEachIndexed { index, it ->
                    if (it.isSelected && it.isEnable) {
                        repository.postTeamAppointments(
                            PostTeamAppointmentsBody(
                                teamId = teamId,
                                taskId = tasks[index].id
                            )
                        )
                    }
                }
                responseSuccess = true
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }
}