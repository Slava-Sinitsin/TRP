package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
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
import com.example.trp.ui.viewmodels.teacher.TaskInfoScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoScreen(
    taskId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).studentInfoScreenViewModelFactory()
    val viewModel: TaskInfoScreenViewModel = viewModel(
        factory = TaskInfoScreenViewModel.provideTaskInfoScreenViewModel(
            factory,
            taskId,
            navController
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { TaskInfoCenterAlignedTopAppBar(viewModel = viewModel) }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
        ) {
            TitleField(viewModel = viewModel, paddingValues = scaffoldPadding)
            DescriptionField(viewModel = viewModel)
            FunctionNameField(viewModel = viewModel)
            LanguageField(viewModel = viewModel)
            if (viewModel.showDeleteDialog) {
                DeleteDialog(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoCenterAlignedTopAppBar(
    viewModel: TaskInfoScreenViewModel
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = viewModel.task.title ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            if (!viewModel.readOnlyMode) {
                IconButton(onClick = { viewModel.onRollBackIconClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Navigation icon"

                    )
                }
            }
        },
        actions = {
            if (viewModel.readOnlyMode) {
                Row {
                    IconButton(onClick = { viewModel.onDeleteButtonClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "DeleteTask"
                        )
                    }
                    IconButton(onClick = { viewModel.onEditButtonClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "EditTaskPropertiesButton"
                        )
                    }
                }
            } else {
                IconButton(
                    onClick = { viewModel.onSaveButtonClick() },
                    enabled = viewModel.applyButtonEnabled
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "ApplyTaskPropertiesButton",
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleField(
    viewModel: TaskInfoScreenViewModel,
    paddingValues: PaddingValues
) {
    Text(
        text = "Title",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = paddingValues.calculateTopPadding() + 10.dp)
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .alpha(viewModel.readOnlyAlpha),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.taskTitle,
        onValueChange = { viewModel.updateTitleValue(it) },
        placeholder = {
            Text(
                "Title",
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
        readOnly = viewModel.readOnlyMode,
        isError = viewModel.taskTitle.isEmpty()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionField(viewModel: TaskInfoScreenViewModel) {
    Text(
        text = "Description",
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
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .alpha(viewModel.readOnlyAlpha),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.taskDescription,
        onValueChange = { viewModel.updateDescriptionValue(it) },
        placeholder = {
            Text(
                "Description",
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
        readOnly = viewModel.readOnlyMode,
        isError = viewModel.taskDescription.isEmpty()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionNameField(viewModel: TaskInfoScreenViewModel) {
    Text(
        text = "Function name",
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
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .alpha(viewModel.readOnlyAlpha),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.taskFunctionName,
        onValueChange = { viewModel.updateFunctionNameValue(it) },
        placeholder = {
            Text(
                "Function name",
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
        readOnly = viewModel.readOnlyMode,
        isError = viewModel.taskFunctionName.isEmpty()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageField(viewModel: TaskInfoScreenViewModel) {
    Text(
        text = "Language",
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
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .alpha(viewModel.readOnlyAlpha),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.taskLanguage,
        onValueChange = { viewModel.updateLanguageValue(it) },
        placeholder = {
            Text(
                "Language",
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
        readOnly = viewModel.readOnlyMode,
        isError = viewModel.taskLanguage.isEmpty()
    )
}

@Composable
fun DeleteDialog(viewModel: TaskInfoScreenViewModel) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = "Delete task",
                color = TRPTheme.colors.primaryText
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
        text = {
            Text(
                text = "Are you sure you want to delete \"${viewModel.task.title}?\"",
                color = TRPTheme.colors.primaryText
            )
        },
        confirmButton = {
            Button(
                onClick = { viewModel.onConfirmButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.errorColor)
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
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.myYellow)
            ) {
                Text(
                    text = "Dismiss",
                    color = TRPTheme.colors.secondaryText
                )
            }
        }
    )
}