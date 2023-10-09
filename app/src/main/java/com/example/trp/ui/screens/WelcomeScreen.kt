package com.example.trp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trp.navigation.graphs.WelcomeNavGraph
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.WelcomeScreenViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("UNCHECKED_CAST")
fun WelcomeScreen(navController: NavHostController = rememberNavController()) {

    val viewModel = viewModel<WelcomeScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WelcomeScreenViewModel(navController) as T
            }
        }
    )

    viewModel.navController.currentBackStackEntryAsState().value?.destination

    Scaffold(
        Modifier.padding(),
        bottomBar = {
            NavigationBar(
                viewModel = viewModel
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
    ) {
        WelcomeNavGraph(navController = navController)
    }
}

@Composable
fun NavigationBar(
    viewModel: WelcomeScreenViewModel
) {
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
            viewModel.screens.forEach { screen ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TRPTheme.colors.myYellow,
                        selectedTextColor = TRPTheme.colors.myYellow,
                        indicatorColor = TRPTheme.colors.iconBackground,
                        unselectedIconColor = TRPTheme.colors.icon,
                        unselectedTextColor = TRPTheme.colors.icon
                    ),
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = viewModel.isSelected(screen),
                    onClick = {
                        viewModel.navigate(screen)
                    }
                )
            }
        }
    }
}