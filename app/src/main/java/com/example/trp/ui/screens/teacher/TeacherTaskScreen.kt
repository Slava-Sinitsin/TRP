package com.example.trp.ui.screens.teacher

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.trp.domain.di.ViewModelFactoryProvider
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = TRPTheme.colors.primaryBackground,
        topBar = {
            TaskCenterAlignedTopAppBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { scaffoldPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            ReviewField(
                viewModel = viewModel,
                paddingValues = scaffoldPadding
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCenterAlignedTopAppBar(
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
                text = "${viewModel.disciplineName} ${viewModel.task.title ?: ""}",
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
    val scrollState = rememberScrollState()
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
                    .verticalScroll(scrollState)
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
                    .horizontalScroll(rememberScrollState())
                    .background(TRPTheme.colors.secondaryBackground)
                    .verticalScroll(scrollState)
            ) {
                viewModel.codeList.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .background(TRPTheme.colors.secondaryBackground)
                            .clickableWithoutRipple(
                                onClick = { Log.e("Click", "Click ${index + 1}") }
                            )
                    ) {
                        Text(text = item, fontFamily = FontFamily.Monospace, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}