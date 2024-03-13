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
    val languageList =
        listOf("c", "c++")
    private val cTypeList =
        listOf("int", "int*", "int**", "float", "float*", "float**", "char", "char*", "char**")
    private val cppTypeList =
        listOf("int", "int*", "int**", "float", "float*", "float**", "char", "string")

    var taskTitle by mutableStateOf("")
        private set
    var taskDescription by mutableStateOf("")
        private set
    var taskLanguage by mutableStateOf("")
        private set
    var taskFunctionType by mutableStateOf("")
        private set
    var taskFunctionName by mutableStateOf("")
        private set

    var typeButtonsEnabled by mutableStateOf(Pair(false, 0.6f))
        private set
    var taskOptionalArgumentList by mutableStateOf(listOf(Triple("", "", false)))
        private set
    var deleteButtonEnabled by mutableStateOf(Pair(false, 0.6f))
        private set
    var functionTypeListExpanded by mutableStateOf(false)
        private set
    var typeList by mutableStateOf(emptyList<String>())
        private set

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

    fun updateLanguageValue(newLanguageValue: String) {
        if (taskLanguage != newLanguageValue) {
            typeButtonsEnabled = typeButtonsEnabled.copy(first = true)
            typeButtonsEnabled = typeButtonsEnabled.copy(second = 1.0f)
            taskLanguage = newLanguageValue
            taskFunctionType = ""
            functionTypeListExpanded = false
            taskOptionalArgumentList = taskOptionalArgumentList.map { item ->
                Triple("", item.second, false)
            }
            when (taskLanguage) {
                "c" -> {
                    typeList = cTypeList
                }

                "c++" -> {
                    typeList = cppTypeList
                }
            }
            applyButtonEnabled = taskLanguage.isNotEmpty()
        }
    }

    fun updateFunctionTypeExpanded(isExpanded: Boolean) {
        functionTypeListExpanded = isExpanded
    }

    fun updateFunctionTypeValue(newFunctionTypeValue: String) {
        taskFunctionType = newFunctionTypeValue
        applyButtonEnabled = taskFunctionType.isNotEmpty()
    }

    fun updateFunctionNameValue(newFunctionNameValue: String) {
        taskFunctionName = newFunctionNameValue
        applyButtonEnabled = taskFunctionName.isNotEmpty()
    }

    fun addOptionalArgument() {
        deleteButtonEnabled = Pair(true, 1.0f)
        taskOptionalArgumentList = taskOptionalArgumentList + Triple("", "", false)
    }

    fun getArgument(index: Int): Triple<String, String, Boolean> {
        return taskOptionalArgumentList[index]
    }

    fun updateArgumentTypeExpanded(index: Int, isExpanded: Boolean) {
        taskOptionalArgumentList = taskOptionalArgumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Triple(item.first, item.second, isExpanded)
            } else {
                item
            }
        }
    }

    fun updateArgumentTypeValue(index: Int, newArgumentTypeValue: String) {
        taskOptionalArgumentList = taskOptionalArgumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Triple(newArgumentTypeValue, item.second, item.third)
            } else {
                item
            }
        }
    }

    fun updateArgumentNameValue(index: Int, newArgumentNameValue: String) {
        taskOptionalArgumentList = taskOptionalArgumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Triple(item.first, newArgumentNameValue, item.third)
            } else {
                item
            }
        }
    }

    fun onDeleteArgumentClick(index: Int) {
        taskOptionalArgumentList = taskOptionalArgumentList.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }
        if (taskOptionalArgumentList.size < 2) {
            deleteButtonEnabled = Pair(false, 0.6f)
        }
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