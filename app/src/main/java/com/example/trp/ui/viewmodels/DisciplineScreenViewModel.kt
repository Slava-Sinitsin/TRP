package com.example.trp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class DisciplineScreenViewModel(
    navController: NavHostController
) : ViewModel() {

    var navController = navController
        private set

}