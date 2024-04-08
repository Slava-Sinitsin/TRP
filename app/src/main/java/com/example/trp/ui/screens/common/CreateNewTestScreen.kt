package com.example.trp.ui.screens.common

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.common.CreateNewTestScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewTestScreen(
    taskId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).adminCreateNewTestScreenViewModelFactory()
    val viewModel: CreateNewTestScreenViewModel = viewModel(
        factory = CreateNewTestScreenViewModel.provideCreateNewTestScreenViewModel(
            factory,
            taskId
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { AddNewTestTopAppBar(viewModel = viewModel, navController = navController) }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
        ) {
            InputField(viewModel = viewModel, paddingValues = scaffoldPadding)
            OutputField(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTestTopAppBar(
    viewModel: CreateNewTestScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = "Add new test",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "CloseButton"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    viewModel.onSaveButtonClick()
                    navController.popBackStack()
                },
                enabled = viewModel.saveButtonEnabled
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "ApplyAddTaskButton",
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    viewModel: CreateNewTestScreenViewModel,
    paddingValues: PaddingValues
) {
    Column(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
        viewModel.argumentsWithRegex.forEachIndexed { index, _ ->
            Text(
                text = "Argument ${index + 1}: ${viewModel.getArgument(index).type} ${
                    viewModel.getArgument(
                        index
                    ).name
                }",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp,
                modifier = Modifier
                    .alpha(0.6f)
                    .padding(start = 5.dp, top = 10.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                value = viewModel.getArgument(index).value ?: "",
                onValueChange = { viewModel.updateInputValue(index = index, newInputValue = it) },
                placeholder = {
                    Text(
                        "Input",
                        color = TRPTheme.colors.primaryText,
                        modifier = Modifier.alpha(0.6f)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = TRPTheme.colors.secondaryBackground,
                    textColor = TRPTheme.colors.primaryText,
                    cursorColor = TRPTheme.colors.primaryText,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = TRPTheme.colors.errorColor,
                    errorCursorColor = TRPTheme.colors.primaryText
                ),
                isError = viewModel.getArgument(index).isMatch == false
                        || viewModel.getArgument(index).value?.isEmpty() ?: true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputField(
    viewModel: CreateNewTestScreenViewModel
) {
    Text(
        text = "Output: ${viewModel.task.returnType}",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = 10.dp)
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.outputValue,
        onValueChange = { viewModel.updateOutputValue(it) },
        placeholder = {
            Text(
                "Output",
                color = TRPTheme.colors.primaryText,
                modifier = Modifier.alpha(0.6f)
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TRPTheme.colors.secondaryBackground,
            textColor = TRPTheme.colors.primaryText,
            cursorColor = TRPTheme.colors.primaryText,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = TRPTheme.colors.errorColor,
            errorCursorColor = TRPTheme.colors.primaryText
        ),
        isError = !viewModel.outputMatchRegex || viewModel.outputValue.isEmpty()
    )
}