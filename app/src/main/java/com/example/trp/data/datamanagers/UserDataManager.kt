package com.example.trp.data.datamanagers

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.trp.data.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("StaticFieldLeak")
object UserDataManager {
    private val Context.userDataStore by dataStore("User.json", UserSerializer)

    private lateinit var context: Context

    fun initialize(context: Context) {
        UserDataManager.context = context
    }

    object UserSerializer : Serializer<User> {
        override val defaultValue: User
            get() = User()

        override suspend fun readFrom(input: InputStream): User {
            return try {
                Json.decodeFromString(
                    deserializer = User.serializer(),
                    string = input.readBytes().decodeToString()
                )
            } catch (e: SerializationException) {
                e.printStackTrace()
                User()
            }
        }

        override suspend fun writeTo(t: User, output: OutputStream) {
            withContext(Dispatchers.IO) {
                output.write(
                    Json.encodeToString(
                        serializer = User.serializer(),
                        value = t
                    ).encodeToByteArray()
                )
            }
        }
    }

    suspend fun getUser() = context.userDataStore.data.first()

    suspend fun updateUser(user: User) {
        context.userDataStore.updateData { user }
    }
}