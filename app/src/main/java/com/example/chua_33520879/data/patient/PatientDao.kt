package com.example.chua_33520879.data.patient

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert
    suspend fun insert(patient: Patient)

    @Update
    suspend fun update(patient: Patient)

    @Query("SELECT * FROM Patient WHERE userID = :userID LIMIT 1")
    fun getUser(userID: Int): Flow<Patient>

    @Query("SELECT phoneNumber FROM Patient WHERE userID = :userID LIMIT 1")
    suspend fun getPhoneNumber(userID: Int): String

    @Query("SELECT registred FROM Patient WHERE userID = :userID LIMIT 1")
    suspend fun getIsRegistered(userID: Int): Boolean

    @Query("SELECT password FROM Patient WHERE userID = :userID LIMIT 1")
    suspend fun getUserPassword(userID: Int): String

    @Query("UPDATE Patient SET registred = 1, password = :password, name = :name WHERE userID = :userID")
    suspend fun setAsRegistered(
        userID: Int,
        password: String,
        name: String
    )

    @Query("SELECT AVG(HEIFATotalScore) FROM Patient WHERE sex = :gender")
    fun getAverageScoreBasedOnGender(gender: String): Flow<Float>

    @Query("SELECT userID FROM Patient")
    fun getIDList(): Flow<List<Int>>

    @Query("SELECT * FROM Patient")
    suspend fun getAllData(): List<Patient>

    @Query("SELECT userID, FruitServeSize, FruitVariationsScore FROM Patient WHERE userID = :userID")
    suspend fun getDetailedFruitInfo(userID: Int):  PatientFruitData
}