package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TeacherTasksScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val labId: Int
) : ViewModel() {
    var isRefreshing by mutableStateOf(false)
        private set
    var tasks by mutableStateOf(emptyList<Task>())

    @AssistedFactory
    interface Factory {
        fun create(
            labId: Int
        ): TeacherTasksScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherTasksScreenViewModel(
            factory: Factory,
            labId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(labId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            tasks = repository.getTasks(labId = labId)
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            tasks = repository.getTasks(labId = labId)
            isRefreshing = false
        }
    }

    fun getTask(index: Int): Task {
        return tasks[index]
    }
}