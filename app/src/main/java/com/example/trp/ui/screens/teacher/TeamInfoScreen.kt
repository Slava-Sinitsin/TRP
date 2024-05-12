package com.example.trp.ui.screens.teacher

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.TaskStatus
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeamInfoScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamInfoScreen(
    teamId: Int,
    groupId: Int,
    onAddTaskToTeamClick: (teamId: Int, labId: Int) -> Unit,
    onTaskClick: (teamAppointmentId: Int) -> Unit,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).teamInfoScreenViewModelFactory()
    val viewModel: TeamInfoScreenViewModel = viewModel(
        factory = TeamInfoScreenViewModel.provideTeamInfoScreenViewModel(
            factory,
            teamId,
            groupId
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            StudentInfoCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Tasks(
            viewModel = viewModel,
            paddingValues = scaffoldPadding,
            onAddTaskToTeamClick = onAddTaskToTeamClick,
            onTaskClick = onTaskClick
        )
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentInfoCenterAlignedTopAppBar(
    viewModel: TeamInfoScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            viewModel.team.students?.map { it.fullName?.split(" ")?.firstOrNull() }
                ?.let { Text(text = it.joinToString()) }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "BackIconButton"
                )
            }
        },
        actions = { }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Tasks(
    viewModel: TeamInfoScreenViewModel,
    paddingValues: PaddingValues,
    onAddTaskToTeamClick: (teamId: Int, labId: Int) -> Unit,
    onTaskClick: (teamAppointmentId: Int) -> Unit
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
            items(viewModel.labs.size) { index ->
                Task(
                    viewModel = viewModel,
                    index = index,
                    onTaskClick = onTaskClick,
                    onAddTaskToTeamClick = onAddTaskToTeamClick
                )
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
fun Task(
    viewModel: TeamInfoScreenViewModel,
    index: Int,
    onTaskClick: (teamAppointmentId: Int) -> Unit,
    onAddTaskToTeamClick: (teamId: Int, labId: Int) -> Unit
) {
    Button(
        modifier = Modifier.padding(8.dp),
        onClick = {
            viewModel.teamAppointments.find { it.task?.labWorkId == viewModel.labs[index].id }
                ?.let { teamAppointment ->
                    teamAppointment.id?.let { onTaskClick(it) }
                } ?: run {
                viewModel.labs[index].id?.let { onAddTaskToTeamClick(viewModel.teamId, it) }
            }
        },
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            viewModel.teamAppointments.find { it.task?.labWorkId == viewModel.labs[index].id }
                ?.let { teamAppointment ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.alpha(0.6f),
                            text = "${viewModel.labs[index].title}",
                            color = TRPTheme.colors.primaryText
                        )
                        Text(
                            text = teamAppointment.task?.title.toString(),
                            color = TRPTheme.colors.primaryText,
                            fontSize = 25.sp
                        )
                    }
                    val status = teamAppointment.id?.let { viewModel.getStatus(it) }
                        ?: TaskStatus.New
                    if (status == TaskStatus.Rated) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Task is graded",
                                tint = TRPTheme.colors.okColor
                            )
                            Spacer(modifier = Modifier.size(5.dp))
                            Text(
                                text =
                                teamAppointment.ratings?.joinToString(separator = ", ") { rating ->
                                    rating.grade.toString()
                                } ?: "",
                                fontSize = 15.sp,
                                color = TRPTheme.colors.primaryText,
                            )
                        }
                    } else {
                        CircularProgressIndicator(
                            progress = status.progress,
                            color = status.color
                        )
                    }
                } ?: run {
                Column {
                    Text(modifier = Modifier.alpha(0.6f), text = "${viewModel.labs[index].title}")
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Not appoint",
                        color = TRPTheme.colors.primaryText,
                        fontSize = 25.sp
                    )
                }
            }
        }
    }
}