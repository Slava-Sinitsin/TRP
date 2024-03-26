package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.StudentRegistration
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.CreateGroupTabs
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class CreateGroupScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var groupName by mutableStateOf("")
        private set
    var selectedTabIndex by mutableStateOf(0)
        private set
    val createGroupScreens = mutableListOf(
        CreateGroupTabs.MainScreen,
        CreateGroupTabs.CreateStudentScreen
    )
    var topBarText by mutableStateOf("Create new group")
        private set

    var studentFullName by mutableStateOf("")
        private set
    var studentUsername by mutableStateOf("")
        private set
    var studentPassword by mutableStateOf("")
        private set
    var groupApplyButtonEnabled by mutableStateOf(false)
        private set
    var studentApplyButtonEnabled by mutableStateOf(false)
        private set
    var students by mutableStateOf(emptyList<StudentRegistration>())
        private set
    var studentEditMode by mutableStateOf(false)
        private set
    private var studentEditIndex by mutableStateOf(0)

    @AssistedFactory
    interface Factory {
        fun create(): CreateGroupScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateGroupScreenViewModel(
            factory: Factory
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create() as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {

        }
    }

    fun updateGroupNameValue(newGroupName: String) {
        groupName = newGroupName
        checkGroupFields()
    }

    fun createNewStudent() {
        studentFullName = ""
        studentUsername = ""
        studentPassword = ""
        checkStudentFields()
        setPagerState(1)
    }

    fun setPagerState(index: Int) {
        selectedTabIndex = index
        when (selectedTabIndex) {
            0 -> {
                topBarText = "Create new group"
            }

            1 -> {
                topBarText = if (studentEditMode) {
                    "Edit student"
                } else {
                    "Create new student"
                }
            }
        }
    }

    fun updateStudentFullNameValue(newStudentFullName: String) {
        studentFullName = newStudentFullName
        checkStudentFields()
    }

    fun updateStudentUsernameValue(newStudentUsername: String) {
        studentUsername = newStudentUsername
        checkStudentFields()
    }

    fun updateStudentPasswordValue(newStudentPassword: String) {
        studentPassword = newStudentPassword
        checkStudentFields()
    }

    private fun checkGroupFields() {
        groupApplyButtonEnabled = (groupName.isNotEmpty())
    }

    private fun checkStudentFields() {
        studentApplyButtonEnabled =
            (studentFullName.isNotEmpty() && studentUsername.isNotEmpty() && studentPassword.isNotEmpty())
    }

    fun onApplyCreateStudentClick() { // TODO
        students += StudentRegistration(
            fullName = studentFullName,
            username = studentUsername,
            password = studentPassword
        )
        setPagerState(0)
    }

    fun onDeleteStudentClick(index: Int) {
        students = students.filterIndexed { currentIndex, _ ->
            currentIndex != index
        }
    }

    fun onEditStudentButtonClick(index: Int) {
        studentEditMode = true
        studentEditIndex = index
        studentFullName = students[studentEditIndex].fullName ?: ""
        studentUsername = students[studentEditIndex].username ?: ""
        studentPassword = students[studentEditIndex].password ?: ""
        checkStudentFields()
        setPagerState(1)
    }

    fun onApplyEditStudentClick() { // TODO
        students = students.toMutableList().also {
            it[studentEditIndex] = StudentRegistration(
                fullName = studentFullName,
                username = studentUsername,
                password = studentPassword
            )
        }
        studentEditMode = false
        setPagerState(0)
    }

    fun onEditStudentBackButtonClick() {
        studentEditMode = false
        setPagerState(0)
    }

    fun onApplyCreateGroupClick() { // TODO

    }
}