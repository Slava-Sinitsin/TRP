package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.data.mappers.teacherappointments.PostNewDisciplineWithInfoBody
import com.example.trp.data.mappers.teacherappointments.PostTeacherAppointmentBody
import com.example.trp.data.mappers.teacherappointments.Teacher
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.AddNewDisciplineTabs
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class AddNewDisciplineScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var disciplineName by mutableStateOf("")
        private set
    var disciplineYear by mutableStateOf((2000..2030).map { it.toString() })
        private set
    private var selectedYear by mutableStateOf(disciplineYear[0])
    var disciplineHalfYear by mutableStateOf(listOf("First", "Second"))
        private set
    var selectedHalfYearIndex by mutableStateOf(0)
        private set
    var disciplineDeprecated by mutableStateOf(listOf("Yes", "No"))
        private set
    var selectedDeprecatedIndex by mutableStateOf(1)
        private set
    var applyButtonEnabled by mutableStateOf(false)
        private set

    var topAppBarText by mutableStateOf("Add new discipline")
        private set
    var selectedTabIndex by mutableStateOf(0)
        private set
    val addNewDisciplineScreens = mutableListOf(
        AddNewDisciplineTabs.MainScreen,
        AddNewDisciplineTabs.SelectScreen
    )
    var teachers by mutableStateOf(emptyList<Teacher>())
        private set
    var groups by mutableStateOf(emptyList<Group>())
        private set
    var selectableGroups by mutableStateOf(emptyList<Pair<Group, Boolean>>())
        private set
    var selectedTeacher by mutableStateOf(Teacher())
        private set
    var errorMessage by mutableStateOf("")
        private set
    var responseSuccess by mutableStateOf(false)
        private set
    var disciplines by mutableStateOf(emptyList<DisciplineData>())
        private set
    var selectedDiscipline by mutableStateOf(DisciplineData())
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
        viewModelScope.launch { init() }
    }

    private suspend fun init() {
        try {
            teachers = repository.getTeachers().sortedBy { it.fullName }
            groups = repository.getGroups().sortedBy { it.name }
            selectableGroups = groups.map { it to false }
            disciplines = repository.getDisciplines()
        } catch (e: SocketTimeoutException) {
            updateErrorMessage("Timeout")
        } catch (e: ConnectException) {
            updateErrorMessage("Check internet connection")
        } catch (e: Exception) {
            updateErrorMessage("Error")
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun updateNameValue(newNameValue: String) {
        disciplineName = newNameValue
        checkFields()
    }

    fun updateYearValue(newYearValue: String) {
        selectedYear = newYearValue
    }

    fun updateHalfYearIndex(newHalfYearValue: Int) {
        selectedHalfYearIndex = newHalfYearValue
    }

    fun updateDeprecatedIndex(newDeprecatedValue: Int) {
        selectedDeprecatedIndex = newDeprecatedValue
    }

    /*repository.postNewDiscipline(
                    PostNewDisciplineBody(
                        name = disciplineName,
                        year = selectedYear.toInt(),
                        halfYear = when (disciplineHalfYear[selectedHalfYearIndex]) {
                            "First" -> "FIRST"
                            "Second" -> "SECOND"
                            else -> null
                        },
                        deprecated = when (disciplineDeprecated[selectedDeprecatedIndex]) {
                            "Yes" -> true
                            "No" -> false
                            else -> null
                        }
                    )
                )*/

    fun beforeSaveButtonClick() {
        responseSuccess = false
        viewModelScope.launch {
            try {
                if (selectedDiscipline == DisciplineData()) {
                    repository.postNewDisciplineWithInfo(
                        PostNewDisciplineWithInfoBody(
                            groupIds = selectableGroups.filter { it.second }
                                .mapNotNull { it.first.id },
                            teacherId = selectedTeacher.id,
                            discipline = DisciplineData(
                                name = disciplineName,
                                year = selectedYear.toInt(),
                                halfYear = when (disciplineHalfYear[selectedHalfYearIndex]) {
                                    "First" -> "FIRST"
                                    "Second" -> "SECOND"
                                    else -> null
                                },
                                deprecated = when (disciplineDeprecated[selectedDeprecatedIndex]) {
                                    "Yes" -> true
                                    "No" -> false
                                    else -> null
                                }
                            )
                        )
                    )
                } else {
                    selectableGroups.filter { it.second }.forEach { group ->
                        repository.postNewTeacherAppointment(
                            PostTeacherAppointmentBody(
                                groupId = group.first.id,
                                teacherId = selectedTeacher.id,
                                disciplineId = selectedDiscipline.id
                            )
                        )
                    }
                }
                responseSuccess = true
            } catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }

    fun setPagerState(index: Int) {
        selectedTabIndex = index
        when (selectedTabIndex) {
            0 -> {
                topAppBarText = "Add new discipline"
            }

            1 -> {
                topAppBarText = "Select discipline"
            }

            2 -> {
                topAppBarText = "Select teacher"
            }

            3 -> {
                topAppBarText = "Select groups"
            }
        }
    }

    fun getTeacher(index: Int): Teacher {
        return teachers[index]
    }

    fun getGroup(index: Int): Group {
        return groups[index]
    }

    fun onTeacherClick(index: Int) {
        selectedTeacher = teachers[index]
        checkFields()
        selectedTabIndex = 0
    }

    fun onDisciplineClick(index: Int) {
        selectedDiscipline = disciplines[index]
        checkFields()
        selectedTabIndex = 0
    }

    fun onDeleteGroupClick(index: Int) {
        selectableGroups = selectableGroups.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                item.copy(second = false)
            } else {
                item
            }
        }
        checkFields()
    }

    fun onGroupClick(index: Int) {
        selectableGroups = selectableGroups.mapIndexed { currentIndex, item ->
            if (currentIndex == index) {
                item.copy(second = !item.second)
            } else {
                item
            }
        }
        checkFields()
    }

    private fun checkFields() {
        applyButtonEnabled = (disciplineName.isNotBlank() || selectedDiscipline != DisciplineData())
                && selectedTeacher != Teacher()
                && selectableGroups.any { it.second }
    }
}