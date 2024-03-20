package com.example.trp.ui.viewmodels.teacher

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.CheckBoxState
import com.example.trp.data.mappers.Team
import com.example.trp.data.mappers.tasks.Student
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
    var students by mutableStateOf(emptyList<Student>())
        private set

    var teams by mutableStateOf(emptyList<Team>())
        private set
    private var maxTeamSize by mutableStateOf(0)
    var selectedStudents by mutableStateOf(emptyList<Student>())
        private set
    var studentsCheckBoxStates by mutableStateOf(emptyList<CheckBoxState>())
        private set
    var teamSizeOverflow by mutableStateOf(false)

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
            studentsCheckBoxStates = List(students.size) { CheckBoxState() }
            maxTeamSize = 2
        }
    }

    fun getStudent(index: Int): Student {
        return students[index]
    }

    fun onStudentClick(index: Int) {
        studentsCheckBoxStates = studentsCheckBoxStates.toMutableList().also {
            it[index] = it[index].copy(isSelected = !it[index].isSelected)
        }
        selectedStudents = studentsCheckBoxStates.mapIndexedNotNull { currentIndex, state ->
            if (state.isSelected) getStudent(currentIndex) else null
        }
        teamSizeOverflow = selectedStudents.size >= maxTeamSize
        studentsCheckBoxStates = studentsCheckBoxStates.map { currentState ->
            currentState.copy(isEnable = !teamSizeOverflow || currentState.isSelected)
        }
        Log.e("selectedStudents", selectedStudents.toString())
    }

    fun onAddButtonClick() {

    }
}