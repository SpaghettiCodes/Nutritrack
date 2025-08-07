package com.example.chua_33520879.data.foodIntake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.chua_33520879.data.patient.Patient

@Entity(
    tableName = "FoodIntake",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = arrayOf("userID"),
            childColumns = arrayOf("patientID"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodIntake (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val eaten: List<String> = emptyList<String>(),
    val persona: String = "",
    val biggestMealTiming: String = "00:00",
    val sleepTiming: String = "00:00",
    val wakeUpTiming: String = "00:00",
    val patientID: Int,
)

class StringListConverters {
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun stringListToString(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }
}