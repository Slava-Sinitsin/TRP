package com.example.trp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.datamanagers.DisciplinesDataManager
import com.example.trp.data.disciplines.DisciplineData
import kotlinx.coroutines.launch

class DisciplineScreenViewModel(
    var onDisciplineClick: (id: Int) -> Unit
) : ViewModel() {

    lateinit var disciplinesData: List<DisciplineData>
        private set

    init {
        viewModelScope.launch {
            DisciplinesDataManager.getDisciplines().collect {
                disciplinesData = it.list!!
            }
        }
    }

    fun getDiscipline(index: Int): DisciplineData {
        return disciplinesData[index]
    }

    fun navigateToTasks(index: Int) {
        getDiscipline(index = index).let { discipline ->
            discipline.id?.let { id -> onDisciplineClick(id) }
        }
    }
}