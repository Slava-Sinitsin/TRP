package com.example.trp.ui.screens.teacher

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.ui.viewmodels.teacher.CreateTeamScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@Composable
fun CreateTeamScreen(
    groupId: Int
) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).createTeamScreenViewModelFactory()
    val viewModel: CreateTeamScreenViewModel = viewModel(
        factory = CreateTeamScreenViewModel.provideCreateTeamScreenViewModel(
            factory,
            groupId
        )
    )
    Text(text = viewModel.groupId.toString())
}