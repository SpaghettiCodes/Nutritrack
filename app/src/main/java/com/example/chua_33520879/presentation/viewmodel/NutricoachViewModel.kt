package com.example.chua_33520879.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chua_33520879.data.ai.AIRepository
import com.example.chua_33520879.data.fruityvice.FruitResponseModel
import com.example.chua_33520879.data.fruityvice.FruityViceRepository
import com.example.chua_33520879.data.patient.PatientRepository
import com.example.chua_33520879.data.patient.toCsvRow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import kotlin.random.Random

class NutricoachViewModel(context: Context): ViewModel() {
    private val _aiState = MutableStateFlow<AiState>(AiState.Initial)
    val aiState = _aiState.asStateFlow()

    private val aiRepo = AIRepository(context)
    private val patientRepo = PatientRepository(context)

    private val _showAllModal = MutableStateFlow(false)
    val showAllModal = _showAllModal.asStateFlow()
    fun setModal(value: Boolean) {
        _showAllModal.value = value
    }

    fun generateMotivationalMessage(userID: Int) {
        val header = "sex,HEIFATotalScore,DiscretionaryHEIFAScore,VegetablesHEIFAScore,FruitHEIFAScore,GrainsAndCerealsHEIFAScore,WholeGrainsHEIFAScore,MeatAndAlternativesHEIFAScore,DairyAndAlternativesHEIFAScore,SodiumHEIFAScore,AlcoholHEIFAScore,WaterHEIFAScore,SugarHEIFAScore,SaturatedFatHEIFAScore,UnsaturatedFatHEIFAScore\n"
        val maxValue = "100,10,5,5,5,5,10,10,10,5,5,10,5,5\n"
        viewModelScope.launch {
            _aiState.value = AiState.Loading
            val userData = patientRepo.getDetails(userID).first()
            val prompt = """
            Given the following user's CSV data:
            ${header + userData.toCsvRow()}
            and the following max values for each of the attributes:
            ${header + maxValue}
            
            The criteria of scoring is given below:
            The HEIFA scoring system evaluates dietary quality across various components. For Discretionary foods, the max score is 10, awarded if males consume fewer than 3 serves and females fewer than 2.5 serves per day, while consuming 6+ (males) or 5.5+ (females) leads to zero. Vegetables score a max of 5 for consuming 6+ (males) or 5+ (females) serves with variety; consuming none scores zero. Fruits have a max of 5 for 2+ serves and 2+ varieties; zero is given for no fruit intake. Grains and cereals score up to 5 for 6+ serves and ≥50% wholegrains; no grains or wholegrains score zero. Wholegrains themselves also have a 5 max. Meat and alternatives can score 10 for 3+ (males) or 2.5+ (females) serves, but 0–0.5 serves score zero. Dairy and alternatives earn up to 10 for ≥2.5 serves, with none scoring zero. Sodium gets a max of 10 for ≤70 mmol/day (920 mg), while >100 mmol (3200 mg) gets zero. Alcohol has a max of 5 for ≤1.4 standard drinks/day, with more scoring zero. Water scores 5 if at least 50% of beverages are water; failing to meet 1.5L of fluids results in zero. Added sugars reach 10 if intake is <15% of energy, and zero if >20%. Saturated fat earns up to 5 if ≤10% of energy, and zero if ≥12%. Unsaturated fats (MUFA/PUFA) score 5 with 4 (males) or 2 (females) serves, and zero with <1 (males) or <0.5 (females) serves.
            
            Determine which score is low, and generate a motivational message for every low scoring HEIFA Score, around 50 - 75 words
            Separate each motivational message with a newline
            Only output the motivational message
            Include emojis in the motivational message
            Do not specify which attribute is the motivational message for
        """.trimIndent()

            try {
                val response = aiRepo.sendPrompt(prompt)
                response?.let { outputContent ->
                    val splittedValue = outputContent.split("\n").filter { line -> line.isNotEmpty() }
                    val msg = splittedValue[Random.nextInt(0, splittedValue.size)]
                    aiRepo.savePrompt(msg, userID)
                    _aiState.value = AiState.Success(msg)
                }
            } catch (e: Exception) {
                _aiState.value = AiState.Error(e.localizedMessage ?: "An error had occurred")
            }
        }
    }

    private val _apiState = MutableStateFlow<ApiState>(ApiState.Initial)
    val apiState = _apiState.asStateFlow()

    private val apiRepo = FruityViceRepository()

    private val _fruit = MutableStateFlow("")
    val fruit = _fruit.asStateFlow()
    fun setFruit(fruit: String) {
        _fruit.value = fruit
    }

    fun getFruitDetails() = viewModelScope.launch {
        _apiState.value = ApiState.Loading
        if (_fruit.value.isEmpty()) {
            _apiState.value = ApiState.Error("Please input a fruit!")
            return@launch
        }
        try {
            _apiState.value = ApiState.Success(apiRepo.getSpecificFruit(_fruit.value))
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val code = throwable.code()
                    var msg = ""
                    when (code) {
                        404 -> msg = "Fruit Not Found"
                    }
                    _apiState.value = ApiState.Error("Error $code: $msg")
                }
                else -> {
                    _apiState.value = ApiState.Error("Something Horrible Went Wrong :(")
                }
            }
        }

    }

    class factory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NutricoachViewModel(context) as T
    }
}

sealed interface AiState {
    object Initial: AiState
    object Loading: AiState
    data class Success<T>(val outputText: T): AiState
    data class Error(val errorMessage: String): AiState
}

sealed interface ApiState {
    object Initial: ApiState
    object Loading: ApiState
    data class Success(val output: FruitResponseModel): ApiState
    data class Error(val errorMessage: String): ApiState
}