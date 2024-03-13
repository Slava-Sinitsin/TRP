package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.NumberPicker
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.components.rememberPickerState
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.admin.AddNewDisciplineScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    val pagerState = rememberPagerState(0)
    LaunchedEffect(viewModel.selectedTabIndex) {
        pagerState.animateScrollToPage(viewModel.selectedTabIndex)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap(),
        topBar = {
            DisciplineInfoCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground),
            state = pagerState,
            pageCount = viewModel.addNewDisciplineScreens.size,
            userScrollEnabled = false
        ) { index ->
            when (index) {
                0 -> {
                    MainScreen(
                        viewModel = viewModel,
                        paddingValues = scaffoldPadding
                    )
                }

                1 -> {
                    SelectScreen(
                        viewModel = viewModel,
                        paddingValues = scaffoldPadding
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: AddNewDisciplineScreenViewModel,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
    ) {
        NameField(viewModel = viewModel, paddingValues = paddingValues)
        YearPicker(viewModel = viewModel)
        HalfYearToggle(viewModel = viewModel)
        DeprecatedToggle(viewModel = viewModel)
        TeacherSelectField(viewModel = viewModel)
        GroupsSelectField(viewModel = viewModel)
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
                text = viewModel.topAppBarText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            if (viewModel.selectedTabIndex == 0) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "CloseButton"
                    )
                }
            } else {
                IconButton(onClick = { viewModel.setPagerState(0) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "BackToMainAddDisciplineScreen"
                    )
                }
            }
        },
        actions = {
            if (viewModel.selectedTabIndex == 0) {
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

@Composable
fun SelectScreen(
    viewModel: AddNewDisciplineScreenViewModel,
    paddingValues: PaddingValues
) {
    if (viewModel.selectedTabIndex == 1) {
        TeacherSelectScreen(
            viewModel = viewModel,
            paddingValues = paddingValues
        )
    } else if (viewModel.selectedTabIndex == 2) {
        GroupsSelectScreen(
            viewModel = viewModel,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun TeacherSelectField(
    viewModel: AddNewDisciplineScreenViewModel
) {
    Text(
        text = "Teacher",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = 10.dp)
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        onClick = { viewModel.setPagerState(1) },
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                textAlign = TextAlign.Start,
                text = viewModel.selectedTeacher.fullName ?: "Select teacher",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp
            )
            Icon(
                imageVector = Icons.Filled.ArrowRight,
                contentDescription = "ArrowRight"
            )
        }
    }
}

@Composable
fun GroupsSelectField(
    viewModel: AddNewDisciplineScreenViewModel
) {
    Text(
        text = "Groups",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp, top = 10.dp)
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        onClick = { viewModel.setPagerState(2) },
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(start = 5.dp),
                textAlign = TextAlign.Start,
                text = "Select groups",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp
            )
            Icon(
                imageVector = Icons.Filled.ArrowRight,
                contentDescription = "ArrowRight"
            )
        }
    }
}

@Composable
fun TeacherSelectScreen(
    viewModel: AddNewDisciplineScreenViewModel,
    paddingValues: PaddingValues
) {
    BackHandler(enabled = true, onBack = { viewModel.setPagerState(0) })
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        items(count = viewModel.teachers.size) { index ->
            var checked by remember { mutableStateOf(false) }
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                onClick = {
                    viewModel.onTeacherClick(index)
                    checked = !checked
                },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TRPTheme.colors.cardButtonColor),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = viewModel.getTeacher(index = index).fullName ?: "",
                        color = TRPTheme.colors.primaryText,
                        fontSize = 15.sp
                    )
                    RadioButton(
                        selected = checked,
                        onClick = {
                            viewModel.onTeacherClick(index)
                            checked = !checked
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = TRPTheme.colors.myYellow
                        )
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun GroupsSelectScreen(
    viewModel: AddNewDisciplineScreenViewModel,
    paddingValues: PaddingValues
) {
    BackHandler(enabled = true, onBack = { viewModel.setPagerState(0) })
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
            .padding(top = paddingValues.calculateTopPadding()),
    ) {
        items(count = viewModel.groups.size) { index ->
            var checked by remember { mutableStateOf(false) }
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                onClick = { checked = !checked },
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TRPTheme.colors.cardButtonColor),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = viewModel.getGroup(index = index).name ?: "",
                        color = TRPTheme.colors.primaryText,
                        fontSize = 15.sp
                    )
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = !checked },
                        colors = CheckboxDefaults.colors(
                            checkedColor = TRPTheme.colors.myYellow
                        )
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}