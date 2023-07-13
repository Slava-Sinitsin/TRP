package com.example.trp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trp.data.JWTDecoder
import com.example.trp.data.User
import com.example.trp.data.UserDataManager
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class WelcomeScreenViewModel : ViewModel() {
    var user by mutableStateOf(User())
        private set
    var selectedItem by mutableStateOf(0)
        private set

    init {
        viewModelScope.launch {
            UserDataManager.getUser().collect {
                user = it
                addUserInformation()
            }
        }
    }

    fun selectItem(newSelectedItem: Int) {
        selectedItem = newSelectedItem
    }

    private suspend fun addUserInformation() {
        val updatedUser = parseToken(user.token.toString())
        UserDataManager.saveUser(updatedUser)
    }

    private fun parseToken(token: String): User {
        val decodedToken = JWTDecoder().decodeToken(token)
        val tempUser = Json.decodeFromString<User>(decodedToken.toString())
        return user.copy(
            sub = tempUser.sub,
            id = tempUser.id,
            username = tempUser.username,
            fullName = tempUser.fullName,
            role = tempUser.role,
            iat = tempUser.iat,
            iss = tempUser.iss,
            exp = tempUser.exp
        )
    }
}