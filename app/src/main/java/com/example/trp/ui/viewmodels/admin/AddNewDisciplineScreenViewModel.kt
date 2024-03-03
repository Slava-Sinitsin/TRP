package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.disciplines.PostNewDisciplineBody
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.mappers.teacherappointments.Teacher
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AddNewDisciplineScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var disciplineName by mutableStateOf("")
        private set
    var disciplineYear by mutableStateOf((2000..2030).map { it.toString() })
        private set
    private var selectedYear by mutableStateOf(disciplineYear[0])
    var disciplineHalfYear by mutableStateOf(listOf("FIRST", "SECOND"))
        private set
    var selectedHalfYear by mutableStateOf(disciplineHalfYear[0])
        private set
    var disciplineDeprecated by mutableStateOf(listOf("True", "False"))
        private set
    var selectedDeprecated by mutableStateOf(disciplineDeprecated[1])
        private set
    var applyButtonEnabled by mutableStateOf(false)
        private set
    private var teachers by mutableStateOf(emptyList<Teacher>())
    var filteredTeachers by mutableStateOf(teachers)
    var teacherDropDownMenuState by mutableStateOf(false)
        private set
    var selectedTeacher by mutableStateOf(Teacher())
        private set
    var groups by mutableStateOf(emptyList<Group>())
        private set
    var groupsDropDownMenuState by mutableStateOf(false)
        private set
    private var selectedGroup by mutableStateOf(Group())
    var selectedGroupText by mutableStateOf("")
        private set

    @AssistedFactory
    interface Factory {
        fun create(): AddNewDisciplineScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideAddNewDisciplineScreenViewModel(
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
            groups = repository.getGroups().sortedBy { it.name }
            teachers = repository.getTeachers().sortedBy { it.fullName }
            filteredTeachers = teachers
        }
    }

    fun updateNameValue(newNameValue: String) {
        applyButtonEnabled = newNameValue.isNotEmpty()
        disciplineName = newNameValue
    }

    fun updateYearValue(newYearValue: String) {
        selectedYear = newYearValue
    }

    fun updateHalfYearValue(newHalfYearValue: String) {
        selectedHalfYear = newHalfYearValue
    }

    fun updateDeprecatedValue(newDeprecatedValue: String) {
        selectedDeprecated = newDeprecatedValue
    }

    fun beforeSaveButtonClick() {
        viewModelScope.launch {
            repository.postNewDiscipline(
                PostNewDisciplineBody(
                    name = disciplineName,
                    year = selectedYear.toInt(),
                    halfYear = selectedHalfYear,
                    deprecated = selectedDeprecated.toBoolean()
                )
            )
        }
    }

    fun onTeacherDropDownMenuExpandedChange(newExpandedValue: Boolean) {
        teacherDropDownMenuState = newExpandedValue
    }

    fun onTeacherValueChange(newTeacherValue: String) {
        selectedTeacher = Teacher(fullName = newTeacherValue)
        teachers.filter { teacher ->
            selectedTeacher.fullName?.let {
                teacher.fullName?.contains(it, ignoreCase = true)
            } ?: false
        }
    }

    fun onTeacherClick(newTeacher: Teacher) {
        selectedTeacher = newTeacher
        teacherDropDownMenuState = false
    }

    fun onGroupsDropDownMenuExpandedChange(newExpandedValue: Boolean) {
        groupsDropDownMenuState = newExpandedValue
    }

    fun onGroupValueChange(newGroupValue: String) {
        selectedGroup = selectedGroup.copy(name = newGroupValue)
    }

    fun onGroupClick(group: Group) {
        selectedGroupText += ", ${group.name}"
    }
}