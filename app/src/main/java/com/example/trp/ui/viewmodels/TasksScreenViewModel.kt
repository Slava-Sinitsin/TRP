package com.example.trp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.data.network.ApiService
import com.example.trp.data.tasks.Task
import com.example.trp.data.tasks.Tasks
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Response

class TasksScreenViewModel(
    val disciplineId: Int,
    var onTaskClick: (id: Int) -> Unit
) : ViewModel() {

    var tasks: Tasks? = Tasks()
        private set

    init {
        viewModelScope.launch {
            tasks = getTasks(disciplineId = disciplineId)
        }
    }

    private suspend fun getTasks(disciplineId: Int): Tasks? {
        val user = UserDataManager.getUser().first()
        val response: Response<Tasks> =
            ApiService.userAPI.getTasks("Bearer " + user.token, disciplineId)
        return response.body()
    }

    fun getTask(index: Int): Task {
        return tasks?.data?.get(index) ?: Task()
    }

    fun navigateToTask(index: Int) {
        getTask(index = index).let { task -> task.id?.let { id -> onTaskClick(id) } }
    }
}