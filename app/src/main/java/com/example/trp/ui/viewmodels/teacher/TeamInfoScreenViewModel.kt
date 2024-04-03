package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.TeamAppointments
import com.example.trp.data.mappers.tasks.ShowTeam
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.TaskStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TeamInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val teamId: Int
) : ViewModel() {
    var team by mutableStateOf(Team())
        private set
    var showTeam by mutableStateOf(ShowTeam())
        private set
    var tasks by mutableStateOf(emptyList<Task>())
        private set
    private var teamAppointments by mutableStateOf(emptyList<TeamAppointments>())

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
        viewModelScope.launch { // TODO
            team = repository.teams.find { it.id == teamId } ?: Team()
            showTeam = ShowTeam(team.id, team.studentIds?.mapNotNull {
                repository.students.find { student -> student.id == it }
            })
            teamAppointments = repository.getTeamAppointments(teamId)
            tasks = repository.getTeamTasks(teamId)
        }
    }

    fun getTask(index: Int): Task {
        return tasks.find { it.id == tasks[index].id } ?: Task()
    }

    private fun getStudentAppointment(index: Int): TeamAppointments {
        return teamAppointments.find { it.taskId == tasks[index].id } ?: TeamAppointments()
    }

    fun getStatus(index: Int): Pair<Float, Color> {
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
    }
}