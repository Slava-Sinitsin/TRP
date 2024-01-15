package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.launch

class TeacherDisciplinesViewModel(
    var onDisciplineClick: (id: Int) -> Unit
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    var disciplines by mutableStateOf(repository.disciplines)
        private set

    init {
        viewModelScope.launch {
            disciplines = repository.getDisciplines()
        }
    }

    fun getGroup(index: Int): com.example.trp.data.mappers.disciplines.DisciplineData {
        return disciplines[index]
    }

    fun navigateToGroups(index: Int) {
        getGroup(index = index).let { group ->
            group.id?.let { id -> onDisciplineClick(id) }
        }
    }
}