package com.example.trp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.WelcomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        Modifier
            .padding(),
        bottomBar = {
            NavigationBar(viewModel = viewModel)
        },
        containerColor = TRPTheme.colors.primaryBackground,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                viewModel.selectedItem.toString(),
                Modifier.align(Alignment.Center),
                fontSize = 40.sp,
                color = TRPTheme.colors.primaryText
            )
        }
    }
}

@Composable
fun NavigationBar(viewModel: WelcomeScreenViewModel) {
    val items = listOf(
        "Tasks",
        "Home",
        "Me"
    )
    val icons = listOf(
        Icons.Filled.Task,
        Icons.Filled.Home,
        Icons.Filled.Person
    )
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp),
        color = Color.Transparent,
        shape = RoundedCornerShape(30.dp),
        shadowElevation = 6.dp
    ) {
        NavigationBar(
            containerColor = TRPTheme.colors.secondaryBackground
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TRPTheme.colors.MyYellow,
                        selectedTextColor = TRPTheme.colors.MyYellow,
                        indicatorColor = TRPTheme.colors.iconBackground,
                        unselectedIconColor = TRPTheme.colors.icon,
                        unselectedTextColor = TRPTheme.colors.icon
                    ),
                    icon = { Icon(icons[index], contentDescription = item) },
                    label = { Text(item) },
                    selected = viewModel.selectedItem == index,
                    onClick = { viewModel.selectItem(index) }
                )
            }
        }
    }
}