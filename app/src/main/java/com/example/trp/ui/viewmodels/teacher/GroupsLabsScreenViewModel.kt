package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import com.example.trp.ui.screens.teacher.tabs.GroupsLabsTabs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class GroupsLabsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int,
    @Assisted("onGroupClick")
    val onGroupClick: (id: Int) -> Unit,
    @Assisted("onTaskClick")
    val onTaskClick: (id: Int) -> Unit
) : ViewModel() {
    var teacherAppointments by mutableStateOf(repository.teacherAppointments)
        private set
    var tasks by mutableStateOf(repository.tasks)
        private set

    val groupsLabsScreens = listOf(
        GroupsLabsTabs.Groups,
        GroupsLabsTabs.Labs
    )

    var selectedTabIndex by mutableStateOf(0)

    @AssistedFactory
    interface Factory {
        fun create(
            disciplineId: Int,
            @Assisted("onGroupClick")
            onGroupClick: (id: Int) -> Unit,
            @Assisted("onTaskClick")
            onTaskClick: (id: Int) -> Unit
        ): GroupsLabsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideGroupsScreenViewModel(
            factory: Factory,
            disciplineId: Int,
            onGroupClick: (id: Int) -> Unit,
            onTaskClick: (id: Int) -> Unit
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(disciplineId, onGroupClick, onTaskClick) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            teacherAppointments =
                repository.getTeacherAppointments().filter { it.discipline?.id == disciplineId }
            tasks = repository.getTasks(disciplineId = disciplineId)
        }
    }

    fun getGroup(index: Int): com.example.trp.data.mappers.teacherappointments.Group {
        return teacherAppointments[index].group
            ?: com.example.trp.data.mappers.teacherappointments.Group()
    }

    fun getTask(index: Int): Task {
        return tasks[index]
    }

    fun navigateToStudents(index: Int) {
        getGroup(index = index).let { task -> task.id?.let { id -> onGroupClick(id) } }
    }

    fun navigateToTask(index: Int) {
        getTask(index = index).let { task -> task.id?.let { id -> onTaskClick(id) } }
    }
}