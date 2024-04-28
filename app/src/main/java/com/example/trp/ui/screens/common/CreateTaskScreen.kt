package com.example.trp.ui.screens.common

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.trp.ui.viewmodels.common.CreateTaskScreenViewModel
import dagger.hilt.android.EntryPointAccessors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    labId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).createTaskScreenViewModelFactory()
    val viewModel: CreateTaskScreenViewModel = viewModel(
        factory = CreateTaskScreenViewModel.provideCreateTaskScreenViewModel(
            factory,
            labId
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
            item { TestableToggle(viewModel = viewModel) }
            if (viewModel.testable == "Yes") {
                item { FunctionTypeNameField(viewModel = viewModel) }
                item { Arguments(viewModel = viewModel) }
                item { Spacer(modifier = Modifier.size(100.dp)) }
            }
        }
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoCenterAlignedTopAppBar(
    viewModel: CreateTaskScreenViewModel,
    navController: NavHostController
) {
    LaunchedEffect(viewModel.responseSuccess) {
        if (viewModel.responseSuccess) {
            navController.popBackStack()
        }
    }
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
                onClick = { viewModel.beforeSaveButtonClick() },
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
    viewModel: CreateTaskScreenViewModel,
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
            .padding(vertical = 5.dp, horizontal = 5.dp),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.title,
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
        isError = viewModel.title.isEmpty(),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionField(viewModel: CreateTaskScreenViewModel) {
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
        value = viewModel.description,
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
        isError = viewModel.description.isEmpty()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageField(viewModel: CreateTaskScreenViewModel) {
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
                    value = viewModel.language,
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
                    isError = viewModel.language.isEmpty()
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

@Composable
fun TestableToggle(viewModel: CreateTaskScreenViewModel) {
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
            text = "Testable",
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
                viewModel.testableList.forEach { text ->
                    Button(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(30.dp))
                            .background(
                                if (text == viewModel.testable) {
                                    TRPTheme.colors.myYellow
                                } else {
                                    TRPTheme.colors.cardButtonColor
                                }
                            ),
                        onClick = { viewModel.updateTestableValue(text) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (text == viewModel.testable) {
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
fun FunctionTypeNameField(viewModel: CreateTaskScreenViewModel) {
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
                value = viewModel.functionType,
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
                isError = viewModel.functionType.isEmpty(),
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
                .weight(2f)
                .widthIn(max = 70.dp),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.functionName,
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
            isError = viewModel.functionName.isEmpty()
        )
    }
}

@Composable
fun Arguments(
    viewModel: CreateTaskScreenViewModel
) {
    Text(
        text = "Arguments",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = 10.dp)
    )
    Column {
        viewModel.argumentList.forEachIndexed { index, _ ->
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
    viewModel: CreateTaskScreenViewModel,
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
            expanded = viewModel.getArgument(index).second,
            onExpandedChange = {
                viewModel.updateArgumentTypeExpanded(
                    index = index,
                    isExpanded = !viewModel.getArgument(index).second
                )
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                readOnly = true,
                value = viewModel.getArgument(index).first.type ?: "",
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
                        expanded = viewModel.getArgument(index).second
                    )
                },
                isError = viewModel.getArgument(index).first.type?.isEmpty() ?: true,
                enabled = viewModel.typeButtonsEnabled.first,
                singleLine = true
            )
            if (viewModel.typeList.isNotEmpty()) {
                ExposedDropdownMenu(
                    modifier = Modifier
                        .background(TRPTheme.colors.secondaryBackground)
                        .exposedDropdownSize(),
                    expanded = viewModel.getArgument(index).second,
                    onDismissRequest = {
                        viewModel.updateArgumentTypeExpanded(
                            index = index,
                            isExpanded = !viewModel.getArgument(index).second
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
                .weight(1.5f),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.getArgument(index).first.name ?: "",
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
            isError = viewModel.getArgument(index).first.name?.isEmpty() ?: true
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