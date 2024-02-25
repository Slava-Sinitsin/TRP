package com.example.trp.ui.screens.admin

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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

    Groups(
        viewModel = viewModel,
        onAddDisciplineClick = onAddDisciplineClick,
        onDisciplineClick = onDisciplineClick
    )
}

@Composable
fun Groups(
    viewModel: AdminDisciplinesScreenViewModel,
    onAddDisciplineClick: () -> Unit,
    onDisciplineClick: (index: Int) -> Unit
) {
    LazyColumn {
        item { AddDiscipline(viewModel = viewModel, onAddDisciplineClick = onAddDisciplineClick) }
        items(viewModel.disciplines.size) { index ->
            Group(
                viewModel = viewModel,
                index = index,
                onDisciplineClick = onDisciplineClick
            )
        }
        item { Spacer(modifier = Modifier.size(100.dp)) }
    }
}

@Composable
fun AddDiscipline(
    viewModel: AdminDisciplinesScreenViewModel,
    onAddDisciplineClick: () -> Unit
) {
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
fun Group(
    viewModel: AdminDisciplinesScreenViewModel,
    index: Int,
    onDisciplineClick: (index: Int) -> Unit
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        onClick = {
            viewModel.getGroup(index = index).let { group ->
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
            textAlign = TextAlign.Start,
            text = viewModel.getGroup(index = index).name.toString(),
            color = TRPTheme.colors.primaryText,
            fontSize = 25.sp
        )
    }
}