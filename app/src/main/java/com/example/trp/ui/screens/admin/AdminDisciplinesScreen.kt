package com.example.trp.ui.screens.admin

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.admin.AdminDisciplinesScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@Composable
fun AdminDisciplinesScreen(
    onDisciplineClick: (index: Int) -> Unit,
    onAddDisciplineClick: () -> Unit
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).adminDisciplinesScreenViewModelFactory()
    val viewModel: AdminDisciplinesScreenViewModel = viewModel(
        factory = AdminDisciplinesScreenViewModel.provideAdminDisciplinesScreenViewModel(
            factory
        )
    )

    Disciplines(
        viewModel = viewModel,
        onAddDisciplineClick = onAddDisciplineClick,
        onDisciplineClick = onDisciplineClick
    )
    if (viewModel.errorMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
        viewModel.updateErrorMessage("")
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Disciplines(
    viewModel: AdminDisciplinesScreenViewModel,
    onAddDisciplineClick: () -> Unit,
    onDisciplineClick: (index: Int) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground)
            .pullRefresh(state = pullRefreshState)
    ) {
        LazyColumn {
            item {
                AddDiscipline(onAddDisciplineClick = onAddDisciplineClick)
            }
            items(viewModel.disciplines.size) { index ->
                Discipline(
                    viewModel = viewModel,
                    index = index,
                    onDisciplineClick = onDisciplineClick
                )
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
fun AddDiscipline(onAddDisciplineClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = { onAddDisciplineClick() },
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
fun Discipline(
    viewModel: AdminDisciplinesScreenViewModel,
    index: Int,
    onDisciplineClick: (index: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            viewModel.getDiscipline(index = index).let { group ->
                group.id?.let { id -> onDisciplineClick(id) }
            }
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
            text = "${viewModel.getDiscipline(index = index).name} ${viewModel.getDiscipline(index = index).year}",
            textAlign = TextAlign.Start,
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}