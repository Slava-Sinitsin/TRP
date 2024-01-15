package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.launch

class GroupsScreenViewModel(
    val disciplineId: Int,
    var onGroupClick: (id: Int) -> Unit
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    var teacherAppointments by mutableStateOf(repository.teacherAppointments)
        private set

    init {
        viewModelScope.launch {
            teacherAppointments =
                repository.getTeacherAppointments().filter { it.discipline?.id == disciplineId }
        }
    }

    fun getGroup(index: Int): com.example.trp.data.mappers.teacherappointments.Group {
        return teacherAppointments[index].group ?: com.example.trp.data.mappers.teacherappointments.Group()
    }

    fun navigateToStudents(index: Int) {
        getGroup(index = index).let { task -> task.id?.let { id -> onGroupClick(id) } }
    }
}