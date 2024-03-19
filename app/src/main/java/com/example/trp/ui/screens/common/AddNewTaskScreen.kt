package com.example.trp.ui.screens.common

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.common.AddNewTaskScreenViewModel
import dagger.hilt.android.EntryPointAccessors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewTaskScreen(
    disciplineId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).addNewTaskScreenViewModelFactory()
    val viewModel: AddNewTaskScreenViewModel = viewModel(
        factory = AddNewTaskScreenViewModel.provideAddNewTaskScreenViewModel(
            factory,
            disciplineId
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap(),
        topBar = {
            TaskInfoCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
        ) {
            item { TitleField(viewModel = viewModel, paddingValues = scaffoldPadding) }
            item { DescriptionField(viewModel = viewModel) }
            item { LanguageField(viewModel = viewModel) }
            item { FunctionTypeNameField(viewModel = viewModel) }
            item { Arguments(viewModel = viewModel) }
            item { Spacer(modifier = Modifier.size(100.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoCenterAlignedTopAppBar(
    viewModel: AddNewTaskScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = "Add new task",
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
                    viewModel.beforeSaveButtonClick()
                    navController.popBackStack()
                },
                enabled = viewModel.applyButtonEnabled
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
fun TitleField(
    viewModel: AddNewTaskScreenViewModel,
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
            .padding(vertical = 5.dp, horizontal = 5.dp),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.taskTitle,
        onValueChange = { viewModel.updateTitleValue(it) },
        placeholder = {
            Text(
                "Title",
                color = TRPTheme.colors.primaryText,
                modifier = Modifier.alpha(0.6f),
                fontSize = 15.sp
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
        isError = viewModel.taskTitle.isEmpty()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionField(viewModel: AddNewTaskScreenViewModel) {
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
            .padding(vertical = 5.dp, horizontal = 5.dp),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.taskDescription,
        onValueChange = { viewModel.updateDescriptionValue(it) },
        placeholder = {
            Text(
                "Description",
                color = TRPTheme.colors.primaryText,
                modifier = Modifier.alpha(0.6f),
                fontSize = 15.sp
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
        isError = viewModel.taskDescription.isEmpty()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageField(viewModel: AddNewTaskScreenViewModel) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(TRPTheme.colors.secondaryBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Language",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp,
                modifier = Modifier.alpha(0.6f)
            )
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(start = 170.dp),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .height(55.dp)
                        .menuAnchor()
                        .shadow(elevation = 10.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                    readOnly = true,
                    value = viewModel.taskLanguage,
                    onValueChange = { },
                    placeholder = {
                        Text(
                            "Language",
                            color = TRPTheme.colors.primaryText,
                            modifier = Modifier.alpha(0.6f),
                            fontSize = 15.sp
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = TRPTheme.colors.primaryBackground,
                        textColor = TRPTheme.colors.primaryText,
                        cursorColor = TRPTheme.colors.primaryText,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = TRPTheme.colors.errorColor,
                        errorCursorColor = TRPTheme.colors.primaryText,
                        errorTrailingIconColor = TRPTheme.colors.errorColor
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    isError = viewModel.taskLanguage.isEmpty()
                )
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(TRPTheme.colors.secondaryBackground)
                        .exposedDropdownSize(),
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    viewModel.languageList.forEach { selectedLanguage ->
                        DropdownMenuItem(
                            modifier = Modifier.background(TRPTheme.colors.secondaryBackground),
                            text = {
                                Text(text = selectedLanguage)
                            },
                            onClick = {
                                viewModel.updateLanguageValue(selectedLanguage)
                                expanded = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = TRPTheme.colors.primaryText
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionTypeNameField(viewModel: AddNewTaskScreenViewModel) {
    Text(
        text = "Function type, name",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = 10.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .alpha(viewModel.typeButtonsEnabled.second),
            expanded = viewModel.functionTypeListExpanded,
            onExpandedChange = {
                viewModel.updateFunctionTypeExpanded(!viewModel.functionTypeListExpanded)
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                readOnly = true,
                value = viewModel.taskFunctionType,
                onValueChange = { },
                placeholder = {
                    Text(
                        "Type",
                        color = TRPTheme.colors.primaryText,
                        modifier = Modifier.alpha(0.6f),
                        fontSize = 15.sp
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
                    errorCursorColor = TRPTheme.colors.primaryText,
                    errorTrailingIconColor = TRPTheme.colors.errorColor
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = viewModel.functionTypeListExpanded
                    )
                },
                isError = viewModel.taskFunctionType.isEmpty(),
                enabled = viewModel.typeButtonsEnabled.first
            )
            if (viewModel.typeList.isNotEmpty()) {
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(TRPTheme.colors.secondaryBackground)
                        .exposedDropdownSize(),
                    expanded = viewModel.functionTypeListExpanded,
                    onDismissRequest = {
                        viewModel.updateFunctionTypeExpanded(false)
                    }
                ) {
                    viewModel.typeList.forEach { selectedType ->
                        DropdownMenuItem(
                            modifier = Modifier.background(TRPTheme.colors.secondaryBackground),
                            text = {
                                Text(text = selectedType)
                            },
                            onClick = {
                                viewModel.updateFunctionTypeValue(selectedType)
                                viewModel.updateFunctionTypeExpanded(false)
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = TRPTheme.colors.primaryText
                            )
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(start = 5.dp)
                .fillMaxHeight()
                .widthIn(max = 65.dp)
                .weight(2.6f)
                .widthIn(max = 70.dp),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.taskFunctionName,
            onValueChange = { viewModel.updateFunctionNameValue(it) },
            placeholder = {
                Text(
                    "Function name",
                    color = TRPTheme.colors.primaryText,
                    modifier = Modifier.alpha(0.6f),
                    fontSize = 15.sp
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
            singleLine = true,
            isError = viewModel.taskFunctionName.isEmpty()
        )
    }
}

@Composable
fun Arguments(
    viewModel: AddNewTaskScreenViewModel
) {
    val taskOptionalArgumentList by rememberUpdatedState(newValue = viewModel.taskArgumentList)
    Text(
        text = "Arguments",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = 10.dp)
    )
    Column {
        taskOptionalArgumentList.forEachIndexed { index, _ ->
            Argument(viewModel = viewModel, index = index)
        }
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
            .height(55.dp),
        onClick = { viewModel.addOptionalArgument() },
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.6f),
            textAlign = TextAlign.Center,
            text = "+",
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Argument(
    viewModel: AddNewTaskScreenViewModel,
    index: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .alpha(viewModel.typeButtonsEnabled.second),
            expanded = viewModel.getArgument(index).third,
            onExpandedChange = {
                viewModel.updateArgumentTypeExpanded(
                    index = index,
                    isExpanded = !viewModel.getArgument(index).third
                )
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                readOnly = true,
                value = viewModel.getArgument(index).first,
                onValueChange = { },
                placeholder = {
                    Text(
                        "Type",
                        color = TRPTheme.colors.primaryText,
                        modifier = Modifier.alpha(0.6f),
                        fontSize = 15.sp
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
                    errorCursorColor = TRPTheme.colors.primaryText,
                    errorTrailingIconColor = TRPTheme.colors.errorColor
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = viewModel.getArgument(index).third
                    )
                },
                isError = viewModel.getArgument(index).first.isEmpty(),
                enabled = viewModel.typeButtonsEnabled.first
            )
            if (viewModel.typeList.isNotEmpty()) {
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(TRPTheme.colors.secondaryBackground)
                        .exposedDropdownSize(),
                    expanded = viewModel.getArgument(index).third,
                    onDismissRequest = {
                        viewModel.updateArgumentTypeExpanded(
                            index = index,
                            isExpanded = !viewModel.getArgument(index).third
                        )
                    }
                ) {
                    viewModel.typeList.forEach { selectedType ->
                        DropdownMenuItem(
                            modifier = Modifier.background(TRPTheme.colors.secondaryBackground),
                            text = {
                                Text(text = selectedType)
                            },
                            onClick = {
                                viewModel.updateArgumentTypeValue(
                                    index = index,
                                    newArgumentTypeValue = selectedType
                                )
                                viewModel.updateArgumentTypeExpanded(
                                    index = index,
                                    isExpanded = false
                                )
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = TRPTheme.colors.primaryText
                            )
                        )
                    }
                }
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(start = 5.dp)
                .fillMaxHeight()
                .weight(2f),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.getArgument(index).second,
            onValueChange = {
                viewModel.updateArgumentNameValue(
                    index = index,
                    newArgumentNameValue = it
                )
            },
            placeholder = {
                Text(
                    "Argument name",
                    color = TRPTheme.colors.primaryText,
                    modifier = Modifier.alpha(0.6f),
                    fontSize = 15.sp
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
            singleLine = true,
            isError = viewModel.getArgument(index).second.isEmpty()
        )
        OutlinedButton(
            modifier = Modifier
                .width(65.dp)
                .fillMaxHeight()
                .padding(start = 5.dp)
                .alpha(viewModel.deleteButtonEnabled.second),
            onClick = { viewModel.onDeleteArgumentClick(index) },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = TRPTheme.colors.secondaryBackground,
                disabledContainerColor = TRPTheme.colors.secondaryBackground
            ),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.dp, Color.Transparent),
            enabled = viewModel.deleteButtonEnabled.first
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteOutline,
                contentDescription = "DeleteArgumentIconButton",
                tint = TRPTheme.colors.errorColor
            )
        }
    }
}