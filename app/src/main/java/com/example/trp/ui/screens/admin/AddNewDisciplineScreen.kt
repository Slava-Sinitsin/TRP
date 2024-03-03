package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.NumberPicker
import com.example.trp.ui.components.rememberPickerState
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.admin.AddNewDisciplineScreenViewModel
import com.kosher9.roundcheckbox.RoundCheckBox
import com.kosher9.roundcheckbox.RoundCheckBoxDefaults
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewDisciplineScreen(
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).adminAddNewDisciplineScreenViewModelFactory()
    val viewModel: AddNewDisciplineScreenViewModel = viewModel(
        factory = AddNewDisciplineScreenViewModel.provideAddNewDisciplineScreenViewModel(
            factory
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            DisciplineInfoCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
        ) {
            NameField(viewModel = viewModel, paddingValues = scaffoldPadding)
            YearPicker(viewModel = viewModel)
            HalfYearToggle(viewModel = viewModel)
            DeprecatedToggle(viewModel = viewModel)
            TeacherDropDownMenu(viewModel = viewModel)
            GroupsDropDownMenu(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineInfoCenterAlignedTopAppBar(
    viewModel: AddNewDisciplineScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = "Add new discipline",
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
                    contentDescription = "ApplyAddDisciplineButton",
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameField(
    viewModel: AddNewDisciplineScreenViewModel,
    paddingValues: PaddingValues
) {
    Text(
        text = "Discipline name",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = paddingValues.calculateTopPadding() + 10.dp)
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.disciplineName,
        onValueChange = { viewModel.updateNameValue(it) },
        placeholder = {
            Text(
                "Discipline name",
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
        isError = viewModel.disciplineName.isEmpty(),
        singleLine = true
    )
}

@Composable
fun YearPicker(viewModel: AddNewDisciplineScreenViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(TRPTheme.colors.secondaryBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(start = 5.dp)
                .alpha(0.6f),
            text = "Year",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
        )
        val values = remember { viewModel.disciplineYear }
        val state = rememberPickerState()
        LaunchedEffect(state.selectedItem) {
            viewModel.updateYearValue(state.selectedItem)
        }
        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TRPTheme.colors.cardButtonColor)
        ) {
            NumberPicker(
                state = state,
                values = values,
                visibleItemsCount = 3,
                textStyle = TextStyle(fontSize = 15.sp),
                startIndex = viewModel.disciplineYear.indexOf("2024")
            )
        }
    }
}

@Composable
fun HalfYearToggle(viewModel: AddNewDisciplineScreenViewModel) {
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
            text = "Half year",
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
                viewModel.disciplineHalfYear.forEach { text ->
                    Button(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(30.dp))
                            .background(
                                if (text == viewModel.selectedHalfYear) {
                                    TRPTheme.colors.myYellow
                                } else {
                                    TRPTheme.colors.cardButtonColor
                                }
                            ),
                        onClick = { viewModel.updateHalfYearValue(text) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (text == viewModel.selectedHalfYear) {
                                TRPTheme.colors.myYellow
                            } else {
                                TRPTheme.colors.cardButtonColor
                            }
                        )
                    ) {
                        Text(
                            text = text[0] + text.substring(1).lowercase(),
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeprecatedToggle(viewModel: AddNewDisciplineScreenViewModel) {
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
            text = "Deprecated",
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
                viewModel.disciplineDeprecated.forEach { text ->
                    Button(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(30.dp))
                            .background(
                                if (text == viewModel.selectedDeprecated) {
                                    TRPTheme.colors.myYellow
                                } else {
                                    TRPTheme.colors.cardButtonColor
                                }
                            ),
                        onClick = { viewModel.updateDeprecatedValue(text) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (text == viewModel.selectedDeprecated) {
                                TRPTheme.colors.myYellow
                            } else {
                                TRPTheme.colors.cardButtonColor
                            }
                        )
                    ) { Text(text = text, color = TRPTheme.colors.primaryText) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDropDownMenu(
    viewModel: AddNewDisciplineScreenViewModel
) {
    val focusManager = LocalFocusManager.current
    Text(
        text = "Teacher",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(top = 10.dp, start = 5.dp)
    )
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp),
        expanded = viewModel.teacherDropDownMenuState,
        onExpandedChange = { viewModel.onTeacherDropDownMenuExpandedChange(!viewModel.teacherDropDownMenuState) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.selectedTeacher.fullName ?: "",
            onValueChange = { viewModel.onTeacherValueChange(it) },
            placeholder = {
                Text(
                    "Select teacher",
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
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(TRPTheme.colors.secondaryBackground),
            expanded = viewModel.teacherDropDownMenuState,
            onDismissRequest = {
                viewModel.onTeacherDropDownMenuExpandedChange(!viewModel.teacherDropDownMenuState)
                focusManager.clearFocus()
            }
        ) {
            viewModel.filteredTeachers.forEach { item ->
                DropdownMenuItem(
                    text = {
                        item.fullName?.let {
                            Text(
                                text = it,
                                color = TRPTheme.colors.primaryText
                            )
                        }
                    },
                    onClick = {
                        viewModel.onTeacherClick(item)
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsDropDownMenu( // TODO
    viewModel: AddNewDisciplineScreenViewModel
) {
    val focusManager = LocalFocusManager.current
    Text(
        text = "Groups",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(top = 10.dp, start = 5.dp)
    )
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp),
        expanded = viewModel.groupsDropDownMenuState,
        onExpandedChange = {
            viewModel.onGroupsDropDownMenuExpandedChange(!viewModel.groupsDropDownMenuState)
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.selectedGroupText,
            onValueChange = { viewModel.onGroupValueChange(it) },
            placeholder = {
                Text(
                    "Select groups",
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
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(TRPTheme.colors.secondaryBackground),
            expanded = viewModel.groupsDropDownMenuState,
            onDismissRequest = {
                viewModel.onGroupsDropDownMenuExpandedChange(!viewModel.groupsDropDownMenuState)
                focusManager.clearFocus()
            }
        ) {
            viewModel.groups.forEach { item ->
                var checked by remember { mutableStateOf(false) }
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.name ?: "",
                                color = TRPTheme.colors.primaryText
                            )
                            RoundCheckBox(
                                isChecked = checked,
                                onClick = {
                                    checked = !checked
                                    viewModel.onGroupClick(item)
                                },
                                color = RoundCheckBoxDefaults.colors(
                                    selectedColor = TRPTheme.colors.myYellow,
                                    borderColor = TRPTheme.colors.myYellow,
                                    tickColor = TRPTheme.colors.primaryText,
                                    disabledSelectedColor = TRPTheme.colors.myYellow
                                )
                            )
                        }
                    },
                    onClick = {
                        checked = !checked
                        viewModel.onGroupClick(item)
                    }
                )
            }
        }
    }
}