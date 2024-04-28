package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.tasks.Lab
import com.example.trp.data.repository.UserAPIRepositoryImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class CreateLabViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val disciplineId: Int
) : ViewModel() {
    var title by mutableStateOf("")
    var ratingList by mutableStateOf((0..150).map { it.toString() })
        private set
    var maxRating by mutableStateOf(ratingList[100])
        private set
    var errorMessage by mutableStateOf("")
        private set
    var responseSuccess by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(
            groupId: Int
        ): CreateLabViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideCreateLabViewModel(
            factory: Factory,
            disciplineId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(disciplineId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {

        }
    }

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateRatingValue(newMaxRating: String) {
        maxRating = newMaxRating
    }

    fun onApplyButtonClick() {
        responseSuccess = false
        viewModelScope.launch {
            try {
                repository.postNewLab(
                    Lab(
                        disciplineId = disciplineId,
                        title = title,
                        maxRating = maxRating.toInt()
                    )
                )
                responseSuccess = true
            }catch (e: SocketTimeoutException) {
                updateErrorMessage("Timeout")
            } catch (e: ConnectException) {
                updateErrorMessage("Check internet connection")
            } catch (e: Exception) {
                updateErrorMessage("Error")
            }
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }
}