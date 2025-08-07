package com.example.chua_33520879.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chua_33520879.data.ai.AIRepository
import com.example.chua_33520879.data.ai.FoodAnalysisResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class CalorieTrackerViewModel(context: Context): ViewModel() {
    private val _aiState = MutableStateFlow<AiState>(AiState.Initial)
    val aiState = _aiState.asStateFlow()

    private val aiRepo = AIRepository(context)

    fun analyzeImage(Image: Bitmap) = viewModelScope.launch {
        _aiState.value = AiState.Loading
        val prompt = """
            Analyze the above image for food or drinks, determine the name of the food and how many calories it has.
            List out 3-4 ingredients in the food, as well as 3-4 nutrition facts about this food.
            Then, recommend out of 10 if you would recommend someone eat this, give 3-4 reasons why.
            Do not include anything else
            Do not format as json
            Do not format as anything
            Ensure calories is a float by including a .0 at the end if it is a integer.
            Format your response exactly as shown below,            
            {
               name: "Name of food",
               calories: 0.1,
               ingredients: ["one", "two", "three"],
               nutrition: ["one", "two", "three"],
               recommend: 0,
               reasons: ["one", "two", "three"]
            }
            If you are unable to analyze the image, just return an error with the reason, like below:
            Error: error reason
        """.trimIndent()

        try {
            val response = aiRepo.sendImage(prompt, Image)
            response?.let { outputContent ->
                Log.w("AI Out", outputContent)
                outputContent.trim('`') // just in case it decide to return in markdown format
                if (outputContent.startsWith("Error")) {
                    _aiState.value = AiState.Error(outputContent)
                    return@launch
                }
                val data = Gson().fromJson<FoodAnalysisResult>(outputContent, FoodAnalysisResult::class.java)
                _aiState.value = AiState.Success(data)
            }
        } catch (e: Exception) {
            _aiState.value = AiState.Error(e.localizedMessage ?: "An error had occurred")
        }

    }

    class factory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CalorieTrackerViewModel(context) as T
    }
}