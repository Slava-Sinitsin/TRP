package com.example.trp.data.maindb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trp.data.mappers.disciplines.DisciplineData

@Dao
interface DisciplinesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscipline(disciplineData: DisciplineData)

    @Query("SELECT * FROM DisciplineData")
    suspend fun getDisciplines(): List<DisciplineData>?
}