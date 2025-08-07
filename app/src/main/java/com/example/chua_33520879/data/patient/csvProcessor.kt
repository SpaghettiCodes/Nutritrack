package com.example.chua_33520879.data.patient

import android.content.Context
import android.util.Log
import com.example.chua_33520879.data.NutritrackDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class csvProcessor(val context: Context, val filename: String) {
    val patientDao = NutritrackDatabase.getDatabase(context).patientDao()

     fun populateDatabase() {
         Log.w("Database", "Importing data from csv...")
        var assets = context.assets

        try {
            val inputStream = assets.open(filename)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val header = reader.readLine().split(",")
            reader.lineSequence().forEach { line ->
                var userRawData = line.split(",")
                val get = getData(userRawData, header)
                val gender = get("Sex")
                var userData = Patient(
                    userID = get("User_ID").toInt(),
                    phoneNumber = get("PhoneNumber"),
                    sex = get("Sex"),
                    HEIFATotalScore = get("HEIFAtotalscore$gender").toFloat(),
                    DiscretionaryHEIFAScore = get("DiscretionaryHEIFAscore$gender").toFloat(),
                    VegetablesHEIFAScore = get("VegetablesHEIFAscore$gender").toFloat(),
                    FruitHEIFAScore = get("FruitHEIFAscore$gender").toFloat(),
                    FruitServeSize = get("Fruitservesize").toFloat(),
                    FruitVariationsScore = get("Fruitvariationsscore").toFloat(),
                    GrainsAndCerealsHEIFAScore = get("GrainsandcerealsHEIFAscore$gender").toFloat(),
                    WholeGrainsHEIFAScore = get("WholegrainsHEIFAscore$gender").toFloat(),
                    MeatAndAlternativesHEIFAScore = get("MeatandalternativesHEIFAscore$gender").toFloat(),
                    DairyAndAlternativesHEIFAScore = get("DairyandalternativesHEIFAscore$gender").toFloat(),
                    SodiumHEIFAScore = get("SodiumHEIFAscore$gender").toFloat(),
                    AlcoholHEIFAScore = get("AlcoholHEIFAscore$gender").toFloat(),
                    WaterHEIFAScore = get("WaterHEIFAscore$gender").toFloat(),
                    SugarHEIFAScore = get("SugarHEIFAscore$gender").toFloat(),
                    SaturatedFatHEIFAScore = get("SaturatedFatHEIFAscore$gender").toFloat(),
                    UnsaturatedFatHEIFAScore = get("UnsaturatedFatHEIFAscore$gender").toFloat(),
                )
                CoroutineScope(Dispatchers.IO).launch {
                    patientDao.insert(userData)
                }
            }
        } catch (e: Exception) {
            Log.w("CSV Reading Error", ":(")
        }
         Log.w("Database", "Database populated")
    }

    fun getData(userData: List<String>, header: List<String>): (String) -> String = { userData[header.indexOf(it)] }
}