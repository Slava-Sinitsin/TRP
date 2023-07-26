package com.example.trp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.datamanagers.DisciplinesDataManager
import com.example.trp.data.disciplines.Discipline
import kotlinx.coroutines.launch

class DisciplineScreenViewModel(
    var onDisciplineClick: () -> Unit
) : ViewModel() {

    lateinit var disciplines: List<Discipline>
        private set

    init {
        viewModelScope.launch {
            DisciplinesDataManager.getDisciplines().collect {
                disciplines = it.list!!
            }
        }
    }

    fun getDiscipline(index: Int): Discipline {
        return disciplines[index]
    }
}