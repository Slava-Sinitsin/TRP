package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AdminUsersGroupInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val groupId: Int
) : ViewModel() {
    var students by mutableStateOf(emptyList<Student>())
    var group by mutableStateOf(Group())
        private set
    var isRefreshing by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(groupId: Int): AdminUsersGroupInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideGroupInfoScreenViewModel(
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
            students = repository.getStudents(groupId)
            group =
                repository.teacherAppointments.find { it.group?.id == groupId }?.group ?: Group()
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            students = repository.getStudents(groupId)
            group =
                repository.teacherAppointments.find { it.group?.id == groupId }?.group ?: Group()
            isRefreshing = false
        }
    }

    fun getStudents(index: Int): Student {
        return students[index]
    }
}