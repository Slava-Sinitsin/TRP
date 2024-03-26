package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CreateTeacherScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var teacherFullName by mutableStateOf("")
        private set
    var teacherUsername by mutableStateOf("")
        private set
    var teacherPassword by mutableStateOf("")
        private set

    var applyButtonEnabled by mutableStateOf(false)
        private set

    var positions by mutableStateOf(listOf("ROLE_LECTURER", "ROLE_ASSISTANT"))
        private set
    var selectedPosition by mutableStateOf(positions[0])
        private set

    @AssistedFactory
    interface Factory {
        fun create(): CreateTeacherScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateTeacherScreenViewModel(
            factory: Factory
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create() as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {

        }
    }

    fun updateTeacherFullNameValue(newTeacherFullName: String) {
        teacherFullName = newTeacherFullName
        checkFields()
    }

    fun updateTeacherUsernameValue(newTeacherUsername: String) {
        teacherUsername = newTeacherUsername
        checkFields()
    }

    fun updateTeacherPasswordValue(newTeacherPassword: String) {
        teacherPassword = newTeacherPassword
        checkFields()
    }

    private fun checkFields() {
        applyButtonEnabled =
            (teacherFullName.isNotEmpty() && teacherUsername.isNotEmpty() && teacherPassword.isNotEmpty())
    }

    fun updatePositionValue(newPosition: String) {
        selectedPosition = newPosition
    }

    fun onApplyButtonClick() { // TODO

    }
}