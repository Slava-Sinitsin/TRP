package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AddNewTestScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int,
    @Assisted
    val navController: NavHostController
) : ViewModel() {
    var inputValue by mutableStateOf("")
        private set
    var outputValue by mutableStateOf("")
        private set

    var applyButtonEnabled by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            taskId: Int,
            navController: NavHostController
        ): AddNewTestScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAddNewTestScreenViewModel(
            factory: Factory,
            taskId: Int,
            navController: NavHostController
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(taskId, navController) as T
                }
            }
        }
    }

    fun updateInputValue(newInputValue: String) {
        inputValue = newInputValue
        applyButtonEnabled = inputValue.isNotEmpty()
    }

    fun updateOutputValue(newOutputValue: String) {
        outputValue = newOutputValue
        applyButtonEnabled = outputValue.isNotEmpty()
    }

    fun onCloseButtonClick() {
        navController.popBackStack()
    }

    fun onSaveButtonClick() {
        // TODO
    }
}