package com.example.trp.ui.screens.teacher

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.AddTaskToTeamScreenViewModel
import com.kosher9.roundcheckbox.RoundCheckBox
import com.kosher9.roundcheckbox.RoundCheckBoxDefaults
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskToTeamScreen(
    teamId: Int,
    labId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).addTaskToTeamScreenViewModelFactory()
    val viewModel: AddTaskToTeamScreenViewModel = viewModel(
        factory = AddTaskToTeamScreenViewModel.provideAddTaskToTeamScreenViewModel(
            factory,
            teamId,
            labId
        )
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AddTaskToStudentTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Tasks(viewModel = viewModel, paddingValues = scaffoldPadding)
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskToStudentTopAppBar(
    viewModel: AddTaskToTeamScreenViewModel,
    navController: NavHostController
) {
    LaunchedEffect(viewModel.responseSuccess) {
        if (viewModel.responseSuccess) {
            navController.popBackStack()
        }
    }
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = { Text(text = "Appoint new lab") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "BackIconButton"
                )
            }
        },
        actions = {
            IconButton(
                onClick = { viewModel.beforeApplyButtonClick() },
                enabled = viewModel.tasksCheckBoxStates.any { it.isSelected }
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "ApplyAddTasksToStudentButton",
                )
            }
        }
    )
}

@Composable
fun Tasks(
    viewModel: AddTaskToTeamScreenViewModel,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        items(count = viewModel.tasks.size) { index ->
            Task(viewModel = viewModel, index = index)
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun Task(
    viewModel: AddTaskToTeamScreenViewModel,
    index: Int
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { viewModel.onCheckBoxClick(index) },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor,
            disabledContainerColor = TRPTheme.colors.cardButtonColor.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(30.dp),
        enabled = viewModel.tasksCheckBoxStates[index].isEnable
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                textAlign = TextAlign.Start,
                text = viewModel.getTask(index = index).title.toString(),
                color = TRPTheme.colors.primaryText,
                fontSize = 20.sp
            )
            Box(
                modifier = Modifier.alpha(viewModel.tasksCheckBoxStates[index].enableAlpha)
            ) {
                RoundCheckBox(
                    modifier = Modifier.alpha(
                        if (viewModel.tasksCheckBoxStates[index].isEnable) {
                            1f
                        } else {
                            0.6f
                        }
                    ),
                    isChecked = viewModel.tasksCheckBoxStates[index].isSelected,
                    onClick = { viewModel.onCheckBoxClick(index) },
                    color = RoundCheckBoxDefaults.colors(
                        selectedColor = TRPTheme.colors.myYellow,
                        borderColor = TRPTheme.colors.myYellow,
                        tickColor = TRPTheme.colors.primaryText,
                    ),
                    enabled = viewModel.tasksCheckBoxStates[index].isEnable
                )
            }
        }
    }
}