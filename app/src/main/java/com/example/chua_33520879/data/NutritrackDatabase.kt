package com.example.chua_33520879.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.chua_33520879.data.ai.AIRecord
import com.example.chua_33520879.data.ai.AIRecordDao
import com.example.chua_33520879.data.ai.DateConverters
import com.example.chua_33520879.data.foodIntake.FoodIntake
import com.example.chua_33520879.data.foodIntake.FoodIntakeDao
import com.example.chua_33520879.data.foodIntake.StringListConverters
import com.example.chua_33520879.data.patient.Patient
import com.example.chua_33520879.data.patient.PatientDao
import com.example.chua_33520879.data.patient.csvProcessor

@Database(entities = [FoodIntake::class, Patient::class, AIRecord::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class, StringListConverters::class)
abstract class NutritrackDatabase: RoomDatabase() {
    abstract fun foodIntakeDao(): FoodIntakeDao

    abstract fun patientDao(): PatientDao

    abstract fun aiRecordDao(): AIRecordDao

    companion object {
        @Volatile
        private var Instance: NutritrackDatabase? = null

        // https://medium.com/android-news/pre-populate-room-database-6920f9acc870
        fun getDatabase(context: Context): NutritrackDatabase{
             return Instance ?: synchronized(this) {
                 Room.databaseBuilder(context, NutritrackDatabase::class.java, "nutritrack_database")
                     .addCallback(object : RoomDatabase.Callback() {
                         override fun onCreate(db: SupportSQLiteDatabase) {
                             super.onCreate(db)
                             // Prepopulate the Database
                             csvProcessor(context, "data.csv").populateDatabase()
                         }
                     })
                     .build()
                     .also { Instance = it }
             }
        }
    }
}