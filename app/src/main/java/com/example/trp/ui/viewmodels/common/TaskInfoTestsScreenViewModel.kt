package com.example.trp.ui.viewmodels.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.trp.data.mappers.CheckBoxState
import com.example.trp.data.mappers.tasks.Task
import com.example.trp.data.mappers.tasks.Test
import com.example.trp.data.repository.UserAPIRepositoryImpl
import com.example.trp.ui.components.tabs.TaskTestsTabs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class TaskInfoTestsScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val taskId: Int
) : ViewModel() {
    var task by mutableStateOf(Task())
        private set
    var taskTitle by mutableStateOf("")
        private set
    var taskDescription by mutableStateOf("")
        private set
    var taskFunctionName by mutableStateOf("")
        private set
    var taskLanguage by mutableStateOf("")
        private set
    var taskReadOnlyMode by mutableStateOf(Pair(true, 0.6f))
        private set
    var testReadOnlyMode by mutableStateOf(true)
        private set
    var applyButtonEnabled by mutableStateOf(true)
        private set
    var showDeleteDialog by mutableStateOf(false)
        private set

    val taskTestsScreens = listOf(
        TaskTestsTabs.TaskInfo,
        TaskTestsTabs.Tests
    )

    var selectedTabIndex by mutableStateOf(0)
        private set

    var tests by mutableStateOf(emptyList<Test>())
        private set

    var testsCheckBoxStates by mutableStateOf(emptyList<CheckBoxState>())
        private set

    var isTestChanged by mutableStateOf(false)
        private set

    var isRefreshing by mutableStateOf(false)
        private set

    @AssistedFactory
    interface Factory {
        fun create(studentId: Int): TaskInfoTestsScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTaskInfoTestsScreenViewModel(
            factory: Factory,
            studentId: Int
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(studentId) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            task = repository.tasks.find { it.id == taskId } ?: Task()
            taskTitle = task.title ?: ""
            taskDescription = task.description ?: ""
            taskFunctionName = task.functionName ?: ""
            taskLanguage = task.language ?: ""
            tests = repository.getTests(taskId).sortedByDescending { it.id }
            testsCheckBoxStates = List(tests.size) { CheckBoxState() }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing = true
            task = repository.tasks.find { it.id == taskId } ?: Task()
            taskTitle = task.title ?: ""
            taskDescription = task.description ?: ""
            taskFunctionName = task.functionName ?: ""
            taskLanguage = task.language ?: ""
            tests = repository.getTests(taskId)
            testsCheckBoxStates = List(tests.size) { CheckBoxState() }
            isRefreshing = false
        }
    }

    fun updateTitleValue(newTitleValue: String) {
        applyButtonEnabled = newTitleValue.isNotEmpty()
        taskTitle = newTitleValue
    }

    fun updateDescriptionValue(newDescriptionValue: String) {
        applyButtonEnabled = newDescriptionValue.isNotEmpty()
        taskDescription = newDescriptionValue
    }

    fun updateFunctionNameValue(newFunctionNameValue: String) {
        applyButtonEnabled = newFunctionNameValue.isNotEmpty()
        taskFunctionName = newFunctionNameValue
    }

    fun updateLanguageValue(newLanguageValue: String) {
        applyButtonEnabled = newLanguageValue.isNotEmpty()
        taskLanguage = newLanguageValue
    }

    fun onEditButtonClick() {
        taskReadOnlyMode = taskReadOnlyMode.copy(first = false, second = 1f)
    }

    fun onSaveButtonClick() {
        viewModelScope.launch {
            repository.putTask(
                Task(
                    id = task.id,
                    labWorkId = task.labWorkId,
                    title = taskTitle,
                    description = taskDescription,
                    functionName = taskFunctionName,
                    language = taskLanguage
                )
            )
            task = repository.getTask(taskId)
            taskReadOnlyMode = taskReadOnlyMode.copy(first = true, second = 0.6f)
        }
    }

    fun onTaskRollBackIconButtonClick() {
        taskTitle = task.title ?: ""
        taskDescription = task.description ?: ""
        taskFunctionName = task.functionName ?: ""
        taskLanguage = task.language ?: ""
        taskReadOnlyMode = taskReadOnlyMode.copy(first = true, second = 0.6f)
    }

    fun onDeleteButtonClick() {
        showDeleteDialog = true
    }

    fun beforeConfirmButtonClick() {
        viewModelScope.launch { task.id?.let { repository.deleteTask(it) } }
        showDeleteDialog = false
    }

    fun onDismissButtonClick() {
        showDeleteDialog = false
    }

    fun setPagerState(index: Int) {
        selectedTabIndex = index
    }

    fun getTest(index: Int): Test {
        return tests[index]
    }

    fun onLongTestClick() {
        testReadOnlyMode = false
    }

    fun onCheckBoxClick(index: Int) {
        testsCheckBoxStates = testsCheckBoxStates.toMutableList().also {
            it[index] = CheckBoxState(isSelected = !it[index].isSelected)
        }
        isTestChanged = checkAllCheckBoxStates()
    }

    private fun checkAllCheckBoxStates(): Boolean {
        testsCheckBoxStates.forEach {
            if (it.isSelected) {
                return true
            }
        }
        return false
    }

    fun onDeleteTestsButtonClick() { // TODO

    }

    fun onTestRollBackIconButtonClick() {
        testReadOnlyMode = true
        testsCheckBoxStates = List(tests.size) { CheckBoxState() }
    }

    fun onSystemBackButtonClick() {
        if (!taskReadOnlyMode.first) {
            taskReadOnlyMode = taskReadOnlyMode.copy(first = true, second = 0.6f)
        } else if (!testReadOnlyMode) {
            testReadOnlyMode = true
        }
    }
}