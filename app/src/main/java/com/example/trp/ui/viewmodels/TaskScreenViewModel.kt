package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.launch

class TaskScreenViewModel(
    taskId: Int
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    var task by mutableStateOf(repository.task)
    var taskDisciplineData by mutableStateOf(repository.taskDisciplineData)
    var taskText by mutableStateOf("")

    init {
        viewModelScope.launch {
            task = repository.getTask(taskId)
            taskDisciplineData = repository.taskDisciplineData
        }
    }

    fun updateTaskText(newTaskText: String) {
        taskText = newTaskText
    }

    fun onRunCodeButtonClick() {
        // TODO
    }
}