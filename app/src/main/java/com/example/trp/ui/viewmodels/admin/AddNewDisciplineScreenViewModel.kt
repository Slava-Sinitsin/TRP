package com.example.trp.ui.viewmodels.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.disciplines.PostNewDisciplineBody
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AddNewDisciplineScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var disciplineName by mutableStateOf("")
    var disciplineYear by mutableStateOf((2000..2030).map { it.toString() })
    private var selectedYear by mutableStateOf(disciplineYear[0])
    var disciplineHalfYear by mutableStateOf(listOf("FIRST", "SECOND"))
    var selectedHalfYear by mutableStateOf(disciplineHalfYear[0])
    var disciplineDeprecated by mutableStateOf(listOf("True", "False"))
    var selectedDeprecated by mutableStateOf(disciplineDeprecated[1])
    var applyButtonEnabled by mutableStateOf(false)

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
}