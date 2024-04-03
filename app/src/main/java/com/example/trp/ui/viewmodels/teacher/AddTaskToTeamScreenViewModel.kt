package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.CheckBoxState
import com.example.trp.data.mappers.PostTeamAppointmentsBody
import com.example.trp.data.mappers.tasks.ShowTeam
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AddTaskToTeamScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val teamId: Int
) : ViewModel() {
    var tasks by mutableStateOf(emptyList<Task>())
        private set

    var tasksCheckBoxStates by mutableStateOf(emptyList<CheckBoxState>())
        private set

    private var tasksCheckBoxStatesBck by mutableStateOf(emptyList<CheckBoxState>())

    var team by mutableStateOf(Team())
        private set
    var showTeam by mutableStateOf(ShowTeam())
        private set

    private var teamAppointments by mutableStateOf(emptyList<Task>())

    var isTaskChanged by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            studentId: Int
        ): AddTaskToTeamScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAddTaskToTeamScreenViewModel(
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
        viewModelScope.launch { // TODO
            team = repository.teams.find { it.id == teamId } ?: Team()
            showTeam = ShowTeam(team.id, team.studentIds?.mapNotNull {
                repository.students.find { student -> student.id == it }
            })
            tasks = repository.getTasks(repository.currentDiscipline).sortedBy { it.title }
            tasksCheckBoxStates = List(tasks.size) { CheckBoxState() }
            teamAppointments = repository.getTeamTasks(teamId)
            teamAppointments.forEach { teamAppointments ->
                tasks.forEachIndexed { index, task ->
                    if (teamAppointments.id == task.id) {
                        tasksCheckBoxStates = tasksCheckBoxStates.toMutableList().also {
                            it[index] = CheckBoxState(isSelected = true, isEnable = false)
                        }
                    }
                }
            }
            tasksCheckBoxStatesBck = tasksCheckBoxStates
        }
    }

    fun getTask(index: Int): Task {
        return tasks[index]
    }

    fun onCheckBoxClick(index: Int) {
        tasksCheckBoxStates = tasksCheckBoxStates.toMutableList().also {
            it[index] = CheckBoxState(isSelected = !it[index].isSelected)
        }
        isTaskChanged = checkAllCheckBoxStates()
    }

    private fun checkAllCheckBoxStates(): Boolean {
        tasksCheckBoxStates.filter { it.isEnable }.forEach {
            if (it.isSelected) {
                return true
            }
        }
        return false
    }

    fun onRollBackIconButtonClick() {
        tasksCheckBoxStates = tasksCheckBoxStatesBck
        isTaskChanged = false
    }

    fun beforeApplyButtonClick() {
        viewModelScope.launch {
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
            isTaskChanged = false
        }
    }
}