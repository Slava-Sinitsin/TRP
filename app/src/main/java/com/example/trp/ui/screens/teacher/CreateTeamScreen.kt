package com.example.trp.ui.screens.teacher

import android.app.Activity
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.trp.ui.viewmodels.teacher.CreateTeamScreenViewModel
import com.kosher9.roundcheckbox.RoundCheckBox
import com.kosher9.roundcheckbox.RoundCheckBoxDefaults
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    groupId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).createTeamScreenViewModelFactory()
    val viewModel: CreateTeamScreenViewModel = viewModel(
        factory = CreateTeamScreenViewModel.provideCreateTeamScreenViewModel(
            factory,
            groupId
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CreateTeamCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        SelectField(viewModel = viewModel, paddingValues = scaffoldPadding)
        if (viewModel.showSelectLeaderDialog) {
            SelectLeaderDialog(viewModel = viewModel, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamCenterAlignedTopAppBar(
    viewModel: CreateTeamScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = { Text(text = "Create new teams") },
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
                onClick = {
                    if (viewModel.selectedStudents.size == 1) {
                        viewModel.onConfirmDialogButtonClick()
                        navController.popBackStack()
                    } else {
                        viewModel.onAddButtonClick()
                    }
                },
                enabled = viewModel.selectedStudents.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "AddButton",
                )
            }
        }
    )
}

@Composable
fun SelectField(
    viewModel: CreateTeamScreenViewModel,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(TRPTheme.colors.primaryBackground)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = viewModel.students.size) { index ->
                Student(viewModel = viewModel, index = index)
            }
            item { Spacer(modifier = Modifier.size(100.dp)) }
        }
    }
}

@Composable
fun Student(
    viewModel: CreateTeamScreenViewModel,
    index: Int
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .alpha(if (viewModel.studentsCheckBoxStates[index].isEnable) 1.0f else 0.6f),
        onClick = { viewModel.onStudentClick(index) },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor,
            disabledContainerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp),
        enabled = viewModel.studentsCheckBoxStates[index].isEnable
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 16.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Start,
                text = viewModel.getStudent(index = index).fullName.toString(),
                color = TRPTheme.colors.primaryText,
                fontSize = 25.sp
            )
            RoundCheckBox(
                isChecked = viewModel.studentsCheckBoxStates[index].isSelected,
                onClick = { viewModel.onStudentClick(index) },
                color = RoundCheckBoxDefaults.colors(
                    selectedColor = TRPTheme.colors.myYellow,
                    borderColor = TRPTheme.colors.myYellow,
                    tickColor = TRPTheme.colors.primaryText,
                    disabledSelectedColor = TRPTheme.colors.myYellow
                ),
                enabled = viewModel.studentsCheckBoxStates[index].isEnable
            )
        }
    }
}

@Composable
fun SelectLeaderDialog(viewModel: CreateTeamScreenViewModel, navController: NavHostController) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = "Select group leader",
                color = TRPTheme.colors.primaryText
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
        text = {
            Column {
                viewModel.selectedStudents.forEachIndexed { index, _ ->
                    DialogStudent(viewModel = viewModel, index = index)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.onConfirmDialogButtonClick()
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.myYellow)
            ) {
                Text(
                    text = "Confirm",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { viewModel.onDismissButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.errorColor)
            ) {
                Text(
                    text = "Dismiss",
                    color = TRPTheme.colors.secondaryText
                )
            }
        }
    )
}

@Composable
fun DialogStudent(
    viewModel: CreateTeamScreenViewModel,
    index: Int
) {
    Button(
        modifier = Modifier.padding(top = 5.dp),
        onClick = { viewModel.onDialogStudentButtonClick(index) },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = viewModel.selectedStudents[index].fullName ?: "",
                color = TRPTheme.colors.primaryText,
                fontSize = 16.sp
            )
            RadioButton(
                selected = viewModel.selectedGroupLeaderIndex == index,
                onClick = { viewModel.onDialogStudentButtonClick(index) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = TRPTheme.colors.myYellow
                )
            )
        }

    }
}