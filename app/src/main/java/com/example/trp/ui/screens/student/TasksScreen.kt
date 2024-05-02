package com.example.trp.ui.screens.student

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.TaskStatus
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.student.TasksScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TasksScreen(
    disciplineId: Int,
    onTaskClick: (id: Int) -> Unit,
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).tasksScreenViewModelFactory()
    val viewModel: TasksScreenViewModel = viewModel(
        factory = TasksScreenViewModel.provideTasksScreenViewModel(
            factory,
            disciplineId
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { scaffoldPadding ->
        val pullRefreshState = rememberPullRefreshState(
            refreshing = viewModel.isRefreshing,
            onRefresh = { viewModel.onRefresh() }
        )
        Box(
            modifier = Modifier
                .padding(top = scaffoldPadding.calculateTopPadding())
                .background(TRPTheme.colors.primaryBackground)
                .pullRefresh(state = pullRefreshState)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = viewModel.labs.size) { index ->
                    Task(viewModel = viewModel, index = index, onTaskClick = onTaskClick)
                }
                item { Spacer(modifier = Modifier.size(100.dp)) }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = viewModel.isRefreshing,
                state = pullRefreshState,
                backgroundColor = TRPTheme.colors.primaryBackground,
                contentColor = TRPTheme.colors.myYellow
            )
        }
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
        }
    }
}

@Composable
fun Task(
    viewModel: TasksScreenViewModel,
    index: Int,
    onTaskClick: (id: Int) -> Unit
) {
    Button(
        modifier = Modifier.padding(8.dp),
        onClick = {
            if (index < viewModel.teamAppointments.size) {
                viewModel.teamAppointments[index].let { teamAppointment ->
                    teamAppointment.task?.id?.let { id -> onTaskClick(id) }
                }
            }
        },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(modifier = Modifier.alpha(0.6f), text = "Lab ${index + 1}")
                if (index < viewModel.teamAppointments.size) {
                    Text(
                        text = viewModel.teamAppointments[index].task?.title.toString(),
                        color = TRPTheme.colors.primaryText,
                        fontSize = 25.sp
                    )
                } else {
                    Text(
                        text = "Not appoint",
                        color = TRPTheme.colors.primaryText,
                        fontSize = 25.sp
                    )
                }
            }
            if (index < viewModel.teamAppointments.size) {
                val status = viewModel.getStatus(index)
                if (status == TaskStatus.Rated) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Task is graded",
                            tint = TRPTheme.colors.okColor
                        )
                        Text(text = "${viewModel.teamAppointments[index].grade}")
                    }
                } else {
                    CircularProgressIndicator(
                        progress = status.progress,
                        color = status.color
                    )
                }
            }
        }
    }
}