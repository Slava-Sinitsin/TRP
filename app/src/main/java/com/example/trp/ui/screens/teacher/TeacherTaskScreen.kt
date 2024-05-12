package com.example.trp.ui.screens.teacher

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chihsuanwu.freescroll.freeScroll
import com.chihsuanwu.freescroll.rememberFreeScrollState
import com.example.trp.data.mappers.tasks.CodeThread
import com.example.trp.data.mappers.tasks.TaskMessage
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.TabIndicator
import com.example.trp.ui.components.TaskStatus
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.components.clickableWithoutRipple
import com.example.trp.ui.components.myTabIndicatorOffset
import com.example.trp.ui.components.tabs.DisabledInteractionSource
import com.example.trp.ui.components.tabs.ReviewTabs
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeacherTaskScreenViewModel
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TeacherTaskScreen(
    teamAppointmentId: Int,
    onOldCodeReviewClick: (codeReviewId: Int) -> Unit,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).teacherTaskScreenViewModelFactory()
    val viewModel: TeacherTaskScreenViewModel = viewModel(
        factory = TeacherTaskScreenViewModel.provideTeacherTaskScreenViewModel(
            factory,
            teamAppointmentId
        )
    )
    if (viewModel.reviewScreens.isNotEmpty()) {
        val pagerState = rememberPagerState(viewModel.selectedTabIndex)
        LaunchedEffect(viewModel.selectedTabIndex) {
            pagerState.animateScrollToPage(viewModel.selectedTabIndex)
        }
        LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
            if (!pagerState.isScrollInProgress) {
                viewModel.selectedTabIndex = pagerState.currentPage
            }
        }
        val indicator = @Composable { tabPositions: List<TabPosition> ->
            TabIndicator(Modifier.myTabIndicatorOffset(tabPositions[viewModel.selectedTabIndex]))
        }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .clearFocusOnTap(),
            containerColor = TRPTheme.colors.primaryBackground,
            topBar = {
                TeacherTaskScreenTopBar(
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
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                        .clip(shape = RoundedCornerShape(20.dp)),
                    selectedTabIndex = viewModel.selectedTabIndex,
                    containerColor = TRPTheme.colors.secondaryBackground,
                    indicator = indicator,
                    divider = {}
                ) {
                    viewModel.reviewScreens.forEachIndexed { index, item ->
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
                    pageCount = viewModel.reviewScreens.size
                ) { index ->
                    when (viewModel.reviewScreens[index]) {
                        ReviewTabs.Description -> {
                            DescriptionScreen(viewModel = viewModel)
                        }

                        ReviewTabs.Review -> {
                            ReviewScreen(viewModel = viewModel)
                        }

                        ReviewTabs.History -> {
                            HistoryScreen(
                                viewModel = viewModel,
                                onOldCodeReviewClick = onOldCodeReviewClick
                            )
                        }
                    }
                }
            }
            if (viewModel.showRejectDialog) {
                RejectDialog(viewModel = viewModel)
            }
            if (viewModel.showSubmitDialog) {
                SubmitDialog(viewModel = viewModel)
            }
            if (viewModel.showAcceptDialog) {
                AcceptDialog(viewModel = viewModel)
            }
            if (viewModel.errorMessage.isNotEmpty()) {
                Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT)
                    .show()
                viewModel.updateErrorMessage("")
            }
            LaunchedEffect(viewModel.responseSuccess) {
                if (viewModel.responseSuccess) {
                    navController.popBackStack()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTaskScreenTopBar(
    viewModel: TeacherTaskScreenViewModel,
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
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "BackIconButton"
                )
            }
        },
        actions = { },
    )
}

@Composable
fun DescriptionScreen(viewModel: TeacherTaskScreenViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { Spacer(modifier = Modifier.size(5.dp)) }
        item { TitleField(viewModel = viewModel) }
        item { DescriptionField(viewModel = viewModel) }
        item { TestsField(viewModel = viewModel) }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleField(viewModel: TeacherTaskScreenViewModel) {
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
fun DescriptionField(viewModel: TeacherTaskScreenViewModel) {
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
fun TestsField(viewModel: TeacherTaskScreenViewModel) {
    if (viewModel.teamAppointment.task?.tests?.isNotEmpty() == true) {
        Text(
            text = "Tests",
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (test.isOpen == true) {
                        Icon(
                            imageVector = Icons.Filled.LockOpen,
                            tint = TRPTheme.colors.primaryText.copy(alpha = 0.6f),
                            contentDescription = "This test is lock"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            tint = TRPTheme.colors.primaryText.copy(alpha = 0.6f),
                            contentDescription = "This test is unlock"
                        )
                    }
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
}

@Composable
fun ReviewScreen(viewModel: TeacherTaskScreenViewModel) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxSize()
    ) {
        item { Spacer(modifier = Modifier.size(5.dp)) }
        item { ReviewField(viewModel = viewModel) }
        item { Spacer(modifier = Modifier.size(5.dp)) }
        item { Messages(viewModel = viewModel) }
        item { Spacer(modifier = Modifier.size(5.dp)) }
        if (viewModel.teamAppointment.status != TaskStatus.Rated.status) {
            item { Comments(viewModel = viewModel) }
            item { AcceptSubmitRejectButtons(viewModel = viewModel) }
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun ReviewField(viewModel: TeacherTaskScreenViewModel) {
    val freeScrollState = rememberFreeScrollState()
    val primaryBackground = TRPTheme.colors.primaryBackground.copy(alpha = 0.6f)
    val secondaryBackground = TRPTheme.colors.secondaryBackground.copy(alpha = 0.6f)
    val selectedColor = TRPTheme.colors.okColor.copy(alpha = 0.3f)
    val theme = TRPTheme.colors.codeTheme

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
                viewModel.padCodeList.forEachIndexed { index, _ ->
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
                viewModel.padCodeList.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .background(
                                if (viewModel.padCodeList[index].second) {
                                    selectedColor
                                } else if (index % 2 == 0) {
                                    primaryBackground
                                } else {
                                    secondaryBackground
                                }
                            )
                            .clickableWithoutRipple(
                                onClick = {
                                    viewModel.onCodeLineClick(index + 1)
                                }
                            )
                    ) {
                        Text(
                            text = parseCodeAsAnnotatedString(
                                parser = viewModel.parser,
                                theme = theme,
                                lang = viewModel.language,
                                code = item.first.text
                            ),
                            fontFamily = FontFamily.Monospace, fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Messages(viewModel: TeacherTaskScreenViewModel) {
    if (viewModel.currentCodeReview.taskMessages?.isNotEmpty() == true
        || viewModel.currentCodeReview.codeThreads?.isNotEmpty() == true
    ) {
        Column {
            Text(
                text = "Comments",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.size(10.dp))
            viewModel.currentCodeReview.taskMessages?.forEach { note ->
                TaskMessage(
                    viewModel = viewModel,
                    taskMessage = note
                )
                Spacer(modifier = Modifier.size(10.dp))
            }
            Spacer(modifier = Modifier.size(10.dp))
            viewModel.currentCodeReview.codeThreads?.forEachIndexed { index, codeThread ->
                CodeThreadField(
                    viewModel = viewModel,
                    codeThread = codeThread,
                    index = index
                )
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun TaskMessage(
    viewModel: TeacherTaskScreenViewModel,
    taskMessage: TaskMessage
) {
    Surface(
        color = TRPTheme.colors.secondaryBackground,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${taskMessage.author?.fullName} ${
                    taskMessage.createdAt?.let { viewModel.formatDate(it) }
                }",
                color = TRPTheme.colors.primaryText.copy(alpha = 0.6f),
                textAlign = if (taskMessage.author?.username == viewModel.user.username) {
                    TextAlign.End
                } else {
                    TextAlign.Start
                }
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = taskMessage.message ?: "",
                color = TRPTheme.colors.primaryText,
                textAlign = if (taskMessage.author?.username == viewModel.user.username) {
                    TextAlign.End
                } else {
                    TextAlign.Start
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeThreadField(
    viewModel: TeacherTaskScreenViewModel,
    codeThread: CodeThread,
    index: Int
) {
    val theme = TRPTheme.colors.codeTheme
    Surface(
        color = TRPTheme.colors.secondaryBackground,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp
    ) {
        val code = codeThread.beginLineNumber?.let { beginLineNumber ->
            codeThread.endLineNumber?.let { endLineNumber ->
                IntRange(beginLineNumber, endLineNumber)
            }
        }?.let { range ->
            viewModel.getCodeInRange(range).map {
                it.first to parseCodeAsAnnotatedString(
                    parser = viewModel.parser,
                    theme = theme,
                    lang = viewModel.language,
                    code = it.second.text
                )
            }
        }

        Column {
            Row {
                Column(modifier = Modifier.background(TRPTheme.colors.cardButtonColor)) {
                    code?.forEach { codeLine ->
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "${codeLine.first}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 15.sp,
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TRPTheme.colors.okColor.copy(alpha = 0.3f))
                        .horizontalScroll(rememberScrollState())
                ) {
                    code?.forEach { codeLine ->
                        Text(
                            text = codeLine.second,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
            codeThread.messages?.forEach { message ->
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth(),
                    text = "${message.author?.fullName} ${
                        message.createdAt?.let { viewModel.formatDate(it) }
                    }",
                    color = TRPTheme.colors.primaryText.copy(alpha = 0.6f),
                    textAlign = if (message.author?.username == viewModel.user.username) {
                        TextAlign.End
                    } else {
                        TextAlign.Start
                    }
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth(),
                    text = message.message ?: "",
                    color = TRPTheme.colors.primaryText,
                    textAlign = if (message.author?.username == viewModel.user.username) {
                        TextAlign.End
                    } else {
                        TextAlign.Start
                    }
                )
                Spacer(modifier = Modifier.size(5.dp))
            }
            if (viewModel.teamAppointment.status != TaskStatus.WaitingForGrade.status
                && viewModel.teamAppointment.status != TaskStatus.Rated.status
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                    value = viewModel.codeThreadCommentList[index],
                    onValueChange = { viewModel.updateCodeThreadComment(index, it) },
                    placeholder = {
                        Text(
                            "Comment",
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
                        errorCursorColor = TRPTheme.colors.primaryText
                    ),
                    singleLine = true,
                    isError = false
                )
            }
        }
    }
}

@Composable
fun Comments(
    viewModel: TeacherTaskScreenViewModel
) {
    Column {
        viewModel.commentList.forEachIndexed { index, _ ->
            Comment(viewModel = viewModel, index = index)
        }
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        onClick = { viewModel.addComment() },
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
fun Comment(
    viewModel: TeacherTaskScreenViewModel,
    index: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.getComment(index).lines ?: "",
            onValueChange = {
                viewModel.updateCommentLines(
                    index = index,
                    newCommentLines = it
                )
            },
            placeholder = {
                Text(
                    "Lines",
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
            isError = viewModel.getComment(index).isMatch == false,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(start = 5.dp)
                .fillMaxHeight()
                .weight(3f),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.getComment(index).comment ?: "",
            onValueChange = {
                viewModel.updateComment(
                    index = index,
                    newComment = it
                )
            },
            placeholder = {
                Text(
                    "Comment",
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
            isError = viewModel.getComment(index).comment?.isEmpty() ?: true
        )
        OutlinedButton(
            modifier = Modifier
                .width(65.dp)
                .fillMaxHeight()
                .padding(start = 5.dp),
            onClick = { viewModel.onDeleteCommentLineClick(index) },
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
                contentDescription = "DeleteArgumentIconButton",
                tint = TRPTheme.colors.errorColor
            )
        }
    }
}

@Composable
fun AcceptSubmitRejectButtons(
    viewModel: TeacherTaskScreenViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onRejectButtonClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = TRPTheme.colors.errorColor,
                disabledContainerColor = TRPTheme.colors.errorColor.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(30.dp),
            contentPadding = PaddingValues(),
            enabled = viewModel.finishButtonsEnabled
        ) {
            Text(
                text = "Reject",
                color = TRPTheme.colors.secondaryText
            )
        }
        Spacer(modifier = Modifier.size(5.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onSubmitButtonClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = TRPTheme.colors.myYellow,
                disabledContainerColor = TRPTheme.colors.myYellow.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(30.dp),
            contentPadding = PaddingValues(),
            enabled = viewModel.finishButtonsEnabled
        ) {
            Text(
                text = "Submit",
                color = TRPTheme.colors.secondaryText
            )
        }
        Spacer(modifier = Modifier.size(5.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = { viewModel.onAcceptButtonClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = TRPTheme.colors.okColor,
                disabledContainerColor = TRPTheme.colors.okColor.copy(alpha = 0.6f)
            ),
            shape = RoundedCornerShape(30.dp),
            contentPadding = PaddingValues(),
            enabled = viewModel.finishButtonsEnabled
        ) {
            Text(
                text = "Accept",
                color = TRPTheme.colors.secondaryText
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RejectDialog(viewModel: TeacherTaskScreenViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.rejectDismissButtonClick() },
        title = {
            Text(
                text = "Reject",
                color = TRPTheme.colors.primaryText
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
        text = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                value = viewModel.reviewMessage,
                onValueChange = { viewModel.updateReviewMessage(it) },
                placeholder = {
                    Text(
                        "Rejection reason",
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
                isError = viewModel.reviewMessage.isEmpty()
            )
        },
        confirmButton = {
            Button(
                modifier = Modifier.alpha(if (viewModel.reviewMessage.isEmpty()) 0.6f else 1.0f),
                onClick = { viewModel.rejectConfirmButtonClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = TRPTheme.colors.myYellow,
                    disabledContainerColor = TRPTheme.colors.myYellow
                ),
                enabled = viewModel.reviewMessage.isNotEmpty()
            ) {
                Text(
                    text = "Confirm",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { viewModel.rejectDismissButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.errorColor)
            ) {
                Text(
                    text = "Dismiss",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitDialog(viewModel: TeacherTaskScreenViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.submitDismissButtonClick() },
        title = {
            Text(
                text = "General comment",
                color = TRPTheme.colors.primaryText
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
        text = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                value = viewModel.reviewMessage,
                onValueChange = { viewModel.updateReviewMessage(it) },
                placeholder = {
                    Text(
                        "Comment",
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
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { viewModel.submitConfirmButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.myYellow)
            ) {
                Text(
                    text = "Confirm",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { viewModel.submitDismissButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.errorColor)
            ) {
                Text(
                    text = "Dismiss",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcceptDialog(viewModel: TeacherTaskScreenViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.acceptDismissButtonClick() },
        title = {
            Text(
                text = "Accept",
                color = TRPTheme.colors.primaryText
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 5.dp, horizontal = 5.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                    value = viewModel.reviewMessage,
                    onValueChange = { viewModel.updateReviewMessage(it) },
                    placeholder = {
                        Text(
                            "Comment",
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
                    )
                )
                viewModel.teamAppointment.team?.students?.forEachIndexed { index, student ->
                    Text(
                        text = "${student.fullName}: ${(viewModel.rateList[index].grade)}",
                        color = TRPTheme.colors.primaryText
                    )
                    Slider(
                        value = viewModel.rateList[index].grade?.toFloat()?.div(100f) ?: 0f,
                        onValueChange = {
                            viewModel.updateMark(it, index)
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = TRPTheme.colors.myYellow,
                            activeTrackColor = TRPTheme.colors.myYellow,
                            activeTickColor = Color.Transparent,
                            inactiveTickColor = Color.Transparent
                        ),
                        valueRange = 0f..viewModel.maxRate,
                        steps = (viewModel.maxRate * 100).toInt()
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.acceptConfirmButtonClick() },
                colors = ButtonDefaults.buttonColors(containerColor = TRPTheme.colors.myYellow)
            ) {
                Text(
                    text = "Confirm",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { viewModel.acceptDismissButtonClick() },
                colors = ButtonDefaults.buttonColors(TRPTheme.colors.errorColor)
            ) {
                Text(
                    text = "Dismiss",
                    color = TRPTheme.colors.secondaryText
                )
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(
    viewModel: TeacherTaskScreenViewModel,
    onOldCodeReviewClick: (codeReviewId: Int) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxSize()
        ) {
            items(viewModel.codeReviews.size) { index ->
                Button(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxSize(),
                    onClick = { viewModel.codeReviews[index].id?.let { onOldCodeReviewClick(it) } },
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TRPTheme.colors.cardButtonColor
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp, bottom = 16.dp)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.Start,
                        text = "Review ${viewModel.codeReviews[index].id}",
                        color = TRPTheme.colors.primaryText,
                        fontSize = 25.sp
                    )
                }
            }
            item { Spacer(modifier = Modifier.size(100.dp)) }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = viewModel.isRefreshing,
            state = pullRefreshState,
            backgroundColor = TRPTheme.colors.primaryBackground,
            contentColor = TRPTheme.colors.myYellow
        )
    }
}