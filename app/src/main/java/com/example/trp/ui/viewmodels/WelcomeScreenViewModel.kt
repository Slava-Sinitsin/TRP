package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.trp.repository.UserAPIRepositoryImpl
import com.example.trp.ui.screens.BottomBarScreen
import kotlinx.coroutines.launch

class WelcomeScreenViewModel(
    navController: NavHostController
) : ViewModel() {
    private val repository = UserAPIRepositoryImpl()

    private var user by mutableStateOf(repository.user)

    init {
        viewModelScope.launch {
            user = repository.getUser()
        }
    }

    var navController = navController
        private set

    val screens = listOf(
        BottomBarScreen.Discipline,
        BottomBarScreen.Home,
        BottomBarScreen.Me
    )

    fun isSelected(screen: BottomBarScreen): Boolean {
        return navController.currentDestination?.hierarchy?.any {
            it.route?.startsWith(screen.route) == true
        } == true
    }

    fun navigate(screen: BottomBarScreen) {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
}