package com.example.trp.data.userdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trp.data.mappers.user.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class UserDB : RoomDatabase() {
    abstract val dao: UserDAO
}