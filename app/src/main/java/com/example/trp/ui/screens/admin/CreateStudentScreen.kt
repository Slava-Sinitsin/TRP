package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.trp.ui.viewmodels.admin.CreateStudentScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudentScreen(groupId: Int, navController: NavHostController) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).createStudentScreenViewModel()
    val viewModel: CreateStudentScreenViewModel = viewModel(
        factory = CreateStudentScreenViewModel.provideCreateStudentScreenViewModel(
            factory,
            groupId
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap(),
        topBar = {
            CreateStudentScreenTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        CreateStudentScreen(
            viewModel = viewModel,
            navController = navController,
            paddingValues = scaffoldPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudentScreenTopAppBar(
    viewModel: CreateStudentScreenViewModel,
    navController: NavHostController
) {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = TRPTheme.colors.myYellow,
            titleContentColor = TRPTheme.colors.secondaryText,
        ),
        title = {
            Text(
                text = "Create new student",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Discard create new student"
                )
            }
        },
        actions = {
            IconButton(
                onClick = { viewModel.onApplyCreateStudentClick() },
                enabled = viewModel.studentApplyButtonEnabled
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "ApplyCreateGroupButton",
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudentScreen(
    viewModel: CreateStudentScreenViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    LaunchedEffect(viewModel.createError) {
        if (!viewModel.createError) {
            navController.popBackStack()
        }
    }
    Column(
        modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
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
            value = viewModel.fullName,
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
            isError = viewModel.fullName.isEmpty(),
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
            value = viewModel.username,
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
            isError = !viewModel.usernameCorrect,
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
            value = viewModel.password,
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
            isError = viewModel.password.isEmpty(),
            singleLine = true
        )
    }
}