package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.disciplines.DisciplineData
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.launch

class DisciplinesScreenViewModel(
    var onDisciplineClick: (id: Int) -> Unit
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    var disciplines by mutableStateOf(repository.disciplines)
        private set

    init {
        viewModelScope.launch {
            repository.disciplinesChanged = false
            disciplines = repository.getDisciplines()
        }
    }

    fun getDiscipline(index: Int): DisciplineData {
        return disciplines[index]
    }

    fun navigateToTasks(index: Int) {
        getDiscipline(index = index).let { discipline ->
            discipline.id?.let { id -> onDisciplineClick(id) }
        }
    }
}