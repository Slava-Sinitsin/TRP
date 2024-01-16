package com.example.trp.ui.screens.student

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.student.TasksScreenViewModel

@Suppress("UNCHECKED_CAST")
@Composable
fun TasksScreen(
    disciplineId: Int,
    onGroupClick: (id: Int) -> Unit,
) {
    val viewModel = viewModel<TasksScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TasksScreenViewModel(
                    disciplineId = disciplineId,
                    onTaskClick = onGroupClick
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
        items(count = viewModel.tasks.size) { index ->
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
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { viewModel.navigateToTask(index = index) },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Start,
            text = viewModel.getTask(index = index).title.toString(),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}