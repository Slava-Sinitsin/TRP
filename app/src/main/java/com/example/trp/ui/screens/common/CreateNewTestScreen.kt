package com.example.trp.ui.screens.common

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
        LazyColumn(
            modifier = Modifier
                .padding(top = scaffoldPadding.calculateTopPadding())
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
        ) {
            item { IsOpenToggle(viewModel = viewModel) }
            item { InputField(viewModel = viewModel) }
            item { OutputField(viewModel = viewModel) }
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

@Composable
fun IsOpenToggle(viewModel: CreateNewTestScreenViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(TRPTheme.colors.secondaryBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(start = 5.dp)
                .alpha(0.6f),
            text = "Open",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
        )
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(30.dp))
        ) {
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(30.dp))
                    .background(TRPTheme.colors.cardButtonColor)
            ) {
                viewModel.isOpenList.forEach { text ->
                    Button(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(30.dp))
                            .background(
                                if (text == viewModel.isOpen) {
                                    TRPTheme.colors.myYellow
                                } else {
                                    TRPTheme.colors.cardButtonColor
                                }
                            ),
                        onClick = { viewModel.updateIsOpenValue(text) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (text == viewModel.isOpen) {
                                TRPTheme.colors.myYellow
                            } else {
                                TRPTheme.colors.cardButtonColor
                            }
                        )
                    ) {
                        Text(
                            text = text,
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(viewModel: CreateNewTestScreenViewModel) {
    Column(modifier = Modifier.padding(horizontal = 5.dp)) {
        viewModel.argumentsWithRegex.forEachIndexed { index, _ ->
            Text(
                modifier = Modifier
                    .alpha(0.6f)
                    .padding(top = 10.dp),
                text = "Argument ${index + 1}: ${viewModel.getArgument(index).type} ${
                    viewModel.getArgument(
                        index
                    ).name
                }",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 5.dp),
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
    Column(modifier = Modifier.padding(horizontal = 5.dp)) {
        Text(
            modifier = Modifier
                .alpha(0.6f)
                .padding(top = 10.dp),
            text = "Output: ${viewModel.task.returnType}",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(vertical = 5.dp),
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
}