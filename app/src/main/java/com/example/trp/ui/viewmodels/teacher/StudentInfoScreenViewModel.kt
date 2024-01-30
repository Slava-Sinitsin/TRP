package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.StudentAppointments
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class StudentInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val studentId: Int,
) : ViewModel() {
    var studentAppointments by mutableStateOf(emptyList<StudentAppointments>())
    var tasks by mutableStateOf(repository.tasks)

    @AssistedFactory
    interface Factory {
        fun create(studentId: Int): StudentInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideStudentInfoScreenViewModel(
            factory: Factory,
            studentId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(studentId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            studentAppointments =
                repository.getStudentAppointments().filter { it.studentId == studentId }
        }
    }

    fun getTask(index: Int): Task {
        return tasks.find { it.id == tasks[index].id } ?: Task()
    }

    fun getStudentAppointment(index: Int): StudentAppointments {
        return studentAppointments.find { studentAppointment ->
            studentAppointment.taskId == tasks.find { task -> task.id == tasks[index].id }?.id
        } ?: StudentAppointments()
    }
}