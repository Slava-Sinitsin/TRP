package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeacherTasksScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTasksScreen(
    labId: Int,
    onTaskClick: (id: Int) -> Unit,
    onCreateTaskClick: (disciplineId: Int) -> Unit
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).teacherTasksScreenViewModelFactory()
    val viewModel: TeacherTasksScreenViewModel = viewModel(
        factory = TeacherTasksScreenViewModel.provideTeacherTasksScreenViewModel(
            factory,
            labId
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { }
    ) { scaffoldPadding ->
        Tasks(
            viewModel = viewModel,
            onCreateTaskClick = onCreateTaskClick,
            onTaskClick = onTaskClick,
            paddingValues = scaffoldPadding
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Tasks(
    viewModel: TeacherTasksScreenViewModel,
    onCreateTaskClick: (disciplineId: Int) -> Unit,
    onTaskClick: (taskId: Int) -> Unit,
    paddingValues: PaddingValues,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
            .padding(top = paddingValues.calculateTopPadding())
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                CreateTaskToDiscipline(
                    viewModel = viewModel,
                    onCreateTaskClick = onCreateTaskClick
                )
            }
            items(count = viewModel.tasks.size) { index ->
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
}

@Composable
fun CreateTaskToDiscipline(
    viewModel: TeacherTasksScreenViewModel,
    onCreateTaskClick: (disciplineId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onCreateTaskClick(viewModel.labId) },
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
                .alpha(0.6f),
            text = "+",
            color = TRPTheme.colors.primaryText,
            fontSize = 45.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Task(
    viewModel: TeacherTasksScreenViewModel,
    index: Int,
    onTaskClick: (taskId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            viewModel.getTask(index = index)
                .let { task -> task.id?.let { taskId -> onTaskClick(taskId) } }
        },
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