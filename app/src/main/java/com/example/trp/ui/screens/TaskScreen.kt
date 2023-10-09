package com.example.trp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.TaskScreenViewModel

@Suppress("UNCHECKED_CAST")
@Composable
fun TaskScreen(taskId: Int) {
    val viewModel = viewModel<TaskScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TaskScreenViewModel(taskId = taskId) as T
            }
        }
    )
    TaskDesc(viewModel = viewModel)
}

@Composable
fun TaskDesc(viewModel: TaskScreenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "${viewModel.taskDisciplineData.name ?: ""}\n${viewModel.task.taskDesc?.description ?: ""}",
            Modifier.align(Alignment.Center),
            fontSize = 40.sp,
            color = TRPTheme.colors.primaryText
        )
    }
}