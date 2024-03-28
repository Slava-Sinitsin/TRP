package com.example.trp.ui.viewmodels.teacher

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.ShowTeam
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.tasks.Team
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TeamsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val groupId: Int
) : ViewModel() {
    var students by mutableStateOf(emptyList<Student>())
        private set

    private var teams by mutableStateOf(emptyList<Team>())
    var showTeams by mutableStateOf(emptyList<ShowTeam>())
        private set

    var group by mutableStateOf(Group())
        private set

    var isMenuShow by mutableStateOf(false)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    var disciplineId by mutableStateOf(repository.currentDiscipline)

    @AssistedFactory
    interface Factory {
        fun create(
            groupId: Int
        ): TeamsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeamsScreenViewModel(
            factory: Factory,
            groupId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(groupId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            students = repository.getStudents(groupId = groupId).sortedBy { it.fullName }
            group =
                repository.teacherAppointments.find { it.group?.id == groupId }?.group ?: Group()
            teams = repository.getTeams(disciplineId)
            showTeams = teams.mapNotNull { team ->
                val teamStudents = students.filter { it.id in team.studentIds.orEmpty() }
                ShowTeam(id = team.id, students = teamStudents).takeIf { teamStudents.isNotEmpty() }
            }
        }
    }

    fun getTeam(index: Int): ShowTeam {
        return showTeams[index]
    }

    fun onMenuButtonClick() {
        isMenuShow = !isMenuShow
    }

    fun beforeCreateTeamClick() {
        isMenuShow = false
    }

    fun onDismissRequest() {
        isMenuShow = false
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            students = repository.getStudents(groupId = groupId).sortedBy { it.fullName }
            group =
                repository.teacherAppointments.find { it.group?.id == groupId }?.group ?: Group()
            teams = repository.getTeams(disciplineId)
            showTeams = teams.mapNotNull { team ->
                val teamStudents = students.filter { it.id in team.studentIds.orEmpty() }
                ShowTeam(id = team.id, students = teamStudents).takeIf { teamStudents.isNotEmpty() }
            }
            isRefreshing = false
        }
    }

    fun onEveryoneAppointButtonClick() {
        // TODO
    }
}