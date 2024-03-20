package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CreateTeamScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val groupId: Int
) : ViewModel() {
    var students by mutableStateOf(repository.students)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            groupId: Int
        ): CreateTeamScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateTeamScreenViewModel(
            factory: Factory,
            groupId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(groupId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            students = repository.getStudents(groupId = groupId).sortedBy { it.fullName }
        }
    }
}