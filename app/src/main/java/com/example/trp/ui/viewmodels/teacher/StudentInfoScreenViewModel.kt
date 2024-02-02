package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.trp.data.mappers.StudentAppointments
import com.example.trp.data.mappers.tasks.Student
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
    @Assisted
    val onAddTaskToStudentClick: (studentId: Int) -> Unit,
    @Assisted
    val navController: NavHostController
) : ViewModel() {
    var studentAppointments by mutableStateOf(emptyList<StudentAppointments>())
        private set
    var tasks by mutableStateOf(repository.tasks)
        private set
    var student by mutableStateOf(Student())
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            studentId: Int,
            onAddTaskToStudentClick: (studentId: Int) -> Unit,
            navController: NavHostController
        ): StudentInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideStudentInfoScreenViewModel(
            factory: Factory,
            studentId: Int,
            onAddTaskToStudentClick: (studentId: Int) -> Unit,
            navController: NavHostController
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(studentId, onAddTaskToStudentClick, navController) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            studentAppointments =
                repository.getStudentAppointments().filter { it.studentId == studentId }
            student = repository.students.find { it.id == studentId } ?: Student()
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

    fun onBackIconButtonClick() {
        navController.popBackStack()
    }

    fun onAddTaskToStudentButtonClick() {
        onAddTaskToStudentClick(studentId)
    }
}