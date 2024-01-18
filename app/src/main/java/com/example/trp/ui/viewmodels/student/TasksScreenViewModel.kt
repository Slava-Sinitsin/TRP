package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TasksScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int,
    @Assisted
    val onTaskClick: (id: Int) -> Unit,
) : ViewModel() {
    var tasks by mutableStateOf(repository.tasks)
        private set

    @AssistedFactory
    interface Factory {
        fun create(disciplineId: Int, onTaskClick: (id: Int) -> Unit): TasksScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTasksScreenViewModel(
            factory: Factory,
            disciplineId: Int,
            onTaskClick: (id: Int) -> Unit
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(disciplineId, onTaskClick) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            tasks = repository.getTasks(disciplineId = disciplineId)
        }
    }

    fun getTask(index: Int): com.example.trp.data.mappers.tasks.Task {
        return tasks[index]
    }

    fun navigateToTask(index: Int) {
        getTask(index = index).let { task -> task.id?.let { id -> onTaskClick(id) } }
    }
}