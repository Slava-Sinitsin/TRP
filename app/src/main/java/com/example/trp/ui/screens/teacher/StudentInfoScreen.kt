package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.trp.ui.viewmodels.teacher.StudentInfoScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentInfoScreen(
    studentId: Int,
    onAddTaskToStudentClick: (id: Int) -> Unit,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).studentInfoScreenViewModelFactory()
    val viewModel: StudentInfoScreenViewModel = viewModel(
        factory = StudentInfoScreenViewModel.provideStudentInfoScreenViewModel(
            factory,
            studentId
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
            onAddTaskToStudentClick = onAddTaskToStudentClick
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentInfoCenterAlignedTopAppBar(
    viewModel: StudentInfoScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = { Text(text = viewModel.student.fullName ?: "") },
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

@Composable
fun Tasks(
    viewModel: StudentInfoScreenViewModel,
    paddingValues: PaddingValues,
    onAddTaskToStudentClick: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        item { AddTask(viewModel = viewModel, onAddTaskToStudentClick = onAddTaskToStudentClick) }
        items(count = viewModel.studentAppointments.size) { index ->
            Task(viewModel = viewModel, index = index)
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun Task(
    viewModel: StudentInfoScreenViewModel,
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
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Start,
            text = viewModel.getTask(index).title.toString() + " ("
                    + viewModel.getStudentAppointment(index).status + ")",
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}

@Composable
fun AddTask(
    viewModel: StudentInfoScreenViewModel,
    onAddTaskToStudentClick: (id: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onAddTaskToStudentClick(viewModel.studentId) },
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