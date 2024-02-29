package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Test
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.TaskTestsTabs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TaskInfoTestsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var task by mutableStateOf(repository.task)
        private set
    var taskTitle by mutableStateOf("")
        private set
    var taskDescription by mutableStateOf("")
        private set
    var taskFunctionName by mutableStateOf("")
        private set
    var taskLanguage by mutableStateOf("")
        private set
    var readOnlyMode by mutableStateOf(true)
        private set
    var readOnlyAlpha by mutableStateOf(0.6f)
        private set
    var applyButtonEnabled by mutableStateOf(true)
        private set
    var showDeleteDialog by mutableStateOf(false)
        private set

    val taskTestsScreens = listOf(
        TaskTestsTabs.TaskInfo,
        TaskTestsTabs.Tests
    )

    var selectedTabIndex by mutableStateOf(0)
        private set

    var tests by mutableStateOf(List(20) {
        Test(
            title = "Test ${it.inc()}",
            input = "[1, 2, 3, 4]",
            output = "4"
        )
    })
        private set

    @AssistedFactory
    interface Factory {
        fun create(studentId: Int): TaskInfoTestsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTaskInfoTestsScreenViewModel(
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

    fun beforeConfirmButtonClick() {
        viewModelScope.launch { task.id?.let { repository.deleteTask(it) } }
        showDeleteDialog = false
    }

    fun onDismissButtonClick() {
        showDeleteDialog = false
    }

    fun setPagerState(index: Int) {
        selectedTabIndex = index
    }

    fun getTest(index: Int): Test {
        return tests[index]
    }
}