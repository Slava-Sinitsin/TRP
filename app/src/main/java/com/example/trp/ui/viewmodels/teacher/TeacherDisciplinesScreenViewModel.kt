package com.example.trp.ui.viewmodels.teacher

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
import java.net.ConnectException
import java.net.SocketTimeoutException

class TeacherDisciplinesScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var disciplines by mutableStateOf(emptyList<DisciplineData>())
        private set
    var errorMessage by mutableStateOf("")
        private set
    var isRefreshing by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(): TeacherDisciplinesScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherDisciplinesScreenViewModel(
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
        viewModelScope.launch { init() }
    }

    private suspend fun init(update: Boolean = false) {
        try {
            disciplines = repository.getDisciplines(update = update).sortedBy { it.name }
        } catch (e: SocketTimeoutException) {
            updateErrorMessage("Timeout")
        } catch (e: ConnectException) {
            updateErrorMessage("Check internet connection")
        } catch (e: Exception) {
            updateErrorMessage("Error")
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            init(update = true)
            isRefreshing = false
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun getGroup(index: Int): DisciplineData {
        return disciplines[index]
    }

    fun setCurrentDiscipline(id: Int) { // TODO
        repository.setCurrentDisciplineId(id)
    }
}