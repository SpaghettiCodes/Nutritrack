package com.example.chua_33520879.data.patient

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Patient")
data class Patient (
    @PrimaryKey(autoGenerate = true)
    val userID: Int = 0,
    val phoneNumber: String = "",
    val password: String = "",
    val name: String = "",
    val sex: String = "",
    val registred: Boolean = false,
    val HEIFATotalScore: Float = 0f,
    val DiscretionaryHEIFAScore: Float = 0f,
    val VegetablesHEIFAScore: Float = 0f,
    val FruitHEIFAScore: Float = 0f,
    val FruitServeSize: Float = 0f,
    val FruitVariationsScore: Float = 0f,
    val GrainsAndCerealsHEIFAScore: Float = 0f,
    val WholeGrainsHEIFAScore: Float = 0f,
    val MeatAndAlternativesHEIFAScore: Float = 0f,
    val DairyAndAlternativesHEIFAScore: Float = 0f,
    val SodiumHEIFAScore: Float = 0f,
    val AlcoholHEIFAScore: Float = 0f,
    val WaterHEIFAScore: Float = 0f,
    val SugarHEIFAScore: Float = 0f,
    val SaturatedFatHEIFAScore: Float = 0f,
    val UnsaturatedFatHEIFAScore: Float = 0f,
)

fun Patient.toCsvRow(): String {
    return listOf(
        sex,
        HEIFATotalScore,
        DiscretionaryHEIFAScore,
        VegetablesHEIFAScore,
        FruitHEIFAScore,
        GrainsAndCerealsHEIFAScore,
        WholeGrainsHEIFAScore,
        MeatAndAlternativesHEIFAScore,
        DairyAndAlternativesHEIFAScore,
        SodiumHEIFAScore,
        AlcoholHEIFAScore,
        WaterHEIFAScore,
        SugarHEIFAScore,
        SaturatedFatHEIFAScore,
        UnsaturatedFatHEIFAScore
    ).joinToString(",")
}
