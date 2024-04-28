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
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

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

    var isRefreshing by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
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
        viewModelScope.launch { init() }
    }

    private suspend fun init() {
        try {
            groups = repository.getGroups().sortedBy { it.name }
            teachers = repository.getTeachers().sortedBy { it.fullName }
        } catch (e: SocketTimeoutException) {
            updateErrorMessage("Timeout")
        } catch (e: ConnectException) {
            updateErrorMessage("Check internet connection")
        } catch (e: Exception) {
            updateErrorMessage("Error")
        }

    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            init()
            isRefreshing = false
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
}