package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
import com.example.trp.ui.components.tabs.DisabledInteractionSource
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.admin.AdminGroupsTasksScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdminGroupsTasksScreen(
    disciplineId: Int,
    onGroupClick: (groupId: Int) -> Unit,
    onAddTaskClick: (disciplineId: Int) -> Unit,
    onTaskClick: (taskId: Int) -> Unit
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).adminGroupsTasksScreenViewModelFactory()
    val viewModel: AdminGroupsTasksScreenViewModel = viewModel(
        factory = AdminGroupsTasksScreenViewModel.provideAdminGroupsTasksScreenViewModel(
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
            viewModel.setPagerState(pagerState.currentPage)
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
                        onClick = { viewModel.setPagerState(index) },
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
                    Tasks(
                        viewModel = viewModel,
                        onAddTaskClick = onAddTaskClick,
                        onTaskClick = onTaskClick
                    )
                }
            }
        }
    }
}

fun Modifier.myTabIndicatorOffset(
    currentTabPosition: TabPosition
): Modifier = composed {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing), label = ""
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing), label = ""
    )
    wrapContentSize(CenterStart)
        .width(currentTabWidth)
        .offset(x = indicatorOffset)
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
fun Groups(viewModel: AdminGroupsTasksScreenViewModel, onGroupClick: (groupId: Int) -> Unit) {
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
    viewModel: AdminGroupsTasksScreenViewModel,
    index: Int,
    onGroupClick: (groupId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { viewModel.getGroup(index = index).let { task -> task.id?.let { groupId -> onGroupClick(groupId) } } },
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

@Composable
fun Tasks(
    viewModel: AdminGroupsTasksScreenViewModel,
    onAddTaskClick: (disciplineId: Int) -> Unit,
    onTaskClick: (taskId: Int) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { AddTaskToDiscipline(viewModel = viewModel, onAddTaskClick = onAddTaskClick) }
        items(count = viewModel.tasks.size) { index ->
            Task(viewModel = viewModel, index = index, onTaskClick = onTaskClick)
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun AddTaskToDiscipline(
    viewModel: AdminGroupsTasksScreenViewModel,
    onAddTaskClick: (disciplineId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onAddTaskClick(viewModel.disciplineId) },
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
fun Task(
    viewModel: AdminGroupsTasksScreenViewModel,
    index: Int,
    onTaskClick: (taskId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { viewModel.getTask(index = index).let { task -> task.id?.let { taskId -> onTaskClick(taskId) } } },
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
            text = viewModel.getTask(index = index).title.toString(),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}