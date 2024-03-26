package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.admin.CreateTeacherScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeacherScreen(navController: NavHostController) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).createTeacherScreenViewModelFactory()
    val viewModel: CreateTeacherScreenViewModel = viewModel(
        factory = CreateTeacherScreenViewModel.provideCreateTeacherScreenViewModel(
            factory
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap(),
        topBar = {
            CreateTeacherScreenTopBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .background(TRPTheme.colors.primaryBackground)
                .fillMaxSize()
                .padding(top = scaffoldPadding.calculateTopPadding())
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
                value = viewModel.teacherFullName,
                onValueChange = { viewModel.updateTeacherFullNameValue(it) },
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
                isError = viewModel.teacherFullName.isEmpty(),
                singleLine = true
            )
            PositionToggle(viewModel = viewModel)
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
                value = viewModel.teacherUsername,
                onValueChange = { viewModel.updateTeacherUsernameValue(it) },
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
                isError = viewModel.teacherUsername.isEmpty(),
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
                value = viewModel.teacherPassword,
                onValueChange = { viewModel.updateTeacherPasswordValue(it) },
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
                isError = viewModel.teacherPassword.isEmpty(),
                singleLine = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeacherScreenTopBar(
    viewModel: CreateTeacherScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = "Create new teacher",
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
                    viewModel.onApplyButtonClick()
                    navController.popBackStack()
                },
                enabled = viewModel.applyButtonEnabled
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "ApplyCreateTeacherButton",
                )
            }
        },
    )
}

@Composable
fun PositionToggle(viewModel: CreateTeacherScreenViewModel) {
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
            text = "Position",
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
                viewModel.positions.forEach { text ->
                    Button(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(30.dp))
                            .background(
                                if (text == viewModel.selectedPosition) {
                                    TRPTheme.colors.myYellow
                                } else {
                                    TRPTheme.colors.cardButtonColor
                                }
                            ),
                        onClick = { viewModel.updatePositionValue(text) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (text == viewModel.selectedPosition) {
                                TRPTheme.colors.myYellow
                            } else {
                                TRPTheme.colors.cardButtonColor
                            }
                        )
                    ) {
                        Text(
                            text = text[5] + text.substring(6).lowercase(),
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    }
}