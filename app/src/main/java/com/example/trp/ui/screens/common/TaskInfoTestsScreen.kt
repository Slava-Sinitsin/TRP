package com.example.trp.ui.screens.common

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.tabs.DisabledInteractionSource
import com.example.trp.ui.screens.admin.MyNewIndicator
import com.example.trp.ui.screens.admin.myTabIndicatorOffset
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.common.TaskInfoTestsScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskInfoTestsScreen(
    taskId: Int,
    navController: NavHostController,
    onAddTestClick: (taskId: Int) -> Unit,
    onTestClick: (testId: Int) -> Unit
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).taskInfoTestsScreenViewModelFactory()
    val viewModel: TaskInfoTestsScreenViewModel = viewModel(
        factory = TaskInfoTestsScreenViewModel.provideTaskInfoTestsScreenViewModel(
            factory,
            taskId
        )
    )

    val pagerState = rememberPagerState(0)
    LaunchedEffect(viewModel.selectedTabIndex) {
        pagerState.animateScrollToPage(viewModel.selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            viewModel.setPagerState(pagerState.currentPage)
        }
    }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        MyNewIndicator(
            Modifier.myTabIndicatorOffset(tabPositions[viewModel.selectedTabIndex])
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TaskInfoCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(top = scaffoldPadding.calculateTopPadding())
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
        ) {
            TabRow(
                modifier = Modifier
                    .background(TRPTheme.colors.primaryBackground)
                    .padding(5.dp)
                    .clip(
                        shape = RoundedCornerShape(20.dp)
                    ),
                selectedTabIndex = viewModel.selectedTabIndex,
                containerColor = TRPTheme.colors.secondaryBackground,
                indicator = indicator,
                divider = {}
            ) {
                viewModel.taskTestsScreens.forEachIndexed { index, item ->
                    Tab(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .padding(bottom = 3.dp)
                            .zIndex(2f),
                        selected = index == viewModel.selectedTabIndex,
                        interactionSource = DisabledInteractionSource(),
                        onClick = { viewModel.setPagerState(index) },
                        text = {
                            Text(
                                text = item.title,
                                color = TRPTheme.colors.secondaryText,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(TRPTheme.colors.primaryBackground),
                state = pagerState,
                pageCount = viewModel.taskTestsScreens.size
            ) { index ->
                if (index == 0) {
                    TaskInfoScreen(viewModel = viewModel)
                } else if (index == 1) {
                    TestsScreen(
                        viewModel = viewModel,
                        onTestClick = onTestClick,
                        onAddTestClick = onAddTestClick
                    )
                }
            }
            if (viewModel.showDeleteDialog) {
                DeleteDialog(viewModel = viewModel, navController = navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoCenterAlignedTopAppBar(
    viewModel: TaskInfoTestsScreenViewModel,
    navController: NavHostController
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
            if (viewModel.readOnlyMode) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "NavigationBackIcon"
                    )
                }
            } else {
                IconButton(onClick = { viewModel.onRollBackIconButtonClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "NavigationIcon"
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
        }
    )
}

@Composable
fun TaskInfoScreen(
    viewModel: TaskInfoTestsScreenViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TitleField(viewModel = viewModel)
        DescriptionField(viewModel = viewModel)
        FunctionNameField(viewModel = viewModel)
        LanguageField(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleField(
    viewModel: TaskInfoTestsScreenViewModel
) {
    Text(
        text = "Title",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(start = 5.dp)
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
fun DescriptionField(viewModel: TaskInfoTestsScreenViewModel) {
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
fun FunctionNameField(viewModel: TaskInfoTestsScreenViewModel) {
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
fun LanguageField(viewModel: TaskInfoTestsScreenViewModel) {
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
fun DeleteDialog(viewModel: TaskInfoTestsScreenViewModel, navController: NavHostController) {
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
                onClick = {
                    viewModel.beforeConfirmButtonClick()
                    navController.popBackStack()
                },
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

@Composable
fun TestsScreen(
    viewModel: TaskInfoTestsScreenViewModel,
    onTestClick: (testId: Int) -> Unit,
    onAddTestClick: (taskId: Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { AddTestToTask(viewModel = viewModel, onAddTestClick = onAddTestClick) }
        items(count = 20) { index ->
            Test(viewModel = viewModel, index = index, onTestClick = onTestClick)
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun AddTestToTask(
    viewModel: TaskInfoTestsScreenViewModel,
    onAddTestClick: (taskId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onAddTestClick(viewModel.taskId) },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues()
    ) {
        Text(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxSize()
                .alpha(0.6f),
            text = "+",
            color = TRPTheme.colors.primaryText,
            fontSize = 45.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Test(
    viewModel: TaskInfoTestsScreenViewModel,
    index: Int,
    onTestClick: (testId: Int) -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f,
        label = "rotationState"
    )
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = {
            viewModel.getTest(index = index).let { task ->
                task.id?.let { groupId ->
                    onTestClick(
                        groupId
                    )
                }
            }
        },
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = TRPTheme.colors.cardButtonColor),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues()
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = viewModel.getTest(index = index).title.toString(),
                    color = TRPTheme.colors.primaryText,
                    fontSize = 25.sp
                )
                IconButton(
                    modifier = Modifier.rotate(rotationState),
                    onClick = { expandedState = !expandedState }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "DropDownArrow"
                    )
                }
            }
            if (expandedState) {
                Text(
                    text = viewModel.getTest(index = index).title.toString(),
                    color = TRPTheme.colors.primaryText,
                    fontSize = 25.sp
                )
            }
        }
    }
}