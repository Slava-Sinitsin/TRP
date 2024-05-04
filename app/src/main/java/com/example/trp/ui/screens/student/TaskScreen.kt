package com.example.trp.ui.screens.student

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chihsuanwu.freescroll.freeScroll
import com.chihsuanwu.freescroll.rememberFreeScrollState
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.TabIndicator
import com.example.trp.ui.components.TaskStatus
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.components.keyboardAsState
import com.example.trp.ui.components.myTabIndicatorOffset
import com.example.trp.ui.components.tabs.DisabledInteractionSource
import com.example.trp.ui.components.tabs.TaskTabs
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.student.TaskScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(
    taskId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).taskScreenViewModelFactory()
    val viewModel: TaskScreenViewModel = viewModel(
        factory = TaskScreenViewModel.provideTaskScreenViewModel(
            factory,
            taskId
        )
    )

    if (viewModel.taskScreens.isNotEmpty()) {
        val pagerState = rememberPagerState(1)
        LaunchedEffect(viewModel.selectedTabIndex) {
            pagerState.animateScrollToPage(viewModel.selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress) {
                viewModel.updateSelectedTabIndex(pagerState.currentPage)
            }
        }
        val indicator = @Composable { tabPositions: List<TabPosition> ->
            TabIndicator(Modifier.myTabIndicatorOffset(tabPositions[viewModel.selectedTabIndex]))
        }

        BackHandler(enabled = true, onBack = {
            if (viewModel.solutionTextFieldValue.text == viewModel.codeBck) {
                navController.popBackStack()
            } else {
                viewModel.showSaveDialog()
            }
        })

        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .clearFocusOnTap(),
            containerColor = TRPTheme.colors.primaryBackground,
            topBar = {
                TaskScreenTopAppBar(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .padding(top = scaffoldPadding.calculateTopPadding())
                    .fillMaxSize()
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
                    viewModel.taskScreens.forEachIndexed { index, item ->
                        Tab(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(20.dp))
                                .padding(bottom = 3.dp)
                                .zIndex(2f),
                            selected = index == viewModel.selectedTabIndex,
                            interactionSource = DisabledInteractionSource(),
                            onClick = { viewModel.updateSelectedTabIndex(index) },
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
                    pageCount = viewModel.taskScreens.size,
                    userScrollEnabled = viewModel.userScrollEnabled
                ) { index ->
                    when (viewModel.taskScreens[index]) {
                        TaskTabs.Description -> {
                            DescriptionScreen(viewModel = viewModel)
                        }

                        TaskTabs.Solution -> {
                            SolutionScreen(viewModel = viewModel)
                        }

                        TaskTabs.Review -> {
                            ReviewScreen(viewModel = viewModel)
                        }
                    }
                }
            }
            if (viewModel.isSaveDialogShow) {
                SaveDialog(viewModel = viewModel, navController = navController)
            }
            if (viewModel.isReviewDialogShow) {
                ReviewDialog(viewModel = viewModel)
            }
            if (viewModel.errorMessage.isNotEmpty()) {
                Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT)
                    .show()
                viewModel.updateErrorMessage("")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenTopAppBar(
    viewModel: TaskScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = viewModel.teamAppointment.task?.title ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (viewModel.solutionTextFieldValue.text == viewModel.codeBck) {
                    navController.popBackStack()
                } else {
                    viewModel.showSaveDialog()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "BackIconButton"
                )
            }
        },
        actions = {
            if (viewModel.taskScreens[viewModel.selectedTabIndex] == TaskTabs.Solution) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (viewModel.teamAppointment.status == TaskStatus.Tested.status && viewModel.reviewButtonEnabled) {
                        IconButton(onClick = { viewModel.showReviewDialog() }) {
                            Icon(
                                imageVector = Icons.Filled.Reviews,
                                contentDescription = "Send to code review"
                            )
                        }
                    }
                    if (viewModel.isRunButtonEnabled) {
                        IconButton(
                            onClick = { viewModel.onRunCodeButtonClick() },
                            enabled = viewModel.runCodeButtonEnabled
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayCircleOutline,
                                contentDescription = "RunCodeButton"
                            )
                        }
                    }
                }
            }
        },
    )
}

@Composable
fun DescriptionScreen(viewModel: TaskScreenViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { TitleField(viewModel = viewModel) }
        item { DescriptionField(viewModel = viewModel) }
        item { TestsField(viewModel = viewModel) }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleField(viewModel: TaskScreenViewModel) {
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
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.teamAppointment.task?.title ?: "",
        onValueChange = { },
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
        readOnly = true,
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionField(viewModel: TaskScreenViewModel) {
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
            .height(200.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.teamAppointment.task?.description ?: "",
        onValueChange = { },
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
        readOnly = true
    )
}

@Composable
fun TestsField(viewModel: TaskScreenViewModel) {
    if (viewModel.teamAppointment.task?.tests?.isNotEmpty() == true) {
        Text(
            text = "Example",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
            modifier = Modifier
                .alpha(0.6f)
                .padding(start = 5.dp, top = 10.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
        ) {
            viewModel.teamAppointment.task?.tests?.forEach { test ->
                Box(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(TRPTheme.colors.secondaryBackground)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
                        text = "${test.input} -> ${test.output}",
                        color = TRPTheme.colors.primaryText
                    )
                }
            }
        }
    }
}

@Composable
fun SolutionScreen(viewModel: TaskScreenViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { TaskText(viewModel = viewModel) }
        item {
            Text(
                modifier = Modifier.padding(start = 5.dp, top = 10.dp),
                text = "Output:",
                fontSize = 20.sp,
                color = TRPTheme.colors.primaryText
            )
        }
        item { OutputText(viewModel = viewModel) }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskText(
    viewModel: TaskScreenViewModel
) {
    val scrollState = rememberScrollState()
    val interactionSource = remember { MutableInteractionSource() }
    val isKeyboardOpen by keyboardAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isKeyboardOpen) {
        viewModel.updateEnableUserScroll(!isKeyboardOpen)
        if (!isKeyboardOpen) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(
                start = 5.dp,
                end = 5.dp
            )
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                modifier = Modifier
                    .width(40.dp)
                    .height(400.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .verticalScroll(scrollState),
                value = viewModel.linesText,
                onValueChange = { },
                interactionSource = interactionSource,
                textStyle = TextStyle(
                    color = TRPTheme.colors.primaryText,
                    textAlign = TextAlign.End,
                    fontFamily = FontFamily.Monospace, fontSize = 15.sp
                ),
                readOnly = true,
                decorationBox = { innerTextField ->
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = viewModel.linesText,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = false,
                        interactionSource = interactionSource,
                        visualTransformation = VisualTransformation.None,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = TRPTheme.colors.cardButtonColor,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        contentPadding = TextFieldDefaults.outlinedTextFieldPadding(
                            top = 2.dp,
                            bottom = 2.dp,
                            start = 2.dp,
                            end = 5.dp
                        ),
                        shape = RoundedCornerShape(0.dp)
                    )
                }
            )
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .verticalScroll(scrollState)
                    .horizontalScroll(
                        state = rememberScrollState(),
                        enabled = !viewModel.userScrollEnabled
                    ),
                value = viewModel.solutionTextFieldValue,
                onValueChange = { viewModel.updateTaskText(it) },
                interactionSource = interactionSource,
                textStyle = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 15.sp),
                cursorBrush = SolidColor(TRPTheme.colors.primaryText),
                decorationBox = { innerTextField ->
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = viewModel.solutionTextFieldValue.text,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = false,
                        interactionSource = interactionSource,
                        visualTransformation = VisualTransformation.None,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = TRPTheme.colors.secondaryBackground,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        contentPadding = TextFieldDefaults.outlinedTextFieldPadding(
                            top = 2.dp,
                            bottom = 2.dp,
                            start = 2.dp,
                            end = 2.dp
                        ),
                        shape = RoundedCornerShape(0.dp)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputText(
    viewModel: TaskScreenViewModel
) {
    TextField(
        modifier = Modifier
            .padding(
                top = 10.dp,
                start = 5.dp,
                end = 5.dp
            )
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        value = viewModel.outputText,
        onValueChange = { },
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = TRPTheme.colors.secondaryBackground,
            textColor = TRPTheme.colors.primaryText,
            cursorColor = TRPTheme.colors.primaryText,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        readOnly = true
    )
}

@Composable
fun SaveDialog(viewModel: TaskScreenViewModel, navController: NavHostController) {
    LaunchedEffect(viewModel.responseSuccess) {
        if (viewModel.responseSuccess) {
            navController.popBackStack()
        }
    }
    AlertDialog(
        onDismissRequest = { viewModel.onDoNotSaveCodeButtonClick() },
        title = {
            Text(
                text = "Save changes?",
                color = TRPTheme.colors.primaryText
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
        text = {
            Text(
                text = "Do you want to save changes?",
                color = TRPTheme.colors.primaryText
            )
        },
        confirmButton = {
            Button(
                onClick = { viewModel.onSaveCodeButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.myYellow)
            ) {
                Text(
                    text = "Yes",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    viewModel.onDoNotSaveCodeButtonClick()
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.errorColor)
            ) {
                Text(
                    text = "No",
                    color = TRPTheme.colors.secondaryText
                )
            }
        }
    )
}

@Composable
fun ReviewDialog(viewModel: TaskScreenViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.onDoNotSaveCodeButtonClick() },
        title = {
            Text(
                text = "Send to review?",
                color = TRPTheme.colors.primaryText
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
        text = {
            Text(
                text = "Are you sure you want to submit this code for review?",
                color = TRPTheme.colors.primaryText
            )
        },
        confirmButton = {
            Button(
                onClick = { viewModel.onPostCodeReviewButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.myYellow)
            ) {
                Text(
                    text = "Yes",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { viewModel.onDismissReviewButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.errorColor)
            ) {
                Text(
                    text = "No",
                    color = TRPTheme.colors.secondaryText
                )
            }
        }
    )
}

@Composable
fun ReviewScreen( // TODO
    viewModel: TaskScreenViewModel
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxSize()
    ) {
        ReviewField(viewModel = viewModel)
    }
}

@Composable
fun ReviewField(
    viewModel: TaskScreenViewModel
) {
    val freeScrollState = rememberFreeScrollState()
    val primaryBackground = TRPTheme.colors.primaryBackground.copy(alpha = 0.6f)
    val secondaryBackground = TRPTheme.colors.secondaryBackground.copy(alpha = 0.6f)
    val selectedColor = TRPTheme.colors.okColor.copy(alpha = 0.3f)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp
    ) {
        Row {
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .weight(0.25f)
                    .background(TRPTheme.colors.cardButtonColor)
                    .verticalScroll(freeScrollState.verticalScrollState)
            ) {
                viewModel.codeList.forEachIndexed { index, _ ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 5.dp),
                        text = "${index + 1}",
                        color = TRPTheme.colors.primaryText,
                        textAlign = TextAlign.End,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 15.sp
                    )
                }
            }
            Column(
                modifier = Modifier
                    .height(400.dp)
                    .weight(2f)
                    .background(TRPTheme.colors.secondaryBackground)
                    .freeScroll(freeScrollState)
            ) {
                viewModel.codeList.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (viewModel.codeList[index].second) {
                                    selectedColor
                                } else if (index % 2 == 0) {
                                    primaryBackground
                                } else {
                                    secondaryBackground
                                }
                            )
                    ) {
                        Text(text = item.first, fontFamily = FontFamily.Monospace, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}