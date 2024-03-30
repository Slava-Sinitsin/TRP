package com.example.trp.domain.di

import com.example.trp.ui.viewmodels.admin.AddNewDisciplineScreenViewModel
import com.example.trp.ui.viewmodels.admin.AdminDisciplinesScreenViewModel
import com.example.trp.ui.viewmodels.admin.AdminGroupsTasksScreenViewModel
import com.example.trp.ui.viewmodels.admin.AdminWelcomeScreenViewModel
import com.example.trp.ui.viewmodels.admin.CreateGroupScreenViewModel
import com.example.trp.ui.viewmodels.admin.CreateTeacherScreenViewModel
import com.example.trp.ui.viewmodels.admin.GroupsTeachersScreenViewModel
import com.example.trp.ui.viewmodels.common.CreateTaskScreenViewModel
import com.example.trp.ui.viewmodels.common.CreateNewTestScreenViewModel
import com.example.trp.ui.viewmodels.common.TaskInfoTestsScreenViewModel
import com.example.trp.ui.viewmodels.student.StudentDisciplinesScreenViewModel
import com.example.trp.ui.viewmodels.student.StudentWelcomeScreenViewModel
import com.example.trp.ui.viewmodels.student.TaskScreenViewModel
import com.example.trp.ui.viewmodels.student.TasksScreenViewModel
import com.example.trp.ui.viewmodels.teacher.AddTaskToStudentScreenViewModel
import com.example.trp.ui.viewmodels.teacher.CreateLabViewModel
import com.example.trp.ui.viewmodels.teacher.CreateTeamScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherDisciplinesScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherGroupsLabsScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherTaskScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherTasksScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeacherWelcomeScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeamInfoScreenViewModel
import com.example.trp.ui.viewmodels.teacher.TeamsScreenViewModel
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
    fun teacherGroupsLabsScreenViewModelFactory(): TeacherGroupsLabsScreenViewModel.Factory
    fun studentsScreenViewModelFactory(): TeamsScreenViewModel.Factory
    fun taskInfoTestsScreenViewModelFactory(): TaskInfoTestsScreenViewModel.Factory
    fun createTaskScreenViewModelFactory(): CreateTaskScreenViewModel.Factory
    fun teamInfoScreenViewModelFactory(): TeamInfoScreenViewModel.Factory
    fun addTaskToStudentScreenViewModelFactory(): AddTaskToStudentScreenViewModel.Factory
    fun teacherTaskScreenViewModelFactory(): TeacherTaskScreenViewModel.Factory
    fun createTeamScreenViewModelFactory(): CreateTeamScreenViewModel.Factory
    fun createLabViewModelFactory(): CreateLabViewModel.Factory
    fun teacherTasksScreenViewModelFactory(): TeacherTasksScreenViewModel.Factory

    fun adminWelcomeScreenViewModelFactory(): AdminWelcomeScreenViewModel.Factory
    fun adminDisciplinesScreenViewModelFactory(): AdminDisciplinesScreenViewModel.Factory
    fun adminGroupsTasksScreenViewModelFactory(): AdminGroupsTasksScreenViewModel.Factory
    fun adminAddNewDisciplineScreenViewModelFactory(): AddNewDisciplineScreenViewModel.Factory
    fun adminCreateNewTestScreenViewModelFactory(): CreateNewTestScreenViewModel.Factory
    fun groupsTeachersScreenViewModelFactory(): GroupsTeachersScreenViewModel.Factory
    fun createGroupScreenViewModelFactory(): CreateGroupScreenViewModel.Factory
    fun createTeacherScreenViewModelFactory(): CreateTeacherScreenViewModel.Factory
}