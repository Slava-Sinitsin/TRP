package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Test
import com.example.trp.data.mappers.tasks.solution.Argument
import com.example.trp.data.mappers.tasks.solution.ArgumentWithRegex
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CreateNewTestScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var task by mutableStateOf(Task())
        private set

    private var arguments by mutableStateOf(emptyList<Argument>())
    private var argumentsRegexes by mutableStateOf(emptyList<String>())
    var argumentsWithRegex by mutableStateOf(emptyList<ArgumentWithRegex>())
        private set

    var outputValue by mutableStateOf("")
        private set
    private var outputRegex by mutableStateOf("")
    var outputMatchRegex by mutableStateOf(false)
        private set

    var saveButtonEnabled by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            taskId: Int
        ): CreateNewTestScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateNewTestScreenViewModel(
            factory: Factory,
            taskId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(taskId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            task = repository.tasks.find { it.id == taskId } ?: Task()
            arguments = task.arguments ?: emptyList()
            argumentsRegexes = splitRegexToList(task.inputRegex ?: "")
            argumentsWithRegex = arguments.mapIndexedNotNull { index, argument ->
                if (index < argumentsRegexes.size) {
                    ArgumentWithRegex(
                        name = argument.name,
                        type = argument.type,
                        regex = argumentsRegexes[index]
                    )
                } else {
                    null
                }
            }
            outputRegex = splitRegexToList(task.outputRegex ?: "").first()
        }
    }

    fun updateInputValue(index: Int, newInputValue: String) {
        argumentsWithRegex = argumentsWithRegex.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                argumentsWithRegex[currentIndex].copy(
                    value = newInputValue,
                    isMatch = isStringMatchingRegex(input = newInputValue, regex = item.regex ?: "")
                )
            } else {
                item
            }
        }
        checkFields()
    }

    fun updateOutputValue(newOutputValue: String) {
        outputValue = newOutputValue
        outputMatchRegex = isStringMatchingRegex(outputValue, outputRegex)
        checkFields()
    }

    fun getArgument(index: Int): ArgumentWithRegex {
        return argumentsWithRegex[index]
    }

    private fun splitRegexToList(input: String): List<String> {
        val parts = input.split(", ")
        val regexList = mutableListOf<String>()
        for (part in parts) {
            regexList.add(part.trim())
        }
        return regexList
    }

    private fun isStringMatchingRegex(input: String, regex: String): Boolean {
        return Regex(regex).matches(input)
    }

    private fun checkFields() {
        saveButtonEnabled = (argumentsWithRegex.all {
            it.value?.isNotEmpty() ?: false && it.isMatch == true
        } && outputValue.isNotEmpty() && outputMatchRegex)
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            repository.postNewTest(
                Test(
                    taskId = taskId,
                    input = argumentsWithRegex.joinToString(", ") { it.value.toString() },
                    output = outputValue
                )
            )
        }
    }
}