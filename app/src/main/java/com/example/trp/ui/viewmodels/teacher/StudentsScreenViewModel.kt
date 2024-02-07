package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.trp.data.mappers.tasks.Student
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class StudentsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val groupId: Int,
    @Assisted
    val onStudentClick: (id: Int) -> Unit,
    @Assisted
    val navController: NavHostController
) : ViewModel() {
    var students by mutableStateOf(repository.students)
        private set

    var group by mutableStateOf(Group())
        private set

    var isMenuShow by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            groupId: Int,
            onStudentClick: (id: Int) -> Unit,
            navController: NavHostController
        ): StudentsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideStudentsScreenViewModel(
            factory: Factory,
            groupId: Int,
            onStudentClick: (id: Int) -> Unit,
            navController: NavHostController
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(groupId, onStudentClick, navController) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            students = repository.getStudents(groupId = groupId)
            group =
                repository.teacherAppointments.find { it.group?.id == groupId }?.group ?: Group()
        }
    }

    fun getStudent(index: Int): Student {
        return students[index]
    }

    fun navigateToStudent(index: Int) {
        getStudent(index = index).let { student -> student.id?.let { id -> onStudentClick(id) } }
    }

    fun onBackIconButtonClick() {
        navController.popBackStack()
    }

    fun onMenuButtonClick() {
        isMenuShow = !isMenuShow
    }

    fun onDismissRequest() {
        isMenuShow = false
    }

    fun onEveryoneAppointButtonClick() {
        // TODO
    }
}