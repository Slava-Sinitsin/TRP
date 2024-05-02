package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.solution.Argument
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class CreateTaskScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val labId: Int
) : ViewModel() {
    val languageList =
        listOf("c", "c++")
    private val cTypeList =
        listOf(
            "int",
            "int*",
            "int**",
            "double",
            "double*",
            "double**",
            "char",
            "char*",
            "char**",
            "bool"
        )
    private val cppTypeList =
        listOf("int", "int*", "int**", "double", "double*", "double**", "char", "string", "bool")

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var language by mutableStateOf("")
        private set
    var testableList by mutableStateOf(listOf("Yes", "No"))
        private set
    var testableIndex by mutableStateOf(0)
        private set
    var functionType by mutableStateOf("")
        private set
    var functionName by mutableStateOf("")
        private set

    var typeButtonsEnabled by mutableStateOf(Pair(false, 0.6f))
        private set
    var argumentList by mutableStateOf(listOf(Pair(Argument(), false)))
        private set
    var deleteButtonEnabled by mutableStateOf(Pair(false, 0.6f))
        private set
    var functionTypeListExpanded by mutableStateOf(false)
        private set
    var typeList by mutableStateOf(emptyList<String>())
        private set

    var applyButtonEnabled by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
        private set
    var responseSuccess by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            labId: Int
        ): CreateTaskScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateTaskScreenViewModel(
            factory: Factory,
            labId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(labId) as T
                }
            }
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun updateTitleValue(newTitleValue: String) {
        title = newTitleValue
        checkFields()
    }

    fun updateDescriptionValue(newDescriptionValue: String) {
        description = newDescriptionValue
        checkFields()
    }

    fun updateLanguageValue(newLanguageValue: String) {
        if (language != newLanguageValue) {
            typeButtonsEnabled = typeButtonsEnabled.copy(first = true)
            typeButtonsEnabled = typeButtonsEnabled.copy(second = 1.0f)
            language = newLanguageValue
            functionType = ""
            functionTypeListExpanded = false
            argumentList = argumentList.map { item ->
                Pair(Argument(type = "", name = item.first.name), false)
            }
            when (language) {
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

    fun updateTestableValue(newTestableValue: Int) {
        testableIndex = newTestableValue
        checkFields()
    }

    fun updateFunctionTypeExpanded(isExpanded: Boolean) {
        functionTypeListExpanded = isExpanded
    }

    fun updateFunctionTypeValue(newFunctionTypeValue: String) {
        functionType = newFunctionTypeValue
        checkFields()
    }

    fun updateFunctionNameValue(newFunctionNameValue: String) {
        functionName = newFunctionNameValue
        checkFields()
    }

    fun addOptionalArgument() {
        deleteButtonEnabled = Pair(true, 1.0f)
        argumentList = argumentList + Pair(Argument(), false)
        checkFields()
    }

    fun getArgument(index: Int): Pair<Argument, Boolean> {
        return argumentList[index]
    }

    fun updateArgumentTypeExpanded(index: Int, isExpanded: Boolean) {
        argumentList = argumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Pair(item.first, isExpanded)
            } else {
                item
            }
        }
    }

    fun updateArgumentTypeValue(index: Int, newArgumentTypeValue: String) {
        argumentList = argumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Pair(Argument(type = newArgumentTypeValue, name = item.first.name), item.second)
            } else {
                item
            }
        }
        checkFields()
    }

    fun updateArgumentNameValue(index: Int, newArgumentNameValue: String) {
        argumentList = argumentList.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                Pair(Argument(type = item.first.type, name = newArgumentNameValue), item.second)
            } else {
                item
            }
        }
        checkFields()
    }

    fun onDeleteArgumentClick(index: Int) {
        argumentList = argumentList.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }
        if (argumentList.size < 2) {
            deleteButtonEnabled = Pair(false, 0.6f)
        }
        checkFields()
    }

    private fun checkFields() {
        applyButtonEnabled =
            ((title.isNotEmpty() && description.isNotEmpty() && language.isNotEmpty() && testableList[testableIndex] == "No")
                    || (title.isNotEmpty() && description.isNotEmpty() && language.isNotEmpty()
                    && functionType.isNotEmpty() && functionName.isNotEmpty()
                    && argumentList.all { it.first.type?.isNotEmpty() ?: false }
                    && argumentList.all { it.first.name?.isNotEmpty() ?: false }))
    }


    fun beforeSaveButtonClick() {
        responseSuccess = false
        viewModelScope.launch {
            try {
                when (testableList[testableIndex]) {
                    "Yes" -> {
                        repository.postTestableTask(
                            Task(
                                labWorkId = labId,
                                title = title,
                                description = description,
                                language = language,
                                functionName = functionName,
                                returnType = functionType,
                                arguments = argumentList.map { it.first },
                            )
                        )
                    }

                    "No" -> {
                        repository.postNonTestableTask(
                            Task(
                                labWorkId = labId,
                                title = title,
                                description = description,
                                language = language,
                            )
                        )
                    }
                }
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