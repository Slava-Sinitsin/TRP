package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.repository.UserAPIRepositoryImpl
import kotlinx.coroutines.launch

class StudentsScreenViewModel(
    val groupId: Int,
    var onStudentClick: (id: Int) -> Unit
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    var students by mutableStateOf(repository.students)
        private set

    init {
        viewModelScope.launch {
            students = repository.getStudents(groupId = groupId)
        }
    }

    fun getStudent(index: Int): com.example.trp.data.mappers.tasks.Student {
        return students[index]
    }

    fun navigateToStudent(index: Int) {
        getStudent(index = index).let { student -> student.id?.let { id -> onStudentClick(id) } }
    }
}