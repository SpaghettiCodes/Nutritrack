package com.example.chua_33520879.data.foodIntake

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodIntakeDao {
    @Insert
    suspend fun insert(foodIntake: FoodIntake)

    @Update
    suspend fun update(foodIntake: FoodIntake)

    @Query("SELECT * FROM FoodIntake WHERE patientID = :userID")
    suspend fun getFoodIntake(userID: Int): FoodIntake?
}