package com.example.trp.ui.viewmodels.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.launch

class TasksScreenViewModel(
    val disciplineId: Int,
    var onTaskClick: (id: Int) -> Unit
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    var tasks by mutableStateOf(repository.tasks)
        private set

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