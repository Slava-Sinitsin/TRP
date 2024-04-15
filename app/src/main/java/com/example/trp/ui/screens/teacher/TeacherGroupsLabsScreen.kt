package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.myTabIndicatorOffset
import com.example.trp.ui.components.tabs.DisabledInteractionSource
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.teacher.TeacherGroupsLabsScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeacherGroupsLabsScreen(
    disciplineId: Int,
    onGroupClick: (groupId: Int) -> Unit,
    onCreateLabClick: (disciplineId: Int) -> Unit,
    onLabClick: (labId: Int) -> Unit
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).teacherGroupsLabsScreenViewModelFactory()
    val viewModel: TeacherGroupsLabsScreenViewModel = viewModel(
        factory = TeacherGroupsLabsScreenViewModel.provideTeacherGroupsLabsScreenViewModel(
            factory,
            disciplineId
        )
    )

    val pagerState = rememberPagerState(0)
    LaunchedEffect(viewModel.selectedTabIndex) {
        pagerState.animateScrollToPage(viewModel.selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            viewModel.selectedTabIndex = pagerState.currentPage
        }
    }
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        MyNewIndicator(
            Modifier.myTabIndicatorOffset(tabPositions[viewModel.selectedTabIndex])
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
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
                viewModel.groupsTasksScreens.forEachIndexed { index, item ->
                    Tab(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .padding(bottom = 3.dp)
                            .zIndex(2f),
                        selected = index == viewModel.selectedTabIndex,
                        interactionSource = DisabledInteractionSource(),
                        onClick = { viewModel.selectedTabIndex = index },
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
                pageCount = viewModel.groupsTasksScreens.size
            ) { index ->
                if (index == 0) {
                    Groups(viewModel = viewModel, onGroupClick = onGroupClick)
                } else if (index == 1) {
                    Labs(
                        viewModel = viewModel,
                        onCreateLabClick = onCreateLabClick,
                        onLabClick = onLabClick
                    )
                }
            }
        }
    }
}

@Composable
fun MyNewIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(
                TRPTheme.colors.myYellow,
                RoundedCornerShape(20.dp),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
    }
}

@Composable
fun Groups(
    viewModel: TeacherGroupsLabsScreenViewModel,
    onGroupClick: (groupId: Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(viewModel.groups.size) { index ->
            Group(
                viewModel = viewModel,
                index = index,
                onGroupClick = onGroupClick
            )
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun Group(
    viewModel: TeacherGroupsLabsScreenViewModel,
    index: Int,
    onGroupClick: (groupId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            viewModel.getGroup(index = index)
                .let { task -> task.id?.let { groupId -> onGroupClick(groupId) } }
        },
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
            text = viewModel.getGroup(index = index).name.toString(),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Labs(
    viewModel: TeacherGroupsLabsScreenViewModel,
    onCreateLabClick: (disciplineId: Int) -> Unit,
    onLabClick: (taskId: Int) -> Unit
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                CreateLabToDiscipline(
                    viewModel = viewModel,
                    onCreateLabClick = onCreateLabClick
                )
            }
            items(count = viewModel.labs.size) { index ->
                Lab(viewModel = viewModel, index = index, onLabClick = onLabClick)
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
fun CreateLabToDiscipline(
    viewModel: TeacherGroupsLabsScreenViewModel,
    onCreateLabClick: (disciplineId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onCreateLabClick(viewModel.disciplineId) },
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
                .alpha(0.6f),
            text = "+",
            color = TRPTheme.colors.primaryText,
            fontSize = 45.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Lab(
    viewModel: TeacherGroupsLabsScreenViewModel,
    index: Int,
    onLabClick: (taskId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            viewModel.getLab(index = index)
                .let { task -> task.id?.let { taskId -> onLabClick(taskId) } }
        },
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
            text = viewModel.getLab(index = index).title.toString(),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}