package com.example.trp.ui.viewmodels.teacher

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.TeamAppointment
import com.example.trp.data.mappers.teacherappointments.TeacherAppointmentsData
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.TaskStatus
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.time.ZonedDateTime

class TeacherHomeScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl
) : ViewModel() {
    var isRefreshing by mutableStateOf(false)
        private set
    var teamAppointments by mutableStateOf(emptyList<TeamAppointment>())
        private set
    var errorMessage by mutableStateOf("")
        private set
    private var teacherAppointments by mutableStateOf(emptyList<TeacherAppointmentsData>())

    @AssistedFactory
    interface Factory {
        fun create(): TeacherHomeScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherHomeScreenViewModel(
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
            teacherAppointments = repository.getTeacherAppointments()
            teamAppointments = teacherAppointments.flatMap { teacherAppointment ->
                val disciplineId = teacherAppointment.discipline?.id
                val groupId = teacherAppointment.group?.id
                if (disciplineId != null && groupId != null) {
                    repository.getAllTeamAppointments(disciplineId, groupId).filter {
                        it.status == TaskStatus.SentToCodeReview.status
                                || it.status == TaskStatus.CodeReview.status
                                || it.status == TaskStatus.WaitingForGrade.status
                    }.map { teamAppointment ->
                        if (teamAppointment.codeReviewIds != null) {
                            teamAppointment.copy(codeReviews = teamAppointment.codeReviewIds
                                .map { codeReviewId ->
                                    repository.getCodeReview(codeReviewId)
                                })
                        } else {
                            teamAppointment
                        }
                    }
                } else {
                    emptyList()
                }
            }.sortedBy { teamAppointment ->
                teamAppointment.codeReviews?.minOfOrNull { codeReview ->
                    ZonedDateTime.parse(codeReview.createdAt).toInstant()
                }
            }
            Log.e("teamAppointments", teamAppointments.toString())
        } catch (e: SocketTimeoutException) {
            updateErrorMessage("Timeout")
        } catch (e: ConnectException) {
            updateErrorMessage("Check internet connection")
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
            updateErrorMessage("Error")
        }
    }

    fun updateErrorMessage(newMessage: String) {
        errorMessage = newMessage
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            init()
            isRefreshing = false
        }
    }

    fun getStatus(id: Int): TaskStatus {
        return when (teamAppointments.find { it.id == id }?.status) {
            TaskStatus.New.status -> TaskStatus.New
            TaskStatus.InProgress.status -> TaskStatus.InProgress
            TaskStatus.OnTesting.status -> TaskStatus.OnTesting
            TaskStatus.Tested.status -> TaskStatus.Tested
            TaskStatus.SentToCodeReview.status -> TaskStatus.SentToCodeReview
            TaskStatus.CodeReview.status -> TaskStatus.CodeReview
            TaskStatus.SentToRework.status -> TaskStatus.SentToRework
            TaskStatus.WaitingForGrade.status -> TaskStatus.WaitingForGrade
            TaskStatus.Rated.status -> TaskStatus.Rated
            else -> TaskStatus.New
        }
    }
}