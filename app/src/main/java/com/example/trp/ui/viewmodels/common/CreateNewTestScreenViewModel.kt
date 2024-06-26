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
import java.net.ConnectException
import java.net.SocketTimeoutException

class CreateNewTestScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var task by mutableStateOf(Task())
        private set

    var isOpenList by mutableStateOf(listOf("Yes", "No"))
        private set
    var isOpenSelectedIndex by mutableStateOf(1)
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

    val placeholdersList = listOf(
        Pair("int", "1"),
        Pair("double", "1.0"),
        Pair("char", "'a'"),
        Pair("int*", "[1, 2, 3]"),
        Pair("int**", "[[1, 2, 3], [4, 5, 6]]"),
        Pair("double*", "[1.0, 2.0, 3.0]"),
        Pair("double**", "[[1.0, 2.0, 3.0], [4.0, 5.0, 6.0]]"),
        Pair("char*", "[a, b, c]"),
        Pair("char**", "[[a, b, c], [d, e, f]]")
    )
    var errorMessage by mutableStateOf("")
        private set
    var responseSuccess by mutableStateOf(false)
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

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun updateIsOpenIndex(newIsOpenIndex: Int) {
        isOpenSelectedIndex = newIsOpenIndex
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
        responseSuccess = false
        viewModelScope.launch {
            try {
                repository.postNewTest(
                    Test(
                        taskId = taskId,
                        input = argumentsWithRegex.joinToString(", ") { it.value.toString() },
                        output = outputValue,
                        isOpen = isOpenList[isOpenSelectedIndex] == "Yes"
                    )
                )
                responseSuccess = true
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }
}