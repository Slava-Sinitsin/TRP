package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.GroupsLabsTabs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class TeacherGroupsLabsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int
) : ViewModel() {
    private var teacherAppointments by mutableStateOf(repository.teacherAppointments)
    var groups by mutableStateOf(emptyList<Group>())
        private set
    var labs by mutableStateOf(emptyList<Lab>())
        private set

    var teams by mutableStateOf(emptyList<Team>())
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    val groupsTasksScreens = listOf(
        GroupsLabsTabs.Groups,
        GroupsLabsTabs.Labs
    )

    var selectedTabIndex by mutableStateOf(0)
    var errorMessage by mutableStateOf("")
        private set

    @AssistedFactory
    interface Factory {
        fun create(disciplineId: Int): TeacherGroupsLabsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherGroupsLabsScreenViewModel(
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
            teacherAppointments =
                repository.getTeacherAppointments().filter { it.discipline?.id == disciplineId }
            groups = teacherAppointments.map { it.group ?: Group() }.sortedBy { it.name }
            labs = repository.getLabs(disciplineId = disciplineId).sortedBy { it.title }
            repository.setCurrentDisciplineId(disciplineId)
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
            try {
                isRefreshing = true
                init()
                isRefreshing = false
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

    fun getGroup(index: Int): Group {
        return groups[index]
    }

    fun getLab(index: Int): Lab {
        return labs[index]
    }
}