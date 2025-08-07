package com.example.chua_33520879.presentation.viewmodel

import android.content.Context
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

class PatientViewModel(context: Context): ViewModel() {
    private val patientRepo: PatientRepository = PatientRepository(context)

    private val _data = MutableStateFlow<Patient?>(null)
    val data = _data.asStateFlow()

    private val _optimalFruit = MutableStateFlow<Boolean>(false)
    val optimalFruit = _optimalFruit.asStateFlow()

    private val aiRepo = AIRepository(context)
    fun getAIHistory(userID: Int) = aiRepo.getHistory(userID)

    fun getUserData(userID: Int) = patientRepo.getDetails(userID)

    class PatientViewModelFactory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientViewModel(context) as T
    }

    fun isFruitScoreOptimal(userID: Int) = viewModelScope.launch {
        val data = patientRepo.getDetailedFruitData(userID)
        _optimalFruit.value = (data.FruitVariationsScore >= 2 && data.FruitServeSize >= 2)
    }
}
