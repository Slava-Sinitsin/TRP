package com.example.trp.ui.viewmodels.student

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
    var disciplineName by mutableStateOf("")
    var solutionText by mutableStateOf("")
    var outputText by mutableStateOf("")

    init {
        viewModelScope.launch {
            task = repository.getTask(taskId)
            disciplineName = repository.taskDisciplineData.name ?: ""
            solutionText = repository.taskSolution.code ?: ""
        }
    }

    fun updateTaskText(newTaskText: String) {
        solutionText = newTaskText
    }

    fun onSaveCodeButtonClick() {
        viewModelScope.launch {
            solutionText.let { repository.postTaskSolution(it) }
        }
    }

    fun onRunCodeButtonClick() {
        // TODO
    }

    fun updateOutputText(newOutputText: String) {
        outputText = newOutputText
    }
}