package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.mappers.teacherappointments.Teacher
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.UsersTabs
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GroupsTeachersScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var selectedTabIndex by mutableStateOf(0)
        private set
    val usersScreens = mutableListOf(
        UsersTabs.GroupsScreen,
        UsersTabs.TeachersScreen
    )
    var groups by mutableStateOf(emptyList<Group>())
        private set
    var teachers by mutableStateOf(emptyList<Teacher>())
        private set

    var groupsIsRefreshing by mutableStateOf(false)
        private set
    var teachersIsRefreshing by mutableStateOf(false)
        private set


    @AssistedFactory
    interface Factory {
        fun create(): GroupsTeachersScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideGroupsTeachersScreenViewModel(
            factory: Factory
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create() as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            groups = repository.getGroups().sortedBy { it.name }
            teachers = repository.getTeachers().sortedBy { it.fullName }
        }
    }

    fun setPagerState(currentPage: Int) {
        selectedTabIndex = currentPage
    }

    fun getGroup(index: Int): Group {
        return groups[index]
    }

    fun getTeacher(index: Int): Teacher {
        return teachers[index]
    }

    fun onRefreshGroups() { // TODO
        viewModelScope.launch {
            groupsIsRefreshing = true
            delay(1000)
            groupsIsRefreshing = false
        }
    }

    fun onRefreshTeachers() { // TODO
        viewModelScope.launch {
            teachersIsRefreshing = true
            delay(1000)
            teachersIsRefreshing = false
        }
    }
}