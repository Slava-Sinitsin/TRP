package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AdminGroupsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int
) : ViewModel() {
    private var teacherAppointments by mutableStateOf(repository.teacherAppointments)
    var groups by mutableStateOf(emptyList<Group>())
        private set

    @AssistedFactory
    interface Factory {
        fun create(disciplineId: Int): AdminGroupsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAdminGroupsScreenViewModel(
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
        }
    }

    fun getGroup(index: Int): Group = groups[index]
}