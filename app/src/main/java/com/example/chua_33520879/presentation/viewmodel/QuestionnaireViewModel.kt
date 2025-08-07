package com.example.chua_33520879.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.chua_33520879.data.foodIntake.FoodIntake
import com.example.chua_33520879.data.foodIntake.FoodIntakeRepository
import com.example.chua_33520879.data.foodIntake.ValidateFoodIntake
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionnaireViewModel(context: Context): ViewModel() {
    private val foodIntakeRepo: FoodIntakeRepository = FoodIntakeRepository(context)

    var _foodCategoryList = MutableStateFlow<List<String>>(emptyList())
    var foodCategoryList = _foodCategoryList.asStateFlow()
    fun removeFoodCategoryItem(item: String) {
        _foodCategoryList.value = _foodCategoryList.value.filter { it != item }
    }
    fun addFoodCategoryItem(item: String) {
        _foodCategoryList.value = _foodCategoryList.value + item
    }

    var _persona = MutableStateFlow("")
    var persona = _persona.asStateFlow()
    fun updatePersona(item: String) {
        _persona.value = item
    }

    var _biggestMealTiming = MutableStateFlow("")
    var biggestMealTiming = _biggestMealTiming.asStateFlow()
    fun updateBiggestMealTiming(item: String) {
        _biggestMealTiming.value = item
    }

    var _sleepTiming = MutableStateFlow("")
    var sleepTiming = _sleepTiming.asStateFlow()
    fun updateSleepTiming(item: String) {
        _sleepTiming.value = item
    }

    var _wakeUpTiming = MutableStateFlow("")
    var wakeUpTiming = _wakeUpTiming.asStateFlow()
    fun updateWakeUpTiming(item: String) {
        _wakeUpTiming.value = item
    }

    var _id: Int = 0
    var _exist: Boolean = false

    suspend fun checkIfExist(userID: Int): Boolean {
        var result = foodIntakeRepo.getFoodIntake(userID)
        return result != null
    }

    fun getUserData(userID: Int) = viewModelScope.launch {
        var result = foodIntakeRepo.getFoodIntake(userID)
        if (result != null) {
            _foodCategoryList.value = result.eaten
            _persona.value = result.persona
            _sleepTiming.value = result.sleepTiming
            _wakeUpTiming.value = result.wakeUpTiming
            _biggestMealTiming.value = result.biggestMealTiming
            _id = result.id
            _exist = true
        }
    }

    var _state = MutableStateFlow<UploadState>(UploadState.Initial)
    var state = _state.asStateFlow()
    fun resetState() {
        _state.value = UploadState.Initial
    }

    fun uploadFoodIntake(userID: Int) =
        viewModelScope.launch {
            val data = FoodIntake(
                id = _id,
                eaten = _foodCategoryList.value,
                persona = _persona.value,
                biggestMealTiming = _biggestMealTiming.value,
                sleepTiming = _sleepTiming.value,
                wakeUpTiming = _wakeUpTiming.value,
                patientID = userID
            )
            val validator = ValidateFoodIntake()
            validator.validate(data)
            if (!validator.ok) {
                _state.value = UploadState.Error(validator.msg)
                return@launch
            }

            if (_exist) {
                foodIntakeRepo.update(data)
            } else {
                foodIntakeRepo.insert(data)
            }
            _state.value = UploadState.Success
        }

    class QuestionnaireViewModelFactory(context: Context): ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuestionnaireViewModel(context) as T
    }
}

sealed interface UploadState{
    object Initial: UploadState
    object Success: UploadState
    data class Error(val msg: String): UploadState
}