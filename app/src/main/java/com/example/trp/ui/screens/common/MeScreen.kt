package com.example.trp.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
        LazyColumn(
            modifier = Modifier.padding(
                top = scaffoldPadding.calculateTopPadding(),
                start = 5.dp,
                end = 5.dp
            )
        ) {
            item {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.onLogoutButtonClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "Logout button",
                            tint = TRPTheme.colors.errorColor
                        )
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(TRPTheme.colors.secondaryBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Full name",
                            fontSize = 15.sp,
                            color = TRPTheme.colors.primaryText.copy(alpha = 0.6f)
                        )
                        Text(
                            text = viewModel.user.fullName ?: "",
                            fontSize = 15.sp,
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.size(5.dp)) }
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(TRPTheme.colors.secondaryBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Username",
                            fontSize = 15.sp,
                            color = TRPTheme.colors.primaryText.copy(alpha = 0.6f)
                        )
                        Text(
                            text = viewModel.user.username ?: "",
                            fontSize = 15.sp,
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.size(5.dp)) }
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(TRPTheme.colors.secondaryBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Role",
                            fontSize = 15.sp,
                            color = TRPTheme.colors.primaryText.copy(alpha = 0.6f)
                        )
                        Text(
                            text = when (viewModel.user.role) {
                                "ROLE_STUDENT" -> "Student"
                                "ROLE_LAB_WORK_TEACHER" -> "Assistant"
                                "ROLE_LECTURE_TEACHER" -> "Teacher"
                                "ROLE_ADMIN" -> "Admin"
                                else -> ""
                            },
                            fontSize = 15.sp,
                            color = TRPTheme.colors.primaryText
                        )
                    }
                }
            }
        }
    }
}