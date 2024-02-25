package com.example.trp.ui.viewmodels.common

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

class AddNewTaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int
) : ViewModel() {
    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var taskFunctionName by mutableStateOf("")
    var taskLanguage by mutableStateOf("")
    var applyButtonEnabled by mutableStateOf(false)

    @AssistedFactory
    interface Factory {
        fun create(
            disciplineId: Int
        ): AddNewTaskScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAddNewTaskScreenViewModel(
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

    fun updateTitleValue(newTitleValue: String) {
        applyButtonEnabled = newTitleValue.isNotEmpty()
        taskTitle = newTitleValue
    }

    fun updateDescriptionValue(newDescriptionValue: String) {
        taskDescription = newDescriptionValue
        applyButtonEnabled = taskDescription.isNotEmpty()
    }

    fun updateFunctionNameValue(newFunctionNameValue: String) {
        taskFunctionName = newFunctionNameValue
        applyButtonEnabled = taskFunctionName.isNotEmpty()
    }

    fun updateLanguageValue(newLanguageValue: String) {
        taskLanguage = newLanguageValue
        applyButtonEnabled = taskLanguage.isNotEmpty()
    }

    fun beforeSaveButtonClick() {
        viewModelScope.launch {
            repository.postTask(
                Task(
                    disciplineId = disciplineId,
                    title = taskTitle,
                    description = taskDescription,
                    functionName = taskFunctionName,
                    language = taskLanguage
                )
            )
        }
    }
}