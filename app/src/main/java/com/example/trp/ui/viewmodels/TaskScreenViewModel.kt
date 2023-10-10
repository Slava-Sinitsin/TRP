package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.data.disciplines.DisciplineData
import com.example.trp.data.disciplines.DisciplineResponse
import com.example.trp.data.tasks.TaskDesc
import com.example.trp.network.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response

class TaskScreenViewModel(
    taskId: Int
) : ViewModel() {
    var task by mutableStateOf(TaskDesc())
    var taskDisciplineData by mutableStateOf(DisciplineData())
    var taskText by mutableStateOf("")

    init {
        viewModelScope.launch {
            task = getTask(taskId)
            taskDisciplineData = getTaskDiscipline(task.taskDesc?.disciplineId)
        }
    }

    private suspend fun getTask(taskId: Int): TaskDesc {
        val user = UserDataManager.getUser().first()
        val response: Response<TaskDesc> =
            ApiService.userAPI.getTaskDesc("Bearer " + user.token, taskId)
        return response.body() ?: TaskDesc()
    }

    private suspend fun getTaskDiscipline(disciplineId: Int?): DisciplineData {
        val user = UserDataManager.getUser().first()
        val response: Response<DisciplineResponse> =
            ApiService.userAPI.getDisciplineByID("Bearer " + user.token, disciplineId ?: -1)
        return response.body()?.disciplineData ?: DisciplineData()
    }

    fun updateTaskText(newTaskText: String) {
        taskText = newTaskText
    }

    fun onRunCodeButtonClick () {
        // TODO
    }
}