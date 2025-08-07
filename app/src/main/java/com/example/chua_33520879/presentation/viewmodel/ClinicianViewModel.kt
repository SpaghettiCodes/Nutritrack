package com.example.chua_33520879.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chua_33520879.data.AuthManager
import com.example.chua_33520879.data.ai.AIRepository
import com.example.chua_33520879.data.patient.Patient
import com.example.chua_33520879.data.patient.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClinicianViewModel(context: Context): ViewModel() {
    private val patientRepo = PatientRepository(context)
    private val aiRepo = AIRepository(context)

    val femaleAvg = patientRepo.getFemaleAverage()
    val maleAvg = patientRepo.getMaleAverage()

    private val _aiState = MutableStateFlow<AiState>(AiState.Initial)
    val aiState = _aiState.asStateFlow()

    private val _loginModal = MutableStateFlow(false)
    val loginModal = _loginModal.asStateFlow()
    fun setLoginModal(value: Boolean) {
        _loginModal.value = value
    }

    private val _key = MutableStateFlow("")
    val key = _key.asStateFlow()
    fun updateKey(value: String) {
        _key.value = value
    }

    private val ADMIN_KEY = "dollar-entry-apples"
    fun validateLogin(): Boolean = _key.value == ADMIN_KEY

    fun analyzeData() {
        viewModelScope.launch {
            _aiState.value = AiState.Loading
            val listOfData = patientRepo.convertToCSV()
            val prompt = """
                 Below is a CSV data:
                 ${listOfData}
                 
                 The criteria of scoring is given below:
                 The HEIFA scoring system evaluates dietary quality across various components. For Discretionary foods, the max score is 10, awarded if males consume fewer than 3 serves and females fewer than 2.5 serves per day, while consuming 6+ (males) or 5.5+ (females) leads to zero. Vegetables score a max of 5 for consuming 6+ (males) or 5+ (females) serves with variety; consuming none scores zero. Fruits have a max of 5 for 2+ serves and 2+ varieties; zero is given for no fruit intake. Grains and cereals score up to 5 for 6+ serves and ≥50% wholegrains; no grains or wholegrains score zero. Wholegrains themselves also have a 5 max. Meat and alternatives can score 10 for 3+ (males) or 2.5+ (females) serves, but 0–0.5 serves score zero. Dairy and alternatives earn up to 10 for ≥2.5 serves, with none scoring zero. Sodium gets a max of 10 for ≤70 mmol/day (920 mg), while >100 mmol (3200 mg) gets zero. Alcohol has a max of 5 for ≤1.4 standard drinks/day, with more scoring zero. Water scores 5 if at least 50% of beverages are water; failing to meet 1.5L of fluids results in zero. Added sugars reach 10 if intake is <15% of energy, and zero if >20%. Saturated fat earns up to 5 if ≤10% of energy, and zero if ≥12%. Unsaturated fats (MUFA/PUFA) score 5 with 4 (males) or 2 (females) serves, and zero with <1 (males) or <0.5 (females) serves.
           
                 
                 Analyze the data and give 3 interesting patterns about the data, according to the criteria for scoring. For each analysis, explain within 50 - 75 words
                 Do not worry about the small dataset, and do not mention about the small dataset, just give the important patterns.
                 Do not do markdown formatting
                 Follow the following format for output
                 Example Output:
                 Title 1===Content of analysis
                 Title 2===Content of analysis
                 Title 3===Content of analysis
            """.trimIndent()
            try {
                val response = aiRepo.sendPrompt(prompt)
                response?.let { outputContent ->
                    Log.w("Analyze", outputContent)
                    val out = outputContent.split("\n").map { data -> data.split("===") }.filter { data -> data.size == 2 }.map { out -> Pair(out[0], out[1]) }
                    _aiState.value = AiState.Success(out)
                }
            } catch (e: Exception) {
                _aiState.value = AiState.Error(e.localizedMessage ?: "An error had occurred")
            }
        }
    }

    class factory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ClinicianViewModel(context) as T
    }
}

