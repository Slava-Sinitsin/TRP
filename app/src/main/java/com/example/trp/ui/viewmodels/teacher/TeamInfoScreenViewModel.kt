package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.StudentAppointments
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.TaskStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TeamInfoScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val studentId: Int
) : ViewModel() {
    var student by mutableStateOf(Student())
        private set
    var tasks by mutableStateOf(emptyList<Task>())
        private set
    private var studentAppointments by mutableStateOf(emptyList<StudentAppointments>())

    @AssistedFactory
    interface Factory {
        fun create(studentId: Int): TeamInfoScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeamInfoScreenViewModel(
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
            student = repository.students.find { it.id == studentId } ?: Student()
            tasks = repository.getStudentTasks(studentId).sortedBy { it.title }
            studentAppointments = repository.getStudentAppointments().filter { studentAppointment ->
                tasks.any { task ->
                    studentAppointment.taskId == task.id && studentAppointment.studentId == studentId
                }
            }
        }
    }

    fun getTask(index: Int): Task {
        return tasks.find { it.id == tasks[index].id } ?: Task()
    }

    private fun getStudentAppointment(index: Int): StudentAppointments {
        return studentAppointments.find { it.taskId == tasks[index].id } ?: StudentAppointments()
    }

    fun getStatus(index: Int): Pair<Float, Color> {
        return when (getStudentAppointment(index).status) {
            TaskStatus.New.status -> Pair(
                TaskStatus.New.float,
                TaskStatus.New.color
            )

            TaskStatus.InProgress.status -> Pair(
                TaskStatus.InProgress.float,
                TaskStatus.InProgress.color
            )

            TaskStatus.Complete.status -> Pair(
                TaskStatus.Complete.float,
                TaskStatus.Complete.color
            )

            else -> Pair(0f, Color.Transparent)
        }
    }
}