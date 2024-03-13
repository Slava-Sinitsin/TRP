package com.example.trp.ui.screens.student

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.student.TaskScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    taskId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).taskScreenViewModelFactory()
    val viewModel: TaskScreenViewModel = viewModel(
        factory = TaskScreenViewModel.provideTaskScreenViewModel(
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
            Text(
                modifier = Modifier.padding(start = 5.dp, top = 10.dp),
                text = "Output:",
                fontSize = 20.sp,
                color = TRPTheme.colors.primaryText
            )
            OutputText(
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCenterAlignedTopAppBar(
    viewModel: TaskScreenViewModel,
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
        actions = {
            IconButton(onClick = { viewModel.onSaveCodeButtonClick() }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "SaveCodeButton"
                )
            }
            IconButton(onClick = { viewModel.onRunCodeButtonClick() }) {
                Icon(
                    imageVector = Icons.Filled.PlayCircleOutline,
                    contentDescription = "RunCodeButton"
                )
            }
        },
    )
}

@Composable
fun TaskText( // TODO
    viewModel: TaskScreenViewModel,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding() + 10.dp,
                start = 5.dp,
                end = 5.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TRPTheme.colors.cardButtonColor)
        ) {
            if (viewModel.linesCount.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                ) {
                    viewModel.linesCount.forEachIndexed { index, top ->
                        Text(
                            modifier = Modifier.offset(y = with(LocalDensity.current) { top.toDp() }),
                            text = "${index + 1}\n",
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
            }
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(TRPTheme.colors.secondaryBackground),
                value = viewModel.solutionTextFieldValue,
                onValueChange = { viewModel.updateTaskText(it) },
                onTextLayout = { viewModel.updateLinesCount(it) },
                cursorBrush = Brush.verticalGradient(
                    Pair(1.0f, TRPTheme.colors.primaryText),
                    Pair(1.0f, TRPTheme.colors.primaryText)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputText(
    viewModel: TaskScreenViewModel
) {
    Surface(
        modifier = Modifier
            .padding(
                top = 10.dp,
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
                .height(100.dp)
                .horizontalScroll(rememberScrollState()),
            value = viewModel.outputText,
            onValueChange = { },
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