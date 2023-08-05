package com.example.trp.ui.viewmodels

import androidx.lifecycle.ViewModel

class TasksScreenViewModel(
    disciplineId: Int,
    var onTaskClick: (id: Int) -> Unit
) : ViewModel() {

    var disciplineId = disciplineId
        private set
}