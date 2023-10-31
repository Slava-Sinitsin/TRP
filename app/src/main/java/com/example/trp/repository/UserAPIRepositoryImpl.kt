package com.example.trp.repository

import com.example.trp.data.disciplines.DisciplineResponse
import com.example.trp.data.disciplines.Disciplines
import com.example.trp.data.network.ApiService
import com.example.trp.data.network.UserAPI
import com.example.trp.data.tasks.TaskDesc
import com.example.trp.data.tasks.Tasks
import com.example.trp.data.user.AuthRequest
import com.example.trp.data.user.User
import retrofit2.Response

class UserAPIRepositoryImpl : UserAPI {
    override suspend fun login(authRequest: AuthRequest): Response<User> {
        return ApiService.userAPI.login(authRequest)
    }

    override suspend fun getDisciplines(token: String): Response<Disciplines> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(token: String, id: Int): Response<Tasks> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskDesc(token: String, id: Int): Response<TaskDesc> {
        TODO("Not yet implemented")
    }

    override suspend fun getDisciplineByID(token: String, id: Int): Response<DisciplineResponse> {
        TODO("Not yet implemented")
    }

}