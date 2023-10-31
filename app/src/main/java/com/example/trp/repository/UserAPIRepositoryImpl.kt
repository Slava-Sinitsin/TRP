package com.example.trp.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.trp.data.datamanagers.UserDataManager
import com.example.trp.data.disciplines.DisciplineResponse
import com.example.trp.data.disciplines.Disciplines
import com.example.trp.data.network.ApiService
import com.example.trp.data.network.UserAPI
import com.example.trp.data.tasks.TaskDesc
import com.example.trp.data.tasks.Tasks
import com.example.trp.data.user.AuthRequest
import com.example.trp.data.user.User
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import retrofit2.Response

class UserAPIRepositoryImpl : UserAPI {
    var user by mutableStateOf(User())
    private var userChanged by mutableStateOf(true)

    override suspend fun getUserResponse(authRequest: AuthRequest): Response<User> {
        return ApiService.userAPI.getUserResponse(authRequest)
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

    suspend fun getUser(authRequest: AuthRequest): User {
        return if (userChanged) {
            val response = getUserResponse(authRequest)
            response.body()?.let {
                user = it
                UserDataManager.updateUser(
                    user.copy(
                        login = authRequest.username,
                        password = authRequest.password
                    )
                )
                userChanged = false
            } ?: run {
                response.errorBody()?.let { errorBody ->
                    user = user.copy(message = JSONObject(errorBody.string()).getString("error"))
                } ?: run {
                    user = user.copy(message = "Bad response")
                }
            }
            user
        } else {
            UserDataManager.getUser().first()
        }
    }
}