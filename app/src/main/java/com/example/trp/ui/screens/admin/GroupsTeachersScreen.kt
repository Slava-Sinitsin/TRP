package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.components.clearFocusOnTap
import com.example.trp.ui.components.tabs.DisabledInteractionSource
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.admin.GroupsTeachersScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GroupsTeachersScreen(
    onCreateGroupClick: () -> Unit,
    onGroupClick: (groupId: Int) -> Unit,
    onCreateTeacherClick: () -> Unit,
    onTeacherClick: (id: Int) -> Unit
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).groupsTeachersScreenViewModelFactory()
    val viewModel: GroupsTeachersScreenViewModel = viewModel(
        factory = GroupsTeachersScreenViewModel.provideGroupsTeachersScreenViewModel(
            factory
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap(),
        topBar = { }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TRPTheme.colors.primaryBackground)
                .padding(top = scaffoldPadding.calculateTopPadding())
        ) {
            TabRow(
                modifier = Modifier
                    .padding(top = 5.dp, start = 5.dp, end = 5.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color.Transparent),
                selectedTabIndex = viewModel.selectedTabIndex,
                containerColor = TRPTheme.colors.secondaryBackground,
                indicator = indicator,
                divider = {}
            ) {
                viewModel.usersScreens.forEachIndexed { index, item ->
                    Tab(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
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
                    .weight(1f),
                state = pagerState,
                pageCount = viewModel.usersScreens.size
            ) { index ->
                when (index) {
                    0 -> GroupsScreen(
                        viewModel = viewModel,
                        onCreateGroupClick = onCreateGroupClick,
                        onGroupClick = onGroupClick
                    )

                    1 -> TeachersScreen(
                        viewModel = viewModel, onCreateTeacherClick = onCreateTeacherClick,
                        onTeacherClick = onTeacherClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroupsScreen(
    viewModel: GroupsTeachersScreenViewModel,
    onCreateGroupClick: () -> Unit,
    onGroupClick: (groupId: Int) -> Unit
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
            modifier = Modifier.fillMaxSize()
        ) {
            item { CreateGroup(onCreateGroupClick = onCreateGroupClick) }
            items(count = viewModel.groups.size) { index ->
                Group(viewModel = viewModel, index = index, onGroupClick = onGroupClick)
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
fun CreateGroup(onCreateGroupClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onCreateGroupClick() },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues()
    ) {
        Text(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
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
fun Group(
    viewModel: GroupsTeachersScreenViewModel,
    index: Int,
    onGroupClick: (groupId: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { viewModel.getGroup(index).id?.let { onGroupClick(it) } },
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
fun TeachersScreen(
    viewModel: GroupsTeachersScreenViewModel,
    onCreateTeacherClick: () -> Unit,
    onTeacherClick: (id: Int) -> Unit
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
                CreateTeacher(onCreateTeacherClick = onCreateTeacherClick)
            }
            items(count = viewModel.teachers.size) { index ->
                Teacher(viewModel = viewModel, index = index, onTeacherClick = onTeacherClick)
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
fun CreateTeacher(onCreateTeacherClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onCreateTeacherClick() },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = TRPTheme.colors.cardButtonColor
        ),
        shape = RoundedCornerShape(30.dp),
        contentPadding = PaddingValues()
    ) {
        Text(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
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
fun Teacher(
    viewModel: GroupsTeachersScreenViewModel,
    index: Int,
    onTeacherClick: (id: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { viewModel.getTeacher(index).id?.let { onTeacherClick(it) } },
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
            text = viewModel.getTeacher(index = index).fullName.toString(),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}