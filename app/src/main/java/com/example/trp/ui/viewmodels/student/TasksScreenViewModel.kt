package com.example.trp.ui.viewmodels.student

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

class TasksScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val labId: Int
) : ViewModel() {
    var tasks by mutableStateOf(repository.tasks)
        private set

    @AssistedFactory
    interface Factory {
        fun create(disciplineId: Int): TasksScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTasksScreenViewModel(
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
            tasks = repository.getTasks(labId = labId).sortedBy { it.title }
        }
    }

    fun getTask(index: Int): Task {
        return tasks[index]
    }
}