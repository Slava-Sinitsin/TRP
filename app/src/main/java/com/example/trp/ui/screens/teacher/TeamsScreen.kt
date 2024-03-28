package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeamsScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    groupId: Int,
    onTeamClick: (id: Int) -> Unit,
    onCreateTeamClick: (groupId: Int) -> Unit,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).studentsScreenViewModelFactory()
    val viewModel: TeamsScreenViewModel = viewModel(
        factory = TeamsScreenViewModel.provideTeamsScreenViewModel(
            factory,
            groupId
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TeamsTopBar(
                viewModel = viewModel,
                onCreateTeamClick = onCreateTeamClick,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Teams(
            viewModel = viewModel,
            paddingValues = scaffoldPadding,
            onTeamClick = onTeamClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsTopBar(
    viewModel: TeamsScreenViewModel,
    onCreateTeamClick: (groupId: Int) -> Unit,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = { Text(text = viewModel.group.name ?: "") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "BackIconButton"
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = { viewModel.onMenuButtonClick() }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "MenuButton"
                    )
                }

            }
            DropdownMenu(
                modifier = Modifier
                    .background(TRPTheme.colors.primaryBackground),
                expanded = viewModel.isMenuShow,
                onDismissRequest = { viewModel.onDismissRequest() }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Create teams",
                            color = TRPTheme.colors.primaryText
                        )
                    },
                    onClick = {
                        viewModel.beforeCreateTeamClick()
                        onCreateTeamClick(viewModel.groupId)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Appoint tasks to everyone in group",
                            color = TRPTheme.colors.primaryText
                        )
                    },
                    onClick = { viewModel.onEveryoneAppointButtonClick() }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Teams(
    viewModel: TeamsScreenViewModel,
    paddingValues: PaddingValues,
    onTeamClick: (id: Int) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
        ) {
            items(count = viewModel.showTeams.size) { index ->
                Team(viewModel = viewModel, index = index, onTeamClick = onTeamClick)
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
fun Team(
    viewModel: TeamsScreenViewModel,
    index: Int,
    onTeamClick: (id: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            viewModel.getTeam(index = index).let { team -> team.id?.let { id -> onTeamClick(id) } }
        },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val students = viewModel.getTeam(index).students
            Column(
                modifier = Modifier
                    .padding(
                        vertical = if (students?.size == 1)
                            19.dp
                        else
                            6.dp
                    )
            ) {
                students?.forEach { student ->
                    Text(
                        text = viewModel.students.find { it.id == student.id }?.fullName ?: "",
                        color = TRPTheme.colors.primaryText,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}