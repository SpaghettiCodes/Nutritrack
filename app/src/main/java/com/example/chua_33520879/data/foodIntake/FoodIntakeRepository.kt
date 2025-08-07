package com.example.chua_33520879.data.foodIntake

import android.content.Context
import com.example.chua_33520879.data.NutritrackDatabase

class FoodIntakeRepository(context: Context) {
    private val foodIntakeDao = NutritrackDatabase.getDatabase(context).foodIntakeDao()

    suspend fun insert(foodIntake: FoodIntake) {
        foodIntakeDao.insert(foodIntake)
    }

    suspend fun update(foodIntake: FoodIntake) {
        foodIntakeDao.update(foodIntake)
    }

    suspend fun getFoodIntake(userID: Int): FoodIntake? {
        return foodIntakeDao.getFoodIntake(userID)
    }
}