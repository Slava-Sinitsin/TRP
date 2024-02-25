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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.StudentsScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsScreen(
    groupId: Int,
    onStudentClick: (id: Int) -> Unit,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).studentsScreenViewModelFactory()
    val viewModel: StudentsScreenViewModel = viewModel(
        factory = StudentsScreenViewModel.provideStudentsScreenViewModel(
            factory,
            groupId
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            StudentsCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Students(
            viewModel = viewModel,
            paddingValues = scaffoldPadding,
            onStudentClick = onStudentClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentsCenterAlignedTopAppBar(
    viewModel: StudentsScreenViewModel,
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

@Composable
fun Students(
    viewModel: StudentsScreenViewModel,
    paddingValues: PaddingValues,
    onStudentClick: (id: Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        items(count = viewModel.students.size) { index ->
            Student(viewModel = viewModel, index = index, onStudentClick = onStudentClick)
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun Student(
    viewModel: StudentsScreenViewModel,
    index: Int,
    onStudentClick: (id: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            viewModel.getStudent(index = index)
                .let { student -> student.id?.let { id -> onStudentClick(id) } }
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
            text = viewModel.getStudent(index = index).fullName.toString(),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}