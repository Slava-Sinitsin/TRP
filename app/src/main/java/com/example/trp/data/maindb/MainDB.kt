package com.example.trp.data.maindb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trp.data.mappers.disciplines.DisciplineData
import com.example.trp.data.mappers.user.User

@Database(
    entities = [User::class, DisciplineData::class],
    version = 1
)
abstract class MainDB : RoomDatabase() {
    abstract val userDAO: UserDAO
    abstract val disciplinesDAO: DisciplinesDAO
}