package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.trp.data.mappers.CheckBoxState
import com.example.trp.data.mappers.PostStudentAppointmentsBody
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AddTaskToStudentScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val studentId: Int,
    @Assisted
    val navController: NavHostController
) : ViewModel() {
    var tasks by mutableStateOf(repository.tasks)
        private set

    var tasksCheckBoxStates by mutableStateOf(List(tasks.size) { CheckBoxState() })
        private set

    private var tasksCheckBoxStatesBck by mutableStateOf(emptyList<CheckBoxState>())

    var student by mutableStateOf(Student())
        private set

    private var studentAppointments by mutableStateOf(repository.studentAppointments)

    var isTaskChanged by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            studentId: Int,
            navController: NavHostController
        ): AddTaskToStudentScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAddTaskToStudentScreenViewModel(
            factory: Factory,
            studentId: Int,
            navController: NavHostController
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(studentId, navController) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            student = repository.students.find { it.id == studentId } ?: Student()
            studentAppointments = studentAppointments.filter { it.studentId == studentId }
            studentAppointments.forEach { studentAppointments ->
                tasks.forEachIndexed { index, task ->
                    if (studentAppointments.taskId == task.id) {
                        tasksCheckBoxStates = tasksCheckBoxStates.toMutableList().also {
                            it[index] = CheckBoxState(isSelected = true, isEnable = false)
                        }
                    }
                }
            }
            tasksCheckBoxStatesBck = tasksCheckBoxStates
        }
    }

    fun getTask(index: Int): Task {
        return tasks[index]
    }

    fun onBackIconButtonClick() {
        navController.popBackStack()
    }

    fun onCheckBoxClick(index: Int) {
        tasksCheckBoxStates = tasksCheckBoxStates.toMutableList().also {
            it[index] = CheckBoxState(isSelected = !it[index].isSelected)
        }
        isTaskChanged = true
    }

    fun onRollBackIconButtonClick() {
        tasksCheckBoxStates = tasksCheckBoxStatesBck
        isTaskChanged = false
    }

    fun onApplyButtonClick() {
        viewModelScope.launch {
            tasksCheckBoxStates.forEachIndexed { index, it ->
                if (it.isSelected && it.isEnable) {
                    repository.postStudentAppointments(
                        PostStudentAppointmentsBody(
                            studentId = studentId,
                            taskId = tasks[index].id
                        )
                    )
                }
            }
            isTaskChanged = false
        }
    }
}