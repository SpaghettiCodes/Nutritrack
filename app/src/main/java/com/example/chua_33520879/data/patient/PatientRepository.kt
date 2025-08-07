package com.example.chua_33520879.data.patient

import android.content.Context
import com.example.chua_33520879.data.NutritrackDatabase
import kotlinx.coroutines.flow.Flow

class PatientRepository(context: Context) {
    private val patientDao = NutritrackDatabase.getDatabase(context).patientDao()

    fun getIDList() = patientDao.getIDList()

    suspend fun insert(patient: Patient) {
        patientDao.insert(patient)
    }

    suspend fun update(patient: Patient) {
        patientDao.update(patient)
    }

    suspend fun getPhoneNumber(userID: Int) = patientDao.getPhoneNumber(userID)

    fun getDetails(userID: Int): Flow<Patient> = patientDao.getUser(userID)

    suspend fun checkPassword(userID: Int, attempt: String): Boolean {
        var password = patientDao.getUserPassword(userID)
        return checkInput(attempt, password)
    }

    suspend fun setAsRegistered(userID: Int, password: String, name: String) {
        patientDao.setAsRegistered(userID, hashPassword(password), name)
    }

    suspend fun isRegistered(userID: Int): Boolean {
        return patientDao.getIsRegistered(userID)
    }

    fun getMaleAverage(): Flow<Float> = patientDao.getAverageScoreBasedOnGender("Male")

    fun getFemaleAverage(): Flow<Float> = patientDao.getAverageScoreBasedOnGender("Female")

    suspend fun convertToCSV(): String {
        val value = patientDao.getAllData()
        val header = "sex,HEIFATotalScore,DiscretionaryHEIFAScore,VegetablesHEIFAScore,FruitHEIFAScore,GrainsAndCerealsHEIFAScore,WholeGrainsHEIFAScore,MeatAndAlternativesHEIFAScore,DairyAndAlternativesHEIFAScore,SodiumHEIFAScore,AlcoholHEIFAScore,WaterHEIFAScore,SugarHEIFAScore,SaturatedFatHEIFAScore,UnsaturatedFatHEIFAScore\n"
        return header + value.joinToString("\n") { data -> data.toCsvRow() }
    }

    suspend fun getDetailedFruitData(userID: Int) = patientDao.getDetailedFruitInfo(userID)
}