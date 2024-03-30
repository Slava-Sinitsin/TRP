package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Test
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
    var inputValue by mutableStateOf("")
        private set
    var outputValue by mutableStateOf("")
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

    fun updateInputValue(newInputValue: String) {
        inputValue = newInputValue
        saveButtonEnabled = inputValue.isNotEmpty()
    }

    fun updateOutputValue(newOutputValue: String) {
        outputValue = newOutputValue
        saveButtonEnabled = outputValue.isNotEmpty()
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            repository.postNewTest(Test(taskId = taskId, input = inputValue, output = outputValue))
        }
    }
}