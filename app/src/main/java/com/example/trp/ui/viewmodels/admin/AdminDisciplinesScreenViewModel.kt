package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AdminDisciplinesScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var disciplines by mutableStateOf(repository.disciplines)
        private set

    @AssistedFactory
    interface Factory {
        fun create(): AdminDisciplinesScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAdminDisciplinesScreenViewModel(
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
            disciplines = repository.getDisciplines()
        }
    }

    fun getGroup(index: Int): DisciplineData {
        return disciplines[index]
    }
}