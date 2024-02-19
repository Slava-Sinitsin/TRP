package com.example.trp.ui.screens.admin

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trp.domain.di.ViewModelFactoryProvider
import com.example.trp.domain.navigation.admin.AdminWelcomeNavGraph
import com.example.trp.ui.theme.TRPTheme
import com.example.trp.ui.viewmodels.admin.AdminWelcomeScreenViewModel
import dagger.hilt.android.EntryPointAccessors

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminWelcomeScreen(navController: NavHostController = rememberNavController()) {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        ViewModelFactoryProvider::class.java
    ).adminWelcomeScreenViewModelFactory()
    val viewModel: AdminWelcomeScreenViewModel = viewModel(
        factory = AdminWelcomeScreenViewModel.provideAdminWelcomeScreenViewModel(
            factory,
            navController
        )
    )

    viewModel.navController.currentBackStackEntryAsState().value?.destination
    Scaffold(
        Modifier.padding(),
        bottomBar = {
            AdminNavigationBar(
                viewModel = viewModel
            )
        },
        containerColor = TRPTheme.colors.primaryBackground,
    ) {
        AdminWelcomeNavGraph(navController = navController)
    }
}

@Composable
fun AdminNavigationBar(
    viewModel: AdminWelcomeScreenViewModel
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