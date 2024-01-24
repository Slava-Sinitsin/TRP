package com.example.trp.domain.di

import com.example.trp.ui.viewmodels.student.StudentDisciplinesScreenViewModel
import com.example.trp.ui.viewmodels.student.StudentWelcomeScreenViewModel
import com.example.trp.ui.viewmodels.student.TaskScreenViewModel
import com.example.trp.ui.viewmodels.student.TasksScreenViewModel
import com.example.trp.ui.viewmodels.teacher.GroupsLabsScreenViewModel
import com.example.trp.ui.viewmodels.teacher.StudentsScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherDisciplinesViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherWelcomeScreenViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {
    fun studentWelcomeScreenViewModelFactory(): StudentWelcomeScreenViewModel.Factory
    fun studentDisciplinesScreenViewModelFactory(): StudentDisciplinesScreenViewModel.Factory
    fun tasksScreenViewModelFactory(): TasksScreenViewModel.Factory
    fun taskScreenViewModelFactory(): TaskScreenViewModel.Factory

    fun teacherWelcomeScreenViewModelFactory(): TeacherWelcomeScreenViewModel.Factory
    fun teacherDisciplinesViewModelFactory(): TeacherDisciplinesViewModel.Factory
    fun groupsLabsScreenViewModelFactory(): GroupsLabsScreenViewModel.Factory
    fun studentsScreenViewModelFactory(): StudentsScreenViewModel.Factory
}