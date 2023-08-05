package com.example.trp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.TasksScreenViewModel

@Suppress("UNCHECKED_CAST")
@Composable
fun TasksScreen(
    disciplineId: Int,
    onTaskClick: (id: Int) -> Unit,
) {

    val viewModel = viewModel<TasksScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TasksScreenViewModel(
                    disciplineId = disciplineId,
                    onTaskClick = onTaskClick
                ) as T
            }
        }
    )

    Tasks(
        viewModel = viewModel
    )
}

@Composable
fun Tasks(
    viewModel: TasksScreenViewModel
) {
    LazyColumn {
        items(viewModel.disciplineId) { index ->
            Task(viewModel = viewModel, index = index)
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun Task(
    viewModel: TasksScreenViewModel,
    index: Int
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                viewModel.onTaskClick(index)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = TRPTheme.colors.cardColor
        )
    ) {
        Text(
            text = "Task ${index + 1}",
            modifier = Modifier.padding(16.dp),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}