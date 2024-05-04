package com.example.trp.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.trp.domain.navigation.common.Graph
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.common.MeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(navController: NavHostController) {
    val viewModel: MeScreenViewModel = hiltViewModel()

    LaunchedEffect(viewModel.isLogged) {
        if (!viewModel.isLogged) {
            navController.navigate(Graph.LOGIN) {
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    }

    Scaffold(containerColor = TRPTheme.colors.primaryBackground) { scaffoldPadding ->
        Row(
            modifier = Modifier
                .padding(top = scaffoldPadding.calculateTopPadding(), start = 5.dp, end = 5.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = viewModel.user.fullName ?: "",
                fontSize = 25.sp,
                color = TRPTheme.colors.primaryText
            )
            IconButton(onClick = { viewModel.onLogoutButtonClick() }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Logout button",
                    tint = TRPTheme.colors.errorColor
                )
            }
        }
    }
}