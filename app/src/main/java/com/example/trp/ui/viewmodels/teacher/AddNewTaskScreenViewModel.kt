package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AddNewTaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int,
    @Assisted
    val navController: NavHostController
) : ViewModel() {
    var taskTitle by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var taskFunctionName by mutableStateOf("")
    var taskLanguage by mutableStateOf("")
    var applyButtonEnabled by mutableStateOf(false)

    @AssistedFactory
    interface Factory {
        fun create(
            disciplineId: Int,
            navController: NavHostController
        ): AddNewTaskScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAddNewTaskScreenViewModel(
            factory: Factory,
            disciplineId: Int,
            navController: NavHostController
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(disciplineId, navController) as T
                }
            }
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

    fun onRollBackIconClick() {
        navController.popBackStack()
    }

    fun onSaveButtonClick() {
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
        navController.popBackStack()
    }
}