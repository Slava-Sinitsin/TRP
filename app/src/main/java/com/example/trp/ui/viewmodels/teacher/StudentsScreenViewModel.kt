package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class StudentsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val groupId: Int,
    @Assisted
    val onStudentClick: (id: Int) -> Unit
) : ViewModel() {
    var students by mutableStateOf(repository.students)
        private set

    @AssistedFactory
    interface Factory {
        fun create(groupId: Int, onStudentClick: (id: Int) -> Unit): StudentsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideStudentsScreenViewModel(
            factory: Factory,
            groupId: Int,
            onStudentClick: (id: Int) -> Unit
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(groupId, onStudentClick) as T
                }
            }
        }
    }

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