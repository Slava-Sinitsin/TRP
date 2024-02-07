package com.example.trp.domain.di

import com.example.trp.ui.viewmodels.admin.AdminDisciplinesScreenViewModel
import com.example.trp.ui.viewmodels.admin.AdminGroupsTasksScreenViewModel
import com.example.trp.ui.viewmodels.admin.AdminWelcomeScreenViewModel
import com.example.trp.ui.viewmodels.student.StudentDisciplinesScreenViewModel
import com.example.trp.ui.viewmodels.student.StudentWelcomeScreenViewModel
import com.example.trp.ui.viewmodels.student.TaskScreenViewModel
import com.example.trp.ui.viewmodels.student.TasksScreenViewModel
import com.example.trp.ui.viewmodels.teacher.AddNewTaskScreenViewModel
import com.example.trp.ui.viewmodels.teacher.AddTaskToStudentScreenViewModel
import com.example.trp.ui.viewmodels.teacher.StudentInfoScreenViewModel
import com.example.trp.ui.viewmodels.teacher.StudentsScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TaskInfoScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherDisciplinesScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherGroupsTasksScreenViewModel
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
    fun teacherDisciplinesScreenViewModelFactory(): TeacherDisciplinesScreenViewModel.Factory
    fun teacherGroupsTasksScreenViewModelFactory(): TeacherGroupsTasksScreenViewModel.Factory
    fun studentsScreenViewModelFactory(): StudentsScreenViewModel.Factory
    fun taskInfoScreenViewModelFactory(): TaskInfoScreenViewModel.Factory
    fun addNewTaskScreenViewModelFactory(): AddNewTaskScreenViewModel.Factory
    fun studentInfoScreenViewModelFactory(): StudentInfoScreenViewModel.Factory
    fun addTaskToStudentScreenViewModelFactory(): AddTaskToStudentScreenViewModel.Factory

    fun adminWelcomeScreenViewModelFactory(): AdminWelcomeScreenViewModel.Factory
    fun adminDisciplinesScreenViewModelFactory(): AdminDisciplinesScreenViewModel.Factory
    fun adminGroupsTasksScreenViewModelFactory(): AdminGroupsTasksScreenViewModel.Factory
}