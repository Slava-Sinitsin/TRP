package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TaskInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var task by mutableStateOf(repository.task)
    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var taskFunctionName by mutableStateOf("")
    var taskLanguage by mutableStateOf("")
    var readOnlyMode by mutableStateOf(true)
    var applyButtonEnabled by mutableStateOf(true)

    @AssistedFactory
    interface Factory {
        fun create(studentId: Int): TaskInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTaskInfoScreenViewModel(
            factory: Factory,
            studentId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(studentId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            task = repository.getTaskProperties(taskId)
            taskTitle = task.title ?: ""
            taskDescription = task.description ?: ""
            taskFunctionName = task.functionName ?: ""
            taskLanguage = task.language ?: ""
        }
    }

    fun updateTitleValue(newTitleValue: String) {
        applyButtonEnabled = newTitleValue.isNotEmpty()
        taskTitle = newTitleValue
    }

    fun updateDescriptionValue(newDescriptionValue: String) {
        applyButtonEnabled = newDescriptionValue.isNotEmpty()
        taskDescription = newDescriptionValue
    }

    fun updateFunctionNameValue(newFunctionNameValue: String) {
        applyButtonEnabled = newFunctionNameValue.isNotEmpty()
        taskFunctionName = newFunctionNameValue
    }

    fun updateLanguageValue(newLanguageValue: String) {
        applyButtonEnabled = newLanguageValue.isNotEmpty()
        taskLanguage = newLanguageValue
    }

    fun onEditButtonClick() {
        readOnlyMode = false
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            if (taskTitle != "" && taskDescription != "" && taskFunctionName != "" && taskLanguage != "") {
                repository.putTask(
                    Task(
                        id = task.id,
                        disciplineId = task.disciplineId,
                        title = taskTitle,
                        description = taskDescription,
                        functionName = taskFunctionName,
                        language = taskLanguage
                    )
                )
                task = repository.getTask(taskId)
                readOnlyMode = true
            }
        }
    }

    fun onRollBackIconClick() {
        taskTitle = task.title ?: ""
        taskDescription = task.description ?: ""
        taskFunctionName = task.functionName ?: ""
        taskLanguage = task.language ?: ""
        readOnlyMode = true
    }
}