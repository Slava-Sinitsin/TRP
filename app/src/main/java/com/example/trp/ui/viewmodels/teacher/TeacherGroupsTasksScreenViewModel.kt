package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.GroupsTasksTabs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TeacherGroupsTasksScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int
) : ViewModel() {
    private var teacherAppointments by mutableStateOf(repository.teacherAppointments)
    var groups by mutableStateOf(emptyList<Group>())
        private set
    var tasks by mutableStateOf(emptyList<Task>())
        private set

    var teams by mutableStateOf(emptyList<Team>())
        private set

    val groupsTasksScreens = listOf(
        GroupsTasksTabs.Groups,
        GroupsTasksTabs.Tasks
    )

    var selectedTabIndex by mutableStateOf(0)

    @AssistedFactory
    interface Factory {
        fun create(disciplineId: Int): TeacherGroupsTasksScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherGroupsTasksScreenViewModel(
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
            teacherAppointments =
                repository.getTeacherAppointments().filter { it.discipline?.id == disciplineId }
            groups = teacherAppointments.map { it.group ?: Group() }.sortedBy { it.name }
            tasks = repository.getTasks(disciplineId = disciplineId).sortedBy { it.title }
            teams = repository.getTeams(disciplineId)
        }
    }

    fun getGroup(index: Int): Group {
        return groups[index]
    }

    fun getTask(index: Int): Task {
        return tasks[index]
    }
}