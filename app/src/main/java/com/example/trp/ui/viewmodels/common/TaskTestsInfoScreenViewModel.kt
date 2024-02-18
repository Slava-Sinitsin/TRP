package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TaskTestsInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int,
    @Assisted
    val navController: NavHostController
) : ViewModel() {
    var task by mutableStateOf(repository.task)
    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var taskFunctionName by mutableStateOf("")
    var taskLanguage by mutableStateOf("")
    var readOnlyMode by mutableStateOf(true)
    var readOnlyAlpha by mutableStateOf(0.6f)
    var applyButtonEnabled by mutableStateOf(true)
    var showDeleteDialog by mutableStateOf(false)

    @AssistedFactory
    interface Factory {
        fun create(studentId: Int, navController: NavHostController): TaskTestsInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTaskInfoScreenViewModel(
            factory: Factory,
            studentId: Int,
            navController: NavHostController
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(studentId, navController) as T
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

    fun onBackIconButtonClick() {
        navController.popBackStack()
    }

    fun onEditButtonClick() {
        readOnlyMode = false
        readOnlyAlpha = 1f
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
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
            readOnlyAlpha = 0.6f
        }
    }

    fun onRollBackIconButtonClick() {
        taskTitle = task.title ?: ""
        taskDescription = task.description ?: ""
        taskFunctionName = task.functionName ?: ""
        taskLanguage = task.language ?: ""
        readOnlyMode = true
        readOnlyAlpha = 0.6f
    }

    fun onDeleteButtonClick() {
        showDeleteDialog = true
    }

    fun onConfirmButtonClick() {
        viewModelScope.launch { task.id?.let { repository.deleteTask(it) } }
        showDeleteDialog = false
        navController.popBackStack()
    }

    fun onDismissButtonClick() {
        showDeleteDialog = false
    }
}