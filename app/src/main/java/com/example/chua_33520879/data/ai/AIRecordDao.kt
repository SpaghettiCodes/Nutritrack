package com.example.chua_33520879.data.ai

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AIRecordDao {
    @Insert
    abstract suspend fun insert(data: AIRecord)

    @Query("SELECT * FROM NutriCoachTips WHERE patientID = :userID ORDER BY createdAt DESC")
    fun getHistory(userID: Int): Flow<List<AIRecord>>
}

