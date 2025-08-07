package com.example.chua_33520879.data.ai

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.chua_33520879.data.patient.Patient
import java.util.Date

@Entity(
    tableName = "NutriCoachTips",
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
data class AIRecord (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val createdAt: Date,
    val patientID: Int,
    val message: String
)

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}