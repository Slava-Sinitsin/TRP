package com.example.trp.ui.screens.admin

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.TabIndicator
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.components.myTabIndicatorOffset
import com.example.trp.ui.components.tabs.DisabledInteractionSource
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
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.updateErrorMessage("")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeacherScreenTopBar(
    viewModel: CreateTeacherScreenViewModel,
    navController: NavHostController
) {
    LaunchedEffect(viewModel.createError) {
        if (!viewModel.createError && viewModel.responseSuccess) {
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
                onClick = { viewModel.onApplyButtonClick() },
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
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        TabIndicator(Modifier.myTabIndicatorOffset(tabPositions[viewModel.selectedPositionIndex]))
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
            text = "Position",
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
            selectedTabIndex = viewModel.selectedPositionIndex,
            indicator = indicator,
            divider = {},
            containerColor = TRPTheme.colors.primaryBackground
        ) {
            viewModel.positions.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier.zIndex(2f),
                    selected = index == viewModel.selectedPositionIndex,
                    interactionSource = DisabledInteractionSource(),
                    onClick = { viewModel.updatePositionValue(index) },
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