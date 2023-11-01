package com.example.trp.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.TaskScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNCHECKED_CAST")
@Composable
fun TaskScreen(taskId: Int) {
    val viewModel = viewModel<TaskScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TaskScreenViewModel(taskId = taskId) as T
            }
        }
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = TRPTheme.colors.primaryBackground,
        topBar = { TaskCenterAlignedTopAppBar(viewModel = viewModel) }
    ) {
        TaskText(
            viewModel = viewModel,
            paddingValues = it
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCenterAlignedTopAppBar(
    viewModel: TaskScreenViewModel
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = "${viewModel.taskDisciplineData.name ?: ""} ${viewModel.task.title ?: ""}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        actions = {
            IconButton(onClick = { viewModel.onRunCodeButtonClick() }) {
                Icon(
                    imageVector = Icons.Filled.PlayCircleOutline,
                    contentDescription = "RunCodeButton"
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskText(
    viewModel: TaskScreenViewModel,
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
            value = viewModel.taskText,
            onValueChange = { viewModel.updateTaskText(it) },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = TRPTheme.colors.secondaryBackground,
                textColor = TRPTheme.colors.primaryText,
                cursorColor = TRPTheme.colors.primaryText,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
