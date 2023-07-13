package com.example.trp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.WelcomeScreenViewModel

@Composable
@Suppress("UNCHECKED_CAST")
fun WelcomeScreen() {
    val viewModel = viewModel<WelcomeScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WelcomeScreenViewModel() as T
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TRPTheme.colors.primaryBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                viewModel.user.login.toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 40.sp
            )
            Text(
                viewModel.user.role.toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 40.sp
            )
            Text(
                viewModel.user.fullName.toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 20.sp
            )
            Text(
                viewModel.user.exp.toString(),
                modifier = Modifier
                    .padding(5.dp),
                color = TRPTheme.colors.primaryText,
                fontSize = 20.sp
            )
        }
    }
}