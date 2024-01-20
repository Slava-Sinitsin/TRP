package com.example.trp.data.userdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trp.data.mappers.user.User

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActiveUser(user: User)

    @Query("SELECT * FROM User")
    suspend fun getAllUser(): List<User>

    @Query("SELECT * FROM User WHERE id LIKE :id")
    suspend fun getUser(id: Int): User

    @Query("SELECT * FROM User WHERE isActive LIKE 1")
    suspend fun getActiveUser(): User?

    @Query("UPDATE user SET isActive = 0")
    suspend fun setAllIsActiveFalse()
}