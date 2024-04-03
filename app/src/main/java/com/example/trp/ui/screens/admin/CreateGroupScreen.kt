package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.trp.ui.viewmodels.admin.CreateGroupScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CreateGroupScreen(navController: NavHostController) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).createGroupScreenViewModelFactory()
    val viewModel: CreateGroupScreenViewModel = viewModel(
        factory = CreateGroupScreenViewModel.provideCreateGroupScreenViewModel(
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
            CreateGroupScreenTopBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
                .padding(top = scaffoldPadding.calculateTopPadding()),
            state = pagerState,
            pageCount = viewModel.createGroupScreens.size,
            userScrollEnabled = false
        ) { index ->
            when (index) {
                0 -> {
                    MainScreen(viewModel = viewModel)
                }

                1 -> {
                    CreateStudentScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreenTopBar(
    viewModel: CreateGroupScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = viewModel.topBarText,
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
            } else if (viewModel.selectedTabIndex == 1) {
                IconButton(onClick = { viewModel.onEditStudentBackButtonClick() }) {
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
                        viewModel.onApplyCreateGroupClick()
                        navController.popBackStack()
                    },
                    enabled = viewModel.groupApplyButtonEnabled
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "ApplyCreateGroupButton",
                    )
                }
            } else if (viewModel.selectedTabIndex == 1) {
                IconButton(
                    onClick = {
                        if (viewModel.studentEditMode) {
                            viewModel.onApplyEditStudentClick()
                        } else {
                            viewModel.onApplyCreateStudentClick()
                        }
                    },
                    enabled = viewModel.studentApplyButtonEnabled
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "ApplyCreateStudentButton",
                    )
                }
            }
        },
    )
}

@Composable
fun MainScreen(
    viewModel: CreateGroupScreenViewModel
) {
    Column(
        modifier = Modifier
            .background(TRPTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {
        NameField(viewModel = viewModel)
        Students(viewModel = viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameField(
    viewModel: CreateGroupScreenViewModel
) {
    Text(
        text = "Group name",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(top = 10.dp, start = 5.dp)
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp),
        textStyle = TextStyle.Default.copy(fontSize = 15.sp),
        value = viewModel.groupName,
        onValueChange = { viewModel.updateGroupNameValue(it) },
        placeholder = {
            Text(
                "Group name",
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
        isError = viewModel.groupName.isEmpty(),
        singleLine = true
    )
}

@Composable
fun Students(
    viewModel: CreateGroupScreenViewModel
) {
    AddStudentButton(viewModel = viewModel)
    Column {
        viewModel.students.forEachIndexed { index, _ ->
            Student(viewModel = viewModel, index = index)
        }
    }
}

@Composable
fun AddStudentButton(
    viewModel: CreateGroupScreenViewModel
) {
    Text(
        text = "Students",
        color = TRPTheme.colors.primaryText,
        fontSize = 15.sp,
        modifier = Modifier
            .alpha(0.6f)
            .padding(top = 10.dp, start = 5.dp)
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        onClick = { viewModel.createNewStudent() },
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
                text = "Create student",
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
fun Student(
    viewModel: CreateGroupScreenViewModel,
    index: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = { viewModel.onEditStudentButtonClick(index) },
            colors = ButtonDefaults.buttonColors(
                containerColor = TRPTheme.colors.cardButtonColor
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                text = viewModel.students[index].fullName ?: "",
                color = TRPTheme.colors.primaryText,
                fontSize = 15.sp
            )
        }
        OutlinedButton(
            modifier = Modifier.padding(start = 5.dp),
            onClick = { viewModel.onDeleteStudentClick(index) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudentScreen(
    viewModel: CreateGroupScreenViewModel
) {
    BackHandler(enabled = true, onBack = { viewModel.setPagerState(0) })
    Column(
        modifier = Modifier
            .background(TRPTheme.colors.primaryBackground)
            .fillMaxSize()
    ) {
        Text(
            text = "Full name",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
            modifier = Modifier
                .alpha(0.6f)
                .padding(top = 10.dp, start = 5.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.studentFullName,
            onValueChange = { viewModel.updateStudentFullNameValue(it) },
            placeholder = {
                Text(
                    "Full name",
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
            isError = viewModel.studentFullName.isEmpty(),
            singleLine = true
        )
        Text(
            text = "Username",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
            modifier = Modifier
                .alpha(0.6f)
                .padding(top = 10.dp, start = 5.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.studentUsername,
            onValueChange = { viewModel.updateStudentUsernameValue(it) },
            placeholder = {
                Text(
                    "Username",
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
            isError = viewModel.studentUsername.isEmpty(),
            singleLine = true
        )
        Text(
            text = "Password",
            color = TRPTheme.colors.primaryText,
            fontSize = 15.sp,
            modifier = Modifier
                .alpha(0.6f)
                .padding(top = 10.dp, start = 5.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp),
            textStyle = TextStyle.Default.copy(fontSize = 15.sp),
            value = viewModel.studentPassword,
            onValueChange = { viewModel.updateStudentPasswordValue(it) },
            placeholder = {
                Text(
                    "Password",
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
            isError = viewModel.studentPassword.isEmpty(),
            singleLine = true
        )
    }
}