package com.example.trp.ui.screens.teacher

import android.app.Activity
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
import androidx.compose.material.icons.filled.Close
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
import com.example.trp.ui.viewmodels.teacher.AddTaskToStudentScreenViewModel
import com.kosher9.roundcheckbox.RoundCheckBox
import com.kosher9.roundcheckbox.RoundCheckBoxDefaults
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskToStudentScreen(
    studentId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).addTaskToStudentScreenViewModelFactory()
    val viewModel: AddTaskToStudentScreenViewModel = viewModel(
        factory = AddTaskToStudentScreenViewModel.provideAddTaskToStudentScreenViewModel(
            factory,
            studentId
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            AddTaskToStudentCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Tasks(viewModel = viewModel, paddingValues = scaffoldPadding)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskToStudentCenterAlignedTopAppBar(
    viewModel: AddTaskToStudentScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = { Text(text = viewModel.student.fullName ?: "") },
        navigationIcon = {
            if (!viewModel.isTaskChanged) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "BackIconButton"
                    )
                }
            } else {
                IconButton(onClick = { viewModel.onRollBackIconButtonClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "RollBackIconButton"
                    )
                }
            }
        },
        actions = {
            if (viewModel.isTaskChanged) {
                IconButton(
                    onClick = { viewModel.onApplyButtonClick() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "ApplyAddTasksToStudentButton",
                    )
                }
            }
        }
    )
}

@Composable
fun Tasks(
    viewModel: AddTaskToStudentScreenViewModel,
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
    viewModel: AddTaskToStudentScreenViewModel,
    index: Int
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp)
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
                fontSize = 25.sp
            )
            Box(
                modifier = Modifier.alpha(viewModel.tasksCheckBoxStates[index].enableAlpha)
            ) {
                RoundCheckBox(
                    isChecked = viewModel.tasksCheckBoxStates[index].isSelected,
                    onClick = { viewModel.onCheckBoxClick(index) },
                    color = RoundCheckBoxDefaults.colors(
                        selectedColor = TRPTheme.colors.myYellow,
                        borderColor = TRPTheme.colors.myYellow,
                        tickColor = TRPTheme.colors.primaryText,
                        disabledSelectedColor = TRPTheme.colors.myYellow
                    ),
                    enabled = viewModel.tasksCheckBoxStates[index].isEnable
                )
            }
        }
    }
}