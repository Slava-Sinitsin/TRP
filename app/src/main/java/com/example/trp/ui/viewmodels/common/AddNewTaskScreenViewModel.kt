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
    var taskArgumentList by mutableStateOf(listOf(Triple("", "", false)))
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
        taskTitle = newTitleValue
        checkFields()
    }

    fun updateDescriptionValue(newDescriptionValue: String) {
        taskDescription = newDescriptionValue
        checkFields()
    }

    fun updateLanguageValue(newLanguageValue: String) {
        if (taskLanguage != newLanguageValue) {
            typeButtonsEnabled = typeButtonsEnabled.copy(first = true)
            typeButtonsEnabled = typeButtonsEnabled.copy(second = 1.0f)
            taskLanguage = newLanguageValue
            taskFunctionType = ""
            functionTypeListExpanded = false
            taskArgumentList = taskArgumentList.map { item ->
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
        }
        checkFields()
    }

    fun updateFunctionTypeExpanded(isExpanded: Boolean) {
        functionTypeListExpanded = isExpanded
    }

    fun updateFunctionTypeValue(newFunctionTypeValue: String) {
        taskFunctionType = newFunctionTypeValue
        checkFields()
    }

    fun updateFunctionNameValue(newFunctionNameValue: String) {
        taskFunctionName = newFunctionNameValue
        checkFields()
    }

    fun addOptionalArgument() {
        deleteButtonEnabled = Pair(true, 1.0f)
        taskArgumentList = taskArgumentList + Triple("", "", false)
    }

    fun getArgument(index: Int): Triple<String, String, Boolean> {
        return taskArgumentList[index]
    }

    fun updateArgumentTypeExpanded(index: Int, isExpanded: Boolean) {
        taskArgumentList = taskArgumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Triple(item.first, item.second, isExpanded)
            } else {
                item
            }
        }
    }

    fun updateArgumentTypeValue(index: Int, newArgumentTypeValue: String) {
        taskArgumentList = taskArgumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Triple(newArgumentTypeValue, item.second, item.third)
            } else {
                item
            }
        }
        checkFields()
    }

    fun updateArgumentNameValue(index: Int, newArgumentNameValue: String) {
        taskArgumentList = taskArgumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Triple(item.first, newArgumentNameValue, item.third)
            } else {
                item
            }
        }
        checkFields()
    }

    fun onDeleteArgumentClick(index: Int) {
        taskArgumentList = taskArgumentList.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }
        if (taskArgumentList.size < 2) {
            deleteButtonEnabled = Pair(false, 0.6f)
        }
        checkFields()
    }

    private fun checkFields() {
        applyButtonEnabled =
            (taskTitle.isNotEmpty() && taskDescription.isNotEmpty() && taskLanguage.isNotEmpty()
                    && taskFunctionType.isNotEmpty() && taskFunctionName.isNotEmpty()
                    && taskArgumentList.all { it.first.isNotEmpty() }
                    && taskArgumentList.all { it.second.isNotEmpty() })
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