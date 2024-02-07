package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.screens.teacher.tabs.GroupsTasksTabs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AdminGroupsTasksScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int,
    @Assisted("onGroupClick")
    val onGroupClick: (groupId: Int) -> Unit,
    @Assisted("onTaskClick")
    val onTaskClick: (taskId: Int) -> Unit,
    @Assisted("onAddTaskClick")
    val onAddTaskClick: (disciplineId: Int) -> Unit
) : ViewModel() {
    var teacherAppointments by mutableStateOf(repository.teacherAppointments)
        private set
    var tasks by mutableStateOf(repository.tasks)
        private set

    val groupsTasksScreens = listOf(
        GroupsTasksTabs.Groups,
        GroupsTasksTabs.Tasks
    )

    var selectedTabIndex by mutableStateOf(0)

    @AssistedFactory
    interface Factory {
        fun create(
            disciplineId: Int,
            @Assisted("onGroupClick")
            onGroupClick: (id: Int) -> Unit,
            @Assisted("onTaskClick")
            onTaskClick: (id: Int) -> Unit,
            @Assisted("onAddTaskClick")
            onAddTaskClick: (id: Int) -> Unit
        ): AdminGroupsTasksScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAdminGroupsTasksScreenViewModel(
            factory: Factory,
            disciplineId: Int,
            onGroupClick: (groupId: Int) -> Unit,
            onTaskClick: (taskId: Int) -> Unit,
            onAddTaskClick: (disciplineId: Int) -> Unit
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(
                        disciplineId,
                        onGroupClick,
                        onTaskClick,
                        onAddTaskClick
                    ) as T
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

    fun getGroup(index: Int): Group {
        return teacherAppointments[index].group
            ?: Group()
    }

    fun getTask(index: Int): Task {
        return tasks[index]
    }

    fun navigateToStudents(index: Int) {
        getGroup(index = index).let { task -> task.id?.let { groupId -> onGroupClick(groupId) } }
    }

    fun navigateToTask(index: Int) {
        getTask(index = index).let { task -> task.id?.let { taskId -> onTaskClick(taskId) } }
    }

    fun onAddTaskButtonClick() {
        onAddTaskClick(disciplineId)
    }
}