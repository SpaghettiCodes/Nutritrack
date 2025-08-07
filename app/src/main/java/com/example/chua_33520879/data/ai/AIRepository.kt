package com.example.chua_33520879.data.ai

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import com.example.chua_33520879.BuildConfig
import com.example.chua_33520879.data.NutritrackDatabase
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow
import java.util.Date

class AIRepository(context: Context) {
    private val aiRecordDao = NutritrackDatabase.getDatabase(context).aiRecordDao()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    suspend fun sendImage(
        prompt: String,
        image: Bitmap
    ): String? {
        val prompt = content {
            image(image)
            text(prompt)
        }
        val response = generativeModel.generateContent(prompt)
        val text = response.text
        if (text == null) {
            return null
        }
        return text
    }

    suspend fun sendPrompt(
        prompt: String
    ): String? {
        val response = generativeModel.generateContent(prompt)
        val text = response.text
        if (text == null) {
            return null
        }
        return text
    }

    suspend fun savePrompt(
        msg: String,
        userID: Int,
    )  {
        aiRecordDao.insert(AIRecord(
            patientID = userID,
            message = msg,
            createdAt = Date()
        ))
    }

    fun getHistory(userID: Int): Flow<List<AIRecord>> = aiRecordDao.getHistory(userID)
}