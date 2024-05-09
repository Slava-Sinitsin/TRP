package com.example.trp.ui.screens.admin

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.data.mappers.teacherappointments.Group
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.HorizontalNumberPicker
import com.example.trp.ui.components.TabIndicator
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.components.myTabIndicatorOffset
import com.example.trp.ui.components.rememberHorizontalPickerState
import com.example.trp.ui.components.tabs.DisabledInteractionSource
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
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
        }
    }
}

@Composable
fun MainScreen(
    viewModel: AddNewDisciplineScreenViewModel,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
    ) {
        item { NameField(viewModel = viewModel, paddingValues = paddingValues) }
        item { YearPicker(viewModel = viewModel) }
        item { HalfYearToggle(viewModel = viewModel) }
        item { DeprecatedToggle(viewModel = viewModel) }
        item { TeacherSelectField(viewModel = viewModel) }
        item { GroupsSelectField(viewModel = viewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineInfoCenterAlignedTopAppBar(
    viewModel: AddNewDisciplineScreenViewModel,
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
                    onClick = { viewModel.beforeSaveButtonClick() },
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
        val state = rememberHorizontalPickerState()
        LaunchedEffect(state.selectedItem) {
            viewModel.updateYearValue(state.selectedItem)
        }
        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TRPTheme.colors.cardButtonColor)
        ) {
            HorizontalNumberPicker(
                state = state,
                values = values,
                visibleItemsCount = 3,
                textStyle = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center),
                startIndex = viewModel.disciplineYear.indexOf("2024")
            )
        }
    }
}

@Composable
fun HalfYearToggle(viewModel: AddNewDisciplineScreenViewModel) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabIndicator(Modifier.myTabIndicatorOffset(tabPositions[viewModel.selectedHalfYearIndex]))
    }
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
                .weight(1f)
                .padding(start = 5.dp)
                .alpha(0.6f),
            text = "Half year",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
        )
        TabRow(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(30.dp))
                .border(
                    width = 2.dp,
                    color = TRPTheme.colors.secondaryBackground,
                    shape = RoundedCornerShape(30.dp)
                ),
            selectedTabIndex = viewModel.selectedHalfYearIndex,
            indicator = indicator,
            divider = {},
            containerColor = TRPTheme.colors.primaryBackground
        ) {
            viewModel.disciplineHalfYear.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier.zIndex(2f),
                    selected = index == viewModel.selectedHalfYearIndex,
                    interactionSource = DisabledInteractionSource(),
                    onClick = { viewModel.updateHalfYearIndex(index) },
                    text = {
                        Text(
                            text = item,
                            color = TRPTheme.colors.primaryText
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DeprecatedToggle(viewModel: AddNewDisciplineScreenViewModel) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabIndicator(Modifier.myTabIndicatorOffset(tabPositions[viewModel.selectedDeprecatedIndex]))
    }
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
                .weight(1f)
                .padding(start = 5.dp)
                .alpha(0.6f),
            text = "Deprecated",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
        )
        TabRow(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(30.dp))
                .border(
                    width = 2.dp,
                    color = TRPTheme.colors.secondaryBackground,
                    shape = RoundedCornerShape(30.dp)
                ),
            selectedTabIndex = viewModel.selectedDeprecatedIndex,
            indicator = indicator,
            divider = {},
            containerColor = TRPTheme.colors.primaryBackground
        ) {
            viewModel.disciplineDeprecated.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier.zIndex(2f),
                    selected = index == viewModel.selectedDeprecatedIndex,
                    interactionSource = DisabledInteractionSource(),
                    onClick = { viewModel.updateDeprecatedIndex(index) },
                    text = {
                        Text(
                            text = item,
                            color = TRPTheme.colors.primaryText
                        )
                    }
                )
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
            .padding(horizontal = 5.dp),
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
    Spacer(modifier = Modifier.size(10.dp))
    viewModel.selectableGroups.forEachIndexed { index, group ->
        if (group.second) {
            Group(
                group = group,
                onDeleteGroupClick = { viewModel.onDeleteGroupClick(index) }
            )
            Spacer(modifier = Modifier.size(5.dp))
        }
    }
}

@Composable
fun Group(
    group: Pair<Group, Boolean>,
    onDeleteGroupClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = TRPTheme.colors.cardButtonColor
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                text = group.first.name ?: "",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp
            )
        }
        OutlinedButton(
            modifier = Modifier.padding(start = 5.dp),
            onClick = { onDeleteGroupClick() },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = TRPTheme.colors.secondaryBackground,
                disabledContainerColor = TRPTheme.colors.secondaryBackground
            ),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.dp, Color.Transparent)
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteOutline,
                contentDescription = "DeleteStudentIconButton",
                tint = TRPTheme.colors.errorColor
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
        items(count = viewModel.selectableGroups.size) { index ->
            Button(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                onClick = { viewModel.onGroupClick(index) },
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
                        checked = viewModel.selectableGroups[index].second,
                        onCheckedChange = { viewModel.onGroupClick(index) },
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