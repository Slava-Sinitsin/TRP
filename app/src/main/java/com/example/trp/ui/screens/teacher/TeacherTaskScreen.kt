package com.example.trp.ui.screens.teacher

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.chihsuanwu.freescroll.freeScroll
import com.chihsuanwu.freescroll.rememberFreeScrollState
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.components.clickableWithoutRipple
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeacherTaskScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTaskScreen(
    taskId: Int,
    navController: NavHostController
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).teacherTaskScreenViewModelFactory()
    val viewModel: TeacherTaskScreenViewModel = viewModel(
        factory = TeacherTaskScreenViewModel.provideTeacherTaskScreenViewModel(
            factory,
            taskId
        )
    )

    Scaffold(
        modifier = Modifier.clearFocusOnTap(),
        containerColor = TRPTheme.colors.primaryBackground,
        topBar = {
            TeacherTaskScreenTopBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ReviewField(
                    viewModel = viewModel,
                    paddingValues = scaffoldPadding
                )
            }
            item { Comments(viewModel = viewModel) }
            item { AcceptSubmitRejectButtons(viewModel = viewModel) }
            item { Spacer(modifier = Modifier.size(100.dp)) }
        }
        if (viewModel.showRejectDialog) {
            RejectDialog(viewModel = viewModel, navController = navController)
        }
        if (viewModel.showSubmitDialog) {
            SubmitDialog(viewModel = viewModel, navController = navController)
        }
        if (viewModel.showAcceptDialog) {
            AcceptDialog(viewModel = viewModel, navController = navController)
        }
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
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
                text = viewModel.task.title ?: "",
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
fun ReviewField(
    viewModel: TeacherTaskScreenViewModel,
    paddingValues: PaddingValues
) {
    val freeScrollState = rememberFreeScrollState()
    val primaryBackground = TRPTheme.colors.primaryBackground.copy(alpha = 0.6f)
    val secondaryBackground = TRPTheme.colors.secondaryBackground.copy(alpha = 0.6f)
    val selectedColor = TRPTheme.colors.okColor.copy(alpha = 0.3f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding() + 10.dp,
                start = 5.dp,
                end = 5.dp
            ),
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
                            .clickableWithoutRipple(
                                onClick = {
                                    viewModel.onCodeLineClick(index + 1)
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

@Composable
fun Comments(
    viewModel: TeacherTaskScreenViewModel
) {
    Column(modifier = Modifier.padding(top = 10.dp)) {
        viewModel.commentList.forEachIndexed { index, _ ->
            Comment(viewModel = viewModel, index = index)
        }
    }
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
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
            .padding(vertical = 10.dp, horizontal = 5.dp),
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
fun RejectDialog(viewModel: TeacherTaskScreenViewModel, navController: NavHostController) {
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
                onClick = {
                    viewModel.rejectConfirmButtonClick()
                    navController.popBackStack()
                },
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
fun SubmitDialog(viewModel: TeacherTaskScreenViewModel, navController: NavHostController) {
    AlertDialog(
        onDismissRequest = { viewModel.submitDismissButtonClick() },
        title = {
            Text(
                text = "Submit these comments",
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
                        "General comment",
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
                onClick = {
                    viewModel.submitConfirmButtonClick()
                    navController.popBackStack()
                },
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
fun AcceptDialog(viewModel: TeacherTaskScreenViewModel, navController: NavHostController) {
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
                        .height(100.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                    value = viewModel.reviewMessage,
                    onValueChange = { viewModel.updateReviewMessage(it) },
                    placeholder = {
                        Text(
                            "Message",
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
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Mark: ${(viewModel.mark * 100).toInt()}",
                    color = TRPTheme.colors.primaryText
                )
                Slider(
                    value = viewModel.mark,
                    onValueChange = {
                        viewModel.updateMark(it)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = TRPTheme.colors.myYellow,
                        activeTrackColor = TRPTheme.colors.myYellow,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent
                    ),
                    valueRange = 0f..viewModel.maxMark,
                    steps = (viewModel.maxMark * 100).toInt()
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.alpha(if (viewModel.reviewMessage.isEmpty()) 0.6f else 1.0f),
                onClick = {
                    viewModel.acceptConfirmButtonClick()
                    navController.popBackStack()
                },
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