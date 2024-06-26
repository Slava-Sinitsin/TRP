package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeacherHomeScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(onEventClick: (id: Int) -> Unit) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).teacherHomeScreenViewModelFactory()
    val viewModel: TeacherHomeScreenViewModel = viewModel(
        factory = TeacherHomeScreenViewModel.provideTeacherHomeScreenViewModel(factory)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { scaffoldPadding ->
        Events(
            viewModel = viewModel,
            paddingValues = scaffoldPadding,
            onEventClick = onEventClick
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Events(
    viewModel: TeacherHomeScreenViewModel,
    paddingValues: PaddingValues,
    onEventClick: (id: Int) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .background(TRPTheme.colors.primaryBackground)
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = viewModel.events.size) { index ->
                Event(viewModel = viewModel, index = index, onEventClick = onEventClick)
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

@Composable
fun Event(
    viewModel: TeacherHomeScreenViewModel,
    index: Int,
    onEventClick: (id: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onEventClick(index) },
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
            text = "Event ${viewModel.getEvent(index = index)}",
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}