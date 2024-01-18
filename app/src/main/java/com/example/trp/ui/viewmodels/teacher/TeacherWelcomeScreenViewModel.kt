package com.example.trp.ui.viewmodels.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.trp.domain.repository.UserAPIRepositoryImpl
import com.example.trp.ui.screens.teacher.TeacherBottomBarScreen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class TeacherWelcomeScreenViewModel @AssistedInject constructor(
    val repository: UserAPIRepositoryImpl,
    @Assisted
    val navController: NavHostController
) : ViewModel() {
    private var user by mutableStateOf(repository.user)

    @AssistedFactory
    interface Factory {
        fun create(navController: NavHostController): TeacherWelcomeScreenViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideTeacherWelcomeScreenViewModel(
            factory: Factory,
            navController: NavHostController
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(navController) as T
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            user = repository.getUser()
        }
    }

    val screens = listOf(
        TeacherBottomBarScreen.TeacherDisciplines,
        TeacherBottomBarScreen.Home,
        TeacherBottomBarScreen.Me
    )

    fun isSelected(screen: TeacherBottomBarScreen): Boolean {
        return navController.currentDestination?.hierarchy?.any {
            it.route?.startsWith(screen.route) == true
        } == true
    }

    fun navigate(screen: TeacherBottomBarScreen) {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
}