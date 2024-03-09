package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeacherTaskScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTaskScreen(
    taskId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).teacherTaskScreenViewModelFactory()
    val viewModel: TeacherTaskScreenViewModel = viewModel(
        factory = TeacherTaskScreenViewModel.provideTeacherTaskScreenViewModel(
            factory,
            taskId
        )
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = TRPTheme.colors.primaryBackground,
        topBar = {
            TaskCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            TaskText(
                viewModel = viewModel,
                paddingValues = scaffoldPadding
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCenterAlignedTopAppBar(
    viewModel: TeacherTaskScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = "${viewModel.disciplineName} ${viewModel.task.title ?: ""}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "BackIconButton"
                )
            }
        },
        actions = { },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskText(
    viewModel: TeacherTaskScreenViewModel,
    paddingValues: PaddingValues
) {
    Surface(
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding() + 10.dp,
                start = 5.dp,
                end = 5.dp
            )
            .fillMaxWidth()
            .wrapContentSize(),
        color = Color.Transparent,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .horizontalScroll(rememberScrollState()),
            value = viewModel.solutionTextFieldValue,
            onValueChange = { viewModel.updateTaskText(it) },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = TRPTheme.colors.secondaryBackground,
                textColor = TRPTheme.colors.primaryText,
                cursorColor = TRPTheme.colors.primaryText,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            readOnly = true
        )
    }
}